package com.teremok.influence.controller;

import com.teremok.influence.model.GameDifficulty;
import com.teremok.influence.model.GameSettings;
import com.teremok.influence.model.Settings;
import com.teremok.influence.test.LibGDXTest;
import org.junit.Before;
import org.junit.Test;

public class SettingsControllerTest extends LibGDXTest {

    Settings settings;
    GameSettings gameSettings;

    @Before
    public void before() {
        settings = new Settings();
        settings.lastAboutScreen = 5;
        settings.debug = false;
        settings.speed = settings.FAST;
        settings.sound = true;
        settings.vibrate = false;

        gameSettings = GameSettings.getDefault();
        gameSettings.customPlayers = GameSettings.getPlayersByDifficulty(GameDifficulty.HARD, 3);
        gameSettings.players = GameSettings.getPlayersByDifficulty(GameDifficulty.EASY, 2);

    }

    @Test
    public void testSave() throws Exception {

    }

    @Test
    public void testSaveGameSettings() throws Exception {

    }

    @Test
    public void testLoad() throws Exception {

    }

    @Test
    public void testLoad1() throws Exception {

    }

    @Test
    public void testLoadGameSettings() throws Exception {

    }
}