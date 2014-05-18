package com.teremok.influence.model.player.strategy;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;
import com.teremok.influence.model.player.Strategist;

import java.util.List;

/**
 * Created by Alexx on 05.02.14
 */
public interface AttackStrategy extends Strategy {
    @Override
    Cell execute(List<Cell> cells, FieldModel fieldModel, Strategist player);
}
