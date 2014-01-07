package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teremok.influence.model.GameType;
import com.teremok.influence.model.Match;
import com.teremok.influence.model.Score;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.ui.TooltipHandler;
import com.teremok.influence.view.Drawer;

/**
 * Created by Alexx on 11.12.13
 */
public class GameScreen extends AbstractScreen {

    Match match;

    public GameScreen(Game game, GameType gameType) {
        super(game);
        match = new Match(gameType);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        match.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width,height);

        AbstractDrawer.setBitmapFont(getFont());

        stage.addActor(match.getField());

        stage.addActor(match.getScore());

        stage.addActor(TooltipHandler.getInstance());

        stage.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return stage.hit(x,y,true) instanceof Score;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled() && match.isHumanActing()) {
                    match.switchPhase();
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && keycode == Input.Keys.R) {
                    System.out.println("Starting new match");
                    match = new Match(match.getGameType());
                    updateMatchDependentActors();
                }
                return true;
            }

        });
    }

    private void updateMatchDependentActors() {
        stage.addActor(match.getField());
        stage.addActor(match.getScore());
    }

    @Override
    public void pause() {
        super.pause();
        Drawer.dispose();
    }
}
