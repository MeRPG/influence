package com.teremok.influence.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.util.CellSchemeGenerator;
import com.teremok.influence.util.DrawHelper;
import com.teremok.influence.view.Drawer;

import java.util.List;
import java.util.Random;

/**
 * Created by Alexx on 11.12.13
 */
public class Field extends Group {

    public static final int MAX_CELLS_Y = 7;
    public static final int MAX_CELLS_X = 5;

    public static final float WIDTH = DrawHelper.UNIT_SIZE*10f;
    public static final float HEIGHT = DrawHelper.UNIT_SIZE*13f;

    private int[][] matrix;
    private int[][] minimal;
    private List<Cell> cells;
    private CellSchemeGenerator generator;

    private Cell selectedCell;

    public Field() {

        float actorX = 0f;
        float actorY = AbstractScreen.HEIGHT - HEIGHT-1;

        float actorWidth = WIDTH-1f;
        float actorHeight = HEIGHT;

        setBounds(actorX, actorY, actorWidth, actorHeight);

        regenerate();
    }

    public void setCells(List<Cell> cells) {
        registerCellsForDrawing(cells);
        this.cells = cells;
    }

    public void regenerate() {
        generator = new CellSchemeGenerator(25);
        generator.generate();
        cells = generator.getCells();
        registerCellsForDrawing(cells);
        matrix = generator.getMatrix();
        minimal = generator.getMinimal();
    }

    public void updateMinimal() {
        generator.updateMinimal();
        cells = generator.getCells();
        registerCellsForDrawing(cells);
        matrix = generator.getMatrix();
        minimal = generator.getMinimal();
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
                        setSelectedCell((Cell)event.getTarget());
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

                int a = selectedCell.getPower();
                int b = cell.getPower();

                int fightResult = fight(selectedCell, cell);

                System.out.println("Result: " + fightResult);

                switch (fightResult) {
                    case 0:
                        cell.setPowerGreaterZero(b - a + 1);
                        selectedCell.setPower(1);
                        break;
                    case 3:
                        cell.setPowerGreaterZero(a - 1);
                        cell.setType(selectedCell.getType());
                        selectedCell.setPower(1);
                        break;
                    case 1:
                        cell.setPowerGreaterZero(a - b - 1);
                        cell.setType(selectedCell.getType());
                        selectedCell.setPower(1);
                        break;
                    case -1:
                        cell.setPowerGreaterZero(b - Math.abs(a - b));
                        selectedCell.setPower(a - Math.abs(a - b));
                        break;
                    case -3:
                        selectedCell.setPower(1);
                        break;
                    default:
                }

                System.out.println(selectedCell.getPower() + " \t--\t " + cell.getPower());
                System.out.println(" - - - ");

                if (fightResult > 0) {
                    selectedCell.setSelected(false);
                    cell.setSelected(true);
                    selectedCell = cell;
                }
            }
            selectedCell.setSelected(false);
            cell.setSelected(true);
            selectedCell = cell;
        } else {
            cell.setSelected(true);
            selectedCell = cell;
        }
    }

    private int fight(Cell attack, Cell defense) {

        System.out.println("Attack!");
        System.out.println(attack.getPower() + " \t->\t " + defense.getPower());

        int attackDice = rollDice(attack.getPower());
        int defenseDice = rollDice(defense.getPower());

        System.out.println(attackDice + " \t->\t " + defenseDice);

        int delta = attackDice - defenseDice;

        System.out.println("Delta: " + delta);

        if (delta == 0) {
            return 0;
        }
        if (delta > 3) {
            return 3;
        }
        if (delta > 0) {
            return 1;
        }
        if (delta > -3) {
            return -1;
        }
        return -3;
    }

    private int rollDice(int number) {
        int result = 0;
        Random rnd = new Random();
        for (int i = 0; i < number; i++) {
            result += rnd.nextInt(6) + 1;
        }
        return result;
    }

    private boolean isCellsConnected(Cell from, Cell to) {

        if (from.getNumber() == to.getNumber())
            return false;

        return minimal[from.getNumber()][to.getNumber()] == 1 || minimal[to.getNumber()][from.getNumber()] == 1;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        Drawer.draw(this, batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    // Auto-generated

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int[][] getMinimal() {
        return minimal;
    }

    public List<Cell> getCells() {return cells; }

}
