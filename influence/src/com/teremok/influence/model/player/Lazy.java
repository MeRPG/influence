package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Match;

import java.util.List;

/**
 * Created by Alexx on 05.02.14
 */
public class Lazy extends ComputerPlayer {
    public Lazy(int type, Match match) {
        super(type, match);
    }

    @Override
    protected void prepareActions() {
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == type && cell.getPower() > 1) {
                List<Cell> enemies = field.getConnectedEnemies(cell);
                if (! enemies.isEmpty()) {
                    int cellNumberToAttack = getMinimalPowerNumber(enemies);
                    Cell enemy = enemies.get(cellNumberToAttack);
                    nextMove = new Move(cell, enemy);
                    break;
                }
            }
        }
    }

    private int getMinimalPowerNumber(List<Cell> cells) {
        int number = 0;
        int minimalPower = 0;

        System.out.println("Lazy -- Cells to attack: ");

        int[] powers = new int[6];


        for (int i = 0; i < cells.size(); i++) {
            Cell c = cells.get(i);
            if (c.getPower() < cells.get(number).getPower()) {
                number = i;
                minimalPower = c.getPower();
            }
            System.out.println("Lazy -- " + i + " " + c);
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

        System.out.println("Lazy -- cell number to attack: " + 0);
        System.out.println("Lazy -- cell: " + number);
        return number;
    }
}
