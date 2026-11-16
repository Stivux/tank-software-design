package ru.mipt.bit.platformer.controller;

/** Port for binding actions to keys. */
public interface KeyBindings {
    void addHandler(Action action, int... keys);

    void addToggleHandler(Action action, int... keys);
}
