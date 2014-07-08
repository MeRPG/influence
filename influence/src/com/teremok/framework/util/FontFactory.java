package com.teremok.framework.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.teremok.framework.TeremokGame;
import com.teremok.framework.ui.font.FontInfo;
import com.teremok.framework.ui.font.FontsLoader;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Алексей on 08.06.2014
 */
public class FontFactory {

    private Map<String, FontInfo> fonts;

    public FontFactory(TeremokGame game) {
        FontsLoader fontsLoader = new FontsLoader(game);
        try {
            fontsLoader.load();
        } catch (IOException e) {
            Gdx.app.error(getClass().getSimpleName(), "Font loading failed");
            game.exit();
        }
        fonts = fontsLoader.getFonts();
    }

    public BitmapFont getFont(String fontName) {
        FontInfo info = fonts.get(fontName);

        if (info == null) {
            throw new IllegalArgumentException("Unknown font name: " + fontName);
        }

        return info.prepare();
    }
}
