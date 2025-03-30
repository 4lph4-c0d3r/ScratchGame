package com.cyberspeed.assignment.helper;

import com.cyberspeed.assignment.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameHelper {

    private static final Logger logger = LoggerFactory.getLogger(GameHelper.class);
    private final GameConfig config;
    private final Random random = new Random();
    public final Map<String, List<String>> appliedWinningCombinations;
    public String appliedBonusSymbol;

    public GameHelper(GameConfig config) {
        this.config = config;
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
    public GameResult playGame(int betAmount) {
        // Generate matrix based on config
        String[][] matrix = generateMatrix();

        // Calculate the base reward
        int reward = calculateBaseReward(betAmount, matrix);


        // Apply bonus symbol effects
        reward = applyBonusEffects(reward, matrix);

        return new GameResult(matrix, reward, new HashMap<>(appliedWinningCombinations), appliedBonusSymbol);
    }

    /**
     * Generates the symbol matrix based on the configuration.
     */
    public String[][] generateMatrix() {
        String[][] matrix = new String[config.getRows()][config.getColumns()];
        for (int row = 0; row < config.getRows(); row++) {
            for (int col = 0; col < config.getColumns(); col++) {
                matrix[row][col] = getRandomSymbol(row, col); // Get symbol for each cell
            }
        }
        // Apply bonus symbols to the matrix
        applyBonusSymbols(matrix);
        return matrix;
    }


    /**
     * Gets a random symbol for a specific cell, considering probabilities.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The randomly selected symbol.
     */
    public String getRandomSymbol(int row, int col) {
        // If there's no probability config, return a default symbol
        if (config.getProbabilities() == null || config.getProbabilities().getStandardSymbols() == null || config.getProbabilities().getStandardSymbols().isEmpty()) {
            logger.info("No standard symbol probabilities defined. Using default symbol 'A'.");
            return "A";
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

        logger.info("Could not determine symbol based on probabilities. Using default symbol 'A'.");
        return "A";
    }


    /**
     * Applies bonus symbols to the matrix based on their probabilities.
     */
    public void applyBonusSymbols(String[][] matrix) {
        // If there's no bonus symbol configuration, return
        if (config.getProbabilities() != null && config.getProbabilities().getBonusSymbols() != null) {
            logger.info("Applying bonus symbols with probabilities: {}", config.getProbabilities().getBonusSymbols().getSymbols());
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
     * Calculates the base reward based on winning combinations.
     * Bonus symbols are not handled here.
     *
     * @param betAmount The amount the user bet.
     * @return The base reward.
     */
    public int calculateBaseReward(int betAmount, String[][] matrix) {
        int totalReward = 0;
        checkWinningCombinations(matrix);  // Check for winning combinations

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
                        totalReward += (int) Math.round(symbolReward * winCombination.getRewardMultiplier());
                    }
                }
            }
        }
        return totalReward;
    }

    /**
     * Applies the effects of any bonus symbols present in the matrix.
     *
     * @param totalReward The current total reward.
     * @return The total reward after applying bonus effects.
     */
    public int applyBonusEffects(int totalReward, String[][] matrix) {
        // Check if it's a "lost" game (e.g., no winning symbols or other conditions)

        System.out.println("IN CODE - Applied Winning Combinations INSIDE applyBonusEffects: " + appliedWinningCombinations);


        boolean isLostGame = isLostGame(matrix);

        System.out.println("IN CODE - Applied Winning Combinations AFTER IS LOST: " + appliedWinningCombinations);

        if (isLostGame) {
            return 0;  // Return 0 reward if it's a lost game
        }

        for (int row = 0; row < config.getRows(); row++) {
            for (int col = 0; col < config.getColumns(); col++) {
                String symbol = matrix[row][col];

                // Check for bonus symbols
                if (config.getSymbols().containsKey(symbol) && "bonus".equals(config.getSymbols().get(symbol).getType()) && totalReward > 0) {
                    appliedBonusSymbol = symbol;
                    SymbolConfig bonusConfig = config.getSymbols().get(symbol);
                    if (bonusConfig != null) {
                        if ("multiply_reward".equals(bonusConfig.getImpact())) {
                            totalReward = (int) Math.round(totalReward * bonusConfig.getRewardMultiplier());
                        } else if ("extra_bonus".equals(bonusConfig.getImpact())) {
                            if (bonusConfig.getExtra() != null) {
                                totalReward += bonusConfig.getExtra();
                            }
                        }
                    }
                    return totalReward;
                }
            }
        }

        // Default bonus symbol if none is found
        appliedBonusSymbol = "MISS";
        return totalReward;
    }

    /**
     * Determines if the game is lost by checking for winning combinations in the matrix.
     *
     * @param matrix The game board as a 2D array.
     * @return {@code true} if no winning combinations are found, otherwise {@code false}.
     */
    public boolean isLostGame(String[][] matrix) {
        System.out.println("1 - IN CODE - appliedWinningCombinations IN isLostGame : " + appliedWinningCombinations);

        checkWinningCombinations(matrix);

        // If any winning combinations were found, the game is NOT lost
        boolean isLost = appliedWinningCombinations.isEmpty();

        System.out.println("2 - IN CODE - appliedWinningCombinations IN isLostGame : " + appliedWinningCombinations);


        if (isLost) {
            logger.info("No winning combinations found. Game is lost.");
            appliedWinningCombinations.clear();
        } else {
            logger.info("Winning combinations found. Game is not lost.");
        }
        return isLost;
    }


    /**
     * Checks for winning combinations in the matrix.
     */
    private void checkWinningCombinations(String[][] matrix) {
        System.out.println("3 - IN CODE - appliedWinningCombinations IN checkWinningCombinations : " + appliedWinningCombinations);

        appliedWinningCombinations.clear();

        checkSameSymbolsCombinations(matrix); // Check for "same_symbols" combinations
        checkLinearSymbolsCombinations(matrix); // Check for "linear_symbols" combinations
    }


    /**
     * Checks for "same_symbols" winning combinations.
     */
    public void checkSameSymbolsCombinations(String[][] matrix) {
        Map<String, Integer> symbolCounts = new HashMap<>();

        // Count occurrences of each standard symbol
        for (String[] row : matrix) {
            for (String symbol : row) {
                if (config.getSymbols().containsKey(symbol) && "standard".equals(config.getSymbols().get(symbol).getType())) {
                    symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
                }
            }
        }

        // Check if any symbol count matches a winning combination
        for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            String symbol = entry.getKey();
            int count = entry.getValue();

            for (Map.Entry<String, WinCombination> winEntry : config.getWinCombinations().entrySet()) {
                WinCombination winCombination = winEntry.getValue();
                if ("same_symbols".equals(winCombination.getWhen()) && count >= winCombination.getCount()) {
                    appliedWinningCombinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(winEntry.getKey());
                }
            }
        }
    }

    /**
     * Checks for "linear_symbols" winning combinations.
     */
    public void checkLinearSymbolsCombinations(String[][] matrix) {
        for (Map.Entry<String, WinCombination> winEntry : config.getWinCombinations().entrySet()) {
            WinCombination winCombination = winEntry.getValue();
            if ("linear_symbols".equals(winCombination.getWhen())) {
                checkLinearCombination(winCombination, matrix);
            }
        }
    }

    /**
     * Checks for a specific "linear_symbols" winning combination.
     *
     * @param winCombination The winning combination to check.
     */
    public void checkLinearCombination(WinCombination winCombination, String[][] matrix) {
        for (List<String> coveredAreaGroup : winCombination.getCoveredAreas()) {
            String firstSymbol = null;
            boolean match = true;

            // Check if all symbols in the covered area match
            for (String cell : coveredAreaGroup) {
                String[] parts = cell.split(":");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                String symbol = matrix[row][col];

                if (firstSymbol == null) {
                    firstSymbol = symbol;
                } else if (!firstSymbol.equals(symbol)) {
                    match = false;
                    break;
                }
                if (config.getSymbols().containsKey(symbol) && "bonus".equals(config.getSymbols().get(symbol).getType())) {
                    match = false;
                    break;
                }
            }

            // If all symbols match, add the winning combination
            if (match && firstSymbol != null && config.getSymbols().containsKey(firstSymbol) && "standard".equals(config.getSymbols().get(firstSymbol).getType())) {
                appliedWinningCombinations.computeIfAbsent(firstSymbol, k -> new ArrayList<>()).add(getKeyByValue(config.getWinCombinations(), winCombination));
            }
        }
    }


    /**
     * Gets the key (name) of a WinCombination from the map, given its value (WinCombination object).
     *
     * @param map   The map of WinCombinations.
     * @param value The WinCombination object.
     * @return The key (name) of the WinCombination.
     */
    private <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
