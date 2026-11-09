package ru.mipt.bit.platformer.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;

public class HealthBarGraphic implements EntityGraphic {
    private static final float BAR_WIDTH = 80f;
    private static final float BAR_HEIGHT = 12f;
    private static final float BAR_GAP = 4f;
    private static final Color BACKGROUND_COLOR = new Color(0.15f, 0.15f, 0.15f, 0.85f);
    private static final Color FILL_COLOR = new Color(0.25f, 0.85f, 0.25f, 1f);

    private final EntityGraphic delegate;
    private final GraphicObject graphic;
    private final Tank tank;
    private final HealthBarVisibility visibility;
    private final Texture pixel;

    public HealthBarGraphic(EntityGraphic delegate, GraphicObject graphic, Tank tank,
                            HealthBarVisibility visibility, Texture pixel) {
        this.delegate = delegate;
        this.graphic = graphic;
        this.tank = tank;
        this.visibility = visibility;
        this.pixel = pixel;
    }

    @Override
    public void draw(Batch batch) {
        delegate.draw(batch);
        if (!visibility.isVisible()) {
            return;
        }
        Rectangle bounds = graphic.getRectangle();
        float x = bounds.x + (bounds.width - BAR_WIDTH) / 2f;
        float y = bounds.y + bounds.height + BAR_GAP;
        float fraction = Math.max(0f, Math.min(1f, tank.getHealth() / (float) tank.getMaxHealth()));
        batch.setColor(BACKGROUND_COLOR);
        batch.draw(pixel, x, y, BAR_WIDTH, BAR_HEIGHT);
        batch.setColor(FILL_COLOR);
        batch.draw(pixel, x, y, BAR_WIDTH * fraction, BAR_HEIGHT);
        batch.setColor(Color.WHITE);
    }
}
