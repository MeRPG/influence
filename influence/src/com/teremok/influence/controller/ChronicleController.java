package com.teremok.influence.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.teremok.influence.model.Chronicle;
import com.teremok.influence.util.Logger;

import java.io.FileWriter;
import java.io.IOException;

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
                .element("cellsLost", cellsLost);

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
}
