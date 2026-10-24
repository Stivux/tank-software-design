package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AiTankControllerTest {

    @Test
    void movesTheTankInTheDirectionChosenByTheStrategy() {
        Tank tank = new Tank(new GridPoint2(1, 1), 1f, free());
        AiTankController ai = new AiTankController(tank, new MoveStrategy() {
            @Override
            public Direction chooseDirection() {
                return Direction.LEFT;
            }
        });

        ai.update();

        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress(), 0f);
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation(), 0f);
    }

    @Test
    void choosesAgainAfterTheTankArrives() {
        Tank tank = new Tank(new GridPoint2(1, 1), 1f, free());
        List<Direction> directions = new ArrayList<Direction>();
        directions.add(Direction.RIGHT);
        directions.add(Direction.UP);
        AiTankController ai = new AiTankController(tank, new MoveStrategy() {
            @Override
            public Direction chooseDirection() {
                return directions.remove(0);
            }
        });

        ai.update();
        tank.update(1f);
        ai.update();

        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(2, 2), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress(), 0f);
    }

    @Test
    void blockedChoiceTurnsTheTankWithoutLeavingTheCell() {
        Tank tank = new Tank(new GridPoint2(1, 1), 1f, new CollisionDetector() {
            @Override
            public boolean isOccupied(GridPoint2 position) {
                return new GridPoint2(1, 2).equals(position);
            }
        });
        AiTankController ai = new AiTankController(tank, new MoveStrategy() {
            @Override
            public Direction chooseDirection() {
                return Direction.UP;
            }
        });

        ai.update();

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress(), 0f);
        assertEquals(Direction.UP.getRotation(), tank.getRotation(), 0f);
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
