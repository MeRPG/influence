package com.teremok.framework.screen;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.teremok.framework.TeremokGame;
import com.teremok.framework.util.Animation;

/**
 * Created by Алексей on 08.07.2014
 */
public abstract class ScreenControllerImpl <T extends TeremokGame> implements ScreenController {

    protected T game;
    protected StaticScreen currentScreen;

    public ScreenControllerImpl(T game) {
        this.game = game;
    }

    @Override
    public void setScreen(String screenName) {
        StaticScreen newScreen = resolve(screenName);
        if (currentScreen == null) {
            currentScreen = newScreen;
            game.setScreen(newScreen);
        } else {
            gracefullyShowScreen(newScreen);
        }
    }

    private void gracefullyShowScreen(StaticScreen screen) {
        currentScreen.initOverlap(true);
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createScreenAction(screen)
        );

        currentScreen.overlap.addAction(sequenceAction);
    }

    public void  gracefullyExitGame() {
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
        currentScreen.overlap.addAction(sequenceAction);
    }

    public Action createScreenAction(final StaticScreen screen) {
        return new Action() {
            @Override
            public boolean act(float delta) {
                game.setScreen(screen);
                currentScreen = screen;
                return true;
            }
        };
    }

    public void exitGame() {
        game.exit();
    }

    // Auto-generated

    @Override
    public StaticScreen getCurrentScreen() {
        return currentScreen;
    }

    @Override
    public void setCurrentScreen(StaticScreen screen) {
        currentScreen = screen;
    }
}
