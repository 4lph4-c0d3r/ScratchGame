package com.cyberspeed.assignment;

import com.cyberspeed.assignment.helper.GameHelper;
import com.cyberspeed.assignment.models.GameConfig;
import com.cyberspeed.assignment.models.GameResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ScratchGame {

    private static final Logger logger = LoggerFactory.getLogger(ScratchGame.class);

    public static void main(String[] args) {
        String configFilePath = args[1];
        int betAmount;

        try {
            betAmount = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            // Prevent any random input
           logger.info("Invalid betting amount. Must be a number.");
            return;
        }

        try (InputStream inputStream = ScratchGame.class.getClassLoader().getResourceAsStream(configFilePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Config file not found in resources: " + configFilePath);
            }
            GameConfig config = loadConfig(inputStream);
            GameHelper gameHelper = new GameHelper(config);
            GameResult result = gameHelper.playGame(betAmount);

            ObjectMapper objectMapper = new ObjectMapper();
            logger.info(objectMapper.writeValueAsString(result));

        } catch (IOException e) {
            logger.error("Error: ", e);
        }
    }

    public static GameConfig loadConfig(InputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(inputStream, GameConfig.class);
    }
}