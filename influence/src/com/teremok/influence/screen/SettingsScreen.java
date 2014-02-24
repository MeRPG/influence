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
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.*;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.Logger;
import com.teremok.influence.util.ResourseManager;
import com.teremok.influence.util.Vibrator;
import com.teremok.influence.view.Animation;

/**
 * Created by Alexx on 08.02.14
 */
public class SettingsScreen extends AbstractScreen {

    private static final String SOUNDS = "sounds";
    private static final String VIBRATE = "vibrate";
    private static final String SPEED_SLOW = "slow";
    private static final String SPEED_NORMAL = "normal";
    private static final String SPEED_FAST = "fast";

    private static final int LANGUAGE_GROUP = 1;
    private static final int SPEED_GROUP = 2;

    private Image background;
    private ColoredPanel overlap;

    int width, height;

    public SettingsScreen(Game game) {
        super(game);
        Settings.init();
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
                                resize(width, height);
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

        this.width = width;
        this.height = height;

        if (atlas != null) {
            atlas.dispose();
        }
        atlas = ResourseManager.getAtlas("settingsScreen_" + Localizator.getLanguage());
        TextureRegion textureRegion = atlas.findRegion("background");
        background = new Image(new TextureRegionDrawable(textureRegion));
        background.setScaling(Scaling.fit);
        background.setAlign(Align.center);
        background.setTouchable(Touchable.disabled);

        TextureRegion soundOn = atlas.findRegion("soundOn");
        TextureRegion soundOff = atlas.findRegion("soundOff");

        TextureRegion vibrationOn = atlas.findRegion("vibrationOn");
        TextureRegion vibrationOff = atlas.findRegion("vibrationOff");

        TextureRegion languageEnglishOn = atlas.findRegion("languageEnglishOn");
        TextureRegion languageEnglishOff = atlas.findRegion("languageEnglishOff");
        TextureRegion languageRussianOn = atlas.findRegion("languageRussianOn");
        TextureRegion languageRussianOff = atlas.findRegion("languageRussianOff");

        TextureRegion speedSlowOn = atlas.findRegion("speedSlowOn");
        TextureRegion speedSlowOff = atlas.findRegion("speedSlowOff");
        TextureRegion speedNormalOn = atlas.findRegion("speedNormalOn");
        TextureRegion speedNormalOff = atlas.findRegion("speedNormalOff");
        TextureRegion speedFastOn = atlas.findRegion("speedFastOn");
        TextureRegion speedFastOff = atlas.findRegion("speedFastOff");

        float[][] coords = new float[2][7];

        if (Localizator.getLanguage().equals(Localizator.LANGUAGE_RUSSIAN)) {
            coords[0][0] = 79f;
            coords[1][0] = 720f-347f;
            coords[0][1] = 265f;
            coords[1][1] = 720f-352f;
            coords[0][2] = 42f;
            coords[1][2] = 720f-490f;
            coords[0][3] = 231f;
            coords[1][3] = 720f-489f;
            coords[0][4] = 0f;
            coords[1][4] = 720f-629f;
            coords[0][5] = 146f;
            coords[1][5] = 720f-629f;
            coords[0][6] = 312f;
            coords[1][6] = 720f-629f;
        } else {
            coords[0][0] = 79f;
            coords[1][0] = 720f-347f;
            coords[0][1] = 265f;
            coords[1][1] = 720f-352f;
            coords[0][2] = 42f;
            coords[1][2] = 720f-490f;
            coords[0][3] = 231f;
            coords[1][3] = 720f-489f;
            coords[0][4] = 18f;
            coords[1][4] = 720f-635f;
            coords[0][5] = 140f;
            coords[1][5] = 720f-635f;
            coords[0][6] = 314f;
            coords[1][6] = 720f-635f;
        }

        Checkbox sounds = new CheckboxTexture(SOUNDS,
                soundOn,
                soundOff,
                coords[0][0], coords[1][0]);
        sounds.setChecked(Settings.sound);

        Checkbox vibrate = new CheckboxTexture(VIBRATE,
                vibrationOn,
                vibrationOff,
                coords[0][1], coords[1][1]);
        vibrate.setChecked(Settings.vibrate);

        RadioGroup languageGroup = new RadioGroup(LANGUAGE_GROUP);
        Checkbox russian = new RadioTexture(Localizator.LANGUAGE_RUSSIAN,
                languageRussianOn,
                languageRussianOff,
                coords[0][2], coords[1][2]);

        Checkbox english = new RadioTexture(Localizator.LANGUAGE_ENGLISH,
                languageEnglishOn,
                languageEnglishOff,
                coords[0][3], coords[1][3]);

        russian.addToGroup(languageGroup);
        english.addToGroup(languageGroup);

        if (Localizator.getLanguage().equals(Localizator.LANGUAGE_RUSSIAN)) {
            russian.check();
        } else {
            english.check();
        }

        RadioGroup speedGroup = new RadioGroup(SPEED_GROUP);
        Checkbox slow = new RadioTexture(SPEED_SLOW,
                speedSlowOn,
                speedSlowOff,
                coords[0][4], coords[1][4]);

        Checkbox normal = new RadioTexture(SPEED_NORMAL,
                speedNormalOn,
                speedNormalOff,
                coords[0][5], coords[1][5]);

        Checkbox fast = new RadioTexture(SPEED_FAST,
                speedFastOn,
                speedFastOff,
                coords[0][6], coords[1][6]);

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

        stage.addActor(background);
        stage.addActor(sounds);

        stage.addActor(vibrate);

        stage.addActor(russian);
        stage.addActor(english);

        stage.addActor(slow);
        stage.addActor(normal);
        stage.addActor(fast);
        stage.addActor(overlap);

        Logger.log("SettingsScreen: show;");
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

    @Override
    public void hide() {
        super.hide();
        Logger.log("SettingsScreen: hide;");
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Logger.log("SettingsScreen: resize;");
    }

    @Override
    public void pause() {
        super.pause();
        Logger.log("SettingsScreen: pause;");
    }

    @Override
    public void resume() {
        super.resume();
        Logger.log("SettingsScreen: resume;");
    }

    @Override
    public void dispose() {
        super.dispose();
        Logger.log("SettingsScreen: dispose;");
    }
}
