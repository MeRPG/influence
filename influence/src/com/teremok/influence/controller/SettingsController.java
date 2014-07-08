package com.teremok.influence.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.teremok.influence.model.Settings;
import com.teremok.framework.util.Localizator;
import com.teremok.influence.view.Animation;

import java.io.FileWriter;
import java.io.IOException;

import static com.teremok.influence.util.IOConstants.DIR;
import static com.teremok.influence.util.IOConstants.SETTINGS_PATH;

/**
 * Created by Алексей on 19.05.2014
 */
public class SettingsController {


    public void save(Settings settings) {
        FileHandle handle = Gdx.files.external(SETTINGS_PATH);
        try {
            FileWriter fileWriter = new FileWriter(handle.file());
            Gdx.app.debug(getClass().getSimpleName(), handle.file().getAbsolutePath());
            XmlWriter xml = new XmlWriter(fileWriter);
            XmlWriter root = xml.element("settings");

            root.element("sound", settings.sound)
                    .element("vibrate", settings.vibrate)
                    .element("speed", settings.speed)
                    .element("language", Localizator.getLanguage())
                    .element("debug", settings.debug)
                    .element("lastAboutScreen", settings.lastAboutScreen);

            XmlWriter animation = root.element("animation");
            animation.element("short", Animation.DURATION_SHORT)
                        .element("normal", Animation.DURATION_NORMAL)
                        .element("long", Animation.DURATION_LONG);
            animation.pop();

            GameSettingsController controller = new GameSettingsController();
            controller.saveGameSettings(settings.gameSettings, root);

            root.pop();

            xml.flush();
            fileWriter.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Settings load() {
        return load(new Settings());
    }

    public Settings load(Settings settings) {
        SettingsController.checkDirs();
        FileHandle handle = Gdx.files.external(SETTINGS_PATH);
        if (handle.exists()) {
            try{
                XmlReader reader = new XmlReader();
                XmlReader.Element root = reader.parse(handle.reader());

                settings.sound = root.getBoolean("sound", true);
                settings.vibrate = root.getBoolean("vibrate", true);
                settings.speed = root.getFloat("speed", 0.5f);
                Localizator.setLanguage(root.getChildByName("language").getText());
                settings.debug = root.getBoolean("debug", true);
                settings.lastAboutScreen = root.getInt("lastAboutScreen", 0);

                Animation.DURATION_SHORT = root.getChildByName("animation").getFloat("short", Animation.DEFAULT_DURATION_SHORT);
                Animation.DURATION_NORMAL = root.getChildByName("animation").getFloat("normal", Animation.DEFAULT_DURATION_NORMAL);
                Animation.DURATION_LONG = root.getChildByName("animation").getFloat("long", Animation.DEFAULT_DURATION_LONG);

                GameSettingsController controller = new GameSettingsController();
                settings.gameSettings = controller.loadGameSettings(root);

                return settings;
            } catch (IOException exception) {
                exception.printStackTrace();
                return settings.reset();
            } catch (RuntimeException exception) {
                exception.printStackTrace();
                return settings.reset();
            }
        } else {
            return settings.reset();
        }
    }

    public static void checkDirs() {
        FileHandle setting = Gdx.files.external(DIR);
        Gdx.app.debug("SettingsController", "checkDirs...");
        if (! setting.exists()) {
            Gdx.app.debug("SettingsController", "creating new root directory");

            setting.mkdirs();
            Gdx.files.external(DIR+"/atlas").mkdirs();
            Gdx.files.external(DIR+"/misc").mkdirs();
            Gdx.files.external(DIR+"/ui").mkdirs();
            Gdx.files.external(DIR+"/missions").mkdirs();
        }
    }


}
