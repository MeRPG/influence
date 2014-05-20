package com.teremok.influence.model.player.strategy.enemy;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.EnemyStrategy;

import java.util.List;

/**
 * Created by Алексей on 20.05.2014
 */
public class ScienceEnemyStrategy implements EnemyStrategy {

    private static final float POWER_COEF = 1.0f;
    private static final float NEAR_POWER_COEF = 0.1f;

    private float bids[];
    private float coef[];
    private int max;
    private float sum;

    @Override
    public Cell execute(Cell attacker, List<Cell> cells, FieldModel fieldModel, Strategist player) {
        int size = cells.size();
        coef = new float[size];
        bids = new float[size];
        max = player.getRnd().nextInt(size);
        checkCoefs(attacker, cells);
        return cells.get(max);
    }

    @Override
    public void cleanUp() {
        sum = 0;
    }

    private void checkCoefs(Cell attacker, List<Cell> cells) {
        int i = 0;
        sum = 0;
        for (Cell cell : cells) {
            bids[i] = checkBids(attacker, cell);
            sum += bids[i];
            i++;
        }
        for (i = 0; i < cells.size(); i++) {
            coef[i] = bids[i] / sum;
            if (coef[i] > coef[max])
                max = i;
        }
    }

    private float checkBids(Cell attacker, Cell cell) {
        float bids;

        float powerBid = (attacker.getPower() - cell.getPower() ) * POWER_COEF;
        if (powerBid < 0) {
            powerBid = 0;
        }

        float nearPowerBid = 0;
        for (Cell enemyOfEnemy : cell.getEnemies()) {
            nearPowerBid += enemyOfEnemy.getPower();
        }
        nearPowerBid /= cell.getEnemies().size();
        nearPowerBid *= NEAR_POWER_COEF;

        bids = powerBid + nearPowerBid;

        return bids;
    }
}
