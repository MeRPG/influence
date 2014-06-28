package com.teremok.influence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.teremok.influence.Influence;
import com.teremok.influence.util.ResourceManager;

/**
 * Created by Alexx on 27.12.13
 */
public class TooltipHandler extends Group {

    private static TooltipHandler instance;

    private ResourceManager resourceManager;

    public static TooltipHandler getInstance() {
        if (instance == null)
            instance = new TooltipHandler();
        return instance;
    }

    private TooltipHandler() {
        resourceManager = ((Influence) Gdx.app.getApplicationListener()).getResourceManager();
    }

    public void addTooltip(String message, Color color, float x, float y, float offset) {
        int nextId = getInstance().getChildren().size+1;
        Tooltip tooltip = new Tooltip(message, resourceManager.getFont("substatusFont"), color, x, y);
        tooltip.setId(nextId);
        tooltip.addActions(offset);
        this.addActor(tooltip);
    }
}
