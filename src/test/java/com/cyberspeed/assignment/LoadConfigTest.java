package com.cyberspeed.assignment;

import com.cyberspeed.assignment.models.GameConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LoadConfigTest extends BaseSpec {

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
}
