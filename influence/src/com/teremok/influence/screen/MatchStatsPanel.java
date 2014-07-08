package com.teremok.influence.screen;

import com.badlogic.gdx.graphics.Color;
import com.teremok.influence.model.Chronicle;
import com.teremok.framework.ui.Label;
import com.teremok.framework.util.FontFactory;

/**
 * Created by Алексей on 29.06.2014
 */
public class MatchStatsPanel extends Popup<GameScreen> {
    
    static final String FONT = FontFactory.POPUP;

    Chronicle.MatchChronicle matchChronicle;
    int influenceDiff;

    MatchStatsPanel(GameScreen gameScreen, Chronicle.MatchChronicle matchChronicle, int influenceDiff) {
        super(gameScreen, "pausePanel");
        this.matchChronicle = matchChronicle;
        this.influenceDiff = influenceDiff;


        addActors();
        addListeners();
    }

    protected void addActors() {

        float baseLine = 550;
        float betweenLines = -40;

        FontFactory fontFactory = new FontFactory(game);

        Label influenceLabel = new Label("Influence earned: ",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines, false);

        Label influenceValue = new Label(influenceDiff+"",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines, false);

        this.addActor(influenceLabel);
        this.addActor(influenceValue);

        Label damageLabel = new Label("Damage: ",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines*2, false);

        Label damageValue = new Label(matchChronicle.damage+"",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines*2, false);

        this.addActor(damageLabel);
        this.addActor(damageValue);

        Label damageGetLabel = new Label("Damage get: ",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines*3, false);

        Label damageGetValue = new Label(matchChronicle.damageGet+"",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines*3, false);

        this.addActor(damageGetLabel);
        this.addActor(damageGetValue);

        Label cellsConqueredLabel = new Label("Cells conquered: ",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines*4, false);

        Label cellsConqueredValue = new Label(matchChronicle.cellsConquered+"",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines*4, false);

        this.addActor(cellsConqueredLabel);
        this.addActor(cellsConqueredValue);

        Label cellsLostLabel = new Label("Cells lost: ",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines*5, false);

        Label cellsLostValue = new Label(matchChronicle.cellsLost+"",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines*5, false);

        this.addActor(cellsLostLabel);
        this.addActor(cellsLostValue);

        Label newInfluenceLabel = new Label("New influence: ",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines*6, false);

        Label newInfluenceValue = new Label(game.getChronicle().influence+" (" + (influenceDiff < 0 ? "-" : "+")+ ") ",
                fontFactory.getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines*6, false);

        this.addActor(newInfluenceLabel);
        this.addActor(newInfluenceValue);
    }

    @Override
    protected void addListeners() {

    }
}
