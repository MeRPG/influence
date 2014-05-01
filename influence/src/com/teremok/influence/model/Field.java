package com.teremok.influence.model;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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

import java.util.List;
import java.util.Random;

import static com.teremok.influence.view.Drawer.*;

/**
 * Created by Alexx on 11.12.13
 */
public class Field extends Group {

    public static float WIDTH =  480f;
    public static float HEIGHT = 624f;

    private static final int INITIAL_CELL_POWER = 2;

    private List<Cell> cells;

    private Match match;
    private PlayerManager pm;
    private Cell selectedCell;
    private Router router;
    private FieldShapeDrawer drawer;

    private GraphGenerator generator;
    private Random random;

    public static float cellWidth;
    public static float cellHeight;

    private float initialX;
    private float initialY;
    private float initialWidth;
    private float initialHeight;

    private int maxCellsY;
    private int maxCellsX;
    private int cellsCount;

    public Field(Match match, GameSettings settings) {
        reset(match, settings);
        addListener();
    }

    public Field(Match match, GameSettings settings, List<Cell> cells, Router router) {
        reset(match, settings, cells, router);
        addListener();
    }

    public void reset(Match match, GameSettings settings) {
        this.match = match;
        this.pm = match.getPm();
        drawer = AbstractDrawer.getFieldShapeDrawer();

        maxCellsX = settings.maxCellsX;
        maxCellsY = settings.maxCellsY;
        cellsCount = settings.cellsCount;

        initialX = 0f;
        initialY = AbstractScreen.HEIGHT - HEIGHT-1f;

        cellWidth = getUnitSize() *2;
        cellHeight = getUnitSize() *2;

        initialWidth = WIDTH;
        initialHeight = HEIGHT;

        setBounds(initialX, initialY, initialWidth, initialHeight);

        generate();
    }

    public void reset(Match match, GameSettings settings, List<Cell> cells, Router router) {
        this.match = match;
        this.pm = match.getPm();
        drawer = AbstractDrawer.getFieldShapeDrawer();

        maxCellsX = settings.maxCellsX;
        maxCellsY = settings.maxCellsY;
        cellsCount = settings.cellsCount;

        initialX = 0f;
        initialY = AbstractScreen.HEIGHT - HEIGHT-1f;

        initialWidth = WIDTH;
        initialHeight = HEIGHT;

        setBounds(initialX, initialY, initialWidth, initialHeight);

        this.cells = cells;
        this.router = router;
    }

    private void generate() {

        if (generator == null)
            generator = new GraphGenerator(cellsCount, maxCellsX, maxCellsY);
        generator.generate();
        cells = generator.getCells();
        router = generator.getRouter();
    }

    public void regenerate() {
        generate();
        updateLists();
    }

