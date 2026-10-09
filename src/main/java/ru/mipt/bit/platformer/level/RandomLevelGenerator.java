package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class RandomLevelGenerator implements LevelGenerator {
    private final int width;
    private final int height;
    private final int obstacleCount;
    private final Random random;

    public RandomLevelGenerator(int width, int height, int obstacleCount, Random random) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Map size must be positive");
        }
        if (obstacleCount < 0 || obstacleCount >= width * height) {
            throw new IllegalArgumentException("Need at least one free cell for the player");
        }
        this.width = width;
        this.height = height;
        this.obstacleCount = obstacleCount;
        this.random = random;
    }

    @Override
    public Level generate() {
        List<GridPoint2> cells = new ArrayList<GridPoint2>(width * height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells.add(new GridPoint2(x, y));
            }
        }
        Collections.shuffle(cells, random);

        List<GameObject> obstacles = new ArrayList<GameObject>(obstacleCount);
        for (int i = 0; i < obstacleCount; i++) {
            obstacles.add(new Tree(cells.get(i)));
        }
        return new Level(cells.get(obstacleCount), obstacles);
    }
}
