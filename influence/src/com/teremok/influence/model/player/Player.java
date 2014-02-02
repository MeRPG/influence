package com.teremok.influence.model.player;

import com.teremok.influence.model.Field;
import com.teremok.influence.model.Match;
import com.teremok.influence.screen.GameScreen;

/**
 * Created by Alexx on 26.12.13
 */
public abstract class Player {

    protected int type;
    protected int powerToDistribute;
    protected int score;
    protected Match match;
    protected Field field;

    protected Player(int type, Match match) {
        this.type = type;
        this.match = match;
        this.field = match.getField();
    }

    public void setPowerToDistribute(int power) {
        this.powerToDistribute = power;
    }

    public int getPowerToDistribute() {
        return powerToDistribute;
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

    // Auto-generated

    public int getType() {
        return type;
    }

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
