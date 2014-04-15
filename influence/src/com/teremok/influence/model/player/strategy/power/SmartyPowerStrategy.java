package com.teremok.influence.model.player.strategy.power;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.Settings;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.PowerStrategy;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Alexx on 06.02.14
 */
public class SmartyPowerStrategy implements PowerStrategy {


    HashSet<Integer> toBePowered = new HashSet<Integer>();

    @Override
    public Cell execute(List<Cell> cells, Field field, Strategist player) {
        if (cells.size() == field.getCellsCount())
            toBePowered.clear();
        for (Cell cell : cells) {
            if (cell.getEnemies().isEmpty()) {
                if (toBePowered.isEmpty()) {
                    field.addPower(cell);
                }
                toBePowered.remove(cell.getNumber());
            } else {
                toBePowered.remove(cell.getNumber());
                if (cell.getPower() < cell.getMaxPower()) {
                    toBePowered.add(cell.getNumber());
                }
                field.addPower(cell);

            }
        }

        HashSet<Integer> toBeRemoved = new HashSet<>();

        for (Integer integer : toBePowered) {
            boolean found = false;
            for (Cell cell : cells) {
                if (cell.getNumber() == integer) {
                    found = true;
                    if (cell.getPower() == cell.getMaxPower()) {
                        found = false;
                    }
                }
            }
            if (! found) {
                toBeRemoved.add(integer);
            }
        }
        for (Integer integer : toBeRemoved) {
            toBePowered.remove(integer);
        }
        return null;
    }

    @Override
    public void afterExecute() {
        //toBePowered.clear();
    }
}
