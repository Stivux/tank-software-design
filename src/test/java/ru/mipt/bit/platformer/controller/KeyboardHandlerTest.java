package ru.mipt.bit.platformer.controller;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyboardHandlerTest {

    @Test
    void runsActionWhenAnyOfItsKeysIsPressed() {
        FakeKeyInput input = new FakeKeyInput();
        KeyboardHandler keyboard = new KeyboardHandler(input);
        List<String> calls = new ArrayList<String>();
        keyboard.addHandler(record(calls, "move"), 1, 2);

        keyboard.update();
        assertEquals(0, calls.size());

        input.press(2);
        keyboard.update();

        assertEquals(List.of("move"), calls);
    }

    @Test
    void runsActionOnceWhenSeveralOfItsKeysAreHeld() {
        FakeKeyInput input = new FakeKeyInput();
        KeyboardHandler keyboard = new KeyboardHandler(input);
        List<String> calls = new ArrayList<String>();
        keyboard.addHandler(record(calls, "move"), 1, 2);
        input.press(1);
        input.press(2);

        keyboard.update();

        assertEquals(List.of("move"), calls);
    }

    @Test
    void runsPressedHandlersInRegistrationOrder() {
        FakeKeyInput input = new FakeKeyInput();
        KeyboardHandler keyboard = new KeyboardHandler(input);
        List<String> calls = new ArrayList<String>();
        keyboard.addHandler(record(calls, "up"), 1);
        keyboard.addHandler(record(calls, "shoot"), 2);
        input.press(2);
        input.press(1);

        keyboard.update();

        assertEquals(List.of("up", "shoot"), calls);
    }

    private static Action record(final List<String> calls, final String name) {
        return new Action() {
            @Override
            public void execute() {
                calls.add(name);
            }
        };
    }
}
