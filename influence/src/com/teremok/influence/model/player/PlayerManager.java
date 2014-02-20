package com.teremok.influence.model.player;

import com.teremok.influence.model.Field;
import com.teremok.influence.model.Match;
import com.teremok.influence.util.Logger;

/**
 * Created by Alexx on 07.01.14
 */
public class PlayerManager {

    private int currentNum = 0;
    private Player[] players = new Player[5];
    private int numberOfPlayers = 0;
    private Field field;
    private Match match;

    public PlayerManager(Match match) {
        this.match = match;
        this.field = match.getField();
    }

    public void addPlayer(Player player, int num) {
        if (num < 0 || num > numberOfPlayers-1) {
            return;
        }

        players[num] = player;

    }

    public Player next() {
        do {
            currentNum++;
            if (currentNum > numberOfPlayers-1) {
                currentNum = 0;
            }
        } while (players[currentNum].getScore() == 0 );
        //Logger.log("Turn ended. Next player : " + currentNum);
        return players[currentNum];
    }

    public Player current() {
        return players[currentNum];
    }

    public void update() {
        Player player;
        for (int i = 0; i < numberOfPlayers; i++) {
            player = players[i];
            int scoreToSet = field.calcScore(player.number);
            player.setScore(scoreToSet);
        }
    }

    public void addPlayersForSingleplayer(Field field) {
        this.field = field;
        resetPlayersArray(8);
        addPlayer(PlayerFactory.getHunter(0, match), 0);
        addPlayer(PlayerFactory.getLazy(1, match), 1);
        addPlayer(PlayerFactory.getSmarty(2, match), 2);
        addPlayer(PlayerFactory.getHunter(3, match), 3);
        addPlayer(PlayerFactory.getHunter(4, match), 4);
        addPlayer(PlayerFactory.getHunter(5, match), 5);
        addPlayer(PlayerFactory.getHunter(6, match), 6);
        addPlayer(PlayerFactory.getHunter(7, match), 7);
        placeStartPositions();
    }

    public  void addPlayersForMultiplayer(Field field) {
        this.field = field;
        resetPlayersArray(2);
        addPlayer(PlayerFactory.getHunter(0, match), 0);
        addPlayer(PlayerFactory.getHunter(1, match), 1);
        placeStartPositionsMultiplayer();
    }

    private void resetPlayersArray(int number) {
        numberOfPlayers = number;
        players = new Player[number];
    }

    private void placeStartPositions() {
        for (Player player : players) {
            field.placeStartPosition(player.getNumber());
        }
    }

    private void placeStartPositionsMultiplayer() {
        field.placeStartPositionFromRange(players[0].number, 0, 70);
        field.placeStartPositionFromRange(players[1].number, 300, 350);
    }

    public boolean isHumanActing() {
        return current() instanceof HumanPlayer;
    }

    public boolean isHumanInGame() {
        for (Player player : players) {
            if (player instanceof HumanPlayer && player.getScore() > 0)
                return true;
        }
        return false;
    }

    public int getNumberOfPlayerInGame(){
        int playersInGame = 0;

        for (Player player : players) {
            if (player.getScore() > 0) {
                playersInGame++;
            }
        }

        return playersInGame;
    }

    public int getTotalScore() {
        int total = 0;
        for (Player player : players) {
            total += player.getScore();
        }
        return total;
    }

    // Auto-generated

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Field getField() {
        return field;
    }
}
