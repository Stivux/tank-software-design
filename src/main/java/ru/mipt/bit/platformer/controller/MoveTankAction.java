package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

public class MoveTankAction implements Action {
    private final Movable movable;
    private final Direction direction;

    public MoveTankAction(Movable movable, Direction direction) {
        this.movable = movable;
        this.direction = direction;
    }

    @Override
    public void execute() {
        movable.tryMove(direction);
    }
}
