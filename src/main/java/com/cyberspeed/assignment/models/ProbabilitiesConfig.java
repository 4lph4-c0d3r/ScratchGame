package com.cyberspeed.assignment.models;

import java.util.List;

public class ProbabilitiesConfig {
    private List<StandardSymbolProbability> standardSymbols;
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
