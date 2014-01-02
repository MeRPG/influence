package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.screen.GameScreen;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alexx
 * Date: 29.12.13
 * Time: 17:00
 * Email: alexey.gorovoy.work@gmail.com
 */
public class ComputerPlayer extends Player {

    public final static float TURN_DELAY = .50f;
    private Map<Integer, Cell> actions;
    private GameScreen screen;
    private Field field;
    private Random rnd = new Random();

    private ComputerPlayer(int type) {
        super(type);
    }

    public ComputerPlayer(int type, GameScreen screen, Field field) {
        this(type);
        this.screen = screen;
        this.field = field;
    }

    protected void actLogic(float delta) {
        if (GameScreen.currentPhase == GameScreen.TurnPhase.ATTACK) {
            actAttackLogic(delta);
        } else {
            actDistributeLogic(delta);
        }
    }

    private float turnTime = 0;
    private int turn = 0;
    boolean oneMoreMove;

    private void actAttackLogic(float delta) {

        if (turnTime == 0 || oneMoreMove) {
            prepareActions();

        } else {
            if (turnTime / TURN_DELAY > turn ) {
                doFirstAction();
            }
        }
        turnTime += delta;
    }

    private void prepareActions() {
        int t = 0;
        actions = new HashMap<Integer, Cell>();
        turn = 0;
        oneMoreMove = false;
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == type && cell.getPower() > 1) {
                List<Cell> enemies = field.getConnectedEnemies(cell);
                if (! enemies.isEmpty()) {
                    int cellNumberToAttack = rnd.nextInt(enemies.size());
                    Cell cellToAttack = enemies.get(cellNumberToAttack);
                    actions.put(t++, cell);
                    actions.put(t++, cellToAttack);
                }
            }
        }
    }

    private void doFirstAction() {
        Cell cell = actions.get(turn);
        if (! actions.isEmpty() && cell != null){
            cell = actions.get(turn);

            int beforeType = cell.getType();
            System.out.println("Selecting cell: " + cell);
            field.setSelectedCell(cell);

            actions.remove(turn);

            if (cell.getType() != beforeType && cell.getPower() > 1) {
                oneMoreMove = true;
                turnTime = 0;
            }

            turn++;
        } else {
            turn = 0;
            turnTime = 0;
            screen.setDistributePhase();
        }
    }

    private void actDistributeLogic(float delta) {
        System.out.println("Distributing power!");
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == type && power > 0) {
                field.addPower(cell);
            }
        }
        oneMoreMove = true;
    }

    // Auto-generated

    public void setField(Field field) {
        this.field = field;
    }
}
