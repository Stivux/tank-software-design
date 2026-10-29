package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevelTest {

    @Test
    void listenerHearsAddsAndRemovalsAndNotTheInitialObstacles() {
        Tree tree = new Tree(new GridPoint2(0, 0));
        Level level = new Level(3, 3, new GridPoint2(1, 1), Collections.singletonList(tree));
        List<GameObject> added = new ArrayList<GameObject>();
        List<GameObject> removed = new ArrayList<GameObject>();
        level.addListener(new LevelListener() {
            @Override
            public void onAdded(GameObject object) {
                added.add(object);
            }

            @Override
            public void onRemoved(GameObject object) {
                removed.add(object);
            }
        });
        assertEquals(0, added.size());

        level.add(tree);
        Tank tank = new Tank(new GridPoint2(1, 1), 1f, free(), level);
        level.add(tank);
        level.remove(tree);
        level.remove(tree);

        assertEquals(List.of(tree, tank), added);
        assertEquals(List.of(tree), removed);
        assertEquals(List.of(tank), level.getObjects());
    }

    @Test
    void updateTicksTanksAndSkipsOnesAlreadyRemoved() {
        Level level = new Level(4, 3, new GridPoint2(0, 0), Collections.<GameObject>emptyList());
        Tank moving = new Tank(new GridPoint2(1, 1), 1f, free(), level);
        Tank removed = new Tank(new GridPoint2(3, 1), 1f, free(), level);
        level.add(moving);
        level.add(removed);
        moving.tryMove(Direction.RIGHT);
        removed.tryMove(Direction.UP);

        level.remove(removed);
        level.update(1f);

        assertEquals(new GridPoint2(2, 1), moving.getCoordinates());
        assertEquals(new GridPoint2(3, 1), removed.getCoordinates());
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
