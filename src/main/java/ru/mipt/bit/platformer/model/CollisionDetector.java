package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface CollisionDetector {
    boolean isOccupied(GridPoint2 position);
}
