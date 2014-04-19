package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.GestureController;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.util.Logger;

import java.util.HashSet;
import java.util.Set;

import static com.teremok.influence.view.Drawer.MIN_SIZE_FOR_TEXT;

/**
 * Created by Alexx on 23.12.13
 */
public class FieldShapeDrawer extends AbstractDrawer<Field> {

    Set<Integer> routerDraw;
    int counter = 0;
    int allCounter = 0;
    float zoomedUnitSize;

    @Override
    public void draw (Field field, SpriteBatch batch, float parentAlpha) {
        super.draw(field, batch, parentAlpha);
        batch.end();
        Gdx.gl.glEnable(GL10.GL_BLEND);
        zoomedUnitSize = Drawer.getUnitSize()* GestureController.getZoom();

        counter = 0;
        allCounter = 0;
        if (routerDraw == null){
            routerDraw = new HashSet<Integer>();
        } else {
            routerDraw.clear();
        }

        renderer.begin(ShapeRenderer.ShapeType.Line);
        // draw Routes
        for (Cell c : current.getCells()) {
            drawCellRoutesShape(c);
        }

        // draw Empty
        for (Cell c : current.getCells()) {
            drawEmpty(c);
        }
        renderer.end();

        // draw Solids
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Cell c : current.getCells()) {
            drawSolid(c);
        }
        renderer.end();

        ////Logger.log("routes: " + counter + "(" + allCounter + ")");

        batch.begin();
        if (zoomedUnitSize > MIN_SIZE_FOR_TEXT) {
            for (Cell c : current.getCells()) {
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
            renderer.circle(cell.getX() + Field.cellWidth/2, cell.getY() + Field.cellHeight/2, zoomedUnitSize * (0.4f + cell.getPower()*0.03f), 6);
        }
    }

    private void drawEmpty(Cell cell) {
        if (current.isCellVisible(cell)) {
            renderer.setColor(getColor(cell));
            renderer.circle(cell.getX() + Field.cellWidth/2, cell.getY() + Field.cellHeight/2, zoomedUnitSize * (0.4f + cell.getMaxPower()*0.03f), 6);
        }
    }

    private void drawText(SpriteBatch batch, Cell cell) {
        if (bitmapFont != null && current.isCellVisible(cell)) {
            BitmapFont.TextBounds textBounds = bitmapFont.getBounds(cell.getPower()+"");
            if (cell.isFree()) {
                bitmapFont.setColor(Drawer.getEmptyCellTextColor());
            } else {
                bitmapFont.setColor(Drawer.getCellTextColor());
            }
            bitmapFont.draw(batch, cell.getPower()+"", current.getX() + cell.getX() + Field.cellWidth/2 - textBounds.width/2,
                    current.getY() + cell.getY() + Field.cellHeight/2 + textBounds.height/2);
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
            Integer code = cell.getNumber() * 1000 + toCell.getNumber();

            if (! routerDraw.contains(code)) {

                routerDraw.add(code);
                code = toCell.getNumber() * 1000 + cell.getNumber();
                routerDraw.add(code);

                if (current.isCellVisible(cell) || current.isCellVisible(toCell)) {
                    float centerX = cell.getX() + Field.cellWidth/2;
                    float centerY = cell.getY() + Field.cellHeight/2;

                    float centerXto = toCell.getX() + Field.cellWidth/2;
                    float centerYto = toCell.getY() + Field.cellHeight/2;

                    renderer.line(centerX, centerY, centerXto, centerYto,
                            getColor(cell),
                            getColor(toCell));
                }

                counter++;
            }
            allCounter++;
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
