package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.Score;
import com.teremok.influence.model.player.ComputerPlayer;
import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.view.TooltipHandler;

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
    public static TurnPhase currentPhase;

    public GameScreen(Game game) {
        super(game);
    }

    public GameScreen(Game game, GameType gameType) {
        super(game);
        addPlayers(gameType);
        field = new Field();
        Player.setField(field);
        score = new Score(field);
    }

    private void addPlayers(GameType gameType) {
        if (gameType == GameType.MULTIPLAYER) {
            Player.setNumberOfPlayers(2);
            addPlayersForMultiplayer();
        } else {
            Player.setNumberOfPlayers(5);
            addPlayersForSingleplayer();
        }

    }

    private void addPlayersForSingleplayer() {
        for (int i = 0; i < 5; i ++) {
            Player.addPlayer(new HumanPlayer(i), i);
        }
    }

    private void addPlayersForMultiplayer() {
        Player.addPlayer(new HumanPlayer(0), 0);
        Player.addPlayer(new ComputerPlayer(1, this), 1);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Player currentPlayer = Player.current();

        if (currentPhase == TurnPhase.DISTRIBUTE && currentPlayer.getPowerToDistribute() == 0) {
            currentPlayer = Player.next();
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
        //field.updateMinimal();

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
                if (! event.isHandled()) {
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
                }
                return true;
            }

        });
    }

    public void setDistributePhase() {
        Player player = Player.current();
        int power = field.getPowerToDistribute(player.getType());
        player.setPowerToDistribute(power);
        currentPhase = TurnPhase.DISTRIBUTE;
        System.out.println("Distribute power phase.");
        field.resetSelection();
    }

    public void setAttackPhase() {
        Player.next();
        currentPhase = TurnPhase.ATTACK;
    }
}
