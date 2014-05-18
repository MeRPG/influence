package com.teremok.influence.model.player.strategy;

import com.teremok.influence.controller.FieldController;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.player.Strategist;

import java.util.List;

/**
 * Created by Alexx on 05.02.14
 */
public interface Strategy {
    Object execute(List<Cell> cells, FieldController field, Strategist player);
    void cleanUp();
}
