package com.teremok.influence.model.player;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;

/**
 * Created by Alexx on 26.12.13
 */
public abstract class Player {

    protected int type;
    protected int power;
    protected int score;

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
