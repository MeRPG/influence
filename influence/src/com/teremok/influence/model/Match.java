package com.teremok.influence.model;

import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.Logger;

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
    boolean endSoundPlayed;
    boolean firstTurn = true;

    public Match(GameType gameType) {
        pm = new PlayerManager(this);
        field = new Field(this);
        score = new Score(this);

        score.setStatus(Localizator.getString("selectYourCell"));

        this.gameType = gameType;

        if (gameType.equals(GameType.MULTIPLAYER)) {
            pm.addPlayersForMultiplayer(field);
        } else {
            pm.addPlayersForSingleplayer(field);
        }

        score.initColoredPanels();
        field.updateLists();
        pm.update();

        phase = Phase.ATTACK;
    }

    public void act(float delta) {
        if (! paused) {
            Player currentPlayer = pm.current();

            if (phase == Phase.DISTRIBUTE && ! currentPlayer.hasPowerToDistribute()) {
                currentPlayer = pm.next();
                phase = Phase.ATTACK;
            }

            if (field.getSelectedCell() == null && pm.isHumanActing()) {
                if (isInDistributePhase() && currentPlayer.hasPowerToDistribute()) {
                    score.setStatus(Localizator.getString("touchToDistribute"));
                } else {
                    score.setStatus(Localizator.getString("selectYourCell"));
                }
            }

            if (isWon()) {
                if (! endSoundPlayed) {
                    FXPlayer.playWinMatch();
                    endSoundPlayed = true;
                }
            } else if (isLost()) {
                if (! endSoundPlayed) {
                    FXPlayer.playLoseMatch();
                    endSoundPlayed = true;
                    isLost();
                }
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
        if (player instanceof HumanPlayer) {
            ((HumanPlayer) player).clearPowered();
        }
        int power = field.getPowerToDistribute(player.getNumber());
        if (firstTurn && gameType == GameType.MULTIPLAYER && pm.getNumberOfPlayers() == 2){
            player.setPowerToDistribute(power-1);
            firstTurn = false;
        } else {
            player.setPowerToDistribute(power);
        }
        phase = Phase.DISTRIBUTE;
        Logger.log("Distribute power phase.");
        field.resetSelection();
    }

    public void setAttackPhase() {
        pm.next();
        phase = Phase.ATTACK;
        Logger.log("Attack phase.");
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

    public boolean isWon() {
        return isEnded() && pm.isHumanInGame();
    }

    public boolean isLost() {
        return isEnded() && ! pm.isHumanInGame();
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
