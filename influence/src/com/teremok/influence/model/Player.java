package com.teremok.influence.model;

/**
 * Created by Alexx on 26.12.13
 */
public class Player {
    static private int currentNum = 0;
    static private Player[] players = new Player[5];

    protected int type;
    protected int power;
    protected int score;

    static public void addPlayer(Player player, int num) {
        if (num < 0 || num > 4) {
            return;
        }

        players[num] = player;

    }

    static public Player next() {
        currentNum++;
        if (currentNum > 4) {
            currentNum = 0;
        }
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

    // Auto-generated


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
}
