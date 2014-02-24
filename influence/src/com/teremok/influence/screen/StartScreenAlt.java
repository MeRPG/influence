package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.teremok.influence.model.GameType;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.Button;
import com.teremok.influence.ui.ButtonTexture;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.Logger;
import com.teremok.influence.view.Animation;

/**
 * Created by Alexx on 20.12.13
 */
public class StartScreenAlt extends StaticScreen {

    private static final String QUICK = "quickGame";
    private static final String SINGLEPLAYER = "singleplayer";
    private static final String MULTIPLAYER = "multiplayer";
    private static final String SETTINGS = "settings";
    private static final String VK_COM = "vk";
    private static final String GOOGLE_PLAY = "google_play";

    private ColoredPanel credits;

    public StartScreenAlt(Game game, String filename) {
        super(game, filename);
        Settings.init();
    }

    @Override
    public void show() {
        super.show();
        FXPlayer.load();

        addActors();

        addListeners();
    }

    private void addActors() {

        ButtonTexture singleplayer = new ButtonTexture(uiElements.get(SINGLEPLAYER));
        ButtonTexture multiplayer = new ButtonTexture(uiElements.get(MULTIPLAYER));
        ButtonTexture settings = new ButtonTexture(uiElements.get(SETTINGS));
        ButtonTexture vk = new ButtonTexture(uiElements.get(VK_COM));
        ButtonTexture googleplay = new ButtonTexture(uiElements.get(GOOGLE_PLAY));

        initOverlap();

        if (credits == null) {
            credits = new ColoredPanel(new Color(0x000000FF), 0f, 0f, WIDTH, 54f);
            stage.addActor(credits);
        }

        stage.addActor(singleplayer);
        stage.addActor(multiplayer);
        stage.addActor(settings);
        stage.addActor(vk);
        stage.addActor(googleplay);
        stage.addActor(overlap);
    }

    private void addListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) instanceof Button;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled()) {
                    FXPlayer.playClick();
                    Button target = (Button)event.getTarget();
                    String code = target.getCode();
                    if (code.equals(QUICK)) {
                        startSingleplayerGame();
                    } else if (code.equals(SINGLEPLAYER)) {
                        startSingleplayerGame();
                    } else if (code.equals(MULTIPLAYER)){
                        startMultiplayerGame();
                    } else if (code.equals(SETTINGS)){
                        //openSettingsScreen();
                        ScreenController.showSettingsScreen();
                    } else if (code.equals(VK_COM)){
                        goToVkCom();
                    } else if (code.equals(GOOGLE_PLAY)){
                        goToGooglePlay();
                    }
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    exitGame();
                    return true;
                }
                return false;
            }
        });

        credits.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) == credits;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (!event.isHandled()) {
                    FXPlayer.playClick();
                    openAboutScreen();
                }
            }
        });
    }

    public void startMultiplayerGame () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createStartGameAction(GameType.MULTIPLAYER)
        );
        overlap.addAction(sequenceAction);
    }

    public void startSingleplayerGame () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createStartGameAction(GameType.SINGLEPLAYER)
        );
        overlap.addAction(sequenceAction);
    }

    public void goToVkCom()
    {
        Gdx.net.openURI("https:/vk.com/teremokgames");
    }

    public void goToGooglePlay()
    {
        if(Localizator.getLanguage().equals("ru"))
            Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.teremok.influence&hl=ru");
        else
            Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.teremok.influence&hl=en");
    }

    private Action createStartGameAction(GameType gameType) {
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

    public void openAboutScreen() {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createAboutAction()
        );
        overlap.addAction(sequenceAction);
    }

    public Action createAboutAction() {
        return new Action() {
            @Override
            public boolean act(float delta) {
                game.setScreen(new AboutScreen(game));
                return false;
            }
        };
    }

    @Override
    public void resume() {
        super.resume();
        FXPlayer.load();
        Logger.log("StartScreenAlt: resume;");
    }
}
