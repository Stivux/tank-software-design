package ru.mipt.bit.platformer.controller;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomMoveStrategyTest {

    @Test
    void sameSeedPicksTheSameDirections() {
        MoveStrategy first = new RandomMoveStrategy(new Random(7L));
        MoveStrategy second = new RandomMoveStrategy(new Random(7L));

        for (int i = 0; i < 20; i++) {
            assertEquals(first.chooseDirection(), second.chooseDirection());
        }
    }
}
