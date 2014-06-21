package com.teremok.influence.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.ButtonTexture;
import com.teremok.influence.ui.RadioTexture;
import com.teremok.influence.util.Logger;

/**
 * Created by Алексей on 01.06.2014
 */
public class ModeScreen extends StaticScreen {

    private final String DARKNESS = "darkness";
    private final String NEXT = "next";

    public ModeScreen(Influence game, String filename) {
        super(game, filename);
    }

    @Override
    protected void addActors() {
        RadioTexture darknessRadio = new RadioTexture(uiElements.get(DARKNESS));
        darknessRadio.check();
        stage.addActor(darknessRadio);

        ButtonTexture next = new ButtonTexture(uiElements.get(NEXT));
        stage.addActor(next);
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
                    Logger.log("Average game started. Darkness: " + Settings.gameSettings.darkness);
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Actor target = stage.hit(x,y,true);
                return target instanceof RadioTexture || target instanceof ButtonTexture;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled() && isInfoMessageVisible()) {
                    hideInfoMessageAnimation();
                }
                Actor target = event.getTarget();

                if (target instanceof RadioTexture) {
                    RadioTexture touchedRadio = (RadioTexture)target;
                    switch (touchedRadio.getCode()) {
                        case "darkness":
                            Settings.gameSettings.darkness = !Settings.gameSettings.darkness;
                            touchedRadio.setChecked(Settings.gameSettings.darkness);
                            break;
                        default:
                    }
                } else if (target instanceof ButtonTexture) {
                    ScreenController.showMapSizeScreen();
                }
            }
        });
    }
}
