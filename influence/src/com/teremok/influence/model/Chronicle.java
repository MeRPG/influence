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

    public int getWinRate() {
        return played == 0 ?  0 : won*100/played;
    }

    public static class MatchChronicle {
        public int damage;
        public int damageGet;
        public int cellsConquered;
        public int cellsLost;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MatchChronicle)) return false;

            MatchChronicle that = (MatchChronicle) o;

            if (cellsConquered != that.cellsConquered) return false;
            if (cellsLost != that.cellsLost) return false;
            if (damage != that.damage) return false;
            if (damageGet != that.damageGet) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = damage;
            result = 31 * result + damageGet;
            result = 31 * result + cellsConquered;
            result = 31 * result + cellsLost;
            return result;
        }
    }

    //Auto-generated


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chronicle)) return false;

        Chronicle chronicle = (Chronicle) o;

        if (cellsConquered != chronicle.cellsConquered) return false;
        if (cellsLost != chronicle.cellsLost) return false;
        if (damage != chronicle.damage) return false;
        if (damageGet != chronicle.damageGet) return false;
        if (influence != chronicle.influence) return false;
        if (played != chronicle.played) return false;
        if (won != chronicle.won) return false;
        if (!match.equals(chronicle.match)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = played;
        result = 31 * result + won;
        result = 31 * result + influence;
        result = 31 * result + damage;
        result = 31 * result + damageGet;
        result = 31 * result + cellsConquered;
        result = 31 * result + cellsLost;
        result = 31 * result + match.hashCode();
        return result;
    }
}
