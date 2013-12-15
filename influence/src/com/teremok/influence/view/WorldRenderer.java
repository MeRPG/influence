package com.teremok.influence.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.model.World;

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

        spriteBatch.end();
        if (debug)
            drawDebug();
    }

    private void drawDebug() {
        debugRenderer.setColor(Color.GREEN);
        debugRenderer.setProjectionMatrix(cam.combined);

        int[][] mask = world.getCells();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                if (mask[i][j] != Integer.MAX_VALUE && mask[i][j] > 0) {
                    drawCell(i, j, mask);

                }
            }
        }
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.rect(0, 0, world.WIDTH, world.HEIGHT);
        debugRenderer.end();
    }

    float getSqX(int i) {
        float sqWidth = getSqWidth();
        float sqX = (i+1) * sqWidth - sqWidth/2;
        return sqX;
    }

    float getSqY(int j) {
        float sqHeight = getSqHeight();
        float  sqY = (j+1) * sqHeight - sqHeight/2;
        return sqY;
    }

    float getSqHeight() {
        return world.HEIGHT / 5;
    }

    float getSqWidth() {
        return world.WIDTH / 7;
    }


    private void drawCell(int i, int j, int[][] mask) {

        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.circle(getSqX(i), shiftY(getSqY(j), i), 0.4f);
        debugRenderer.end();

        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int offI = -1; offI < 2; offI++) {
            for (int offJ = -1; offJ < 2; offJ++) {
                if (
                        (
                                (offI != -1 && offJ != 1 || offI != 1 && offJ != 1)
                                && i%2==0
                        )
                        ||
                        (
                                (offI != -1 && offJ != -1 || offI != 1 && offJ != -1)
                                && i%2==1
                        )
                    )
                    drawConnection(i, j, offI, offJ, mask);
            }
        }
        debugRenderer.end();


    }

    private void drawConnection(int i, int j, int offI, int offJ, int mask[][]) {
        if (hasHeigboor(i+offI, j+offJ, mask)) {
            debugRenderer.line(getSqX(i), shiftY(getSqY(j), i), getSqX(i+offI), shiftY(getSqY(j + offJ), i+offI));
        }
    }

    float shiftY(float sqY, int i) {
        if (i%2 == 1) {
            sqY += getSqHeight() / 2;
        }
        return sqY;
    }


    private boolean hasHeigboor(int i, int j, int[][] mask) {
        if (i < 0 || j < 0)
            return false;
        if (i >= 7 || j >= 5)
            return false;
        return mask[i][j] > 0 && mask[i][j] < Integer.MAX_VALUE;
    }

}
