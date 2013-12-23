package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.model.Field;
import com.teremok.influence.view.CellDrawer;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen extends AbstractScreen {

    Field field;

    public GameScreen(Game game) {
        super(game);

        field = new Field();

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);

        field.setTouchable(Touchable.childrenOnly);
        CellDrawer.setBitmapFont(getFont());
        field.updateMinimal();
        stage.addActor(field);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
