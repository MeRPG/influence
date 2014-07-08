package com.teremok.framework.ui.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.teremok.framework.TeremokGame;
import com.teremok.framework.util.ResourceManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Алексей on 08.07.2014
 */
public class FontsLoader {

    private static final String DEFAULT_FONT_FILE = "font/fonts.xml";

    private static final String FONT_NODE = "font";

    private static final String NAME_ATTR = "name";
    private static final String SCALEXY_ATTR = "scalexy";
    private static final String SCALEX_ATTR = "scalex";
    private static final String SCALEY_ATTR = "scaley";
    private static final String BITMAP_ATTR = "bitmap";

    private String filePath;
    private TeremokGame game;
    private ResourceManager resourceManager;
    private Map<String, FontInfo> fonts;

    public FontsLoader(TeremokGame game) {
        this.game = game;
        this.filePath = DEFAULT_FONT_FILE;
        this.resourceManager = game.getResourceManager();
        fonts = new HashMap<>();
    }

    public FontsLoader(TeremokGame game, String filePath) {
        this(game);
        this.filePath = filePath;
    }

    public void load() throws IOException {

        FileHandle handle = Gdx.files.internal(filePath);
        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(handle.reader());

        loadFonts(root);

    }

    private void loadFonts(XmlReader.Element root) {
        for (XmlReader.Element fontElement : root.getChildrenByName(FONT_NODE)) {
            loadFont(fontElement);
        }
    }

    private void loadFont(XmlReader.Element fontElement) {
        FontInfo fontInfo = new FontInfo();

        fontInfo.name = fontElement.getAttribute(NAME_ATTR);
        fontInfo.bitmap = resourceManager.getFont(fontElement.getAttribute(BITMAP_ATTR));
        fontInfo.scalexy = fontElement.getFloatAttribute(SCALEXY_ATTR, 0f);
        fontInfo.scalex = fontElement.getFloatAttribute(SCALEX_ATTR, 0f);
        fontInfo.scaley = fontElement.getFloatAttribute(SCALEY_ATTR, 0f);

        fonts.put(fontInfo.name, fontInfo);
    }

    public Map<String, FontInfo> getFonts() {
        return fonts;
    }
}
