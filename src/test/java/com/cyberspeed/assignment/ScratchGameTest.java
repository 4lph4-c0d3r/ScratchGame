package com.cyberspeed.assignment;

import com.cyberspeed.assignment.models.GameConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ScratchGameTest {

    @Test
    public void testLoadConfig() throws IOException {
        String fileName = "config.json";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        assertNotNull(inputStream);

        GameConfig config = ScratchGame.loadConfig(inputStream);

        assertNotNull(config);
        assertEquals(4, config.getColumns());
        assertEquals(4, config.getRows());
        assertEquals(11, config.getSymbols().size());
        assertEquals(11, config.getWinCombinations().size());
        assertNotNull(config.getProbabilities());
        assertNotNull(config.getProbabilities().getStandardSymbols());
        assertNotNull(config.getProbabilities().getBonusSymbols());
        assertEquals(9, config.getProbabilities().getStandardSymbols().size());
    }
}
