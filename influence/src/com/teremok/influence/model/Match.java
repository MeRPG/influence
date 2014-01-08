package com.teremok.influence.model;

import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.model.player.Player;

/**
 * Created by Alexx on 07.01.14
 */
public class Match {

    public static enum Phase {
        ATTACK,
        DISTRIBUTE
    }

    Field field;
    Phase phase;
    PlayerManager pm;
    Score score;
    GameType gameType;
    boolean paused;

    public Match(GameType gameType) {
        pm = new PlayerManager(this);
        field = new Field(this);
        score = new Score(this);
        this.gameType = gameType;

        if (gameType.equals(GameType.MULTIPLAYER)) {
            pm.addPlayersForMultiplayer(field);
        } else {
            pm.addPlayersForSingleplayer(field);
        }

        phase = Phase.ATTACK;

    }

    public void act(float delta) {
        if (! paused) {
            Player currentPlayer = pm.current();

            if (phase == Phase.DISTRIBUTE && currentPlayer.getPowerToDistribute() == 0) {
                currentPlayer = pm.next();
                phase = Phase.ATTACK;
            }

            currentPlayer.act(delta);
        }
    }


    public void switchPhase() {
        if (phase == Phase.ATTACK) {
            setDistributePhase();
        } else {

            Player player = pm.current();

            if (player instanceof HumanPlayer) {
                ((HumanPlayer) player).setAuto(true);
            } else {
                setAttackPhase();
            }
        }

    }

    public void setDistributePhase() {
        Player player = pm.current();
        int power = field.getPowerToDistribute(player.getType());
        player.setPowerToDistribute(power);
        phase = Phase.DISTRIBUTE;
        System.out.println("Distribute power phase.");
        field.resetSelection();
    }

    public void setAttackPhase() {
        pm.next();
        phase = Phase.ATTACK;
        System.out.println("Attack phase.");
    }

    public boolean isInDistributePhase() {
        return phase.equals(Phase.DISTRIBUTE);
    }

    public boolean isInAttackPhase() {
        return phase.equals(Phase.ATTACK);
    }

    public boolean canHumanActing() {
        return pm.isHumanActing() && !paused;
    }

    public boolean isEnded() {
        int playersInGame = pm.getNumberOfPlayerInGame();

        return playersInGame == 1 || ! pm.isHumanInGame();
    }

    // Auto-generated

    public Field getField() {
        return field;
    }

    public PlayerManager getPm() {
        return pm;
    }

    public Score getScore() {
        return score;
    }

    public GameType getGameType() {
        return gameType;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
