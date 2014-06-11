package com.teremok.influence.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.teremok.influence.model.Chronicle;
import com.teremok.influence.model.FieldSize;
import com.teremok.influence.model.player.PlayerType;
import com.teremok.influence.util.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static com.teremok.influence.model.Chronicle.MatchChronicle;
import static com.teremok.influence.util.IOConstants.CHRONICLE_PATH;

/**
 * Created by Алексей on 19.05.2014
 */
public class ChronicleController {

    public ChronicleController() {
    }

    public void save(Chronicle chronicle) {
        FileHandle handle = Gdx.files.external(CHRONICLE_PATH);
        try {
            FileWriter fileWriter = new FileWriter(handle.file());
            Logger.log(handle.file().getAbsolutePath());
            XmlWriter xml = new XmlWriter(fileWriter);
            XmlWriter root = xml.element("chronicle");

            root.element("played", chronicle.played)
                .element("won", chronicle.won)
                .element("damage", chronicle.damage)
                .element("damageGet", chronicle.damageGet)
                .element("cellsConquered", chronicle.cellsConquered)
                .element("cellsLost", chronicle.cellsLost)
                .element("influence", chronicle.influence);


            XmlWriter match = root.element("match");
            MatchChronicle matchChronicle = chronicle.match;
            match.element("damage", matchChronicle == null ? 0 : matchChronicle.damage)
                .element("damageGet", matchChronicle == null ? 0 : matchChronicle.damageGet)
                .element("cellsConquered",  matchChronicle == null ? 0 : matchChronicle.cellsConquered)
                .element("cellsLost",  matchChronicle == null ? 0 : matchChronicle.cellsLost)
                .pop();

            root.pop();
            xml.flush();
            fileWriter.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Chronicle load() {
        Chronicle chronicle = new Chronicle();

        FileHandle handle = Gdx.files.external(CHRONICLE_PATH);
        if (handle.exists()) {
            try{
                XmlReader reader = new XmlReader();
                XmlReader.Element root = reader.parse(handle.reader());

                chronicle.match = new MatchChronicle();

                chronicle.played = root.getInt("played", 0);
                chronicle.won = root.getInt("won", 0);
                chronicle.influence = root.getInt("influence", 0);
                chronicle.damage = root.getInt("damage", 0);
                chronicle.damageGet = root.getInt("damageGet", 0);
                chronicle.cellsConquered = root.getInt("cellsConquered", 0);
                chronicle.cellsLost = root.getInt("cellsLost", 0);


                XmlReader.Element match = root.getChildByName("match");
                chronicle.match.damage = match.getInt("damage", 0);
                chronicle.match.damageGet = match.getInt("damageGet", 0);
                chronicle.match.cellsConquered = match.getInt("cellsConquered", 0);
                chronicle.match.cellsLost = match.getInt("cellsLost", 0);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return chronicle;
    }

    public MatchChronicle matchStart() {
        MatchChronicle matchChronicle = new MatchChronicle();
        return matchChronicle;
    }

    public void matchEnd(Map<Integer, PlayerType> players, FieldSize fieldSize, Chronicle chronicle, boolean isWin) {
        MatchChronicle matchChronicle = chronicle.match;
        chronicle.damage += matchChronicle.damage;
        chronicle.damageGet += matchChronicle.damageGet;
        chronicle.cellsConquered += matchChronicle.cellsConquered;
        chronicle.cellsLost += matchChronicle.cellsLost;

        chronicle.played++;

        if (isWin) {
            chronicle.won++;
            chronicle.influence += getWinInfluence(chronicle, players.values(), fieldSize);
        } else {
            chronicle.influence += getLoseInfluence(chronicle, players.values(), fieldSize);
        }

        save(chronicle);
    }

    public void clearMatchScores(Chronicle chronicle) {
        chronicle.match = null;
    }

    public int getWinInfluence(Chronicle chronicle, Collection<PlayerType> players, FieldSize fieldSize) {
        int score;
        float playerSum = 0;
        for (PlayerType player : players) {
            playerSum += getW(player);
        }
        playerSum *= Math.exp(1);

        float fieldSum = getW(fieldSize) * (float)Math.exp(1);

        float cellsSum = chronicle.cellsConquered * (float)Math.exp(1);

        score = (int)(playerSum + fieldSum + cellsSum);

        return score;
    }

    public int getLoseInfluence(Chronicle chronicle, Collection<PlayerType> players, FieldSize fieldSize) {
        int score;
        float playerSum = 0;
        for (PlayerType player : players) {
            playerSum += getW(player);
        }
        playerSum = (float) (-playerSum*Math.exp(1) + playerSum);

        float fieldSum = -getW(fieldSize) * (float) Math.exp(1) + getW(fieldSize);

        float cellsSum = - (chronicle.cellsConquered + chronicle.cellsLost) * (float)Math.exp(1);


        score = (int) (playerSum + fieldSum + cellsSum);

        return score;
    }

    private static float getW(PlayerType type) {
        float ret = 0;
        switch (type){
            case Human:
                ret = 0;
                break;
            case Random:
                ret = 5;
                break;
            case Dummy:
                ret = 10;
                break;
            case Lazy:
                ret = 15;
                break;
            case Beefy:
                ret = 30;
                break;
            case Smarty:
                ret = 60;
                break;
            case Hunter:
                ret = 100;
                break;
        }
        return ret;
    }

    private static float getW(FieldSize size) {
        float ret = 0;
        switch (size){
            case SMALL:
                ret = 80;
                break;
            case NORMAL:
                ret = 100;
                break;
            case LARGE:
                ret = 50;
                break;
            case XLARGE:
                ret = 30;
                break;
        }
        return ret;
    }
}
