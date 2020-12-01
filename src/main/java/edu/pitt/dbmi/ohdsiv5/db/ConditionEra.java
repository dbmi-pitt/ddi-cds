package edu.pitt.dbmi.ohdsiv5.db;

import java.sql.Timestamp;




public class ConditionEra  {

    // Constructors

    /** default constructor */
    public ConditionEra() {
    }

    public ConditionEra(Long conditionEraId, Timestamp conditionEraStartDate, Timestamp conditionEraEndDate, Long personId, Integer conditionConceptID, Integer conditionOccurrenceCount) {
	super();
    this.conditionEraId = conditionEraId;
	this.conditionEraStartDate = conditionEraStartDate;
	this.conditionEraEndDate = conditionEraEndDate;
	this.personId = personId;
	this.conditionConceptID = conditionConceptID;
	this.conditionOccurrenceCount = conditionOccurrenceCount;
    }
   
    // Property accessors
                    Long conditionEraId;
    public Long getConditionEraId() {
        return this.conditionEraId;
    }    
    public void setConditionEraId(Long id) {
        this.conditionEraId = id;
    }

       Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }


        Timestamp conditionEraStartDate;
    public void setConditionEraStartDate(Timestamp conditionEraStartDate) {
	this.conditionEraStartDate = conditionEraStartDate;
    }    
    public Timestamp getConditionEraStartDate() {
	return conditionEraStartDate;
    }



        Timestamp conditionEraEndDate;
    public void setConditionEraEndDate(Timestamp conditionEraEndDate) {
	this.conditionEraEndDate = conditionEraEndDate;
    }    
    public Timestamp getConditionEraEndDate() {
	return conditionEraEndDate;
    }

        Integer conditionConceptID;
    public void setConditionConceptId(Integer condId) { 
	this.conditionConceptID = condId;
    }
    public Integer getConditionConceptId() { 
	return conditionConceptID;
    }

        Integer conditionOccurrenceCount;
    public void setConditionOccurrenceCount(Integer conditionOccurrenceCount) { 
	this.conditionOccurrenceCount = conditionOccurrenceCount;
    }
    public Integer getConditionOccurrenceCount() { 
	return conditionOccurrenceCount;
    }

}
