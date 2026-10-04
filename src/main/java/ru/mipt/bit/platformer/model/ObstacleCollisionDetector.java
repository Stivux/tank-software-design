package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Collection;

public class ObstacleCollisionDetector implements CollisionDetector {
    private final Collection<? extends GameObject> objects;

    public ObstacleCollisionDetector(Collection<? extends GameObject> objects) {
        this.objects = objects;
    }

    @Override
    public boolean isOccupied(GridPoint2 position) {
        for (GameObject object : objects) {
            if (object.getCoordinates().equals(position)) {
                return true;
            }
        }
        return false;
    }
}
