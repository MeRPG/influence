package com.teremok.influence.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.teremok.influence.view.Animation;
import com.teremok.influence.view.Drawer;

/**
 * Created by Alexx on 27.12.13
 */
public class Tooltip extends Actor {

    int id;
    String message;
    BitmapFont font;
    Color color;

    public Tooltip(String message, BitmapFont font, Color color, float x, float y) {
        this.message = message;
        this.font = font;
        this.color = color;

        BitmapFont.TextBounds bounds = font.getBounds(message);
        setBounds(x, y, bounds.width, bounds.height);
        setTouchable(Touchable.disabled);
    }

    public void addActions() {
        ParallelAction parallelAction = Actions.parallel(
            fadeOutAfterDelay(),
            Actions.moveBy(0, Drawer.UNIT_SIZE, Animation.DURATION_LONG)
        );
        addAction(parallelAction);
    }

    private Action fadeOutAfterDelay() {
        return Actions.sequence(
            Actions.delay(0.5f),
            Actions.alpha(0f, 0.75f),
            Actions.removeActor()
        );
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        color.a = getColor().a;
        font.setColor(color);
        font.draw(batch, message, getX(), getY());
    }

    // Auto-generated

    public void setId(int id) {
        this.id = id;
    }
}
