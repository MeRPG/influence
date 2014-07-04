package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teremok.influence.Influence;
import com.teremok.influence.controller.*;
import com.teremok.influence.model.*;
import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.ui.TexturePanel;
import com.teremok.influence.ui.TooltipHandler;
import com.teremok.influence.util.FlurryHelper;
import com.teremok.influence.util.Localizator;
import com.teremok.influence.view.Animation;
import com.teremok.influence.view.Drawer;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys;

/**
 * Created by Alexx on 11.12.13
 */

public class GameScreen extends StaticScreen {

    Match match;
    MatchSaver matchSaver;

    Chronicle chronicle;
    ChronicleController chronicleController;

    SettingsController settingsController;

    GestureController gestureController;

    PausePanel pausePanel;
    TexturePanel backlight;

    TexturePanel borderTop;
    TexturePanel borderRight;
    TexturePanel borderBottom;
    TexturePanel borderLeft;

    Map<TexturePanel, Boolean> borderState = new HashMap<>();
    boolean backlightState;

    long lastBackPress = 0;
    boolean endSoundPlayed = false;

    public static Color colorForBacklight;

    public GameScreen(Influence game) {
        super(game, "gameScreen");

        chronicle = game.getChronicle();
        chronicleController = game.getChronicleController();

        settingsController = game.getSettingsController();

        Chronicle.MatchChronicle matchChronicle = chronicleController.matchStart();
        match = new Match(game.getSettings().gameSettings, matchChronicle);
        matchSaver = game.getMatchSaver();
        matchSaver.save(match);
    }

    public GameScreen(Influence game, Match match) {
        super(game, "gameScreen");
        chronicle = game.getChronicle();
        chronicleController = game.getChronicleController();
        settingsController = game.getSettingsController();
        matchSaver = game.getMatchSaver();
        this.match = match;
        resumeMatch();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Match.Phase phase = match.getPhase();
        int turn = match.getTurn();

        GameSettings gameSettings = match.getGameSettings();

        match.act(delta);

        if (match.getTurn() > turn && ! match.isEnded()) {
            matchSaver.save(match);
            if (match.getTurn() == 1) {
                FlurryHelper.logMatchStartEvent(match.getGameSettings());
            }
        }

        // TODO вынести сюда переключение игрока на следующего

        if (!match.getPhase().equals(phase)) {
            if (match.getPhase() == Match.Phase.ATTACK) {
                if (match.getPm().getNumberOfHumansInGame() > 1 && match.getPm().getNextPlayer() instanceof HumanPlayer) {
                    match.setPaused(true);
                    addInfoMessage(new PassPhonePanel(this, match.getPm().getNextPlayerNumber()));
                    showInfoMessageAnimation(
                            new Action() {
                                @Override
                                public boolean act(float delta) {
                                    match.getPm().nextCurrentPlayer();
                                    match.setPaused(false);
                                    return true;
                                }
                            }
                    );
                } else {
                    match.getPm().nextCurrentPlayer();
                }
            }
        }

        // TODO: refactor!
        if (colorForBacklight != null) {
            if (match.isEnded()) {
                turnOnBacklight(colorForBacklight);
            } else {
                flashBacklight(colorForBacklight);
                colorForBacklight = null;
            }
        }
        if (fieldIsScrollable()) {
            if (match.getFieldController().getX() < -5) {
                turnOnBorder(borderLeft);
            } else {
                turnOffBorder(borderLeft);
            }

            if (match.getFieldController().getX() + match.getFieldController().getWidth() > AbstractScreen.WIDTH + 5) {
                turnOnBorder(borderRight);
            } else {
                turnOffBorder(borderRight);
            }

            if (match.getFieldController().getY() < AbstractScreen.HEIGHT - FieldModel.HEIGHT - 5) {
                turnOnBorder(borderBottom);
            } else {
                turnOffBorder(borderBottom);
            }

            if (match.getFieldController().getY() + match.getFieldController().getHeight() > AbstractScreen.HEIGHT + 5) {
                turnOnBorder(borderTop);
            } else {
                turnOffBorder(borderTop);
            }
        }

        if (match.isWon()) {
            if (! endSoundPlayed) {
                game.getFXPlayer().playWinMatch();
                matchSaver.clearFile();
                endSoundPlayed = true;
                if (match.getPm().getNumberOfHumans() == 1) {
                    int lastInfluence = chronicle.influence;
                    chronicle.match = match.getMatchChronicle();
                    chronicleController.matchEnd(chronicle, gameSettings.players, gameSettings.fieldSize, true);
                    int deltaInfluence = chronicle.influence - lastInfluence;
                    addInfoMessage(new MatchStatsPanel(this, chronicle.match, deltaInfluence));
                    showInfoMessageAnimation();
                }
                GameScreen.colorForBacklight = Drawer.getPlayerColor(match.getPm().current());
                FlurryHelper.logMatchEndEvent(FlurryHelper.END_REASON_WIN, match.getTurn());
            }
        } else if (match.isLost()) {
            if (! endSoundPlayed) {
                game.getFXPlayer().playLoseMatch();
                matchSaver.clearFile();
                endSoundPlayed = true;
                if (match.getPm().getNumberOfHumans() == 1) {
                    int lastInfluence = chronicle.influence;
                    chronicle.match = match.getMatchChronicle();
                    chronicleController.matchEnd(chronicle, gameSettings.players, gameSettings.fieldSize, false);
                    int deltaInfluence = chronicle.influence - lastInfluence;
                    addInfoMessage(new MatchStatsPanel(this, chronicle.match, deltaInfluence));
                    showInfoMessageAnimation();
                }

                GameScreen.colorForBacklight = Drawer.getBacklightLoseColor();
                FlurryHelper.logMatchEndEvent(FlurryHelper.END_REASON_LOSE, match.getTurn());
            }
        }

        if (match.getPm().getNumberOfPlayerInGame() == 1) {
            GameScreen.colorForBacklight = Drawer.getPlayerColor(match.getPm().current());
        }

    }

