package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Match;
import com.teremok.influence.util.FlurryHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alexx on 26.12.13
 */
public class HumanPlayer extends Player {

    boolean auto;
    Set<Integer> powered;

    protected HumanPlayer(int type, Match match) {
        super(type, match);
        powered = new HashSet<>();
        this.type = PlayerType.Human;
    }

    @Override
    protected void actLogic(float delta) {
        if (match.isInPowerPhase() && auto) {
            distributePowerAuto();
        } else {
            auto = false;
        }

    }

    private void distributePowerAuto() {
        //Logger.log("Distributing power!");
        for (Cell cell : cells) {
            if (!powered.contains(cell.getNumber()) && cell.getPower() != cell.getMaxPower() && powerToDistribute > 0) {
                field.addPower(cell);
            }
        }
        FlurryHelper.logAutoPowerEvent();

        clearPowered();
    }

    public void addPowered(int number) {
        powered.add(number);
    }

    public void clearPowered() {
        powered.clear();
    }

    // Auto-generated

    public void setAuto(boolean auto) {
        this.auto = auto;
    }
}
