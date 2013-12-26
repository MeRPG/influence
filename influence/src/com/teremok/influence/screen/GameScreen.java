package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teremok.influence.model.player.*;
import com.teremok.influence.model.*;
import com.teremok.influence.view.CellDrawer;
import com.teremok.influence.view.ScoreDrawer;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen extends AbstractScreen {

    public static enum TurnPhase {
        ATTACK,
        DISTRIBUTE
    };

    Field field;
    Score score;
    public static TurnPhase currentPhase;

    public GameScreen(Game game) {
        super(game);
        field = new Field();
        score = new Score(field);

        for (int i = 0; i < 5; i ++) {
            Player.addPlayer(new HumanPlayer(i), i);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (currentPhase == TurnPhase.DISTRIBUTE && Player.current().getPowerToDistribute() == 0) {
            Player.next();
            currentPhase = TurnPhase.ATTACK;
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);

        currentPhase = TurnPhase.ATTACK;

        CellDrawer.setBitmapFont(getFont());
        ScoreDrawer.setBitmapFont(getFont());

        field.setTouchable(Touchable.childrenOnly);
        field.updateMinimal();

        stage.addActor(field);
        stage.addActor(score);

        stage.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) instanceof Score;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled()) {
                    switch (currentPhase) {
                        case ATTACK:
                            Player.current().setPowerToDistribute(25);
                            currentPhase = TurnPhase.DISTRIBUTE;
                            System.out.println("Distribute power phase.");
                            field.resetSelection();
                            break;
                        case DISTRIBUTE:
                            Player.next();
                            currentPhase = TurnPhase.ATTACK;
                            break;
                        default:
                            ;
                    }
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.R) {
                    field.regenerate();
                }
                return true;
            }

        });
    }
}
