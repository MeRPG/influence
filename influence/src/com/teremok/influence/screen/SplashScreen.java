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
import com.teremok.influence.util.RenderHelper;

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

        atlas = new TextureAtlas(Gdx.files.internal("splash.pack"));
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(RenderHelper.SCREEN_WIDTH, RenderHelper.SCREEN_HEIGHT, false);
        // let's make sure the stage is clear
        stage.clear();

        // in the image atlas, our splash image begins at (0,0) of the
        // upper-left corner and has a dimension of 512x301

        TextureRegion splashRegion;
        splashRegion = atlas.findRegion("splashLogo");
        // here we create the splash image actor and set its size

        Image splashImage = new Image( new TextureRegionDrawable(splashRegion), Scaling.stretch, Align.bottom | Align.left );
        splashImage.setWidth(width);
        splashImage.setHeight(height);

        // this is needed for the fade-in effect to work correctly; we're just
        // making the image completely transparent
        splashImage.getColor().a = 0f;

        // configure the fade-in/out effect on the splash image
        AlphaAction alphaIn = new AlphaAction();
        alphaIn.setAlpha(1f);
        alphaIn.setDuration(0.75f);

        DelayAction delayAction = new DelayAction();
        delayAction.setDuration(1.75f);

        AlphaAction alphaOut = new AlphaAction();
        alphaOut.setAlpha(0f);
        alphaOut.setDuration(0.75f);

        Action completeAction = new Action(){
            public boolean act( float delta ) {
                game.setScreen( new GameScreen(game) );
                return true;
            }
        };

        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(alphaIn);
        sequenceAction.addAction(delayAction);
        sequenceAction.addAction(alphaOut);
        sequenceAction.addAction(completeAction);

        splashImage.addAction(sequenceAction);

        // and finally we add the actors to the stage, on the correct order
        stage.addActor( splashImage );
    }
}
