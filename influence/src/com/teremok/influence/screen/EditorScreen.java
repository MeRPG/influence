package com.teremok.influence.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.teremok.influence.controller.FieldController;
import com.teremok.influence.controller.MatchSaver;
import com.teremok.influence.model.Cell;
import com.teremok.influence.model.FieldModel;
import com.teremok.influence.model.Match;
import com.teremok.influence.model.Settings;
import com.teremok.influence.ui.TexturePanel;
import com.teremok.influence.view.AbstractDrawer;
import com.teremok.influence.view.Drawer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Алексей on 17.05.2014
 */
public class EditorScreen extends StaticScreen {

    TexturePanel borderTop;
    TexturePanel borderRight;
    TexturePanel borderBottom;
    TexturePanel borderLeft;

    TexturePanel backlight;

    Match match;
    FieldController field;

    FieldModel fieldModel;

    public List<Cell> hiddenCells;

    boolean shifted;
    int fromRoute;


    public EditorScreen(Game game, String filename) {
        super(game, filename);

    }

    void initBorders() {
        borderTop = new TexturePanel(uiElements.get("borderTop"));
        borderTop.setColor(1f, 1f, 1f, 0f);

        borderRight = new TexturePanel(uiElements.get("borderRight"));
        borderRight.setColor(1f, 1f, 1f, 0f);

        borderBottom = new TexturePanel(uiElements.get("borderBottom"));
        borderBottom.setColor(1f, 1f, 1f, 0f);

        borderLeft = new TexturePanel(uiElements.get("borderLeft"));
        borderLeft.setColor(1f, 1f, 1f, 0f);
    }

    void initBacklight() {
        backlight = new TexturePanel(uiElements.get("backlight"));
        backlight.setColor(1f, 1f, 1f, 0f);
    }

    @Override
    protected void addActors() {
        Drawer.MIN_SIZE_FOR_TEXT = 5;
        AbstractDrawer.setBitmapFont(getFont());
        switch (Settings.gameSettings.fieldSize) {
            case SMALL:
                Settings.gameSettings.cellsCount = 18;
                break;
            case NORMAL:
                Settings.gameSettings.cellsCount = 32;
                break;
            case LARGE:
                Settings.gameSettings.cellsCount = 65;
                break;
            case XLARGE:
                Settings.gameSettings.cellsCount = 133;
                break;
        }
        match = new Match(Settings.gameSettings);
        field = match.getFieldController();
        field.setTouchable(Touchable.disabled);
        fieldModel = field.getModel();
        loadHiddenCells();
        initBorders();
        initBacklight();
        stage.addActor(field);
    }

    @Override
    protected void addListeners() {
        stage.addListener( new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Logger.log("Editor - touchDown");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (event.isHandled())
                    return;
                event.handle();
                //Logger.log("Editor - touchUp on " + x + "; " + y);
                Cell target = field.hit(x-fieldModel.initialX, y-fieldModel.initialY);
                Cell hidden = hitHidden(x-fieldModel.initialX, y-fieldModel.initialY);
                switch (button) {
                    case Input.Buttons.LEFT:

                        if (target == null && hidden != null) {
                            showCell(hidden);
                            field.updateLists();
                            return;
                        }
                        if (target == null)
                            return;

                        if (shifted) {
                            if (fromRoute == -1) {
                                fromRoute = target.getNumber();
                            } else if (!fieldModel.router.routePossible(fromRoute, target.getNumber())){
                                fromRoute = target.getNumber();
                            } else {
                                fieldModel.router.toggle(fromRoute, target.getNumber());
                                field.updateLists();
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
                        if (target != null) {
                            if (shifted) {
                                hideCell(target);
                                field.updateLists();
                                return;
                            }

                            int type = target.getType();
                            type++;
                            if (type >= Settings.gameSettings.getNumberOfPlayers()) {
                                type = -1;
                            }
                            target.setType(type);
                        }
                        break;
                    case Input.Buttons.MIDDLE:
                        if (target != null) {
                            if (target.getMaxPower() == 8) {
                                target.setMaxPower(12);
                            } else {
                                target.setMaxPower(8);
                            }
                            if (target.getPower() > target.getMaxPower()) {
                                target.setPower(target.getMaxPower());
                            }
                        }
                        break;
                }
                field.updateLists();
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                //Logger.log("keyUp: " + keycode);
                if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) {
                    shifted = false;
                    fromRoute = -1;
                }
                if (keycode == Input.Keys.S) {
                    clearRoutes();
                    MatchSaver.save(match);
                }
                if (keycode == Input.Keys.D) {
                    match = MatchSaver.load();
                    field = match.getFieldController();
                    field.setTouchable(Touchable.disabled);
                    fieldModel = field.getModel();

                    stage.getRoot().clearChildren();
                    stage.addActor(field);

                    loadHiddenCells();
                }
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                //Logger.log("keyDown: " + keycode);
                if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) {
                    shifted = true;
                }
                return true;
            }
        });
    }

    public void loadHiddenCells() {
        hiddenCells = new LinkedList<>();
        for (int i = 0; i < fieldModel.maxCellsY; i++) {
            for (int j = 0; j < fieldModel.maxCellsX; j++) {
                Cell cell = Cell.makeEmptyCell(fieldModel.getNum(i, j), i, j);
                if (! fieldModel.cells.contains(cell)) {
                    hiddenCells.add(cell);
                }
            }
        }
    }

    void showCell(Cell cell) {
        fieldModel.cells.add(cell);
        swapUnitsCoords(cell);
        hiddenCells.remove(cell);
        fieldModel.cellsCount++;
    }

    void hideCell(Cell cell) {
        hiddenCells.add(cell);
        swapUnitsCoords(cell);
        fieldModel.cells.remove(cell);
        fieldModel.cellsCount--;
        fieldModel.router.disableForNumber(cell.getNumber());
    }

    public void clearRoutes() {
        for (Cell cell : hiddenCells) {
            fieldModel.router.removeForNumber(cell.getNumber());
        }
    }

    void removeRoutes(Cell cell) {
        fieldModel.router.removeForNumber(cell.getNumber());
    }

    void swapUnitsCoords(Cell cell) {
        int tmp = cell.getUnitsX();
        cell.setUnitsX(cell.getUnitsY());
        cell.setUnitsY(tmp);

    }

    public Cell hitHidden(float x, float y) {
        //Logger.log("hit hidden on " + x + "; " + y);
        int unitsY = (int)(y/ FieldController.cellHeight);

        if (unitsY%2 == 1) {
            x -= FieldController.cellWidth/2;
        }

        int unitsX = (int)(x/ FieldController.cellWidth);

        if (unitsX < 0 || unitsX > fieldModel.maxCellsX -1 || unitsY < 0 || unitsY > fieldModel.maxCellsY -1) {
            fieldModel.cells.get(0);
        }

        return getHiddenCellByNumber(fieldModel.getNum(unitsX, unitsY));
    }

    private Cell getHiddenCellByNumber(int number) {
        if (hiddenCells != null)
            for (Cell cell : hiddenCells) {
                if (cell.getNumber() == number){
                    return  cell;
                }
            }
        return null;
    }
}
