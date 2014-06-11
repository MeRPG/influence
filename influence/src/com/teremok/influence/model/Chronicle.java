package com.teremok.influence.model;

/**
 * Created by Алексей on 19.05.2014
 */
public class Chronicle {
    public int played;
    public int won;

    public int influence;

    public int damage;
    public int damageGet;

    public int cellsConquered;
    public int cellsLost;

    public MatchChronicle match;

    public static class MatchChronicle {
        public int damage;
        public int damageGet;
        public int cellsConquered;
        public int cellsLost;
    }

    public int getWinRate() {
        return played == 0 ?  0 : won*100/played;
    }
}
