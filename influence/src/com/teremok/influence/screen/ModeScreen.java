package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.Influence;
import com.teremok.influence.model.GameSettings;
import com.teremok.influence.ui.ButtonTexture;
import com.teremok.influence.ui.Checkbox;
import com.teremok.influence.ui.RadioTexture;

/**
 * Created by Алексей on 29.03.14
 */
public class ModeScreen extends StaticScreen {

    private static final String DARKNESS = "darkness";
    private static final String NEXT = "next";

    Checkbox darkness;
    GameSettings gameSettings;


    public ModeScreen(Influence game, String filename) {
        super(game, filename);
        gameSettings = game.getSettings().gameSettings;
    }

    @Override
    protected void addActors() {

        darkness = new RadioTexture(uiElements.get(DARKNESS));
        darkness.setChecked(gameSettings.darkness);

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
                    screenController.showStartScreen();
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Actor hit = stage.hit(x, y, true);
                return hit != null && (hit instanceof Checkbox || hit instanceof ButtonTexture);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!event.isHandled()) {
                    Actor target = stage.hit(x, y, true);
                    if (target == null)
                        return;
                    game.getFXPlayer().playClick();
                    if (target instanceof ButtonTexture) {
                        screenController.showMapSizeScreen();
                    } else {
                        Checkbox selectedSize = (Checkbox) target;
                        switch (selectedSize.getCode()) {
                            case DARKNESS:
                                gameSettings.darkness = !gameSettings.darkness;
                                darkness.setChecked(gameSettings.darkness);
                                break;
                        }
                        Gdx.app.debug(getClass().getSimpleName(), "Darkness: " + gameSettings.darkness);
                    }
                }
            }
        });
    }
}
