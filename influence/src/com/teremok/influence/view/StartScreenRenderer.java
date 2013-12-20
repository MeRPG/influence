package com.teremok.influence.view;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.screen.StartScreen;

/**
 * Created by Alexx on 20.12.13.
 */
public class StartScreenRenderer extends Renderer {

    private StartScreen screen;

    public StartScreenRenderer(StartScreen screen) {
        super();
        this.screen = screen;
    }

    @Override
    protected void loadTextures() {

    }

    @Override
    protected void drawDebug() {

    }

    @Override
    public void render() {
        prerender();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.circle(64,64,UNIT_SIZE*2);
        renderer.end();
    }
}
