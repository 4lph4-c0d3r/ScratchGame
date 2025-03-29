package com.cyberspeed.assignment.helper;

import com.cyberspeed.assignment.models.GameConfig;
import com.cyberspeed.assignment.models.StandardSymbolProbability;
import com.cyberspeed.assignment.models.SymbolConfig;
import com.cyberspeed.assignment.models.WinCombination;

import java.util.*;

public class GameHelper {

    private final GameConfig config;
    private final Random random = new Random();
    private final String[][] matrix;
    private final Map<String, List<String>> appliedWinningCombinations;
    private String appliedBonusSymbol;

    public GameHelper(GameConfig config) {
        this.config = config;
        this.matrix = new String[config.getRows()][config.getColumns()];
        this.appliedWinningCombinations = new HashMap<>();
        this.appliedBonusSymbol = "MISS";
    }

    /**
     * Executes the main game logic: generates the matrix, calculates the reward,
     * and determines applied winning combinations and bonus symbols.
     *
     * @param betAmount The amount the user bet.
     * @return A map containing the game's results (matrix, reward, etc.).
     */
    public Map<String, Object> playGame(int betAmount) {
        generateMatrix();

        int reward = calculateReward(betAmount);

        return Map.of(
                "matrix", matrix,
                "reward", reward,
                "appliedWinningCombinations", appliedWinningCombinations,
                "appliedBonusSymbol", appliedBonusSymbol
        );
    }

    /**
     * Generates the symbol matrix based on the configuration.
     */
    private void generateMatrix() {
        for (int row = 0; row < config.getRows(); row++) {
            for (int col = 0; col < config.getColumns(); col++) {
                matrix[row][col] = getRandomSymbol(row, col); // Get symbol for each cell
            }
        }
        // Apply bonus symbols to the matrix
        applyBonusSymbols();
    }


    /**
     * Gets a random symbol for a specific cell, considering probabilities.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The randomly selected symbol.
     */
    private String getRandomSymbol(int row, int col) {
        // If there's no probability config, return a default symbol
        if (config.getProbabilities() == null || config.getProbabilities().getStandardSymbols() == null || config.getProbabilities().getStandardSymbols().isEmpty()) {
            return "A"; // add some default value
        }

        StandardSymbolProbability probabilityConfig = config.getProbabilities().getStandardSymbols().get(0);

        // Find the probability configuration for the current cell
        for (StandardSymbolProbability prob : config.getProbabilities().getStandardSymbols()) {
            if (prob.getRow() == row && prob.getColumn() == col) {
                probabilityConfig = prob;
                break;
            }
        }

        Map<String, Integer> symbols = probabilityConfig.getSymbols();
        int totalWeight = symbols.values().stream().mapToInt(Integer::intValue).sum();
        int randomNum = random.nextInt(totalWeight) + 1;

        // Determine the symbol based on the random number and weights
        for (Map.Entry<String, Integer> entry : symbols.entrySet()) {
            randomNum -= entry.getValue();
            if (randomNum <= 0) {
                return entry.getKey(); // Return the symbol
            }
        }

        return "A";
    }


    /**
     * Applies bonus symbols to the matrix based on their probabilities.
     */
    private void applyBonusSymbols() {
        // If there's no bonus symbol configuration, return
        if (config.getProbabilities() == null || config.getProbabilities().getBonusSymbols() == null || config.getProbabilities().getBonusSymbols().getSymbols() == null) {
            return;
        }

        // Iterate through bonus symbols and their probabilities
        for (Map.Entry<String, Integer> entry : config.getProbabilities().getBonusSymbols().getSymbols().entrySet()) {
            if (random.nextInt(100) < entry.getValue()) {
                int row = random.nextInt(config.getRows());
                int col = random.nextInt(config.getColumns());
                // Place the bonus symbol in the matrix
                matrix[row][col] = entry.getKey();
                return;
            }
        }
    }


    /**
     * Calculates the total reward based on winning combinations and bonus symbols.
     *
     * @param betAmount The amount the user bet.
     * @return The total reward.
     */
    private int calculateReward(int betAmount) {
        int totalReward = 0;
        checkWinningCombinations();

        // Calculate reward for each symbol and its winning combinations
        for (Map.Entry<String, List<String>> symbolEntry : appliedWinningCombinations.entrySet()) {
            String symbol = symbolEntry.getKey();
            List<String> combinations = symbolEntry.getValue();

            if (config.getSymbols().containsKey(symbol)) {
                SymbolConfig symbolConfig = config.getSymbols().get(symbol);
                double symbolReward = betAmount * symbolConfig.getRewardMultiplier();

                // Add reward for each winning combination
                for (String combination : combinations) {
                    if (config.getWinCombinations().containsKey(combination)) {
                        WinCombination winCombination = config.getWinCombinations().get(combination);
                        totalReward += (int)Math.round(symbolReward * winCombination.getRewardMultiplier());
                    }
                }
            }
        }


        return totalReward;
    }


    /**
     * Checks for winning combinations in the matrix.
     */
    private void checkWinningCombinations() {
       //TODO: Add logic
        checkSameSymbolsCombinations(); // Check for "same_symbols" combinations
        checkLinearSymbolsCombinations(); // Check for "linear_symbols" combinations
    }


}
