package ru.mipt.bit.platformer.controller;

import ru.mipt.bit.platformer.model.Direction;

public interface MoveStrategy {
    Direction chooseDirection();
}
