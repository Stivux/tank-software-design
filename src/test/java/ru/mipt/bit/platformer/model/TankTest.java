package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TankTest {
    private static final float SPEED = 1f;

    private Tank tank;

    @BeforeEach
    void setUp() {
        tank = new Tank(new GridPoint2(1, 1), SPEED, free());
    }

    @Test
    void startsAtGivenCellFacingRight() {
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress(), 0f);
        assertEquals(Direction.RIGHT.getRotation(), tank.getRotation(), 0f);
    }

    @Test
    void tryMoveAimsAtTheNextCell() {
        tank.tryMove(Direction.UP);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress(), 0f);
        assertEquals(Direction.UP.getRotation(), tank.getRotation(), 0f);
    }

    @Test
    void updateAdvancesProgressAndSnapsOntoTheDestination() {
        tank.tryMove(Direction.RIGHT);

        tank.update(0.5f);
        assertEquals(0.5f, tank.getMovementProgress(), 0.0001f);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());

        tank.update(0.5f);
        assertEquals(1f, tank.getMovementProgress(), 0.0001f);
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    void largeDeltaCompletesMovementWithinOneUpdate() {
        tank.tryMove(Direction.DOWN);

        tank.update(5f);

        assertEquals(1f, tank.getMovementProgress(), 0f);
        assertEquals(new GridPoint2(1, 0), tank.getCoordinates());
    }

    @Test
    void ignoresFurtherMovesUntilTheCurrentOneFinishes() {
        tank.tryMove(Direction.UP);
        tank.tryMove(Direction.LEFT);

        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP.getRotation(), tank.getRotation(), 0f);
    }

    @Test
    void canMoveAgainAfterArrival() {
        tank.tryMove(Direction.RIGHT);
        tank.update(1f);

        tank.tryMove(Direction.RIGHT);

        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(3, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress(), 0f);
    }

    @Test
    void blockedMoveKeepsTheCellButTurnsTheTank() {
        tank = new Tank(new GridPoint2(1, 1), SPEED, occupied(new GridPoint2(1, 2)));

        tank.tryMove(Direction.UP);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress(), 0f);
        assertEquals(Direction.UP.getRotation(), tank.getRotation(), 0f);
    }

    @Test
    void doesNotEnterAnObstacleCell() {
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        obstacles.add(new Tree(new GridPoint2(1, 2)));
        tank = new Tank(new GridPoint2(1, 1), SPEED, new ObstacleCollisionDetector(obstacles));

        tank.tryMove(Direction.UP);
        tank.update(1f);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    private static CollisionDetector free() {
        return new CollisionDetector() {
            @Override
            public boolean isOccupied(GridPoint2 position) {
                return false;
            }
        };
    }

    private static CollisionDetector occupied(final GridPoint2 cell) {
        return new CollisionDetector() {
            @Override
            public boolean isOccupied(GridPoint2 position) {
                return cell.equals(position);
            }
        };
    }
}
