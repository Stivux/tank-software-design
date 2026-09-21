package ru.mipt.bit.platformer.controller;

import java.util.HashSet;
import java.util.Set;

final class FakeKeyInput implements KeyInput {
    private final Set<Integer> pressed = new HashSet<Integer>();

    void press(int key) {
        pressed.add(key);
    }

    void release(int key) {
        pressed.remove(key);
    }

    @Override
    public boolean isKeyPressed(int key) {
        return pressed.contains(key);
    }
}
