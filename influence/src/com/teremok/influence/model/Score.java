package com.teremok.influence.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.view.Drawer;

/**
 * Created by Alexx on 24.12.13
 */
public class Score extends Actor {

    Field field;
    int[] scores;


    public Score(Field field) {
        this.field = field;
        scores = new int[Cell.MAX_TYPE+1];

        float actorX = 0;
        float actorY = 0;
        float actorWidth = AbstractScreen.WIDTH-1f;
        float actorHeight = AbstractScreen.HEIGHT - Field.HEIGHT-1f;

        setBounds(actorX, actorY, actorWidth, actorHeight);
    }

    public void update() {
        for (int i = 0; i < Cell.MAX_TYPE+1; i++) {
            scores[i] = 0;
        }
        for (Cell cell : field.getCells()) {
            if (cell.isValid()) {
                scores[cell.getType()] += cell.getPower();
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Drawer.draw(this, batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update();
    }

    // Auto-generated

    public int[] getScores() {
        return scores;
    }
}
