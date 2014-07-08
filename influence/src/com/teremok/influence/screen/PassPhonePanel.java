package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.framework.ui.Label;
import com.teremok.framework.util.FontFactory;
import com.teremok.influence.view.Drawer;

/**
 * Created by Алексей on 01.07.2014
 */
public class PassPhonePanel extends Popup<GameScreen> {

    private int nextPlayerNumber;
    private ShapeRenderer renderer;
    public PassPhonePanel(GameScreen screen, int nextPlayerNumber) {
        super(screen, "pausePanel");
        this.nextPlayerNumber = nextPlayerNumber;

        renderer = new ShapeRenderer();

        addActors();
        addListeners();
    }

    @Override
    protected void addActors() {
        FontFactory fontFactory = currentScreen.getFontFactory();
        Label passPhone = new Label("Pass your phone to the ", fontFactory.getFont(FontFactory.POPUP),
                Color.LIGHT_GRAY.cpy(), 64, 510, false);
        this.addActor(passPhone);


        Label nextPlayer = new Label("next player", fontFactory.getFont(FontFactory.POPUP),
                Drawer.getCellColorByNumber(nextPlayerNumber), 64 + passPhone.getTextBounds().width, 510, false);
        this.addActor(nextPlayer);
    }

    @Override
    protected void addListeners() {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());

        if (currentScreen.match.getGameSettings().darkness) {
            drawBlackBackground();
        }

        batch.begin();
        super.draw(batch, parentAlpha);
    }

    private void drawBlackBackground() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Color black = new Color(0,0,0,getColor().a);

        renderer.setColor(black);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        float fieldX = currentScreen.getMatch().getFieldController().getX();
        float fieldY = currentScreen.getMatch().getFieldController().getY();
        float fieldWidth = currentScreen.getMatch().getFieldController().getWidth();
        float fieldHeight = currentScreen.getMatch().getFieldController().getHeight();
        renderer.rect(fieldX, fieldY, fieldWidth, fieldHeight-8);
        renderer.end();
    }
}
