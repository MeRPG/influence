package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by Alexx on 20.12.13
 */
public class StartScreen extends AbstractScreen {

    public StartScreen(Game game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        StartScreenActor actor = new StartScreenActor(getFont());
        actor.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
            }
        });

        stage.addActor(actor);
    }

    private static class StartScreenActor extends Actor {

        BitmapFont font;

        public StartScreenActor(BitmapFont font) {
            this.font = font;
            setBounds(0, 0, WIDTH, HEIGHT);
        }

        @Override
        public void draw(SpriteBatch batch, float parentAlpha) {

            BitmapFont.TextBounds textBounds = font.getBounds("Touch to continue...");
            font.draw(batch, "Touch to continue...",    (WIDTH- textBounds.width)/2,
                                                        (HEIGHT - textBounds.height)/2 + textBounds.height);

            super.draw(batch, parentAlpha);
        }
    }


}
