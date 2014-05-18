package com.teremok.influence.model.player.strategy.attack;

import com.teremok.influence.controller.FieldController;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.AttackStrategy;

import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 06.02.14
 */
public class RandomAttackStrategy implements AttackStrategy {
    Random rnd;

    @Override
    public Cell execute(List<Cell> cells, FieldController field, Strategist player) {
        rnd = player.getRnd();
        Cell cell;
        int size = cells.size();

        if (size == field.getCellsCount()) {
            return null;
        }

        int count = 0;
        do {
            cell = cells.get(rnd.nextInt(size));
            count++;
            if (count > size)
                break;
        } while (cell.getEnemies().isEmpty());
            return cell;
    }

    @Override
    public void cleanUp() {
    }
}
