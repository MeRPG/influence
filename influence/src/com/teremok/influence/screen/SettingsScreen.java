package com.teremok.influence.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.*;
import com.teremok.influence.util.FlurryHelper;
import com.teremok.influence.util.Localizator;
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

    Settings settings;

    public SettingsScreen(Influence game, String filename) {
        super(game, filename);
        settings = game.getSettings();
    }

    @Override
    protected void addActors() {

        Checkbox sounds = new CheckboxTexture(uiElements.get(SOUNDS));
        sounds.setChecked(settings.sound);

        Checkbox vibrate = new CheckboxTexture(uiElements.get(VIBRATE));
        vibrate.setChecked(settings.vibrate);

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

        if (settings.speed == settings.NORMAL) {
            normal.check();
        } else if (settings.speed == settings.SLOW) {
            slow.check();
        } else if (settings.speed == settings.FAST) {
            fast.check();
        }

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
                                settings.vibrate = checkbox.isChecked();
                                Vibrator.bzz();
                            } else if (code.equals(SOUNDS)) {
                                settings.sound = checkbox.isChecked();
                            }
                        } else {
                            if (group.getCode() == LANGUAGE_GROUP) {
                                String oldLanguage = Localizator.getLanguage();
                                switch (code) {
                                    case Localizator.LANGUAGE_RUSSIAN:
                                        Localizator.setRussianLanguage();
                                        break;
                                    default:
                                        Localizator.setEnglishLanguage();
                                        break;
                                }
                                if (! oldLanguage.equals(Localizator.getLanguage())) {
                                    screenController.showSettingsScreen();
                                }
                            } else {
                                switch (code) {
                                    case SPEED_NORMAL:
                                        settings.speed = settings.NORMAL;
                                        break;
                                    case SPEED_FAST:
                                        settings.speed = settings.FAST;
                                        break;
                                    case SPEED_SLOW:
                                        settings.speed = settings.SLOW;
                                        break;
                                }
                            }
                        }
                    }
                    game.getFXPlayer().playClick();
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    game.getSettingsController().save(settings);
                    FlurryHelper.logSettingsChangeEvent(settings);
                    screenController.showStartScreen();
                    return true;
                }
                return false;
            }
        });
    }
}
