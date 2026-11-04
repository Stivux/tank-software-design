package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Collection;

public final class MovementCollisionDetector implements CollisionDetector {
    private final int width;
    private final int height;
    private final CollisionDetector obstacles;
    private final Collection<? extends Movable> tanks;

    public MovementCollisionDetector(int width, int height, CollisionDetector obstacles,
                                     Collection<? extends Movable> tanks) {
        this.width = width;
        this.height = height;
        this.obstacles = obstacles;
        this.tanks = tanks;
    }

    @Override
    public boolean isOccupied(GridPoint2 position) {
        if (position.x < 0 || position.y < 0 || position.x >= width || position.y >= height) {
            return true;
        }
        if (obstacles.isOccupied(position)) {
            return true;
        }
        for (Movable tank : tanks) {
            if (tank.getCoordinates().equals(position) || tank.getDestinationCoordinates().equals(position)) {
                return true;
            }
        }
        return false;
    }
}
