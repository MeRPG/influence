package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.teremok.influence.model.Settings;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Алексей on 15.05.2014
 */
public class AboutScreenChecker implements Runnable {

    public static void check(Settings settings) {
        Thread thread = new Thread( new AboutScreenChecker(settings));
        thread.start();
    }

    Settings settings;

    private AboutScreenChecker(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void run() {
        try {
            FileHandle fileHandle = Gdx.files.external("/.influence/atlas/test.txt");
            URL url = new URL("http://timeforlime.ru/influence/test.txt");
            InputStream is = url.openStream();
            fileHandle.write(is,false);
            is.close();

            int i = Integer.parseInt(new String(fileHandle.readBytes()));
            if (i != settings.lastAboutScreen) {
                settings.lastAboutScreen = 0;


                fileHandle = Gdx.files.external("/.influence/atlas/aboutScreen.pack");
                url = new URL("http://timeforlime.ru/influence/aboutScreen.pack");
                is = url.openStream();
                fileHandle.write(is,false);
                is.close();

                fileHandle = Gdx.files.external("/.influence/atlas/aboutScreen2.png");
                url = new URL("http://timeforlime.ru/influence/aboutScreen2.png");
                is = url.openStream();
                fileHandle.write(is,false);
                is.close();

                fileHandle = Gdx.files.external("/.influence/atlas/aboutScreen.png");
                url = new URL("http://timeforlime.ru/influence/aboutScreen.png");
                is = url.openStream();
                fileHandle.write(is,false);
                is.close();

                settings.lastAboutScreen = i;

                Gdx.app.debug(getClass().getSimpleName(), "new about screen: " + i);
            } else {
                Gdx.app.debug(getClass().getSimpleName(), "no new about screen :( ");
            }

        } catch (Exception ex) {
            Gdx.app.debug(getClass().getSimpleName(), "Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
