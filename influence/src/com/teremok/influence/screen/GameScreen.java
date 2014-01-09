package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.model.GameType;
import com.teremok.influence.model.Match;
import com.teremok.influence.model.Score;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.ui.TooltipHandler;
import com.teremok.influence.view.Animation;
import com.teremok.influence.view.Drawer;

import static com.badlogic.gdx.Input.Keys;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen extends AbstractScreen {

    Match match;
    PausePanel pausePanel;
    ColoredPanel overlap;
    Image backlight;

    public static Color colorForBorder;

    public GameScreen(Game game, GameType gameType) {
        super(game);
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
    public void resize(int width, int height) {
        super.resize(width,height);

        AbstractDrawer.setBitmapFont(getFont());
        initOverlap();
        initBacklight();
        pausePanel = new PausePanel(this);

        updateMatchDependentActors();

        addInputListenerToStage();
    }

    void initOverlap() {
        overlap = new ColoredPanel(Color.BLACK, 0, 0, WIDTH, HEIGHT);
        overlap.setTouchable(Touchable.disabled);
        overlap.addAction(Actions.alpha(0f, Animation.DURATION_NORMAL));
    }

    void initBacklight() {
        Texture.setEnforcePotImages(false); // Удалить!
        Texture texture = new Texture("backlight.png");
        backlight = new Image(texture);
        backlight.setScaling(Scaling.fit);
        backlight.setAlign(Align.center);
        backlight.getColor().a = 0f;
        backlight.setTouchable(Touchable.disabled);
    }

    void addInputListenerToStage() {
        stage.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) instanceof Score;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled()) {
                    if (match.canHumanActing())
                        match.switchPhase();
                    if (match.isEnded())
                        startNewMatch();
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled()) {
                    if (keycode == Keys.R) {
                        gracefullyStartNewMatch();
                    }
                    if (keycode == Keys.BACK || keycode == Keys.MENU || keycode == Keys.ESCAPE) {
                        pauseMatch();
                    }
                }
                return true;
            }

        });
    }

    void  startNewMatch() {
        System.out.println("Starting new match");
        match = new Match(match.getGameType());
        updateMatchDependentActors();
    }

    private void updateMatchDependentActors() {
        stage.getRoot().clearChildren();
        stage.addActor(match.getField());
        stage.addActor(backlight);
        stage.addActor(match.getScore());
        stage.addActor(TooltipHandler.getInstance());
        stage.addActor(pausePanel);
        stage.addActor(overlap);
    }

    void pauseMatch() {
        if (match.isPaused()) {
            pausePanel.hide();
        } else {
            pausePanel.show();
        }
        match.setPaused(! match.isPaused());
    }

    void backToStartScreen() {
        pausePanel.hide();

        SequenceAction sequenceAction = Actions.sequence(
            Actions.alpha(1f, Animation.DURATION_NORMAL),
            new Action() {
                @Override
                public boolean act(float delta) {
                    game.setScreen(new StartScreen(game));
                    return true;
            }
        });
        overlap.addAction(sequenceAction);
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
        Drawer.dispose();
    }
}
