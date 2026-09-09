package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;

import java.util.EnumMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

public class KeyboardTankController implements Controller {
    private final Tank tank;
    private final Input input;
    private final Map<Direction, int[]> keyBindings;

    public KeyboardTankController(Tank tank, Input input) {
        this.tank = tank;
        this.input = input;
        this.keyBindings = createDefaultKeyBindings();
    }

    @Override
    public void update() {
        for (Map.Entry<Direction, int[]> binding : keyBindings.entrySet()) {
            if (isAnyKeyPressed(binding.getValue())) {
                tank.tryMove(binding.getKey());
            }
        }
    }

    private boolean isAnyKeyPressed(int[] keys) {
        for (int key : keys) {
            if (input.isKeyPressed(key)) {
                return true;
            }
        }
        return false;
    }

    private static Map<Direction, int[]> createDefaultKeyBindings() {
        Map<Direction, int[]> bindings = new EnumMap<Direction, int[]>(Direction.class);
        bindings.put(Direction.UP, new int[]{UP, W});
        bindings.put(Direction.LEFT, new int[]{LEFT, A});
        bindings.put(Direction.DOWN, new int[]{DOWN, S});
        bindings.put(Direction.RIGHT, new int[]{RIGHT, D});
        return bindings;
    }
}
