package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class GameDesktopLauncher implements ApplicationListener {
    private static final int TILE_SIZE = 128;

    private GameLoop loop;

    @Override
    public void create() {
        loop = GameSetup.create();
    }

    @Override
    public void render() {
        loop.tick(Gdx.graphics.getDeltaTime());
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
        loop.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(GameSetup.MAP_WIDTH * TILE_SIZE, GameSetup.MAP_HEIGHT * TILE_SIZE);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
