package com.teremok.influence.util;

import com.flurry.android.FlurryAgent;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion;
import com.teremok.influence.model.Localizator;
import com.teremok.influence.model.MatchSaver;
import com.teremok.influence.model.Settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 25.04.2014
 */
public class FlurryHelper {

    public static final String END_REASON_WIN = "WIN";
    public static final String END_REASON_LOSE = "LOSE";
    public static final String END_REASON_EXITGAME = "EXIT_GAME";
    public static final String END_REASON_RESTART = "RESTART";
    public static final String END_REASON_EXITMENU = "EXIT_MENU";
    public static boolean enabled;

    public static void logFullPowerEvent() {
        if (flurryEnabled()) {
            FlurryAgent.logEvent("Full_Power");
        }
    }

    public static void logAutoPowerEvent() {
        if (flurryEnabled()) {
            FlurryAgent.logEvent("Auto_Power");
        }
    }

    public static void logVkClickEvent() {
        if (flurryEnabled()) {
            Map<String,String> parameters = new HashMap<>();
            parameters.put("Language", Localizator.getLanguage());
            FlurryAgent.logEvent("Vk_Click", parameters);
        }
    }

    public static void logPlayClickEvent() {
        if (flurryEnabled()) {
            Map<String,String> parameters = new HashMap<>();
            parameters.put("Language", Localizator.getLanguage());
            FlurryAgent.logEvent("Play_Click", parameters);
        }
    }

    public static void logNewGameEvent() {
        if (flurryEnabled()) {
            Map<String,String> parameters = new HashMap<>();
            parameters.put("Has_Saved_Game", MatchSaver.hasNotEnded() ? "Yes" : "No");
            FlurryAgent.logEvent("New_Game", parameters);
        }
    }

    public static void logContinueGameEvent() {
        if (flurryEnabled()) {
            FlurryAgent.logEvent("Continue_Game");
        }
    }

    public static void logStartScreenEvent() {
        if (flurryEnabled()) {
            FlurryAgent.logEvent("Start_Screen");
        }
    }

    public static void logMapSizeScreenEvent() {
        if (flurryEnabled()) {
            FlurryAgent.logEvent("MapSize_Screen");
        }
    }

    public static void logAboutScreenEvent() {
        if (flurryEnabled()) {
            FlurryAgent.logEvent("About_Screen");
        }
    }

    public static void logSettingsScreenEvent() {
        if (flurryEnabled()) {
            FlurryAgent.logEvent("Settings_Screen");
        }
    }

    public static void logPlayersScreenEvent() {
        if (flurryEnabled()) {
            FlurryAgent.logEvent("Players_Screen");
        }
    }

    public static void logSettingsChangeEvent() {
        if (flurryEnabled()) {
            Map<String,String> parameters = new HashMap<>();
            parameters.put("Sound", Settings.sound ? "On" : "Off");
            parameters.put("Vibration", Settings.vibrate ? "On" : "Off");
            parameters.put("Language", Localizator.getLanguage());
            parameters.put("Game_Speed", Settings.speed+"");
            FlurryAgent.logEvent("Settings_Change", parameters);
        }
    }

    public static void logMatchStartEvent(boolean isResume) {
        if (flurryEnabled()) {
            Map<String,String> parameters = new HashMap<>();
            parameters.put("Field_Size", Settings.gameSettings.fieldSize.toString());
            parameters.put("Game_Difficulty", Settings.gameSettings.difficulty.toString());
            parameters.put("Enemies", Settings.gameSettings.getNumberOfPlayers()+"");
            parameters.put("Humans", Settings.gameSettings.getNumberOfHumans()+"");
            parameters.put("isResume", isResume ? "Yes" : "No");
            FlurryAgent.logEvent("Match_Start", parameters);
        }
    }


    public static void logMatchEndEvent(String reason) {
        if (flurryEnabled()) {
            Map<String,String> parameters = new HashMap<>();
            parameters.put("Reason", reason);
            FlurryAgent.logEvent("Match_End", parameters);
        }
    }

    private static boolean flurryEnabled() {
        return enabled;
    }

}
