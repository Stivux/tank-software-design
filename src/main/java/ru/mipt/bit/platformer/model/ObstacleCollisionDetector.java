package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ObstacleCollisionDetector implements CollisionDetector {
    private final List<Obstacle> obstacles;

    public ObstacleCollisionDetector(Collection<? extends Obstacle> obstacles) {
        this.obstacles = Collections.unmodifiableList(new ArrayList<Obstacle>(obstacles));
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
