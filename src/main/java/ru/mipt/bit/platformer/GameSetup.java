package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.controller.AiTankController;
import ru.mipt.bit.platformer.controller.Controller;
import ru.mipt.bit.platformer.controller.GdxKeyInput;
import ru.mipt.bit.platformer.controller.KeyboardHandler;
import ru.mipt.bit.platformer.controller.TankControls;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.level.RandomTankGenerator;
import ru.mipt.bit.platformer.level.TextLevelGenerator;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.MovementCollisionDetector;
import ru.mipt.bit.platformer.model.ObstacleCollisionDetector;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Updatable;
import ru.mipt.bit.platformer.renderer.GameRenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GameSetup {
    static final int MAP_WIDTH = 10;
    static final int MAP_HEIGHT = 8;

    private static final float MOVEMENT_SPEED = 0.4f;
    private static final int RANDOM_TREE_COUNT = 10;
    private static final int AI_TANK_COUNT = 4;
    private static final boolean USE_FILE_LEVEL = true;
    private static final String LEVEL_MAP = "level.tmx";
    private static final String LEVEL_GRID = "level.txt";

    private GameSetup() {
    }

    public static GameLoop create() {
        Level level = createLevelGenerator().generate();
        CollisionDetector collisions = new MovementCollisionDetector(
                level.getWidth(),
                level.getHeight(),
                new ObstacleCollisionDetector(level.getObstacles()),
                level.getObjects());
        Random random = new Random();

        Tank player = new Tank(level.getPlayerStart(), MOVEMENT_SPEED, collisions, level);
        KeyboardHandler keyboard = new KeyboardHandler(new GdxKeyInput(Gdx.input));
        TankControls.bindMovement(keyboard, player);
        TankControls.bindShooting(keyboard, player);

        List<Controller> controllers = new ArrayList<Controller>();
        controllers.add(keyboard);
        List<GridPoint2> aiStarts = new RandomTankGenerator(AI_TANK_COUNT, random).generate(level);
        List<Tank> aiTanks = new ArrayList<Tank>();
        for (GridPoint2 start : aiStarts) {
            Tank ai = new Tank(start, MOVEMENT_SPEED, collisions, level);
            aiTanks.add(ai);
            controllers.add(new AiTankController(ai, random));
        }

        GameRenderer gameRenderer = new GameRenderer(LEVEL_MAP);
        level.addListener(gameRenderer);
        for (GameObject obstacle : level.getObstacles()) {
            level.add(obstacle);
        }
        level.add(player);
        for (Tank ai : aiTanks) {
            level.add(ai);
        }

        List<Updatable> updatables = new ArrayList<Updatable>();
        updatables.add(level);
        return new GameLoop(gameRenderer, controllers, updatables);
    }

    private static LevelGenerator createLevelGenerator() {
        if (USE_FILE_LEVEL) {
            return new TextLevelGenerator(readLines(LEVEL_GRID));
        }
        return new RandomLevelGenerator(MAP_WIDTH, MAP_HEIGHT, RANDOM_TREE_COUNT, new Random());
    }

    private static List<String> readLines(String resource) {
        InputStream stream = GameSetup.class.getClassLoader().getResourceAsStream(resource);
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
