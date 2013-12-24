package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.model.Field;
import com.teremok.influence.model.Score;
import com.teremok.influence.view.CellDrawer;
import com.teremok.influence.view.ScoreDrawer;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen extends AbstractScreen {

    Field field;
    Score score;

    public GameScreen(Game game) {
        super(game);
        field = new Field();
        score = new Score(field);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);

        CellDrawer.setBitmapFont(getFont());
        ScoreDrawer.setBitmapFont(getFont());

        field.setTouchable(Touchable.childrenOnly);
        field.updateMinimal();
        stage.addActor(field);
        stage.addActor(score);
    }
}
