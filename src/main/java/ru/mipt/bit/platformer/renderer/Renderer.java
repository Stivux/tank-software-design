package ru.mipt.bit.platformer.renderer;

import com.badlogic.gdx.utils.Disposable;

/** Port for drawing a frame. */
public interface Renderer extends Disposable {
    void render();
}
