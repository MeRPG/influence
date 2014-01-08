package com.teremok.influence.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.view.Drawer;
import com.teremok.influence.util.GraphGenerator;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.ui.Tooltip;
import com.teremok.influence.ui.TooltipHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 11.12.13
 */
public class Field extends Group {

    public static final int MAX_CELLS_Y = 7;
    public static final int MAX_CELLS_X = 5;

    public static final float WIDTH = Drawer.UNIT_SIZE*10f;
    public static final float HEIGHT = Drawer.UNIT_SIZE*13f;

    private static final int CELLS_COUNT = 25;

    private static final int INITIAL_CELL_POWER = 2;

    private int[][] graphMatrix;
    private List<Cell> cells;
    private GraphGenerator generator;

    private Match match;
    private PlayerManager pm;
    private Cell selectedCell;

    public Field(Match match) {
        this.match = match;
        this.pm = match.getPm();

        float actorX = 0f;
        float actorY = AbstractScreen.HEIGHT - HEIGHT-1;

        float actorWidth = WIDTH-1f;
        float actorHeight = HEIGHT;

        setBounds(actorX, actorY, actorWidth, actorHeight);

        regenerate();
    }

    public void regenerate() {
        generator = new GraphGenerator(CELLS_COUNT);
        generator.generate();
        cells = generator.getCells();
        registerCellsForDrawing(cells);
        graphMatrix = generator.getMatrix();
    }

    public void placeStartPosition(int type) {
        Random rnd = new Random();
        int number;
        Cell target;

        do {
            number = rnd.nextInt(cells.size());
            target = cells.get(number);

            if (isValidForStartPosition(target)) {
                break;
            }

        } while (true);
        System.out.println("Placing player: " + target);
        target.setPower(INITIAL_CELL_POWER);
        target.setType(type);
    }

    public void placeStartPositionFromRange(int type, int startNumber, int endNumber) {

        if (startNumber < 0 || startNumber >= cells.size()) {
            startNumber = 0;
        }

        if (endNumber <= 0 || endNumber >= cells.size()) {
            endNumber = cells.size() - 1;
        }

        if (endNumber < startNumber) {
            int tmp = startNumber;
            startNumber = endNumber;
            endNumber = tmp;
        }

        Random rnd = new Random();
        int number;
        Cell target;

        do {
            number = rnd.nextInt(cells.size());
            target = cells.get(number);

            if (isValidForStartPosition(target) && isBetween(number, startNumber, endNumber)) {
                break;
            }

        } while (true);
        System.out.println("Placing player: " + target);
        target.setPower(INITIAL_CELL_POWER);
        target.setType(type);
    }

    private boolean isBetween (int number, int start, int end) {
        return number >= start && number <= end;
    }

