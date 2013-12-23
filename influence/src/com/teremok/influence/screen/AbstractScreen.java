package com.teremok.influence.screen;

/**
 * Created by Alexx on 22.12.13
 * */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class AbstractScreen implements Screen {

    public static final float WIDTH = 320f;
    public static final float HEIGHT = 480f;

    protected final Game game;
    protected BitmapFont font;
    protected final Stage stage;

    public AbstractScreen(Game game) {
        this.game = game;
        this.stage = new Stage( 0, 0, true );
    }

    public BitmapFont getFont() {
        if (font == null)
            font = new BitmapFont();
        return font;
    }

    // Screen implementation

    @Override
    public void show() {
        Gdx.input.setInputProcessor( stage );
    }

    @Override
    public void resize(int width, int height) {
        // resize the stage
        stage.setViewport(WIDTH, HEIGHT, true);
// и выравниваем камеру по центру
        stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);
    }

    @Override
    public void render(float delta) {

        stage.act( delta );

        // the following code clears the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        // update and draw the stage actors
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