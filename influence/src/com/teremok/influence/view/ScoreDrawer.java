package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.controller.ScoreController;
import com.teremok.influence.model.FieldSize;
import com.teremok.influence.model.GameSettings;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.ScoreModel;
import com.teremok.influence.screen.AbstractScreen;

import static com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import static com.teremok.influence.view.Drawer.getCellColorByNumber;
import static com.teremok.influence.view.Drawer.getTextColor;

/**
 * Created by Alexx on 24.12.13
 */
public class ScoreDrawer extends AbstractDrawer<ScoreController> {

    private static final float COVER_SIZE = 160;

    @Override
    public void draw(ScoreController score, Batch batch, float parentAlpha) {
        super.draw(score, batch, parentAlpha);

        batch.end();

        GameSettings gameSettings = score.getMatch().getGameSettings();

        if (! (gameSettings.fieldSize == FieldSize.SMALL
                || gameSettings.fieldSize == FieldSize.NORMAL)) {

           drawCovers();
        }

        batch.begin();

        drawStatusBar(batch);

        //drawStatusString(batch);
    }

    private void drawStatusBar(Batch batch) {
        drawStatus(batch);
        drawSubstatus(batch);
    }


    private void drawStatus(Batch batch) {
        String statusText = current.getModel().status;
        if (isAttackPhase(statusText)) {
            drawStatusAttackPhase(batch, current.getModel());
        } else if (isPowerPhase(statusText)) {
            drawStatusPowerPhase(batch, current.getModel());
        } else {
            drawAnyOtherStatus(batch, current.getModel());
        }
    }

    private boolean isAttackPhase(String statusText) {
        return Localizator.getString("selectYourCell").equals(statusText);
    }

    private void drawStatusAttackPhase(Batch batch, ScoreModel model) {
        BitmapFont statusFont = fontFactory.getStatusFont();
        String colorString = Localizator.getString("ofYourColor");
        TextBounds colorStringBounds = statusFont.getBounds(colorString, new TextBounds());
        TextBounds statusBounds = statusFont.getBounds(model.status, new TextBounds());

        float statusX = current.getWidth()/2 - statusBounds.width/2 - colorStringBounds.width/2;
        float statusY = 16 + (current.getHeight() - 24f + statusBounds.height)/2;

        float colorStringX = statusX + statusBounds.width;

        statusFont.setColor(Drawer.getTextColor());
        statusFont.draw(batch, model.status, current.getX() + statusX, current.getY() + statusY);
        statusFont.setColor(getCellColorByNumber(current.getPm().current().getNumber()));
        statusFont.draw(batch, colorString, current.getX() + colorStringX, current.getY() + statusY);
    }

    private boolean isPowerPhase(String statusText) {
        return Localizator.getString("touchToPower").equals(statusText);
    }

    private void drawStatusPowerPhase(Batch batch, ScoreModel model) {
        BitmapFont statusFont = fontFactory.getStatusFont();

        String powerString = " (" + current.getPm().current().getPowerToDistribute() + ")";
        String colorString = Localizator.getString("yourCells");
        String appendix = Localizator.getLanguage().equals(Localizator.LANGUAGE_GERMAN) ? Localizator.getString("auf") : "";

        TextBounds powerStringBounds = statusFont.getBounds(powerString, new TextBounds());
        TextBounds statusBounds = statusFont.getBounds(model.status, new TextBounds());
        TextBounds colorStringBounds = statusFont.getBounds(colorString, new TextBounds());
        TextBounds appendixBounds = statusFont.getBounds(appendix, new TextBounds());

        float statusX = current.getWidth()/2 - statusBounds.width/2
                        - colorStringBounds.width/2 - appendixBounds.width/2 - powerStringBounds.width/2;
        float statusY = current.getY() + 16 + (current.getHeight() - 24f + statusBounds.height)/2;

        float colorStringX = statusX + statusBounds.width;
        float appendixX = colorStringX + colorStringBounds.width;
        float powerStringX = appendixX + appendixBounds.width;

        statusFont.setColor(getTextColor());
        statusFont.draw(batch, model.status, current.getX() + statusX, current.getY() + statusY);
        statusFont.setColor(getCellColorByNumber(current.getPm().current().getNumber()));
        statusFont.draw(batch, colorString, current.getX() + colorStringX, current.getY() + statusY);
        statusFont.setColor(getTextColor());
        statusFont.draw(batch, appendix, current.getX() + appendixX, current.getY() + statusY);
        statusFont.setColor(getCellColorByNumber(current.getPm().current().getNumber()));
        statusFont.draw(batch, powerString, current.getX() + powerStringX, current.getY() + statusY);
    }

    private void drawAnyOtherStatus(Batch batch, ScoreModel model) {
        BitmapFont statusFont = fontFactory.getStatusFont();
        if (Localizator.getLanguage().equals(Localizator.LANGUAGE_GERMAN) && model.status.equals(Localizator.getString("touchNearby")))
            statusFont.setScale(0.95f, 1);
        TextBounds statusBounds = statusFont.getBounds(model.status);
        float statusX = current.getWidth()/2 - statusBounds.width/2;
        float statusY = current.getY() + 16 + (current.getHeight() - 24f + statusBounds.height)/2;
        statusFont.setColor(Drawer.getTextColor());
        statusFont.draw(batch, model.status, statusX, statusY);
        statusFont.setScale(1,1);
    }

    private void drawSubstatus(Batch batch) {
        ScoreModel model = current.getModel();
        if (model.substatusExists()) {
            BitmapFont substatusFont = fontFactory.getSubstatusFont();
            TextBounds substatusBounds = substatusFont.getBounds(model.subStatus);
            TextBounds statusBounds = substatusFont.getBounds(model.status, new TextBounds());
            float substatusX = current.getWidth() / 2 - substatusBounds.width / 2;
            float statusY = current.getY() + 16 + (current.getHeight() - 24f + statusBounds.height) / 2;
            float substatusY = statusY - substatusBounds.height * 1.5f;
            substatusFont.setColor(Drawer.getDimmedTextColor());
            substatusFont.draw(batch, model.subStatus, substatusX, substatusY);
        }
    }

    @Override
    protected void drawBoundingBox() {

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        renderer.setColor(Drawer.getBackgroundColor());
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(0,0,current.getWidth(), current.getHeight());
        renderer.end();
    }

    protected void drawCovers() {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        //left
        renderer.rect(-COVER_SIZE, current.getHeight()- COVER_SIZE, COVER_SIZE, AbstractScreen.HEIGHT+ COVER_SIZE *2);
        //bottom
        renderer.rect(-COVER_SIZE, current.getHeight()- COVER_SIZE, AbstractScreen.WIDTH+COVER_SIZE *2, COVER_SIZE-8f);
        //top
        renderer.rect(-COVER_SIZE, AbstractScreen.HEIGHT, AbstractScreen.WIDTH+ COVER_SIZE *2, COVER_SIZE);
        //right
        renderer.rect(AbstractScreen.WIDTH-1, current.getHeight()- COVER_SIZE, COVER_SIZE, AbstractScreen.HEIGHT+ COVER_SIZE *2);
        renderer.end();
    }
}
