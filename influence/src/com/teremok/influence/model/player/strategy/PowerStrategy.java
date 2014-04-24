package com.teremok.influence.model.player.strategy;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.Strategist;

import java.util.List;
import java.util.Map;

/**
 * Created by Alexx on 06.02.14
 */
public interface PowerStrategy extends Strategy {

    @Override
    void cleanUp();

    @Override
    Map<Cell, Integer> execute(List<Cell> cells, Field field, Strategist player);

    void prepare(Strategist player);
}
