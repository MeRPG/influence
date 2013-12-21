package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.World;

import java.util.List;

/**
 * Created by Alexx on 11.12.13.
 */
public class WorldRenderer  extends Renderer {

    private static final float STANDARD_SIZE_MULTIPLIER = 0.4f;
    private static final float BIG_SIZE_MULTIPLIER = 0.6f;

    private World world;

    /** Textures **/
    private Texture texture;
    private Sprite sprite;

    public WorldRenderer(World world) {
        super();
        this.world = world;
    }

    @Override
    protected void loadTextures() {
        //texture = new Texture(Gdx.files.internal("data/libgdx.png"));
        //sprite = new Sprite(texture);
    }

    @Override
    public void render() {
        prerender();

        spriteBatch.begin();
        /* draw textures */
        // sprite.draw(spriteBatch);
        spriteBatch.end();
        //drawTouchFields();
        drawList();


        if (debug)
            drawDebug();
    }

    private void drawCells(List<Cell> cells) {
        for (Cell c : cells) {
            if (c.isValid()) {
                drawCell(c);
            }
        }
    }

    private void drawCell(Cell cell) {
        Color color = getCellColor(cell.getType());
        renderer.setColor(color);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        if (cell.isBig()) {
            renderCellWithMultiplier(cell, BIG_SIZE_MULTIPLIER);
        } else {
            renderCellWithMultiplier(cell, STANDARD_SIZE_MULTIPLIER);
        }

        if (cell.isSelected())
            renderer.setColor(Color.WHITE);
            renderer.circle(shiftX(getSqX(cell.getY()), cell.getX()), getSqY(cell.getX()), UNIT_SIZE * 0.1f);
        renderer.end();
    }

    private void renderCellWithMultiplier(Cell cell, float multiplier) {
        float centerX = shiftX(getSqX(cell.getY()), cell.getX());
        float centerY = getSqY(cell.getX());

        renderer.circle(centerX, centerY, UNIT_SIZE * multiplier, 6);

        renderer.setColor(Color.BLACK);

        float powerArc = (float)cell.getPower() / cell.getMaxPower()*360f;
        renderer.arc(centerX, centerY, UNIT_SIZE * multiplier * .7f, 270f, powerArc);

        renderer.setColor(getCellColor(cell.getType()));
        renderer.circle(centerX, centerY, UNIT_SIZE * multiplier * .6f);

    }

    private void drawRoutes(int[][] matrix) {
        Gdx.gl.glLineWidth(3);
        for (Cell c : world.getCells()) {
            if (c.isValid()) {
                drawCellRoutes(c, matrix);
            }
        }
        Gdx.gl.glLineWidth(1);
    }

    private void drawCellRoutes(Cell cell, int[][] matrix) {
        for (int j = 0; j < 35; j++) {
            if (cell.isValid() && matrix[cell.getNumber()][j] == 1) {
                Cell toCell = world.getCells().get(j);
                if (toCell.isValid()) {
                    if (cell.getType() == toCell.getType()) {
                        renderer.setColor(getCellColor(cell.getType()));
                    } else {
                        renderer.setColor(Color.GRAY);
                    }
                    renderer.begin(ShapeRenderer.ShapeType.Line);
                    renderer.line(shiftX(getSqX(cell.getY()), cell.getX()), getSqY(cell.getX()),
                            shiftX(getSqX(toCell.getY()), toCell.getX()), getSqY(toCell.getX()));
                    renderer.end();
                }
            }
        }
    }

    private void drawTouchFields() {
        int unit_size_w = (int)FIELD_WIDTH/5;
        int unit_size_h = (int)FIELD_HEIGHT/7;

        if (world.getSelectedCell() != null) {
            renderer.setColor(new Color(.85f,.85f,.17f,.04f));
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.rect(0, world.getSelectedCell().getX()*unit_size_h, FIELD_WIDTH, unit_size_h);
            renderer.end();

            renderer.setColor(new Color(.95f,.75f,.17f,.04f));
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.rect(world.getSelectedCell().getY()*unit_size_h, 0,  unit_size_w, FIELD_HEIGHT);
            renderer.end();
        }

        for (int i = 0; i < 5; i++ ) {
            for (int j = 0; j < 7; j++) {
                renderer.setColor(Color.WHITE);
                renderer.begin(ShapeRenderer.ShapeType.Line);
                if (j%2 == 1) {
                    renderer.rect(i*unit_size_w+unit_size_w/2, j*unit_size_h, unit_size_w, unit_size_h );
                } else {
                    renderer.rect(i*unit_size_w, j*unit_size_h, unit_size_w, unit_size_h );
                }
                renderer.end();
            }
        }
    }

    private Color getCellColor(int type) {
        Color color;
        switch (type) {
            case 0:
                color = Color.CYAN;
                break;
            case 1:
                color = Color.LIGHT_GRAY;
                break;
            case 2:
                color = Color.ORANGE;
                break;
            case 3:
                color = Color.PINK;
                break;
            case 4:
                color = Color.MAGENTA;
                break;
            default:
                color = Color.GRAY;
                break;
        }
        return color;
    }

    private void drawList() {
        renderer.setColor(new Color(0.2f, 0.2f, 0.5f, 0.75f));

        drawRoutes(world.getMinimal());
        drawCells(world.getCells());

        renderer.setColor(Color.GRAY);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(0, 0, FIELD_WIDTH-1, FIELD_HEIGHT-1);
        renderer.end();

    }

    @Override
    protected void drawDebug() {
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.setProjectionMatrix(cam.combined);

    }

    float getSqX(int i) {
        float sqWidth = getSqWidth();
        return (i+1) * sqWidth - sqWidth/2;
    }

    float getSqY(int j) {
        float sqHeight = getSqHeight();
        return (j+1) * sqHeight - sqHeight/2;
    }

    float getSqHeight() {
        return FIELD_HEIGHT / World.MAX_CELLS_Y;
    }

    float getSqWidth() {
        return FIELD_WIDTH / World.MAX_CELLS_X;
    }

    float shiftY(float sqY, int i) {
        if (i%2 == 1) {
            sqY += getSqHeight() / 2;
        }
        return sqY;
    }

    float shiftX(float sqX, int j) {
        if (j%2 == 1) {
            sqX += getSqWidth() / 2;
        }
        return sqX;
    }
}
