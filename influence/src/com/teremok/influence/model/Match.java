package com.teremok.influence.model;

import com.teremok.influence.controller.*;
import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.util.FlurryHelper;

import java.util.List;

/**
 * Created by Alexx on 07.01.14
 */
public class Match {

    public static enum Phase {
        ATTACK,
        POWER
    }

    FieldController fieldController;
    Phase phase;
    PlayerManager pm;
    ScoreController scoreController;

    boolean paused;

    int turn;

    public Match(GameSettings settings, List<Cell> cells, Router router) {
        pm = new PlayerManager(this);
        fieldController = new FieldController(this, settings, cells, router);
        scoreController = new ScoreController(this);

        turn = 0;

        pm.addPlayersFromMap(settings.players, fieldController);

        ChronicleController.matchStart();

        scoreController.init();
        fieldController.updateLists();
        pm.update();
        fieldController.resize();

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

        if (fieldController == null) {
            fieldController = new FieldController(this, settings);
        } else {
            fieldController.reset(this, settings);
        }

        if (scoreController == null) {
            scoreController = new ScoreController(this);
        } else {
            scoreController.reset(this);
        }

        turn = 0;

        ChronicleController.matchStart();

        pm.addPlayersFromMap(settings.players, fieldController);
        pm.placeStartPositions();

        scoreController.init();
        fieldController.updateLists();
        pm.update();
        fieldController.resize();

        phase = Phase.ATTACK;

        MatchSaver.save(this);
        SettingsSaver.save();
    }

    public void act(float delta) {
        if (! paused) {
            if (needToSwitchPhase()) {
                setAttackPhase();
                startNewTurnIfNeeded();
            }
            pm.current().act(delta);
        }
    }

    private boolean needToSwitchPhase() {
        return phase == Phase.POWER && ! pm.current().hasPowerToDistribute();
    }

    private void startNewTurnIfNeeded() {
        if ( needToStartNewTurn() ) {
            startNewTurn();
        }
    }

    private boolean needToStartNewTurn() {
        return pm.isHumanActing() && pm.current().getNumber() == 0 && ! isEnded();
    }

    private void startNewTurn() {
        MatchSaver.save(this);
        turn++;
        if (turn == 1) {
            FlurryHelper.logMatchStartEvent();
        }
    }

    public void switchPhase() {
        if (phase == Phase.ATTACK) {
            setPowerPhase();
        } else {

            Player player = pm.current();

            if (player instanceof HumanPlayer) {
                ((HumanPlayer) player).setAuto(true);
            } else {
                setAttackPhase();
            }
        }

    }

    public void setPowerPhase() {
        pm.current().updatePowerToDistribute();
        if (needToSubtractPower()){
            pm.current().subtractPowerToDistribute();
        }
        phase = Phase.POWER;
        fieldController.resetSelection();
    }

    private boolean needToSubtractPower() {
        return turn == 1 && pm.getNumberOfPlayers() == 2 && pm.current().getNumber() == 0;
    }

    public void setAttackPhase() {
        pm.nextCurrentPlayer();
        phase = Phase.ATTACK;
    }

    public boolean isInPowerPhase() {
        return phase.equals(Phase.POWER);
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

    public FieldController getFieldController() {
        return fieldController;
    }

    public PlayerManager getPm() {
        return pm;
    }

    public ScoreController getScoreController() {
        return scoreController;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }
}
