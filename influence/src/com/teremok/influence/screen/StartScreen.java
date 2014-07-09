package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.framework.screen.StaticScreen;
import com.teremok.influence.Influence;
import com.teremok.influence.model.Match;
import com.teremok.framework.ui.Button;
import com.teremok.framework.ui.ButtonTexture;
import com.teremok.framework.ui.ColoredPanel;
import com.teremok.framework.ui.Label;
import com.teremok.influence.ui.FontNames;
import com.teremok.influence.util.AboutScreenChecker;
import com.teremok.influence.util.FlurryHelper;
import com.teremok.framework.util.Localizator;

import static com.teremok.influence.screen.InfluenceScreenController.*;

/**
 * Created by Alexx on 20.12.13
 */
public class StartScreen extends StaticScreen <Influence> {

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
        AboutScreenChecker.check(game.getSettings());
    }

    @Override
    protected void addActors() {
        Match match = game.getMatchSaver().load();
        if (match != null)
            game.getSettings().gameSettings = match.getGameSettings();

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

        Label label = new Label("Developer preview v1.3", fontFactory.getFontInfo(FontNames.DEBUG),
                Color.RED.cpy(), 0, HEIGHT-20f, false);

        stage.addActor(newGame);
        stage.addActor(resume);
        stage.addActor(settings);
        stage.addActor(statistics);
        stage.addActor(vk);
        stage.addActor(f);
        stage.addActor(googleplay);
        stage.addActor(label);
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
                    game.getFXPlayer().playClick();
                    Button target = (Button)event.getTarget();
                    String code = target.getCode();
                    switch (code) {
                        case CONTINUE:
                            FlurryHelper.logContinueGameEvent();
                            ((InfluenceScreenController)screenController).continueGame();
                            break;
                        case NEWGAME:
                            FlurryHelper.logNewGameEvent(game.getMatchSaver().hasNotEnded());
                            screenController.setScreen(MODE_SCREEN);
                            break;
                        case SETTINGS:
                            screenController.setScreen(SETTINGS_SCREEN);
                            break;
                        case STATISTICS:
                            screenController.setScreen(STATISTICS_SCREEN);
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
                    screenController.gracefullyExitGame();
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
                    game.getFXPlayer().playClick();
                    screenController.setScreen(ABOUT_SCREEN);
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
        Gdx.app.debug(getClass().getSimpleName(), "StartScreen: resume;");
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
