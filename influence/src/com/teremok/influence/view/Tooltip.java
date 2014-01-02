package com.teremok.influence.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 * Created by Alexx on 27.12.13
 */
public class Tooltip extends Actor {

    int id;
    float delay;
    String message;
    BitmapFont font;
    Color color;

    public Tooltip(String message, BitmapFont font, Color color, float x, float y) {
        this.message = message;
        this.font = font;
        this.color = color;
        delay = 0;

        BitmapFont.TextBounds bounds = font.getBounds(message);

        setBounds(x, y, bounds.width, bounds.height);

        this.addAction(constructSequenceAction());
    }

    public Tooltip(String message, BitmapFont font, Color color, float x, float y, float delay) {
        this(message, font, color, x, y);
        this.delay = delay;
    }

    private Action createDelayAction(){
        DelayAction delayAction = new DelayAction();
        delayAction.setDuration(delay);
        return delayAction;
    }

    private Action constructSequenceAction() {
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(createDelayAction());
        sequenceAction.addAction(createFadeOutAction());
        sequenceAction.addAction(createCompleteAction());
        return sequenceAction;
    }

    private Action createFadeOutAction(){
        AlphaAction fadeOut = new AlphaAction();
        fadeOut.setAlpha(0f);
        fadeOut.setDuration(1f);
        return fadeOut;
    }

    private Action createCompleteAction(){
        return new Action(){
            public boolean act( float delta ) {
                TooltipHandler.removeTooltip(id);
                return true;
            }
        };
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.setColor(color);
        font.draw(batch, message, getX(), getY());
    }

    // Auto-generated

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
