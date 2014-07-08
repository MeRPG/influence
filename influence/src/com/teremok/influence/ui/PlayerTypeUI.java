package com.teremok.influence.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teremok.framework.ui.UIElementParams;
import com.teremok.influence.model.player.PlayerType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 31.03.2014
 */
public class PlayerTypeUI extends ButtonTexture {

    private static Map<PlayerType, TextureRegion> regions;
    private static Map<Integer, PlayerType> map;

    int currentNum = 0;

    static {
        map = new HashMap<>();
        map.put(0, PlayerType.Human);
        map.put(1, PlayerType.Random);
        map.put(2, PlayerType.Dummy);
        map.put(3, PlayerType.Lazy);
        map.put(4, PlayerType.Beefy);
        map.put(5, PlayerType.Smarty);
        map.put(6, PlayerType.Hunter);
    }


    public PlayerTypeUI(TextureRegion region, float x, float y, String code) {
        super(code, region, x, y);
        this.code = code;
    }

    public PlayerTypeUI(UIElementParams params) {
        super(params);
    }

    public void setTypeByNumber(int i) {
        currentNum = i;
        region = regions.get(map.get(currentNum));
    }

    public void setType(PlayerType playerType) {
        currentNum = playerType.ordinal();
        Gdx.app.debug(getClass().getSimpleName(), "PlayerTypeUI - playerType.ordinal = " + playerType.ordinal());
        region =  regions.get(map.get(currentNum));
    }

    public PlayerType getType() {
        return map.get(currentNum);
    }

    public void next() {
        Gdx.app.debug(getClass().getSimpleName(), "PlayerTypeUI - next, code:" + code);
        if (currentNum < map.size()-1) {
            currentNum++;
        } else {
            currentNum = 0;
        }
        region = regions.get(map.get(currentNum));
    }

    public static void setRegions(Map<PlayerType, TextureRegion> regions) {
        PlayerTypeUI.regions = regions;
    }

    public String getCode() {
        return code;
    }
}
