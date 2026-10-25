package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Direction;

import java.util.Random;

public final class RandomMoveStrategy implements MoveStrategy {
    private final Random random;

    public RandomMoveStrategy(Random random) {
        this.random = random;
    }

    @Override
    public Direction chooseDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }
}
