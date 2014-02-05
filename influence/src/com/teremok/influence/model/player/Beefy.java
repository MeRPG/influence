package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Match;

import java.util.List;

/**
 * Created by Alexx on 05.02.14
 */
public class Beefy extends ComputerPlayer {

    public Beefy(int type, Match match) {
        super(type, match);
    }

    @Override
    protected void prepareActions() {
        Cell cell = getCellToAct(field.getCells());
        if (cell.getPower() > 1) {
            List<Cell> enemies = field.getConnectedEnemies(cell);
            int cellNumberToAttack = rnd.nextInt(enemies.size());
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
}
