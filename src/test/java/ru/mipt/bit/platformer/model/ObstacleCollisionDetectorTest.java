package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObstacleCollisionDetectorTest {

    @Test
    void cellWithAnObstacleIsOccupied() {
        List<GameObject> obstacles = new ArrayList<GameObject>();
        obstacles.add(new Tree(new GridPoint2(1, 3)));
        obstacles.add(new Tree(new GridPoint2(4, 2)));
        ObstacleCollisionDetector detector = new ObstacleCollisionDetector(obstacles);

        assertTrue(detector.isOccupied(new GridPoint2(1, 3)));
        assertTrue(detector.isOccupied(new GridPoint2(4, 2)));
        assertFalse(detector.isOccupied(new GridPoint2(1, 1)));
    }

    @Test
    void emptyFieldIsFree() {
        ObstacleCollisionDetector detector = new ObstacleCollisionDetector(Collections.<GameObject>emptyList());

        assertFalse(detector.isOccupied(new GridPoint2(0, 0)));
    }

    @Test
    void followsObstaclesAddedToTheSameCollection() {
        List<GameObject> obstacles = new ArrayList<GameObject>();
        ObstacleCollisionDetector detector = new ObstacleCollisionDetector(obstacles);

        assertFalse(detector.isOccupied(new GridPoint2(2, 2)));

        obstacles.add(new Tree(new GridPoint2(2, 2)));

        assertTrue(detector.isOccupied(new GridPoint2(2, 2)));
    }

    @Test
    void occupiesCellsOfAnyGameObject() {
        List<GameObject> occupants = new ArrayList<GameObject>();
        occupants.add(new GameObject() {
            @Override
            public GridPoint2 getCoordinates() {
                return new GridPoint2(3, 1);
            }
        });
        ObstacleCollisionDetector detector = new ObstacleCollisionDetector(occupants);

        assertTrue(detector.isOccupied(new GridPoint2(3, 1)));
        assertFalse(detector.isOccupied(new GridPoint2(1, 3)));
    }
}
