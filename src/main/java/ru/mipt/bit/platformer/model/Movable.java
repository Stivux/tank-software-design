package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface Movable extends GameObject {
    void tryMove(Direction direction);

    GridPoint2 getDestinationCoordinates();

    float getMovementProgress();

    float getRotation();

    @Override
    default boolean occupies(GridPoint2 cell) {
        return getCoordinates().equals(cell) || getDestinationCoordinates().equals(cell);
    }

    @Override
    default boolean blocksMovement() {
        return true;
    }

    @Override
    default <R> R accept(GameObjectVisitor<R> visitor) {
        return visitor.visitMovable(this);
    }
}
