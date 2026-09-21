package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.controller.Controller;
import ru.mipt.bit.platformer.controller.GdxKeyInput;
import ru.mipt.bit.platformer.controller.KeyboardHandler;
import ru.mipt.bit.platformer.controller.TankControls;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.ObstacleCollisionDetector;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.renderer.GameRenderer;
import ru.mipt.bit.platformer.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public class GameDesktopLauncher implements ApplicationListener {
    private static final float MOVEMENT_SPEED = 0.4f;
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";

    private Renderer renderer;
    private Tank tank;
    private Controller controller;

    @Override
    public void create() {
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        obstacles.add(new Tree(new GridPoint2(1, 3)));

        CollisionDetector collisions = new ObstacleCollisionDetector(obstacles);
        tank = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED, collisions);
        KeyboardHandler keyboard = new KeyboardHandler(new GdxKeyInput(Gdx.input));
        TankControls.bindMovement(keyboard, tank);
        controller = keyboard;

        GameRenderer gameRenderer = new GameRenderer();
        gameRenderer.register(tank, TANK_TEXTURE);
        for (Obstacle obstacle : obstacles) {
            gameRenderer.register(obstacle, TREE_TEXTURE);
        }
        renderer = gameRenderer;
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        controller.update();
        renderer.render();
        tank.update(deltaTime);
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
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
