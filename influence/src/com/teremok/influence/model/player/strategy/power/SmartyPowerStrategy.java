package com.teremok.influence.model.player.strategy.power;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.Settings;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.PowerStrategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexx on 06.02.14
 */
public class SmartyPowerStrategy extends BasicPowerStrategy {
    HashSet<Integer> toBePowered = new HashSet<Integer>();

    @Override
    public Map<Cell, Integer> execute(List<Cell> cells, Field field, Strategist player) {
        powerToDistribute = player.getPowerToDistribute();
        while (powerToDistribute > 0) {
            if (cells.size() == field.getCellsCount()) {
                toBePowered.clear();
            }
            for (Cell cell : cells) {
                if (cell.getEnemies().isEmpty()) {
                    if (toBePowered.isEmpty()) {
                        addPower(cell);
                    }
                    toBePowered.remove(cell.getNumber());
                } else {
                    toBePowered.remove(cell.getNumber());
                    if (getNewPower(cell) < cell.getMaxPower()) {
                        toBePowered.add(cell.getNumber());
                    }
                    addPower(cell);

                }
            }

            HashSet<Integer> toBeRemoved = new HashSet<>();

            for (Integer integer : toBePowered) {
                boolean found = false;
                for (Cell cell : cells) {
                    if (cell.getNumber() == integer) {
                        found = getNewPower(cell) != cell.getMaxPower();
                    }
                }
                if (! found) {
                    toBeRemoved.add(integer);
                }
            }
            for(Integer num : toBeRemoved) {
                toBePowered.remove(num);
            }
        }
        return powerMap;
    }


}
