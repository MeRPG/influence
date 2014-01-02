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
        PlayerManager.setNumberOfPlayers(5);
        PlayerManager.addPlayer(new HumanPlayer(0), 0);
        //Player.addPlayer(new ComputerPlayer(0, gameScreen), 0);
        for (int i = 1; i < PlayerManager.getNumberOfPlayers(); i ++) {
            PlayerManager.addPlayer(new ComputerPlayer(i, gameScreen, field), i);
        }
    }

    static public  void addPlayersForMultiplayer(GameScreen gameScreen) {
        PlayerManager.setNumberOfPlayers(2);
        PlayerManager.addPlayer(new HumanPlayer(0), 0);
        PlayerManager.addPlayer(new ComputerPlayer(1, gameScreen, field), 1);
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

    public static void setField(Field field) {
        PlayerManager.field = field;
    }
}