    public void placeStartPosition(int type) {
        if (random == null)
            random = new Random();
        int number;
        Cell target;

        do {
            number = random.nextInt(cells.size());
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
        if (startNumber < 0 || startNumber >= maxCellsY * maxCellsX -1) {
            startNumber = 0;
        }

        if (endNumber <= 0 || endNumber >= maxCellsY * maxCellsX -1) {
            endNumber = maxCellsY * maxCellsX -1;
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

        if (random == null)
            random = new Random();
        int number;
        Cell target;

        do {
            number = firstInRange + random.nextInt(range);
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

        if (Settings.gameSettings.fieldSize == FieldSize.SMALL)
            return target.isFree();

        if (target.isValid() && target.isFree()) {
            for (Cell enemy : target.getEnemies()) {
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

                        if (GameScreen.editor) {

                            switch (button) {
                                case Input.Buttons.LEFT:

                                    if (GameScreen.shifted) {
                                        if (GameScreen.fromRoute == -1) {
                                            GameScreen.fromRoute = target.getNumber();
                                        } else if (!router.routePossible(GameScreen.fromRoute, target.getNumber())){
                                            GameScreen.fromRoute = target.getNumber();
                                        } else {
                                            router.toggle(GameScreen.fromRoute, target.getNumber());
                                            updateLists();
                                        }
                                        return;
                                    }

                                    int pow = target.getPower();
                                    pow++;
                                    if (pow > target.getMaxPower()) {
                                        target.setPower(0);
                                    } else {
                                        target.setPower(pow);
                                    }
                                    break;
                                case Input.Buttons.RIGHT:

                                    int type = target.getType();
                                    type++;
                                    if (type >= Settings.gameSettings.getNumberOfPlayers()) {
                                        type = -1;
                                    }
                                    target.setType(type);

                                    break;
                                case Input.Buttons.MIDDLE:
                                    if (target.getMaxPower() == 8) {
                                        target.setMaxPower(12);
                                    } else {
                                        target.setMaxPower(8);
                                    }
                                    if (target.getPower() > target.getMaxPower()) {
                                        target.setPower(target.getMaxPower());
                                    }
                                    break;
                            }

                            updateLists();
                            return;
                        }
                        //Logger.log("actual target: " + target);
                        if (target == null)
                            return;

                        if (target.getType() == pm.current().getNumber() || connectedToSelected(target) && selectedCell.getPower() > 1)
                            FXPlayer.playClick();
                        if (match.isInAttackPhase()) {
                            setSelectedCell(target);
                        } else {
                            if (pm.current() instanceof HumanPlayer && target.getType() == pm.current().getNumber() && target.getPower() < target.getMaxPower()) {
                                HumanPlayer player = (HumanPlayer) pm.current();
                                player.addPowered(target.getNumber());
                                addPower(target);
                            }
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

        Logger.log("hit: " + unitsX + "; " + unitsY + "; " + getNum(unitsX,unitsY));

        if (unitsX < 0 || unitsX > maxCellsX -1 || unitsY < 0 || unitsY > maxCellsY -1) {
            cells.get(0);
        }

        return getCellByNumber(getNum(unitsX, unitsY));
    }

    private Cell getCellByNumber(int number) {
        for (Cell cell : cells) {
            if (cell.getNumber() == number){
                return  cell;
            }
        }
        return cells.get(0);
    }

    private int getNum( int i, int j) {
        return i + j* maxCellsX;
    }

    private boolean connectedToSelected(Cell cell) {
        if (selectedCell == null)
            return false;
        for (Cell c : cell.getNeighbors()) {
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
        addPower(cell, 1);
    }

    public void addPowerFull(Cell cell) {

        int powerToDistribute = pm.current().getPowerToDistribute();
        int delta = cell.getMaxPower() - cell.getPower();
        if (powerToDistribute > 0 && delta > 0) {
            int toAdd = delta < powerToDistribute ? delta : powerToDistribute;
            addPower(cell, toAdd);
        }
    }

    public void addPower(Cell cell, int powerToAdd) {
        cell.power += powerToAdd;
        riseAddPowerTooltip(cell, "+"+powerToAdd);
        pm.current().subtractPowerToDistribute(powerToAdd);
        Logger.log("Add " + powerToAdd + " power to " + cell);
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

        riseDiceTooltips(attack, defense);
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

            if (defense.getPower() > 0) {
                calcA--;
            }

        }



        attack.setPower(calcA);
        defense.setPower(calcB);
    }

    private boolean isCellsConnected(Cell from, Cell to) {

        if (from.getNumber() == to.getNumber())
            return false;

        return router.routeExist(from.getNumber(), to.getNumber());

    }

    // TODO: refactor
    public void fastShowBacklight(Cell attack, Cell defence) {

        if (pm.isHumanActing()) {

            if (Calculator.getDelta() > 0) {
                if (defence.getType() != -1) {
                    GameScreen.colorForBacklight = getBacklightWinColor();
                    FXPlayer.playWin();
                }
            } else {
                GameScreen.colorForBacklight = getBacklightLoseColor();
                FXPlayer.playLose();
            }
            if (defence.getPower() != 0) {
                Vibrator.bzz();
            }
        } else {
            for (Player player : pm.getPlayers()) {
                if (player instanceof HumanPlayer && defence.getType() == player.getNumber()) {
                    if (Calculator.getDelta() > 0) {
                        GameScreen.colorForBacklight = getBacklightLoseColor();
                        FXPlayer.playWin();
                    } else {
                        GameScreen.colorForBacklight = getBacklightWinColor();
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

        if (defense.isFree() || getUnitSize() * GestureController.getZoom() <= MIN_SIZE_FOR_TEXT ) {
            return;
        }

        String message;
        Color color;
        BitmapFont font = AbstractDrawer.getBitmapFont();
        float tooltipX, tooltipY;
        message = Calculator.getN() + "";
        if (Calculator.getDelta() >= 0) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
        tooltipX = calculateTooltipX(attack.getX());
        tooltipY = calculateTooltipY(attack.getY());

        if (isTooltipVisible(tooltipX, tooltipY)) {
            TooltipHandler.addTooltip(new Tooltip(message, font, color, tooltipX, tooltipY));
        }

        message = Calculator.getM() + "";
        if (Calculator.getDelta() <= 0) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
        tooltipX = calculateTooltipX(defense.getX());
        tooltipY = calculateTooltipY(defense.getY());
        if (isTooltipVisible(tooltipX, tooltipY)) {
            TooltipHandler.addTooltip(new Tooltip(message, font, color, tooltipX, tooltipY));
        }
    }

    public void riseAddPowerTooltip(Cell cell, String tooltip) {

        if (getUnitSize() * GestureController.getZoom() <= MIN_SIZE_FOR_TEXT ) {
            return;
        }

        BitmapFont font = AbstractDrawer.getBitmapFont();
        Color color = Color.GREEN;

        float tooltipX = calculateTooltipX(cell.getX());
        float tooltipY = calculateTooltipY(cell.getY());

        if (isTooltipVisible(tooltipX, tooltipY)) {
            TooltipHandler.addTooltip(new Tooltip(tooltip, font, color, tooltipX, tooltipY));
        }
    }

    public boolean isTooltipVisible (float tooltipX, float tooltipY) {
        return  tooltipX > -5
                && tooltipX < AbstractScreen.WIDTH
                && tooltipY > (AbstractScreen.HEIGHT - Field.HEIGHT-5)
                && tooltipY < AbstractScreen.HEIGHT;
    }

    public boolean isCellVisible(Cell cell) {
        float absoluteCellX = cell.getX() + getX();
        float absoluteCellY = cell.getY() + getY();
        float actualCellWidth = cellWidth*GestureController.getZoom();
        return  absoluteCellX > (-actualCellWidth/2)
                && absoluteCellX < AbstractScreen.WIDTH
                && absoluteCellY > (AbstractScreen.HEIGHT - Field.HEIGHT-actualCellWidth/2)
                && absoluteCellY < AbstractScreen.HEIGHT;
    }

    private float calculateTooltipX(float cellX) {
        return getX() + cellX + getUnitSize() /2;
    }

    private float calculateTooltipY(float cellY) {
        return getY() + cellY + getUnitSize() /2;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        drawer.draw(this, batch, parentAlpha);
        super.draw(batch, parentAlpha);
    }

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

        cellWidth = Field.getZoomedWidth() / maxCellsX;
        cellHeight = Field.getZoomedHeight() / maxCellsY;

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

        Logger.log("update lists called.");

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
                if (isCellsConnected(cell, cell2)) {
                    cell.addNeighbor(cell2);
                    if (( cell.getType() != cell2.getType() || cell.isFree() )) {
                        cell.addEnemy(cell2);
                    }
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

    public int getMaxCellsX() {
        return maxCellsX;
    }

    public int getMaxCellsY() {
        return maxCellsY;
    }

    public int getCellsCount() {
        return cellsCount;
    }

    public Router getRouter() {
        return router;
    }
}
