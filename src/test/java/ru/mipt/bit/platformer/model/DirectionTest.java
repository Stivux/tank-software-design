package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @Test
    void storesGridVectorAndRotation() {
        assertEquals(new GridPoint2(0, 1), Direction.UP.getVector());
        assertEquals(90f, Direction.UP.getRotation(), 0f);

        assertEquals(new GridPoint2(-1, 0), Direction.LEFT.getVector());
        assertEquals(-180f, Direction.LEFT.getRotation(), 0f);

        assertEquals(new GridPoint2(0, -1), Direction.DOWN.getVector());
        assertEquals(-90f, Direction.DOWN.getRotation(), 0f);

        assertEquals(new GridPoint2(1, 0), Direction.RIGHT.getVector());
        assertEquals(0f, Direction.RIGHT.getRotation(), 0f);
    }

    @Test
    void nextPositionShiftsByVectorWithoutChangingSource() {
        GridPoint2 from = new GridPoint2(3, 2);

        assertEquals(new GridPoint2(3, 3), Direction.UP.addDirectionDelta(from));
        assertEquals(new GridPoint2(2, 2), Direction.LEFT.addDirectionDelta(from));
        assertEquals(new GridPoint2(3, 1), Direction.DOWN.addDirectionDelta(from));
        assertEquals(new GridPoint2(4, 2), Direction.RIGHT.addDirectionDelta(from));
        assertEquals(new GridPoint2(3, 2), from);
    }

    @Test
    void returnedVectorIsIndependentFromTheEnum() {
        GridPoint2 vector = Direction.LEFT.getVector();
        vector.set(5, 5);

        assertEquals(new GridPoint2(-1, 0), Direction.LEFT.getVector());
    }
}