    private boolean isValidForStartPosition(Cell target) {
        if (target.isValid() && target.getType() == -1) {
            for (Cell enemy : getConnectedEnemies(target)) {
                if (enemy.getType() != -1){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void registerCellsForDrawing(List<Cell> cells) {
        this.clear();

        for (final Cell cell : cells) {
            if (cell.isValid()) {
                cell. addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchDown(event,x,y,pointer,button);
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (! event.isHandled() && match.canHumanActing()) {
                            Cell target = (Cell)event.getTarget();
                            if (match.isInAttackPhase()) {
                                setSelectedCell(target);
                            } else {
                                HumanPlayer player = (HumanPlayer)pm.current();
                                player.addPowered(target.getNumber());
                                addPower(target);
                            }
                        }
                    }
                });
                cell.setTouchable(Touchable.enabled);

                this.addActor(cell);
            }
        }
    }

    public void setSelectedCell(Cell cell) {

        if (selectedCell != null) {
            if (isCellsConnected(selectedCell, cell)
                    && selectedCell.getPower() > 1
                    && selectedCell.getType() != cell.getType()) {

                int delta = fight(selectedCell, cell);

                if (delta > 0) {
                    cell.setType(selectedCell.getType());
                    reallySetSelected(cell);
                }

            } else  {
                reallySetSelected(cell);
            }
        } else {
            reallySetSelected(cell);
        }
    }

    public void addPower(Cell cell) {
        if (cell.getType() == pm.current().getType()) {
            int newPower = cell.getPower() + 1;
            int maxPower = cell.getMaxPower();
            if (newPower <= maxPower) {

                //riseAddPowerTooltip(cell);

                cell.setPower(cell.getPower() + 1);
                pm.current().subtractPowerToDistribute();
            }
        }
    }

    private void reallySetSelected(Cell cell) {
        if (cell.getType() == pm.current().getType()) {
            if (selectedCell != null)
                selectedCell.setSelected(false);
            cell.setSelected(true);
            selectedCell = cell;
        }
    }

    public void resetSelection() {
        if (selectedCell != null) {
            selectedCell.setSelected(false);
            selectedCell = null;
        }
    }

    private int fight(Cell attack, Cell defense) {

        System.out.println("Attack!");
        System.out.println(attack.getPower() + " \t->\t " + defense.getPower());

        int delta = Calculator.fight(attack.getPower(), defense.getPower());

        riseDiceTooltips(attack, defense);

        setResultPower(attack, defense);

        return delta;
    }

    private void setResultPower(Cell attack, Cell defense) {
        int calcA = Calculator.getResultPowerA();
        int calcB = Calculator.getResultPowerB();

        if (calcB > defense.getMaxPower()) {
            calcA += calcB - defense.getMaxPower();
            calcB = defense.getMaxPower();
        }

        attack.setPower(calcA);
        defense.setPower(calcB);
    }

    private boolean isCellsConnected(Cell from, Cell to) {

        if (from.getNumber() == to.getNumber())
            return false;

        if (! (from.isValid() && to.isValid()))
            return false;

        return graphMatrix[from.getNumber()][to.getNumber()] == 1 || graphMatrix[to.getNumber()][from.getNumber()] == 1;
    }

    public int getPowerToDistribute(int type){
        int power = 0;
        int maxCapacity = 0;
        for (Cell cell : cells) {
            if (cell.isValid() && cell.getType() == type) {
                power += 1;
                maxCapacity += cell.getMaxPower() - cell.getPower();
            }
        }
        if (power > maxCapacity)
            power = maxCapacity;
        return power;
    }


    public void riseDiceTooltips(Cell attack, Cell defense) {
        String message = Calculator.getN() + "";
        BitmapFont font = AbstractDrawer.getBitmapFont();
        Color color;
        if (Calculator.getDelta() >= 0) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
        float tooltipX = calculateTooltipX(attack.getX());
        float tooltipY = calculateTooltipY(attack.getY());
        TooltipHandler.addTooltip(new Tooltip(message, font, color, tooltipX, tooltipY));

        message = Calculator.getM() + "";
        if (Calculator.getDelta() <= 0) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
        tooltipX = calculateTooltipX(defense.getX());
        tooltipY = calculateTooltipY(defense.getY());
        TooltipHandler.addTooltip(new Tooltip(message, font, color, tooltipX, tooltipY, 0.75f));

    }

    public void riseAddPowerTooltip(Cell cell) {

        String message = "+1";
        BitmapFont font = AbstractDrawer.getBitmapFont();
        Color color = Color.GREEN;

        float tooltipX = calculateTooltipX(cell.getX());
        float tooltipY = calculateTooltipY(cell.getY());
        TooltipHandler.addTooltip(new Tooltip(message, font, color, tooltipX, tooltipY));

    }

    private float calculateTooltipX(float cellX) {
        return getX() + cellX + Drawer.UNIT_SIZE/2;
    }

    private float calculateTooltipY(float cellY) {
        return getY() + cellY + Drawer.UNIT_SIZE/2;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        com.teremok.influence.view.Drawer.draw(this, batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    public List<Cell> getConnectedCells(Cell cell) {
        List<Cell> list = new LinkedList<Cell>();

        for (int i = 0; i < MAX_CELLS_X*MAX_CELLS_Y; i++) {
            Cell cellToAdd = cells.get(i);
            if (isCellsConnected(cell, cellToAdd)) {
                if (cellToAdd.isValid())
                    list.add(cellToAdd);
            }
        }

        return list;
    }

    public List<Cell> getConnectedEnemies(Cell cell) {
        List<Cell> enemies = new LinkedList<Cell>();
        for (Cell enemy : getConnectedCells(cell)) {
            if (enemy.getType() != cell.getType()) {
                enemies.add(enemy);
            }
        }
        return enemies;
    }

    public int calcScore(int type) {
        int score = 0;
        for (Cell cell : cells) {
            if (cell.isValid() && cell.getType() == type) {
                score += cell.getPower();
            }
        }
        return score;
    }

    // Auto-generated

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public int[][] getGraphMatrix() {
        return graphMatrix;
    }

    public List<Cell> getCells() {return cells; }
}
