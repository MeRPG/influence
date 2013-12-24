package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
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
    public void resize(int width, int height) {
        super.resize(width,height);

        CellDrawer.setBitmapFont(getFont());

        field.setTouchable(Touchable.childrenOnly);
        field.updateMinimal();
        stage.addActor(field);
    }
}
