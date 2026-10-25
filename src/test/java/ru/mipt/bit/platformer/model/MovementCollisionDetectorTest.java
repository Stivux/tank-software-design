package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovementCollisionDetectorTest {

    @Test
    void outsideTheMapIsOccupied() {
        MovementCollisionDetector detector = detector(4, 3, Collections.<GameObject>emptyList(), Collections.<Movable>emptyList());

        assertTrue(detector.isOccupied(new GridPoint2(-1, 0)));
        assertTrue(detector.isOccupied(new GridPoint2(0, -1)));
        assertTrue(detector.isOccupied(new GridPoint2(4, 0)));
        assertTrue(detector.isOccupied(new GridPoint2(0, 3)));
        assertFalse(detector.isOccupied(new GridPoint2(0, 0)));
        assertFalse(detector.isOccupied(new GridPoint2(3, 2)));
    }

    @Test
    void obstacleCellIsOccupied() {
        List<GameObject> obstacles = new ArrayList<GameObject>();
        obstacles.add(new Tree(new GridPoint2(1, 1)));
        MovementCollisionDetector detector = detector(4, 3, obstacles, Collections.<Movable>emptyList());

        assertTrue(detector.isOccupied(new GridPoint2(1, 1)));
        assertFalse(detector.isOccupied(new GridPoint2(2, 1)));
    }

    @Test
    void movingTankOccupiesItsOriginAndItsDestination() {
        List<Movable> tanks = new ArrayList<Movable>();
        MovementCollisionDetector detector = detector(5, 5, Collections.<GameObject>emptyList(), tanks);
        Tank tank = new Tank(new GridPoint2(2, 2), 1f, detector);
        tanks.add(tank);

        assertTrue(detector.isOccupied(new GridPoint2(2, 2)));
        assertFalse(detector.isOccupied(new GridPoint2(3, 2)));

        tank.tryMove(Direction.RIGHT);

        assertTrue(detector.isOccupied(new GridPoint2(2, 2)));
        assertTrue(detector.isOccupied(new GridPoint2(3, 2)));
        assertFalse(detector.isOccupied(new GridPoint2(2, 3)));
    }

    @Test
    void seesTanksAddedToTheSameCollection() {
        List<Movable> tanks = new ArrayList<Movable>();
        MovementCollisionDetector detector = detector(4, 4, Collections.<GameObject>emptyList(), tanks);

        assertFalse(detector.isOccupied(new GridPoint2(1, 1)));

        tanks.add(new Tank(new GridPoint2(1, 1), 1f, detector));

        assertTrue(detector.isOccupied(new GridPoint2(1, 1)));
    }

    private static MovementCollisionDetector detector(int width, int height, List<GameObject> obstacles,
                                                      List<Movable> tanks) {
        return new MovementCollisionDetector(width, height, new ObstacleCollisionDetector(obstacles), tanks);
    }
}
