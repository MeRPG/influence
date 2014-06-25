package com.teremok.influence.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.XmlReader;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.util.Logger;
import com.teremok.influence.util.ResourceManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 11.06.2014
 */
public class UIElementsXMLLoader {

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

    private String atlasName;
    private TextureAtlas atlas;
    private Map<String, UIElementParams> uiElementsParams;
    private Image background;

    private ResourceManager resourceManager;

    public UIElementsXMLLoader(ResourceManager resourceManager, String filename) throws IOException {

        this.resourceManager = resourceManager;

        uiElementsParams = new HashMap<>();
        FileHandle handle = resourceManager.getScreenUi(filename);
        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(handle.reader());

        loadAtlas(root);
        loadBackground(root);
        loadElements(root);
    }

    private void loadAtlas(XmlReader.Element root) {
        boolean localizedAtlas = Boolean.parseBoolean(root.getAttribute(LOCALE_ATTR));

        atlasName = root.getAttribute(ATLAS_ATTR);
        if (localizedAtlas) {
            atlas = resourceManager.getAtlas(atlasName + "_" + Localizator.getLanguage());
        } else {
            atlas = resourceManager.getAtlas(atlasName);
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

        }
    }

    private void loadElements(XmlReader.Element root) {
        Array<XmlReader.Element> elements = root.getChildrenByName(ELEMENT_NODE);
        for (XmlReader.Element element : elements) {
            loadElement(element);
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
        uiElementsParams.put(params.name, params);
    }

    // Auto-generated


    public String getAtlasName() {
        return atlasName;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public Map<String, UIElementParams> getUiElementsParams() {
        return uiElementsParams;
    }

    public Image getBackground() {
        return background;
    }
}
