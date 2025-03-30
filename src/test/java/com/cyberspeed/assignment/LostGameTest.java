package com.cyberspeed.assignment;

import com.cyberspeed.assignment.models.GameResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LostGameTest extends BaseSpec {

    @Test
    public void testLostGame() {
        // Define the 3x3 matrix representing a lost game
        String[][] lostMatrix = {
                {"A", "B", "C"},
                {"E", "B", "5x"},
                {"F", "D", "C"}
        };

        boolean isLost = gameHelper.isLostGame(lostMatrix);
        assertTrue(isLost);

        int reward = gameHelper.applyBonusEffects(0, lostMatrix);

        // Ensure reward remains 0
        assertEquals(0, reward);

        // Validate game result
        GameResult result = new GameResult(lostMatrix, reward, gameHelper.appliedWinningCombinations, gameHelper.appliedBonusSymbol);

        assertEquals(0, result.getReward());
        assertEquals("MISS", result.getAppliedBonusSymbol());
    }
}
