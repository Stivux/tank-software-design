package ru.mipt.bit.platformer.controller;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.renderer.HealthBarVisibility;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleHealthBarsActionTest {

    @Test
    void togglesVisibilityOnEveryExecution() {
        HealthBarVisibility visibility = new HealthBarVisibility();
        Action action = new ToggleHealthBarsAction(visibility);

        assertFalse(visibility.isVisible());
        action.execute();
        assertTrue(visibility.isVisible());
        action.execute();
        assertFalse(visibility.isVisible());
    }
}
