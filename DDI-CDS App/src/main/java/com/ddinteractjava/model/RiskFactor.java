package com.ddinteractjava.model;

public class RiskFactor {
    String riskName;
    //Default value of false
    boolean hasRiskFactor;
    String resourceName;
    String effectiveDate;
    double value;

    public RiskFactor() {

    }

    public RiskFactor(String riskName, boolean hasRiskFactor, String resourceName, String effectiveDate, double value) {
        this.riskName = riskName;
        this.hasRiskFactor = hasRiskFactor;
        this.resourceName = resourceName;
        this.effectiveDate = effectiveDate;
        this.value = value;
    }

    public String getRiskName() {
        return riskName;
    }

    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }

    public Boolean getHasRiskFactor() {
        return hasRiskFactor;
    }

    public void setHasRiskFactor(Boolean hasRiskFactor) {
        this.hasRiskFactor = hasRiskFactor;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
