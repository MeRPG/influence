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
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.model.GameType;
import com.teremok.influence.model.Match;
import com.teremok.influence.model.Score;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.ui.TooltipHandler;
import com.teremok.influence.view.Drawer;

import static com.badlogic.gdx.Input.Keys;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen extends AbstractScreen {

    Match match;
    PausePanel pausePanel;
    Image overlap;
    Image border;

    public static Color colorForBorder;

    public GameScreen(Game game, GameType gameType) {
        super(game);
        match = new Match(gameType);
        pausePanel = new PausePanel(this);
        pausePanel.getColor().a = 0f;
        pausePanel.setTouchable(Touchable.disabled);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        match.act(delta);
        if (colorForBorder != null) {
            fastShowBorder(colorForBorder);
            colorForBorder = null;
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);

        AbstractDrawer.setBitmapFont(getFont());

        TextureRegion reg =  new TextureAtlas(Gdx.files.internal("startScreen.pack")).findRegion("background");

        overlap = new Image( new TextureRegionDrawable(reg), Scaling.fit, Align.center);
        overlap.setColor(Color.BLACK);
        overlap.addAction(createFadeOutAction(0.75f));
        overlap.setTouchable(Touchable.disabled);


        Texture.setEnforcePotImages(false); // Удалить!
        Texture texture = new Texture("backlight.png");
        border = new Image(texture);
        border.setScaling(Scaling.fit);
        border.setAlign(Align.center);
        border.getColor().a = 0f;
        border.setTouchable(Touchable.disabled);

        updateMatchDependentActors();

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
                        startNewMatch();
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
        if (match.isPaused()) {
            pausePanel.addAction(createFadeOutAction(0.75f));
            pausePanel.setTouchable(Touchable.disabled);
        }
        System.out.println("Starting new match");
        match = new Match(match.getGameType());
        updateMatchDependentActors();
    }

    private void updateMatchDependentActors() {
        stage.addActor(match.getField());
        stage.addActor(border);
        stage.addActor(match.getScore());

        stage.getRoot().removeActor(TooltipHandler.getInstance());
        stage.addActor(TooltipHandler.getInstance());

        stage.addActor(pausePanel);
        stage.addActor(overlap);
    }

    void pauseMatch() {
        if (match.isPaused()) {
            pausePanel.addAction(createFadeOutAction(0.75f));
            pausePanel.setTouchable(Touchable.disabled);
        } else {
            pausePanel.addAction(createFadeInAction(0.75f));
            pausePanel.setTouchable(Touchable.enabled);
        }
        match.setPaused(! match.isPaused());
    }

    void backToStartScreen() {
        pausePanel.addAction(createFadeOutAction(0.75f));

        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(createFadeInAction(0.75f));
        sequenceAction.addAction( new Action() {
            @Override
            public boolean act(float delta) {
                game.setScreen(new StartScreen(game));
                return true;
            }
        });
        overlap.getColor().a = 0f;
        overlap.addAction(sequenceAction);
    }

    public void fastShowBorder(Color color) {
        border.setColor(color);
        border.getColor().a = 1f;

        SequenceAction sequenceAction = new SequenceAction();
        //sequenceAction.addAction(createFadeInAction(0.25f));
        sequenceAction.addAction(createDelayAction(0.25f));
        sequenceAction.addAction(createFadeOutAction(0.75f));

        border.addAction(sequenceAction);
    }

    @Override
    public void pause() {
        super.pause();
        Drawer.dispose();
    }
}
