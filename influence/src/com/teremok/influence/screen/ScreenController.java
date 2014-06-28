package com.teremok.influence.screen;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Match;
import com.teremok.influence.util.FlurryHelper;
import com.teremok.influence.view.Animation;

/**
 * Created by Alexx on 24.02.14
 */
public class ScreenController {

    private Influence game;

    private StaticScreen currentScreen;

    public ScreenController(Influence game) {
        this.game = game;
    }

    public void showSplashScreen() {
        game.setScreen(new SplashScreen(game));
    }

    public void showStartScreen() {
        StartScreen startScreen = new StartScreen(game, "startScreen");
        startScreen.setKeepInMemory(true);
        FlurryHelper.logStartScreenEvent();
        if (currentScreen == null) {
            currentScreen = startScreen;
            game.setScreen(startScreen);
        } else {
            gracefullyShowScreen(startScreen);
        }
    }

    public void showSettingsScreen() {
        SettingsScreen settingsScreen = new SettingsScreen(game, "settingsScreen");
        FlurryHelper.logSettingsScreenEvent();
        gracefullyShowScreen(settingsScreen);
    }

    public void showModeScreen() {
        ModeScreen gameTypeScreen = new ModeScreen(game, "modeScreen");
        FlurryHelper.logModeScreenEvent();
        gracefullyShowScreen(gameTypeScreen);
    }

    public void showEditorScreen() {
        EditorScreen editorScreen = new EditorScreen(game, "gameScreen");
        gracefullyShowScreen(editorScreen);
    }

    public void showStatisticsScreen() {
        StatisticsScreen statisticsScreen = new StatisticsScreen(game, "statisticsScreen");
        FlurryHelper.logStatisticsScreenEvent();
        gracefullyShowScreen(statisticsScreen);
    }

    public void showMapSizeScreen() {
        MapSizeScreen mapSizeScreen = new MapSizeScreen(game, "mapSize");
        mapSizeScreen.setKeepInMemory(true);
        FlurryHelper.logMapSizeScreenEvent();
        gracefullyShowScreen(mapSizeScreen);
    }

    public void showPlayersScreen() {
        PlayersScreen playersScreen = new PlayersScreen(game, "players");
        playersScreen.setKeepInMemory(true);
        FlurryHelper.logPlayersScreenEvent();
        gracefullyShowScreen(playersScreen);
    }

    public void showAboutScreen() {
        AboutScreen aboutScreen = new AboutScreen(game, "aboutScreen");
        FlurryHelper.logAboutScreenEvent();
        gracefullyShowScreen(aboutScreen);
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

    public void exitGame() {
        game.exit();
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

    public void continueGame() {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createResumeGameAction()
        );
        currentScreen.overlap.addAction(sequenceAction);
    }

    public void startSingleplayerGame () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createStartGameAction()
        );
        currentScreen.overlap.addAction(sequenceAction);
    }

    private Action createStartGameAction() {
        return new StartGameAction(game);
    }

    private Action createResumeGameAction() {
        return new StartGameAction(game, game.getMatchSaver().getNotEnded());
    }

    public static class StartGameAction extends Action {
        Influence game;
        Match match;

        private StartGameAction(Influence game) {
            this.game = game;
        }

        private StartGameAction(Influence game, Match match) {
            this(game);
            this.match = match;
        }

        @Override
        public boolean act(float delta) {
            GameScreen gameScreen;
            if (match == null){
                gameScreen = new GameScreen(game);
            } else {
                gameScreen =  new GameScreen(game, match);
            }
            gameScreen.setKeepInMemory(true);
            game.getScreenController().currentScreen = gameScreen;
            game.setScreen(game.getScreenController().currentScreen);
            return true;
        }
    }
}
