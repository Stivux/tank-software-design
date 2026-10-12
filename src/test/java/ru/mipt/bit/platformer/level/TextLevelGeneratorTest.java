package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GameObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileLevelGeneratorTest {

    @Test
    void readsTreesAndPlayerFromGrid() {
        Level level = new TextLevelGenerator(Arrays.asList(
                "__TT_T_TTT",
                "TT___X____",
                "TTTT___T_T",
                "__________")).generate();

        assertEquals(new GridPoint2(5, 2), level.getPlayerStart());
        assertEquals(Set.of(
                cell(2, 3), cell(3, 3), cell(5, 3), cell(7, 3), cell(8, 3), cell(9, 3),
                cell(0, 2), cell(1, 2),
                cell(0, 1), cell(1, 1), cell(2, 1), cell(3, 1), cell(7, 1), cell(9, 1)),
                coordinatesOf(level.getObstacles()));
    }

    @Test
    void firstRowIsTheTopOfTheGrid() {
        Level level = new TextLevelGenerator(Arrays.asList(
                "X_",
                "T_")).generate();

        assertEquals(new GridPoint2(0, 1), level.getPlayerStart());
        assertEquals(treeSet(cell(0, 0)), coordinatesOf(level.getObstacles()));
    }

    @Test
    void rejectsMissingPlayer() {
        assertThrows(IllegalArgumentException.class, () -> new TextLevelGenerator(Arrays.asList("__", "TT")));
    }

    @Test
    void rejectsSecondPlayer() {
        assertThrows(IllegalArgumentException.class, () -> new TextLevelGenerator(Arrays.asList("X_", "_X")));
    }

    @Test
    void rejectsUnevenRows() {
        assertThrows(IllegalArgumentException.class, () -> new TextLevelGenerator(Arrays.asList("X__", "T")));
    }

    @Test
    void rejectsUnknownCells() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLevelGenerator(Arrays.asList("__TT_TITTT", "TT___X____")));
    }

    private static Set<GridPoint2> coordinatesOf(List<GameObject> objects) {
        Set<GridPoint2> cells = new HashSet<GridPoint2>();
        for (GameObject object : objects) {
            assertTrue(cells.add(new GridPoint2(object.getCoordinates())));
        }
        return cells;
    }

    private static Set<GridPoint2> treeSet(GridPoint2... cells) {
        return new HashSet<>(Arrays.asList(cells));
    }

    private static GridPoint2 cell(int x, int y) {
        return new GridPoint2(x, y);
    }
}
