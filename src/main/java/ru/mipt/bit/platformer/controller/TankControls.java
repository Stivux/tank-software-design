package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

public final class TankControls {
    private TankControls() {
    }

    public static void bindMovement(KeyBindings bindings, Movable tank) {
        bindings.addHandler(new MoveTankAction(tank, Direction.UP), UP, W);
        bindings.addHandler(new MoveTankAction(tank, Direction.LEFT), LEFT, A);
        bindings.addHandler(new MoveTankAction(tank, Direction.DOWN), DOWN, S);
        bindings.addHandler(new MoveTankAction(tank, Direction.RIGHT), RIGHT, D);
    }
}
