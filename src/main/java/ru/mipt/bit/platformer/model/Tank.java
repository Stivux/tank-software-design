package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank implements Movable {
    private static final int DEFAULT_HEALTH = 1;
    private static final int BULLET_DAMAGE = 1;
    private static final float BULLET_SPEED = 0.2f;
    private static final float SHOOT_COOLDOWN = 0.4f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final float movementSpeed;
    private final CollisionDetector collisionDetector;
    private final World world;
    private final int bulletDamage;
    private float movementProgress = 1f;
    private float shootCooldown;
    private Direction facing = Direction.RIGHT;
    private int health;

    public Tank(GridPoint2 startCoordinates, float movementSpeed, CollisionDetector collisionDetector) {
        this(startCoordinates, movementSpeed, collisionDetector, null, DEFAULT_HEALTH, BULLET_DAMAGE);
    }

    public Tank(GridPoint2 startCoordinates, float movementSpeed, CollisionDetector collisionDetector, World world) {
        this(startCoordinates, movementSpeed, collisionDetector, world, DEFAULT_HEALTH, BULLET_DAMAGE);
    }

    public Tank(GridPoint2 startCoordinates, float movementSpeed, CollisionDetector collisionDetector,
                World world, int health, int bulletDamage) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.destinationCoordinates = new GridPoint2(startCoordinates);
        this.movementSpeed = movementSpeed;
        this.collisionDetector = collisionDetector;
        this.world = world;
        this.health = health;
        this.bulletDamage = bulletDamage;
    }

    @Override
    public void tryMove(Direction direction) {
        if (health <= 0 || !hasReachedDestination()) {
            return;
        }
        facing = direction;
        GridPoint2 candidate = direction.addDirectionDelta(coordinates);
        if (!collisionDetector.isOccupied(candidate)) {
            destinationCoordinates.set(candidate);
            movementProgress = 0f;
        }
    }

    public void shoot() {
        if (world == null || health <= 0 || shootCooldown > 0f) {
            return;
        }
        if (!hasReachedDestination()) {
            return;
        }
        GridPoint2 cell = facing.addDirectionDelta(coordinates);
        if (!world.inBounds(cell)) {
            return;
        }
        shootCooldown = SHOOT_COOLDOWN;
        world.add(new Bullet(cell, facing, BULLET_SPEED, bulletDamage, world));
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    @Override
    public void hitByBullet(int damage, World world) {
        takeDamage(damage);
        if (health <= 0) {
            world.remove(this);
        }
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void update(float deltaTime) {
        movementProgress = advanceProgress(movementProgress, deltaTime, movementSpeed);
        if (hasReachedDestination()) {
            coordinates.set(destinationCoordinates);
        }
        if (shootCooldown > 0f) {
            shootCooldown -= deltaTime;
            if (shootCooldown < 0f) {
                shootCooldown = 0f;
            }
        }
    }

    private boolean hasReachedDestination() {
        return isEqual(movementProgress, 1f);
    }

    private static float advanceProgress(float progress, float deltaTime, float speed) {
        float next = progress + deltaTime / speed;
        if (next < 0f) {
            return 0f;
        }
        if (next > 1f) {
            return 1f;
        }
        return next;
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public float getRotation() {
        return facing.getRotation();
    }
}
