package com.teremok.influence.screen;

/**
 * Created by Alexx on 22.12.13
 * */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.teremok.influence.model.Settings;

public abstract class AbstractScreen implements Screen {

    public static final float WIDTH = 480f;
    public static final float HEIGHT = 720f;

    protected final Game game;
    protected BitmapFont font;
    protected final Stage stage;
    protected SpriteBatch batch;
    protected TextureAtlas atlas;

    public AbstractScreen(Game game) {
        this.game = game;
        this.stage = new Stage( 0, 0, true );
    }

    public BitmapFont getFont() {
        if (font == null)  {
            font = new BitmapFont(
                    Gdx.files.internal("font/mainFont.fnt"),
                    Gdx.files.internal("font/mainFont.png"), false
            );
        }
        return font;
    }

    public SpriteBatch getBatch() {
        if (batch == null)
            batch = stage.getSpriteBatch();
        return batch;
    }

    void exitGame() {
        Settings.save();
        Gdx.app.exit();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor( stage );
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
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
        stage.dispose();
        if (atlas != null) atlas.dispose();
        if (font  != null) font.dispose();
    }
}