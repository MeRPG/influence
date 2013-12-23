package com.teremok.influence.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.util.CellSchemeGenerator;
import com.teremok.influence.util.RenderHelper;
import com.teremok.influence.view.FieldDrawer;

import java.util.List;

/**
 * Created by Alexx on 11.12.13.
 */
public class Field extends Group {

    public static final int MAX_CELLS_Y = 7;
    public static final int MAX_CELLS_X = 5;

    public static final float WIDTH = RenderHelper.UNIT_SIZE*10f;
    public static final float HEIGHT = RenderHelper.UNIT_SIZE*13f;

    private int[][] matrix;
    private int[][] minimal;
    private List<Cell> cells;
    private CellSchemeGenerator generator;

    private Cell selectedCell;

    private int P;

    public void setP(int p) {
        this.P = p;
    }

    public int getP() {
        return this.P;
    }


    public Field() {

        float actorX = 0f;
        float actorY = RenderHelper.SCREEN_HEIGHT - HEIGHT-1;

        float actorWidth = WIDTH-1f;
        float actorHeight = HEIGHT;

        setBounds(actorX, actorY, actorWidth, actorHeight);

        setP(7);
        regenerate();
    }

    public List<Cell> getCells() {return cells; }

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

    public void setCells(List<Cell> cells) {
        registerCellsForDrawing(cells);
        this.cells = cells;
    }

    public void regenerate() {
        generator = new CellSchemeGenerator(25, P*0.1f);
        generator.generate();
        cells = generator.getCells();
        registerCellsForDrawing(cells);
        matrix = generator.getMatrix();
        minimal = generator.getMinimal();
    }

    public void updateMinimal() {
        generator.updateMinimal(P*0.1f);
        cells = generator.getCells();
        registerCellsForDrawing(cells);
        matrix = generator.getMatrix();
        minimal = generator.getMinimal();
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int[][] getMinimal() {
        return minimal;
    }

    private boolean isCellsConnected(Cell from, Cell to) {
        return minimal[from.getNumber()][to.getNumber()] == 1 || minimal[to.getNumber()][from.getNumber()] == 1;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(Cell cell) {
        if (selectedCell != null && isCellsConnected(selectedCell, cell))
            cell.setType(selectedCell.getType());

        if (selectedCell != null)
            selectedCell.setSelected(false);

        cell.setSelected(true);
        selectedCell = cell;

    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        FieldDrawer.draw(this, batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }
}
