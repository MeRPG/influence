package com.teremok.influence.model.player.strategy.concrete.power;

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
            if (field.getConnectedEnemies(cell).isEmpty()) {
                if (toBePowered.isEmpty()) {
                    field.addPower(cell);
                }
                toBePowered.remove(cell.getNumber());
            } else {
                toBePowered.remove(cell.getNumber());
                if (cell.getPower() < cell.getMaxPower()) {
                    toBePowered.add(cell.getNumber());
                }
                System.out.println("--- add power to cell " + cell + ", type" + cell.getType() + ", enemies: ");
                for (Cell en : field.getConnectedEnemies(cell)) {
                    System.out.println(en + ", type: " + en.getType());
                }
                System.out.println("--- end of the enemy list");
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
