package com.teremok.influence.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.influence.screen.AbstractScreen;
import com.teremok.influence.screen.GameScreen;
import com.teremok.influence.ui.Tooltip;
import com.teremok.influence.ui.TooltipHandler;
import com.teremok.influence.util.FXPlayer;
import com.teremok.influence.util.GraphGenerator;
import com.teremok.influence.util.Logger;
import com.teremok.influence.util.Vibrator;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.view.FieldShapeDrawer;

import java.util.*;

import static com.teremok.influence.view.Drawer.*;

/**
 * Created by Alexx on 11.12.13
 */
public class Field extends Group {

    private static final String ADD_POWER_TOOLTIP = "+1";

    public static final int SIZE_MULTIPLIER = 1;

    public static final int MAX_CELLS_Y = 7*SIZE_MULTIPLIER;
    public static final int MAX_CELLS_X = 5*SIZE_MULTIPLIER;

    public static float WIDTH = UNIT_SIZE*10f*SIZE_MULTIPLIER;
    public static float HEIGHT = UNIT_SIZE*13f*SIZE_MULTIPLIER;

    public static final int CELLS_COUNT = 75;

    private static final int INITIAL_CELL_POWER = 2;

    private List<Cell> cells;

    private Match match;
    private PlayerManager pm;
    private Cell selectedCell;
    private FieldShapeDrawer drawer;

    short[][] matrix;

    public static float cellWidth = UNIT_SIZE*2;
    public static float cellHeight = UNIT_SIZE*2;

    private float initialX;
    private float initialY;
    private float initialWidth;
    private float initialHeight;

    public Field(Match match) {
        this.match = match;
        this.pm = match.getPm();
        drawer = new FieldShapeDrawer();

        initialX = 0f;
        initialY = AbstractScreen.HEIGHT - HEIGHT-1f;

        initialWidth = WIDTH;
        initialHeight = HEIGHT;

        setBounds(initialX, initialY, initialWidth, initialHeight);

        generate();
        addListener();
    }

    public Field(Match match, List<Cell> cells) {
        this.match = match;
        this.pm = match.getPm();
        drawer = new FieldShapeDrawer();

        initialX = 0f;
        initialY = AbstractScreen.HEIGHT - HEIGHT-1f;

        initialWidth = WIDTH;
        initialHeight = HEIGHT;

        setBounds(initialX, initialY, initialWidth, initialHeight);
        GraphGenerator generator = new GraphGenerator(CELLS_COUNT);
        generator.parse(cells);
        this.cells = generator.getCells();
        this.matrix = generator.getMatrix();

        addListener();
    }

    private void generate() {
        GraphGenerator generator = new GraphGenerator(CELLS_COUNT);
        generator.generate();
        cells = generator.getCells();
        matrix = generator.getMatrix();
    }

    public void regenerate() {
        generate();
        updateLists();
    }

    public void placeStartPosition(int type) {
        Random rnd = new Random();
        int number;
        Cell target;

        do {
            number = rnd.nextInt(cells.size());
            target = cells.get(number);

            if (isValidForStartPosition(target)) {
                break;
            }

        } while (true);
        //Logger.log("Placing player: " + target);
        target.setPower(INITIAL_CELL_POWER);
        target.setType(type);
    }

    public void placeStartPositionFromRange(int type, int startNumber, int endNumber) {
        if (startNumber < 0 || startNumber >= MAX_CELLS_Y*MAX_CELLS_X-1) {
            startNumber = 0;
        }

        if (endNumber <= 0 || endNumber >= MAX_CELLS_Y*MAX_CELLS_X-1) {
            endNumber = MAX_CELLS_Y*MAX_CELLS_X-1;
        }

        if (endNumber < startNumber) {
            int tmp = startNumber;
            startNumber = endNumber;
            endNumber = tmp;
        }

        int firstInRange = 0;
        int range = 0;
        for (Cell cell : cells) {
            if (cell.getNumber() >= endNumber)
                break;
            if (cell.getNumber() < startNumber) {
                firstInRange++;
            } else {
                range++;
            }
        }

        //Logger.log("placeStartPositionFromRange [" + startNumber + "; " + endNumber + "]");

        Random rnd = new Random();
        int number;
        Cell target;

        do {
            number = firstInRange + rnd.nextInt(range);
            target = cells.get(number);

            //Logger.log("Trying number " + number);

            if (isValidForStartPosition(target)) {
                break;
            }

        } while (true);
        //Logger.log("Placing player: " + target);
        target.setPower(INITIAL_CELL_POWER);
        target.setType(type);
    }

