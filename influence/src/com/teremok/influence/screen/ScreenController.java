package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.teremok.influence.Influence;
import com.teremok.influence.model.GameType;
import com.teremok.influence.view.Animation;

/**
 * Created by Alexx on 24.02.14
 */
public class ScreenController {

    private static Influence game;

    private static AboutScreen aboutScreen;
    private static StartScreen startScreen;
    private static SettingsScreen settingsScreen;
    private static StaticScreen currentScreen;

    public static void init(Influence game) {
        ScreenController.game = game;
    }

    public static void showStartScreen() {
        if (startScreen == null) {
            startScreen = new StartScreen(game, "startScreen");
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
            settingsScreen = new SettingsScreen(game, "settingsScreen");
        }
        gracefullyShowScreen(settingsScreen);
    }

    public static void showAboutScreen() {
        if (aboutScreen == null) {
            aboutScreen = new AboutScreen(game, "aboutScreen");
        }
        gracefullyShowScreen(aboutScreen);
    }

    private static void gracefullyShowScreen(StaticScreen screen) {
        currentScreen.initOverlap(true);
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

    public static void startMultiplayerGame () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createStartGameAction(GameType.MULTIPLAYER)
        );
        currentScreen.overlap.addAction(sequenceAction);
    }

    public static void startSingleplayerGame () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createStartGameAction(GameType.SINGLEPLAYER)
        );
        currentScreen.overlap.addAction(sequenceAction);
    }

    private static Action createStartGameAction(GameType gameType) {
        return new StartGameAction(game, gameType);
    }

    public static class StartGameAction extends Action {
        Game game;
        GameType gameType;
        private StartGameAction(Game game, GameType gameType) {
            this.gameType = gameType;
            this.game = game;
        }
        @Override
        public boolean act(float delta) {
            game.setScreen(new GameScreen(game, gameType));
            return true;
        }
    }

}
