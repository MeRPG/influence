package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.model.FieldSize;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.ButtonTexture;
import com.teremok.influence.ui.Checkbox;
import com.teremok.influence.ui.RadioGroup;
import com.teremok.influence.ui.RadioTexture;

/**
 * Created by Алексей on 29.03.14
 */
public class MapSizeScreen extends StaticScreen {

    private static final String S = "s";
    private static final String M = "m";
    private static final String L = "l";
    private static final String XL = "xl";

    private static final String NEXT = "next";

    private static final int SIZE_GROUP = 11;

    Checkbox s;
    Checkbox m;
    Checkbox l;
    Checkbox xl;


    public MapSizeScreen(Game game, String filename) {
        super(game, filename);
    }

    @Override
    protected void addActors() {

        RadioGroup sizeGroup = new RadioGroup(SIZE_GROUP);
        s = new RadioTexture(uiElements.get(S));
        m = new RadioTexture(uiElements.get(M));
        l = new RadioTexture(uiElements.get(L));
        xl = new RadioTexture(uiElements.get(XL));

        s.addToGroup(sizeGroup);
        m.addToGroup(sizeGroup);
        l.addToGroup(sizeGroup);
        xl.addToGroup(sizeGroup);

        checkSelectedSize();

        ButtonTexture next = new ButtonTexture(uiElements.get(NEXT));

        stage.addActor(s);
        stage.addActor(m);
        stage.addActor(l);
        stage.addActor(xl);
        stage.addActor(next);
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
                if (! event.isHandled()) {
                    Actor target = stage.hit(x, y, true);
                    if (target == null)
                        return;
                    if (target instanceof ButtonTexture) {
                        ScreenController.showPlayersScreen();
                    } else {
                        Checkbox selectedSize = (Checkbox) target;
                        switch (selectedSize.getCode()) {
                            case S:
                                Settings.gameSettings.setSize(FieldSize.SMALL);
                                break;
                            case M:
                                Settings.gameSettings.setSize(FieldSize.NORMAL);
                                break;
                            case L:
                                Settings.gameSettings.setSize(FieldSize.LARGE);
                                break;
                            case XL:
                                Settings.gameSettings.setSize(FieldSize.XLARGE);
                                break;
                        }
                        checkSelectedSize();
                    }
                }
            }
        });
    }

    private void checkSelectedSize() {
        switch (Settings.gameSettings.fieldSize) {
            case SMALL:
                s.check();
                break;
            case NORMAL:
                m.check();
                break;
            case LARGE:
                l.check();
                break;
            case XLARGE:
                xl.check();
                break;
        }
    }
}
