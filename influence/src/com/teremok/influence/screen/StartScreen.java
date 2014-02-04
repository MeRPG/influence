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
import com.teremok.influence.model.GameType;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.ui.Button;
import com.teremok.influence.ui.ButtonColored;
import com.teremok.influence.ui.ButtonTexture;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.view.Animation;
import com.teremok.influence.view.Drawer;

/**
 * Created by Alexx on 20.12.13
 */
public class StartScreen extends AbstractScreen {

    private static final String SINGLEPLAYER = "singleplayer";
    private static final String MULTIPLAYER = "multiplayer";

    private Image background;
    private ColoredPanel overlap;

    public StartScreen(Game game) {
        super(game);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("startScreen.pack"));
        for (Texture tex : atlas.getTextures()) {
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        TextureRegion textureRegion = atlas.findRegion("background");
        background = new Image(new TextureRegionDrawable(textureRegion));
        background.setScaling(Scaling.fit);
        background.setAlign(Align.center);
        background.setTouchable(Touchable.disabled);

        ButtonTexture singleplayer = new ButtonTexture(SINGLEPLAYER,
                atlas.findRegion(SINGLEPLAYER + "_" + Localizator.getLanguage()),
                        115f, 296f);


        ButtonTexture multiplayer = new ButtonTexture(MULTIPLAYER,
                atlas.findRegion(MULTIPLAYER + "_" + Localizator.getLanguage()),
                115f, 192f);

        overlap = new ColoredPanel(Color.BLACK, 0, 0, WIDTH, HEIGHT);
        overlap.setTouchable(Touchable.disabled);
        overlap.addAction(Actions.alpha(0f, Animation.DURATION_NORMAL));

        stage.addActor(background);
        stage.addActor(singleplayer);
        stage.addActor(multiplayer);
        stage.addActor(overlap);
    }

    private float getCenterX(float width) {
        return (WIDTH - width) / 2;
    }

    @Override
    public void show() {
        super.show();
        FXPlayer.load();
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) instanceof Button;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled()) {
                    FXPlayer.playClick();
                    Button target = (Button)event.getTarget();
                    if (target.getCode().equals(SINGLEPLAYER)) {
                        startSingleplayerGame();
                    } else {
                        startMultiplayerGame();
                    }
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    exitGame();
                    return true;
                }
                return false;
            }
        });
    }

    public void startMultiplayerGame () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createStartGameAction(GameType.MULTIPLAYER)
        );
        overlap.addAction(sequenceAction);
    }

    public void startSingleplayerGame () {
        SequenceAction sequenceAction = Actions.sequence(
            Actions.fadeIn(Animation.DURATION_NORMAL),
            createStartGameAction(GameType.SINGLEPLAYER)
        );
        overlap.addAction(sequenceAction);
    }

    private Action createStartGameAction(GameType gameType) {
        return new StartGameAction(game, gameType);
    }

    public static class StartGameAction extends Action {
        Game game;
        GameType gameType;
        private StartGameAction(Game game, GameType gameType) {
            this.gameType = gameType;
            this.game = game;
        }
        @Override
        public boolean act(float delta) {
            game.setScreen(new GameScreen(game, gameType));
            return true;
        }
    }

    @Override
    public void resume() {
        super.resume();
        FXPlayer.load();
    }
}
