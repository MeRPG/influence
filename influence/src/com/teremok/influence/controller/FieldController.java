package com.teremok.influence.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.model.*;
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
public class FieldController extends Group {
    private static final int INITIAL_CELL_POWER = 2;

    private Match match;
    private PlayerManager pm;
    private FieldShapeDrawer drawer;

    public Cell selectedCell;
    FieldModel model;

    public static float cellWidth;
    public static float cellHeight;

    private GraphGenerator generator;
    private Random random;

    public FieldController(Match match, GameSettings settings) {
        model = new FieldModel();
        reset(match, settings);
        addListener();
    }

    public FieldController(Match match, GameSettings settings, List<Cell> cells, Router router) {
        model = new FieldModel();
        reset(match, settings, cells, router);
        addListener();
    }

    public void reset(Match match, GameSettings settings) {
        this.match = match;
        this.pm = match.getPm();

        drawer = AbstractDrawer.getFieldShapeDrawer();

        selectedCell = null;

        cellWidth = getUnitSize() *2;
        cellHeight = getUnitSize() *2;

        model.reset(settings);

        setBounds(model.initialX, model.initialY, model.initialWidth, model.initialHeight);

        generate();
    }

    public void reset(Match match, GameSettings settings, List<Cell> cells, Router router) {
        this.match = match;
        this.pm = match.getPm();
        drawer = AbstractDrawer.getFieldShapeDrawer();

        selectedCell = null;

        cellWidth = getUnitSize() *2;
        cellHeight = getUnitSize() *2;

        model.reset(settings, cells, router);

        setBounds(model.initialX, model.initialY, model.initialWidth, model.initialHeight);
    }

    private void generate() {
        if (generator == null || ! generator.matchModel(model))
            generator = new GraphGenerator(model);
        generator.generate(model);
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
            number = random.nextInt(model.cells.size());
            target = model.cells.get(number);
            if (isValidForStartPosition(target)) {
                break;
            }
        } while (true);
        target.setPower(INITIAL_CELL_POWER);
        target.setType(type);
    }

    public void placeStartPositionFromRange(int type, int startNumber, int endNumber) {
        if (startNumber < 0 || startNumber >= model.maxCellsY * model.maxCellsX -1) {
            startNumber = 0;
        }

        if (endNumber <= 0 || endNumber >= model.maxCellsY * model.maxCellsX -1) {
            endNumber = model.maxCellsY * model.maxCellsX -1;
        }

        if (endNumber < startNumber) {
            int tmp = startNumber;
            startNumber = endNumber;
            endNumber = tmp;
        }

        int firstInRange = 0;
        int range = 0;
        for (Cell cell : model.cells) {
            if (cell.getNumber() >= endNumber)
                break;
            if (cell.getNumber() < startNumber) {
                firstInRange++;
            } else {
                range++;
            }
        }

        if (random == null)
            random = new Random();

        int number;
        Cell target;

        do {
            number = firstInRange + random.nextInt(range);
            target = model.cells.get(number);
            if (isValidForStartPosition(target)) {
                break;
            }
        } while (true);

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
        Logger.log("hit field on " + x + "; " + y);
        int unitsY = (int)(y/cellHeight);

        if (unitsY%2 == 1) {
            x -= cellWidth/2;
        }

        int unitsX = (int)(x/cellWidth);

        Logger.log("hit: " + unitsX + "; " + unitsY + "; " + model.getNum(unitsX, unitsY));

        if (unitsX < 0 || unitsX > model.maxCellsX -1 || unitsY < 0 || unitsY > model.maxCellsY -1) {
            model.cells.get(0);
        }

        return model.getCell(unitsX, unitsY);
    }

    private boolean connectedToSelected(Cell cell) {
        return selectedCell != null && model.isCellsConnected(selectedCell, cell);
    }

    public void setSelectedCell(Cell cell) {

        if (selectedCell != null) {
            if (model.isCellsConnected(selectedCell, cell)
                    && selectedCell.getPower() > 1
                    && selectedCell.getType() != cell.getType()) {

                int delta = fight(selectedCell, cell);

                if (delta > 0) {
                    cell.setType(selectedCell.getType());
                    reallySetSelected(cell);
                    updateLists();
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
        cell.setPower(powerToAdd + cell.getPower());
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
        }
    }

    public void resetSelection() {
        if (selectedCell != null) {
            selectedCell.setSelected(false);
            selectedCell = null;
        }
    }

    private int fight(Cell attack, Cell defense) {

        int delta = Calculator.fight(attack.getPower(), defense.getPower());

        riseDiceTooltips(attack, defense);
        fastShowBacklight(attack, defense);
        setResultPower(attack, defense);

        if (pm.getNumberOfHumans() == 1) {
            if (attack.getType() == 0) {
                if (defense.getType() != -1) {
                    Chronicle.match.damage += Calculator.getN();
                    Chronicle.match.damageGet += Calculator.getM();
                }
                if (delta > 0)
                    Chronicle.match.cellsConquered++;
            }

            if (defense.getType() == 0) {
                Chronicle.match.damageGet += Calculator.getN();
                Chronicle.match.damage += Calculator.getM();
                if (delta > 0)
                    Chronicle.match.cellsLost++;
            }
        }

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
                && tooltipY > (AbstractScreen.HEIGHT - FieldModel.HEIGHT-5)
                && tooltipY < AbstractScreen.HEIGHT;
    }

    public boolean isCellVisible(Cell cell) {
        float absoluteCellX = cell.getX() + getX();
        float absoluteCellY = cell.getY() + getY();
        float actualCellWidth = cellWidth*GestureController.getZoom();
        return  absoluteCellX > (-actualCellWidth/2)
                && absoluteCellX < AbstractScreen.WIDTH
                && absoluteCellY > (AbstractScreen.HEIGHT - FieldModel.HEIGHT-actualCellWidth/2)
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

        if (newX > model.initialX)
            newX = model.initialX;
        if (newY > model.initialY)
            newY = model.initialY;

        float minX = model.initialX - (getZoomedWidth() - model.initialWidth);
        float minY = model.initialY - (getZoomedHeight() - model.initialHeight);

        if (newX < minX)
            newX = minX;
        if (newY < minY)
            newY = minY;

        this.setX(newX);
        this.setY(newY);
    }

    public void resize() {
        this.setWidth(getZoomedWidth());
        this.setHeight(getZoomedHeight());

        cellWidth = FieldController.getZoomedWidth() / model.maxCellsX;
        cellHeight = FieldController.getZoomedHeight() / model.maxCellsY;

        float cellX;
        float cellY;

        for (Cell c : model.cells) {
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
        return FieldModel.WIDTH * GestureController.getZoom();
    }

    public static float getZoomedHeight() {
        return FieldModel.HEIGHT * GestureController.getZoom();

    }

    public void updateLists() {

        Logger.log("update lists called.");

        Player[] players = pm.getPlayers();
        if (players != null) {
            for (Player player : players) {
                player.clearCells();
            }
        }

        model.updateLists();

        for (Cell cell : model.cells) {
            if (cell.getType() != -1) {
                pm.getPlayers()[cell.getType()].addCell(cell);
            }
        }
    }

    // Auto-generated

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public FieldModel getModel() {
        return model;
    }
}
