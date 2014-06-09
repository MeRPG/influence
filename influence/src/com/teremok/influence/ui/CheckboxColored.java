package com.teremok.influence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Alexx on 08.02.14
 */
public class CheckboxColored extends Checkbox {

    Color color;
    Color colorOn;
    Color colorOff;
    ShapeRenderer renderer;

    public CheckboxColored(String code, Color colorOn, Color colorOff, float x, float y, float width, float height) {
        this.code = code;
        this.color = colorOn;
        this.colorOn = colorOn;
        this.colorOff = colorOff;

        setColor(color);
        setBounds(x, y, width, height);

        renderer = new ShapeRenderer();
    }

    @Override
    public void check() {
        super.check();
        color = colorOn;
        setColor(color);
        //Logger.log(code + " checked!");
    }

    @Override
    public void unCheck() {
        super.unCheck();
        color = colorOff;
        setColor(color);
        //Logger.log(code + " unchecked!");
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            check();
        } else {
            unCheck();
        }
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
}
