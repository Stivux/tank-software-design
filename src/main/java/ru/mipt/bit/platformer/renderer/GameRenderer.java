package ru.mipt.bit.platformer.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameRenderer implements Renderer {
    private static final float BACKGROUND_RED = 0f;
    private static final float BACKGROUND_GREEN = 0f;
    private static final float BACKGROUND_BLUE = 0.2f;
    private static final float BACKGROUND_ALPHA = 1f;

    private final Batch batch;
    private final TiledMap level;
    private final MapRenderer levelRenderer;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;
    private final Map<String, Texture> textures = new HashMap<String, Texture>();
    private final List<MovableView> movableViews = new ArrayList<MovableView>();
    private final List<GraphicObject> staticGraphics = new ArrayList<GraphicObject>();

    public GameRenderer() {
        batch = new SpriteBatch();
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    public void register(Movable movable, String texturePath) {
        movableViews.add(new MovableView(movable, new GraphicObject(texture(texturePath))));
    }

    public void register(Obstacle obstacle, String texturePath) {
        GraphicObject graphics = new GraphicObject(texture(texturePath));
        graphics.placeAtTile(groundLayer, obstacle.getCoordinates());
        staticGraphics.add(graphics);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE, BACKGROUND_ALPHA);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        for (MovableView view : movableViews) {
            Movable movable = view.movable;
            view.graphics.interpolate(
                    tileMovement,
                    movable.getCoordinates(),
                    movable.getDestinationCoordinates(),
                    movable.getMovementProgress()
            );
        }

        levelRenderer.render();

        batch.begin();
        for (MovableView view : movableViews) {
            view.graphics.draw(batch, view.movable.getRotation());
        }
        for (GraphicObject graphics : staticGraphics) {
            graphics.draw(batch, 0f);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        level.dispose();
        batch.dispose();
    }

    private Texture texture(String path) {
        Texture texture = textures.get(path);
        if (texture == null) {
            texture = new Texture(path);
            textures.put(path, texture);
        }
        return texture;
    }

    private static class MovableView {
        final Movable movable;
        final GraphicObject graphics;

        MovableView(Movable movable, GraphicObject graphics) {
            this.movable = movable;
            this.graphics = graphics;
        }
    }
}
