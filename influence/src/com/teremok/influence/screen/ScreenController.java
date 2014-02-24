package com.teremok.influence.screen;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.teremok.influence.Influence;
import com.teremok.influence.view.Animation;

/**
 * Created by Alexx on 24.02.14
 */
public class ScreenController {

    private static Influence game;

    private static StartScreenAlt startScreen;
    private static SettingsScreenAlt settingsScreen;
    private static StaticScreen currentScreen;

    public static void init(Influence game) {
        ScreenController.game = game;
    }

    public static void showStartScreen() {
        if (startScreen == null) {
            startScreen = new StartScreenAlt(game, "abstractScreen");
        }
        if (currentScreen == null) {
            currentScreen = startScreen;
            game.setScreen(startScreen);
        } else {
            gracefullyShowScreen(startScreen);
        }
    }

    public static void showSettingsScreen() {
        if (settingsScreen == null) {
            settingsScreen = new SettingsScreenAlt(game, "settingsScreen");
        }
        gracefullyShowScreen(settingsScreen);
    }

    private static void gracefullyShowScreen(StaticScreen screen) {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createScreenAction(screen)
        );
        currentScreen.overlap.addAction(sequenceAction);
    }


    public static Action createScreenAction(final StaticScreen screen) {
        return new Action() {
            @Override
            public boolean act(float delta) {
                game.setScreen(screen);
                currentScreen = screen;
                return true;
            }
        };
    }

}
