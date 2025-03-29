package com.cyberspeed.assignment;

import com.cyberspeed.assignment.models.GameConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ScratchGame {

    private static final Logger logger = LoggerFactory.getLogger(ScratchGame.class);

    public static void main(String[] args) {
        // Input format :
        // java -jar <your-jar-file> --config config.json --betting-amount 100

        String configFilePath = args[1];
        int betAmount;

        try {
            betAmount = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            // Prevent any random input
            System.err.println("Invalid betting amount. Must be a number.");
            return;
        }

        try (InputStream inputStream = new FileInputStream(new File(configFilePath))) {
            GameConfig config = loadConfig(inputStream);
            //TODO: add helper class to handle the game logic

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static GameConfig loadConfig(InputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(inputStream, GameConfig.class);
    }
}