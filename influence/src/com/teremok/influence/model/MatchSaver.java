package com.teremok.influence.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.model.player.PlayerType;
import com.teremok.influence.util.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexx on 21.02.14
 */
public class MatchSaver {
    private static final String ROOT="match";

    private static final String TURN_ATTR = "turn";

    private static final String FIELD="field";
    private static final String PLAYER="player";
    private static final String PLAYERS="players";
    private static final String NUMBER="number";

    private static final String CELL="cell";
    private static final String UNITS_X="unitsX";
    private static final String UNITS_Y="unitsY";
    private static final String MAX_POWER="maxPower";
    private static final String TYPE="type";

    private static final String ROUTE="route";
    private static final String FROM="from";
    private static final String TO="to";
    private static final String ENABLED="enabled";

    private static String FILENAME ="match.xml";

    private static Match notEnded;

    public static void save(Match match) {
        try {
            if (! match.isEnded()){
                saveInFile(match);
                notEnded = match;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearFile() {
        try {
            FileHandle handle = Gdx.files.external(Settings.DIR+FILENAME);
            FileWriter fileWriter = new FileWriter(handle.file());
            Logger.log("Game save cleared: " + handle.file().getAbsolutePath());
            XmlWriter xml = new XmlWriter(fileWriter);
            XmlWriter xmlMatch = xml.element(ROOT);
            xmlMatch.pop();
            xml.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private static void saveInFile(Match match) throws IOException {
        FileHandle handle = Gdx.files.external(Settings.DIR+FILENAME);
        FileWriter fileWriter = new FileWriter(handle.file());
        Logger.log(handle.file().getAbsolutePath());
        XmlWriter xml = new XmlWriter(fileWriter);
        XmlWriter xmlMatch = xml.element(ROOT).attribute(TURN_ATTR, match.turn);

        XmlWriter playersXml = xml.element(PLAYERS);
        PlayerManager pm = match.getPm();
        for (Player player : pm.getPlayers()) {
            playersXml.element(PLAYER)
                    .attribute(NUMBER, player.getNumber())
                    .text(player.getType())
                    .pop()
                    ;
        }
        playersXml.pop();

        Settings.saveGameSettings(xmlMatch);

        XmlWriter fieldXml = xml.element(FIELD);
        FieldModel fieldModel = match.getField().getModel();
        for (Cell cell : fieldModel.cells) {
            fieldXml.element(CELL)
                    .attribute(NUMBER, cell.getNumber())
                    .attribute(UNITS_X, cell.getUnitsX())
                    .attribute(UNITS_Y, cell.getUnitsY())
                    .attribute(MAX_POWER, cell.getMaxPower())
                    .attribute(TYPE, cell.getType())
                    .text(cell.getPower())
                    .pop();
        }

        for (Route route : fieldModel.router.getAsCollection()) {
            fieldXml.element(ROUTE)
                    .attribute(FROM, route.from)
                    .attribute(TO, route.to)
                    .attribute(ENABLED, route.enabled)
                    .pop();
        }

        fieldXml.pop();
        xml.close();
    }

    public static Match load() {
        Match match = null;
        try {
            match = loadFromFile();
            notEnded = match;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return match;
    }

    private static Match loadFromFile() throws IOException{
        Match match;
        FileHandle handle = Gdx.files.external(Settings.DIR+FILENAME);
        Logger.log("loading match from file " + handle.path());
        if (handle.exists()) {
            XmlReader reader = new XmlReader();

            XmlReader.Element root = reader.parse(handle.reader());

            Settings.loadGameSettings(root);

            GameSettings gameSettings = Settings.gameSettings;

            Logger.log("loading players");
            loadPlayers(root, gameSettings);
            List<Cell> cells = loadCells(root);

            Router router = loadRoutes(root);

            match = new Match(gameSettings, cells, router);
            match.turn = Integer.parseInt(root.getAttribute(TURN_ATTR, "0"));

        } else {
            throw new IOException("File with saved match not found " + Settings.DIR+FILENAME);
        }
        return match;
    }

    private static void loadPlayers(XmlReader.Element root, GameSettings settings) {
        Integer number;
        String type;
        Map<Integer, PlayerType> players = new HashMap<>();

        XmlReader.Element playersRoot = root.getChildByName(PLAYERS);
        for (XmlReader.Element player : playersRoot.getChildrenByName(PLAYER)) {
            number = Integer.parseInt(player.getAttribute(NUMBER, "0"));
            type = player.getText();
            players.put(number, PlayerType.valueOf(type));
            Logger.log("adding player " + type + " with number " + number);
        }

        settings.players = players;
    }

    private static List<Cell> loadCells(XmlReader.Element root) {
        int number;
        int unitsX;
        int unitsY;
        int power;
        int maxPower;
        int type;

        Cell cell;

        List<Cell> cells = new LinkedList<>();

        XmlReader.Element playersRoot = root.getChildByName(FIELD);
        for (XmlReader.Element player : playersRoot.getChildrenByName(CELL)) {
            number = Integer.parseInt(player.getAttribute(NUMBER, "0"));
            unitsX = Integer.parseInt(player.getAttribute(UNITS_X, "0"));
            unitsY = Integer.parseInt(player.getAttribute(UNITS_Y, "0"));
            maxPower = Integer.parseInt(player.getAttribute(MAX_POWER, "8"));
            type = Integer.parseInt(player.getAttribute(TYPE, "-1"));
            power = Integer.parseInt(player.getText());

            cell = new Cell(number, unitsX, unitsY, power, maxPower, type);
            cells.add(cell);
            Logger.log("adding cell " + cell);
        }
        return cells;
    }


    private static Router loadRoutes(XmlReader.Element root) {
        int from;
        int to;
        boolean enabled;

        Route route;

        Router router = new Router();

        XmlReader.Element playersRoot = root.getChildByName(FIELD);
        for (XmlReader.Element player : playersRoot.getChildrenByName(ROUTE)) {
            from = player.getIntAttribute(FROM, 0);
            to = player.getIntAttribute(TO, 0);
            enabled =  player.getBooleanAttribute(ENABLED, false);

            route = new Route(from, to, enabled);
            router.add(route);
        }
        router.print();
        return router;
    }

    public static boolean hasNotEnded() {
        return (notEnded != null && !notEnded.isEnded());
    }

    // Auto-generated

    public static Match getNotEnded() {
        return notEnded;
    }
}
