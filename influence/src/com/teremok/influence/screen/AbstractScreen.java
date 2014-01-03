package com.teremok.influence.screen;

/**
 * Created by Alexx on 22.12.13
 * */

import android.graphics.Color;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class AbstractScreen implements Screen {

    public static final float WIDTH = 480f;
    public static final float HEIGHT = 720f;

    protected final Game game;
    protected BitmapFont font;
    protected final Stage stage;
    protected SpriteBatch batch;

    public AbstractScreen(Game game) {
        this.game = game;
        this.stage = new Stage( 0, 0, true );
    }

    public BitmapFont getFont() {
        if (font == null)  {
            font = new BitmapFont(
                    Gdx.files.internal("test.fnt"),
                    Gdx.files.internal("test.png"), false);
        }
        return font;
    }

    public SpriteBatch getBatch() {
        if (batch == null)
            batch = stage.getSpriteBatch();
        return batch;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor( stage );
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(WIDTH, HEIGHT, true);
        // выравнивание камеры по центру
        stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);
    }

    @Override
    public void render(float delta) {
        stage.act( delta );

        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        // dispose the collaborators
        stage.dispose();
        if (font  != null) font.dispose();
    }
}