package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public enum Direction {
    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    private final int deltaX;
    private final int deltaY;
    private final float rotation;

    Direction(int deltaX, int deltaY, float rotation) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.rotation = rotation;
    }

    public GridPoint2 addDirectionDelta(GridPoint2 source) {
        return new GridPoint2(source).add(deltaX, deltaY);
    }

    float rotation() {
        return rotation;
    }
}
