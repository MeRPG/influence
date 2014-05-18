package com.teremok.influence.model;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.view.Drawer;

/**
 * Created by Алексей on 18.05.2014
 */
public class ScoreModel {

    public String status;
    public String subStatus;

    public int lastTotalScore = 0;

    public ColoredPanel[] panelsTop;
    public ColoredPanel[] panelsBottom;
    public ColoredPanel background;

    public void initColoredPanels(int numberOfPlayers, float y, float width, float height) {
        panelsTop = new ColoredPanel[numberOfPlayers];
        panelsBottom = new ColoredPanel[numberOfPlayers];

        float panelWidth = width/numberOfPlayers;
        for (int i = 0; i < numberOfPlayers; i++) {
            ColoredPanel newPanel = new ColoredPanel(Drawer.getCellColorByNumber(i),
                    panelWidth*i, height-16f, panelWidth, 8f
            );
            newPanel.setTouchable(Touchable.disabled);
            panelsTop[i] = newPanel;
            ColoredPanel newNewPanel = newPanel.copy();
            newNewPanel.setTouchable(Touchable.disabled);
            panelsBottom[i] = newNewPanel;
            newNewPanel.setY(y);
        }
    }

    public void resetStatus() {
        status = "---emptyStatus---";
        subStatus = "";
    }

    public void setWonStatus() {
        status = Localizator.getString("youWon");
        subStatus = Localizator.getString("touchForNewGame");
    }

    public void setLostStatus() {
        status = Localizator.getString("youLost");
        subStatus = Localizator.getString("touchForNewGame");
    }

    public void setAttackPhaseStatus() {
        status = Localizator.getString("selectYourCell");
        subStatus = Localizator.getString("orTouchToEndAttack");
    }

    public void setSelectMoreThanOneStatus() {
        status = Localizator.getString("selectMoreThanOne");
        subStatus = Localizator.getString("orTouchToEndAttack");
    }

    public void setTouchToAttackStatus() {
        status = Localizator.getString("touchNearby");
        subStatus = Localizator.getString("orTouchToEndAttack");
    }

    public void setPowerPhaseStatus() {
        status = Localizator.getString("touchToPower");
        subStatus = Localizator.getString("orTouchToEndTurn");
    }

    public void setWaitStatus() {
        status = Localizator.getString("waitYourMove");
        subStatus = null;
    }

    public void setEndAttackStatus() {
        status = Localizator.getString("touchToEndAttack");
        subStatus = null;
    }

}
