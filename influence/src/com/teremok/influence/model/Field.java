package com.teremok.influence.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.screen.GameScreen;
import com.teremok.influence.util.CellSchemeGenerator;
import com.teremok.influence.util.DrawHelper;
import com.teremok.influence.view.Drawer;

import java.util.List;

/**
 * Created by Alexx on 11.12.13
 */
public class Field extends Group {

    public static final int MAX_CELLS_Y = 7;
    public static final int MAX_CELLS_X = 5;

    public static final float WIDTH = DrawHelper.UNIT_SIZE*10f;
    public static final float HEIGHT = DrawHelper.UNIT_SIZE*13f;

    private static final int GREAT_WIN_DELTA = 7;

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
                        if (! event.isHandled()) {
                            if (GameScreen.currentPhase == GameScreen.TurnPhase.ATTACK) {
                                setSelectedCell((Cell)event.getTarget());
                            } else {
                                addPower((Cell)event.getTarget());
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

                int a = selectedCell.getPower();
                int b = cell.getPower();

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

    private void addPower(Cell cell) {
        if (cell.getType() == Player.current().getType()) {
            int newPower = cell.getPower() + 1;
            int maxPower = cell.getMaxPower();
            if (newPower <= maxPower) {
                cell.setPower(cell.getPower() + 1);
                Player.current().subtractPowerToDistribute();
            }
        }
    }

    private void reallySetSelected(Cell cell) {
        if (cell.getType() == Player.current().getType()) {
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

        attack.setPower(Calculator.getResultPowerA());
        defense.setPower(Calculator.getResultPowerB());

        return delta;
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
