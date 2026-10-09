package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.mipt.bit.platformer.controller.Controller;
import ru.mipt.bit.platformer.controller.GdxKeyInput;
import ru.mipt.bit.platformer.controller.KeyboardHandler;
import ru.mipt.bit.platformer.controller.TankControls;
import ru.mipt.bit.platformer.level.TextLevelGenerator;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.ObstacleCollisionDetector;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Updatable;
import ru.mipt.bit.platformer.renderer.GameRenderer;
import ru.mipt.bit.platformer.renderer.MovableGraphic;
import ru.mipt.bit.platformer.renderer.ObstacleGraphic;
import ru.mipt.bit.platformer.renderer.Renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameDesktopLauncher implements ApplicationListener {
    private static final float MOVEMENT_SPEED = 0.4f;
    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 8;
    private static final int RANDOM_TREE_COUNT = 10;
    private static final boolean USE_FILE_LEVEL = true;
    private static final String LEVEL_MAP = "level.tmx";
    private static final String LEVEL_GRID = "level.txt";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";

    private Renderer renderer;
    private final List<Controller> controllers = new ArrayList<Controller>();
    private final List<Updatable> updatables = new ArrayList<Updatable>();

    @Override
    public void create() {
        Level level = createLevelGenerator().generate();
        CollisionDetector collisions = new ObstacleCollisionDetector(level.getObstacles());
        Tank tank = new Tank(level.getPlayerStart(), MOVEMENT_SPEED, collisions);
        KeyboardHandler keyboard = new KeyboardHandler(new GdxKeyInput(Gdx.input));
        TankControls.bindMovement(keyboard, tank);
        controllers.add(keyboard);
        updatables.add(tank);

        GameRenderer gameRenderer = new GameRenderer(LEVEL_MAP);
        gameRenderer.add(new MovableGraphic(tank, gameRenderer.createGraphic(TANK_TEXTURE), gameRenderer.getTileMovement()));
        for (GameObject obstacle : level.getObstacles()) {
            gameRenderer.add(new ObstacleGraphic(obstacle, gameRenderer.createGraphic(TREE_TEXTURE), gameRenderer.getGroundLayer()));
        }
        renderer = gameRenderer;
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        for (Controller controller : controllers) {
            controller.update();
        }
        renderer.render();
        for (Updatable updatable : updatables) {
            updatable.update(deltaTime);
        }
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(MAP_WIDTH * 128, MAP_HEIGHT * 128);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }

    private static LevelGenerator createLevelGenerator() {
        if (USE_FILE_LEVEL) {
            return new TextLevelGenerator(readLines(LEVEL_GRID));
        }
        return new RandomLevelGenerator(MAP_WIDTH, MAP_HEIGHT, RANDOM_TREE_COUNT, new Random());
    }

    private static List<String> readLines(String resource) {
        InputStream stream = GameDesktopLauncher.class.getClassLoader().getResourceAsStream(resource);
        if (stream == null) {
            throw new IllegalStateException("Missing resource: " + resource);
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            try {
                List<String> lines = new ArrayList<String>();
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                return lines;
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + resource, e);
        }
    }
}
