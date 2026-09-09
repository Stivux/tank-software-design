package ru.mipt.bit.platformer.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

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

    public void placeAtTile(TiledMapTileLayer layer, GridPoint2 coordinates) {
        moveRectangleAtTileCenter(layer, rectangle, coordinates);
    }

    public void interpolate(TileMovement tileMovement, GridPoint2 from, GridPoint2 to, float progress) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, from, to, progress);
    }
}
