package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.Logger;
import com.teremok.influence.view.Animation;

/**
 * Created by Alexx on 20.12.13
 */
public class AboutScreen extends AbstractScreen {

    private Image background;
    private ColoredPanel overlap;
    private ColoredPanel credits;

    public AboutScreen(Game game) {
        super(game);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Logger.log("AboutScreen: resize;");
    }

    @Override
    public void hide() {
        super.hide();
        Logger.log("AboutScreen: hide;");
    }

    @Override
    public void pause() {
        super.pause();
        Logger.log("AboutScreen: pause;");
    }

    @Override
    public void dispose() {
        super.dispose();
        Logger.log("AboutScreen: dispose;");
    }

    @Override
    public void show() {
        super.show();
        FXPlayer.load();

        if (credits == null) {
            credits = new ColoredPanel(new Color(0x000000FF), 0f, 0f, WIDTH, 54f);
            stage.addActor(credits);
        }

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    openStartScreen();
                    return true;
                }
                return false;
            }
        });


        credits.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) == credits;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (!event.isHandled()) {
                    FXPlayer.playClick();
                    openStartScreen();
                }
            }
        });

        atlas = new TextureAtlas(Gdx.files.internal("aboutScreen.pack"));
        for (Texture tex : atlas.getTextures()) {
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        TextureRegion textureRegion = atlas.findRegion("background_" + Localizator.getLanguage());
        background = new Image(new TextureRegionDrawable(textureRegion));
        background.setScaling(Scaling.fit);
        background.setAlign(Align.center);
        background.setTouchable(Touchable.disabled);

        overlap = new ColoredPanel(Color.BLACK, 0, 0, WIDTH, HEIGHT);
        overlap.setTouchable(Touchable.disabled);
        overlap.addAction(Actions.alpha(0f, Animation.DURATION_NORMAL));

        stage.addActor(background);
        stage.addActor(overlap);

        Logger.log("AboutScreen: show;");
    }

    public void openStartScreen () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createStartScreenAction()
        );
        overlap.addAction(sequenceAction);
    }

    public Action createStartScreenAction() {
        return new Action() {
            @Override
            public boolean act(float delta) {
                game.setScreen(new StartScreen(game));
                return false;
            }
        };
    }

    @Override
    public void resume() {
        super.resume();
        FXPlayer.load();
    }
}
