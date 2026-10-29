package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public final class Bullet implements Movable {
    private final GridPoint2 coordinates;
    private final GridPoint2 destination;
    private final Direction direction;
    private final float speed;
    private final int damage;
    private final World world;
    private float progress;

    public Bullet(GridPoint2 start, Direction direction, float speed, int damage, World world) {
        this.coordinates = new GridPoint2(start);
        this.destination = new GridPoint2(start);
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
        this.world = world;
    }

    @Override
    public void update(float deltaTime) {
        float timeLeft = deltaTime;
        while (true) {
            if (coordinates.equals(destination)) {
                if (resolve(coordinates)) {
                    return;
                }
                if (!aimNext()) {
                    return;
                }
                if (timeLeft <= 0f) {
                    return;
                }
            }
            float need = (1f - progress) * speed;
            if (timeLeft < need) {
                progress += timeLeft / speed;
                return;
            }
            timeLeft -= need;
            progress = 1f;
            coordinates.set(destination);
        }
    }

    @Override
    public void tryMove(Direction direction) {
    }

    private boolean aimNext() {
        GridPoint2 next = direction.addDirectionDelta(coordinates);
        if (!world.inBounds(next)) {
            world.remove(this);
            return false;
        }
        destination.set(next);
        progress = 0f;
        return true;
    }

    private boolean resolve(GridPoint2 cell) {
        List<GameObject> hits = new ArrayList<GameObject>();
        for (GameObject object : world.getObjects()) {
            if (object != this && object.occupies(cell)) {
                hits.add(object);
            }
        }
        if (hits.isEmpty()) {
            return false;
        }
        for (GameObject hit : hits) {
            hit.hitByBullet(damage, world);
        }
        world.remove(this);
        return true;
    }

    @Override
    public boolean blocksMovement() {
        return false;
    }

    @Override
    public void hitByBullet(int damage, World world) {
        world.remove(this);
    }

    @Override
    public <R> R accept(GameObjectVisitor<R> visitor) {
        return visitor.visitBullet(this);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return destination;
    }

    @Override
    public float getMovementProgress() {
        return progress;
    }

    @Override
    public float getRotation() {
        return direction.getRotation();
    }
}
