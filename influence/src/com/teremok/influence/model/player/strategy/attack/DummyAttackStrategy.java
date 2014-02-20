package com.teremok.influence.model.player.strategy.attack;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.AttackStrategy;
import com.teremok.influence.util.Logger;

import java.util.List;

/**
 * Created by Alexx on 06.02.14
 */
public class DummyAttackStrategy implements AttackStrategy {

    int i = 0;

    @Override
    public Cell execute(List<Cell> cells, Field field, Strategist player) {
        int size = cells.size();
        if (i >= size) {
            i = 0;
        }
        int last = i;
        Cell cell = cells.get(i);
        if (cell.getPower() == 1 || cell.getEnemiesList().isEmpty()){
            do {
                i++;
                if (i >= size)
                    i = 0;
                if (i == last)
                    break;
                cell = cells.get(i);
            } while (cell.getPower()==1 || cell.getEnemiesList().isEmpty());
        }
        //Logger.log("attacker: " + i);
        return cell;
    }

    @Override
    public void afterExecute() {
        //i = 0;
    }
}
