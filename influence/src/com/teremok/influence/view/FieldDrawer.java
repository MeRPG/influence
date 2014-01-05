package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Field;

/**
 * Created by Alexx on 23.12.13
 */
public class FieldDrawer extends AbstractDrawer<Field> {

    TextureAtlas atlas;
    TextureRegion route;
    TextureRegion background;

    public FieldDrawer() {
        atlas = new TextureAtlas(Gdx.files.internal("gameScreen.pack"));

        route = atlas.findRegion("route");
        route.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        background = atlas.findRegion("background");
        background.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void draw (Field field, SpriteBatch batch, float parentAlpha) {
        super.draw(field, batch, parentAlpha);
        batch.setColor(Color.WHITE);
        batch.draw(background, 0,0);

        for (Cell c : current.getCells()) {
            drawCellRoutesTexture(c, batch);
        }
    }

    private void drawCellRoutes(Cell cell) {
        for (Cell toCell : current.getConnectedCells(cell)) {
            if (toCell.isValid()) {
                if (cell.getType() == toCell.getType()) {
                    renderer.setColor(Drawer.getCellColorByType(cell.getType()));
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

    private void drawCellRoutesTexture(Cell cell, SpriteBatch batch) {
        for (Cell toCell : current.getConnectedCells(cell)) {
            if (toCell.isValid()) {
                if (cell.getType() == toCell.getType()) {
                    batch.setColor(Drawer.getCellColorByType(cell.getType()));
                } else {
                    batch.setColor(Color.GRAY);
                }
                float centerX = current.getX() + cell.getX() + cell.getWidth()/2;
                float centerY = current.getY() + cell.getY() + cell.getHeight()/2;

                float rotation;

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
                        rotation = 180f;
                        batch.draw(route, centerX, centerY, 0, 0, 96, 5, 1, 1, rotation);
                        break;
                    case 4:
                        rotation = 300f;
                        batch.draw(route, centerX, centerY, 0, 0, 96, 5, 1, 1, rotation);
                        break;
                    case 5:
                        if (cell.getUnitsX() % 2 == 1) {
                            rotation = 240f;
                        } else {
                            rotation = 300f;
                        }
                        batch.draw(route, centerX, centerY, 0, 0, 96, 5, 1, 1, rotation);
                        break;
                    case 6:
                        rotation = 240f;
                        batch.draw(route, centerX, centerY, 0, 0, 96, 5, 1, 1, rotation);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void drawBoundingBox() {

        Cell selected = current.getSelectedCell();

        if (selected != null && selected.isValid()) {
            renderer.setColor(Drawer.getCellColorByType(selected.getType()));
        } else {
            renderer.setColor(Color.WHITE);
        }

        super.drawBoundingBox();
    }
}
