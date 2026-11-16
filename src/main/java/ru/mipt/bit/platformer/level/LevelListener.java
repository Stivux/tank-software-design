package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.GameObject;

/** Port notified when an object is added or removed. */
public interface LevelListener {
    void onAdded(GameObject object);

    void onRemoved(GameObject object);
}
