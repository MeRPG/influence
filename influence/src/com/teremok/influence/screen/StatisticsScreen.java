package com.teremok.influence.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Chronicle;
import com.teremok.influence.ui.Label;
import com.teremok.influence.ui.TextureNumber;
import com.teremok.influence.util.FontFactory;
import com.teremok.influence.util.TextureNumberFactory;

/**
 * Created by Алексей on 26.05.2014
 */
public class StatisticsScreen extends StaticScreen {

    public StatisticsScreen (Influence game, String filename) {
        super(game, filename);
    }

    @Override
    protected void addActors() {

        Label gamesPlayedLabel = new Label("gamesPlayed", fontFactory.getFont(FontFactory.STATUS), Color.WHITE,
                WIDTH/2, 520f, true, Label.Align.CENTER);

        Label winRateLabel = new Label("winRate", fontFactory.getFont(FontFactory.STATUS), Color.WHITE,
                WIDTH/2, 426f, true, Label.Align.CENTER);

        Label cellsConqueredLabel = new Label("cellsConquered", fontFactory.getFont(FontFactory.STATUS), Color.WHITE,
                WIDTH/2, 332f, true, Label.Align.CENTER);

        Label cellsLostLabel = new Label("cellsLost", fontFactory.getFont(FontFactory.STATUS), Color.WHITE,
                WIDTH/2, 238f, true, Label.Align.CENTER);

        Label influenceLabel = new Label("influence", fontFactory.getFont(FontFactory.STATUS), Color.WHITE,
                WIDTH/2, 144f, true, Label.Align.CENTER);

        stage.addActor(gamesPlayedLabel);
        stage.addActor(winRateLabel);
        stage.addActor(cellsConqueredLabel);
        stage.addActor(cellsLostLabel);
        stage.addActor(influenceLabel);

        TextureNumberFactory numberFactory = new TextureNumberFactory();
        Chronicle chronicle = game.getChronicle();
        TextureNumber number;
        number = numberFactory.getNumber(chronicle.played, 240, 455, false);
        number.setColor(chronicle.played == 0 ? numberFactory.BAD_COLOR : numberFactory.NORMAL_COLOR);
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(chronicle.getWinRate(), 240, 361, true);
        number.setColor(numberFactory.getCompareColor(chronicle.getWinRate(), 50));
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(chronicle.cellsConquered, 240, 267, false);
        number.setColor(chronicle.cellsConquered == 0 ? numberFactory.BAD_COLOR : numberFactory.GOOD_COLOR);
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(chronicle.cellsLost, 240, 173, false);
        number.setColor(numberFactory.BAD_COLOR);
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(chronicle.influence, 240, 79, false);
        number.setColor(chronicle.influence < 1 ? numberFactory.BAD_COLOR : numberFactory.NORMAL_COLOR);
        number.centre();
        stage.addActor(number);

    }

    @Override
    protected void addListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    screenController.showStartScreen();
                    return true;
                }
                return false;
            }
        });
    }
}
