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
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class GameRenderer implements Disposable {
    private static final float BACKGROUND_RED = 0f;
    private static final float BACKGROUND_GREEN = 0f;
    private static final float BACKGROUND_BLUE = 0.2f;
    private static final float BACKGROUND_ALPHA = 1f;

    private final Batch batch;
    private final TiledMap level;
    private final MapRenderer levelRenderer;
    private final TileMovement tileMovement;
    private final Texture tankTexture;
    private final Texture treeTexture;
    private final GraphicObject tankGraphics;
    private final List<GraphicObject> obstacleGraphics = new ArrayList<GraphicObject>();

    public GameRenderer(Collection<? extends Obstacle> obstacles) {
        batch = new SpriteBatch();
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        tankTexture = new Texture("images/tank_blue.png");
        treeTexture = new Texture("images/greenTree.png");
        tankGraphics = new GraphicObject(tankTexture);

        for (Obstacle obstacle : obstacles) {
            GraphicObject graphics = new GraphicObject(treeTexture);
            moveRectangleAtTileCenter(groundLayer, graphics.getRectangle(), obstacle.getCoordinates());
            obstacleGraphics.add(graphics);
        }
    }

    public void render(Tank tank) {
        Gdx.gl.glClearColor(BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE, BACKGROUND_ALPHA);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        tileMovement.moveRectangleBetweenTileCenters(
                tankGraphics.getRectangle(),
                tank.getCoordinates(),
                tank.getDestinationCoordinates(),
                tank.getMovementProgress()
        );

        levelRenderer.render();

        batch.begin();
        tankGraphics.draw(batch, tank.getRotation());
        for (GraphicObject graphics : obstacleGraphics) {
            graphics.draw(batch, 0f);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        treeTexture.dispose();
        tankTexture.dispose();
        level.dispose();
        batch.dispose();
    }
}
