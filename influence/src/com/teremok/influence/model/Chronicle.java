package com.teremok.influence.model;

import com.teremok.influence.controller.ChronicleController;

/**
 * Created by Алексей on 19.05.2014
 */
public class Chronicle {
    public static int played;
    public static int won;

    public static int damage;
    public static int damageGet;

    public static int totalScore;

    public static int cellsConquered;
    public static int cellsLost;

    public static class match {
        public static int damage;
        public static int damageGet;
        public static int cellsConquered;
        public static int cellsLost;
    }

    public static void init() {
        ChronicleController.load();
    }

    public static void matchStart() {
        match.cellsLost = 0;
        match.cellsConquered = 0;
        match.damage = 0;
        match.damageGet = 0;
    }

    public static void matchEnd(boolean won, int score) {
        Chronicle.played++;
        if (won) {
            Chronicle.won++;
        }

        damage += match.damage;
        damageGet += match.damageGet;
        cellsConquered += match.cellsConquered;
        cellsLost += match.cellsLost;

        Chronicle.totalScore = countScore();

        match.damage = 0;
        match.damageGet = 0;
        match.cellsConquered = 0;
        match.cellsLost = 0;

        ChronicleController.save();
    }

    private static int countScore() {
        return played*100 - (played-won)*100 + cellsConquered*50 - cellsLost*50 + damage - damageGet;
    }
}
