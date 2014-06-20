package com.teremok.influence.test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.teremok.influence.Influence;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.Locale;

/**
 * Created by Алексей on 12.06.2014
 */
public class LibGDXTest {

    static LwjglApplication app;

    @BeforeClass
    public static void runApp() throws InterruptedException {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Influence";
        cfg.useGL30 = false;
        cfg.width = 480;
        cfg.height = 720;

        Locale locale = Locale.getDefault(Locale.Category.DISPLAY);

        app = new LwjglApplication(new Influence(locale), cfg);
        Thread.sleep(2000);
    }

    @AfterClass
    public static void exitApp() {
        app.exit();
    }
}
