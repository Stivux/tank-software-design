package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
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
    void stopsAtTheMapBorderTheSameWayAsAtAnObstacle() {
        List<Movable> tanks = new ArrayList<Movable>();
        tank = new Tank(new GridPoint2(0, 1), SPEED, field(3, 3, Collections.<GameObject>emptyList(), tanks));
        tanks.add(tank);

        tank.tryMove(Direction.LEFT);

        assertEquals(new GridPoint2(0, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress(), 0f);
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation(), 0f);
    }

    @Test
    void anotherTankCannotEnterEitherCellOfAMovingTank() {
        List<Movable> tanks = new ArrayList<Movable>();
        CollisionDetector collisions = field(5, 5, Collections.<GameObject>emptyList(), tanks);
        Tank moving = new Tank(new GridPoint2(2, 2), SPEED, collisions);
        Tank ontoDestination = new Tank(new GridPoint2(2, 4), SPEED, collisions);
        Tank ontoOrigin = new Tank(new GridPoint2(1, 2), SPEED, collisions);
        tanks.add(moving);
        tanks.add(ontoDestination);
        tanks.add(ontoOrigin);

        moving.tryMove(Direction.UP);
        ontoDestination.tryMove(Direction.DOWN);
        ontoOrigin.tryMove(Direction.RIGHT);

        assertEquals(new GridPoint2(2, 3), moving.getDestinationCoordinates());
        assertEquals(new GridPoint2(2, 4), ontoDestination.getDestinationCoordinates());
        assertEquals(new GridPoint2(1, 2), ontoOrigin.getDestinationCoordinates());
        assertEquals(1f, ontoDestination.getMovementProgress(), 0f);
        assertEquals(1f, ontoOrigin.getMovementProgress(), 0f);
    }

    @Test
    void freesTheOriginCellAfterTheTankArrives() {
        List<Movable> tanks = new ArrayList<Movable>();
        CollisionDetector collisions = field(5, 5, Collections.<GameObject>emptyList(), tanks);
        Tank moving = new Tank(new GridPoint2(2, 2), SPEED, collisions);
        Tank follower = new Tank(new GridPoint2(2, 1), SPEED, collisions);
        tanks.add(moving);
        tanks.add(follower);

        moving.tryMove(Direction.RIGHT);
        moving.update(1f);
        follower.tryMove(Direction.UP);

        assertEquals(new GridPoint2(3, 2), moving.getCoordinates());
        assertEquals(new GridPoint2(2, 2), follower.getDestinationCoordinates());
    }

    @Test
    void doesNotEnterAnObstacleCell() {
        List<GameObject> obstacles = new ArrayList<GameObject>();
        obstacles.add(new Tree(new GridPoint2(1, 2)));
        tank = new Tank(new GridPoint2(1, 1), SPEED, new ObstacleCollisionDetector(obstacles));

        tank.tryMove(Direction.UP);
        tank.update(1f);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    private static CollisionDetector field(int width, int height, List<GameObject> obstacles, List<Movable> tanks) {
        return new MovementCollisionDetector(width, height, new ObstacleCollisionDetector(obstacles), tanks);
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
