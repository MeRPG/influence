package com.teremok.influence.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.Influence;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.ui.Label;
import com.teremok.influence.util.FontFactory;

/**
 * Created by Alexx on 25.02.14
 */
public class AboutScreen extends StaticScreen {


    private ColoredPanel credits;

    public AboutScreen(Influence game, String filename) {
        super(game, filename);
    }

    @Override
    protected void addActors() {
        Label fansLabel = new Label("Fans of the month", fontFactory.getFont(FontFactory.SUBSTATUS), Color.WHITE,
                WIDTH/2, 298f, false, Label.Align.CENTER);

        Label teamLabel = new Label("Team", fontFactory.getFont(FontFactory.SUBSTATUS), Color.WHITE,
                WIDTH/2, 529f, false, Label.Align.CENTER);

        Label reviewFirst = new Label("Leave the best review on Google Play.", fontFactory.getFont(FontFactory.SUBSTATUS), new Color(0xbababaff),
                WIDTH/2, 131f, false, Label.Align.CENTER);

        Label reviewSecond = new Label("Become a fan of the month!", fontFactory.getFont(FontFactory.SUBSTATUS), new Color(0xbababaff),
                WIDTH/2, 102f, false, Label.Align.CENTER);

        stage.addActor(fansLabel);
        stage.addActor(teamLabel);
        stage.addActor(reviewFirst);
        stage.addActor(reviewSecond);

        if (credits == null) {
            credits = new ColoredPanel(new Color(0x00000000), 0f, 0f, WIDTH, 54f);
            stage.addActor(credits);
        }
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


        credits.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) == credits;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (!event.isHandled()) {
                    game.getFXPlayer().playClick();
                    screenController.showStartScreen();
                }
            }
        });
    }
}
