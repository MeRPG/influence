package com.teremok.influence.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.teremok.influence.model.*;
import com.teremok.influence.model.player.PlayerType;
import com.teremok.influence.util.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

            saveGameSettings(settings.gameSettings, root);

            root.pop();

            xml.flush();
            fileWriter.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void saveGameSettings(GameSettings gameSettings, XmlWriter root) throws IOException{
        XmlWriter gameXml = root.element("game");
        gameXml
                .element("players", gameSettings.getNumberOfPlayers())
                .element("difficulty", gameSettings.difficulty)
                .element("fieldSize", gameSettings.fieldSize)
                .element("gameForInfluence", gameSettings.gameForInfluence);
        XmlWriter playersXml = gameXml.element("customPlayers");
        int num = 0;
        for (PlayerType player : gameSettings.customPlayers.values()) {
            playersXml.element("player")
                    .attribute("number", num++)
                    .text(player)
                    .pop();
        }
        playersXml.pop();
        gameXml.pop();
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

                loadGameSettings(settings, root);

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

    public void loadGameSettings(Settings settings, XmlReader.Element root) {

        GameSettings gameSettings = settings.gameSettings;
        try {

            if (root.toString().equals("<match/>")){
                return;
            }


            XmlReader.Element settingsXml = root.getChildByName("game");
            FieldSize size =  FieldSize.valueOf(settingsXml.getChildByName("fieldSize").getText());
            gameSettings.setSize(size);
            gameSettings.difficulty =  GameDifficulty.valueOf(settingsXml.getChildByName("difficulty").getText());
            gameSettings.gameForInfluence = settingsXml.getBoolean("gameForInfluence", false);
            int playersNumber = settingsXml.getInt("players",5);
            if (playersNumber < 2 || playersNumber > 5)
                playersNumber = 5;
            gameSettings.players = GameSettings.getPlayersByDifficulty(gameSettings.difficulty, playersNumber);


            try {
                Integer number;
                String type;
                Map<Integer, PlayerType> players = new HashMap<>();

                XmlReader.Element playersRoot = settingsXml.getChildByName("customPlayers");
                for (XmlReader.Element player : playersRoot.getChildrenByName("player")) {
                    number = player.getIntAttribute("number", 0);
                    type = player.getText();
                    players.put(number, PlayerType.valueOf(type));
                    Logger.log("adding customPlayer " + type + " with number " + number);
                }

                gameSettings.customPlayers = players;

            } catch (Exception ex) {
                ex.printStackTrace();
                gameSettings.difficulty = GameDifficulty.NORMAL;
                gameSettings.customPlayers = gameSettings.getPlayers(GameDifficulty.CUSTOM, 5);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (gameSettings == null || gameSettings.players == null || gameSettings.difficulty == null || gameSettings.fieldSize == null) {
            settings.gameSettings = GameSettings.getDefault();
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
