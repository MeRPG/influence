package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Created by Alexx on 22.12.13
 */
public class SplashScreen extends AbstractScreen {

    private TextureAtlas atlas;

    public SplashScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        atlas = new TextureAtlas(Gdx.files.internal("splashScreen.pack"));
    }

    @Override
    public void resize(int width, int height) {
       super.resize(width, height);

        TextureRegion splashRegion;
        splashRegion = atlas.findRegion("splashLogo");

        Image splashImage = new Image( new TextureRegionDrawable(splashRegion), Scaling.fit, Align.center);

        splashImage.addAction(constructSequenceAction());

        stage.addActor( splashImage );
    }

    private Action constructSequenceAction() {
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(createFadeInAction());
        sequenceAction.addAction(createDelayAction());
        sequenceAction.addAction(createFadeOutAction());
        sequenceAction.addAction(createCompleteAction());
        return sequenceAction;
    }

    private Action createFadeInAction(){
        AlphaAction fadeIn = new AlphaAction();
        fadeIn.setAlpha(1f);
        fadeIn.setDuration(0.75f);
        return fadeIn;
    }

    private Action createFadeOutAction(){
        AlphaAction fadeOut = new AlphaAction();
        fadeOut.setAlpha(0f);
        fadeOut.setDuration(0.75f);
        return fadeOut;
    }

    private Action createDelayAction(){
        DelayAction delayAction = new DelayAction();
        delayAction.setDuration(1.75f);
        return delayAction;
    }

    private Action createCompleteAction(){
        Action completeAction = new Action(){
            public boolean act( float delta ) {
                game.setScreen( new StartScreen(game) );
                return true;
            }
        };
        return completeAction;
    }
}
