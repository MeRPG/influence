package com.teremok.influence.view;

import android.util.Log;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;
import com.teremok.influence.util.Logger;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Alexx on 23.12.13
 */
public class FieldShapeDrawer extends AbstractDrawer<Field> {

    Set<Integer> routerDraw;
    int counter = 0;
    int allCounter = 0;

    @Override
    public void draw (Field field, SpriteBatch batch, float parentAlpha) {
        super.draw(field, batch, parentAlpha);
        batch.setColor(Color.WHITE);
        batch.end();

        drawShapeBackground();

        counter = 0;
        allCounter = 0;
        if (routerDraw == null){
            routerDraw = new HashSet<Integer>();
        } else {
            routerDraw.clear();
        }
        for (Cell c : current.getCells()) {
            drawCellRoutesShape(c);
        }
        Logger.log("routes: " + counter + "(" + allCounter + ")");

        batch.begin();
    }

    private void drawShapeBackground() {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Drawer.getBackgroundColor());
        renderer.rect(0, 0, current.getWidth(), current.getHeight());
        renderer.end();
    }

    private void drawCellRoutesShape(Cell cell) {
        for (Cell toCell : cell.getNeighborsList()) {
            float centerX = cell.getX() + cell.getWidth()/2;
            float centerY = cell.getY() + cell.getHeight()/2;

            float centerXto = toCell.getX() + toCell.getWidth()/2;
            float centerYto = toCell.getY() + toCell.getHeight()/2;

            Integer code = cell.getNumber() * 1000 + toCell.getNumber();

            if (! routerDraw.contains(code)) {
                routerDraw.add(code);
                Logger.log("add code" + code);
                code = toCell.getNumber() * 1000 + cell.getNumber();
                routerDraw.add(code);
                Logger.log("add code" + code);

                renderer.begin(ShapeRenderer.ShapeType.Line);
                renderer.line(centerX, centerY, centerXto, centerYto,
                        Drawer.getCellColorByNumber(cell.getType()),
                        Drawer.getCellColorByNumber(toCell.getType()));
                renderer.end();

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

    private static class MyInt {
        int i;
        private MyInt(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MyInt)) return false;

            MyInt myInt = (MyInt) o;

            if (i != myInt.i) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return i;
        }
    }
}
