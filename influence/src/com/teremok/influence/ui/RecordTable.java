package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.net.Record;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.util.Logger;

import java.util.List;

/**
 * Created by Алексей on 03.06.2014
 */
public class RecordTable extends Actor {

    List<Record> records;
    BitmapFont font;

    public RecordTable(List<Record> records, BitmapFont font) {
        this.records = records;
        this.font = font;
        setX(50f);
        setY(600f);
        Logger.log("RecordTable created, size: " + records.size());
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.DARK_GRAY);
        renderer.rect(0f, 0f, AbstractScreen.WIDTH, AbstractScreen.HEIGHT);
        renderer.end();

        batch.begin();
        batch.setColor(Color.WHITE);
        Color fontColor = font.getColor();
        font.setColor(Color.WHITE.cpy());
        int i = 0;
        for (Record record : records) {
            i++;
            font.draw(batch, record.getPlace() + ". " + record.getName() + " - " + record.getInfluence(), getX(), getY()-50*i);
        }
        font.setColor(fontColor);
    }
}
