package com.teremok.influence.model.player.strategy.power;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.util.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by Алексей on 20.05.2014
 */
public class SciencePowerStrategy extends BasicPowerStrategy {

    private static final float INITIAL_BID = 0.1f;
    private static final float POWER_COEF = 0.75f;
    private static final float ENEMIES_NUMBER_COEF = 0.50f;
    private static final float EMPTY_ENEMY_COEF = 1.0f;



    private float bids[];
    private float coef[];
    private int max;
    private float sum;

    @Override
    public void cleanUp() {
        super.cleanUp();
    }

    @Override
    public void prepare(Strategist player) {
        super.prepare(player);
        int size = player.getCells().size();
        bids = new float[size];
        coef = new float[size];
        max = player.getRnd().nextInt(size);
        sum = 0;
    }

    @Override
    public Map<Cell, Integer> execute(Cell attacker, List<Cell> cells, FieldModel fieldModel, Strategist player) {


        while (powerToDistribute > 0) {
            checkCoefs(cells);
            addPower(cells.get(max));
            max = player.getRnd().nextInt(cells.size());
            sum = 0;
        }

        return powerMap;
    }

    private void checkCoefs(List<Cell> cells) {
        int i = 0;
        sum = 0;
        for (Cell cell : cells) {
            Logger.append( i + "");
            bids[i] = checkBids(cell);
            sum += bids[i];
            i++;
        }
        Logger.log("bid sum: " + sum);
        for (i = 0; i < cells.size(); i++) {
            coef[i] = bids[i] / sum;
            Logger.log( i + " - coef: " + coef[i]);
            if (coef[i] > coef[max])
                max = i;
        }
        Logger.log("max coef: " + coef[max] + "; bid: " + bids[max]);
    }

    private float checkBids(Cell cell) {
        float bids;
        if (getNewPower(cell) == cell.getMaxPower())
            return 0;

        float routesBid = cell.getEnemies().size() * ENEMIES_NUMBER_COEF;

        float powerBid = -(getNewPower(cell) -1);
        float emptyEnemyBid = 0;
        for (Cell enemy : cell.getEnemies()) {
            powerBid += enemy.getPower();
            if (enemy.getPower() == 0) {
                emptyEnemyBid++;
            }
        }
        powerBid *= POWER_COEF;

        if (powerBid < 0)
            powerBid = 0;

        emptyEnemyBid *= EMPTY_ENEMY_COEF;

        bids = INITIAL_BID + powerBid + emptyEnemyBid + routesBid;
        Logger.log("bids = INITIAL (" + INITIAL_BID + ") + " + powerBid + " + " + emptyEnemyBid + " + " + routesBid);
        if (bids < 0)
            bids = 0;
        return bids;
    }
}
