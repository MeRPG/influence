package com.teremok.influence.model.player.strategy.power;

import com.teremok.influence.controller.FieldController;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.player.Strategist;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Alexx on 06.02.14
 */
public class RandomPowerStrategy extends BasicPowerStrategy {
    Random rnd;

    @Override
    public Map<Cell, Integer> execute(List<Cell> cells, FieldController field, Strategist player) {
        rnd = player.getRnd();
        Cell cell;

        while (powerToDistribute > 0) {
            cell = cells.get(rnd.nextInt(cells.size()));
            addPower(cell);
        }

        return powerMap;
    }
}
