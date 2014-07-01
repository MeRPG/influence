package com.teremok.influence.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.teremok.influence.Influence;
import com.teremok.influence.ui.*;
import com.teremok.influence.util.FontFactory;
import com.teremok.influence.view.Animation;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Alexx on 23.02.14
 */
public abstract class StaticScreen extends AbstractScreen {

    private String filename;
    private String atlasName;
    protected boolean loaded;
    protected boolean keepInMemory;

    protected ScreenController screenController;

    protected Map<String, UIElementParams> uiElements;
    protected Image background;
    public ColoredPanel overlap;
    private Actor infoMessage;

    Label fps;

    private StaticScreen(Influence game) {
        super(game);
        this.screenController = game.getScreenController();
    }

    public StaticScreen(Influence game, String filename) {
        this(game);
        this.filename = filename;
    }

    private void loadScreenCatchEx() {
        if (! loaded) {
            try {
                loadScreen();
                addActors();
                if (game.getSettings().debug) {
                    addFps();
                }
                addListeners();
                addNonparsed();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addFps() {
        fps = new Label("fps: \t" + Gdx.graphics.getFramesPerSecond(), fontFactory.getFont(FontFactory.DEBUG_FONT),
                Color.RED.cpy(), WIDTH - 90f, HEIGHT-20f, false);
        stage.addActor(fps);
    }

    private void updateFps() {
        fps.setCode("fps: \t" + Gdx.graphics.getFramesPerSecond());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (game.getSettings().debug) {
            updateFps();
        }
    }

    private void loadScreen() throws IOException {

        UIElementsXMLLoader loader = new UIElementsXMLLoader(game.getResourceManager(), filename);

        atlasName = loader.getAtlasName();
        atlas = loader.getAtlas();

        background = loader.getBackground();
        if (background != null)
            stage.addActor(background);

        uiElements = loader.getUiElementsParams();

        loaded = true;
    }

    protected void addNonparsed() {
        for (UIElementParams params : uiElements.values()) {
            if (! params.parsed) {
                stage.addActor(new TexturePanel(params));
                Gdx.app.debug(getClass().getSimpleName(), "add non parsed element as TexturePanel: " + params.name);
            }
        }
    }


    protected void initOverlap(boolean transparent) {

        Color color;

        if (transparent) {
            color = new Color(0x00000000);
        } else {
            color = new Color(0x000000FF);
        }

        if (overlap == null) {
            overlap = new ColoredPanel(color, 0, 0, WIDTH, HEIGHT);
            overlap.setTouchable(Touchable.disabled);
            stage.addActor(overlap);
        } else {
            overlap.setColor(color);
            stage.getRoot().removeActor(overlap);
            stage.addActor(overlap);
        }
    }

    protected void fadeOutOverlap() {
        if (overlap == null) {
            initOverlap(false);
        }
        overlap.addAction(
            Actions.fadeOut(Animation.DURATION_NORMAL)
        );
    }

    protected void showInfoMessage(TextureRegion textureRegion, float x, float y) {
        addInfoMessage(textureRegion, x, y);
        showInfoMessage();
    }

    protected void addInfoMessage(TextureRegion textureRegion, float x, float y) {
        addInfoMessage(new TexturePanel(textureRegion, x, y));
    }

    protected void addInfoMessage(Actor infoMessage) {
        this.infoMessage = infoMessage;
        stage.addActor(infoMessage);
        hideInfoMessage();
    }

    protected void showInfoMessageAnimation() {
        infoMessage.getColor().a = 0.0f;
        showInfoMessage();
        Gdx.app.debug(getClass().getSimpleName(), "infoMessage alpha: " + infoMessage.getColor().a);
        infoMessage.addAction(Actions.fadeIn(Animation.DURATION_SHORT));
        Gdx.app.debug(getClass().getSimpleName(), "infoMessage alpha after fadeIn: " + infoMessage.getColor().a);
    }

    protected void hideInfoMessageAnimation() {
        infoMessage.getColor().a = 1.0f;
        Gdx.app.debug(getClass().getSimpleName(), "infoMessage alpha: " + infoMessage.getColor().a);
        infoMessage.addAction(
                Actions.sequence(
                        Actions.fadeOut(Animation.DURATION_SHORT),
                        new Action() {
                            @Override
                            public boolean act(float v) {
                                Gdx.app.debug(getClass().getSimpleName(), "infoMessage alpha after fadeOut: " + infoMessage.getColor().a);
                                hideInfoMessage();
                                return true;
                            }
                        }
                )
        );

    }

    private void showInfoMessage() {
        infoMessage.setVisible(true);
        infoMessage.setTouchable(Touchable.enabled);
    }

    private void hideInfoMessage() {
        infoMessage.setVisible(false);
        infoMessage.setTouchable(Touchable.disabled);
    }

    protected boolean isInfoMessageVisible() {
        return infoMessage != null && infoMessage.isVisible();
    }

    protected abstract void addActors();
    protected abstract void addListeners();

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        addNonparsed();
        fadeOutOverlap();
        Gdx.app.debug(getClass().getSimpleName(), "resize");
    }

    @Override
    public void show() {
        super.show();
        Gdx.app.debug(getClass().getSimpleName(), "show - loading textures from " + filename);
        loadScreenCatchEx();
    }

    @Override
    public void hide() {
        super.hide();
        dispose();
        Gdx.app.debug(getClass().getSimpleName(), "hide");
    }

    @Override
    public void dispose() {
        super.dispose();
        if (! keepInMemory) {
            game.getResourceManager().disposeAtlas(atlasName);
        }
        Gdx.app.debug(this.getClass().getSimpleName(), "dispose called");
        loaded = false;
    }

    // Auto-generated

    public String getAtlasName() {
        return atlasName;
    }

    public boolean isKeepInMemory() {
        return keepInMemory;
    }

    public void setKeepInMemory(boolean keepInMemory) {
        this.keepInMemory = keepInMemory;
    }
}

