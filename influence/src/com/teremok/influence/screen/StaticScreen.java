package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.XmlReader;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.ui.ColoredPanel;
import com.teremok.influence.ui.TexturePanel;
import com.teremok.influence.ui.UIElementParams;
import com.teremok.influence.util.Logger;
import com.teremok.influence.util.ResourceManager;
import com.teremok.influence.view.Animation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexx on 23.02.14
 */
public abstract class StaticScreen extends AbstractScreen {

    private String filename;
    private String atlasName;
    protected boolean loaded;

    protected Map<String, UIElementParams> uiElements;
    protected Image background;
    public ColoredPanel overlap;
    private Actor infoMessage;

    private static final String ATLAS_ATTR = "atlas";
    private static final String NAME_ATTR = "name";
    private static final String REGION_ATTR = "region";
    private static final String SECOND_REGION_ATTR = "secondRegion";
    private static final String LOCALE_ATTR = "localized";
    private static final String LOCALE_BACK_ATTR = "localizedBackground";
    private static final String X_ATTR = "x";
    private static final String Y_ATTR = "y";

    private static final String ROOT_NODE = "screen";
    private static final String ELEMENT_NODE = "element";

    private StaticScreen(Game game) {
        super(game);
    }

    public StaticScreen(Game game, String filename) {
        this(game);
        this.filename = filename;
        uiElements = new HashMap<String, UIElementParams>();
    }

    private void loadScreenCatchEx() {
        if (! loaded) {
            try {
                loadScreen();
                addActors();
                addListeners();
                addNonparsed();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadScreen() throws IOException {
        FileHandle handle = ResourceManager.getScreenUi(filename);
        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(handle.reader());

        loadAtlas(root);
        loadBackground(root);
        loadElements(root);

        loaded = true;
    }

    private void loadAtlas(XmlReader.Element root) {
       boolean localizedAtlas = Boolean.parseBoolean(root.getAttribute(LOCALE_ATTR));

        atlasName = root.getAttribute(ATLAS_ATTR);
        if (localizedAtlas) {
            atlas = ResourceManager.getAtlas(atlasName + "_" + Localizator.getLanguage());
        } else {
            atlas = ResourceManager.getAtlas(atlasName);
        }
    }

    private void loadBackground(XmlReader.Element root) {
        boolean localized = root.getBoolean(LOCALE_BACK_ATTR, false);
        TextureRegion textureRegion;
        if (localized) {
            textureRegion = atlas.findRegion("background_" + Localizator.getLanguage());
        } else {
            textureRegion = atlas.findRegion("background");
        }
        if (textureRegion != null) {
            background = new Image(new TextureRegionDrawable(textureRegion));
            background.setScaling(Scaling.fit);
            background.setAlign(Align.center);
            background.setTouchable(Touchable.disabled);

            stage.addActor(background);
        }
    }

    private void loadElements(XmlReader.Element root) {
        Array<XmlReader.Element> elements = root.getChildrenByName(ELEMENT_NODE);
        for (XmlReader.Element element : elements) {
            loadElement(element);
        }
        for (UIElementParams params : uiElements.values()) {
            Logger.log(params.toString());
        }
    }

    protected void addNonparsed() {
        for (UIElementParams params : uiElements.values()) {
            if (! params.parsed) {
                stage.addActor(new TexturePanel(params));
                Logger.log("add non parsed element as TexturePanel: " + params.name);
            }
        }
    }

    private void loadElement(XmlReader.Element element) {
        UIElementParams params = new UIElementParams();
        params.name = element.getAttribute(NAME_ATTR);

        String region = element.getAttribute(REGION_ATTR);
        String secondRegion = element.getAttribute(SECOND_REGION_ATTR, "");

        params.localized = Boolean.parseBoolean(element.getAttribute(LOCALE_ATTR));

        if (params.localized) {
            params.region = atlas.findRegion(region + "_" + Localizator.getLanguage());
            Logger.log("finding localized region: " + region + "_" + Localizator.getLanguage() + " : " + params.region);
            if (! secondRegion.isEmpty()) {
                params.region2 = atlas.findRegion(secondRegion + "_" + Localizator.getLanguage());
                Logger.log("finding localized region: " + secondRegion + "_" + Localizator.getLanguage() + " : " + params.region2);
            }
        } else {
            params.region = atlas.findRegion(region);
            Logger.log("finding region: " + region + " : " + params.region);
            if (! secondRegion.isEmpty()) {
                params.region2 = atlas.findRegion(secondRegion);
                Logger.log("finding region: " + secondRegion + " : " + params.region2);
            }
        }

        params.x = Float.parseFloat(element.getAttribute(X_ATTR));
        params.y = Float.parseFloat(element.getAttribute(Y_ATTR));
        uiElements.put(params.name, params);
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
        Logger.log("infoMessage alpha: " + infoMessage.getColor().a);
        infoMessage.addAction(Actions.fadeIn(Animation.DURATION_SHORT));
        Logger.log("infoMessage alpha after fadeIn: " + infoMessage.getColor().a);
    }

    protected void hideInfoMessageAnimation() {
        infoMessage.getColor().a = 1.0f;
        Logger.log("infoMessage alpha: " + infoMessage.getColor().a);
        infoMessage.addAction(
                Actions.sequence(
                        Actions.fadeOut(Animation.DURATION_SHORT),
                        new Action() {
                            @Override
                            public boolean act(float v) {
                                Logger.log("infoMessage alpha after fadeOut: " + infoMessage.getColor().a);
                                hideInfoMessage();
                                return true;
                            }
                        }
                )
        );

    }

    protected void showInfoMessage() {
        infoMessage.setVisible(true);
        infoMessage.setTouchable(Touchable.enabled);
    }

    protected void hideInfoMessage() {
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
        Logger.log("resize");
    }

    @Override
    public void show() {
        super.show();
        Logger.log("show - loading textures from " + filename);
        loadScreenCatchEx();
    }

    @Override
    public void hide() {
        //super.hide();
        Logger.log("hide");
    }

    @Override
    public void dispose() {
        super.dispose();
        loaded = false;
        //ResourceManager.disposeAtlas(atlasName);
    }

    // Auto-generated


    public String getAtlasName() {
        return atlasName;
    }
}

