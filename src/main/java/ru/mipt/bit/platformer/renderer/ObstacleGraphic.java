package ru.mipt.bit.platformer.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mipt.bit.platformer.model.Obstacle;

public class ObstacleGraphic implements EntityGraphic {
    private final GraphicObject graphics;

    public ObstacleGraphic(Obstacle obstacle, GraphicObject graphics, TiledMapTileLayer layer) {
        this.graphics = graphics;
        graphics.placeAtTile(layer, obstacle.getCoordinates());
    }

    @Override
    public void draw(Batch batch) {
        graphics.draw(batch, 0f);
    }
}
