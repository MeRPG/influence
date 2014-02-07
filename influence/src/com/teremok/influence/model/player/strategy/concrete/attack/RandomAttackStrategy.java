package com.teremok.influence.model.player.strategy.concrete.attack;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
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
    public Cell execute(List<Cell> cells, Field field, Strategist player) {
        rnd = player.getRnd();
        Cell cell;
        int size = cells.size();
        do {
            cell = cells.get(rnd.nextInt(size));
        } while (field.getConnectedEnemies(cell).isEmpty());
            return cell;
    }

    @Override
    public void afterExecute() {
    }
}
