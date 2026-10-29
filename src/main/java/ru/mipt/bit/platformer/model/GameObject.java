package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface GameObject extends Updatable {
    GridPoint2 getCoordinates();

    default boolean occupies(GridPoint2 cell) {
        return getCoordinates().equals(cell);
    }

    default boolean blocksMovement() {
        return false;
    }

    default void hitByBullet(int damage, World world) {
    }

    @Override
    default void update(float deltaTime) {
    }

    default <R> R accept(GameObjectVisitor<R> visitor) {
        return visitor.visitGameObject(this);
    }
}
