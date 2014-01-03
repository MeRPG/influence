package com.teremok.influence.view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Alexx on 24.12.13
 */
public class AbstractDrawer <T extends Actor> {

    private static AbstractDrawer instance;

    protected T current;
    protected ShapeRenderer renderer;

    protected static BitmapFont bitmapFont;

    protected AbstractDrawer () {
        renderer = new ShapeRenderer();
    }

    public void draw(T actor, SpriteBatch batch, float parentAlpha) {
        current = actor;

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(current.getX(), current.getY(), 0);
    }

    protected void drawBoundingBox() {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(0,0,current.getWidth(), current.getHeight());
        renderer.end();
    }

    public static void setBitmapFont(BitmapFont bitmapFont) {
        AbstractDrawer.bitmapFont = bitmapFont;
    }

    public static BitmapFont getBitmapFont() {
        return bitmapFont;
    }
}
