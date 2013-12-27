package com.teremok.influence.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
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

    public static void addTooltip(Tooltip tooltip) {
        int nextId = getInstance().getChildren().size+1;
        tooltip.setId(nextId);
        getInstance().addActor(tooltip);
    }

    public static void removeTooltip(int id) {
        Tooltip tooltip = null;
        for (Actor actor : getInstance().getChildren()) {
            if (actor instanceof Tooltip) {
                tooltip = (Tooltip)actor;
                if (tooltip.getId() == id) {
                    break;
                }
            }
        }
        if (tooltip != null)
            getInstance().removeActor(tooltip);
    }
}
