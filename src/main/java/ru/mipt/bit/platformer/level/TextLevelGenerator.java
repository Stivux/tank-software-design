package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;

public final class TextLevelGenerator implements LevelGenerator {
    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    private final Level level;

    public TextLevelGenerator(List<String> rows) {
        this.level = parse(trimTrailingEmptyRows(rows));
    }

    @Override
    public Level generate() {
        return level;
    }

    private static Level parse(List<String> rows) {
        int height = rows.size();
        int width = rows.get(0).length();
        GridPoint2 playerStart = null;
        List<GameObject> obstacles = new ArrayList<GameObject>();

        for (int row = 0; row < height; row++) {
            String line = rows.get(row);
            if (line.length() != width) {
                throw new IllegalArgumentException("Level rows must have equal length");
            }
            int y = height - 1 - row;
            for (int x = 0; x < width; x++) {
                char cell = line.charAt(x);
                if (cell == TREE) {
                    obstacles.add(new Tree(new GridPoint2(x, y)));
                } else if (cell == PLAYER) {
                    if (playerStart != null) {
                        throw new IllegalArgumentException("Level must contain exactly one player start");
                    }
                    playerStart = new GridPoint2(x, y);
                } else if (cell != EMPTY) {
                    throw new IllegalArgumentException("Unknown level cell: '" + cell + "'");
                }
            }
        }

        if (playerStart == null) {
            throw new IllegalArgumentException("Level must contain exactly one player start");
        }
        return new Level(width, height, playerStart, obstacles);
    }

    private static List<String> trimTrailingEmptyRows(List<String> rows) {
        int last = rows.size() - 1;
        while (last >= 0 && rows.get(last).isEmpty()) {
            last--;
        }
        if (last < 0) {
            throw new IllegalArgumentException("Level is empty");
        }
        return rows.subList(0, last + 1);
    }
}
