package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Chronicle;
import com.teremok.influence.ui.Label;

/**
 * Created by Алексей on 29.06.2014
 */
public class MatchStatsPanel extends Group {
    
    static final String FONT = "ratingFont";

    TextureAtlas atlas;

    TextureRegion background;

    Influence game;
    GameScreen gameScreen;

    Chronicle.MatchChronicle matchChronicle;
    int influenceDiff;

    MatchStatsPanel(GameScreen gameScreen, Chronicle.MatchChronicle matchChronicle, int influenceDiff) {
        this.gameScreen = gameScreen;
        this.game = ((Influence) Gdx.app.getApplicationListener());
        this.matchChronicle = matchChronicle;
        this.influenceDiff = influenceDiff;

        getColor().a = 0f;
        setTouchable(Touchable.disabled);
        setBounds(0,0, AbstractScreen.WIDTH, AbstractScreen.HEIGHT);

        addActors();
        addListeners();
    }

    private void addActors() {

        atlas = gameScreen.getGame().getResourceManager().getAtlas("pausePanel");

        background = atlas.findRegion("background");
        Image backImage = new Image( new TextureRegionDrawable(background), Scaling.fit, Align.center );
        this.addActor(backImage);

        float baseLine = 550;
        float betweenLines = -40;

        Label influenceLabel = new Label("Influence earned: ",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines, false);

        Label influenceValue = new Label(influenceDiff+"",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines, false);

        this.addActor(influenceLabel);
        this.addActor(influenceValue);

        Label damageLabel = new Label("Damage: ",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines*2, false);

        Label damageValue = new Label(matchChronicle.damage+"",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines*2, false);

        this.addActor(damageLabel);
        this.addActor(damageValue);

        Label damageGetLabel = new Label("Damage get: ",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines*3, false);

        Label damageGetValue = new Label(matchChronicle.damageGet+"",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines*3, false);

        this.addActor(damageGetLabel);
        this.addActor(damageGetValue);

        Label cellsConqueredLabel = new Label("Cells conquered: ",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines*4, false);

        Label cellsConqueredValue = new Label(matchChronicle.cellsConquered+"",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines*4, false);

        this.addActor(cellsConqueredLabel);
        this.addActor(cellsConqueredValue);

        Label cellsLostLabel = new Label("Cells lost: ",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines*5, false);

        Label cellsLostValue = new Label(matchChronicle.cellsLost+"",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines*5, false);

        this.addActor(cellsLostLabel);
        this.addActor(cellsLostValue);

        Label newInfluenceLabel = new Label("New influence: ",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                64, baseLine+betweenLines*6, false);

        Label newInfluenceValue = new Label(game.getChronicle().influence+" (" + (influenceDiff < 0 ? "-" : "+")+ ") ",
                game.getResourceManager().getFont(FONT), Color.LIGHT_GRAY.cpy(),
                320, baseLine+betweenLines*6, false);

        this.addActor(newInfluenceLabel);
        this.addActor(newInfluenceValue);
    }

    private void addListeners() {
        this.addListener( new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                gameScreen.hideInfoMessageAnimation();
            }
        });
    }
}
