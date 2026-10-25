package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTest {

    @Test
    void keepsACopyOfTheGivenCoordinates() {
        GridPoint2 given = new GridPoint2(4, 5);
        Tree tree = new Tree(given);

        given.set(0, 0);

        assertEquals(new GridPoint2(4, 5), tree.getCoordinates());
    }
}
