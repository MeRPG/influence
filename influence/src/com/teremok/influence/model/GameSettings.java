package com.teremok.influence.model;

import com.teremok.influence.model.player.PlayerType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexx on 27.02.14
 */
public class GameSettings {
    public Map<Integer, PlayerType> players;
    GameDifficulty difficulty;

    public static GameSettings createSingleplayerFromDifficulty(GameDifficulty difficulty, int playersNumber) {
        GameSettings settings;
        switch (difficulty){
            case EASY:
                settings = singlePlayerEasy(playersNumber);
                break;
            case NORMAL:
                settings = singlePlayerNormal(playersNumber);
                break;
            case HARD:
                settings = singlePlayerHard(playersNumber);
                break;
            case INSANE:
                settings = singlePlayerInsane(playersNumber);
                break;
            default:
                settings = singlePlayerNormal(playersNumber);
        }
        settings.difficulty = difficulty;
        return settings;
    }

    public static GameSettings singlePlayerEasy(int playersNumber) {
        GameSettings gameSettings = new GameSettings();
        Map<Integer, PlayerType> players = new HashMap<Integer, PlayerType>();

        switch (playersNumber) {
            case 2:
                players.put(0, PlayerType.Human);
                players.put(1, PlayerType.Dummy);
                break;
            case 3:
                players.put(0, PlayerType.Human);
                players.put(1, PlayerType.Dummy);
                players.put(2, PlayerType.Lazy);
                break;
            case 4:
                players.put(0, PlayerType.Human);
                players.put(1, PlayerType.Dummy);
                players.put(2, PlayerType.Lazy);
                players.put(3, PlayerType.Beefy);
                break;
            case 5:
                players.put(0, PlayerType.Human);
                players.put(1, PlayerType.Dummy);
                players.put(2, PlayerType.Dummy);
                players.put(3, PlayerType.Lazy);
                players.put(4, PlayerType.Beefy);
                break;
            default:
                players.put(0, PlayerType.Human);
                players.put(1, PlayerType.Human);
                break;

        }
        gameSettings.players = players;
        return gameSettings;
    }

    public static GameSettings singlePlayerNormal(int playersNumber) {
        GameSettings gameSettings = new GameSettings();
        Map<Integer, PlayerType> players = new HashMap<Integer, PlayerType>();

        switch (playersNumber) {
            case 5:
                players.put(4, PlayerType.Beefy);
                break;
            case 4:
                players.put(3, PlayerType.Lazy);
                break;
            case 3:
                players.put(2, PlayerType.Dummy);
                break;
            case 2:
                players.put(1, PlayerType.Dummy);
                break;
            default:
                players.put(0, PlayerType.Human);
                break;

        }
        gameSettings.players = players;
        return gameSettings;
    }

    public static GameSettings singlePlayerHard(int playersNumber) {
        GameSettings gameSettings = new GameSettings();
        Map<Integer, PlayerType> players = new HashMap<Integer, PlayerType>();

        switch (playersNumber) {
            case 5:
                players.put(4, PlayerType.Hunter);
                break;
            case 4:
                players.put(3, PlayerType.Hunter);
                break;
            case 3:
                players.put(2, PlayerType.Smarty);
                break;
            case 2:
                players.put(1, PlayerType.Smarty);
                break;
            default:
                players.put(0, PlayerType.Human);
                break;

        }
        gameSettings.players = players;
        return gameSettings;
    }

    public static GameSettings singlePlayerInsane(int playersNumber) {
        GameSettings gameSettings = new GameSettings();
        Map<Integer, PlayerType> players = new HashMap<Integer, PlayerType>();

        switch (playersNumber) {
            case 5:
                players.put(4, PlayerType.Beefy);
                break;
            case 4:
                players.put(3, PlayerType.Lazy);
                break;
            case 3:
                players.put(2, PlayerType.Dummy);
                break;
            case 2:
                players.put(1, PlayerType.Dummy);
                break;
            default:
                players.put(0, PlayerType.Human);
                break;

        }
        gameSettings.players = players;
        return gameSettings;
    }
}