    private boolean isValidForStartPosition(Cell target) {
        if (target.isValid() && target.isFree()) {
            for (Cell enemy : getConnectedEnemies(target)) {
                if (enemy.getType() != -1){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void addListener() {
        this.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Cell target = hit(x,y);
                    if (target != null) {
                        return true;
                    }
                    Logger.log("Field - touchDown");
                    return event.getType().equals(InputEvent.Type.touchDown);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (!event.isHandled() && ! GestureController.isActing() && match.canHumanActing()) {
                        Cell target = hit(x, y);
                        //Logger.log("actual target: " + target);
                        if (target == null)
                            return;

                        if (target.getType() == pm.current().getNumber() || connectedToSelected(target) && selectedCell.getPower() > 1)
                            FXPlayer.playClick();
                        if (match.isInAttackPhase()) {
                            setSelectedCell(target);
                        } else {
                            HumanPlayer player = (HumanPlayer) pm.current();
                            player.addPowered(target.getNumber());
                            addPower(target);
                        }
                        Logger.log("Field - touchUp ");
                    }
                }
        });

        this.setTouchable(Touchable.enabled);
    }

    public Cell hit(float x, float y) {

        int unitsY = (int)(y/cellHeight);

        if (unitsY%2 == 1) {
            x -= cellWidth/2;
        }

        int unitsX = (int)(x/cellWidth);

        //Logger.log("hit: " + unitsX + "; " + unitsY + "; " + getNum(unitsX,unitsY));

        if (unitsX < 0 || unitsX > MAX_CELLS_X-1 || unitsY < 0 || unitsY > MAX_CELLS_Y-1) {
            cells.get(0);
        }

        return getByNumber(getNum(unitsX,unitsY));
    }

    private Cell getByNumber(int number) {
        for (Cell cell : cells) {
            if (cell.getNumber() == number){
                //Logger.log("found by number");
                return  cell;
            }
        }
        return cells.get(0);
    }

    private int getNum( int i, int j) {
        return i + j*MAX_CELLS_X;
    }

    private boolean connectedToSelected(Cell cell) {
        if (selectedCell == null)
            return false;
        for (Cell c : getConnectedCells(cell)) {
            if (selectedCell == c) {
                return true;
            }
        }
        return false;
    }

    public void setSelectedCell(Cell cell) {

        if (selectedCell != null) {
            if (isCellsConnected(selectedCell, cell)
                    && selectedCell.getPower() > 1
                    && selectedCell.getType() != cell.getType()) {

                int delta = fight(selectedCell, cell);

                if (delta > 0) {
                    cell.setType(selectedCell.getType());
                    reallySetSelected(cell);
                    updateLists();
                } else if (pm.isHumanActing()) {
                    match.score.setStatus(Localizator.getString("selectMoreThanOne"));
                }
            } else  {
                reallySetSelected(cell);
            }
        } else {
            reallySetSelected(cell);
        }
    }

    public void addPower(Cell cell) {
        if (cell.getType() == pm.current().getNumber()) {
            int newPower = cell.getPower() + 1;
            int maxPower = cell.getMaxPower();
            if (newPower <= maxPower && pm.current().getPowerToDistribute() > 0) {

                //riseAddPowerTooltip(cell);

                cell.setPower(cell.getPower() + 1);
                pm.current().subtractPowerToDistribute();
            }  else {
                //Logger.log("Wrong add power " + cell);
            }
        }
    }

    public void addPowerFull(Cell cell) {
        while (pm.current().getPowerToDistribute() > 0 && cell.getPower() < cell.getMaxPower()) {
            addPower(cell);
        }
    }

    private void reallySetSelected(Cell cell) {
        if (cell.getType() == pm.current().getNumber()) {
            if (selectedCell != null)
                selectedCell.setSelected(false);
            cell.setSelected(true);
            selectedCell = cell;

            if (pm.isHumanActing()) {
                if (cell.getPower() <= 1) {
                    match.score.setStatus(Localizator.getString("selectMoreThanOne"));
                } else {
                    match.score.setStatus(Localizator.getString("touchNearby"));
                }
            }  else {
                match.score.setStatus(Localizator.getString("waitYourMove"));
            }
        }
    }

    public void resetSelection() {
        if (selectedCell != null) {
            selectedCell.setSelected(false);
            selectedCell = null;
        }
    }

    private int fight(Cell attack, Cell defense) {

        //Logger.log("Attack!");
        //Logger.log(attack.getPower() + " \t->\t " + defense.getPower());

        int delta = Calculator.fight(attack.getPower(), defense.getPower());

        //riseDiceTooltips(attack, defense);
        fastShowBacklight(attack, defense);
        setResultPower(attack, defense);

        return delta;
    }

    private void setResultPower(Cell attack, Cell defense) {
        int calcA = Calculator.getResultPowerA();
        int calcB = Calculator.getResultPowerB();

        if (calcB > defense.getMaxPower()) {
            calcA += calcB - defense.getMaxPower();
            calcB = defense.getMaxPower();
        }

        attack.setPower(calcA);
        defense.setPower(calcB);
    }

    private boolean isCellsConnected(Cell from, Cell to) {

        if (from.getNumber() == to.getNumber())
            return false;

        return matrix[from.getNumber()][to.getNumber()] == 1 || matrix[to.getNumber()][from.getNumber()] == 1;

    }

    public int getPowerToDistribute(int type){
        int power = 0;
        int maxCapacity = 0;
        for (Cell cell : cells) {
            if (cell.isValid() && cell.getType() == type) {
                power += 1;
                maxCapacity += cell.getMaxPower() - cell.getPower();
            }
        }
        if (power > maxCapacity)
            power = maxCapacity;
        return power;
    }

    // TODO: refactor
    public void fastShowBacklight(Cell attack, Cell defence) {

        if (pm.isHumanActing()) {

            if (Calculator.getDelta() > 0) {
                if (defence.getType() != -1) {
                    GameScreen.colorForBorder = getBacklightWinColor();
                    FXPlayer.playWin();
                }
            } else {
                GameScreen.colorForBorder = getBacklightLoseColor();
                FXPlayer.playLose();
            }
            if (defence.getPower() != 0) {
                Vibrator.bzz();
            }
        } else {
            for (Player player : pm.getPlayers()) {
                if (player instanceof HumanPlayer && defence.getType() == player.getNumber()) {
                    if (Calculator.getDelta() > 0) {
                        GameScreen.colorForBorder = getBacklightLoseColor();
                        FXPlayer.playWin();
                    } else {
                        GameScreen.colorForBorder = getBacklightWinColor();
                        FXPlayer.playLose();
                    }
                    Vibrator.bzz();
                    return;
                } else {
                    if (Calculator.getDelta() > 0) {
                        FXPlayer.playWin();
                    }  else {
                        FXPlayer.playLose();
                    }
                }
            }
        }
    }

    public void riseDiceTooltips(Cell attack, Cell defense) {

        if (defense.isFree() || UNIT_SIZE * GestureController.getZoom() <= MIN_SIZE_FOR_TEXT ) {
            return;
        }

        String message = Calculator.getN() + "";
        BitmapFont font = AbstractDrawer.getBitmapFont();
        Color color;
        if (Calculator.getDelta() >= 0) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
        float tooltipX = calculateTooltipX(attack.getX());
        float tooltipY = calculateTooltipY(attack.getY());
        TooltipHandler.addTooltip(new Tooltip(message, font, color, tooltipX, tooltipY));

        message = Calculator.getM() + "";
        if (Calculator.getDelta() <= 0) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
        tooltipX = calculateTooltipX(defense.getX());
        tooltipY = calculateTooltipY(defense.getY());
        TooltipHandler.addTooltip(new Tooltip(message, font, color, tooltipX, tooltipY));

    }

    public void riseAddPowerTooltip(Cell cell) {
        if (UNIT_SIZE * GestureController.getZoom() > MIN_SIZE_FOR_TEXT) {
            BitmapFont font = AbstractDrawer.getBitmapFont();
            Color color = Color.GREEN;

            float tooltipX = calculateTooltipX(cell.getX());
            float tooltipY = calculateTooltipY(cell.getY());
            TooltipHandler.addTooltip(new Tooltip(ADD_POWER_TOOLTIP, font, color, tooltipX, tooltipY));
        }

    }

    private float calculateTooltipX(float cellX) {
        return getX() + cellX + UNIT_SIZE/2;
    }

    private float calculateTooltipY(float cellY) {
        return getY() + cellY + UNIT_SIZE/2;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        drawer.draw(this, batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

    public List<Cell> getConnectedCells(Cell cell) {
        List<Cell> list = new LinkedList<Cell>();

        for (int i = 0; i < CELLS_COUNT; i++) {
            Cell cellToAdd = cells.get(i);
            if (isCellsConnected(cell, cellToAdd)) {
                if (cellToAdd.isValid())
                    list.add(cellToAdd);
            }
        }

        return list;
    }

    public List<Cell> getConnectedEnemies(Cell cell) {
        List<Cell> enemies = new LinkedList<Cell>();
        for (Cell enemy : getConnectedCells(cell)) {
            if (enemy.getType() != cell.getType()) {
                enemies.add(enemy);
            }
        }
        return enemies;
    }

    public int calcScore(int type) {
        int score = 0;
        for (Cell cell : cells) {
            if (cell.isValid() && cell.getType() == type) {
                score += cell.getPower();
            }
        }
        return score;
    }
    /*
    public List<Cell> getCellsByType(int type) {
        List<Cell> list = new LinkedList<Cell>();
        for (Cell cell : cells) {
            if (cell.getType() == type) {
                list.add(cell);
            }
        }
        return list;
    }
    */

    public void moveBy(float deltaX, float deltaY) {
        float newX = getX() + deltaX;
        float newY = getY() + deltaY;

        //Logger.log("move: " + newX + "; " + newY);

        if (newX > initialX)
            newX = initialX;
        if (newY > initialY)
            newY = initialY;

        float minX = initialX - (getZoomedWidth() - initialWidth);
        float minY = initialY - (getZoomedHeight() - initialHeight);

        //Logger.log("zoomed: " + getZoomedWidth() + "; " + getZoomedHeight());
        //Logger.log("minimal: " + minX + "; " + minY);

        if (newX < minX)
            newX = minX;
        if (newY < minY)
            newY = minY;

        this.setX(newX);
        this.setY(newY);

        //Logger.log("actual move: " + newX + "; " + newY);
    }

    public void resize() {
        this.setWidth(getZoomedWidth());
        this.setHeight(getZoomedHeight());

        cellWidth = Field.getZoomedWidth() / MAX_CELLS_X;
        cellHeight = Field.getZoomedHeight() / MAX_CELLS_Y;

        float cellX;
        float cellY;

        for (Cell c : cells) {
            cellX = c.getUnitsY()*cellWidth;
            if (c.getUnitsX()%2 == 1) {
                cellX += cellHeight/2;
            }
            cellY = c.getUnitsX()*cellHeight - 8f;
            c.setX(cellX);
            c.setY(cellY);
        }

        moveBy(0,0);

    }

    public static float getZoomedWidth() {
        return WIDTH * GestureController.getZoom();
    }

    public static float getZoomedHeight() {
        return HEIGHT * GestureController.getZoom();

    }

    public void updateLists() {

        //Logger.log("update lists called.");

        Player[] players = pm.getPlayers();
        if (players != null) {
            for (Player player : players) {
                player.clearCells();
            }
        }

        for (Cell cell : cells) {
            cell.clearEnemies();
            cell.clearNeighbors();
            for (Cell cell2 : cells) {
                if (isCellsConnected(cell, cell2) && ( cell.getType() != cell2.getType() || cell.isFree() )) {
                    cell.addNeighbor(cell2);
                    cell.addEnemy(cell2);
                }
            }
            if (cell.getType() != -1) {
                pm.getPlayers()[cell.getType()].addCell(cell);
            }
        }

    }

    // Auto-generated

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public List<Cell> getCells() {return cells; }
}
