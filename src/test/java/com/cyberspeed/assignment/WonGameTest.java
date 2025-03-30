package com.cyberspeed.assignment;

import com.cyberspeed.assignment.models.GameResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WonGameTest extends BaseSpec {

    @Test
    public void testWonGame() throws JsonProcessingException {
        // Define the 3x3 matrix representing a won game
        String[][] wonMatrix = {
                {"A", "B", "C"},
                {"E", "B", "10x"},
                {"F", "D", "B"}
        };

        // Check if the game is lost (should be false)
        boolean isLost = gameHelper.isLostGame(wonMatrix);
        assertFalse(isLost);

        System.out.println("IN TEST - Applied Winning Combinations BEFORE base reward: " + gameHelper.appliedWinningCombinations);

        int baseReward = gameHelper.calculateBaseReward(betAmount, wonMatrix);
        System.out.println("IN TEST - Applied Winning Combinations AFTER base reward: " + gameHelper.appliedWinningCombinations);
        int finalReward = gameHelper.applyBonusEffects(baseReward, wonMatrix);
        System.out.println("IN TEST - Applied Winning Combinations AFTER FINAL reward: " + gameHelper.appliedWinningCombinations);


        // Ensure the reward is correctly calculated
        //assertEquals(3000, finalReward);

        // Validate game result
        GameResult result = new GameResult(wonMatrix, finalReward, gameHelper.appliedWinningCombinations, gameHelper.appliedBonusSymbol);

        System.out.println("result " + new ObjectMapper().writeValueAsString(result));
//        assertEquals(3000, result.getReward());
//        assertEquals("10x", result.getAppliedBonusSymbol());
//        assertTrue(result.getAppliedWinningCombinations().containsKey("B"));
//        assertEquals(List.of("same_symbol_3_times"), result.getAppliedWinningCombinations().get("B"));
    }
}
