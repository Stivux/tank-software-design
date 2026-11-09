package ru.mipt.bit.platformer.controller;

import java.util.ArrayList;
import java.util.List;

public class KeyboardHandler implements Controller, KeyBindings {
    private final KeyInput input;
    private final List<KeyBinding> bindings = new ArrayList<KeyBinding>();

    public KeyboardHandler(KeyInput input) {
        this.input = input;
    }

    @Override
    public void addHandler(Action action, int... keys) {
        bindings.add(new KeyBinding(action, false, keys));
    }

    @Override
    public void addToggleHandler(Action action, int... keys) {
        bindings.add(new KeyBinding(action, true, keys));
    }

    @Override
    public void update() {
        for (KeyBinding binding : bindings) {
            binding.update(input);
        }
    }

    private static final class KeyBinding {
        private final Action action;
        private final boolean onPressOnly;
        private final int[] keys;
        private boolean wasPressed;

        private KeyBinding(Action action, boolean onPressOnly, int[] keys) {
            this.action = action;
            this.onPressOnly = onPressOnly;
            this.keys = keys.clone();
        }

        private void update(KeyInput input) {
            boolean pressed = isPressed(input);
            if (pressed && (!onPressOnly || !wasPressed)) {
                action.execute();
            }
            wasPressed = pressed;
        }

        private boolean isPressed(KeyInput input) {
            for (int key : keys) {
                if (input.isKeyPressed(key)) {
                    return true;
                }
            }
            return false;
        }
    }
}
