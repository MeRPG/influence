package com.teremok.influence.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.teremok.influence.util.Logger;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Alexx on 07.02.14
 */
public class Settings {

    public static final float FAST = 0.25f;
    public static final float NORMAL = 0.4f;
    public static final float SLOW = 0.6f;

    public static boolean sound;
    public static boolean vibrate;
    public static float speed;

    public static boolean debug = true;

    public static GameSettings gameSettings;

    private static final String FILENAME = ".influence-settings";

    public static void save() {
        FileHandle handle = Gdx.files.external(FILENAME);
        try {
            FileWriter fileWriter = new FileWriter(handle.file());
            Logger.log(handle.file().getAbsolutePath());
            XmlWriter xml = new XmlWriter(fileWriter);
            XmlWriter root = xml.element("settings");

            root.element("sound", sound)
                    .element("vibrate", vibrate)
                    .element("speed", speed)
                    .element("language", Localizator.getLanguage())
                    .element("debug", debug);


            saveGameSettings(root);

            root.pop();

            xml.flush();
            fileWriter.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void saveGameSettings(XmlWriter root) throws IOException{
        root.element("game")
                .element("players", gameSettings.getNumberOfPlayers())
                .element("difficulty", gameSettings.difficulty)
                .element("fieldSize", gameSettings.fieldSize)
                .pop();

    }

    public static boolean load() {
        FileHandle handle = Gdx.files.external(FILENAME);
        if (handle.exists()) {
            try{
                XmlReader reader = new XmlReader();
                String myString;

                XmlReader.Element root = reader.parse(handle.reader());
                myString = root.getChildByName("sound").getText();
                sound = Boolean.parseBoolean(myString);

                myString = root.getChildByName("vibrate").getText();
                vibrate = Boolean.parseBoolean(myString);

                myString = root.getChildByName("speed").getText();
                speed = Float.parseFloat(myString);

                myString = root.getChildByName("language").getText();
                Localizator.setLanguage(myString);

                myString = getElementText(root, "debug");
                if (! myString.isEmpty()) {
                    debug = Boolean.parseBoolean(myString);
                }

                loadGameSettings(root);

                return true;
            } catch (IOException exception) {
                exception.printStackTrace();
                return false;
            } catch (RuntimeException exception) {
                exception.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static void loadGameSettings(XmlReader.Element root) {
        try {
            XmlReader.Element settingsXml = root.getChildByName("game");
            gameSettings = new GameSettings();
            FieldSize size =  FieldSize.valueOf(settingsXml.getChildByName("fieldSize").getText());
            gameSettings.setSize(size);
            gameSettings.difficulty =  GameDifficulty.valueOf(settingsXml.getChildByName("difficulty").getText());
            int playersNumber = settingsXml.getInt("players",5);
            gameSettings.players = GameSettings.createSingleplayerFromDifficulty(gameSettings.difficulty, playersNumber);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (gameSettings == null || gameSettings.players == null || gameSettings.difficulty == null || gameSettings.fieldSize == null) {
            gameSettings = GameSettings.getDefault();
        }

    }

    private static String getElementText(XmlReader.Element root, String elementName) {
        XmlReader.Element element = root.getChildByName(elementName);
        if (element != null) {
            return element.getText();
        }
        return "";
    }

    public static void reset() {
        sound = true;
        vibrate = true;
        speed = NORMAL;
        debug = false;
        gameSettings = GameSettings.getDefault();
        Localizator.setDefaultLanguage();
    }

    public static void init() {
        reset();
        if (! load())
            reset();
    }
}
