package com.cyberspeed.assignment;

import com.cyberspeed.assignment.helper.GameHelper;
import com.cyberspeed.assignment.models.GameConfig;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BaseSpec {

    public static GameHelper gameHelper;
    public int betAmount = 100;

    public GameConfig loadConfig() throws IOException {
        String fileName = "config.json";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        assertNotNull(inputStream);
        return ScratchGame.loadConfig(inputStream);
    }

    @BeforeEach
    public void setUp() throws IOException {
        GameConfig config = loadConfig();
        gameHelper = new GameHelper(config);
    }


}
