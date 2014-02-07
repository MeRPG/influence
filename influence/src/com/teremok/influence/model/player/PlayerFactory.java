package com.teremok.influence.model.player;

import com.teremok.influence.model.Match;
import com.teremok.influence.model.player.strategy.attack.BeefyAttackStrategy;
import com.teremok.influence.model.player.strategy.attack.RandomAttackStrategy;
import com.teremok.influence.model.player.strategy.enemy.RandomEnemyStrategy;
import com.teremok.influence.model.player.strategy.power.RandomPowerStrategy;
import com.teremok.influence.model.player.strategy.power.SmartyPowerStrategy;
import com.teremok.influence.model.player.strategy.enemy.LazyEnemyStrategy;

/**
 * Created by Alexx on 06.02.14
 */
public class PlayerFactory {

    public static Player getDummy(int type, Match match) {
        return new Strategist(type, match);
    }

    public static Strategist getBeefy(int type, Match match) {
        Strategist beefy = new Strategist(type, match);
        beefy.setAttackStrategy(new BeefyAttackStrategy());
        return  beefy;
    }

    public static Strategist getLazy(int type, Match match) {
        Strategist lazy = new Strategist(type, match);
        lazy.setEnemyStrategy(new LazyEnemyStrategy());
        return  lazy;
    }

    public static Strategist getSmarty(int type, Match match) {
        Strategist smarty = new Strategist(type, match);
        smarty.setPowerStrategy(new SmartyPowerStrategy());
        return smarty;
    }

    public static Player getHunter(int type, Match match) {
        Strategist hunter = new Strategist(type, match);
        hunter.setAttackStrategy(new BeefyAttackStrategy());
        hunter.setEnemyStrategy(new LazyEnemyStrategy());
        hunter.setPowerStrategy(new SmartyPowerStrategy());
        return hunter;
    }

    public static Strategist getRandomizer(int type, Match match) {
        Strategist randomizer = new Strategist(type, match);
        randomizer.setAttackStrategy(new RandomAttackStrategy());
        randomizer.setEnemyStrategy(new RandomEnemyStrategy());
        randomizer.setPowerStrategy(new RandomPowerStrategy());
        return randomizer;
    }
}
