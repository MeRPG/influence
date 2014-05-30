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

import static com.teremok.influence.model.Chronicle.*;
import static com.teremok.influence.util.IOConstants.CHRONICLE_PATH;

/**
 * Created by Алексей on 19.05.2014
 */
public class ChronicleController {

    public static void save() {
        FileHandle handle = Gdx.files.external(CHRONICLE_PATH);
        try {
            FileWriter fileWriter = new FileWriter(handle.file());
            Logger.log(handle.file().getAbsolutePath());
            XmlWriter xml = new XmlWriter(fileWriter);
            XmlWriter root = xml.element("chronicle");

            root.element("played", played)
                .element("won", won)
                .element("damage", damage)
                .element("damageGet", damageGet)
                .element("cellsConquered", cellsConquered)
                .element("cellsLost", cellsLost)
                .element("influence", influence);

            XmlWriter match = root.element("match");

            match.element("damage", Chronicle.match.damage)
                .element("damageGet", Chronicle.match.damageGet)
                .element("cellsConquered", Chronicle.match.cellsConquered)
                .element("cellsLost", Chronicle.match.cellsLost)
                .pop();

            root.pop();
            xml.flush();
            fileWriter.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void load() {
        FileHandle handle = Gdx.files.external(CHRONICLE_PATH);
        if (handle.exists()) {
            try{
                XmlReader reader = new XmlReader();
                XmlReader.Element root = reader.parse(handle.reader());

                played = root.getInt("played", 0);
                won = root.getInt("won", 0);
                influence = root.getInt("influence", 0);
                damage = root.getInt("damage", 0);
                damageGet = root.getInt("damageGet", 0);
                cellsConquered = root.getInt("cellsConquered", 0);
                cellsLost = root.getInt("cellsLost", 0);


                XmlReader.Element match = root.getChildByName("match");
                Chronicle.match.damage = match.getInt("damage", 0);
                Chronicle.match.damageGet = match.getInt("damageGet", 0);
                Chronicle.match.cellsConquered = match.getInt("cellsConquered", 0);
                Chronicle.match.cellsLost = match.getInt("cellsLost", 0);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void matchStart() {
        match.cellsLost = 0;
        match.cellsConquered = 0;
        match.damage = 0;
        match.damageGet = 0;
    }

    public static void matchEnd(Map<Integer, PlayerType> players, FieldSize fieldSize, boolean isWin) {
        damage += match.damage;
        damageGet += match.damageGet;
        cellsConquered += match.cellsConquered;
        cellsLost += match.cellsLost;

        played++;

        if (isWin) {
            won++;
            Chronicle.influence += getWinInfluence(players.values(), fieldSize);
        } else {
            Chronicle.influence += getLoseInfluence(players.values(), fieldSize);
        }

        clearMatchScores();

        ChronicleController.save();
    }

    public static void clearMatchScores() {
        match.damage = 0;
        match.damageGet = 0;
        match.cellsConquered = 0;
        match.cellsLost = 0;
    }

    public static int getWinInfluence(Collection<PlayerType> players, FieldSize fieldSize) {
        int score = 0;
        float playerSum = 0;
        for (PlayerType player : players) {
            playerSum += getW(player);
        }
        playerSum *= Math.exp(1);

        float fieldSum = getW(fieldSize) * (float)Math.exp(1);

        float cellsSum = cellsConquered * (float)Math.exp(1);

        score = (int)(playerSum + fieldSum + cellsSum);

        return score;
    }

    public static int getLoseInfluence(Collection<PlayerType> players, FieldSize fieldSize) {
        int score;
        float playerSum = 0;
        for (PlayerType player : players) {
            playerSum += getW(player);
        }
        playerSum = (float) (-playerSum*Math.exp(1) + playerSum);

        float fieldSum = -getW(fieldSize) * (float) Math.exp(1) + getW(fieldSize);

        float cellsSum = - (cellsConquered + cellsLost) * (float)Math.exp(1);


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
