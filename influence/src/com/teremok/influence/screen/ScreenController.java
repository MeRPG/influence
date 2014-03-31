package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Match;
import com.teremok.influence.model.MatchSaver;
import com.teremok.influence.model.Settings;
import com.teremok.influence.util.Logger;
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
    private static MapSizeScreen mapSizeScreen;
    private static PlayersScreen playersScreen;

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

    public static void showMapSizeScreen() {
        if (mapSizeScreen == null) {
            mapSizeScreen = new MapSizeScreen(game, "mapSize");
        }
        gracefullyShowScreen(mapSizeScreen);
    }

    public static void showPlayersScreen() {
        if (playersScreen == null) {
            playersScreen = new PlayersScreen(game, "players");
        }
        gracefullyShowScreen(playersScreen);
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

    public static void  gracefullyExitGame() {
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

    public static void exitGame() {
        Settings.save();
        Gdx.app.exit();
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

    public static void startQuickGame () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createResumeGameAction()
        );
        currentScreen.overlap.addAction(sequenceAction);
    }

    public static void startMultiplayerGame () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createStartGameAction()
        );
        currentScreen.overlap.addAction(sequenceAction);
    }

    public static void startSingleplayerGame () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createStartGameAction()
        );
        currentScreen.overlap.addAction(sequenceAction);
    }

    private static Action createStartGameAction() {
        return new StartGameAction(game);
    }

    private static Action createResumeGameAction() {
        return new StartGameAction(game, MatchSaver.getNotEnded());
    }

    public static class StartGameAction extends Action {
        Game game;
        Match match;

        private StartGameAction(Game game) {
            this.game = game;
        }

        private StartGameAction(Game game, Match match) {
            this(game);
            this.match = match;
        }

        @Override
        public boolean act(float delta) {
            if (match == null){
                currentScreen  =  new GameScreen(game);
            } else {
                currentScreen  =  new GameScreen(game, match);
            }
            game.setScreen(currentScreen);
            return true;
        }
    }

}
