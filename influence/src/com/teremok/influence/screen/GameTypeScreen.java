package com.teremok.influence.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.Influence;
import com.teremok.influence.model.GameSettings;
import com.teremok.influence.util.Logger;

/**
 * Created by Алексей on 01.06.2014
 */
public class GameTypeScreen extends StaticScreen {

    GameSettings gameSettings;

    public GameTypeScreen(Influence game, String filename) {
        super(game, filename);
        gameSettings = game.getSettings().gameSettings;
    }

    @Override
    protected void addActors() {

    }

    @Override
    protected void addListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    if (isInfoMessageVisible()) {
                        hideInfoMessageAnimation();
                        return true;
                    }
                    ScreenController.showStartScreen();
                    return true;
                }
                if (! event.isHandled() && (keycode == Input.Keys.SPACE)) {
                    ScreenController.showMapSizeScreen();
                    gameSettings.gameForInfluence = false;
                    Logger.log("Average game started.");
                }
                if (! event.isHandled() && (keycode == Input.Keys.ENTER)) {
                    ScreenController.showMapSizeScreen();
                    gameSettings.gameForInfluence = true;
                    Logger.log("Game for Influence started.");
                }
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled() && isInfoMessageVisible()) {
                    hideInfoMessageAnimation();
                }
            }
        });
    }
}
