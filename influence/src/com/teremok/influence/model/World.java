package com.teremok.influence.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.teremok.influence.util.CellSchemeGenerator;
import com.teremok.influence.util.RenderHelper;

import java.util.List;

/**
 * Created by Alexx on 11.12.13.
 */
public class World extends Group {

    public static final int MAX_CELLS_Y = 7;
    public static final int MAX_CELLS_X = 5;

    //public static final float WIDTH = 7f;
    //public static final float HEIGHT = 8.5f;

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

    ShapeRenderer renderer;
    BitmapFont bitmapFont;

    public World() {

        float actorX = 0f;
        //setY(RenderHelper.reflectY(0));
        float actorY = RenderHelper.SCREEN_HEIGHT - RenderHelper.FIELD_HEIGHT-1;

        float actorWidth = RenderHelper.FIELD_WIDTH-1f;
        float actorHeight = RenderHelper.FIELD_HEIGHT;

        setBounds(actorX, actorY, actorWidth, actorHeight);

        renderer = new ShapeRenderer();

        setP(7);
        regenerate();
    }

    public List<Cell> getCells() {return cells; }

    private void registerCellsForDrawing(List<Cell> cells) {
        this.clear();

        for (final Cell cell : cells) {
            if (cell.isValid()) {
                cell.setBitmapFont(bitmapFont);
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
         batch.end();

        for (Cell c : cells) {
            drawCellRoutes(c);
        }

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(0, 0, getWidth(), getHeight());
        renderer.end();

        batch.begin();

        super.draw(batch, parentAlpha);
    }

    private void drawCellRoutes(Cell cell) {
        for (int j = 0; j < 35; j++) {
            if (cell.isValid() && minimal[cell.getNumber()][j] == 1) {
                Cell toCell = cells.get(j);
                if (toCell.isValid()) {
                    if (cell.getType() == toCell.getType()) {
                        renderer.setColor(RenderHelper.getCellColorByType(cell.getType()));
                    } else {
                        renderer.setColor(Color.GRAY);
                    }
                    renderer.begin(ShapeRenderer.ShapeType.Line);
                    renderer.line(cell.getX()+cell.getWidth()/2, cell.getY() + cell.getHeight()/2,
                                toCell.getX()+toCell.getWidth()/2, toCell.getY() + toCell.getHeight()/2);
                    renderer.end();
                }
            }
        }
    }

    public void setBitmapFont(BitmapFont bitmapFont) {
        this.bitmapFont = bitmapFont;
    }
}
