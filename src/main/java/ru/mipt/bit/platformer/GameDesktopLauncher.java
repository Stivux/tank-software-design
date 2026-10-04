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
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.ObstacleCollisionDetector;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.model.Updatable;
import ru.mipt.bit.platformer.renderer.GameRenderer;
import ru.mipt.bit.platformer.renderer.MovableGraphic;
import ru.mipt.bit.platformer.renderer.ObstacleGraphic;
import ru.mipt.bit.platformer.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public class GameDesktopLauncher implements ApplicationListener {
    private static final float MOVEMENT_SPEED = 0.4f;
    private static final String LEVEL_PATH = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";

    private Renderer renderer;
    private final List<Controller> controllers = new ArrayList<Controller>();
    private final List<Updatable> updatables = new ArrayList<Updatable>();

    @Override
    public void create() {
        Tree tree = new Tree(new GridPoint2(1, 3));
        List<GameObject> obstacles = new ArrayList<GameObject>();
        obstacles.add(tree);

        CollisionDetector collisions = new ObstacleCollisionDetector(obstacles);
        Tank tank = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED, collisions);
        KeyboardHandler keyboard = new KeyboardHandler(new GdxKeyInput(Gdx.input));
        TankControls.bindMovement(keyboard, tank);
        controllers.add(keyboard);
        updatables.add(tank);

        GameRenderer gameRenderer = new GameRenderer(LEVEL_PATH);
        gameRenderer.add(new MovableGraphic(tank, gameRenderer.createGraphic(TANK_TEXTURE), gameRenderer.getTileMovement()));
        gameRenderer.add(new ObstacleGraphic(tree, gameRenderer.createGraphic(TREE_TEXTURE), gameRenderer.getGroundLayer()));
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
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
