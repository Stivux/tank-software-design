package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank implements Movable {
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final float movementSpeed;
    private final CollisionDetector collisionDetector;
    private float movementProgress = 1f;
    private float rotation;

    public Tank(GridPoint2 startCoordinates, float movementSpeed, CollisionDetector collisionDetector) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.destinationCoordinates = new GridPoint2(startCoordinates);
        this.movementSpeed = movementSpeed;
        this.collisionDetector = collisionDetector;
        this.rotation = 0f;
    }

    public void tryMove(Direction direction) {
        if (!hasReachedDestination()) {
            return;
        }
        GridPoint2 candidate = direction.addDirectionDelta(coordinates);
        if (!collisionDetector.isOccupied(candidate)) {
            destinationCoordinates.set(candidate);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    public void update(float deltaTime) {
        movementProgress = advanceProgress(movementProgress, deltaTime, movementSpeed);
        if (hasReachedDestination()) {
            coordinates.set(destinationCoordinates);
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
        return rotation;
    }
}
