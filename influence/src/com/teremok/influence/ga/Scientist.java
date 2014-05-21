package com.teremok.influence.ga;

import com.teremok.influence.screen.ScreenController;
import com.teremok.influence.util.Logger;

import java.util.Random;

/**
 * Created by Алексей on 20.05.2014
 */
public class Scientist {

    private static int MATCHES_PER_UNIT = 3;
    private static int POPULATION_SIZE = 20;
    private static int GENERATIONS = 10;

    private static float MUTATION = 0.25f;
    private static float MUTATION_STEP = MUTATION / GENERATIONS;

    private static Unit[] population = new Unit[POPULATION_SIZE];
    private static float[] results = new float[POPULATION_SIZE];
    private static int current = 0;
    private static int matches = 0;
    private static int generation = 0;

    public static int maxCurrentScore = 0;

    private static float averageScore = 0;
    private static float lastAverageScore = 0;

    public static boolean END = false;

    public static class Unit {
        public float A_ENEMIES_NUMBER_COEF = 0.25f;
        public float A_POWER_COEF = 1.0f;

        public float E_POWER_COEF = 1.0f;
        public float E_NEAR_POWER_COEF = 0.1f;

        public float P_INITIAL_BID = 0.1f;
        public float P_POWER_COEF = 0.75f;
        public float P_ENEMIES_NUMBER_COEF = 0.50f;
        public float P_EMPTY_ENEMY_COEF = 1.0f;

        public static Unit getRandomUnit() {
            Random random = new Random();
            Unit newUnit = new Unit();
            newUnit.A_ENEMIES_NUMBER_COEF = random.nextFloat();
            newUnit.A_POWER_COEF = random.nextFloat();

            newUnit.E_NEAR_POWER_COEF = random.nextFloat();
            newUnit.E_POWER_COEF = random.nextFloat();

            newUnit.P_ENEMIES_NUMBER_COEF = random.nextFloat();
            newUnit.P_EMPTY_ENEMY_COEF = random.nextFloat();
            newUnit.P_INITIAL_BID = random.nextFloat();
            newUnit.P_POWER_COEF = random.nextFloat();

            return newUnit;
        }

        public static Unit getPredefinedUnit() {
            Unit newUnit = new Unit();
            newUnit.A_ENEMIES_NUMBER_COEF = 0.34879237f;
            newUnit.A_POWER_COEF = 0.2554891f;

            newUnit.E_NEAR_POWER_COEF = 0.09971225f;
            newUnit.E_POWER_COEF = 0.4240656f;

            newUnit.P_ENEMIES_NUMBER_COEF = 0.24177796f;
            newUnit.P_EMPTY_ENEMY_COEF = 0.32915026f;
            newUnit.P_INITIAL_BID = 0.7866433f;
            newUnit.P_POWER_COEF = 0.5504208f;

            return newUnit;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append(" attack {");
            sb.append(" A_ENEMIES_NUMBER_COEF: ");
            sb.append(A_ENEMIES_NUMBER_COEF);
            sb.append(" A_POWER_COEF: ");
            sb.append(A_POWER_COEF);
            sb.append(" }\n enemy {");
            sb.append(" E_POWER_COEF: ");
            sb.append(E_POWER_COEF);
            sb.append(" E_NEAR_POWER_COEF: ");
            sb.append(E_NEAR_POWER_COEF);
            sb.append(" }\n power {");
            sb.append(" P_INITIAL_BID: ");
            sb.append(P_INITIAL_BID);
            sb.append(" P_POWER_COEF: ");
            sb.append(P_POWER_COEF);
            sb.append(" P_ENEMIES_NUMBER_COEF: ");
            sb.append(P_ENEMIES_NUMBER_COEF);
            sb.append(" P_EMPTY_ENEMY_COEF: ");
            sb.append(P_EMPTY_ENEMY_COEF);
            sb.append(" }");
            return sb.toString();
        }
    }

    public static Unit unit;

    public static void fillRandom() {
        unit = Unit.getRandomUnit();
        Logger.log("New unit generated: ");
        Logger.log(unit.toString());
    }

    public static void fillPredefined() {
        unit = Unit.getPredefinedUnit();
        Logger.log("Predefined unit loaded: ");
        Logger.log(unit.toString());
    }

