package com.ddi_cds_app.model;

public class Summary {
    String summary;
    String clinicalSummary;
    String warningSymbol;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getClinicalSummary() {
        return clinicalSummary;
    }

    public void setClinicalSummary(String clinicalSummary) {
        this.clinicalSummary = clinicalSummary;
    }

    public String getWarningSymbol() {
        return warningSymbol;
    }

    public void setWarningSymbol(String warningSymbol) {
        this.warningSymbol = warningSymbol;
    }
}
