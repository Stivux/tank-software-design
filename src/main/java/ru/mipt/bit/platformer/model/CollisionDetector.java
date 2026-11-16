package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/** Port: whether a cell is occupied. */
public interface CollisionDetector {
    boolean isOccupied(GridPoint2 position);
}
