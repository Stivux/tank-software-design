package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.renderer.HealthBarVisibility;

public final class ToggleHealthBarsAction implements Action {
    private final HealthBarVisibility visibility;

    public ToggleHealthBarsAction(HealthBarVisibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public void execute() {
        visibility.toggle();
    }
}
