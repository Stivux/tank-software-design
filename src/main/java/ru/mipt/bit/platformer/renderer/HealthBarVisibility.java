package ru.mipt.bit.platformer.renderer;

public final class HealthBarVisibility {
    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void toggle() {
        visible = !visible;
    }
}
