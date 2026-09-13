package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank implements GameObject {
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final float movementSpeed;
    private float movementProgress = 1f;
    private float rotation;

    public Tank(GridPoint2 startCoordinates, float movementSpeed) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.destinationCoordinates = new GridPoint2(startCoordinates);
        this.movementSpeed = movementSpeed;
        this.rotation = 0f;
    }

    public void tryMove(Direction direction, CollisionDetector collisionDetector) {
        if (!hasReachedDestination()) {
            return;
        }
        GridPoint2 candidate = direction.nextPosition(coordinates);
        if (!collisionDetector.isOccupied(candidate)) {
            destinationCoordinates.set(candidate);
            movementProgress = 0f;
        }
        rotation = direction.getRotation();
    }

    public void update(float deltaTime) {
        movementProgress = continueProgress(movementProgress, deltaTime, movementSpeed);
        if (hasReachedDestination()) {
            coordinates.set(destinationCoordinates);
        }
    }

    public boolean hasReachedDestination() {
        return isEqual(movementProgress, 1f);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public float getRotation() {
        return rotation;
    }
}
