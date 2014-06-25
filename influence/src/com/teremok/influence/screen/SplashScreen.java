package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.influence.Influence;
import com.teremok.influence.util.ResourceManager;

/**
 * Created by Алексей on 26.06.2014
 */
public class SplashScreen extends AbstractScreen {

    ShapeRenderer renderer;
    ResourceManager resourceManager;

    public SplashScreen(Influence game) {
        super(game);
        resourceManager = game.getResourceManager();
        renderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        super.render(delta);


        Gdx.app.debug(getClass().getSimpleName(), "Loading resources: " + resourceManager.getPercentsProgress());
        if (resourceManager.update()){
            //TODO читерство
            game.getFontFactory().load();
            Gdx.app.debug(getClass().getSimpleName(), "Loading resources: Done!");
            game.getScreenController().showStartScreen();
        }

        renderer.setTransformMatrix(getBatch().getTransformMatrix());
        renderer.setProjectionMatrix(getBatch().getProjectionMatrix());

        renderer.setColor(Color.WHITE);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(180, 347, 120, 26);
        renderer.end();
        int border = 3;
        float progress = resourceManager.getAssetManager().getProgress();
        renderer.setColor(Color.WHITE);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(180+border,347+border, (120-border*2)*progress, 26-border*2);
        renderer.end();



    }
}
