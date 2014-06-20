package com.teremok.influence.controller;

import com.teremok.influence.model.Chronicle;
import com.teremok.influence.model.FieldSize;
import com.teremok.influence.model.player.PlayerType;
import com.teremok.influence.test.LibGDXTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.teremok.influence.model.Chronicle.MatchChronicle;
import static com.teremok.influence.model.FieldSize.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Алексей on 12.06.2014
 */
public class ChronicleControllerTest extends LibGDXTest {

    Chronicle chronicle;
    MatchChronicle matchChronicle;
    ChronicleController chronicleController;

    @Before
    public void setUp(){
        matchChronicle = new MatchChronicle();
        matchChronicle.cellsConquered = 40;
        matchChronicle.cellsLost = 15;
        matchChronicle.damage = 10;
        matchChronicle.damageGet = 20;

        chronicle = new Chronicle();
        chronicle.cellsConquered = 10;
        chronicle.cellsLost = 10;
        chronicle.damage = 1000;
        chronicle.damageGet = 800;
        chronicle.played = 10;
        chronicle.won = 5;
        chronicle.influence = 5000;
        chronicle.match = matchChronicle;


        chronicleController = new ChronicleController();
    }

    @Test
    public void testSaveAndLoad() {
        chronicleController.save(chronicle);
        Chronicle loadedChronicle = chronicleController.load();

        assertEquals(chronicle, loadedChronicle);
    }

    @Test
    public void testSaveWithNullMatchChronicle() {
        chronicle.match = null;
        chronicleController.save(chronicle);

        chronicle.match = new MatchChronicle();

        Chronicle loadedChronicle = chronicleController.load();

        assertEquals(chronicle, loadedChronicle);
    }

    @Test
    public void testL3Hunters1Smarty() {
        testL3Hunters1Smarty(1196, true);
        testL3Hunters1Smarty(-845, false);
    }

    public void testL3Hunters1Smarty(int expectedInfluence, boolean win) {
        Map<Integer, PlayerType> playerTypeMap = get3Hunters1Smarty();
        testInfluence(playerTypeMap, FieldSize.LARGE, expectedInfluence, win);
    }


    @Test
    public void testS1Lazy1Beefy1Smarty1Hunter() {
        testS1Lazy1Beefy1Smarty1Hunter(801, true);
        testS1Lazy1Beefy1Smarty1Hunter(-533, false);
    }

    public void testS1Lazy1Beefy1Smarty1Hunter(int expectedInfluence, boolean win) {
        Map<Integer, PlayerType> playerTypeMap = get1Lazy1Beefy1Smarty1Hunter();
        testInfluence(playerTypeMap, SMALL, expectedInfluence, win);
    }

    @Test
    public void testM4Hunters() {
        testM4Hunters(1413, true);
        testM4Hunters(-946, false);
    }

    public void testM4Hunters(int expectedInfluence, boolean win) {
        Map<Integer, PlayerType> playerTypeMap = get4Hunters();
        testInfluence(playerTypeMap, NORMAL, expectedInfluence, win);
    }

    @Test
    public void testL2Lazy() {
        testL2Lazy(299, true);
        testL2Lazy(-278, false);
    }

    public void testL2Lazy(int expectedInfluence, boolean win) {
        Map<Integer, PlayerType> playerTypeMap = get2Lazy();
        testInfluence(playerTypeMap, LARGE, expectedInfluence, win);
    }

    @Test
    public void testXL4Hunters() {
        testXL4Hunters(1277, true);
        testXL4Hunters(-918, false);
    }

    void testXL4Hunters(int expectedInfluence, boolean win) {
        Map<Integer, PlayerType> playerTypeMap = get4Hunters();
        testInfluence(playerTypeMap, XLARGE, expectedInfluence, win);
    }

    void testInfluence(Map<Integer, PlayerType> playerTypeMap, FieldSize fieldSize, int expectedInfluence, boolean win) {
        chronicle = new Chronicle();
        chronicle.match = chronicleController.matchStart();

        setUpMatchChronicleForField(fieldSize);

        chronicleController.matchEnd(chronicle, playerTypeMap, fieldSize, win);

        assertEquals(expectedInfluence, chronicle.influence);
    }

    void setUpMatchChronicleForField (FieldSize fieldSize) {
        switch (fieldSize) {
            case SMALL:
                setUpMatchChronicleForS();
                break;
            case NORMAL:
                setUpMatchChronicleForM();
                break;
            case LARGE:
                setUpMatchChronicleForL();
                break;
            case XLARGE:
                setUpMatchChronicleForXL();
                break;
        }
    }

    private void setUpMatchChronicleForS() {
        chronicle.match.cellsConquered = 10;
        chronicle.match.cellsLost = 6;
        chronicle.match.damage = 0;
        chronicle.match.damageGet = 0;
    }

    private void setUpMatchChronicleForM() {
        chronicle.match.cellsConquered = 20;
        chronicle.match.cellsLost = 12;
        chronicle.match.damage = 0;
        chronicle.match.damageGet = 0;
    }

    private void setUpMatchChronicleForL() {
        chronicle.match.cellsConquered = 30;
        chronicle.match.cellsLost = 22;
        chronicle.match.damage = 0;
        chronicle.match.damageGet = 0;
    }

    private void setUpMatchChronicleForXL() {
        chronicle.match.cellsConquered = 40;
        chronicle.match.cellsLost = 26;
        chronicle.match.damage = 0;
        chronicle.match.damageGet = 0;
    }

    public Map<Integer, PlayerType> get4Hunters() {
        Map<Integer, PlayerType> playerTypeMap = new HashMap<>();
        playerTypeMap.put(0, PlayerType.Human);
        playerTypeMap.put(1, PlayerType.Hunter);
        playerTypeMap.put(2, PlayerType.Hunter);
        playerTypeMap.put(3, PlayerType.Hunter);
        playerTypeMap.put(4, PlayerType.Hunter);
        return playerTypeMap;
    }

    public Map<Integer, PlayerType> get2Lazy() {
        Map<Integer, PlayerType> playerTypeMap = new HashMap<>();
        playerTypeMap.put(0, PlayerType.Human);
        playerTypeMap.put(1, PlayerType.Lazy);
        playerTypeMap.put(2, PlayerType.Lazy);
        return playerTypeMap;
    }

    public Map<Integer, PlayerType> get1Lazy1Beefy1Smarty1Hunter() {
        Map<Integer, PlayerType> playerTypeMap = new HashMap<>();
        playerTypeMap.put(0, PlayerType.Human);
        playerTypeMap.put(1, PlayerType.Lazy);
        playerTypeMap.put(2, PlayerType.Beefy);
        playerTypeMap.put(3, PlayerType.Smarty);
        playerTypeMap.put(4, PlayerType.Hunter);
        return playerTypeMap;
    }

    public Map<Integer, PlayerType> get3Hunters1Smarty() {
        Map<Integer, PlayerType> playerTypeMap = new HashMap<>();
        playerTypeMap.put(0, PlayerType.Human);
        playerTypeMap.put(1, PlayerType.Hunter);
        playerTypeMap.put(2, PlayerType.Hunter);
        playerTypeMap.put(3, PlayerType.Hunter);
        playerTypeMap.put(4, PlayerType.Smarty);
        return playerTypeMap;
    }
}
