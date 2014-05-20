package com.teremok.influence.model.player;

import com.teremok.influence.model.Cell;
import com.teremok.influence.model.Match;
import com.teremok.influence.model.player.strategy.AttackStrategy;
import com.teremok.influence.model.player.strategy.EnemyStrategy;
import com.teremok.influence.model.player.strategy.PowerStrategy;
import com.teremok.influence.model.player.strategy.attack.DummyAttackStrategy;
import com.teremok.influence.model.player.strategy.enemy.DummyEnemyStrategy;
import com.teremok.influence.model.player.strategy.power.DummyPowerStrategy;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Alexx on 05.02.14
 */
public class Strategist extends ComputerPlayer {

    public Strategist(int type, Match match) {
        super(type, match);
    }

    AttackStrategy attackStrategy;
    EnemyStrategy enemyStrategy;
    PowerStrategy powerStrategy;

    @Override
    protected void actAttackLogic(float delta) {

        initDummyStrategies();

        super.actAttackLogic(delta);

        attackStrategy.cleanUp();
        enemyStrategy.cleanUp();
        powerStrategy.cleanUp();
    }

    private void initDummyStrategies(){
        if (attackStrategy == null) {
            attackStrategy = new DummyAttackStrategy();
        }
        if (enemyStrategy == null) {
            enemyStrategy = new DummyEnemyStrategy();
        }
        if (powerStrategy == null) {
            powerStrategy = new DummyPowerStrategy();
        }
    }

    @Override
    protected void prepareActions() {
        Cell cell = attackStrategy.execute(null, cells, field.getModel(), this);
        if (cell != null && cell.getPower() > 1) {
            List<Cell> enemies = cell.getEnemies();
            if (! enemies.isEmpty()) {
                Cell enemy = enemyStrategy.execute(cell, enemies, field.getModel(), this);
                nextMove = new Move(cell, enemy);
            }
        }
    }

    @Override
    protected void actPowerLogic(float delta) {
        powerStrategy.prepare(this);
        Map<Cell, Integer> powerMap = powerStrategy.execute(null, cells, field.getModel(), this);
        for (Cell cell : powerMap.keySet()) {
            field.addPower(cell, powerMap.get(cell));
        }
    }

    // Auto-generated
    public Random getRnd() {
        return rnd;
    }

    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public void setEnemyStrategy(EnemyStrategy enemyStrategy) {
        this.enemyStrategy = enemyStrategy;
    }

    public void setPowerStrategy(PowerStrategy powerStrategy) {
        this.powerStrategy = powerStrategy;
    }
}
