package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.teremok.influence.model.GameType;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.Button;
import com.teremok.influence.ui.ButtonTexture;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.view.Animation;

/**
 * Created by Alexx on 20.12.13
 */
public class StartScreen extends AbstractScreen {

    private static final String QUICK = "quickGame";
    private static final String SINGLEPLAYER = "singleplayer";
    private static final String MULTIPLAYER = "multiplayer";
    private static final String SETTINGS = "settings";
    private static final String VK_COM = "vk";
    private static final String GOOGLE_PLAY = "google_play";

    private Image background;
    private ColoredPanel overlap;
    private ColoredPanel credits;

    public StartScreen(Game game) {
        super(game);
        Settings.init();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("startScreen.pack"));
        for (Texture tex : atlas.getTextures()) {
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        TextureRegion textureRegion = atlas.findRegion("background");
        background = new Image(new TextureRegionDrawable(textureRegion));
        background.setScaling(Scaling.fit);
        background.setAlign(Align.center);
        background.setTouchable(Touchable.disabled);
        /*
        ButtonColored quick = new ButtonColored(QUICK, getFont(),
                Drawer.getTextColor(), Drawer.getCellColorByNumber(0),
                115f, 400f, 256f, 64f);
        */
        ButtonTexture singleplayer = new ButtonTexture(SINGLEPLAYER,
                atlas.findRegion(SINGLEPLAYER + "_" + Localizator.getLanguage()),
                        115f, 296f);


        ButtonTexture multiplayer = new ButtonTexture(MULTIPLAYER,
                atlas.findRegion(MULTIPLAYER + "_" + Localizator.getLanguage()),
                115f, 192f);

        ButtonTexture settings = new ButtonTexture(SETTINGS,
                atlas.findRegion(SETTINGS),
                401f, 68f);

        ButtonTexture vkcom = new ButtonTexture(VK_COM,
                atlas.findRegion(VK_COM),
                17f, 69f);

        ButtonTexture googleplay = new ButtonTexture(GOOGLE_PLAY,
                atlas.findRegion(GOOGLE_PLAY),
                84f, 69f);

        overlap = new ColoredPanel(Color.BLACK, 0, 0, WIDTH, HEIGHT);
        overlap.setTouchable(Touchable.disabled);
        overlap.addAction(Actions.alpha(0f, Animation.DURATION_NORMAL));

        stage.addActor(background);
        //stage.addActor(quick);
        stage.addActor(singleplayer);
        stage.addActor(multiplayer);
        stage.addActor(settings);
        stage.addActor(vkcom);
        stage.addActor(googleplay);
        stage.addActor(overlap);
    }

    @Override
    public void show() {
        super.show();
        FXPlayer.load();

        if (credits == null) {
            credits = new ColoredPanel(new Color(0x000000FF), 0f, 0f, WIDTH, 54f);
            stage.addActor(credits);
        }

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
                        openSettingsScreen();
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

    public void openSettingsScreen () {
        SequenceAction sequenceAction = Actions.sequence(
                Actions.fadeIn(Animation.DURATION_NORMAL),
                createSettingsAction()
        );
        overlap.addAction(sequenceAction);
    }

    public Action createSettingsAction() {
        return new Action() {
            @Override
            public boolean act(float delta) {
                game.setScreen(new SettingsScreen(game));
                return false;
            }
        };
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
    }
}
