package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.ui.Button;
import com.teremok.influence.ui.ButtonTexture;
import com.teremok.influence.util.FlurryHelper;
import com.teremok.influence.view.Animation;

/**
 * Created by Alexx on 08.01.14
 */
public class PausePanel extends Popup<GameScreen> {

    private static final String RESUME_CODE = "resume";
    private static final String MENU_CODE = "menu";
    private static final String F5_CODE = "f5";
    private static final String EXIT_CODE = "exit";

    ButtonTexture resume;
    ButtonTexture menu;
    ButtonTexture f5;
    ButtonTexture exit;
    ButtonTexture pause;

    boolean loaded;

    PausePanel(GameScreen gameScreen) {
        super(gameScreen, "pausePanel");
        loaded = false;
    }

    @Override
    protected void addActors() {
        TextureRegion menuRegion = atlas.findRegion("menu");
        menu = new ButtonTexture(MENU_CODE, menuRegion, 82f, 389f);
        this.addActor(menu);

        TextureRegion f5Region = atlas.findRegion("f5");
        f5 = new ButtonTexture(F5_CODE, f5Region, 216f, 389f);
        this.addActor(f5);

        TextureRegion exitRegion = atlas.findRegion("exit");
        exit = new ButtonTexture(EXIT_CODE, exitRegion, 345f, 389f);
        this.addActor(exit);

        TextureRegion resumeRegion = atlas.findRegion("resume_" + Localizator.getLanguage());
        resume = new ButtonTexture(RESUME_CODE, resumeRegion, 113f, 290f);
        this.addActor(resume);

        TextureRegion pauseRegion = atlas.findRegion("pause_" + Localizator.getLanguage());
        pause = new ButtonTexture("-1", pauseRegion, 191f, 493f);
        this.addActor(pause);
    }

    @Override
    protected void addListeners() {
        this.clearListeners();
        this.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return hit(x, y, true) instanceof Button;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (! event.isHandled()) {
                    game.getFXPlayer().playClick();
                    Button target = (Button)event.getTarget();
                    if (target.getCode().equals(RESUME_CODE)) {
                        resume();
                    } else if (target.getCode().equals(MENU_CODE)) {
                        menu();
                    } else if (target.getCode().equals(F5_CODE)) {
                        f5();
                    } else if (target.getCode().equals(EXIT_CODE)) {
                        exitGame();
                    }
                }
            }
        });
    }

    public void show() {

        if (! loaded) {
            addActors();
            addListeners();
            loaded = true;
        }

        clearActions();
        addAction(Actions.alpha(1f, Animation.DURATION_NORMAL));
        setTouchable(Touchable.enabled);
    }

    public void hide() {
        clearActions();
        addAction(Actions.alpha(0f, Animation.DURATION_NORMAL));
        setTouchable(Touchable.disabled);
    }

    private void  resume() {
        currentScreen.resumeMatch();
    }

    private void  menu() {
        FlurryHelper.logPauseExitMenuEvent();
        currentScreen.backToStartScreen();
    }

    private void  f5() {
        FlurryHelper.logPauseRestartEvent();
        currentScreen.gracefullyStartNewMatch();
    }

    private void  exitGame() {
        currentScreen.exitGame();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
