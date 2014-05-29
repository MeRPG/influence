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
        number = numberFactory.getNumber(Chronicle.played, 240, 455, false);
        number.setColor(Chronicle.played == 0 ? numberFactory.BAD_COLOR : numberFactory.NORMAL_COLOR);
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(Chronicle.getWinRate(), 240, 361, true);
        number.setColor(numberFactory.getCompareColor(Chronicle.getWinRate(), 50));
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(Chronicle.cellsConquered, 240, 267, false);
        number.setColor(Chronicle.cellsConquered == 0 ? numberFactory.BAD_COLOR : numberFactory.GOOD_COLOR);
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(Chronicle.cellsLost, 240, 173, false);
        number.setColor(numberFactory.BAD_COLOR);
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(Chronicle.getInfluence(), 240, 79, false);
        number.setColor(Chronicle.getInfluence() == 0 ? numberFactory.BAD_COLOR : numberFactory.NORMAL_COLOR);
        number.centre();
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
