package com.teremok.influence.model.player.strategy.enemy;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.player.Strategist;
import com.teremok.influence.model.player.strategy.EnemyStrategy;

import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 05.02.14
 */
public class LazyEnemyStrategy implements EnemyStrategy {
    @Override
    public Cell execute(List<Cell> cells, Field field, Strategist player) {
        Random rnd = player.getRnd();

        int number = 0;
        int minimalPower = 0;

        //Logger.log("Lazy -- Cells to attack: ");

        int[] powers = new int[6];


        for (int i = 0; i < cells.size(); i++) {
            Cell c = cells.get(i);
            if (c.getPower() < cells.get(number).getPower()) {
                number = i;
                minimalPower = c.getPower();
            }
            //Logger.log("Lazy -- " + i + " " + c);
            powers[i] = c.getPower();
        }

        int cellsWithMinimal = 0;
        int[] numbersOfMinimal = new int[6];
        for (int i = 0; i < cells.size(); i++) {
            if (powers[i] == minimalPower) {
                numbersOfMinimal[cellsWithMinimal] = i;
                cellsWithMinimal++;
            }
        }

        if (cellsWithMinimal > 0) {
            number = numbersOfMinimal[rnd.nextInt(cellsWithMinimal)];
        }

        //Logger.log("Lazy -- cell number to attack: " + 0);
        //Logger.log("Lazy -- cell: " + number);
        return cells.get(number);
    }

    @Override
    public void afterExecute() {

    }
}
