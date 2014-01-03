package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Alexx on 03.01.14
 */
public class Button extends Actor {
    String code;
    private TextureRegion region;

    public Button(String code, TextureRegion region, float x, float y) {
        this.code = code;
        this.region = region;

        float actorWidth = region.getRegionWidth();
        float actorHeight = region.getRegionHeight();

        setBounds(x, y, actorWidth, actorHeight);

    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(region, getX(), getY());
    }

    // Auto-generated

    public String getCode() {
        return code;
    }
}
