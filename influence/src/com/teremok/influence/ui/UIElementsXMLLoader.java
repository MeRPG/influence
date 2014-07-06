package com.teremok.influence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.XmlReader;
import com.teremok.influence.Influence;
import com.teremok.influence.util.FontFactory;
import com.teremok.influence.util.Localizator;
import com.teremok.influence.util.ResourceManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    private static final String DEFAULT_BACK_ATTR = "defaultBackground";
    private static final String X_ATTR = "x";
    private static final String Y_ATTR = "y";
    private static final String FONT_ATTR = "font";
    private static final String COLOR_ATTR = "color";
    private static final String ALIGN_ATTR = "align";
    private static final String ALPHA_ATTR = "alpha";

    private static final String ROOT_NODE = "screen";
    private static final String ELEMENT_NODE = "element";
    private static final String LABEL_NODE = "label";

    private String atlasName;
    private TextureAtlas atlas;
    private TextureAtlas backgroundAtlas;
    private Map<String, UIElementParams> uiElementsParams;
    private Image background;
    private List<Label> labels;

    private FontFactory fontFactory;
    private ResourceManager resourceManager;

    public UIElementsXMLLoader(Influence game, String filename) throws IOException {
        fontFactory = new FontFactory(game);
        this.resourceManager = game.getResourceManager();
        this.backgroundAtlas = resourceManager.getAtlas("background");
        uiElementsParams = new HashMap<>();
        labels = new LinkedList<>();
        FileHandle handle = resourceManager.getScreenUi(filename);
        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(handle.reader());

        loadAtlas(root);
        loadBackground(root);
        loadElements(root);
        loadLabels(root);
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
        boolean defaultBackground = root.getBoolean(DEFAULT_BACK_ATTR, false);
        TextureRegion textureRegion;
        if (defaultBackground) {
            textureRegion = backgroundAtlas.findRegion("background");
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
            Gdx.app.debug(getClass().getSimpleName(), "finding localized region: " + region + "_" + Localizator.getLanguage() + " : " + params.region);
            if (! secondRegion.isEmpty()) {
                params.region2 = atlas.findRegion(secondRegion + "_" + Localizator.getLanguage());
                Gdx.app.debug(getClass().getSimpleName(), "finding localized region: " + secondRegion + "_" + Localizator.getLanguage() + " : " + params.region2);
            }
        } else {
            params.region = atlas.findRegion(region);
            Gdx.app.debug(getClass().getSimpleName(), "finding region: " + region + " : " + params.region);
            if (! secondRegion.isEmpty()) {
                params.region2 = atlas.findRegion(secondRegion);
                Gdx.app.debug(getClass().getSimpleName(), "finding region: " + secondRegion + " : " + params.region2);
            }
        }

        params.x = Float.parseFloat(element.getAttribute(X_ATTR));
        params.y = Float.parseFloat(element.getAttribute(Y_ATTR));
        uiElementsParams.put(params.name, params);
    }

    private void loadLabels(XmlReader.Element root) {
        Array<XmlReader.Element> labelElements = root.getChildrenByName(LABEL_NODE);
        for (XmlReader.Element labelElement : labelElements) {
            loadLabel(labelElement);
        }
    }

    private void loadLabel(XmlReader.Element element) {

        String string = element.getText();
        BitmapFont font = fontFactory.getFont(element.getAttribute(FONT_ATTR, FontFactory.STATUS));
        Color color = Color.valueOf(element.getAttribute(COLOR_ATTR, "FFFFFF"));

        Gdx.app.debug(getClass().getSimpleName(), "Color parsed, r: " + color.r + " g: " + color.g + " b: " + color.b);
        color.a = element.getFloat(ALPHA_ATTR, 1.0f);
        Gdx.app.debug(getClass().getSimpleName(), "Color alpha: " + color.a);
        float x = element.getFloatAttribute(X_ATTR, 0);
        float y = element.getFloatAttribute(Y_ATTR, 0);
        boolean localized = element.getBooleanAttribute(LOCALE_ATTR, false);
        Label.Align align = Label.Align.valueOf(element.getAttribute(ALIGN_ATTR, "LEFT"));

        Label label = new Label(string, font, color, x, y, localized, align);

        labels.add(label);
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

    public List<Label> getLabels() {
        return labels;
    }
}
