package com.teremok.influence.model.player.strategy.power;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.PowerStrategy;

import java.util.List;

/**
 * Created by Alexx on 06.02.14
 */
public class DummyPowerStrategy implements PowerStrategy {

   int i = 0;

    @Override
    public Cell execute(List<Cell> cells, Field field, Strategist player) {
        if (i >= cells.size())
            i = 0;
        Cell cell = cells.get(i);
        i++;
        return cell;
    }

    @Override
    public void afterExecute() {
        i = 0;
    }
}
