package com.teremok.influence.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.teremok.influence.model.Settings;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by Алексей on 15.05.2014
 */
public class AboutScreenChecker implements Runnable {

    public static void check() {
        Thread thread = new Thread( new AboutScreenChecker());
        thread.start();
    }

    private AboutScreenChecker() {
    }

    @Override
    public void run() {
        try {
            Logger.log("start about screen check");
            FileHandle fileHandle = Gdx.files.external("/.influence/atlas/test.txt");
            URL website = new URL("http://timeforlime.ru/influence/test.txt");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(fileHandle.file());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            fos.close();
            rbc.close();

            int i = Integer.parseInt(new String(fileHandle.readBytes()));
            if (i > Settings.lastAboutScreen) {
                fileHandle = Gdx.files.external("/.influence/atlas/aboutScreen.png");
                website = new URL("http://timeforlime.ru/influence/aboutScreen.png");
                rbc = Channels.newChannel(website.openStream());
                fos = new FileOutputStream(fileHandle.file());
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                fos.close();
                rbc.close();

                fileHandle = Gdx.files.external("/.influence/atlas/aboutScreen.pack");
                website = new URL("http://timeforlime.ru/influence/aboutScreen.pack");
                rbc = Channels.newChannel(website.openStream());
                fos = new FileOutputStream(fileHandle.file());
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                fos.close();
                rbc.close();

                Settings.lastAboutScreen = i;

                Logger.log("new about screen: " + i);
            } else {
                Logger.log("no new about screen :( ");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
