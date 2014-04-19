package com.teremok.influence.model.player.strategy.power;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.PowerStrategy;
import com.teremok.influence.model.player.strategy.Strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 19.04.2014
 */
public abstract class BasicPowerStrategy implements PowerStrategy {

    protected Map<Cell, Integer> powerMap;
    protected int powerToDistribute;

    @Override
    public void prepare(Strategist player) {
        if (powerMap == null)
            powerMap = new HashMap<>();
        powerToDistribute = player.getPowerToDistribute();
    }

    @Override
    public void cleanUp() {
        if (powerMap != null)
            powerMap.clear();
        powerToDistribute = 0;
    }

    protected boolean addPower(Cell cell) {
        if (powerToDistribute > 0) {
            int pow = 0;
            if (powerMap.containsKey(cell))
                pow = powerMap.get(cell);
            pow++;

            if (pow + cell.getPower() <= cell.getMaxPower()) {
                powerToDistribute--;
                powerMap.put(cell, pow);
                return true;
            }
        }
        return false;
    }

    protected int getNewPower(Cell cell) {
        if (powerMap.containsKey(cell)) {
            return powerMap.get(cell)+cell.getPower();
        }
        return cell.getPower();
    }
}
