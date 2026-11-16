package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.Input;

/** Adapter from LibGDX {@link com.badlogic.gdx.Input} to {@link KeyInput}. */
public class GdxKeyInput implements KeyInput {
    private final Input input;

    public GdxKeyInput(Input input) {
        this.input = input;
    }

    @Override
    public boolean isKeyPressed(int key) {
        return input.isKeyPressed(key);
    }
}
