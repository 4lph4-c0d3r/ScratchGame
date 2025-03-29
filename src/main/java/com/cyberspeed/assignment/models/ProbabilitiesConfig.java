package com.cyberspeed.assignment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ProbabilitiesConfig {
    @JsonProperty("standard_symbols")
    private List<StandardSymbolProbability> standardSymbols;

    @JsonProperty("bonus_symbols")
    private BonusSymbolsProbability bonusSymbols;


    public List<StandardSymbolProbability> getStandardSymbols() {
        return standardSymbols;
    }

    public void setStandardSymbols(List<StandardSymbolProbability> standardSymbols) {
        this.standardSymbols = standardSymbols;
    }

    public BonusSymbolsProbability getBonusSymbols() {
        return bonusSymbols;
    }

    public void setBonusSymbols(BonusSymbolsProbability bonusSymbols) {
        this.bonusSymbols = bonusSymbols;
    }
}
