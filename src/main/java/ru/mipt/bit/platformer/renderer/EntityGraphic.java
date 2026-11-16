package ru.mipt.bit.platformer.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;

/** Port for drawing one object. */
public interface EntityGraphic {
    void draw(Batch batch);
}
