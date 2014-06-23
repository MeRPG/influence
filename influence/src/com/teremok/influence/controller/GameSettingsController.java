package com.teremok.influence.controller;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.teremok.influence.model.FieldSize;
import com.teremok.influence.model.GameDifficulty;
import com.teremok.influence.model.GameSettings;
import com.teremok.influence.model.player.PlayerType;
import com.teremok.influence.util.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 23.06.2014
 */
public class GameSettingsController {
    public void saveGameSettings(GameSettings gameSettings, XmlWriter root) throws IOException {
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

    public GameSettings loadGameSettings(XmlReader.Element root) {

        GameSettings gameSettings = GameSettings.getDefault();
        try {

            if (root.toString().equals("<match/>")){
                return gameSettings;
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
            gameSettings = GameSettings.getDefault();
        }
        return gameSettings;
    }
}
