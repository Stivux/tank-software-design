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
    private final List<EntityGraphic> graphics = new ArrayList<EntityGraphic>();

    public GameRenderer(String levelPath) {
        batch = new SpriteBatch();
        level = new TmxMapLoader().load(levelPath);
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
    }

    public void add(EntityGraphic graphic) {
        graphics.add(graphic);
    }

    public GraphicObject createGraphic(String texturePath) {
        return new GraphicObject(texture(texturePath));
    }

    public TileMovement getTileMovement() {
        return tileMovement;
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE, BACKGROUND_ALPHA);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        levelRenderer.render();

        batch.begin();
        for (EntityGraphic graphic : graphics) {
            graphic.draw(batch);
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
}
