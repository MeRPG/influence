package com.teremok.influence.model.player.strategy.power;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;
import com.teremok.influence.model.player.Strategist;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexx on 06.02.14
 */
public class SmartyPowerStrategy extends BasicPowerStrategy {
    HashSet<Cell> haveEnemies;

    @Override
    public void cleanUp() {
        super.cleanUp();
        if (haveEnemies != null)
            haveEnemies.clear();
    }

    @Override
    public void prepare(Strategist player) {
        super.prepare(player);
        if (haveEnemies == null)
            haveEnemies = new HashSet<>();
    }

    @Override
    public Map<Cell, Integer> execute(Cell attacker, List<Cell> cells, FieldModel fieldModel, Strategist player) {

        int needToFull = 0;
        for (Cell cell : cells) {
            if (cell.getEnemies().size() > 0) {
                haveEnemies.add(cell);
                needToFull += cell.getMaxPower() - cell.getPower();
            }
        }

        while (powerToDistribute > 0 && needToFull > 0) {
            for (Cell cell : haveEnemies) {
                if (addPower(cell))
                    needToFull--;
            }
        }

        while (powerToDistribute > 0) {
            for (Cell cell : cells) {
                addPower(cell);
            }
        }

        return powerMap;
    }


}
