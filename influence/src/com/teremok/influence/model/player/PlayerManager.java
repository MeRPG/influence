package com.teremok.influence.model.player;

import com.badlogic.gdx.Gdx;
import com.teremok.influence.controller.FieldController;
import com.teremok.influence.model.Match;

import java.util.Map;

/**
 * Created by Alexx on 07.01.14
 */
public class PlayerManager {

    private int currentNum = 0;
    private Player[] players = new Player[5];
    private int numberOfPlayers = 0;
    private FieldController field;
    private Match match;
    private Player lastHumanPlayer;

    public PlayerManager(Match match) {
        reset(match);
    }

    public void reset(Match match) {
        this.match = match;
        this.field = match.getFieldController();
        currentNum = 0;
    }

    public void addPlayer(Player player, int num) {
        if (num < 0 || num > numberOfPlayers-1) {
            return;
        }

        players[num] = player;

    }

    public Player nextCurrentPlayer() {
        do {
            currentNum++;
            if (currentNum > numberOfPlayers-1) {
                currentNum = 0;
            }
        } while (players[currentNum].getScore() == 0 );
        if (players[currentNum] instanceof HumanPlayer) {
            lastHumanPlayer = players[currentNum];
        }
        return players[currentNum];
    }

    public Player current() {
        return players[currentNum];
    }

    public void update() {
        for (Player player : players) {
            player.updateScore();
        }
    }

    public void addPlayersFromMap(Map<Integer, PlayerType> map, FieldController field) {
        this.field = field;
        resetPlayersArray(map.size());
        for (Integer i = 0; i < map.size(); i++) {
            addPlayer(PlayerFactory.getByType(map.get(i), i, match), i);
            Gdx.app.debug(getClass().getSimpleName(), "PM adding player in match");
        }
        lastHumanPlayer = players[0];
    }

    private void resetPlayersArray(int number) {
        numberOfPlayers = number;
        players = new Player[number];
    }

    public void placeStartPositions() {
        for (Player player : players) {
            field.placeStartPosition(player);
            field.updateLists();
        }
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

    public int getNumberOfHumans() {
        int humans = 0;
        for (Player player : players) {
            if (player instanceof HumanPlayer)
                humans++;
        }
        return humans;
    }

    public int getNumberOfHumansInGame() {
        int humans = 0;
        for (Player player : players) {
            if (player instanceof HumanPlayer && player.score > 0)
                humans++;
        }
        return humans;
    }

    public int getNextPlayerNumber() {
        int nextNum = currentNum;
        do {
            nextNum++;
            if (nextNum == numberOfPlayers) {
                nextNum = 0;
            }
        } while (players[nextNum].score == 0);
        return nextNum;
    }

    public Player getNextPlayer() {
        return players[getNextPlayerNumber()];
    }

    // Auto-generated

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public Player[] getPlayers() {
        return players;
    }

    public FieldController getField() {
        return field;
    }

    public Player getLastHumanPlayer() {
        return lastHumanPlayer;
    }
}
