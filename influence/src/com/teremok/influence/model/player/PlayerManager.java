package com.teremok.influence.model.player;

import com.teremok.influence.model.Field;
import com.teremok.influence.model.Match;
import com.teremok.influence.util.Logger;

import java.util.Map;

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

    // TODO переписать, чтобы Player сам проходил по своему массиву клеток
    public void update() {
        Player player;
        for (int i = 0; i < numberOfPlayers; i++) {
            player = players[i];
            int scoreToSet = field.calcScore(player.number);
            player.setScore(scoreToSet);
        }
    }

    public void addPlayersFromMap(Map<Integer, PlayerType> map, Field field) {
        this.field = field;
        resetPlayersArray(map.size());
        for (Integer i = 0; i < map.size(); i++) {
            addPlayer(PlayerFactory.getByType(map.get(i), i, match), i);
            Logger.log("PM adding player in match");
        }
    }

    private void resetPlayersArray(int number) {
        numberOfPlayers = number;
        players = new Player[number];
    }

    public void placeStartPositions() {
        if (players.length == 2) {
            placeStartPositionsTwo();
        } else {
            placeStartPositionsMany();
        }
    }

    private void placeStartPositionsMany() {
        for (Player player : players) {
            field.placeStartPosition(player.getNumber());
        }
    }

    private void placeStartPositionsTwo() {
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
