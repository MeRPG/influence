package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teremok.influence.model.*;
import com.teremok.influence.ui.TexturePanel;
import com.teremok.influence.ui.TooltipHandler;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.Logger;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.view.Animation;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen extends StaticScreen {

    Match match;
    PausePanel pausePanel;
    TexturePanel backlight;

    TexturePanel borderTop;
    TexturePanel borderRight;
    TexturePanel borderBottom;
    TexturePanel borderLeft;

    Map<TexturePanel, Boolean> borderState = new HashMap<>();
    boolean backlightState;

    public static boolean editor = false;
    public static boolean shifted = false;
    public static int fromRoute = -1;

    long lastBackPress = 0;

    public static Color colorForBacklight;

    public GameScreen(Game game) {
        super(game, "gameScreen");
        match = new Match(Settings.gameSettings);
    }

    public GameScreen(Game game, Match match) {
        super(game, "gameScreen");
        this.match = match;
        resumeMatch();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        match.act(delta);

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
            if (match.getField().getX() < -5) {
                turnOnBorder(borderLeft);
            } else {
                turnOffBorder(borderLeft);
            }

            if (match.getField().getX() + match.getField().getWidth() > AbstractScreen.WIDTH + 5) {
                turnOnBorder(borderRight);
            } else {
                turnOffBorder(borderRight);
            }

            if (match.getField().getY() < AbstractScreen.HEIGHT - Field.HEIGHT - 5) {
                turnOnBorder(borderBottom);
            } else {
                turnOffBorder(borderBottom);
            }

            if (match.getField().getY() + match.getField().getHeight() > AbstractScreen.HEIGHT + 5) {
                turnOnBorder(borderTop);
            } else {
                turnOffBorder(borderTop);
            }
        }
    }

    private void turnOnBorder(TexturePanel border) {
       if (!borderState.get(border)) {
            Logger.log("border turned on");
            SequenceAction sequenceAction = Actions.sequence(
                    Actions.alpha(1f, Animation.DURATION_NORMAL)
            );
            border.addAction(sequenceAction);
            borderState.put(border, true);
       }
    }

    private void turnOffBorder(TexturePanel border) {
        if (borderState.get(border)) {
            Logger.log("border turned off");
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
        Logger.log("GameScreen: show;");
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
        //Logger.log("Starting new match");
        if (match == null) {
            match = new Match(Settings.gameSettings);
        } else {
            match.reset(Settings.gameSettings);
        }
        updateMatchDependentActors();
    }

    private void updateMatchDependentActors() {
        stage.getRoot().clearChildren();
        stage.addActor(backlight);
        stage.addActor(match.getField());

        if (fieldIsScrollable()) {
            stage.addActor(borderTop);
            stage.addActor(borderRight);
            stage.addActor(borderBottom);
            stage.addActor(borderLeft);
        }

        stage.addActor(match.getScore());
        stage.addActor(TooltipHandler.getInstance());
        stage.addActor(pausePanel);
        if (overlap != null) {
            stage.addActor(overlap);
        }
    }

    private boolean fieldIsScrollable() {
        return Settings.gameSettings.fieldSize == FieldSize.LARGE || Settings.gameSettings.fieldSize == FieldSize.XLARGE;
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
        ScreenController.showStartScreen();
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

    public void flashBacklight(Color color) {
        backlight.setColor(color);
        backlight.getColor().a = 1f;
        Logger.log("backlight : flash! " + backlight.getColor().a);
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
        initBorders();
        pausePanel = new PausePanel(this);
        updateMatchDependentActors();
    }

    @Override
    protected void addListeners() {

        final GestureController gestureController = new GestureController(this);

        stage.addListener(new ClickListener() {

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (! event.isHandled() && gestureController.bigField() && !match.isPaused()) {
                    GestureController.changeZoomBySteps(-amount);
                    match.getField().resize();
                }
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (editor) {
                    return match.getField().hit(x,y) != null;
                }
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
                        editor = false;
                    }
                    if (keycode == Keys.L) {
                        Localizator.switchLanguage();
                    }
                    if (keycode == Keys.U) {
                        try {
                            FileHandle fileHandle = Gdx.files.external("/.influence/atlas/test.txt");
                            URL website = new URL("http://timeforlime.ru/influence/test.txt");
                            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                            FileOutputStream fos = new FileOutputStream(fileHandle.file());
                            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                            fos.close();
                            rbc.close();

                            int i = Integer.parseInt(new String(fileHandle.readBytes()));
                            if (i > Settings.lastAboutScreen) {
                                fileHandle = Gdx.files.external("/.influence/atlas/aboutScreen.png");
                                website = new URL("http://timeforlime.ru/influence/aboutScreen.png");
                                rbc = Channels.newChannel(website.openStream());
                                fos = new FileOutputStream(fileHandle.file());
                                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                                fos.close();
                                rbc.close();

                                fileHandle = Gdx.files.external("/.influence/atlas/aboutScreen.pack");
                                website = new URL("http://timeforlime.ru/influence/aboutScreen.pack");
                                rbc = Channels.newChannel(website.openStream());
                                fos = new FileOutputStream(fileHandle.file());
                                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                                fos.close();
                                rbc.close();

                                Settings.lastAboutScreen = i;

                                Logger.log("new about screen: " + i);
                            } else {
                                Logger.log("no new about screen :( ");
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (keycode == Keys.I) {
                        Settings.load();
                    }
                    if (keycode == Keys.O) {
                        Settings.save();
                    }
                    if (keycode == Keys.PLUS && gestureController.bigField() && !match.isPaused()) {
                        GestureController.addZoom();
                        match.getField().resize();
                    }
                    if (keycode == Keys.MINUS && gestureController.bigField() && !match.isPaused()) {
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
                    if (keycode == Keys.E) {

                        if (! editor) {
                            switch (Settings.gameSettings.fieldSize) {
                                case SMALL:
                                    Settings.gameSettings.cellsCount = 18;
                                    break;
                                case NORMAL:
                                    Settings.gameSettings.cellsCount = 32;
                                    break;
                                case LARGE:
                                    Settings.gameSettings.cellsCount = 65;
                                    break;
                                case XLARGE:
                                    Settings.gameSettings.cellsCount = 133;
                                    break;
                            }
                            gracefullyStartNewMatch();
                            match.getField().loadHiddenCells();
                        } else {
                            match.getField().clearRoutes();
                        }

                        editor = !editor;
                        Logger.log("enable editor: " + editor);
                    }

                    if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) {
                        if (editor) {
                            shifted = true;
                            Logger.log("shift down");
                        }
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

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Keys.SHIFT_LEFT || keycode == Keys.SHIFT_RIGHT) {
                    if (editor) {
                        shifted = false;
                        fromRoute = -1;
                        Logger.log("shift up");
                    }
                }
                return super.keyUp(event, keycode);
            }
        });
        getMatch().getField().resize();
        stage.addListener(gestureController);
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
