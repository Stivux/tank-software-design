package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GameObject;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelGeneratorTest {

    @Test
    void placesUniqueTreesAndAFreePlayer() {
        Level level = new RandomLevelGenerator(4, 3, 5, new Random(1L)).generate();

        assertEquals(4, level.getWidth());
        assertEquals(3, level.getHeight());
        assertEquals(5, level.getObstacles().size());
        Set<GridPoint2> occupied = new HashSet<GridPoint2>();
        for (GameObject obstacle : level.getObstacles()) {
            GridPoint2 cell = obstacle.getCoordinates();
            assertTrue(inBounds(cell, 4, 3));
            assertTrue(occupied.add(new GridPoint2(cell)));
        }
        GridPoint2 player = level.getPlayerStart();
        assertTrue(inBounds(player, 4, 3));
        assertTrue(occupied.add(new GridPoint2(player)));
    }

    @Test
    void sameSeedProducesTheSameLayout() {
        Level first = new RandomLevelGenerator(5, 5, 8, new Random(42L)).generate();
        Level second = new RandomLevelGenerator(5, 5, 8, new Random(42L)).generate();

        assertEquals(first.getPlayerStart(), second.getPlayerStart());
        assertEquals(coordinates(first), coordinates(second));
    }

    @Test
    void rejectsAFullMapOfTrees() {
        assertThrows(IllegalArgumentException.class, () ->
                new RandomLevelGenerator(2, 2, 4, new Random()));
    }

    private static boolean inBounds(GridPoint2 cell, int width, int height) {
        return cell.x >= 0 && cell.x < width && cell.y >= 0 && cell.y < height;
    }

    private static Set<GridPoint2> coordinates(Level level) {
        Set<GridPoint2> cells = new HashSet<GridPoint2>();
        for (GameObject obstacle : level.getObstacles()) {
            cells.add(new GridPoint2(obstacle.getCoordinates()));
        }
        return cells;
    }
}
