package com.teremok.influence.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL10;
import com.teremok.influence.controller.WorldController;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.World;
import com.teremok.influence.view.Renderer;
import com.teremok.influence.view.WorldRenderer;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen implements Screen, InputProcessor {

    private Game game;
    private World world;
    private WorldRenderer renderer;
    private WorldController controller;

    private int width, height;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        controller.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void show() {
        world = new World();
        renderer = new WorldRenderer(world);
        controller = new WorldController(world);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.D)
            renderer.setDebug(!renderer.isDebug());
        if (keycode == Input.Keys.R)
            world.regenerate();
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (renderer.isDebug()) {
            touchUpDebug(screenX, screenY, pointer, button);
        } else {
            touchUpNormal(screenX, screenY, pointer, button);
        }
        return false;
    }

    private void touchUpDebug(int screenX, int screenY, int pointer, int button) {
        if (screenY < height/2 && screenX < width/2)
            renderer.setDebug(!renderer.isDebug());
        if (screenY < height/2 && screenX > width/2)
            world.regenerate();
        if (screenY > height/2 && screenX < width/2) {
            if (world.getP() < 10) {
                world.setP(world.getP()+1);
            }
            world.updateMinimal();
        }
        if (screenY > height/2 && screenX > width/2) {
            if (world.getP() > 0) {
                world.setP(world.getP()-1);
            }
            world.updateMinimal();
        }
    }

    private void touchUpNormal(int screenX, int screenY, int pointer, int button) {

        float realToVirtualRatioW = Renderer.SCREEN_WIDTH /width;
        float realToVirtualRatioH = Renderer.SCREEN_HEIGHT / height;

        double translatedX = (screenX * realToVirtualRatioH);
        double translatedY = (screenY * realToVirtualRatioW);

        if (translatedY < Renderer.FIELD_HEIGHT) {

            double unit_size_w = (Renderer.FIELD_WIDTH/5);
            double unit_size_h = (Renderer.FIELD_HEIGHT/7);

            double dblY = (translatedY / (unit_size_h));
            double dblX = (int)dblY % 2 == 1 ? ((translatedX-unit_size_w/2)/(unit_size_w)) : (translatedX/(unit_size_w));

            int x = (int) Math.floor(dblX);
            int y = (int) Math.floor(dblY);

            Cell cell = world.getCells().get(Cell.calcNumber(x, y));

            if (cell.getNumber() != -1 && x >= 0 && y >= 0)
                world.setSelectedCell(cell);
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}