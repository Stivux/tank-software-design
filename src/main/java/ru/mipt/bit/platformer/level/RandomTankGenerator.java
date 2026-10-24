package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class RandomTankGenerator {
    private final int tankCount;
    private final Random random;

    public RandomTankGenerator(int tankCount, Random random) {
        if (tankCount < 0) {
            throw new IllegalArgumentException("Tank count must be non-negative");
        }
        this.tankCount = tankCount;
        this.random = random;
    }

    public List<GridPoint2> generate(Level level) {
        List<GridPoint2> free = freeCells(level);
        if (free.size() < tankCount) {
            throw new IllegalArgumentException("Not enough free cells for tanks");
        }
        Collections.shuffle(free, random);
        return new ArrayList<GridPoint2>(free.subList(0, tankCount));
    }

    private static List<GridPoint2> freeCells(Level level) {
        boolean[][] taken = new boolean[level.getWidth()][level.getHeight()];
        for (GameObject obstacle : level.getObstacles()) {
            GridPoint2 cell = obstacle.getCoordinates();
            taken[cell.x][cell.y] = true;
        }
        GridPoint2 player = level.getPlayerStart();
        taken[player.x][player.y] = true;

        List<GridPoint2> free = new ArrayList<GridPoint2>();
        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                if (!taken[x][y]) {
                    free.add(new GridPoint2(x, y));
                }
            }
        }
        return free;
    }
}
