package com.teremok.influence.model.player.strategy.enemy;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.EnemyStrategy;

import java.util.List;

/**
 * Created by Alexx on 06.02.14
 */
public class DummyEnemyStrategy implements EnemyStrategy {

    int i = 0;

    @Override
    public Cell execute(Cell attacker, List<Cell> cells, FieldModel fieldModel, Strategist player) {
        if (i >= cells.size())
            i = 0;
        Cell cell = cells.get(i);
        i++;
        return cell;
    }

    @Override
    public void cleanUp() {
        i = 0;
    }
}
