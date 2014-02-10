package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.model.GameType;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.*;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.Vibrator;
import com.teremok.influence.view.Animation;
import com.teremok.influence.view.Drawer;

/**
 * Created by Alexx on 08.02.14
 */
public class SettingsScreen extends AbstractScreen {

    private static final String CHOOSE_LANGUAGE = "chooseLanguage";
    private static final String SOUNDS = "sounds";
    private static final String VIBRATE = "vibrate";
    private static final String SPEED = "computerPlayerSpeed";
    private static final String SPEED_SLOW = "slow";
    private static final String SPEED_NORMAL = "normal";
    private static final String SPEED_FAST = "fast";

    private static final int LANGUAGE_GROUP = 1;
    private static final int SPEED_GROUP = 2;

    private Image background;
    private ColoredPanel overlap;

    public SettingsScreen(Game game) {
        super(game);
        Settings.init();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        /*
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("settingsScreen.pack"));
        for (Texture tex : atlas.getTextures()) {
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        TextureRegion textureRegion = atlas.findRegion("background");
        background = new Image(new TextureRegionDrawable(textureRegion));
        background.setScaling(Scaling.fit);
        background.setAlign(Align.center);
        background.setTouchable(Touchable.disabled);
        */

        Label soundsLabel = new Label(SOUNDS, getFont(),Drawer.getTextColor(), 120f, 584f);

        Checkbox sounds = new CheckboxColored(SOUNDS,
                Drawer.getBacklightWinColor(),
                Drawer.getBacklightLoseColor(),
                150f, 510f, 50f, 50f);
        sounds.setChecked(Settings.sound);

        Label vibrateLabel = new Label(VIBRATE, getFont(),Drawer.getTextColor(), 120f, 484f);

        Checkbox vibrate = new CheckboxColored(VIBRATE,
                Drawer.getBacklightWinColor(),
                Drawer.getBacklightLoseColor(),
                150f, 410f, 50f, 50f);
        vibrate.setChecked(Settings.vibrate);

        Label language = new Label(CHOOSE_LANGUAGE, getFont(),Drawer.getTextColor(), 120f, 384f);

        RadioGroup languageGroup = new RadioGroup(LANGUAGE_GROUP);
        Checkbox russian = new CheckboxColored(Localizator.LANGUAGE_RUSSIAN,
                Drawer.getBacklightWinColor(),
                Drawer.getBacklightLoseColor(),
                150f, 310f, 50f, 50f);

        Checkbox english = new CheckboxColored(Localizator.LANGUAGE_ENGLISH,
                Drawer.getBacklightWinColor(),
                Drawer.getBacklightLoseColor(),
                280f, 310f, 50f, 50f);

        russian.addToGroup(languageGroup);
        english.addToGroup(languageGroup);

        if (Localizator.getLanguage().equals(Localizator.LANGUAGE_RUSSIAN)) {
            russian.check();
        } else {
            english.check();
        }

        Label speed = new Label(SPEED, getFont(),Drawer.getTextColor(), 120f, 284f);

        RadioGroup speedGroup = new RadioGroup(SPEED_GROUP);
        Checkbox slow = new CheckboxColored(SPEED_SLOW,
                Drawer.getBacklightWinColor(),
                Drawer.getBacklightLoseColor(),
                150f, 210f, 50f, 50f);

        Checkbox normal = new CheckboxColored(SPEED_NORMAL,
                Drawer.getBacklightWinColor(),
                Drawer.getBacklightLoseColor(),
                280f, 210f, 50f, 50f);

        Checkbox fast = new CheckboxColored(SPEED_FAST,
                Drawer.getBacklightWinColor(),
                Drawer.getBacklightLoseColor(),
                215f, 210f, 50f, 50f);

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

        overlap = new ColoredPanel(Color.BLACK, 0, 0, WIDTH, HEIGHT);
        overlap.setTouchable(Touchable.disabled);
        overlap.addAction(Actions.alpha(0f, Animation.DURATION_NORMAL));

        //stage.addActor(background);
        stage.addActor(soundsLabel);
        stage.addActor(sounds);

        stage.addActor(vibrateLabel);
        stage.addActor(vibrate);

        stage.addActor(language);
        stage.addActor(russian);
        stage.addActor(english);

        stage.addActor(speed);
        stage.addActor(slow);
        stage.addActor(normal);
        stage.addActor(fast);
        stage.addActor(overlap);
    }

    private float getCenterX(float width) {
        return (WIDTH - width) / 2;
    }

    @Override
    public void show() {
        super.show();
        FXPlayer.load();
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
                        if (checkbox.isChecked()) {
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
                    openSettingsScreen();
                    return true;
                }
                return false;
            }
        });
    }

    public void openSettingsScreen () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createStartScreenAction()
        );
        overlap.addAction(sequenceAction);
    }

    public Action createStartScreenAction() {
        return new Action() {
            @Override
            public boolean act(float delta) {
                game.setScreen(new StartScreen(game));
                return false;
            }
        };
    }
}
