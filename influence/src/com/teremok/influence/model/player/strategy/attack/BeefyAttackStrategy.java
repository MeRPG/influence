package com.teremok.influence.model.player.strategy.attack;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.AttackStrategy;

import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 05.02.14
 */
public class BeefyAttackStrategy implements AttackStrategy {
    @Override
    public Cell execute(List<Cell> cells, Field field, Strategist player) {

        if (cells.size() == field.getCellsCount()) {
            return null;
        }

        int type = player.getNumber();
        Random rnd = player.getRnd();

        int number;
        Cell cell = cells.get(0);
        boolean end = false;
        while(! end) {
            end = true;
            number = rnd.nextInt(cells.size());
            cell = cells.get(number);
            if (! cell.isValid())
                end = false;
            if (cell.getType()!= type)
                end = false;
            if (cell.getEnemies().isEmpty())
                end = false;
            if (player.getCells().size() == field.getCellsCount())
                end = true;
        }

        int maxPower = cell.getPower();

        for (Cell cell2 : cells) {
            if (cell2.isValid() && cell2.getType() == type) {
                if (cell2.getPower() > maxPower && ! cell2.getEnemies().isEmpty()) {
                    cell = cell2;
                    maxPower = cell2.getPower();
                }
            }
        }

        return cell;
    }

    @Override
    public void cleanUp() {
    }
}
