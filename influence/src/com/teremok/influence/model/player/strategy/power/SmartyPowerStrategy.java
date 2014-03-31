package com.teremok.influence.model.player.strategy.power;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
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
        if (cells.size() == 25)
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
                //Logger.log("--- add power to cell " + cell + ", number" + cell.getType() + ", enemies: ");
                for (Cell en : cell.getEnemies()) {
                    //Logger.log(en + ", number: " + en.getType());
                }
                //Logger.log("--- end of the enemy list");
                field.addPower(cell);

            }
        }
        return null;
    }

    @Override
    public void afterExecute() {
        //toBePowered.clear();
    }
}
