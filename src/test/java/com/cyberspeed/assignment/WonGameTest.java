package com.cyberspeed.assignment;

import com.cyberspeed.assignment.models.GameResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WonGameTest extends BaseSpec {

    @Test
    public void testWonGame() {
        // Define the 3x3 matrix representing a won game
        String[][] wonMatrix = {
                {"A", "B", "C"},
                {"E", "B", "10x"},
                {"F", "D", "B"}
        };

        // Check if the game is lost (should be false)
        boolean isLost = gameHelper.isLostGame(wonMatrix);
        assertFalse(isLost);

        /**
         * ------------- REWARD -------------
         * Formula: 100(betting amount) x 3 | reward(symbol B) x1(at least 3 same symbols winning combination)
         *  x10(x10 bonus symbol) = 3000 (winning amount)
         *  i.e  100×3×1×10 = 3,000
         **/

        int baseReward = gameHelper.calculateBaseReward(betAmount, wonMatrix);
        int finalReward = gameHelper.applyBonusEffects(baseReward, wonMatrix);
        GameResult result = new GameResult(wonMatrix, finalReward, gameHelper.appliedWinningCombinations, gameHelper.appliedBonusSymbol);

        assertEquals(3000, result.getReward());
        assertEquals("10x", result.getAppliedBonusSymbol());
        assertTrue(result.getAppliedWinningCombinations().containsKey("B"));
        assertEquals(List.of("same_symbol_3_times"), result.getAppliedWinningCombinations().get("B"));
    }
}
