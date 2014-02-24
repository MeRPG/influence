package com.teremok.influence.screen;

import com.teremok.influence.Influence;

/**
 * Created by Alexx on 24.02.14
 */
public class ScreenController {

    private static Influence game;

    private static StartScreenAlt startScreen;
    private static SettingsScreenAlt settingsScreen;

    public static void init(Influence game) {
        ScreenController.game = game;
    }

    public static void showStartScreen() {
        if (startScreen == null) {
            startScreen = new StartScreenAlt(game, "abstractScreen");
        }
        game.setScreen(startScreen);
    }

    public static void showSettingsScreen() {
        if (settingsScreen == null) {
            settingsScreen = new SettingsScreenAlt(game, "settingsScreen");
        }
        game.setScreen(settingsScreen);
    }

}
