package com.teremok.influence.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlWriter;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.util.Logger;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Alexx on 21.02.14
 */
public class MatchSaver {

    private static String PATH=".influence-match";

    public static void save(Match match) {
        try {
            saveInFile(match);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveInFile(Match match) throws IOException {
        FileHandle handle = Gdx.files.external(PATH);
        FileWriter fileWriter = new FileWriter(handle.file());
        Logger.log(handle.file().getAbsolutePath());
        XmlWriter xml = new XmlWriter(fileWriter);
        XmlWriter xmlMatch = xml.element("match");

        XmlWriter playersXml = xml.element("players");
        PlayerManager pm = match.getPm();
        for (Player player : pm.getPlayers()) {
            playersXml.element("player")
                    .attribute("number", player.getNumber())
                    .text(player.getType())
                    .pop()
                    ;
        }
        playersXml.pop();


        XmlWriter fieldXml = xml.element("field");
        Field field = match.getField();
        for (Cell cell : field.getCells()) {
            fieldXml.element("cell")
                    .attribute("number", cell.getNumber())
                    .attribute("unitsX", cell.getUnitsX())
                    .attribute("unitsX", cell.getUnitsY())
                    .attribute("maxPower", cell.getMaxPower())
                    .attribute("type", cell.getType())
                    .text(cell.getPower())
                    .pop();
        }
        fieldXml.pop();
        xml.close();
    }

}
