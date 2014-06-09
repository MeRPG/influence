package com.teremok.influence.screen;

/**
 * Created by Alexx on 22.12.13
 * */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teremok.influence.util.FontFactory;

public abstract class AbstractScreen implements Screen {

    public static final float WIDTH = 480f;
    public static final float HEIGHT = 720f;

    protected final Game game;
    protected BitmapFont font;
    protected final Stage stage;
    protected Batch batch;
    protected TextureAtlas atlas;

    public AbstractScreen(Game game) {
        this.game = game;
        this.stage = new Stage();
        Camera camera = new OrthographicCamera();
        Viewport viewport = new FitViewport(WIDTH, HEIGHT, camera);
        camera.translate(WIDTH/2, HEIGHT/2, 0);
        stage.setViewport(viewport);
    }

    public BitmapFont getFont() {
        if (font == null)  {
            font = FontFactory.getCellsFont();
        }
        return font;
    }

    public Batch getBatch() {
        if (batch == null)
            batch = stage.getBatch();
        return batch;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor( stage );
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void render(float delta) {
        stage.getCamera().update();
        stage.act(delta);

        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        Gdx.gl.glEnable(GL20.GL_BLEND);
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