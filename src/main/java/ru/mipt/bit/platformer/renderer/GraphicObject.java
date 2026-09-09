package ru.mipt.bit.platformer.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class GraphicObject {
    private final TextureRegion region;
    private final Rectangle rectangle;

    public GraphicObject(Texture texture) {
        this.region = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(region);
    }

    public void draw(Batch batch, float rotation) {
        drawTextureRegionUnscaled(batch, region, rectangle, rotation);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
