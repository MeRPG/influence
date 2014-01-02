package com.teremok.influence.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.view.Drawer;

/**
 * Created by Alexx on 24.12.13
 */
public class Score extends Actor {

    Field field;

    public Score(Field field) {
        this.field = field;

        float actorX = 0;
        float actorY = 0;
        float actorWidth = AbstractScreen.WIDTH-1f;
        float actorHeight = AbstractScreen.HEIGHT - Field.HEIGHT-1f;

        setBounds(actorX, actorY, actorWidth, actorHeight);
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Drawer.draw(this, batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Player.update();
    }

    // Auto-generated
}
