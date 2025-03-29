package com.cyberspeed.assignment.helper;

import com.cyberspeed.assignment.models.GameConfig;

import java.util.*;

public class GameHelper {

    private final GameConfig config;
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
        // TODO: generate matrix

        //TODO: calculate reward
        int reward = 0;

        return Map.of(
                "matrix", matrix,
                "reward", reward,
                "appliedWinningCombinations", appliedWinningCombinations,
                "appliedBonusSymbol", appliedBonusSymbol
        );
    }


}
