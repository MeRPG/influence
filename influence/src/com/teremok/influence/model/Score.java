package com.teremok.influence.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.view.Animation;
import com.teremok.influence.view.Drawer;

/**
 * Created by Alexx on 24.12.13
 */
public class Score extends Group {

    Match match;
    PlayerManager pm;
    String status;
    String subStatus;

    int lastTotalScore = 0;

    ColoredPanel[] panelsTop;
    ColoredPanel[] panelsBottom;
    ColoredPanel background;

    public Score(Match match) {
        this.match = match;
        this.pm = match.getPm();
        status = "---emptyStatus---";

        float actorX = 0;
        float actorY = 0;
        float actorWidth = AbstractScreen.WIDTH-1f;
        float actorHeight = AbstractScreen.HEIGHT - Field.HEIGHT-1f;

        setBounds(actorX, actorY, actorWidth, actorHeight);

        background = new ColoredPanel(Color.BLACK.cpy(), 0f, AbstractScreen.HEIGHT-8f, actorWidth, 8f);
        background.setTouchable(Touchable.disabled);
        this.addActor(background);
    }

    public void initColoredPanels() {
        panelsTop = new ColoredPanel[pm.getNumberOfPlayers()];
        panelsBottom = new ColoredPanel[pm.getNumberOfPlayers()];

        float panelWidth = getWidth()/pm.getNumberOfPlayers();
        for (int i = 0; i < pm.getNumberOfPlayers(); i++) {
            ColoredPanel newPanel = new ColoredPanel(Drawer.getCellColorByType(i),
                    panelWidth*i, getHeight()-16f, panelWidth, 8f
            );
            newPanel.setTouchable(Touchable.disabled);
            panelsTop[i] = newPanel;
            this.addActor(newPanel);
            ColoredPanel newNewPanel = newPanel.copy();
            newNewPanel.setTouchable(Touchable.disabled);
            panelsBottom[i] = newNewPanel;
            this.addActor(newNewPanel);
            newNewPanel.setY(getY());
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
        pm.update();
        if (match.isWon()) {
            status = Localizator.getString("youWon");
        } else if (match.isLost()) {
            status = Localizator.getString("youLost");
        }

        if (match.getPm().isHumanActing()){
            if (match.isInAttackPhase()) {
                subStatus = Localizator.getString("orTouchToEndAttack");
            } else {
                subStatus = Localizator.getString("orTouchToEndTurn");
            }
        } else {
            subStatus = null;
        }

        updateWidth();
        updateBackground();
    }

    private void updateWidth(){
        int totalScore = pm.getTotalScore();
        if (lastTotalScore == totalScore) {
            return;
        }
        lastTotalScore = totalScore;

        float[] width = new float[pm.getNumberOfPlayers()];

        for (int i = 0; i < pm.getNumberOfPlayers(); i++) {
            width[i] = (AbstractScreen.WIDTH-1)*((float) (pm.getPlayers()[i]).getScore() / totalScore);
        }

        setPanelsWidths(width);
    }

    private void updateBackground() {
        Color scoreBackground = Drawer.getCellColorByType(pm.current().getType()).cpy();
        //scoreBackground.add(0.2f, 0.2f, 0.2f, 0.2f);

        background.addAction(
                Actions.color( scoreBackground, Animation.DURATION_SHORT)
        );
    }

    public void setPanelsWidths(float[] widths) {
        float prevWidth = 0;
        for (int i = 0; i < pm.getNumberOfPlayers(); i++) {
            panelsTop[i].addAction(
                Actions.parallel(
                    Actions.sizeTo(widths[i], panelsTop[i].getHeight(), Animation.DURATION_SHORT),
                    Actions.moveTo(prevWidth, panelsTop[i].getY(), Animation.DURATION_SHORT)
                )
            );


            panelsBottom[i].addAction(
                    Actions.parallel(
                            Actions.sizeTo(widths[i], panelsBottom[i].getHeight(), Animation.DURATION_SHORT),
                            Actions.moveTo(prevWidth, panelsBottom[i].getY(), Animation.DURATION_SHORT)
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubStatus() {
        return subStatus;
    }
}
