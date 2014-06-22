package com.teremok.influence.ui;

import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Created by Alexx on 27.12.13
 */
public class TooltipHandler extends Group {

    private static TooltipHandler instance;

    public static TooltipHandler getInstance() {
        if (instance == null)
            instance = new TooltipHandler();
        return instance;
    }

    public static void addTooltip(Tooltip tooltip, float offset) {
        int nextId = getInstance().getChildren().size+1;
        tooltip.setId(nextId);
        tooltip.addActions(offset);
        getInstance().addActor(tooltip);
    }
}
