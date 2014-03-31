package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.*;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.Logger;
import com.teremok.influence.util.Vibrator;

/**
 * Created by Alexx on 24.02.14
 */
public class SettingsScreen extends StaticScreen {

    private static final String SOUNDS = "sounds";
    private static final String VIBRATE = "vibrate";
    private static final String SPEED_SLOW = "slow";
    private static final String SPEED_NORMAL = "normal";
    private static final String SPEED_FAST = "fast";

    private static final int LANGUAGE_GROUP = 1;
    private static final int SPEED_GROUP = 2;


    public SettingsScreen(Game game, String filename) {
        super(game, filename);
    }

    @Override
    protected void addActors() {
        Checkbox sounds = new CheckboxTexture(uiElements.get(SOUNDS));
        sounds.setChecked(Settings.sound);

        Checkbox vibrate = new CheckboxTexture(uiElements.get(VIBRATE));
        vibrate.setChecked(Settings.vibrate);

        RadioGroup languageGroup = new RadioGroup(LANGUAGE_GROUP);
        Checkbox russian = new RadioTexture(uiElements.get(Localizator.LANGUAGE_RUSSIAN));
        Checkbox english = new RadioTexture(uiElements.get(Localizator.LANGUAGE_ENGLISH));

        russian.addToGroup(languageGroup);
        english.addToGroup(languageGroup);

        if (Localizator.getLanguage().equals(Localizator.LANGUAGE_RUSSIAN)) {
            russian.check();
        } else {
            english.check();
        }

        RadioGroup speedGroup = new RadioGroup(SPEED_GROUP);
        Checkbox slow = new RadioTexture(uiElements.get(SPEED_SLOW));

        Checkbox normal = new RadioTexture(uiElements.get(SPEED_NORMAL));

        Checkbox fast = new RadioTexture(uiElements.get(SPEED_FAST));

        slow.addToGroup(speedGroup);
        normal.addToGroup(speedGroup);
        fast.addToGroup(speedGroup);

        if (Settings.speed == Settings.NORMAL) {
            normal.check();
        } else if (Settings.speed == Settings.SLOW) {
            slow.check();
        } else if (Settings.speed == Settings.FAST) {
            fast.check();
        }

        stage.addActor(background);
        stage.addActor(sounds);

        stage.addActor(vibrate);

        stage.addActor(russian);
        stage.addActor(english);

        stage.addActor(slow);
        stage.addActor(normal);
        stage.addActor(fast);
    }

    @Override
    protected void addListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Actor hit = stage.hit(x,y,true);
                return hit != null;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled()) {
                    if (event.getTarget() instanceof Checkbox) {
                        Checkbox checkbox = (Checkbox)event.getTarget();
                        if (checkbox instanceof RadioColored || checkbox instanceof RadioTexture) {
                            checkbox.check();
                        } else if (checkbox.isChecked()) {
                            checkbox.unCheck();
                        } else {
                            checkbox.check();
                        }
                        String code = checkbox.getCode();
                        RadioGroup group = (RadioGroup)checkbox.getGroup();
                        if (group == null) {
                            if (code.equals(VIBRATE)) {
                                Settings.vibrate = checkbox.isChecked();
                                Vibrator.bzz();
                            } else if (code.equals(SOUNDS)) {
                                Settings.sound = checkbox.isChecked();
                            }
                        } else {
                            if (group.getCode() == LANGUAGE_GROUP) {
                                if (code.equals(Localizator.LANGUAGE_RUSSIAN)) {
                                    Localizator.setRussianLanguage();
                                } else {
                                    Localizator.setEnglishLanguage();
                                }
                                loaded = false;
                                ScreenController.showSettingsScreen();
                            } else {
                                if (code.equals(SPEED_NORMAL)) {
                                    Settings.speed = Settings.NORMAL;
                                } else if (code.equals(SPEED_FAST)) {
                                    Settings.speed = Settings.FAST;
                                } else if (code.equals(SPEED_SLOW)) {
                                    Settings.speed = Settings.SLOW;
                                }
                            }
                        }
                    }
                    FXPlayer.playClick();
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    Settings.save();
                    ScreenController.showStartScreen();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void show() {
        super.show();
        FXPlayer.load();
        Logger.log("SettingsScreen: show;");
    }
}
