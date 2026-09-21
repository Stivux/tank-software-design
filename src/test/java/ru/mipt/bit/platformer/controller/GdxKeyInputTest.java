package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GdxKeyInputTest {

    @Test
    void delegatesKeyStateToGdxInput() {
        GdxKeyInput keys = new GdxKeyInput(pressed(SPACE));

        assertTrue(keys.isKeyPressed(SPACE));
        assertFalse(keys.isKeyPressed(UP));
    }

    private static Input pressed(final int pressedKey) {
        return (Input) Proxy.newProxyInstance(
                Input.class.getClassLoader(),
                new Class<?>[]{Input.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) {
                        if ("isKeyPressed".equals(method.getName())) {
                            return pressedKey == (Integer) args[0];
                        }
                        if (method.getReturnType() == boolean.class) {
                            return false;
                        }
                        if (method.getReturnType() == int.class) {
                            return 0;
                        }
                        if (method.getReturnType() == float.class) {
                            return 0f;
                        }
                        return null;
                    }
                });
    }
}
