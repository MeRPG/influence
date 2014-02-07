package com.teremok.influence.model.player.strategy;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.player.Strategist;

import java.util.List;

/**
 * Created by Alexx on 06.02.14
 */
public interface PowerStrategy extends Strategy {
    @Override
    Cell execute(List<Cell> cells, Field field, Strategist player);
}
