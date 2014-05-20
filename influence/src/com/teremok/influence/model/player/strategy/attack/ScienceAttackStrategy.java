package com.teremok.influence.model.player.strategy.attack;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.AttackStrategy;

import java.util.List;

/**
 * Created by Алексей on 20.05.2014
 */
public class ScienceAttackStrategy implements AttackStrategy {

    //private static final float DELTA_POWER_COEF = 0.75f;
    private static final float ENEMIES_NUMBER_COEF = 0.25f;
    private static final float POWER_COEF = 1.0f;

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
        checkCoefs(cells);
        return cells.get(max);
    }

    @Override
    public void cleanUp() {
        sum = 0;
    }

    private void checkCoefs(List<Cell> cells) {
        int i = 0;
        sum = 0;
        for (Cell cell : cells) {
            bids[i] = checkBids(cell);
            sum += bids[i];
            i++;
        }
        for (i = 0; i < cells.size(); i++) {
            coef[i] = bids[i] / sum;
            if (coef[i] > coef[max])
                max = i;
        }
    }

    private float checkBids(Cell cell) {
        float bids;

        if (cell.getPower() < 2) {
            return 0;
        }

        float numberOfEnemiesBid = cell.getEnemies().size() * ENEMIES_NUMBER_COEF;

        /*
        float deltaPowerBid = 0;
        for (Cell enemies : cell.getEnemies()) {
            deltaPowerBid += (cell.getPower() - enemies.getPower());
        }
        deltaPowerBid /= cell.getEnemies().size();
        deltaPowerBid *= DELTA_POWER_COEF;    */

        float powerBid = (cell.getPower()-1) * POWER_COEF;

        bids = powerBid + numberOfEnemiesBid;

        return bids;
    }
}
