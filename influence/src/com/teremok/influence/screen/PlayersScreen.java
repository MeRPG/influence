package com.teremok.influence.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.Influence;
import com.teremok.influence.model.GameDifficulty;
import com.teremok.influence.model.GameSettings;
import com.teremok.influence.model.player.PlayerType;
import com.teremok.influence.ui.*;
import com.teremok.influence.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexx on 25.02.14
 */
public class PlayersScreen extends StaticScreen {


    private static final String EASY = "easy";
    private static final String NORMAL = "normal";
    private static final String HARD = "hard";
    private static final String CUSTOM = "custom";

    private static final String START = "start";
    private static final String DELETE = "delete";
    private static final String ADD = "add";

    private static final String GREEN = "green";
    private static final String YELLOW = "yellow";
    private static final String RED = "red";
    private static final String PURPLE = "purple";

    private static final int DIFFICULTY_GROUP = 13;

    GameSettings gameSettings;

    Checkbox easy;
    Checkbox normal;
    Checkbox hard;
    Checkbox custom;

    ButtonTexture delete;
    ButtonTexture add;

    TexturePanel playerGreen;
    TexturePanel playerYellow;
    TexturePanel playerRed;
    TexturePanel playerPurple;

    PlayerTypeUI[] players;
    int numberOfPlayers;

    public PlayersScreen(Influence game, String filename) {
        super(game, filename);
        gameSettings = game.getSettings().gameSettings;
        numberOfPlayers = gameSettings.getNumberOfPlayers();
    }

    @Override
    protected void addActors() {

        Logger.log("number of players: " + numberOfPlayers);

        playerGreen = new TexturePanel(uiElements.get(GREEN));
        playerGreen.setTouchable(Touchable.disabled);
        stage.addActor(playerGreen);
        playerYellow = new TexturePanel(uiElements.get(YELLOW));
        playerYellow.setTouchable(Touchable.disabled);
        stage.addActor(playerYellow);
        playerRed = new TexturePanel(uiElements.get(RED));
        playerRed.setTouchable(Touchable.disabled);
        stage.addActor(playerRed);
        playerPurple = new TexturePanel(uiElements.get(PURPLE));
        playerPurple.setTouchable(Touchable.disabled);
        stage.addActor(playerPurple);

        players = new PlayerTypeUI[4];
        players[0] = new PlayerTypeUI(uiElements.get("Dummy").region, playerGreen.getX(), playerGreen.getY(), GREEN);
        players[1] = new PlayerTypeUI(uiElements.get("Dummy").region, playerYellow.getX(), playerYellow.getY(), YELLOW);
        players[2] = new PlayerTypeUI(uiElements.get("Dummy").region, playerRed.getX(), playerRed.getY(), RED);
        players[3] = new PlayerTypeUI(uiElements.get("Dummy").region, playerPurple.getX(), playerPurple.getY(), PURPLE);

        Map<PlayerType, TextureRegion> regions = new HashMap<>();
        regions.put(PlayerType.Human, uiElements.get(PlayerType.Human.toString()).region);
        regions.put(PlayerType.Dummy, uiElements.get(PlayerType.Dummy.toString()).region);
        regions.put(PlayerType.Lazy, uiElements.get(PlayerType.Lazy.toString()).region);
        regions.put(PlayerType.Beefy, uiElements.get(PlayerType.Beefy.toString()).region);
        regions.put(PlayerType.Smarty, uiElements.get(PlayerType.Smarty.toString()).region);
        regions.put(PlayerType.Hunter, uiElements.get(PlayerType.Hunter.toString()).region);
        regions.put(PlayerType.Random, uiElements.get(PlayerType.Random.toString()).region);
        PlayerTypeUI.setRegions(regions);

        for (int i = 0; i < 4; i++) {
            players[i].setVisible(false);
            stage.addActor(players[i]);
        }

        RadioGroup sizeGroup = new RadioGroup(DIFFICULTY_GROUP);
        easy = new RadioTexture(uiElements.get(EASY));
        normal = new RadioTexture(uiElements.get(NORMAL));
        hard = new RadioTexture(uiElements.get(HARD));
        custom = new RadioTexture(uiElements.get(CUSTOM));

        delete = new ButtonTexture(uiElements.get(DELETE));
        add = new ButtonTexture(uiElements.get(ADD));

        easy.addToGroup(sizeGroup);
        normal.addToGroup(sizeGroup);
        hard.addToGroup(sizeGroup);
        custom.addToGroup(sizeGroup);

        checkSelectedDifficulty();

        ButtonTexture start = new ButtonTexture(uiElements.get(START));

        stage.addActor(easy);
        stage.addActor(normal);
        stage.addActor(hard);
        stage.addActor(custom);
        stage.addActor(delete);
        stage.addActor(add);
        stage.addActor(start);
    }

