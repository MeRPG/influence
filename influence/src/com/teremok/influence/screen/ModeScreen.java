package com.teremok.influence.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.ButtonTexture;
import com.teremok.influence.ui.Checkbox;
import com.teremok.influence.ui.RadioTexture;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.Logger;

/**
 * Created by Алексей on 29.03.14
 */
public class ModeScreen extends StaticScreen {

    private static final String DARKNESS = "darkness";
    private static final String NEXT = "next";

    Checkbox darkness;


    public ModeScreen(Influence game, String filename) {
        super(game, filename);
    }

    @Override
    protected void addActors() {

        darkness = new RadioTexture(uiElements.get(DARKNESS));
        darkness.setChecked(Settings.gameSettings.darkness);

        ButtonTexture next = new ButtonTexture(uiElements.get(NEXT));

        stage.addActor(darkness);
        stage.addActor(next);
    }

    @Override
    protected void addListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (!event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE)) {
                    ScreenController.showStartScreen();
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Actor hit = stage.hit(x, y, true);
                if (hit != null) {
                    return hit instanceof Checkbox || hit instanceof ButtonTexture;
                } else {
                    return false;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!event.isHandled()) {
                    Actor target = stage.hit(x, y, true);
                    if (target == null)
                        return;
                    FXPlayer.playClick();
                    if (target instanceof ButtonTexture) {
                        ScreenController.showMapSizeScreen();
                    } else {
                        Checkbox selectedSize = (Checkbox) target;
                        switch (selectedSize.getCode()) {
                            case DARKNESS:
                                Settings.gameSettings.darkness = !Settings.gameSettings.darkness;
                                darkness.setChecked(Settings.gameSettings.darkness);
                                break;
                        }
                        Logger.log("Darkness: " + Settings.gameSettings.darkness);
                    }
                }
            }
        });
    }
}