    private void turnOnBorder(TexturePanel border) {
       if (!borderState.get(border)) {
            SequenceAction sequenceAction = Actions.sequence(
                    Actions.alpha(1f, Animation.DURATION_NORMAL)
            );
            border.addAction(sequenceAction);
            borderState.put(border, true);
       }
    }

    private void turnOffBorder(TexturePanel border) {
        if (borderState.get(border)) {
            SequenceAction sequenceAction = Actions.sequence(
                    Actions.alpha(0f, Animation.DURATION_NORMAL)
            );
            border.addAction(sequenceAction);
            borderState.put(border, false);
        }
    }


    @Override
    public void show() {
        super.show();
        Gdx.app.debug(getClass().getSimpleName(), "GameScreen: show;");
    }

    void initBacklight() {
        backlight = new TexturePanel(uiElements.get("backlight"));
        backlight.setColor(1f, 1f, 1f, 0f);
    }

    void initBorders() {
        borderTop = new TexturePanel(uiElements.get("borderTop"));
        borderTop.setColor(1f, 1f, 1f, 0f);
        borderState.put(borderTop, false);

        borderRight = new TexturePanel(uiElements.get("borderRight"));
        borderRight.setColor(1f, 1f, 1f, 0f);
        borderState.put(borderRight, false);

        borderBottom = new TexturePanel(uiElements.get("borderBottom"));
        borderBottom.setColor(1f, 1f, 1f, 0f);
        borderState.put(borderBottom, false);

        borderLeft = new TexturePanel(uiElements.get("borderLeft"));
        borderLeft.setColor(1f, 1f, 1f, 0f);
        borderState.put(borderLeft, false);

    }

    void  startNewMatch() {
        Settings settings = game.getSettings();
        Chronicle.MatchChronicle matchChronicle = chronicleController.matchStart();
        if (match == null) {
            match = new Match(settings.gameSettings, matchChronicle);
        } else {
            match.reset(settings.gameSettings, matchChronicle);
        }
        endSoundPlayed = false;
        matchSaver.save(match);
        settingsController.save(settings);
        updateMatchDependentActors();
    }

