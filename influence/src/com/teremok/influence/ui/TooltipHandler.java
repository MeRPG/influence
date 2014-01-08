package com.teremok.influence.ui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.*;
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

        tooltip.setTouchable(Touchable.disabled);
        tooltip.addAction(constructParallelAction(tooltip));

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


    private static Action createDelayAction(float delay){
        DelayAction delayAction = new DelayAction();
        delayAction.setDuration(delay);
        return delayAction;
    }

    private static Action constructParallelAction(Tooltip tooltip) {
        ParallelAction parallelAction = new ParallelAction();
        parallelAction.addAction(constructSequenceAction(tooltip.id));
        parallelAction.addAction(createMoveByAction());
        return parallelAction;
    }

    private static Action createMoveByAction() {
        MoveByAction moveByAction = new MoveByAction();
        moveByAction.setAmount(0, Drawer.UNIT_SIZE);
        moveByAction.setDuration(1.5f);
        return moveByAction;
    }

    private static Action constructSequenceAction(int id) {
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(createDelayAction(0.75f));
        sequenceAction.addAction(createFadeOutAction(0.75f));
        sequenceAction.addAction(createCompleteAction(id));
        return sequenceAction;
    }

    private static Action createFadeOutAction(float duration){
        AlphaAction fadeOut = new AlphaAction();
        fadeOut.setAlpha(0f);
        fadeOut.setDuration(duration);
        return fadeOut;
    }

    private static Action createCompleteAction(final int id){
        return new Action(){
            public boolean act( float delta ) {
                TooltipHandler.removeTooltip(id);
                return true;
            }
        };
    }
}
