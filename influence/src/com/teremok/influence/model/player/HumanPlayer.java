package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Match;

import java.util.*;

/**
 * Created by Alexx on 26.12.13
 */
public class HumanPlayer extends Player {

    boolean auto;
    Set<Integer> powered;

    protected HumanPlayer(int type, Match match) {
        super(type, match);
        powered = new HashSet<Integer>();
    }

    @Override
    protected void actLogic(float delta) {
        if (match.isInDistributePhase() && auto) {
            distributePowerAuto();
        } else {
            auto = false;
        }

    }

    private void distributePowerAuto() {
        System.out.println("Distributing power!");
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == type && powerToDistribute > 0 && !powered.contains(cell.getNumber())) {
                field.addPower(cell);
            }
        }
        powered.clear();
    }

    public void addPowered(int number) {
        powered.add(number);
    }

    // Auto-generated

    public void setAuto(boolean auto) {
        this.auto = auto;
    }
}
