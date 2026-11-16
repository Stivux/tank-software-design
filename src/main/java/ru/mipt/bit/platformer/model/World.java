package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

/** Port for the world: objects and bounds. */
public interface World {
    void add(GameObject object);

    void remove(GameObject object);

    boolean inBounds(GridPoint2 cell);

    List<GameObject> getObjects();
}