    @Override
    protected void addListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    screenController.showMapSizeScreen();
                    if (gameSettings.difficulty == GameDifficulty.CUSTOM) {
                        gameSettings.customPlayers.putAll(gameSettings.players);
                    }
                    return true;
                }
                if (! event.isHandled() && (keycode == Input.Keys.E)) {
                    if (gameSettings.difficulty == GameDifficulty.CUSTOM) {
                        gameSettings.customPlayers.putAll(gameSettings.players);
                    }
                    screenController.showEditorScreen();
                }
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Actor hit = stage.hit(x, y, true);
                return hit != null;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled()) {
                    Actor target = stage.hit(x, y, true);
                    if (target == null)
                        return;
                    game.getFXPlayer().playClick();
                    if (target instanceof PlayerTypeUI) {
                        if (gameSettings.difficulty != GameDifficulty.CUSTOM) {
                            gameSettings.customPlayers = gameSettings.getPlayers(gameSettings.difficulty, 5);
                            gameSettings.difficulty = GameDifficulty.CUSTOM;
                        }
                        PlayerTypeUI type = (PlayerTypeUI)target;
                        type.next();
                        switch (type.getCode()) {
                            case GREEN:
                                if (playerGreen.isVisible()) {
                                    gameSettings.players.put(1, type.getType());
                                    if (gameSettings.difficulty == GameDifficulty.CUSTOM) {
                                        gameSettings.customPlayers.put(1, type.getType());
                                    }
                                }
                                break;
                            case YELLOW:
                                if (playerGreen.isVisible()) {
                                    gameSettings.players.put(2, type.getType());
                                    if (gameSettings.difficulty == GameDifficulty.CUSTOM) {
                                        gameSettings.customPlayers.put(2, type.getType());
                                    }
                                }
                                break;
                            case RED:
                                if (playerGreen.isVisible()) {
                                    gameSettings.players.put(3, type.getType());
                                    if (gameSettings.difficulty == GameDifficulty.CUSTOM) {
                                        gameSettings.customPlayers.put(3, type.getType());
                                    }
                                }
                                break;
                            case PURPLE:
                                if (playerGreen.isVisible()) {
                                    gameSettings.players.put(4, type.getType());
                                    if (gameSettings.difficulty == GameDifficulty.CUSTOM) {
                                        gameSettings.customPlayers.put(4, type.getType());
                                    }
                                }
                                break;
                        }
                    } else if (target instanceof ButtonTexture) {
                        ButtonTexture clicked = (ButtonTexture) target;
                        switch (clicked.getCode()) {
                            case START:
                                screenController.startSingleplayerGame();
                                if (gameSettings.difficulty == GameDifficulty.CUSTOM) {
                                    gameSettings.customPlayers.putAll(gameSettings.players);
                                }
                                break;
                            case DELETE:
                                numberOfPlayers--;
                                break;
                            case ADD:
                                gameSettings.players.put(numberOfPlayers, gameSettings.customPlayers.get(numberOfPlayers));
                                numberOfPlayers++;
                                break;
                        }
                    } else if (target instanceof Checkbox) {
                        Checkbox selectedSize = (Checkbox) target;
                        switch (selectedSize.getCode()) {
                            case EASY:
                                gameSettings.difficulty = GameDifficulty.EASY;
                                break;
                            case NORMAL:
                                gameSettings.difficulty = GameDifficulty.NORMAL;
                                break;
                            case HARD:
                                gameSettings.difficulty = GameDifficulty.HARD;
                                break;
                            case CUSTOM:
                                gameSettings.customPlayers = gameSettings.getPlayers(gameSettings.difficulty, 5);
                                gameSettings.difficulty = GameDifficulty.CUSTOM;
                                break;
                        }
                    }
                    checkSelectedDifficulty();
                }
            }
        });
    }

    private void checkSelectedDifficulty() {
        switch (gameSettings.difficulty) {
            case EASY:
                easy.check();
                break;
            case NORMAL:
                normal.check();
                break;
            case HARD:
                hard.check();
                break;
            case CUSTOM:

                for (int i = 0; i < 5; i++) {
                    if (i < numberOfPlayers) {
                        gameSettings.players.put(i, gameSettings.customPlayers.get(i));
                    } else {
                        gameSettings.players.remove(i);
                    }
                }


                custom.check();
                break;
        }
        gameSettings.players = gameSettings.getPlayers(gameSettings.difficulty, numberOfPlayers);

        delete.setY(174+50*(5-numberOfPlayers));
        add.setY(122 + 50 * (5 - numberOfPlayers));

        switch (numberOfPlayers) {
            case 2:
                delete.setVisible(false);
                add.setVisible(true);

                players[0].setType(gameSettings.players.get(1));

                players[0].setVisible(true);
                players[1].setVisible(false);
                players[2].setVisible(false);
                players[3].setVisible(false);

                playerGreen.setVisible(true);
                playerYellow.setVisible(false);
                playerRed.setVisible(false);
                playerPurple.setVisible(false);
                break;
            case 3:
                delete.setVisible(true);
                add.setVisible(true);

                players[0].setType(gameSettings.players.get(1));
                players[1].setType(gameSettings.players.get(2));

                players[0].setVisible(true);
                players[1].setVisible(true);
                players[2].setVisible(false);
                players[3].setVisible(false);

                playerGreen.setVisible(true);
                playerYellow.setVisible(true);
                playerRed.setVisible(false);
                playerPurple.setVisible(false);
                break;
            case 4:
                delete.setVisible(true);
                add.setVisible(true);

                players[0].setType(gameSettings.players.get(1));
                players[1].setType(gameSettings.players.get(2));
                players[2].setType(gameSettings.players.get(3));

                players[0].setVisible(true);
                players[1].setVisible(true);
                players[2].setVisible(true);
                players[3].setVisible(false);

                playerGreen.setVisible(true);
                playerYellow.setVisible(true);
                playerRed.setVisible(true);
                playerPurple.setVisible(false);
                break;
            case 5:
                delete.setVisible(true);
                add.setVisible(false);

                players[0].setType(gameSettings.players.get(1));
                players[1].setType(gameSettings.players.get(2));
                players[2].setType(gameSettings.players.get(3));
                players[3].setType(gameSettings.players.get(4));

                players[0].setVisible(true);
                players[1].setVisible(true);
                players[2].setVisible(true);
                players[3].setVisible(true);

                playerGreen.setVisible(true);
                playerYellow.setVisible(true);
                playerRed.setVisible(true);
                playerPurple.setVisible(true);
                break;
        }

    }
}
