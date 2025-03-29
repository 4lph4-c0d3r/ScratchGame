package com.cyberspeed.assignment.models;

import java.util.List;
import java.util.Map;

public class GameConfig {
    private int columns;
    private int rows;
    private Map<String, SymbolConfig> symbols;
    private ProbabilitiesConfig probabilities;
    private Map<String, WinCombination> winCombinations;


    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Map<String, SymbolConfig> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, SymbolConfig> symbols) {
        this.symbols = symbols;
    }

    public ProbabilitiesConfig getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(ProbabilitiesConfig probabilities) {
        this.probabilities = probabilities;
    }

    public Map<String, WinCombination> getWinCombinations() {
        return winCombinations;
    }

    public void setWinCombinations(Map<String, WinCombination> winCombinations) {
        this.winCombinations = winCombinations;
    }

    public List<StandardSymbolProbability> getStandardSymbolsProbabilities() {
        return probabilities.getStandardSymbols();
    }

    public BonusSymbolsProbability getBonusSymbolsProbabilities() {
        return probabilities.getBonusSymbols();
    }
}