package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShootTankActionTest {

    @Test
    void asksTheTankToShoot() {
        Level level = new Level(4, 3, new GridPoint2(0, 0), Collections.<GameObject>emptyList());
        Tank tank = new Tank(new GridPoint2(1, 1), 1f, free(), level);
        level.add(tank);

        new ShootTankAction(tank).execute();

        assertEquals(2, level.getObjects().size());
        assertTrue(level.getObjects().get(1) instanceof Bullet);
        assertEquals(new GridPoint2(2, 1), level.getObjects().get(1).getCoordinates());
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