    public static void populateRandom() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = Unit.getRandomUnit();
        }
        setCurrent(0);
    }

    public static void newGeneration() {

        Unit[] newPopulation = new Unit[POPULATION_SIZE];

        // Выбираем 3 лучших
        int best = pickTheBestErase();
        int second = pickTheBestErase();
        int third = pickTheBestErase();

        newPopulation[0] = population[best];
        newPopulation[1] = population[second];
        newPopulation[2] = population[third];

        // 3 лучших-мутанта

        newPopulation[3] = mutate(population[best]);
        newPopulation[4] = mutate(population[second]);
        newPopulation[5] = mutate(population[third]);

        //2 после-лучших-мутанта
        int mutant1 = pickTheBestErase();
        int mutant2 = pickTheBestErase();

        newPopulation[6] = mutate(population[mutant1]);
        newPopulation[7] = mutate(population[mutant2]);

        //6 потомков
        newPopulation[8] = sex(population[best], population[second]);
        newPopulation[9] = sex(population[second], population[best]);
        newPopulation[10] = sex(population[best], population[third]);
        newPopulation[11] = sex(population[third], population[best]);
        newPopulation[12] = sex(population[second], population[third]);
        newPopulation[13]= sex(population[third], population[second]);

        //3 потомка-мутанта
        newPopulation[14] = mutate(newPopulation[8]);
        newPopulation[15] = mutate(newPopulation[10]);
        newPopulation[16] = mutate(newPopulation[12]);

        //3 полных рандома
        newPopulation[17] = Unit.getRandomUnit();
        newPopulation[18] = Unit.getRandomUnit();
        newPopulation[19] = Unit.getRandomUnit();

        setCurrent(0);
        clearResults();
    }

    private static void clearResults() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            results[i] = 0;
        }
    }

    public static Unit sex(Unit unit1, Unit unit2) {
        Unit child = new Unit();
        child.A_ENEMIES_NUMBER_COEF =unit1.A_ENEMIES_NUMBER_COEF;
        child.A_POWER_COEF = unit2.A_POWER_COEF;

        child.E_NEAR_POWER_COEF = unit1.E_NEAR_POWER_COEF;
        child.E_POWER_COEF = unit2.E_POWER_COEF;

        child.P_ENEMIES_NUMBER_COEF = unit1.P_ENEMIES_NUMBER_COEF;
        child.P_EMPTY_ENEMY_COEF = unit2.P_EMPTY_ENEMY_COEF;
        child.P_INITIAL_BID = unit1.P_INITIAL_BID;
        child.P_POWER_COEF = unit2.P_POWER_COEF;

        return child;
    }

    public static Unit mutate(Unit source) {
        Unit hulk = new Unit();
        hulk.A_ENEMIES_NUMBER_COEF = Math.abs(source.A_ENEMIES_NUMBER_COEF + randWithDeviation(MUTATION));
        hulk.A_POWER_COEF = Math.abs(source.A_POWER_COEF + randWithDeviation(MUTATION));

        hulk.E_NEAR_POWER_COEF = Math.abs(source.E_NEAR_POWER_COEF + randWithDeviation(MUTATION));
        hulk.E_POWER_COEF = Math.abs(source.E_POWER_COEF + randWithDeviation(MUTATION));

        hulk.P_ENEMIES_NUMBER_COEF = Math.abs(source.P_ENEMIES_NUMBER_COEF + randWithDeviation(MUTATION));
        hulk.P_EMPTY_ENEMY_COEF = Math.abs(source.P_EMPTY_ENEMY_COEF + randWithDeviation(MUTATION));
        hulk.P_INITIAL_BID = Math.abs(source.P_INITIAL_BID + randWithDeviation(MUTATION));
        hulk.P_POWER_COEF = Math.abs(source.P_POWER_COEF + randWithDeviation(MUTATION));

        return hulk;
    }


    public static void processMatchResult(int score) {
        results[current] += score;
        Logger.log("Match " + matches + " ends");
        if (score == 0) {
            results[current] += maxCurrentScore;
            Logger.log("Lose!");
        } else {
            Logger.log("Won!");
        }
        maxCurrentScore = 0;
        matches++;
        END = true;
        if (matches % MATCHES_PER_UNIT == 0) {
            results[current] /= MATCHES_PER_UNIT;
            Logger.log("Unit " + current + " tested. Average score: " + results[current]);
            int next = current+1;
            if (next == POPULATION_SIZE) {
                printPopulation();

                if (lastAverageScore > averageScore)  {
                    increaseMutationCoef();
                } else {
                    decreaseMutationCoef();
                }

                lastAverageScore = averageScore;

                newGeneration();


                if (generation == GENERATIONS) {
                    Logger.log("------ GENERATIONS LIMIT! ------");
                    ScreenController.exitGame();
                }

                generation++;
            } else {
                setCurrent(current + 1);
            }
        }
    }

    public static void printPopulation() {
        Logger.log("Generation: " + generation);
        float totalScore  = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            Logger.log(i+": "+ population[i] + " - score:" + results[i] + ";");
            totalScore += results[i];
        }
        averageScore = totalScore/POPULATION_SIZE;
        Logger.log("Population average score: " + totalScore/POPULATION_SIZE);
    }

    public static float randWithDeviation(float deviation) {
        Random random = new Random();
        return random.nextFloat()*deviation*2 - deviation;
    }

    private static int pickTheBest(){
        int max = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            if (results[i] > results[max]) {
                max = i;
            }
        }
        return max;
    }

    private static int pickTheBestErase(){
        int max = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            if (results[i] > results[max]) {
                max = i;
            }
        }
        results[max] = 0;
        return max;
    }

    private static void setCurrent(int i) {
        current = i;
        unit = population[current];
    }

    private static void increaseMutationCoef() {
        MUTATION += MUTATION_STEP;
        if (MUTATION > 1.0f) {
            MUTATION = 1.0f;
        }
        Logger.log("Increase mutation coef to " + MUTATION);
    }

    private static void decreaseMutationCoef() {
        MUTATION -= MUTATION_STEP;
        if (MUTATION < 0.0f) {
            MUTATION = 0.0f;
        }
        Logger.log("Decrease mutation coef to " + MUTATION);
    }
}
