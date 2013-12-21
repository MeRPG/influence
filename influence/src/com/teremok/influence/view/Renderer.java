package com.teremok.influence.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Alexx on 20.12.13.
 */
public abstract class Renderer {

    public static final float UNIT_SIZE = 32f;

    public static final float SCREEN_WIDTH = UNIT_SIZE*10f; // 320px
    public static final float SCREEN_HEIGHT = UNIT_SIZE*15f; // 348px

    public static final float FIELD_WIDTH = UNIT_SIZE*10f;
    public static final float FIELD_HEIGHT = UNIT_SIZE*13f;

    protected OrthographicCamera cam;

    protected ShapeRenderer renderer = new ShapeRenderer();

    protected SpriteBatch spriteBatch;
    protected boolean debug = false;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {

        return debug;
    }

    public Renderer() {
        this.cam = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        cam.setToOrtho(true, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.cam.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);
        this.cam.update();
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    protected void prerender() {
        renderer.setProjectionMatrix(cam.combined);
    }

    abstract protected void loadTextures();
    abstract protected void drawDebug();
    abstract public void render();



}
