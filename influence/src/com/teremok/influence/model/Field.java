package com.teremok.influence.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.util.CellSchemeGenerator;
import com.teremok.influence.util.DrawHelper;
import com.teremok.influence.view.FieldDrawer;

import java.util.List;

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
                        System.out.println("Cell touched!");
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
            if (isCellsConnected(selectedCell, cell)) {
                cell.setType(selectedCell.getType());
            }
            selectedCell.setSelected(false);
        }

        cell.setSelected(true);
        selectedCell = cell;

    }

    private boolean isCellsConnected(Cell from, Cell to) {
        return minimal[from.getNumber()][to.getNumber()] == 1 || minimal[to.getNumber()][from.getNumber()] == 1;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        FieldDrawer.draw(this, batch, parentAlpha);
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
