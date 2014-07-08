package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.teremok.framework.screen.AbstractScreen;
import com.teremok.framework.util.ResourceManager;
import com.teremok.influence.Influence;

import static com.teremok.influence.screen.InfluenceScreenController.*;


/**
 * Created by Алексей on 26.06.2014
 */
public class SplashScreen extends AbstractScreen {

    Influence game;
    ShapeRenderer renderer;
    ResourceManager resourceManager;
    Color color = new Color(0x35b7deff);

    public SplashScreen(Influence game) {
        this.game = game;
        resourceManager = game.getResourceManager();
        renderer = new ShapeRenderer();
        renderer.setColor(color);
    }

    @Override
    public void render(float delta) {
        super.render(delta);


        Gdx.app.debug(getClass().getSimpleName(), "Loading resources: " + resourceManager.getPercentsProgress());
        if (resourceManager.update()){
            Gdx.app.debug(getClass().getSimpleName(), "Loading resources: Done!");
            game.getScreenController().setScreen(START_SCREEN);
        }

        Gdx.gl.glLineWidth(2);

        renderer.setTransformMatrix(getBatch().getTransformMatrix());
        renderer.setProjectionMatrix(getBatch().getProjectionMatrix());

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(122, 347, 236, 27);
        renderer.end();
        int border = 5;
        float progress = resourceManager.getAssetManager().getProgress();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(122+border,347+border, (236-border*2)*progress, 27-border*2);
        renderer.end();


        Gdx.gl.glLineWidth(1);

    }
}