    private void updateMatchDependentActors() {
        stage.getRoot().clearChildren();
        stage.addActor(backlight);
        stage.addActor(match.getFieldController());

        if (fieldIsScrollable()) {
            stage.addActor(borderTop);
            stage.addActor(borderRight);
            stage.addActor(borderBottom);
            stage.addActor(borderLeft);
        }

        stage.addActor(match.getScoreController());
        stage.addActor(TooltipHandler.getInstance());
        stage.addActor(pausePanel);
        if (overlap != null) {
            stage.addActor(overlap);
        }
    }

    private boolean fieldIsScrollable() {
        GameSettings gameSettings = game.getSettings().gameSettings;
        return gameSettings.fieldSize == FieldSize.LARGE || gameSettings.fieldSize == FieldSize.XLARGE;
    }

    void pauseMatch() {
        if(pausePanel != null)
            pausePanel.show();
        match.setPaused(true);
    }

    void resumeMatch() {
        match.setPaused(false);
        if(pausePanel != null)
            pausePanel.hide();
    }

    void backToStartScreen() {
        pausePanel.hide();
        screenController.showStartScreen();
    }

    void gracefullyStartNewMatch() {
        pausePanel.hide();
        match.setPaused(false);
        initOverlap(true);
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

    public void exitGame() {
        game.exit();
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

    public void turnOnBacklight(Color color) {
        if (!backlightState && backlight.getColor().toIntBits() != color.toIntBits())
            backlight.addAction(Actions.color(color, Animation.DURATION_NORMAL));
    }

    public void turnOffBacklight(Color color) {
        if (backlightState) {
            Color real = color.cpy();
            real.a = 0f;
            backlight.addAction(Actions.color(real, Animation.DURATION_NORMAL));
        }
    }

    @Override
    public void pause() {
        super.pause();
        pauseMatch();
        Gdx.app.debug(getClass().getSimpleName(), "GameScreen: show;");
    }

    @Override
    public void resume() {
        super.resume();
        if (match.isPaused())
            pausePanel.show();
        Gdx.app.debug(getClass().getSimpleName(), "GameScreen: show;");
    }

    @Override
    protected void addActors() {
        initBacklight();
        initBorders();
        pausePanel = new PausePanel(this);
        updateMatchDependentActors();
    }

    @Override
    protected void addListeners() {

        gestureController = new GestureController(game.getSettings().gameSettings, this);

        stage.addListener(new ClickListener() {

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (! event.isHandled() && gestureController.bigField() && !match.isPaused()) {
                    gestureController.changeZoomBySteps(-amount);
                    match.getFieldController().resize();
                }
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) instanceof ScoreController;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled()) {
                    game.getFXPlayer().playClick();
                    if (isInfoMessageVisible()) {
                        hideInfoMessageAnimation();
                        return;
                    }
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
                    if (keycode == Keys.PLUS && gestureController.bigField() && !match.isPaused()) {
                        gestureController.addZoom();
                        match.getFieldController().resize();
                    }
                    if (keycode == Keys.MINUS && gestureController.bigField() && !match.isPaused()) {
                        gestureController.subZoom();
                        match.getFieldController().resize();
                    }
                    if (keycode == Keys.S) {
                        matchSaver.save(match);
                    }
                    if (keycode == Keys.D) {
                        match = matchSaver.load();
                        updateMatchDependentActors();
                    }
                    if (keycode == Keys.BACK || keycode == Keys.MENU || keycode == Keys.ESCAPE) {
                        if (isInfoMessageVisible()) {
                            hideInfoMessageAnimation();
                            return true;
                        }
                        if (keycode != Keys.MENU) {
                            long newTime = System.currentTimeMillis();

                            if (newTime - lastBackPress < 250) {
                                backToStartScreen();
                                FlurryHelper.logDoubleClickExitMenuEvent();
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
        getMatch().getFieldController().resize();
        stage.addListener(gestureController);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Gdx.app.debug(getClass().getSimpleName(), "GameScreen: resize;");
    }

    @Override
    public void hide() {
        super.hide();
        Gdx.app.debug(getClass().getSimpleName(), "GameScreen: hide;");
    }

    @Override
    public void dispose() {
        super.dispose();
        Gdx.app.debug(getClass().getSimpleName(), "GameScreen: dispose;");
    }


    // Auto-generated


    public Match getMatch() {
        return match;
    }
}
