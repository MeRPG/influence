package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.ui.Button;

/**
 * Created by Alexx on 08.01.14
 */
public class PausePanel extends Group {

    private final String RESUME_CODE = "resume";
    private final String MENU_CODE = "menu";
    private final String F5_CODE = "f5";
    private final String EXIT_CODE = "exit";

    Button resume;
    Button menu;
    Button f5;
    Button exit;

    TextureAtlas atlas;
    TextureRegion background;

    GameScreen gameScreen;

    PausePanel(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        atlas = new TextureAtlas(Gdx.files.internal("pausePanel.pack"));

        for (Texture texture : atlas.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        setBounds(0,0, AbstractScreen.WIDTH, AbstractScreen.HEIGHT);

        background = atlas.findRegion("background");
        Image backImage = new Image( new TextureRegionDrawable(background), Scaling.fit, Align.center );
        this.addActor(backImage);

        TextureRegion resumeRegion = atlas.findRegion("resume");
        resume = new Button(RESUME_CODE, resumeRegion, 112f, 320f);
        this.addActor(resume);

        TextureRegion menuRegion = atlas.findRegion("menu");
        menu = new Button(MENU_CODE, menuRegion, 112f, 416f);
        this.addActor(menu);

        TextureRegion f5Region = atlas.findRegion("f5");
        f5 = new Button(F5_CODE, f5Region, 208, 416f);
        this.addActor(f5);

        TextureRegion exitRegion = atlas.findRegion("exit");
        exit = new Button(EXIT_CODE, exitRegion, 304f, 416f);
        this.addActor(exit);

        this.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return hit(x, y, true) instanceof Button;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (! event.isHandled()) {
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

    private void  resume() {
        gameScreen.pauseMatch();
    }

    private void  menu() {
        gameScreen.backToStartScreen();
    }

    private void  f5() {
        gameScreen.startNewMatch();
    }

    private void  exitGame() {
        gameScreen.exit();
    }
}
