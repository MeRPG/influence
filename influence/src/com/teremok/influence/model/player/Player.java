package com.teremok.influence.model.player;

import com.teremok.influence.model.Field;
import com.teremok.influence.model.Match;

/**
 * Created by Alexx on 26.12.13
 */
public abstract class Player {

    protected int number;
    protected int powerToDistribute;
    protected int score;
    protected Match match;
    protected Field field;
    protected PlayerType type;

    protected Player(int number, Match match) {
        this.number = number;
        this.match = match;
        this.field = match.getField();
    }

    public void subtractPowerToDistribute() {
        if (powerToDistribute > 0)
            powerToDistribute--;
        else
            powerToDistribute = 0;
    }

    public void act(float delta) {
        actLogic(delta);
    }

    protected abstract void actLogic(float delta);

    public boolean hasPowerToDistribute() {
        return powerToDistribute > 0;
    }

    // Auto-generated

    public void setPowerToDistribute(int power) {
        this.powerToDistribute = power;
    }

    public int getPowerToDistribute() {
        return powerToDistribute;
    }

    public int getNumber() {
        return number;
    }

    public PlayerType getType() { return type; }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
