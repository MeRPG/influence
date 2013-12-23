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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teremok.influence.util.RenderHelper;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen implements Screen {
    protected final Game game;
    protected BitmapFont font;
    protected SpriteBatch batch;
    protected Skin skin;
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

    public SpriteBatch getBatch() {
        if (batch == null)
            batch = new SpriteBatch();
        return batch;
    }

    public Skin getSkin() {
        if( skin == null ) {
            skin = new Skin( Gdx.files.internal( "uiskin.json" ), new TextureAtlas(Gdx.files.internal( "uiskin.png" )) );
        }
        return skin;
    }

    protected String getName()
    {
        return getClass().getSimpleName();
    }

    // Screen implementation

    @Override
    public void show() {
        Gdx.input.setInputProcessor( stage );
    }

    @Override
    public void resize(int width, int height) {
        // resize the stage
        stage.setViewport(RenderHelper.SCREEN_WIDTH, RenderHelper.SCREEN_HEIGHT, true);
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
        if (stage != null) stage.dispose();
        if (batch != null) batch.dispose();
        if (font  != null) font.dispose();
    }
}