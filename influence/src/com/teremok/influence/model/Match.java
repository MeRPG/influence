package com.teremok.influence.model;

import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.screen.GameScreen;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.FlurryHelper;
import com.teremok.influence.util.Logger;
import com.teremok.influence.view.Drawer;

import java.util.List;

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

    boolean paused;
    boolean endSoundPlayed;

    int turn;

    public Match(GameSettings settings, List<Cell> cells, Router router) {
        pm = new PlayerManager(this);
        field = new Field(this, settings, cells, router);
        score = new Score(this);

        turn = 0;
        endSoundPlayed = false;

        score.setStatus(Localizator.getString("selectYourCell"));

        pm.addPlayersFromMap(settings.players, field);

        score.initColoredPanels();
        field.updateLists();
        pm.update();
        field.resize();

        phase = Phase.ATTACK;
    }

    public Match(GameSettings settings) {
        reset(settings);
    }

    public void reset(GameSettings settings) {
        if (pm == null) {
            pm = new PlayerManager(this);
        } else {
            pm.reset(this);
        }

        if (field == null) {
            field = new Field(this, settings);
        } else {
            field.reset(this, settings);
        }

        if (score == null) {
            score = new Score(this);
        } else {
            score.reset(this);
        }

        turn = 0;
        endSoundPlayed = false;

        score.setStatus(Localizator.getString("selectYourCell"));

        pm.addPlayersFromMap(settings.players, field);
        pm.placeStartPositions();

        score.initColoredPanels();
        field.updateLists();
        pm.update();
        field.resize();

        phase = Phase.ATTACK;

        MatchSaver.save(this);
        Settings.save();
    }

    public void act(float delta) {
        if (! paused) {
            Player currentPlayer = pm.current();
            if (phase == Phase.DISTRIBUTE && ! currentPlayer.hasPowerToDistribute()) {
                currentPlayer = pm.next();
                phase = Phase.ATTACK;
                if ( pm.isHumanActing() && pm.current().getNumber() == 0 && ! isEnded()) {
                    MatchSaver.save(this);
                    turn++;
                    if (turn == 1) {
                        FlurryHelper.logMatchStartEvent();
                    }
                }
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
                    MatchSaver.clearFile();
                    endSoundPlayed = true;
                    GameScreen.colorForBacklight = Drawer.getPlayerColor(pm.current());
                    FlurryHelper.logMatchEndEvent(FlurryHelper.END_REASON_WIN, turn);
                }
            } else if (isLost()) {
                if (! endSoundPlayed) {
                    FXPlayer.playLoseMatch();
                    MatchSaver.clearFile();
                    endSoundPlayed = true;
                    GameScreen.colorForBacklight = Drawer.getBacklightLoseColor();
                    FlurryHelper.logMatchEndEvent(FlurryHelper.END_REASON_LOSE, turn);
                }
            }

            if (pm.getNumberOfPlayerInGame() == 1) {
                GameScreen.colorForBacklight = Drawer.getPlayerColor(pm.current());
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
        player.updatePowerToDistribute();
        if (turn == 1 && pm.getNumberOfPlayers() == 2){
            player.subtractPowerToDistribute();
        }
        phase = Phase.DISTRIBUTE;
        //Logger.log("Distribute power phase.");
        field.resetSelection();
    }

    public void setAttackPhase() {
        pm.next();
        phase = Phase.ATTACK;
        //Logger.log("Attack phase.");
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

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
