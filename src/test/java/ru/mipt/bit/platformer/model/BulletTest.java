package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulletTest {

    @Test
    void shootPlacesTheBulletOnTheNextCellAndFacesThatWay() {
        Fixture world = new Fixture();
        Tank tank = world.tank(1, 1);

        tank.shoot();

        Bullet bullet = onlyBullet(world.level);
        assertEquals(new GridPoint2(2, 1), bullet.getCoordinates());
        assertEquals(Direction.RIGHT.getRotation(), bullet.getRotation(), 0f);
    }

    @Test
    void holdingFireDoesNotStackBulletsUntilTheCooldownPasses() {
        Fixture world = new Fixture();
        Tank tank = world.tank(1, 1);

        tank.shoot();
        tank.shoot();
        assertEquals(1, bullets(world.level).size());

        world.level.update(10f);
        assertEquals(0, bullets(world.level).size());

        tank.shoot();
        assertEquals(1, bullets(world.level).size());
    }

    @Test
    void slidingTankDoesNotShoot() {
        Fixture world = new Fixture();
        Tank tank = world.tank(1, 1);
        tank.tryMove(Direction.UP);

        tank.shoot();

        assertEquals(0, bullets(world.level).size());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
    }

    @Test
    void shotOffTheMapDoesNotSpawnABullet() {
        Fixture world = new Fixture();
        Tank tank = world.tank(0, 1);
        tank.tryMove(Direction.LEFT);

        tank.shoot();

        assertEquals(0, bullets(world.level).size());
        assertEquals(new GridPoint2(0, 1), tank.getCoordinates());
        assertEquals(Direction.LEFT.getRotation(), tank.getRotation(), 0f);
    }

    @Test
    void bulletFliesAlongItsDirectionAndThenLeavesTheMap() {
        Fixture world = new Fixture();
        Bullet bullet = new Bullet(new GridPoint2(1, 1), Direction.UP, 1f, 1, world.level);
        world.level.add(bullet);

        world.level.update(1f);
        assertEquals(new GridPoint2(1, 2), bullet.getCoordinates());
        assertTrue(world.level.getObjects().contains(bullet));

        world.level.update(10f);
        assertFalse(world.level.getObjects().contains(bullet));
    }

    @Test
    void adjacentTankLosesHealthAndBothObjectsDisappearWhenHealthRunsOut() {
        Fixture world = new Fixture();
        Tank shooter = world.tank(1, 1);
        Tank enemy = world.tank(2, 1, 1, 1);

        shooter.shoot();
        world.level.update(0f);

        assertFalse(world.level.getObjects().contains(enemy));
        assertEquals(0, bullets(world.level).size());
        assertTrue(world.level.getObjects().contains(shooter));

        shooter.tryMove(Direction.RIGHT);
        assertEquals(new GridPoint2(2, 1), shooter.getDestinationCoordinates());
    }

    @Test
    void tankStaysWhileHealthRemains() {
        Fixture world = new Fixture();
        Tank shooter = world.tank(1, 1);
        Tank enemy = world.tank(2, 1, 2, 1);

        shooter.shoot();
        world.level.update(0f);

        assertEquals(1, enemy.getHealth());
        assertTrue(world.level.getObjects().contains(enemy));
        assertEquals(0, bullets(world.level).size());
    }

    @Test
    void adjacentTreeRemovesOnlyTheBullet() {
        Tree tree = new Tree(new GridPoint2(2, 1));
        Fixture world = new Fixture(Collections.<GameObject>singletonList(tree));
        world.level.add(tree);
        Tank tank = world.tank(1, 1);

        tank.shoot();
        world.level.update(0f);

        assertTrue(world.level.getObjects().contains(tree));
        assertEquals(0, bullets(world.level).size());
        assertTrue(world.level.getObjects().contains(tank));
    }

    @Test
    void bulletsOnTheSameCellRemoveEachOther() {
        Fixture world = new Fixture();
        Bullet right = new Bullet(new GridPoint2(2, 1), Direction.RIGHT, 1f, 1, world.level);
        Bullet left = new Bullet(new GridPoint2(2, 1), Direction.LEFT, 1f, 1, world.level);
        world.level.add(right);
        world.level.add(left);

        world.level.update(0f);

        assertFalse(world.level.getObjects().contains(right));
        assertFalse(world.level.getObjects().contains(left));
    }

    @Test
    void meetingBulletsRemoveEachOther() {
        Fixture world = new Fixture();
        Bullet right = new Bullet(new GridPoint2(1, 1), Direction.RIGHT, 1f, 1, world.level);
        Bullet left = new Bullet(new GridPoint2(2, 1), Direction.LEFT, 1f, 1, world.level);
        world.level.add(right);
        world.level.add(left);

        world.level.update(0f);

        assertFalse(world.level.getObjects().contains(right));
        assertFalse(world.level.getObjects().contains(left));
    }

    private static Bullet onlyBullet(Level level) {
        List<Bullet> found = bullets(level);
        assertEquals(1, found.size());
        return found.get(0);
    }

    private static List<Bullet> bullets(Level level) {
        List<Bullet> found = new ArrayList<Bullet>();
        for (GameObject object : level.getObjects()) {
            if (object instanceof Bullet) {
                found.add((Bullet) object);
            }
        }
        return found;
    }

    private static final class Fixture {
        private final Level level;
        private final CollisionDetector collisions;

        private Fixture() {
            this(Collections.<GameObject>emptyList());
        }

        private Fixture(List<GameObject> obstacles) {
            level = new Level(6, 4, new GridPoint2(0, 0), obstacles);
            collisions = new MovementCollisionDetector(
                    6, 4, new ObstacleCollisionDetector(level.getObstacles()), level.getObjects());
        }

        private Tank tank(int x, int y) {
            return tank(x, y, 1, 1);
        }

        private Tank tank(int x, int y, int health, int damage) {
            Tank tank = new Tank(new GridPoint2(x, y), 1f, collisions, level, health, damage);
            level.add(tank);
            return tank;
        }
    }
}
