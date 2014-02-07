package com.teremok.influence.model.player.strategy.enemy;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.EnemyStrategy;

import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 06.02.14
 */
public class RandomEnemyStrategy implements EnemyStrategy {
    Random rnd;

    @Override
    public Cell execute(List<Cell> cells, Field field, Strategist player) {
        rnd = player.getRnd();
        Cell cell = cells.get(rnd.nextInt(cells.size()));
        return cell;
    }

    @Override
    public void afterExecute() {
    }
}
