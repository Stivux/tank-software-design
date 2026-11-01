package ru.mipt.bit.platformer;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.controller.KeyboardHandler;
import ru.mipt.bit.platformer.controller.TankControls;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.GameObjectVisitor;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.MovementCollisionDetector;
import ru.mipt.bit.platformer.model.ObstacleCollisionDetector;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.renderer.Renderer;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTankTest {
    private final Set<Integer> pressed = new HashSet<Integer>();

    @Test
    void playerMovesAndShootsThroughGameLoop() {
        Level level = new Level(6, 4, new GridPoint2(1, 1), Collections.<GameObject>emptyList());
        CollisionDetector collisions = new MovementCollisionDetector(
                6, 4, new ObstacleCollisionDetector(level.getObstacles()), level.getObjects());
        Tank player = new Tank(new GridPoint2(1, 1), 1f, collisions, level);
        KeyboardHandler keyboard = new KeyboardHandler(pressed::contains);
        TankControls.bindMovement(keyboard, player);
        TankControls.bindShooting(keyboard, player);
        level.add(player);

        GameLoop loop = new GameLoop(new Renderer() {
            @Override
            public void render() {
            }

            @Override
            public void dispose() {
            }
        }, List.of(keyboard), List.of(level));

        pressed.add(Input.Keys.RIGHT);
        loop.tick(0f);
        loop.tick(1f);
        pressed.remove(Input.Keys.RIGHT);
        assertEquals(new GridPoint2(2, 1), player.getCoordinates());

        pressed.add(Input.Keys.SPACE);
        loop.tick(0f);
        pressed.remove(Input.Keys.SPACE);
        assertEquals(2, level.getObjects().size());
        assertEquals(new GridPoint2(3, 1), level.getObjects().get(1).getCoordinates());
    }

    @Test
    void movableObjectsAreDispatchedAsMovable() {
        Level level = new Level(6, 4, new GridPoint2(1, 1), Collections.<GameObject>emptyList());
        Object tankKind = new Tank(new GridPoint2(1, 1), 1f, position -> false, level)
                .accept(kindVisitor());
        Object treeKind = new Tree(new GridPoint2(2, 2)).accept(kindVisitor());
        Object bulletKind = new Bullet(new GridPoint2(1, 1), Direction.RIGHT, 1f, 1, level)
                .accept(kindVisitor());

        assertEquals("movable", tankKind);
        assertEquals("gameObject", treeKind);
        assertEquals("movable", bulletKind);
    }

    private static GameObjectVisitor<String> kindVisitor() {
        return new GameObjectVisitor<String>() {
            @Override
            public String visitGameObject(GameObject object) {
                return "gameObject";
            }

            @Override
            public String visitMovable(Movable movable) {
                return "movable";
            }
        };
    }
}
