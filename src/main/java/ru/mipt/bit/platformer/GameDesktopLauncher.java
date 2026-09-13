package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.controller.Controller;
import ru.mipt.bit.platformer.controller.KeyboardTankController;
import ru.mipt.bit.platformer.model.CollisionDetector;
import ru.mipt.bit.platformer.model.ObstacleCollisionDetector;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.renderer.GameRenderer;

import java.util.Collections;

public class GameDesktopLauncher implements ApplicationListener {
    private static final float MOVEMENT_SPEED = 0.4f;

    private GameRenderer renderer;
    private Tank tank;
    private Controller tankController;

    @Override
    public void create() {
        Tree tree = new Tree(new GridPoint2(1, 3));
        CollisionDetector collisions = new ObstacleCollisionDetector(Collections.singletonList(tree));

        tank = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED);
        tankController = new KeyboardTankController(tank, collisions);
        renderer = new GameRenderer(Collections.singletonList(tree));
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        tankController.update();
        tank.update(deltaTime);
        renderer.render(tank);
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
