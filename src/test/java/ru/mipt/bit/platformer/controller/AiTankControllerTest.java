package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AiTankControllerTest {

    @Test
    void movesTheTankInTheFirstScriptedDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), 1f, free());
        AiTankController ai = new AiTankController(tank, new ScriptedRandom(1));

        ai.update();

        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress(), 0f);
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation(), 0f);
    }

    @Test
    void choosesAgainAfterTheTankArrives() {
        Tank tank = new Tank(new GridPoint2(1, 1), 1f, free());
        AiTankController ai = new AiTankController(tank, new ScriptedRandom(3, 0));

        ai.update();
        tank.update(1f);
        ai.update();

        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(2, 2), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress(), 0f);
    }

    @Test
    void sometimesShootsInsteadOfMoving() {
        Level level = new Level(5, 5, new GridPoint2(0, 0), Collections.<GameObject>emptyList());
        Tank tank = new Tank(new GridPoint2(1, 1), 1f, free(), level);
        level.add(tank);

        new AiTankController(tank, new ScriptedRandom(Direction.values().length)).update();

        assertEquals(1f, tank.getMovementProgress(), 0f);
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
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
    void blockedChoiceTurnsTheTankWithoutLeavingTheCell() {
        Tank tank = new Tank(new GridPoint2(1, 1), 1f, new CollisionDetector() {
            @Override
            public boolean isOccupied(GridPoint2 position) {
                return new GridPoint2(1, 2).equals(position);
            }
        });
        AiTankController ai = new AiTankController(tank, new ScriptedRandom(0));

        ai.update();

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(1f, tank.getMovementProgress(), 0f);
        assertEquals(Direction.UP.getRotation(), tank.getRotation(), 0f);
    }

    private static final class ScriptedRandom extends Random {
        private int cursor;
        private final int[] values;

        ScriptedRandom(int... values) {
            super(0);
            this.values = values;
        }

        @Override
        public int nextInt(int bound) {
            return values[Math.min(cursor++, values.length - 1)];
        }
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
