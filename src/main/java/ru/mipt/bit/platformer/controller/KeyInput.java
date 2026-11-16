package ru.mipt.bit.platformer.controller;

/** Port for polling keys. */
public interface KeyInput {
    boolean isKeyPressed(int key);
}
