package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.model.World;
import com.teremok.influence.util.RenderHelper;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen extends AbstractScreen {

    World world;

    public GameScreen(Game game) {
        super(game);

        world = new World();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(RenderHelper.SCREEN_WIDTH, RenderHelper.SCREEN_HEIGHT, true);
// и выравниваем камеру по центру
        stage.getCamera().translate(-stage.getGutterWidth(), -stage.getGutterHeight(), 0);

        world.setTouchable(Touchable.childrenOnly);
        stage.addActor(world);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
