package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.model.player.Player;

/**
 * Created by Alexx on 20.12.13
 */
public class StartScreen extends AbstractScreen {

    private final String WELCOME_TEXT = "Touch to continue...";

    public StartScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                startSingleplayerGame();
            }
        });
    }

    public void startMultiplayerGame () {
        game.setScreen(new GameScreen(game, GameScreen.GameType.MULTIPLAYER));
    }


    public void startSingleplayerGame () {
        game.setScreen(new GameScreen(game, GameScreen.GameType.SINGLEPLAYER));
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch = getBatch();
        font = getFont();

        batch.begin();
        BitmapFont.TextBounds textBounds = font.getBounds(WELCOME_TEXT);
        font.draw(batch, WELCOME_TEXT,    (WIDTH- textBounds.width)/2,
                (HEIGHT - textBounds.height)/2 + textBounds.height);
        batch.end();
    }
}
