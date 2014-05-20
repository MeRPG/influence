package com.teremok.influence.model.player.strategy.power;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;
import com.teremok.influence.model.player.Strategist;

import java.util.List;
import java.util.Map;

/**
 * Created by Alexx on 06.02.14
 */
public class DummyPowerStrategy extends BasicPowerStrategy {
    @Override
    public Map<Cell, Integer> execute(Cell attacker, List<Cell> cells, FieldModel fieldModel, Strategist player) {

        while(powerToDistribute > 0) {
            for (Cell c : cells) {
                addPower(c);
            }
        }

        return powerMap;
    }
}
