package com.teremok.influence.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.util.Vibrator;
import com.teremok.influence.Influence;
import com.teremok.influence.model.*;
import com.teremok.influence.model.player.HumanPlayer;
import com.teremok.influence.model.player.Player;
import com.teremok.influence.model.player.PlayerManager;
import com.teremok.framework.screen.AbstractScreen;
import com.teremok.influence.screen.GameScreen;
import com.teremok.influence.ui.TooltipHandler;
import com.teremok.influence.util.*;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.view.Drawer;
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
    private Chronicle.MatchChronicle matchChronicle;

    public Cell selectedCell;
    FieldModel model;

    GameSettings gameSettings;

    public static float cellWidth;
    public static float cellHeight;

    private GraphGenerator generator;
    private Random random;
    private Calculator calculator;

    // TODO Не должно тут быть этого класса...
    private FXPlayer fxPlayer;

    public FieldController(Match match, GameSettings gameSettings) {
        model = new FieldModel();
        fxPlayer = ((Influence) Gdx.app.getApplicationListener()).getFXPlayer();
        reset(match, gameSettings);
        addListener();
    }

    public FieldController(Match match, GameSettings gameSettings, List<Cell> cells, Router router) {
        model = new FieldModel();
        fxPlayer = ((Influence) Gdx.app.getApplicationListener()).getFXPlayer();
        reset(match, gameSettings, cells, router);
        addListener();
    }

    public void reset(Match match, GameSettings gameSettings) {
        this.match = match;
        this.pm = match.getPm();
        this.gameSettings = gameSettings;

        drawer = AbstractDrawer.getFieldShapeDrawer();

        selectedCell = null;

        cellWidth = getUnitSize(gameSettings.maxCellsY) *2;
        cellHeight = getUnitSize(gameSettings.maxCellsY) *2;

        model.reset(gameSettings);
        if (calculator == null)
            calculator = new Calculator();

        setBounds(model.initialX, model.initialY, model.initialWidth, model.initialHeight);

        generate();
    }

    public void reset(Match match, GameSettings gameSettings, List<Cell> cells, Router router) {
        this.match = match;
        this.pm = match.getPm();
        this.gameSettings = gameSettings;
        drawer = AbstractDrawer.getFieldShapeDrawer();

        selectedCell = null;

        cellWidth = getUnitSize(gameSettings.maxCellsY) *2;
        cellHeight = getUnitSize(gameSettings.maxCellsY) *2;

        if (calculator == null)
            calculator = new Calculator();

        model.reset(gameSettings, cells, router);

        setBounds(model.initialX, model.initialY, model.initialWidth, model.initialHeight);
    }

    private void generate() {
        if (generator == null || ! generator.matchModel(model))
            generator = new GraphGenerator(model);
        generator.generate(model);
    }

    public void placeStartPosition(Player player) {
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
        target.setType(player.getNumber());
    }

    private boolean isValidForStartPosition(Cell target) {

        if (gameSettings.fieldSize == FieldSize.SMALL)
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
                    Gdx.app.debug(getClass().getSimpleName(), "Field - touchDown");
                    return event.getType().equals(InputEvent.Type.touchDown);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (!event.isHandled() && ! GestureController.isActing() && match.canHumanActing()) {

                        Cell target = hit(x, y);

                        if (target == null)
                            return;


                        if (target.getType() == pm.current().getNumber() || connectedToSelected(target) && selectedCell.getPower() > 1) {
                            fxPlayer.playClick();
                        }
                        if (match.isInAttackPhase()) {
                            setSelectedCell(target);
                        } else {
                            if (pm.current() instanceof HumanPlayer && target.getType() == pm.current().getNumber() && target.getPower() < target.getMaxPower()) {
                                HumanPlayer player = (HumanPlayer) pm.current();
                                player.addPowered(target.getNumber());
                                addPower(target);
                            }
                        }
                        Gdx.app.debug(getClass().getSimpleName(), "Field - touchUp ");
                    }
                }
        });

        this.setTouchable(Touchable.enabled);
    }

    public Cell hit(float x, float y) {
        Gdx.app.debug(getClass().getSimpleName(), "hit field on " + x + "; " + y);
        int unitsY = (int)(y/cellHeight);

        if (unitsY%2 == 1) {
            x -= cellWidth/2;
        }

        int unitsX = (int)(x/cellWidth);

        Gdx.app.debug(getClass().getSimpleName(), "hit: " + unitsX + "; " + unitsY + "; " + model.getNum(unitsX, unitsY));

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
        Gdx.app.debug(getClass().getSimpleName(), "Add " + powerToAdd + " power to " + cell);
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

        int delta = calculator.fight(attack.getPower(), defense.getPower());

        riseDiceTooltips(attack, defense);
        fastShowBacklight(attack, defense);
        setResultPower(attack, defense);

        if (pm.getNumberOfHumans() == 1) {
            if (attack.getType() == 0) {
                if (defense.getType() != -1) {
                    matchChronicle.damage += calculator.getResultPowerA();
                    //Chronicle.match.damageGet += calculator.getResultPowerB();
                }
                if (delta > 0)
                    matchChronicle.cellsConquered++;
            }

            if (defense.getType() == 0) {
                matchChronicle.damageGet += calculator.getResultPowerA();
                //Chronicle.match.damage += calculator.getResultPowerB();
                if (delta > 0)
                    matchChronicle.cellsLost++;
            }
        }

        return delta;
    }

    private void setResultPower(Cell attack, Cell defense) {
        int calcA = calculator.getResultPowerA();
        int calcB = calculator.getResultPowerB();

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

            if (calculator.getDelta() > 0) {
                if (defence.getType() != -1) {
                    GameScreen.colorForBacklight = getBacklightWinColor();
                    fxPlayer.playWin();
                }
            } else {
                GameScreen.colorForBacklight = getBacklightLoseColor();
                fxPlayer.playLose();
            }
            if (defence.getPower() != 0) {
                Vibrator.bzz();
            }
        } else {
            for (Player player : pm.getPlayers()) {
                if (player instanceof HumanPlayer && defence.getType() == player.getNumber()) {
                    if (calculator.getDelta() > 0) {
                        GameScreen.colorForBacklight = getBacklightLoseColor();
                        fxPlayer.playWin();
                    } else {
                        GameScreen.colorForBacklight = getBacklightWinColor();
                        fxPlayer.playLose();
                    }
                    Vibrator.bzz();
                    return;
                } else {
                    if (calculator.getDelta() > 0) {
                        fxPlayer.playWin();
                    }  else {
                        fxPlayer.playLose();
                    }
                }
            }
        }
    }

    public void riseDiceTooltips(Cell attack, Cell defense) {

        if (defense.isFree() || getUnitSize(gameSettings.maxCellsY) * GestureController.getZoom() <= MIN_SIZE_FOR_TEXT ) {
            return;
        }

        String message;
        Color color;
        float tooltipX, tooltipY;
        message = calculator.getN() + "";
        if (calculator.getDelta() >= 0) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
        tooltipX = calculateTooltipX(attack.getX());
        tooltipY = calculateTooltipY(attack.getY());

        if (isCellVisible(attack)) {
            TooltipHandler.getInstance().addTooltip(message, color, tooltipX, tooltipY, Drawer.getUnitSize(model.maxCellsY));
        }

        message = calculator.getM() + "";
        if (calculator.getDelta() <= 0) {
            color = Color.GREEN;
        } else {
            color = Color.RED;
        }
        tooltipX = calculateTooltipX(defense.getX());
        tooltipY = calculateTooltipY(defense.getY());
        if (isCellVisible(defense)) {
            TooltipHandler.getInstance().addTooltip(message, color, tooltipX, tooltipY, Drawer.getUnitSize(model.maxCellsY));
        }
    }

    public void riseAddPowerTooltip(Cell cell, String tooltip) {

        if (getUnitSize(gameSettings.maxCellsY) * GestureController.getZoom() <= MIN_SIZE_FOR_TEXT ) {
            return;
        }

        Color color = Color.GREEN;

        float tooltipX = calculateTooltipX(cell.getX());
        float tooltipY = calculateTooltipY(cell.getY());

        if (isCellVisible(cell)) {
            TooltipHandler.getInstance().addTooltip(tooltip, color, tooltipX, tooltipY, Drawer.getUnitSize(model.maxCellsY));
        }
    }

    public boolean isCellVisible(Cell cell) {

        float absoluteCellX = cell.getX() + getX();
        float absoluteCellY = cell.getY() + getY();
        float actualCellWidth = cellWidth*GestureController.getZoom();
        if (absoluteCellX > (-actualCellWidth/2)
                && absoluteCellX < AbstractScreen.WIDTH
                && absoluteCellY > (AbstractScreen.HEIGHT - FieldModel.HEIGHT-actualCellWidth/2)
                && absoluteCellY < AbstractScreen.HEIGHT ) {
            if (gameSettings.darkness) {
                if (isCellOwnedByActingHuman(cell)) {
                    return true;
                }
                for (Cell enemy : cell.getEnemies()) {
                    if (isCellOwnedByActingHuman(enemy)) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean isCellOwnedByActingHuman(Cell cell) {
        Player owner = getCellOwner(cell);
        return owner instanceof HumanPlayer && owner == pm.getLastHumanPlayer();
    }

    private Player getCellOwner(Cell cell) {
        return cell.getType() == -1 ? null : pm.getPlayers()[cell.getType()];
    }

    private float calculateTooltipX(float cellX) {
        return getX() + cellX + getUnitSize(gameSettings.maxCellsY) /2;
    }

    private float calculateTooltipY(float cellY) {
        return getY() + cellY + getUnitSize(gameSettings.maxCellsY) /2;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
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

        Gdx.app.debug(getClass().getSimpleName(), "update lists called.");

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

    public void setMatchChronicle(Chronicle.MatchChronicle matchChronicle) {
        this.matchChronicle = matchChronicle;
    }
}
