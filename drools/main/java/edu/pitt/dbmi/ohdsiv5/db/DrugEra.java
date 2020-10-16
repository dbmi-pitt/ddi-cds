package edu.pitt.dbmi.ohdsiv5.db;


import java.util.Calendar;
import java.sql.Timestamp;



public class DrugEra  {

    // Constructors

    /** default constructor */
    public DrugEra() {
    }

    public DrugEra(Long drugEraId, Timestamp drugEraStartDate, Long personId, Timestamp drugEraEndDate, Integer drugConceptID, Integer drugExposureCount) {
	super();
    this.drugEraId = drugEraId;
	this.drugEraStartDate = drugEraStartDate;
	this.personId = personId;
	this.drugEraEndDate = drugEraEndDate;
	this.drugConceptID = drugConceptID;
	this.drugExposureCount = drugExposureCount;
    }

    // Property accessors
    Long drugEraId;
    public Long getDrugEraId() {
        return this.drugEraId;
    }    
    public void setDrugEraId(Long id) {
        this.drugEraId = id;
    }

    Timestamp drugEraStartDate;
    public void setDrugEraStartDate(Timestamp drugEraStartDate) {
	this.drugEraStartDate = drugEraStartDate;
    }    
    public Timestamp getDrugEraStartDate() {
	return drugEraStartDate;
    }


    Timestamp drugEraEndDate;
    public void setDrugEraEndDate(Timestamp drugEraEndDate) {
	this.drugEraEndDate = drugEraEndDate;
    }    
    public Timestamp getDrugEraEndDate() {
	return drugEraEndDate;
    }

    Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }

    Integer drugConceptID;
    public void setDrugConceptId(Integer condId) { 
	this.drugConceptID = condId;
    }
    public Integer getDrugConceptId() { 
	return drugConceptID;
    }

    Integer drugExposureCount;
    public void setDrugExposureCount(Integer drugExposureCount) { 
	this.drugExposureCount = drugExposureCount;
    }
    public Integer getDrugExposureCount() { 
	return drugExposureCount;
    }

}
