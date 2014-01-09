package com.teremok.influence.ui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.teremok.influence.view.Animation;
import com.teremok.influence.view.Drawer;

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
        tooltip.addActions();
        getInstance().addActor(tooltip);
    }
}
