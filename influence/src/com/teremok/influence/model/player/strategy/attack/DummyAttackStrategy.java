package com.teremok.influence.model.player.strategy.attack;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.AttackStrategy;

import java.util.List;

/**
 * Created by Alexx on 06.02.14
 */
public class DummyAttackStrategy implements AttackStrategy {

    int i = 0;

    @Override
    public Cell execute(List<Cell> cells, FieldModel fieldModel, Strategist player) {
        int size = cells.size();

        if (size == fieldModel.cellsCount) {
            return null;
        }

        if (i >= size) {
            i = 0;
        }
        int last = i;
        Cell cell = cells.get(i);
        if (cell.getPower() == 1 || cell.getEnemies().isEmpty()){
            do {
                i++;
                if (i >= size)
                    i = 0;
                if (i == last)
                    break;
                cell = cells.get(i);
            } while (cell.getPower()==1 || cell.getEnemies().isEmpty());
        }
        //Logger.log("attacker: " + i);
        return cell;
    }

    @Override
    public void cleanUp() {
        //i = 0;
    }
}
