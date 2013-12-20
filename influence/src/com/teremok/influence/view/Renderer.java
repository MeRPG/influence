package com.teremok.influence.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Alexx on 20.12.13.
 */
public abstract class Renderer {

    protected static final float UNIT_SIZE = 32f;

    protected static final float CAMERA_WIDTH = UNIT_SIZE*10f;
    protected static final float CAMERA_HEIGHT = UNIT_SIZE*15f;

    protected static final float FIELD_HEIGHT = CAMERA_HEIGHT*.85f;
    protected static final float FIELD_WIDTH = CAMERA_WIDTH*1f;

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
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        cam.setToOrtho(true, CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
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
