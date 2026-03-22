package com.example.abyssaldescent.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGL20;
import org.junit.jupiter.api.BeforeAll;

/**
 * Базовый класс для тестов, которым нужен инициализированный {@link Gdx}.
 */
public abstract class GdxTestBase {

    private static boolean initialized;

    @BeforeAll
    static void initGdx() {
        if (initialized) {
            return;
        }

        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() {}, config);

        // Для части API требуется GL, подменяем мок-реализацией.
        Gdx.gl = new MockGL20();
        Gdx.gl20 = (MockGL20) Gdx.gl;

        initialized = true;
    }
}
