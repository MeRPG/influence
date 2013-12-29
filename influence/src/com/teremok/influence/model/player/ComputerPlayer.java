package com.teremok.influence.model.player;

import android.util.Pair;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.screen.GameScreen;

import java.util.HashMap;
import java.util.Map;

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

    public ComputerPlayer(int type) {
        super(type);
    }

    public ComputerPlayer(int type, GameScreen screen) {
        this(type);
        this.screen = screen;
    }

    protected void actLogic(float delta) {
        if (GameScreen.currentPhase == GameScreen.TurnPhase.ATTACK) {
            actAttackLogic(delta);
        } else {
            actDistributeLogic(delta);
        }
    }

    private float timelost = 0;
    private int turn = 0;
    boolean oneMoreMove;

    private void actAttackLogic(float delta) {

        if (timelost == 0 || oneMoreMove) {
            prepareActions();

        }  else {
            if (timelost / TURN_DELAY > turn ) {
                doFirstAction();
            }
        }
        timelost += delta;
    }

    private void prepareActions() {
        int t = 0;
        actions = new HashMap<Integer, Cell>();
        turn = 0;
        oneMoreMove = false;
        outer:
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == type) {
                inner:
                for (Cell enemy : field.getConnectedCells(cell)) {
                    if ( cell.getPower() != 0 && cell.getType() != enemy.getType()  ) {
                        actions.put(t++,cell);
                        actions.put(t++,enemy);
                        break outer;
                    }
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
            }

            turn++;
        } else {
            turn = 0;
            timelost = 0;
            screen.setDistributePhase();
        }
    }

    private void actDistributeLogic(float delta) {
        System.out.println("Distributing power!");
        for (Cell cell : field.getCells()) {
            if (cell.isValid() && cell.getType() == type) {
                field.addPower(cell);
            }
        }
        oneMoreMove = true;
    }

}
