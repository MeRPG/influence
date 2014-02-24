package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.teremok.influence.ui.UIElementParams;
import com.teremok.influence.util.Logger;
import com.teremok.influence.util.ResourseManager;
import com.teremok.influence.view.Animation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexx on 23.02.14
 */
public class StaticScreen extends AbstractScreen {

    private String filename;
    protected Map<String, UIElementParams> uiElements;
    protected Image background;
    public ColoredPanel overlap;

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
        try {
            loadScreen();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadScreen() throws IOException {
        FileHandle handle = ResourseManager.getScreenUi(filename);
        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(handle.reader());

        loadAtlas(root);
        loadBackground(root);
        loadElements(root);
    }

    private void loadAtlas(XmlReader.Element root) {
        String atlasName;

        boolean localizedAtlas = Boolean.parseBoolean(root.getAttribute(LOCALE_ATTR));

        atlasName = root.getAttribute(ATLAS_ATTR);
        if (localizedAtlas) {
            atlas = ResourseManager.getAtlas(atlasName + "_" + Localizator.getLanguage());
        } else {
            atlas = ResourseManager.getAtlas(atlasName);
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
        background = new Image(new TextureRegionDrawable(textureRegion));
        background.setScaling(Scaling.fit);
        background.setAlign(Align.center);
        background.setTouchable(Touchable.disabled);

        stage.addActor(background);
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

    protected void initOverlap() {
        overlap = new ColoredPanel(Color.BLACK, 0, 0, WIDTH, HEIGHT);
        overlap.setTouchable(Touchable.disabled);
        overlap.addAction(Actions.alpha(0f, Animation.DURATION_NORMAL));
        stage.addActor(overlap);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
    }
}

