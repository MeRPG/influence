package com.teremok.influence.model.player;

import com.teremok.influence.model.Match;
import com.teremok.influence.model.player.strategy.attack.BeefyAttackStrategy;
import com.teremok.influence.model.player.strategy.attack.RandomAttackStrategy;
import com.teremok.influence.model.player.strategy.enemy.LazyEnemyStrategy;
import com.teremok.influence.model.player.strategy.enemy.RandomEnemyStrategy;
import com.teremok.influence.model.player.strategy.power.RandomPowerStrategy;
import com.teremok.influence.model.player.strategy.power.SmartyPowerStrategy;

/**
 * Created by Alexx on 06.02.14
 */
public class PlayerFactory {

    public static Player getByType(PlayerType type, int number, Match match) {
        Player player;

        switch (type) {
            case Dummy:
                player = getDummy(number, match);
                break;
            case Beefy:
                player = getBeefy(number, match);
                break;
            case Lazy:
                player = getLazy(number, match);
                break;
            case Smarty:
                player = getSmarty(number, match);
                break;
            case Random:
                player = getRandomizer(number, match);
                break;
            case Hunter:
                player = getHunter(number, match);
                break;
            case Human:
                player = getHuman(number, match);
                break;
            default:
                player = getHuman(number, match);

        }
        player.setType(type);
        return player;
    }

    public static Player getHuman(int number, Match match) {
        return new HumanPlayer(number, match);
    }

    public static Player getDummy(int number, Match match) {
        return new Strategist(number, match);
    }

    public static Strategist getBeefy(int number, Match match) {
        Strategist beefy = new Strategist(number, match);
        beefy.setAttackStrategy(new BeefyAttackStrategy());
        return  beefy;
    }

    public static Strategist getLazy(int number, Match match) {
        Strategist lazy = new Strategist(number, match);
        lazy.setEnemyStrategy(new LazyEnemyStrategy());
        return  lazy;
    }

    public static Strategist getSmarty(int number, Match match) {
        Strategist smarty = new Strategist(number, match);
        smarty.setPowerStrategy(new SmartyPowerStrategy());
        return smarty;
    }

    public static Player getHunter(int number, Match match) {
        Strategist hunter = new Strategist(number, match);
        hunter.setAttackStrategy(new BeefyAttackStrategy());
        hunter.setEnemyStrategy(new LazyEnemyStrategy());
        hunter.setPowerStrategy(new SmartyPowerStrategy());
        return hunter;
    }

    public static Strategist getRandomizer(int number, Match match) {
        Strategist randomizer = new Strategist(number, match);
        randomizer.setAttackStrategy(new RandomAttackStrategy());
        randomizer.setEnemyStrategy(new RandomEnemyStrategy());
        randomizer.setPowerStrategy(new RandomPowerStrategy());
        return randomizer;
    }
}
