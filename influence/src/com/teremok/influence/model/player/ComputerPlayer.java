package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Match;
import com.teremok.influence.model.Settings;

import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 29.12.13
 */
public class ComputerPlayer extends Player {

    protected Move nextMove;
    protected Random rnd = new Random();
    protected float turnTime;

    protected class Move {
        protected Cell cell;
        protected Cell enemy;

        protected Move(Cell cell, Cell enemy) {
            this.cell = cell;
            this.enemy = enemy;
        }
    }

    public ComputerPlayer(int type, Match match) {
        super(type, match);
        this.type = PlayerType.Dummy;
    }

    protected void actLogic(float delta) {
        if (match.isInAttackPhase()) {
            actAttackLogic(delta);
        } else {
            actDistributeLogic(delta);
        }
    }

    protected void actAttackLogic(float delta) {
        if (turnTime > Settings.speed) {
            if (nextMove == null) {
                prepareActions();
                if (nextMove == null) {
                    match.switchPhase();
                }
            } else {
                doNextMove();
                turnTime = 0;
            }
        }
        turnTime += delta;
    }

    protected void prepareActions() {
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == number && cell.getPower() > 1) {
                List<Cell> enemies = cell.getEnemiesList();
                if (! enemies.isEmpty()) {
                    int cellNumberToAttack = rnd.nextInt(enemies.size());
                    Cell enemy = enemies.get(cellNumberToAttack);
                    nextMove = new Move(cell, enemy);
                    break;
                }
            }
        }
    }

    protected void doNextMove() {
        Cell cell;
        if (nextMove.cell != null) {
            cell = nextMove.cell;
            nextMove.cell = null;
        } else {
            cell = nextMove.enemy;
            nextMove = null;
        }

        field.setSelectedCell(cell);
    }

    protected void actDistributeLogic(float delta) {
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == number && powerToDistribute > 0) {
                field.addPower(cell);
            }
        }
    }
}
