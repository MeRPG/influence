package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.Influence;

/**
 * Created by Алексей on 01.07.2014
 */
public abstract class Popup <T extends StaticScreen> extends Group {

    Influence game;
    T currentScreen;

    TextureAtlas atlas;
    TextureRegion background;


    public Popup(T screen, String atlasName) {
        this.currentScreen = screen;
        this.game = ((Influence) Gdx.app.getApplicationListener());
        this.atlas = game.getResourceManager().getAtlas(atlasName);

        getColor().a = 0f;
        setTouchable(Touchable.disabled);
        setBounds(0,0, AbstractScreen.WIDTH, AbstractScreen.HEIGHT);

        addBackground();
        addDefaultListener();
    }


    protected void addBackground() {
        atlas = game.getResourceManager().getAtlas("pausePanel");

        background = atlas.findRegion("background");
        Image backImage = new Image( new TextureRegionDrawable(background), Scaling.fit, Align.center );
        this.addActor(backImage);
    }

    protected void addDefaultListener() {
        this.addListener( new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                currentScreen.hideInfoMessageAnimation();
            }
        });
    }

    protected abstract void addActors();
    protected abstract void addListeners();
}
