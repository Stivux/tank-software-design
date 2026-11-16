package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.level.Level;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class TankStateTest {
    private static final float SPEED = 1f;

    @Test
    void fullHealthIsLightAndMovesAtTheBaseSpeed() {
        Tank tank = tank(200);

        assertSame(LightTankState.INSTANCE, tank.getState());
        assertEquals(1f, progressAfter(tank, 1f), 0.0001f);
    }

    @Test
    void justAboveSeventyPercentStaysLight() {
        Tank tank = tank(200);
        tank.takeDamage(59);

        assertEquals(141, tank.getHealth());
        assertSame(LightTankState.INSTANCE, tank.getState());
        assertEquals(1f, progressAfter(tank, 1f), 0.0001f);
    }

    @Test
    void seventyPercentDownToFifteenPercentMovesTwiceAsSlowAndStillShoots() {
        Tank atSeventy = tank(200);
        atSeventy.takeDamage(60);
        assertEquals(140, atSeventy.getHealth());
        assertSame(MediumTankState.INSTANCE, atSeventy.getState());
        assertEquals(0.5f, progressAfter(atSeventy, 1f), 0.0001f);

        Tank atFifteen = tank(200);
        atFifteen.takeDamage(170);
        assertEquals(30, atFifteen.getHealth());
        assertSame(MediumTankState.INSTANCE, atFifteen.getState());
        assertEquals(0.5f, progressAfter(atFifteen, 1f), 0.0001f);

        Level level = level();
        Tank shooter = tank(200, level);
        shooter.takeDamage(60);
        shooter.shoot();
        assertEquals(1, level.getObjects().size());
        shooter.shoot();
        assertEquals(1, level.getObjects().size());
        shooter.update(0.4f);
        shooter.shoot();
        assertEquals(2, level.getObjects().size());
    }

    @Test
    void belowFifteenPercentMovesThreeTimesAsSlowAndDoesNotShoot() {
        Level level = level();
        Tank tank = tank(200, level);
        tank.takeDamage(171);

        assertEquals(29, tank.getHealth());
        assertSame(HeavyTankState.INSTANCE, tank.getState());
        tank.shoot();
        tank.shoot();
        assertEquals(0, level.getObjects().size());

        tank.tryMove(Direction.RIGHT);
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        tank.update(1f);
        assertEquals(1f / 3f, tank.getMovementProgress(), 0.0001f);
    }

    @Test
    void damageDuringAMoveSlowsTheRestOfThatMove() {
        Tank tank = tank(100);
        tank.tryMove(Direction.RIGHT);
        tank.update(0.5f);
        assertEquals(0.5f, tank.getMovementProgress(), 0.0001f);

        tank.takeDamage(30);
        assertSame(MediumTankState.INSTANCE, tank.getState());
        tank.update(0.5f);

        assertEquals(0.75f, tank.getMovementProgress(), 0.0001f);
    }

    @Test
    void aStateOtherThanHealthCanMoveAndShootOnItsOwnTerms() {
        Level level = level();
        Tank tank = tank(100, level);
        tank.setState(new TankState() {
            @Override
            public float cellTravelTimeFactor() {
                return 0.5f;
            }

            @Override
            public float shootIntervalFactor() {
                return 0.5f;
            }

            @Override
            public boolean canShoot() {
                return true;
            }

            @Override
            public TankState afterHealthChange(int health, int initialHealth) {
                return this;
            }
        });

        tank.takeDamage(90);
        assertEquals(10, tank.getHealth());
        assertEquals(1f, progressAfter(tank, 0.5f), 0.0001f);

        tank.shoot();
        tank.update(0.2f);
        tank.shoot();
        assertEquals(2, level.getObjects().size());
    }

    @Test
    void percentagesUseTheHealthTheTankWasCreatedWith() {
        Tank tank = tank(20);
        assertEquals(20, tank.getMaxHealth());
        assertSame(LightTankState.INSTANCE, tank.getState());

        tank.takeDamage(10);
        assertSame(MediumTankState.INSTANCE, tank.getState());
        assertEquals(0.5f, progressAfter(tank, 1f), 0.0001f);
    }

    private static float progressAfter(Tank tank, float deltaTime) {
        tank.tryMove(Direction.RIGHT);
        tank.update(deltaTime);
        return tank.getMovementProgress();
    }

    private static Tank tank(int health) {
        return tank(health, null);
    }

    private static Tank tank(int health, Level level) {
        return new Tank(new GridPoint2(1, 1), SPEED, free(), level, health, 1);
    }

    private static Level level() {
        return new Level(6, 4, new GridPoint2(0, 0), Collections.<GameObject>emptyList());
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
