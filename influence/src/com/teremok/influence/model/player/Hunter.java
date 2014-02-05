package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Match;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Alexx on 05.02.14
 */
public class Hunter extends ComputerPlayer {
    public Hunter(int type, Match match) {
        super(type, match);
    }

    @Override
    protected void actAttackLogic(float delta) {
        super.actAttackLogic(delta);
        toBePowered.clear();
    }

    HashSet<Integer> toBePowered = new HashSet<Integer>();

    @Override
    protected void actDistributeLogic(float delta) {
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == type && powerToDistribute > 0) {
                if (field.getConnectedEnemies(cell).isEmpty()) {
                    if (toBePowered.isEmpty()) {
                        field.addPower(cell);
                    }
                } else {
                    toBePowered.add(cell.getNumber());
                    field.addPower(cell);
                    if (cell.getPower() == cell.getMaxPower()) {
                        toBePowered.remove(cell.getNumber());
                    }
                }
            }
        }
        System.out.println("delta: " + delta + ";\tneedToPower: " + toBePowered.size());
    }

    @Override
    protected void prepareActions() {
        Cell cell = getCellToAct(field.getCells());
        if (cell.getPower() > 1) {
            List<Cell> enemies = field.getConnectedEnemies(cell);
            int cellNumberToAttack = getMinimalPowerNumber(enemies);
            Cell enemy = enemies.get(cellNumberToAttack);
            nextMove = new Move(cell, enemy);
        }
    }

    private Cell getCellToAct(List<Cell> cells) {
        int number;
        Cell cell = cells.get(0);
        boolean end = false;
        while(! end) {
            end = true;
            number = rnd.nextInt(cells.size());
            cell = cells.get(number);
            if (! cell.isValid())
                end = false;
            if (cell.getType()!= type)
                end = false;
            if (field.getConnectedEnemies(cell).isEmpty())
                end = false;
        }

        int maxPower = cell.getPower();

        for (Cell cell2 : cells) {
            if (cell2.isValid() && cell2.getType() == type) {
                if (cell2.getPower() > maxPower && ! field.getConnectedEnemies(cell2).isEmpty()) {
                    cell = cell2;
                    maxPower = cell2.getPower();
                }
            }
        }

        return cell;
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
