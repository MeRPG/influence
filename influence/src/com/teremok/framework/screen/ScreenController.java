package com.teremok.framework.screen;

/**
 * Created by Алексей on 08.07.2014
 */
public interface ScreenController {

    public void setScreen (String screenName);

    public StaticScreen getCurrentScreen();
    public void setCurrentScreen(StaticScreen screen);

    public StaticScreen resolve(String screenName);

    public void exitGame();
    public void gracefullyExitGame();

}
