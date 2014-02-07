package com.teremok.influence.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;

/**
 * Created by Alexx on 23.12.13
 */
public class FieldShapeDrawer extends AbstractDrawer<Field> {

    @Override
    public void draw (Field field, SpriteBatch batch, float parentAlpha) {
        super.draw(field, batch, parentAlpha);
        batch.setColor(Color.WHITE);

        drawShapeBackground(batch);

        for (Cell c : current.getCells()) {
            drawCellRoutesShape(batch, c);
        }
    }

    private void drawShapeBackground(SpriteBatch batch) {
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(Drawer.getBackgroundColor());
        renderer.rect(0,0, current.getWidth(), current.getHeight());

        renderer.end();
        batch.begin();
    }

    private void drawCellRoutesShape(SpriteBatch batch, Cell cell) {
        for (Cell toCell : current.getConnectedCells(cell)) {
            if (toCell.isValid()) {

                float centerX = cell.getX() + cell.getWidth()/2;
                float centerY = cell.getY() + cell.getHeight()/2;


                float centerXto = toCell.getX() + toCell.getWidth()/2;
                float centerYto = toCell.getY() + toCell.getHeight()/2;

                batch.end();
                renderer.begin(ShapeRenderer.ShapeType.Line);
                switch (cell.getNumber() - toCell.getNumber()) {
                    case 0:
                        break;
                    case -5:
                        break;
                    case -4:
                        break;
                    case -1:
                        break;
                    case 1:
                        renderer.line(centerX, centerY, centerXto, centerYto,
                                Drawer.getCellColorByNumber(cell.getType()),
                                Drawer.getCellColorByNumber(toCell.getType())
                        );
                        break;
                    case 4:
                        renderer.line(centerX, centerY, centerXto, centerYto,
                                Drawer.getCellColorByNumber(cell.getType()),
                                Drawer.getCellColorByNumber(toCell.getType())
                        );
                        break;
                    case 5:
                        renderer.line(centerX, centerY, centerXto, centerYto,
                                Drawer.getCellColorByNumber(cell.getType()),
                                Drawer.getCellColorByNumber(toCell.getType())
                        );
                        break;
                    case 6:
                        renderer.line(centerX, centerY, centerXto, centerYto,
                                Drawer.getCellColorByNumber(cell.getType()),
                                Drawer.getCellColorByNumber(toCell.getType())
                        );
                        break;
                    default:
                        break;
                }
                renderer.end();
                batch.begin();
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
