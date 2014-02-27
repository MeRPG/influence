package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.model.*;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.ui.TexturePanel;
import com.teremok.influence.ui.TooltipHandler;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.Logger;
import com.teremok.influence.util.ResourseManager;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.view.Animation;
import com.teremok.influence.view.Drawer;

import static com.badlogic.gdx.Input.Keys;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen extends StaticScreen {

    Match match;
    PausePanel pausePanel;
    TexturePanel backlight;

    long lastBackPress = 0;

    public static Color colorForBorder;

    public GameScreen(Game game, GameType gameType) {
        super(game, "gameScreen");
        match = new Match(gameType);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        match.act(delta);

        // TODO: refactor!
        if (colorForBorder != null) {
            flashBacklight(colorForBorder);
            colorForBorder = null;
        }
    }

    @Override
    public void show() {
        super.show();

        //updateMatchDependentActors();

        Logger.log("GameScreen: show;");
    }

    void initBacklight() {
        backlight = new TexturePanel(uiElements.get("backlight"));
        backlight.setColor(1f,1f,1f,0f);
    }

    void  startNewMatch() {
        //Logger.log("Starting new match");
        match = new Match(match.getGameType());
        updateMatchDependentActors();
    }

    private void updateMatchDependentActors() {
        stage.getRoot().clearChildren();
        stage.addActor(backlight);
        stage.addActor(match.getField());
        stage.addActor(match.getScore());
        stage.addActor(TooltipHandler.getInstance());
        stage.addActor(pausePanel);
//        stage.addActor(overlap);
    }

    void pauseMatch() {
        pausePanel.show();
        match.setPaused(true);
    }

    void resumeMatch() {
        match.setPaused(false);
        pausePanel.hide();
    }

    void backToStartScreen() {
        pausePanel.hide();
        ScreenController.showStartScreen();
                                                 /*
        SequenceAction sequenceAction = Actions.sequence(
                Actions.alpha(1f, Animation.DURATION_NORMAL),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        game.setScreen(new StartScreen(game));
                        return true;
                    }
                });
        overlap.addAction(sequenceAction);     */
    }

    void gracefullyStartNewMatch() {
        pausePanel.hide();
        SequenceAction sequenceAction = Actions.sequence(
                Actions.alpha(1f, Animation.DURATION_NORMAL),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        startNewMatch();
                        return true;
                    }
                },
                Actions.alpha(0f, Animation.DURATION_NORMAL)
        );

        overlap.addAction(sequenceAction);
    }

    public void flashBacklight(Color color) {
        backlight.setColor(color);
        backlight.getColor().a = 1f;

        SequenceAction sequenceAction = Actions.sequence(
                Actions.delay(Animation.DURATION_SHORT),
                Actions.alpha(0f, Animation.DURATION_NORMAL)
        );

        backlight.addAction(sequenceAction);
    }

    void gracefullyExitGame() {
        pausePanel.hide();
        SequenceAction sequenceAction = Actions.sequence(
                Actions.alpha(1f, Animation.DURATION_NORMAL),
                new Action() {
                    @Override
                    public boolean act(float delta) {
                        exitGame();
                        return true;
                    }
                }
        );

        overlap.addAction(sequenceAction);
    }

    @Override
    public void pause() {
        super.pause();
        pausePanel.dispose();
        FXPlayer.dispose();
        pauseMatch();
        Logger.log("GameScreen: show;");
    }

    @Override
    public void resume() {
        super.resume();
        FXPlayer.load();
        if (match.isPaused())
            pausePanel.show();
        Logger.log("GameScreen: show;");
    }

    @Override
    protected void addActors() {
        AbstractDrawer.setBitmapFont(getFont());
        initBacklight();
        pausePanel = new PausePanel(this);
        updateMatchDependentActors();
    }

    @Override
    protected void addListeners() {
        stage.addListener(new ClickListener() {

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (! event.isHandled()) {
                    GestureController.changeZoomBySteps(-amount);
                    match.getField().resize();
                }
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) instanceof Score;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled()) {
                    FXPlayer.playClick();
                    if (match.canHumanActing() && ! match.isEnded())
                        match.switchPhase();
                    if (match.isEnded())
                        gracefullyStartNewMatch();
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled()) {
                    if (keycode == Keys.R) {
                        gracefullyStartNewMatch();
                    }
                    if (keycode == Keys.L) {
                        Localizator.switchLanguage();
                    }
                    if (keycode == Keys.I) {
                        Settings.load();
                    }
                    if (keycode == Keys.O) {
                        Settings.save();
                    }
                    if (keycode == Keys.PLUS) {
                        GestureController.addZoom();
                        match.getField().resize();
                    }
                    if (keycode == Keys.MINUS) {
                        GestureController.subZoom();
                        match.getField().resize();
                    }
                    if (keycode == Keys.S) {
                        MatchSaver.save(match);
                    }
                    if (keycode == Keys.D) {
                        match = MatchSaver.load();
                        updateMatchDependentActors();
                    }
                    if (keycode == Keys.BACK || keycode == Keys.MENU || keycode == Keys.ESCAPE) {

                        if (keycode != Keys.MENU) {
                            long newTime = System.currentTimeMillis();

                            if (newTime - lastBackPress < 250) {
                                backToStartScreen();
                            }

                            lastBackPress = newTime;
                        }

                        if (match.isPaused()) {
                            resumeMatch();
                        } else {
                            pauseMatch();
                        }

                    }
                }
                return true;
            }

        });

        stage.addListener(new GestureController(this));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Logger.log("GameScreen: resize;");
    }

    @Override
    public void hide() {
        super.hide();
        Logger.log("GameScreen: hide;");
    }

    @Override
    public void dispose() {
        super.dispose();
        Logger.log("GameScreen: dispose;");
    }


    // Auto-generated


    public Match getMatch() {
        return match;
    }
}
