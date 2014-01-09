package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.model.GameType;
import com.teremok.influence.ui.Button;
import com.teremok.influence.view.Animation;

/**
 * Created by Alexx on 20.12.13
 */
public class StartScreen extends AbstractScreen {

    private static final String SINGLEPLAYER = "singleplayer";
    private static final String MULTIPLAYER = "multiplayer";

    private TextureAtlas atlas;
    private TextureRegion background;

    public StartScreen(Game game) {
        super(game);
        atlas = new TextureAtlas(Gdx.files.internal("startScreen.pack"));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background = atlas.findRegion("background");

        Image backImage = new Image( new TextureRegionDrawable(background), Scaling.fit, Align.center );

        TextureRegion singlePlayerRegion =  atlas.findRegion(SINGLEPLAYER);
        TextureRegion multiPlayerRegion = atlas.findRegion(MULTIPLAYER);
        float centerX = getCenterX(singlePlayerRegion.getRegionWidth());
        Button singleplayer = new Button(SINGLEPLAYER, singlePlayerRegion, centerX, 360);
        Button multiplayer = new Button(MULTIPLAYER, multiPlayerRegion, centerX, 256);

        stage.addActor(backImage);
        stage.addActor(singleplayer);
        stage.addActor(multiplayer);

        stage.getRoot().getColor().a = 0;
        stage.getRoot().addAction(createFadeInAction(0.75f));
    }

    private float getCenterX(float width) {
        return (WIDTH - width) / 2;
    }

    @Override
    public void show() {
        super.show();

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) instanceof Button;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled()) {
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
                Actions.fadeOut(Animation.DURATION_NORMAL),
                createStartGameAction(GameType.MULTIPLAYER)
        );
        stage.addAction(sequenceAction);
    }

    public void startSingleplayerGame () {
        SequenceAction sequenceAction = Actions.sequence(
            Actions.fadeOut(Animation.DURATION_NORMAL),
            createStartGameAction(GameType.SINGLEPLAYER)
        );
        stage.addAction(sequenceAction);
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
}
