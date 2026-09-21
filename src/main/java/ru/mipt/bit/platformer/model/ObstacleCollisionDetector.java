package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Collection;

public class ObstacleCollisionDetector implements CollisionDetector {
    private final Collection<? extends Obstacle> obstacles;

    public ObstacleCollisionDetector(Collection<? extends Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    @Override
    public boolean isOccupied(GridPoint2 position) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getCoordinates().equals(position)) {
                return true;
            }
        }
        return false;
    }
}
