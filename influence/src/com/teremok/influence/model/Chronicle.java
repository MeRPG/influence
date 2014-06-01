package com.teremok.influence.model;

/**
 * Created by Алексей on 19.05.2014
 */
public class Chronicle {
    public static int played;
    public static int won;

    public static int influence;

    public static int damage;
    public static int damageGet;

    public static int cellsConquered;
    public static int cellsLost;

    public static class match {
        public static int damage;
        public static int damageGet;
        public static int cellsConquered;
        public static int cellsLost;
    }

    public static int getWinRate() {
        return played == 0 ?  0 : won*100/played;
    }
}
