package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Tree;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomTankGeneratorTest {

    @Test
    void placesDistinctTanksOnFreeCells() {
        Level level = level();
        List<GridPoint2> starts = new RandomTankGenerator(4, new Random(1L)).generate(level);

        assertEquals(4, starts.size());
        Set<GridPoint2> occupied = taken(level);
        for (GridPoint2 start : starts) {
            assertTrue(start.x >= 0 && start.x < level.getWidth() && start.y >= 0 && start.y < level.getHeight());
            assertTrue(occupied.add(new GridPoint2(start)));
        }
    }

    @Test
    void sameSeedPlacesTheSameTanks() {
        List<GridPoint2> first = new RandomTankGenerator(3, new Random(5L)).generate(level());
        List<GridPoint2> second = new RandomTankGenerator(3, new Random(5L)).generate(level());

        assertEquals(first, second);
    }

    @Test
    void zeroTanksPlacesNothing() {
        assertEquals(List.of(), new RandomTankGenerator(0, new Random(1L)).generate(level()));
    }

    @Test
    void rejectsANegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomTankGenerator(-1, new Random()));
    }

    @Test
    void rejectsMoreTanksThanFreeCells() {
        assertThrows(IllegalArgumentException.class, () -> new RandomTankGenerator(5, new Random(1L)).generate(level()));
    }

    private static Level level() {
        return new Level(3, 2, new GridPoint2(0, 0), List.of(new Tree(new GridPoint2(1, 0))));
    }

    private static Set<GridPoint2> taken(Level level) {
        Set<GridPoint2> cells = new HashSet<GridPoint2>();
        cells.add(level.getPlayerStart());
        cells.add(new GridPoint2(1, 0));
        return cells;
    }
}
