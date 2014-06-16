package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.Influence;
import com.teremok.influence.controller.GestureController;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.Button;
import com.teremok.influence.ui.ButtonTexture;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.util.AboutScreenChecker;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.FlurryHelper;
import com.teremok.influence.util.Logger;

/**
 * Created by Alexx on 20.12.13
 */
public class StartScreen extends StaticScreen {

    private static final String CONTINUE = "continue";
    private static final String NEWGAME = "newgame";
    private static final String SETTINGS = "settings";
    private static final String STATISTICS = "stat";
    private static final String VK_COM = "vk";
    private static final String F = "f";
    private static final String GOOGLE_PLAY = "google_play";

    private ColoredPanel credits;
    private ButtonTexture newGame;
    private ButtonTexture resume;

    public StartScreen(Influence game, String filename) {
        super(game, filename);
        Settings.init();
        Logger.init();
        AboutScreenChecker.check();
    }

    @Override
    public void show() {
        super.show();
        FXPlayer.load();
    }

    @Override
    protected void addActors() {
        game.getMatchSaver().load();

        newGame = new ButtonTexture(uiElements.get(NEWGAME));
        resume = new ButtonTexture(uiElements.get(CONTINUE));

        ButtonTexture settings = new ButtonTexture(uiElements.get(SETTINGS));
        ButtonTexture statistics = new ButtonTexture(uiElements.get(STATISTICS));

        ButtonTexture vk = new ButtonTexture(uiElements.get(VK_COM));
        ButtonTexture googleplay = new ButtonTexture(uiElements.get(GOOGLE_PLAY));
        ButtonTexture f = new ButtonTexture(uiElements.get(F));

        if (credits == null) {
            credits = new ColoredPanel(new Color(0x00000000), 0f, 0f, WIDTH, 54f);
            stage.addActor(credits);
        }

        stage.addActor(newGame);
        stage.addActor(resume);
        stage.addActor(settings);
        stage.addActor(statistics);
        stage.addActor(vk);
        stage.addActor(f);
        stage.addActor(googleplay);
    }

    @Override
    protected void addListeners() {
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
                    switch (code) {
                        case CONTINUE:
                            FlurryHelper.logContinueGameEvent();
                            ScreenController.continueGame();
                            GestureController.resetZoom();
                            break;
                        case NEWGAME:
                            FlurryHelper.logNewGameEvent(game.getMatchSaver().hasNotEnded());
                            ScreenController.showMapSizeScreen();
                            break;
                        case SETTINGS:
                            ScreenController.showSettingsScreen();
                            break;
                        case STATISTICS:
                            ScreenController.showStatisticsScreen();
                            break;
                        case F:
                            FlurryHelper.logFacebookClickEvent();
                            goToF();
                            break;
                        case VK_COM:
                            FlurryHelper.logVkClickEvent();
                            goToVkCom();
                            break;
                        case GOOGLE_PLAY:
                            FlurryHelper.logPlayClickEvent();
                            goToGooglePlay();
                            break;
                    }
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    ScreenController.gracefullyExitGame();
                    return true;
                }
                if (keycode == Input.Keys.S) {
                    ScreenController.showStatisticsScreen();
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
                    ScreenController.showAboutScreen();
                }
            }
        });
    }

    public void goToVkCom()
    {
        Gdx.net.openURI("https:/vk.com/teremokgames");
    }

    public void goToF()
    {
        Gdx.net.openURI("https://www.facebook.com/teremokgames");
    }

    public void goToGooglePlay()
    {
        if(Localizator.getLanguage().equals("ru"))
            Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.teremok.influence&hl=ru");
        else
            Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.teremok.influence&hl=en");
    }

    @Override
    public void resume() {
        super.resume();
        FXPlayer.load();
        Logger.log("StartScreen: resume;");
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        checkMatchSave();
    }

    private void checkMatchSave() {
        if (game.getMatchSaver().hasNotEnded()) {
            newGame.setY(uiElements.get(NEWGAME).y);
            resume.setVisible(true);
        } else {
            newGame.setY(277f);
            resume.setVisible(false);
        }
    }
}
