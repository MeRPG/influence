package com.teremok.influence.model.player;

import com.teremok.influence.model.Field;
import com.teremok.influence.screen.GameScreen;

/**
 * Created by Alexx on 26.12.13
 */
public class HumanPlayer extends Player {


    protected HumanPlayer(int type, GameScreen screen, Field field) {
        super(type, screen, field);
    }

    @Override
    protected void actLogic(float delta) {

    }
}
