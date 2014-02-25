package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.Button;
import com.teremok.influence.ui.ButtonTexture;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.Logger;

/**
 * Created by Alexx on 20.12.13
 */
public class StartScreen extends StaticScreen {

    private static final String SINGLEPLAYER = "singleplayer";
    private static final String MULTIPLAYER = "multiplayer";
    private static final String SETTINGS = "settings";
    private static final String VK_COM = "vk";
    private static final String GOOGLE_PLAY = "google_play";

    private ColoredPanel credits;

    public StartScreen(Game game, String filename) {
        super(game, filename);
        Settings.init();
    }

    @Override
    public void show() {
        super.show();
        FXPlayer.load();
    }

    @Override
    protected void addActors() {

        ButtonTexture singleplayer = new ButtonTexture(uiElements.get(SINGLEPLAYER));
        ButtonTexture multiplayer = new ButtonTexture(uiElements.get(MULTIPLAYER));
        ButtonTexture settings = new ButtonTexture(uiElements.get(SETTINGS));
        ButtonTexture vk = new ButtonTexture(uiElements.get(VK_COM));
        ButtonTexture googleplay = new ButtonTexture(uiElements.get(GOOGLE_PLAY));

        if (credits == null) {
            credits = new ColoredPanel(new Color(0x00000000), 0f, 0f, WIDTH, 54f);
            stage.addActor(credits);
        }

        stage.addActor(singleplayer);
        stage.addActor(multiplayer);
        stage.addActor(settings);
        stage.addActor(vk);
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
                    if (code.equals(SINGLEPLAYER)) {
                        ScreenController.startSingleplayerGame();
                    } else if (code.equals(MULTIPLAYER)){
                        ScreenController.startMultiplayerGame();
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
                    ScreenController.showAboutScreen();
                }
            }
        });
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

    @Override
    public void resume() {
        super.resume();
        FXPlayer.load();
        Logger.log("StartScreen: resume;");
    }
}
