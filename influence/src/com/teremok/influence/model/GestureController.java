package com.teremok.influence.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teremok.influence.screen.GameScreen;
import com.teremok.influence.util.Logger;
import com.teremok.influence.util.Vibrator;
import com.teremok.influence.view.Drawer;

/**
 * Created by Alexx on 15.02.14
 */
public class GestureController extends ActorGestureListener{


    private static final float ZOOM_DEFAULT = 1.0f;
    private static final float ZOOM_STEP = 0.1f;
    private static final float ZOOM_MIN = 0.2f;
    private static final float ZOOM_MAX = 5.0f;

    private GameScreen screen;
    private static float zoom;

    public GestureController(GameScreen screen) {
        this.screen = screen;
        zoom = 1.0f;
        getGestureDetector().setLongPressSeconds(0.5f);
    }

    @Override
    public boolean longPress(Actor actor, float x, float y) {
        Actor hit =  getField().hit(x - getField().getX(),y - getField().getY(),true);
        /*if ( hit instanceof Cell) {
            Cell cell = (Cell)hit;
            getField().addPowerFull(cell);
            Vibrator.bzz();
        }
        */
        return super.longPress(actor, x, y);
    }


    @Override
    public void zoom(InputEvent event, float initialDistance, float distance) {
        float delta = (distance - initialDistance) /  getField().WIDTH;
        changeZoom(delta);
        getField().resize();
        Logger.log("Zooom! delta: " + delta);
        Logger.log("Zooom! zoom: " + zoom);
        Logger.log("Zoomed unit size: " + Drawer.UNIT_SIZE*zoom);
    }
      
    @Override
    public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
        getField().setX( getField().getX() + deltaX);
        getField().setY( getField().getY() + deltaY);
        Logger.log("pan! deltaX: " + deltaX + "; deltaY: " + deltaY);
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
        Logger.log("change zoom: " + zoom);
    }

    private Field getField() {
        return screen.getMatch().getField();
    }

    // Auto-generated


    public static float getZoom() {
        return zoom;
    }
}
