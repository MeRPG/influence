package com.teremok.influence.model;

import com.teremok.influence.model.player.PlayerType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexx on 27.02.14
 */
public class GameSettings {
    public Map<Integer, PlayerType> players;
    public GameDifficulty difficulty;
    public FieldSize fieldSize;

    public int maxCellsX;
    public int maxCellsY;
    public int cellsCount;

    public Map<Integer, PlayerType> customPlayers;

    public static GameSettings getDefault() {
        GameSettings settings = new GameSettings();
        settings.players = getPlayersByDifficulty(GameDifficulty.NORMAL, 5);
        settings.customPlayers = getPlayersByDifficulty(GameDifficulty.EASY, 5);
        settings.setSize(FieldSize.NORMAL);
        settings.difficulty = GameDifficulty.NORMAL;
        return settings;
    }

    public Map<Integer, PlayerType> getPlayers(GameDifficulty difficulty, int playersNumber) {
        if (difficulty == GameDifficulty.CUSTOM && players != null)
        {
            return players;
        }else {
            return getPlayersByDifficulty(difficulty, playersNumber);
        }
    }

    public static Map<Integer, PlayerType> getPlayersByDifficulty(GameDifficulty difficulty, int playersNumber) {
        Map<Integer, PlayerType> players;
        switch (difficulty){
            case EASY:
                players = singlePlayerEasy(playersNumber);
                break;
            case NORMAL:
                players = singlePlayerNormal(playersNumber);
                break;
            case HARD:
                players = singlePlayerHard(playersNumber);
                break;
            case INSANE:
                players = singlePlayerInsane(playersNumber);
                break;
            default:
                players = singlePlayerNormal(playersNumber);
        }
        return players;
    }

    public static Map<Integer, PlayerType> singlePlayerEasy(int playersNumber) {
        Map<Integer, PlayerType> players = new HashMap<Integer, PlayerType>();
        switch (playersNumber) {
            case 5:
                players.put(4, PlayerType.Lazy);
            case 4:
                players.put(3, PlayerType.Lazy);
            case 3:
                players.put(2, PlayerType.Dummy);
            case 2:
                players.put(1, PlayerType.Random);
            default:
                players.put(0, PlayerType.Human);
                break;

        }
        return players;
    }

    public static Map<Integer, PlayerType>   singlePlayerNormal(int playersNumber) {
        Map<Integer, PlayerType> players = new HashMap<Integer, PlayerType>();

        switch (playersNumber) {
            case 5:
                players.put(4, PlayerType.Beefy);
            case 4:
                players.put(3, PlayerType.Lazy);
            case 3:
                players.put(2, PlayerType.Dummy);
            case 2:
                players.put(1, PlayerType.Dummy);
            default:
                players.put(0, PlayerType.Human);
        }
        return players;
    }

    public static Map<Integer, PlayerType> singlePlayerHard(int playersNumber) {
        Map<Integer, PlayerType> players = new HashMap<Integer, PlayerType>();

        switch (playersNumber) {
            case 5:
                players.put(4, PlayerType.Hunter);
            case 4:
                players.put(3, PlayerType.Hunter);
            case 3:
                players.put(2, PlayerType.Smarty);
            case 2:
                players.put(1, PlayerType.Smarty);
            default:
                players.put(0, PlayerType.Human);
        }
        return players;
    }

    public static Map<Integer, PlayerType> singlePlayerInsane(int playersNumber) {
        Map<Integer, PlayerType> players = new HashMap<Integer, PlayerType>();

        switch (playersNumber) {
            case 5:
                players.put(4, PlayerType.Beefy);
            case 4:
                players.put(3, PlayerType.Lazy);
            case 3:
                players.put(2, PlayerType.Dummy);
            case 2:
                players.put(1, PlayerType.Dummy);
            default:
                players.put(0, PlayerType.Human);
                break;
        }
        return players;
    }

    public void setSize(FieldSize size) {

        this.fieldSize = size;

        switch (size) {
            case SMALL:
                cellsCount = 15;
                maxCellsX = 4;
                maxCellsY = 5;
                break;
            case NORMAL:
                cellsCount = 25;
                maxCellsX = 5;
                maxCellsY = 7;
                break;
            case LARGE:
                cellsCount = 50;
                maxCellsX = 7;
                maxCellsY = 10;
                break;
            case XLARGE:
                cellsCount = 80;
                maxCellsX = 10;
                maxCellsY = 14;
                break;
        }

    }

    public int getNumberOfPlayers() {
        if (players != null){
            return players.size();
        } else {
            return 0;
        }
    }

    public int getNumberOfHumans() {
        if (players != null){
            int i = 0;
            for (Integer player : players.keySet()) {
                if (players.get(player).equals(PlayerType.Human)) {
                    i++;
                }
            }
            return i;
        } else {
            return 0;
        }
    }
}
