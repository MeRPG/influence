package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.Influence;
import com.teremok.framework.util.FontFactory;

/**
 * Created by Alexx on 24.12.13
 */
public class AbstractDrawer <T extends Actor> {

    protected T current;
    protected ShapeRenderer renderer;

    protected FontFactory fontFactory;


    protected AbstractDrawer () {
        renderer = new ShapeRenderer();
        fontFactory = new FontFactory((Influence)Gdx.app.getApplicationListener());
    }

    public void draw(T actor, Batch batch, float parentAlpha) {
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

    private static FieldShapeDrawer fieldShapeDrawer;

    public static FieldShapeDrawer getFieldShapeDrawer() {
        if (fieldShapeDrawer == null) {
            fieldShapeDrawer = new FieldShapeDrawer();
        }
        return fieldShapeDrawer;
    }
}
