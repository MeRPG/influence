package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.model.GameType;
import com.teremok.influence.ui.Button;

/**
 * Created by Alexx on 20.12.13
 */
public class StartScreen extends AbstractScreen {

    private final String SINGLEPLAYER = "singleplayer";
    private final String MULTIPLAYER = "multiplayer";

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
        game.setScreen(new GameScreen(game, GameType.MULTIPLAYER));
    }

    public void startSingleplayerGame () {
        game.setScreen(new GameScreen(game, GameType.SINGLEPLAYER));
    }
}
