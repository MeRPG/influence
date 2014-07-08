package com.teremok.influence.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.framework.screen.StaticScreen;
import com.teremok.framework.ui.ButtonTexture;
import com.teremok.framework.ui.Checkbox;
import com.teremok.framework.ui.RadioGroup;
import com.teremok.framework.ui.RadioTexture;
import com.teremok.influence.Influence;
import com.teremok.influence.model.FieldSize;
import com.teremok.influence.model.Settings;

import static com.teremok.influence.screen.InfluenceScreenController.*;

/**
 * Created by Алексей on 29.03.14
 */
public class MapSizeScreen extends StaticScreen <Influence> {

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

    Settings settings;

    public MapSizeScreen(Influence game, String filename) {
        super(game, filename);
        settings = game.getSettings();
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
                    screenController.setScreen(MODE_SCREEN);
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
                if (! event.isHandled()) {
                    Actor target = stage.hit(x, y, true);
                    if (target == null)
                        return;
                    game.getFXPlayer().playClick();
                    if (target instanceof ButtonTexture) {
                        screenController.setScreen(PLAYERS_SCREEN);
                    } else {
                        Checkbox selectedSize = (Checkbox) target;
                        switch (selectedSize.getCode()) {
                            case S:
                                settings.gameSettings.setSize(FieldSize.SMALL);
                                break;
                            case M:
                                settings.gameSettings.setSize(FieldSize.NORMAL);
                                break;
                            case L:
                                settings.gameSettings.setSize(FieldSize.LARGE);
                                break;
                            case XL:
                                settings.gameSettings.setSize(FieldSize.XLARGE);
                                break;
                        }
                        checkSelectedSize();
                    }
                }
            }
        });
    }

    private void checkSelectedSize() {
        switch (settings.gameSettings.fieldSize) {
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
