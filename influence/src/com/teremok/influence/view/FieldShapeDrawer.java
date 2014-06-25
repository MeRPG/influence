package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.controller.FieldController;
import com.teremok.influence.controller.GestureController;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;

import static com.teremok.influence.view.Drawer.MIN_SIZE_FOR_TEXT;

/**
 * Created by Alexx on 23.12.13
 */
public class FieldShapeDrawer extends AbstractDrawer<FieldController> {

    float zoomedUnitSize;
    boolean[][] routes;

    @Override
    public void draw (FieldController field, Batch batch, float parentAlpha) {
        super.draw(field, batch, parentAlpha);

        FieldModel model = field.getModel();

        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        int maxCellsY = field.getModel().maxCellsY;
        zoomedUnitSize = Drawer.getUnitSize(maxCellsY)* GestureController.getZoom();

        if (routes == null){
            routes = new boolean[140][140];
        } else {
            for (int i = 0; i < 140; i++) {
                for (int j = 0; j < 140; j++) {
                    routes[i][j] = false;
                }
            }
        }

        renderer.begin(ShapeRenderer.ShapeType.Line);
        // draw Routes
        for (Cell c : model.cells) {
            drawCellRoutesShape(c);
        }

        // draw Empty
        for (Cell c : model.cells) {
            drawEmpty(c);
        }
        renderer.end();

        // draw Solids
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Cell c : model.cells) {
            drawSolid(c);
        }
        renderer.end();

        ////Logger.log("routes: " + counter + "(" + allCounter + ")");

        batch.begin();
        if (zoomedUnitSize > MIN_SIZE_FOR_TEXT) {
            for (Cell c : model.cells) {
                drawText(batch, c);
            }
        }
    }

    private void drawShapeBackground() {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Drawer.getBackgroundColor());
        renderer.rect(0, 0, current.getWidth(), current.getHeight());
        renderer.end();
    }

    private void drawSolid(Cell cell) {
        if (current.isCellVisible(cell)) {
            renderer.setColor(getColor(cell));
            renderer.circle(cell.getX() + FieldController.cellWidth/2, cell.getY() + FieldController.cellHeight/2, zoomedUnitSize * (0.4f + cell.getPower()*0.03f), 6);
        }
    }

    private void drawEmpty(Cell cell) {
        if (current.isCellVisible(cell)) {
            renderer.setColor(getColor(cell));
            renderer.circle(cell.getX() + FieldController.cellWidth/2, cell.getY() + FieldController.cellHeight/2, zoomedUnitSize * (0.4f + cell.getMaxPower()*0.03f), 6);
        }
    }

    private void drawText(Batch batch, Cell cell) {
        BitmapFont cellsFont = fontFactory.getCellsFont();
        if (cellsFont != null && current.isCellVisible(cell)) {
            BitmapFont.TextBounds textBounds = cellsFont.getBounds(cell.getPower()+"");
            if (cell.isFree()) {
                cellsFont.setColor(Drawer.getEmptyCellTextColor());
            } else {
                cellsFont.setColor(Drawer.getCellTextColor());
            }
            cellsFont.draw(batch, cell.getPower()+"", current.getX() + cell.getX() + FieldController.cellWidth/2 - textBounds.width/2,
                    current.getY() + cell.getY() + FieldController.cellHeight/2 + textBounds.height/2);
        }
    }

    private Color getColor(Cell cell) {
        Color color;
        if (cell.isSelected()) {
            color = Drawer.getCellSelectedColorByNumber(cell.getType());
        } else {
            color = Drawer.getCellColorByNumber(cell.getType());
        }
        return color;
    }

    private void drawCellRoutesShape(Cell cell) {
        for (Cell toCell : cell.getNeighbors()) {
            int fromCode =  cell.getNumber();
            int toCode =  toCell.getNumber();
            if (! (routes[fromCode][toCode] || routes[toCode][fromCode])) {
                routes[fromCode][toCode] = true;
                routes[toCode][fromCode] = true;

                if (current.isCellVisible(cell) || current.isCellVisible(toCell)) {
                    float centerX = cell.getX() + FieldController.cellWidth/2;
                    float centerY = cell.getY() + FieldController.cellHeight/2;

                    float centerXto = toCell.getX() + FieldController.cellWidth/2;
                    float centerYto = toCell.getY() + FieldController.cellHeight/2;

                    renderer.line(centerX, centerY, centerXto, centerYto,
                            getColor(cell),
                            getColor(toCell));
                }
            }
        }
    }

    @Override
    protected void drawBoundingBox() {

        Cell selected = current.getSelectedCell();

        if (selected != null && selected.isValid()) {
            renderer.setColor(Drawer.getCellColorByNumber(selected.getType()));
        } else {
            renderer.setColor(Color.WHITE);
        }

        super.drawBoundingBox();
    }
}
