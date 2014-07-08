package com.teremok.influence.screen;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.teremok.framework.screen.ScreenControllerImpl;
import com.teremok.framework.screen.StaticScreen;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Match;
import com.teremok.influence.util.FlurryHelper;
import com.teremok.influence.view.Animation;

/**
 * Created by Alexx on 24.02.14
 */
public class InfluenceScreenController extends ScreenControllerImpl<Influence> {

    public static final String START_SCREEN = "startScreen";
    public static final String ABOUT_SCREEN = "aboutScreen";
    public static final String SETTINGS_SCREEN = "settingsScreen";
    public static final String EDITOR_SCREEN = "editorScreen";
    public static final String STATISTICS_SCREEN = "statisticsScreen";
    public static final String MAP_SIZE_SCREEN = "mapSize";
    public static final String MODE_SCREEN = "modeScreen";
    public static final String GAME_SCREEN = "gameScreen";
    public static final String PLAYERS_SCREEN = "players";

    public InfluenceScreenController(Influence game) {
        super(game);
    }

    @Override
    public StaticScreen resolve(String screenName) {
        StaticScreen screen = null;

        switch (screenName) {
            case START_SCREEN:
                screen = new StartScreen(game, START_SCREEN);
                screen.setKeepInMemory(true);
                FlurryHelper.logStartScreenEvent();
                break;
            case ABOUT_SCREEN:
                screen = new AboutScreen(game, ABOUT_SCREEN);
                FlurryHelper.logAboutScreenEvent();
                break;
            case MODE_SCREEN:
                screen = new ModeScreen(game, MODE_SCREEN);
                screen.setKeepInMemory(true);
                FlurryHelper.logModeScreenEvent();
                break;
            case SETTINGS_SCREEN:
                screen = new SettingsScreen(game, SETTINGS_SCREEN);
                FlurryHelper.logSettingsScreenEvent();
                break;
            case EDITOR_SCREEN:
                screen = new EditorScreen(game, EDITOR_SCREEN);
                FlurryHelper.logSettingsScreenEvent();
                break;
            case STATISTICS_SCREEN:
                screen = new StatisticsScreen(game, STATISTICS_SCREEN);
                FlurryHelper.logStatisticsScreenEvent();
                break;
            case MAP_SIZE_SCREEN:
                screen = new MapSizeScreen(game, MAP_SIZE_SCREEN);
                screen.setKeepInMemory(true);
                FlurryHelper.logMapSizeScreenEvent();
                break;
            case PLAYERS_SCREEN:
                screen = new PlayersScreen(game, PLAYERS_SCREEN);
                screen.setKeepInMemory(true);
                FlurryHelper.logPlayersScreenEvent();
                break;

        }

        return screen;
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
            game.getScreenController().setCurrentScreen(gameScreen);
            game.setScreen(game.getScreenController().getCurrentScreen());
            return true;
        }
    }
}
