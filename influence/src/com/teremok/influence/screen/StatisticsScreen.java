package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.model.Chronicle;
import com.teremok.influence.ui.TextureNumber;
import com.teremok.influence.util.TextureNumberFactory;

/**
 * Created by Алексей on 26.05.2014
 */
public class StatisticsScreen extends StaticScreen {

    public StatisticsScreen (Game game, String filename) {
        super(game, filename);
    }

    @Override
    protected void addActors() {
        TextureNumberFactory numberFactory = new TextureNumberFactory();

        TextureNumber number;
        number = numberFactory.getNumber(Chronicle.played, 240, 550);
        stage.addActor(number);
        number = numberFactory.getNumber(Chronicle.won, 240, 500);
        stage.addActor(number);
        number = numberFactory.getNumber(Chronicle.played - Chronicle.won, 240, 450);
        stage.addActor(number);
        number = numberFactory.getNumber(Chronicle.damage, 240, 400);
        stage.addActor(number);
        number = numberFactory.getNumber(Chronicle.damageGet, 240, 350);
        stage.addActor(number);
        number = numberFactory.getNumber(Chronicle.cellsConquered, 240, 300);
        stage.addActor(number);
        number = numberFactory.getNumber(Chronicle.cellsLost, 240, 250);
        stage.addActor(number);
        number = numberFactory.getNumber(Chronicle.totalScore, 240, 200);
        stage.addActor(number);

    }

    @Override
    protected void addListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    ScreenController.showStartScreen();
                    return true;
                }
                return false;
            }
        });
    }
}
