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
        bindings.add(new KeyBinding(action, keys));
    }

    @Override
    public void update() {
        for (KeyBinding binding : bindings) {
            if (binding.isPressed(input)) {
                binding.execute();
            }
        }
    }

    private static final class KeyBinding {
        private final Action action;
        private final int[] keys;

        private KeyBinding(Action action, int[] keys) {
            this.action = action;
            this.keys = keys.clone();
        }

        private boolean isPressed(KeyInput input) {
            for (int key : keys) {
                if (input.isKeyPressed(key)) {
                    return true;
                }
            }
            return false;
        }

        private void execute() {
            action.execute();
        }
    }
}
