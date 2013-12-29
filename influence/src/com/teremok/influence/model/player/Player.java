package com.teremok.influence.model.player;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.model.Field;

/**
 * Created by Alexx on 26.12.13
 */
public abstract class Player {
    static private int currentNum = 0;
    static private Player[] players = new Player[5];
    static private int numberOfPlayers = 0;
    static protected Field field;


    protected int type;
    protected int power;
    protected int score;

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

    // Non-static

    public Player(int type) {
        this.type = type;
    }

    public void setPowerToDistribute(int power) {
        this.power = power;
    }

    public int getPowerToDistribute() {
        return power;
    }

    public void subtractPowerToDistribute() {
        power--;
    }

    public void act(float delta) {
        actLogic(delta);
    }

    protected abstract void actLogic(float delta);

    // Auto-generated

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static void setNumberOfPlayers(int numberOfPlayers) {
        Player.numberOfPlayers = numberOfPlayers;
    }

    public static Player[] getPlayers() {
        return players;
    }

    public int getType() {
        return type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static Field getField() {
        return field;
    }

    public static void setField(Field field) {
        Player.field = field;
    }
}
