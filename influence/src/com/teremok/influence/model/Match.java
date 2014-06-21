package com.teremok.influence.model;

import com.teremok.influence.controller.FieldController;
import com.teremok.influence.controller.ScoreController;
import com.teremok.influence.controller.SettingsController;
import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.PlayerManager;

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
    Chronicle.MatchChronicle matchChronicle;

    boolean paused;

    int turn;

    public Match(GameSettings settings, List<Cell> cells, Router router, int turn, Chronicle.MatchChronicle matchChronicle) {

        this.turn = turn;
        this.matchChronicle = matchChronicle;

        pm = new PlayerManager(this);
        fieldController = new FieldController(this, settings, cells, router);
        fieldController.setMatchChronicle(matchChronicle);
        scoreController = new ScoreController(this);

        pm.addPlayersFromMap(settings.players, fieldController);

        scoreController.init();
        fieldController.updateLists();
        pm.update();
        fieldController.resize();

        phase = Phase.ATTACK;
    }

    public Match(GameSettings settings, Chronicle.MatchChronicle matchChronicle) {
        reset(settings, matchChronicle);
    }

    public void reset(GameSettings settings,  Chronicle.MatchChronicle matchChronicle) {

        this.matchChronicle = matchChronicle;

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
        fieldController.setMatchChronicle(matchChronicle);

        if (scoreController == null) {
            scoreController = new ScoreController(this);
        } else {
            scoreController.reset(this);
        }

        turn = 0;


        pm.addPlayersFromMap(settings.players, fieldController);
        pm.placeStartPositions();

        scoreController.init();
        fieldController.updateLists();
        pm.update();
        fieldController.resize();

        phase = Phase.ATTACK;

        SettingsController.save();
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
        turn++;
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

    public Chronicle.MatchChronicle getMatchChronicle() {
        return matchChronicle;
    }
}
