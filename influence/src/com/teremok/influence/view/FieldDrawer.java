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
    TextureRegion maskRoute;
    TextureRegion background;

    public FieldDrawer() {
        atlas = new TextureAtlas(Gdx.files.internal("gameScreen.pack"));

        route = atlas.findRegion("route");
        route.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        maskRoute = atlas.findRegion("maskRoute");
        maskRoute.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        background = atlas.findRegion("background");
        background.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void draw (Field field, SpriteBatch batch, float parentAlpha) {
        super.draw(field, batch, parentAlpha);
        batch.setColor(Color.WHITE);
        //batch.draw(background, 0,0);

        drawShapeBackground(batch);

        for (Cell c : current.getCells()) {
            //drawCellRoutesTexture(c, batch);
            drawCellRoutesShape(batch, c);
        }
    }

    private void drawShapeBackground(SpriteBatch batch) {
        batch.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(Drawer.BACKGROUND_COLOR);
        renderer.rect(0,0, current.getWidth(), current.getHeight());

        renderer.end();
        batch.begin();
    }

    private void drawCellRoutesShape(SpriteBatch batch, Cell cell) {
        for (Cell toCell : current.getConnectedCells(cell)) {
            if (toCell.isValid()) {
                if (cell.getType() == toCell.getType()) {
                    batch.setColor(Drawer.getCellColorByType(cell.getType()));
                } else {
                    batch.setColor(Drawer.getCellColorByType(-1));
                }

                float centerX = cell.getX() + cell.getWidth()/2;
                float centerY = cell.getY() + cell.getHeight()/2;


                float centerXto = toCell.getX() + toCell.getWidth()/2;
                float centerYto = toCell.getY() + toCell.getHeight()/2;

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
                                    Drawer.getCellColorByType(cell.getType()),
                                    Drawer.getCellColorByType(toCell.getType())
                                    );
                        break;
                    case 4:
                        renderer.line(centerX, centerY, centerXto, centerYto,
                                Drawer.getCellColorByType(cell.getType()),
                                Drawer.getCellColorByType(toCell.getType())
                        );
                        break;
                    case 5:
                        renderer.line(centerX, centerY, centerXto, centerYto,
                                Drawer.getCellColorByType(cell.getType()),
                                Drawer.getCellColorByType(toCell.getType())
                        );
                        break;
                    case 6:
                        renderer.line(centerX, centerY, centerXto, centerYto,
                                Drawer.getCellColorByType(cell.getType()),
                                Drawer.getCellColorByType(toCell.getType())
                        );
                        break;
                    default:
                        break;
                }
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
                    batch.setColor(Drawer.getCellColorByType(-1));
                }
                float centerX = current.getX() + cell.getX() + cell.getWidth()/2 - 8;
                float centerY = current.getY() + cell.getY() + cell.getHeight()/2 + 6;

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
                        drawRoute(batch, centerX, centerY, rotation);
                        break;
                    case 4:
                        rotation = 300f;
                        drawRoute(batch, centerX, centerY, rotation);
                        break;
                    case 5:
                        if (cell.getUnitsX() % 2 == 1) {
                            rotation = 240f;
                        } else {
                            rotation = 300f;
                        }
                        drawRoute(batch, centerX, centerY, rotation);
                        break;
                    case 6:
                        rotation = 240f;
                        drawRoute(batch, centerX, centerY, rotation);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
   private void drawRoute(SpriteBatch batch, float centerX, float centerY, float rotation) {
       /*
       if (rotation != 180)
        rotation -= 5;
       */
       batch.draw(maskRoute, centerX, centerY, 0, 0,
               maskRoute.getRegionWidth(), maskRoute.getRegionHeight(),
                1, 1, rotation);

       batch.setColor(Color.WHITE);

       batch.draw(route, centerX, centerY, 0, 0,
               route.getRegionWidth(), route.getRegionHeight(),
               1, 1, rotation);
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
