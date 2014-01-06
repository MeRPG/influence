package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.Score;
import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.ui.TooltipHandler;
import com.teremok.influence.view.Drawer;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen extends AbstractScreen {

    public static enum TurnPhase {
        ATTACK,
        DISTRIBUTE
    }

    public static enum GameType {
        SINGLEPLAYER,
        MULTIPLAYER
    }

    Field field;
    Score score;
    GameType gameType;
    public static TurnPhase currentPhase;

    public GameScreen(Game game) {
        super(game);
    }

    public GameScreen(Game game, GameType gameType) {
        super(game);

        this.gameType = gameType;

        field = new Field();
        PlayerManager.setField(field);
        addPlayers();
        score = new Score(field);
    }

    private void addPlayers() {
        if (gameType == GameType.MULTIPLAYER) {
            addPlayersForMultiplayer();
        } else {
            addPlayersForSingleplayer();
        }

    }

    private void addPlayersForSingleplayer() {
        PlayerManager.addPlayersForSingleplayer(this);
    }

    private void addPlayersForMultiplayer() {
        PlayerManager.addPlayersForMultiplayer(this);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Player currentPlayer = PlayerManager.current();

        if (currentPhase == TurnPhase.DISTRIBUTE && currentPlayer.getPowerToDistribute() == 0) {
            currentPlayer = PlayerManager.next();
            currentPhase = TurnPhase.ATTACK;
        }

        currentPlayer.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);

        currentPhase = TurnPhase.ATTACK;

        AbstractDrawer.setBitmapFont(getFont());

        field.setTouchable(Touchable.childrenOnly);
        stage.addActor(field);

        stage.addActor(score);

        stage.addActor(TooltipHandler.getInstance());

        stage.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) instanceof Score;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled() && PlayerManager.current() instanceof HumanPlayer) {
                    switch (currentPhase) {
                        case ATTACK:
                            setDistributePhase();
                            break;
                        case DISTRIBUTE:
                            setAttackPhase();
                            break;
                        default:

                    }
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.R) {
                    field.regenerate();
                    addPlayers();
                }
                return true;
            }

        });
    }

    public void switchPhase() {
        if (currentPhase == TurnPhase.ATTACK) {
            setDistributePhase();
        } else {
            setAttackPhase();
        }

    }

    public void setDistributePhase() {
        Player player = PlayerManager.current();
        int power = field.getPowerToDistribute(player.getType());
        player.setPowerToDistribute(power);
        currentPhase = TurnPhase.DISTRIBUTE;
        System.out.println("Distribute power phase.");
        field.resetSelection();
    }

    public void setAttackPhase() {
        PlayerManager.next();
        currentPhase = TurnPhase.ATTACK;
        System.out.println("Attack phase.");
    }

    @Override
    public void pause() {
        super.pause();
        Drawer.dispose();
    }
}
