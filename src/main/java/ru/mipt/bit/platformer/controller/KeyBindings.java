package ru.mipt.bit.platformer.controller;

public interface KeyBindings {
    void addHandler(Action action, int... keys);

    void addToggleHandler(Action action, int... keys);
}
