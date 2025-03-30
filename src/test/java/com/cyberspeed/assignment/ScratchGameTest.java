package com.cyberspeed.assignment;

import com.cyberspeed.assignment.helper.GameHelper;
import com.cyberspeed.assignment.models.GameConfig;
import com.cyberspeed.assignment.models.GameResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class ScratchGameTest {

    private GameConfig loadConfig() throws IOException {
        String fileName = "config.json";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        assertNotNull(inputStream);
        return ScratchGame.loadConfig(inputStream);
    }

    @Test
    public void testLoadConfig() throws IOException {
        GameConfig config = loadConfig();

        assertNotNull(config);
        assertEquals(3, config.getColumns());
        assertEquals(3, config.getRows());
        assertEquals(11, config.getSymbols().size());
        assertEquals(11, config.getWinCombinations().size());
        assertNotNull(config.getProbabilities());
        assertNotNull(config.getProbabilities().getStandardSymbols());
        assertNotNull(config.getProbabilities().getBonusSymbols());
        assertEquals(2, config.getProbabilities().getStandardSymbols().size());
    }


    @Test
    public void testMatrixGeneration() throws IOException {
        GameConfig config = loadConfig();
        GameHelper gameHelper = new GameHelper(config);

        int betAmount = 100;

        GameResult result = gameHelper.playGame(betAmount);

        String[][] matrix = result.getMatrix();

        // Assert matrix dimensions based on config.json - 3x3
        assertEquals(3, matrix.length);
        for (String[] row : matrix) {
            assertEquals(3, row.length);
        }
    }
}
