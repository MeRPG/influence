package com.teremok.influence.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.model.player.PlayerType;
import com.teremok.influence.util.Logger;

import javax.xml.bind.annotation.XmlElement;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    public static int lastAboutScreen = 0;

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
                    .element("debug", true);


            saveGameSettings(root);

            root.pop();

            xml.flush();
            fileWriter.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void saveGameSettings(XmlWriter root) throws IOException{
        XmlWriter gameXml = root.element("game");
        gameXml
            .element("players", gameSettings.getNumberOfPlayers())
            .element("difficulty", gameSettings.difficulty)
            .element("fieldSize", gameSettings.fieldSize);
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
                   /*
                myString = getElementText(root, "debug");
                if (! myString.isEmpty()) {
                    debug = Boolean.parseBoolean(myString);
                }*/

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

            if (root.toString().equals("<match/>")){
                return;
            }

            XmlReader.Element settingsXml = root.getChildByName("game");
            gameSettings = new GameSettings();
            FieldSize size =  FieldSize.valueOf(settingsXml.getChildByName("fieldSize").getText());
            gameSettings.setSize(size);
            gameSettings.difficulty =  GameDifficulty.valueOf(settingsXml.getChildByName("difficulty").getText());
            int playersNumber = settingsXml.getInt("players",5);
            if (playersNumber < 2 || playersNumber > 5)
                playersNumber = 5;
            gameSettings.players = GameSettings.getPlayersByDifficulty(gameSettings.difficulty, playersNumber);


            try {
                Integer number;
                String type;
                Map<Integer, PlayerType> players = new HashMap<Integer, PlayerType>();

                XmlReader.Element playersRoot = settingsXml.getChildByName("customPlayers");
                for (XmlReader.Element player : playersRoot.getChildrenByName("player")) {
                    number = Integer.parseInt(player.getAttribute("number", "0"));
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
        debug = true;
        gameSettings = GameSettings.getDefault();
        Localizator.setDefaultLanguage();
    }

    public static void init() {
        reset();
        if (! load())
            reset();
    }
}
