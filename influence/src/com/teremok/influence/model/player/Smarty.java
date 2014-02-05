package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Match;

import java.util.HashSet;

/**
 * Created by Alexx on 05.02.14
 */
public class Smarty extends ComputerPlayer {


    public Smarty(int type, Match match) {
        super(type, match);
    }

    @Override
    protected void actAttackLogic(float delta) {
        super.actAttackLogic(delta);
        toBePowered.clear();
    }

    HashSet<Integer> toBePowered = new HashSet<Integer>();

    @Override
    protected void actDistributeLogic(float delta) {
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == type && powerToDistribute > 0) {
                if (field.getConnectedEnemies(cell).isEmpty()) {
                    if (toBePowered.isEmpty()) {
                        field.addPower(cell);
                    }
                } else {
                    toBePowered.add(cell.getNumber());
                    field.addPower(cell);
                    if (cell.getPower() == cell.getMaxPower()) {
                        toBePowered.remove(cell.getNumber());
                    }
                }
            }
        }
        System.out.println("delta: " + delta + ";\tneedToPower: " + toBePowered.size());
    }
}
