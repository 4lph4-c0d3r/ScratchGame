package com.cyberspeed.assignment;

import com.cyberspeed.assignment.models.GameResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatrixGenerationTest extends BaseSpec {

    @Test
    public void testMatrixGeneration() {

        GameResult result = gameHelper.playGame(betAmount);

        String[][] matrix = result.getMatrix();

        // Assert matrix dimensions based on test\resources\config.json - 3x3
        assertEquals(3, matrix.length);
        for (String[] row : matrix) {
            assertEquals(3, row.length);
        }
    }
}
