package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.World;

import java.util.List;

/**
 * Created by Alexx on 11.12.13.
 */
public class WorldRenderer {

    private static final float CAMERA_WIDTH = 7f;
    private static final float CAMERA_HEIGHT = 10f;

    private World world;
    private OrthographicCamera cam;

    /** for debug rendering **/
    ShapeRenderer debugRenderer = new ShapeRenderer();

    /** Textures **/
    private Texture texture;
    private Sprite sprite;

    /** Animations **/

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
    private float ppuX;	// pixels per unit on the X axis
    private float ppuY;	// pixels per unit on the Y axis

    public void setSize (int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = (float)width / CAMERA_WIDTH;
        ppuY = (float)height / CAMERA_HEIGHT;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    public WorldRenderer(World world) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        cam.setToOrtho(true, CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    private void loadTextures() {
        //texture = new Texture(Gdx.files.internal("data/libgdx.png"));
        //sprite = new Sprite(texture);

        /* // Example!
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/textures.pack"));
        bobIdleLeft = atlas.findRegion("bob-01");
        bobIdleRight = new TextureRegion(bobIdleLeft);
        bobIdleRight.flip(true, false);
        blockTexture = atlas.findRegion("block");
        TextureRegion[] walkLeftFrames = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            walkLeftFrames[i] = atlas.findRegion("bob-0" + (i + 2));
        }
        walkLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkLeftFrames);
        TextureRegion[] walkRightFrames = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            walkRightFrames[i] = new TextureRegion(walkLeftFrames[i]);
            walkRightFrames[i].flip(true, false);
        }

        walkRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkRightFrames);
        */
    }

    public void render() {
        spriteBatch.begin();
        /* draw textures */
        // sprite.draw(spriteBatch);

        drawList();

        spriteBatch.end();
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
        debugRenderer.setColor(color);
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.circle(shiftX(getSqX(cell.getY()), cell.getX()), getSqY(cell.getX()), 0.4f);
        debugRenderer.end();
    }

    private void drawRoutes(int[][] matrix) {
        for (Cell c : world.getCells()) {
            if (c.isValid()) {
                drawCellRoutes(c, matrix);
            }
        }
    }

    private void drawCellRoutes(Cell cell, int[][] matrix) {
        for (int j = 0; j < 35; j++) {
            if (cell.isValid() && matrix[cell.getNumber()][j] == 1) {
                Cell toCell = world.getCells().get(j);
                if (toCell.isValid()) {
                    if (cell.getType() == toCell.getType()) {
                        debugRenderer.setColor(getCellColor(cell.getType()));
                    } else {
                        debugRenderer.setColor(Color.GRAY);
                    }
                    debugRenderer.begin(ShapeRenderer.ShapeType.Line);
                    debugRenderer.line(shiftX(getSqX(cell.getY()), cell.getX()), getSqY(cell.getX()),
                            shiftX(getSqX(toCell.getY()), toCell.getX()), getSqY(toCell.getX()));
                    debugRenderer.end();
                }
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
                color = Color.DARK_GRAY;
                break;
        }
        return color;
    }

    private void drawList() {
        debugRenderer.setColor(new Color(0.2f, 0.2f, 0.5f, 0.75f));
        Gdx.gl.glLineWidth(3);
        debugRenderer.setProjectionMatrix(cam.combined);

        drawRoutes(world.getMinimal());
        drawCells(world.getCells());

    }

    private void drawDebug() {
        debugRenderer.setColor(Color.LIGHT_GRAY);
        debugRenderer.setProjectionMatrix(cam.combined);

        drawCells(world.getCells());
        drawRoutes(world.getMatrix());

        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.rect(0, 0, World.WIDTH, World.HEIGHT);
        debugRenderer.end();
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
        return World.HEIGHT / World.MAX_CELLS_Y;
    }

    float getSqWidth() {
        return World.WIDTH / World.MAX_CELLS_X;
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
