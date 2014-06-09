package com.teremok.influence.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;
import com.teremok.influence.model.Match;
import com.teremok.influence.model.ScoreModel;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.view.Animation;
import com.teremok.influence.view.Drawer;
import com.teremok.influence.view.ScoreDrawer;

/**
 * Created by Alexx on 24.12.13
 */
public class ScoreController extends Group {

    Match match;
    PlayerManager pm;
    ScoreDrawer drawer;

    ScoreModel model;

    public ScoreController(Match match) {
        reset(match);
    }

    public void reset(Match match) {
        this.match = match;
        this.pm = match.getPm();
        drawer = new ScoreDrawer();

        model = new ScoreModel();
        model.resetStatus();

        float actorX = 0;
        float actorY = 0;
        float actorWidth = AbstractScreen.WIDTH-1f;
        float actorHeight = AbstractScreen.HEIGHT - FieldModel.HEIGHT-1f;

        setBounds(actorX, actorY, actorWidth, actorHeight);

        model.background = new ColoredPanel(Color.BLACK.cpy(), 0f, AbstractScreen.HEIGHT-8f, actorWidth, 8f);
        model.background.setTouchable(Touchable.disabled);

        this.getChildren().clear();
        this.addActor(model.background);
    }

    public void init() {
        int numberOfPlayers = pm.getNumberOfPlayers();
        model.initColoredPanels(numberOfPlayers, getY(), getWidth(), getHeight());
        for (int i = 0; i < numberOfPlayers; i++) {
            this.addActor(model.panelsTop[i]);
            this.addActor(model.panelsBottom[i]);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawer.draw(this, batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        pm.update();
        updateStatus();
        updateWidth();
        updateBackground();
    }

    private void updateStatus() {

        if (match.isWon()) {
            model.setWonStatus();
            return;
        }

        if (match.isLost()) {
            model.setLostStatus();
            return;
        }

        if (! pm.isHumanActing()) {
            model.setWaitStatus();
            return;
        }

        Cell selectedCell = match.getFieldController().selectedCell;

        if (match.isInAttackPhase()) {
            model.setAttackPhaseStatus();
            if (selectedCell != null) {
                if (selectedCell.getPower() > 1) {
                    model.setTouchToAttackStatus();
                } else if (noCellsToAttack()) {
                    model.setEndAttackStatus();
                } else {
                    model.setSelectMoreThanOneStatus();
                }
            }
        }

        if (match.isInPowerPhase()) {
            if (pm.current().getPowerToDistribute() > 0)
                model.setPowerPhaseStatus();
        }

    }

    boolean noCellsToAttack() {
        if (pm.current().getScore() == pm.current().getCells().size()){
            return true;
        } else {
            for (Cell cell : pm.current().getCells()) {
                if (cell.hasEnemies() && cell.getPower() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateWidth(){
        int totalScore = pm.getTotalScore();
        if (model.lastTotalScore == totalScore) {
            return;
        }
        model.lastTotalScore = totalScore;

        float[] width = new float[pm.getNumberOfPlayers()];

        for (int i = 0; i < pm.getNumberOfPlayers(); i++) {
            width[i] = (AbstractScreen.WIDTH-1)*((float) (pm.getPlayers()[i]).getScore() / totalScore);
        }

        setPanelsWidths(width);
    }

    private void updateBackground() {
        Color scoreBackground = Drawer.getCellColorByNumber(pm.current().getNumber()).cpy();
        model.background.addAction(
                Actions.color( scoreBackground, Animation.DURATION_SHORT)
        );
    }

    public void setPanelsWidths(float[] widths) {
        float prevWidth = 0;
        for (int i = 0; i < pm.getNumberOfPlayers(); i++) {
            model.panelsTop[i].addAction(
                Actions.parallel(
                    Actions.sizeTo(widths[i], model.panelsTop[i].getHeight(), Animation.DURATION_SHORT),
                    Actions.moveTo(prevWidth, model.panelsTop[i].getY(), Animation.DURATION_SHORT)
                )
            );

            model.panelsBottom[i].addAction(
                    Actions.parallel(
                            Actions.sizeTo(widths[i], model.panelsBottom[i].getHeight(), Animation.DURATION_SHORT),
                            Actions.moveTo(prevWidth, model.panelsBottom[i].getY(), Animation.DURATION_SHORT)
                    )
            );

            prevWidth += widths[i];
        }
    }

    // Auto-generated

    public Match getMatch() {
        return match;
    }

    public PlayerManager getPm() {
        return pm;
    }

    public ScoreModel getModel() { return model; }
}
