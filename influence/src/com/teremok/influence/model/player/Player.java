package com.teremok.influence.model.player;

import com.teremok.influence.model.Field;
import com.teremok.influence.screen.GameScreen;

/**
 * Created by Alexx on 26.12.13
 */
public abstract class Player {

    protected int type;
    protected int powerToDistribute;
    protected int score;
    protected GameScreen gameScreen;
    protected Field field;

    protected Player(int type, GameScreen gameScreen, Field field) {
        this.type = type;
        this.gameScreen = gameScreen;
        this.field = field;
    }

    public void setPowerToDistribute(int power) {
        this.powerToDistribute = power;
    }

    public int getPowerToDistribute() {
        return powerToDistribute;
    }

    public void subtractPowerToDistribute() {
        powerToDistribute--;
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
