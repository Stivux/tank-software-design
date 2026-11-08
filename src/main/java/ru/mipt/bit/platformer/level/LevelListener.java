package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.GameObject;

public interface LevelListener {
    void onAdded(GameObject object);

    void onRemoved(GameObject object);
}
