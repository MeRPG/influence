package com.teremok.influence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Alexx on 09.01.14
 */
public class ColoredPanel extends Actor {

    ShapeRenderer renderer;

    public ColoredPanel(Color color) {
        setColor(color.cpy());

        renderer = new ShapeRenderer();
    }

    public ColoredPanel(Color color, float x, float y, float width, float height) {
        this(color);

        setBounds(x, y, width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        Color color = getColor().cpy();
        color.a = getColor().a < parentAlpha ? getColor().a : parentAlpha;
        renderer.setColor(color);
        renderer.rect(0, 0, getWidth(), getHeight());
        renderer.end();

        batch.begin();
    }

    public ColoredPanel copy() {
        return new ColoredPanel(getColor().cpy(), getX(), getY(), getWidth(), getHeight());
    }
}
