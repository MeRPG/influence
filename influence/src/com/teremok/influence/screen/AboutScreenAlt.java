package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.util.FXPlayer;

/**
 * Created by Alexx on 25.02.14
 */
public class AboutScreenAlt extends StaticScreen {


    private ColoredPanel credits;

    public AboutScreenAlt(Game game, String filename) {
        super(game, filename);
    }

    @Override
    protected void addActors() {
        if (credits == null) {
            credits = new ColoredPanel(new Color(0x000000FF), 0f, 0f, WIDTH, 54f);
            stage.addActor(credits);
        }
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


        credits.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) == credits;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (!event.isHandled()) {
                    FXPlayer.playClick();
                    ScreenController.showStartScreen();
                }
            }
        });
    }
}
