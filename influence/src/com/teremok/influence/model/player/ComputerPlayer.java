package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.screen.GameScreen;

import java.util.*;

/**
 * Created by Alexx on 29.12.13
 */
public class ComputerPlayer extends Player {

    public final static float TURN_DELAY = .5f;
    private Move nextMove;
    private Random rnd = new Random();
    private float turnTime;

    private class Move {
        private Cell cell;
        private Cell enemy;

        private Move(Cell cell, Cell enemy) {
            this.cell = cell;
            this.enemy = enemy;
        }
    }

    public ComputerPlayer(int type, GameScreen screen, Field field) {
        super(type, screen, field);
    }

    protected void actLogic(float delta) {
        if (gameScreen.currentPhase == GameScreen.TurnPhase.ATTACK) {
            System.out.println("actAttackLogic, time = " + turnTime);
            actAttackLogic(delta);
        } else {
            System.out.println("actDistributeLogic, time = " + turnTime);
            actDistributeLogic(delta);
        }
    }

    private void actAttackLogic(float delta) {

        if (nextMove == null) {
            System.out.println("prepareActions, time = " + turnTime);
            prepareActions();

            turnTime = 0;
            if (nextMove == null) {
                System.out.println("setDistributePhase, time = " + turnTime);
                gameScreen.setDistributePhase();
            }

        } else {
            if (turnTime > TURN_DELAY) {
                System.out.println("doNextMove, time = " + turnTime);
                doNextMove();
                turnTime = 0;
            }
        }
        turnTime += delta;
    }

    private void prepareActions() {
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == type && cell.getPower() > 1) {
                List<Cell> enemies = field.getConnectedEnemies(cell);
                if (! enemies.isEmpty()) {
                    int cellNumberToAttack = rnd.nextInt(enemies.size());
                    Cell enemy = enemies.get(cellNumberToAttack);
                    nextMove = new Move(cell, enemy);
                    break;
                }
            }
        }
    }

    private void doNextMove() {
        Cell cell;
        if (nextMove.cell != null) {
            cell = nextMove.cell;
            nextMove.cell = null;
        } else {
            cell = nextMove.enemy;
            nextMove = null;
        }

        System.out.println("Selecting cell: " + cell);
        field.setSelectedCell(cell);
    }

    private void actDistributeLogic(float delta) {
        if (turnTime > TURN_DELAY) {
            System.out.println("Distributing power!");
            for (Cell cell : field.getCells()) {
                if (cell.isValid() && cell.getType() == type && powerToDistribute > 0) {
                    field.addPower(cell);
                }
            }
        }
        turnTime += delta;
    }
}
