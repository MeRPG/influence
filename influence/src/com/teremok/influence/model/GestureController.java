package com.teremok.influence.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teremok.influence.screen.GameScreen;
import com.teremok.influence.util.Logger;
import com.teremok.influence.util.Vibrator;

/**
 * Created by Alexx on 15.02.14
 */
public class GestureController extends ActorGestureListener{


    private static final float ZOOM_DEFAULT = 1.0f;
    private static final float ZOOM_STEP = 0.1f;
    private static final float ZOOM_MIN = 1.0f;
    private static final float ZOOM_MAX = 5.0f;

    private static boolean acting;

    private GameScreen screen;
    private static float zoom;

    public GestureController(GameScreen screen) {
        this.screen = screen;
        zoom = 1.0f;
        getGestureDetector().setLongPressSeconds(0.5f);
    }

    @Override
    public boolean longPress(Actor actor, float x, float y) {
        if (! screen.getMatch().isPaused()) {
            Cell hit =  getField().hit(x - getField().getX(),y - getField().getY());
            if (hit != null) {
                getField().addPowerFull(hit);
                Vibrator.bzz();
            }
        }

        return super.longPress(actor, x, y);
    }


    @Override
    public void zoom(InputEvent event, float initialDistance, float distance) {
        if (! screen.getMatch().isPaused()) {
            float delta = (distance - initialDistance) /  (getField().WIDTH * 10);
            changeZoom(delta);
            getField().resize();
            acting = true;
            //Logger.log("Zooom! delta: " + delta);
            //Logger.log("Zooom! zoom: " + zoom);
            //Logger.log("Zoomed unit size: " + Drawer.UNIT_SIZE*zoom);
        }
    }
      
    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        if (! screen.getMatch().isPaused()) {
            getField().moveBy(deltaX, deltaY);
            acting = true;
            ////Logger.log("pan! deltaX: " + deltaX + "; deltaY: " + deltaY);
        }
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (! screen.getMatch().isPaused()) {
            super.touchUp(event, x, y, pointer, button);
            acting = false;
            Logger.log("GestureController - touchUp ");
        }
    }

    @Override
    public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (! screen.getMatch().isPaused()) {
            super.touchDown(event, x, y, pointer, button);
            Logger.log("GestureController - touchDown ");
        }
    }

    public static void changeZoomBySteps(int steps) {
        changeZoom(ZOOM_STEP * steps);
    }

    public static void addZoom() {
        changeZoom(ZOOM_STEP);
    }


    public static void subZoom() {
        changeZoom(-ZOOM_STEP);
    }

    public static void changeZoom(float delta) {
        if (delta > 0) {
           if (zoom + delta > ZOOM_MAX) {
               zoom = ZOOM_MAX;
           } else {
               zoom += delta;
           }
        } else {
           if (zoom + delta < ZOOM_MIN) {
               zoom = ZOOM_MIN;
           } else {
               zoom += delta;
           }
        }
        //Logger.log("change zoom: " + zoom);
    }

    private Field getField() {
        return screen.getMatch().getField();
    }

    // Auto-generated

    public static boolean isActing() {
        return acting;
    }

    public static float getZoom() {
        return zoom;
    }
}
