package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveTankActionTest {

    @Test
    void movesTheTankInTheBoundDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), 1f, new CollisionDetector() {
            @Override
            public boolean isOccupied(GridPoint2 position) {
                return false;
            }
        });

        new MoveTankAction(tank, Direction.LEFT).execute();

        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation(), 0f);
    }
}
