package com.teremok.influence.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.GestureController;
import com.teremok.influence.model.Match;
import com.teremok.influence.util.Logger;

import java.util.HashSet;
import java.util.Set;

import static com.teremok.influence.view.Drawer.*;

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
        zoomedUnitSize = Drawer.getUnitSize()* GestureController.getZoom();

        drawShapeBackground();

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
        renderer.setColor(getColor(cell));
        if (cell.getUnitsX() == cell.getUnitsY() && cell.getUnitsX() == 0)
            Logger.log("draw solid w/color = " + renderer.getColor().r + "; " +  renderer.getColor().g + "; " +  renderer.getColor().b + "; " +  renderer.getColor().a);

        renderer.circle(cell.getX() + Field.cellWidth/2, cell.getY() + Field.cellHeight/2, zoomedUnitSize * (0.4f + cell.getPower()*0.03f), 6);
    }

    private void drawEmpty(Cell cell) {
        if (isNeedToDraw(cell)) {
            renderer.setColor(getColor(cell));
            renderer.circle(cell.getX() + Field.cellWidth/2, cell.getY() + Field.cellHeight/2, zoomedUnitSize * (0.4f + cell.getMaxPower()*0.03f), 6);
        }
    }

    private void drawText(SpriteBatch batch, Cell cell) {
        if (bitmapFont != null) {
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

        float absoluteCellX = cell.getX() + current.getX();
        float absoluteCellY = cell.getY() + current.getY();

        if (cell.getUnitsX() == cell.getUnitsY() && cell.getUnitsX() == 0)
            Logger.log("cell abs coords: " + absoluteCellX + "; " + absoluteCellY);

        if (absoluteCellX < 0f) {
            float deltaX = Math.abs(current.getX());
            float deltaY = Math.abs(95f - cell.getY());

            float maxDelta = deltaX > deltaY ? deltaX : deltaY;

            Color newColor = color.cpy();

            //newColor.a = maxDelta / (Field.cellWidth*GestureController.getZoom());

            newColor.a = 0f;

            if (cell.getUnitsX() == cell.getUnitsY() && cell.getUnitsX() == 0)
              Logger.log("new    color = " + newColor.r + "; " +  newColor.g + "; " +  newColor.b + "; " +  newColor.a);

            return newColor;
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

                float centerX = cell.getX() + Field.cellWidth/2;
                float centerY = cell.getY() + Field.cellHeight/2;

                float centerXto = toCell.getX() + Field.cellWidth/2;
                float centerYto = toCell.getY() + Field.cellHeight/2;

                renderer.line(centerX, centerY, centerXto, centerYto,
                        getColor(cell),
                        getColor(toCell));

                counter++;
            }
            allCounter++;
        }
    }

    private boolean isNeedToDraw(Cell cell) {
        float absoluteCellX = cell.getX() + current.getX();
        float absoluteCellY = cell.getY() + current.getY();
        return absoluteCellX > (-Field.cellWidth*GestureController.getZoom());
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
