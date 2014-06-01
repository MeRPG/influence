package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.teremok.influence.model.Chronicle;
import com.teremok.influence.ui.TextureNumber;
import com.teremok.influence.util.Logger;
import com.teremok.influence.util.TextureNumberFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 26.05.2014
 */
public class StatisticsScreen extends StaticScreen {

    public StatisticsScreen (Game game, String filename) {
        super(game, filename);
    }

    @Override
    protected void addActors() {
        TextureNumberFactory numberFactory = new TextureNumberFactory();

        TextureNumber number;
        number = numberFactory.getNumber(Chronicle.played, 240, 455, false);
        number.setColor(Chronicle.played == 0 ? numberFactory.BAD_COLOR : numberFactory.NORMAL_COLOR);
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(Chronicle.getWinRate(), 240, 361, true);
        number.setColor(numberFactory.getCompareColor(Chronicle.getWinRate(), 50));
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(Chronicle.cellsConquered, 240, 267, false);
        number.setColor(Chronicle.cellsConquered == 0 ? numberFactory.BAD_COLOR : numberFactory.GOOD_COLOR);
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(Chronicle.cellsLost, 240, 173, false);
        number.setColor(numberFactory.BAD_COLOR);
        number.centre();
        stage.addActor(number);

        number = numberFactory.getNumber(Chronicle.influence, 240, 79, false);
        number.setColor(Chronicle.influence < 1 ? numberFactory.BAD_COLOR : numberFactory.NORMAL_COLOR);
        number.centre();
        stage.addActor(number);

    }

    @Override
    protected void addListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (! event.isHandled() && (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) ){
                    ScreenController.showStartScreen();
                    return true;
                }
                return false;
            }


        });
        stage.addListener( new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (! event.isHandled() && isInfoMessageVisible()) {
                    hideInfoMessageAnimation();
                    event.handle();
                    return;
                }
                if (! event.isHandled()) {

                    event.handle();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("id", 1 +"");
                    parameters.put("influence", Chronicle.influence +"");

                    Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.GET);
                    httpGet.setUrl("http://timeforlime.ru/influence/index.php");
                    httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));

                    Gdx.net.sendHttpRequest (httpGet, new Net.HttpResponseListener() {
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            String status = httpResponse.getResultAsString();
                            int stat = Integer.parseInt(status);
                            addInfoMessage(new TextureNumberFactory().getNumber(stat, 100, 100, false));
                            showInfoMessageAnimation();
                            Logger.log("HttpResponse " + status);
                        }

                        public void failed(Throwable t) {
                            String status = "failed";
                            //do stuff here based on the failed attempt
                            Logger.log("HttpResponse " + status);
                            t.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
