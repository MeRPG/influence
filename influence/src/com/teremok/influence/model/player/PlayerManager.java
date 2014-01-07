package com.teremok.influence.model.player;

import com.teremok.influence.model.Field;
import com.teremok.influence.screen.GameScreen;

/**
 * Created by Alexx on 02.01.14
 */

public class PlayerManager {

    static private int currentNum = 0;
    static private Player[] players = new Player[5];
    static private int numberOfPlayers = 0;
    static protected Field field;

    static public void addPlayer(Player player, int num) {
        if (num < 0 || num > numberOfPlayers-1) {
            return;
        }

        players[num] = player;

    }

    static public Player next() {
        do {
            currentNum++;
            if (currentNum > numberOfPlayers-1) {
                currentNum = 0;
            }
        } while (players[currentNum].getScore() == 0 );
        System.out.println("Turn ended. Next player : " + currentNum);
        return players[currentNum];
    }

    static public Player current() {
        return players[currentNum];
    }

    static public void update() {
        Player player;
        for (int i = 0; i < numberOfPlayers; i++) {
            player = players[i];
            int scoreToSet = field.calcScore(player.type);
            player.setScore(scoreToSet);
        }
    }


    static public void addPlayersForSingleplayer(GameScreen gameScreen) {
        resetPlayersArray(5);
        addPlayer(new HumanPlayer(0, gameScreen, field), 0);
        //Player.addPlayer(new ComputerPlayer(0, gameScreen), 0);
        for (int i = 1; i < numberOfPlayers; i ++) {
            addPlayer(new ComputerPlayer(i, gameScreen, field), i);
        }
        placeStartPositions();
    }

    static public  void addPlayersForMultiplayer(GameScreen gameScreen) {
        resetPlayersArray(2);
        addPlayer(new HumanPlayer(0, gameScreen, field), 0);
        addPlayer(new HumanPlayer(1, gameScreen, field), 1);
        placeStartPositionsMultiplayer();
    }

    static private void resetPlayersArray(int number) {
        numberOfPlayers = number;
        players = new Player[number];
    }

    private static void placeStartPositions() {
        for (Player player : players) {
            field.placeStartPosition(player.getType());
        }
    }

    private static void placeStartPositionsMultiplayer() {
        field.placeStartPositionFromRange(players[0].type, 0, 9);
        field.placeStartPositionFromRange(players[1].type, 26, 35);
    }

    public static void setField(Field field) {
        PlayerManager.field = field;

        for (Player player : players) {
            if (player instanceof ComputerPlayer) {
                player.setField(field);
            }
        }
    }

    // Auto-generated

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        PlayerManager.numberOfPlayers = numberOfPlayers;
    }

    public static Player[] getPlayers() {
        return players;
    }

    public static Field getField() {
        return field;
    }
}
