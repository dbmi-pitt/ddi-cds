package edu.pitt.dbmi.ohdsiv5.db;

import java.sql.Timestamp;


public class DrugStrength {
    
    // Constructor

    /** default constructor */
    public DrugStrength() {
    }

    public DrugStrength(
			Integer drugConceptId,
			Integer ingredientConceptId,
			Double amountValue,
			Integer amountUnitConceptId,
			Double numeratorValue,
			Integer numeratorUnitConceptId,
			Double denominatorValue,
			Integer denominatorUnitConceptId,
			Timestamp validEndDate,
			Timestamp validStartDate,
			String invalidReason
			) {
	this.drugConceptId = drugConceptId;
	this.ingredientConceptId = ingredientConceptId;
	this.amountValue = amountValue;
	this.amountUnitConceptId = amountUnitConceptId;
	this.numeratorValue = numeratorValue;
	this.numeratorUnitConceptId = numeratorUnitConceptId;
	this.denominatorValue = denominatorValue;
	this.denominatorUnitConceptId = denominatorUnitConceptId;
	this.validEndDate = validEndDate;
	this.validStartDate = validStartDate;
	this.invalidReason = invalidReason;
    }

    // Property accessors
            Integer drugConceptId;
    public Integer getDrugConceptId() {
	return this.drugConceptId;
    }
    public void setDrugConceptId(Integer drugConceptId) {
	this.drugConceptId = drugConceptId;
    }

        Integer ingredientConceptId;
    public Integer getIngredientConceptId() {
	return this.ingredientConceptId;
    }
    public void setIngredientConceptId(Integer ingredientConceptId) {
	this.ingredientConceptId = ingredientConceptId;
    }

        Double amountValue;
    public Double getAmountValue() {
	return this.amountValue;
    }
    public void setAmountValue(Double amountValue) {
	this.amountValue = amountValue;
    }

        Integer amountUnitConceptId;
    public Integer getAmountUnitConceptId() {
	return this.amountUnitConceptId;
    }
    public void setAmountUnitConceptId(Integer amountUnitConceptId) {
	this.amountUnitConceptId = amountUnitConceptId;
    }

        Double numeratorValue;
    public Double getNumeratorValue() {
	return this.numeratorValue;
    }
    public void setNumeratorValue(Double numeratorValue) {
	this.numeratorValue = numeratorValue;
    }

        Integer numeratorUnitConceptId;
    public Integer getNumeratorUnitConceptId() {
	return this.numeratorUnitConceptId;
    }
    public void setNumeratorUnitConceptId(Integer numeratorUnitConceptId) {
	this.numeratorUnitConceptId = numeratorUnitConceptId;
    }

        Double denominatorValue;
    public Double getDenominatorValue() {
	return this.denominatorValue;
    }
    public void setDenominatorValue(Double denominatorValue) {
	this.denominatorValue = denominatorValue;
    }
    
        Integer denominatorUnitConceptId;
    public Integer getDenominatorUnitConceptId() {
	return this.denominatorUnitConceptId;
    }
    public void setDenominatorUnitConceptId(Integer denominatorUnitConceptId) {
	this.denominatorUnitConceptId = denominatorUnitConceptId;
    }

        Timestamp validStartDate;
    public Timestamp getValidStartDate() {
	return this.validStartDate;
    }
    public void setValidStartDate(Timestamp validStartDate) {
	this.validStartDate = validStartDate;
    }    
   
        Timestamp validEndDate;
    public Timestamp getValidEndDate() {
	return this.validEndDate;
    }
    public void setValidEndDate(Timestamp validEndDate) {
	this.validEndDate = validEndDate;
    }

        String invalidReason;
    public String getInvalidReason() {
	return this.invalidReason;
    }
    public void setInvalidReason(String invalidReason) {
	this.invalidReason = invalidReason;
    }
}
