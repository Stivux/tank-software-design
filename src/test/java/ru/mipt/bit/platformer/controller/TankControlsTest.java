package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.ObstacleCollisionDetector;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TankControlsTest {
    private FakeKeyInput input;
    private KeyboardHandler keyboard;
    private Tank tank;
    private List<String> extraActions;

    @BeforeEach
    void setUp() {
        input = new FakeKeyInput();
        keyboard = new KeyboardHandler(input);
        tank = new Tank(new GridPoint2(1, 1), 1f, free());
        extraActions = new ArrayList<String>();
        TankControls.bindMovement(keyboard, tank);
        keyboard.addHandler(new Action() {
            @Override
            public void execute() {
                extraActions.add("shoot");
            }
        }, SPACE);
    }

    @Test
    void arrowsAndWasdStartMovement() {
        pressAndUpdate(W);
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());

        arrive();
        pressAndUpdate(LEFT);
        assertEquals(new GridPoint2(0, 2), tank.getDestinationCoordinates());

        arrive();
        pressAndUpdate(S);
        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());

        arrive();
        pressAndUpdate(D);
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
    }

    @Test
    void arrowKeysAreBoundAsWell() {
        pressAndUpdate(UP);
        assertEquals(Direction.UP.getRotation(), tank.getRotation(), 0f);

        arrive();
        pressAndUpdate(A);
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation(), 0f);

        arrive();
        pressAndUpdate(DOWN);
        assertEquals(Direction.DOWN.getRotation(), tank.getRotation(), 0f);

        arrive();
        pressAndUpdate(RIGHT);
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation(), 0f);
    }

    @Test
    void spaceSpawnsABulletAheadOfTheTank() {
        Level level = new Level(5, 5, new GridPoint2(1, 1), Collections.<GameObject>emptyList());
        Tank shooter = new Tank(new GridPoint2(1, 1), 1f, free(), level);
        KeyboardHandler keys = new KeyboardHandler(input);
        TankControls.bindShooting(keys, shooter);

        input.press(SPACE);
        keys.update();

        List<Bullet> bullets = new ArrayList<Bullet>();
        for (GameObject object : level.getObjects()) {
            if (object instanceof Bullet) {
                bullets.add((Bullet) object);
            }
        }
        assertEquals(1, bullets.size());
        assertEquals(new GridPoint2(2, 1), bullets.get(0).getCoordinates());
    }

    @Test
    void additionalButtonHandlerDoesNotDisturbMovement() {
        input.press(SPACE);
        input.press(RIGHT);
        keyboard.update();

        assertEquals(List.of("shoot"), extraActions);
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    void earlierDirectionWinsWhenSeveralMovementKeysAreHeld() {
        input.press(UP);
        input.press(RIGHT);
        keyboard.update();

        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation(), 0f);
    }

    @Test
    void laterFreeDirectionAppliesWhenTheEarlierOneIsBlocked() {
        List<GameObject> obstacles = new ArrayList<GameObject>();
        obstacles.add(new Tree(new GridPoint2(1, 2)));
        tank = new Tank(new GridPoint2(1, 1), 1f, new ObstacleCollisionDetector(obstacles));
        keyboard = new KeyboardHandler(input);
        TankControls.bindMovement(keyboard, tank);

        input.press(UP);
        input.press(RIGHT);
        keyboard.update();

        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation(), 0f);
        assertEquals(0f, tank.getMovementProgress(), 0f);
    }

    private void pressAndUpdate(int key) {
        input.press(key);
        keyboard.update();
        input.release(key);
    }

    private void arrive() {
        tank.update(1f);
    }

    private static CollisionDetector free() {
        return new CollisionDetector() {
            @Override
            public boolean isOccupied(GridPoint2 position) {
                return false;
            }
        };
    }
}
