package com.teremok.influence.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.teremok.influence.model.*;
import com.teremok.influence.util.Logger;

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
            Logger.log(handle.file().getAbsolutePath());
            XmlWriter xml = new XmlWriter(fileWriter);
            XmlWriter root = xml.element("settings");

            root.element("sound", settings.sound)
                    .element("vibrate", settings.vibrate)
                    .element("speed", settings.speed)
                    .element("language", Localizator.getLanguage())
                    .element("debug", settings.debug)
                    .element("lastAboutScreen", settings.lastAboutScreen);

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
        Logger.log("checkDirs...");
        if (! setting.exists()) {
            Logger.log("creating new root directory");

            setting.mkdirs();
            Gdx.files.external(DIR+"/atlas").mkdirs();
            Gdx.files.external(DIR+"/misc").mkdirs();
            Gdx.files.external(DIR+"/ui").mkdirs();
            Gdx.files.external(DIR+"/missions").mkdirs();
        }
    }


}
