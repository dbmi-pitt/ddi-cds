package edu.pitt.dbmi.ohdsiv5.db;

import java.sql.Timestamp;


/**
 * ConditionOccurrence 
 */

public class ConditionOccurrence  {

    // Constructors

    /** default constructor */
    public ConditionOccurrence() {
    }
    
    /** full constructor */
    public ConditionOccurrence(Long personId, Timestamp conditionStartDate, Timestamp conditionEndDate, Integer occurrenceType, Integer conditionConceptID, String sourceConditionCode, Integer conditionSourceConceptId) {
	super();
        this.personId = personId;
	this.conditionStartDate = conditionStartDate;
	this.conditionEndDate = conditionEndDate;
	this.occurrenceType = occurrenceType;
	this.conditionConceptID = conditionConceptID;
	this.sourceConditionCode = sourceConditionCode;
	this.associatedProviderId = 9999; // No provider
	this.conditionSourceConceptId = conditionSourceConceptId;
    }
    
  
    // Property accessors
        Long conditionOccurrenceId;
    public Long getConditionOccurrenceId() {
        return this.conditionOccurrenceId;
    }    
    public void setConditionOccurrenceId(Long id) {
        this.conditionOccurrenceId = id;
    }
    
        Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }

        Integer conditionConceptID;
    public void setConditionConceptId(Integer condId) { 
	this.conditionConceptID = condId;
    }
    public Integer getConditionConceptId() { 
	return conditionConceptID;
    }
 

        Timestamp conditionStartDate;
    public void setConditionStartDate(Timestamp conditionStartDate) {
	this.conditionStartDate = conditionStartDate;
    }    
    public Timestamp getConditionStartDate() {
	return conditionStartDate;
    }

        Timestamp conditionEndDate;
    public void setConditionEndDate(Timestamp conditionEndDate) {
	this.conditionEndDate = conditionEndDate;
    }
    public Timestamp getConditionEndDate() {
	return conditionEndDate;
    }
    
        Integer occurrenceType;
    public void setConditionOccurrenceType(Integer occurrenceType) {
	this.occurrenceType = occurrenceType;
    }
    public Integer getConditionOccurrenceType() { 
	return occurrenceType;
    }
 

      String stopReason;
   public void setStopReason(String reason) {
        this.stopReason = reason;
   }
   public String getStopReason() { 
        return stopReason;   
   }

	    Integer associatedProviderId;
	public Integer getAssociatedProviderId() {
		return associatedProviderId;
	}


	/**
	 * 	 */

	public void setAssociatedProviderId(Integer associatedProviderId) {
		this.associatedProviderId = associatedProviderId;
	}


        Integer visitOccurrenceId;
    public Integer getVisitOccurrenceId() {
	return visitOccurrenceId;
    }
    

    /**
     *      */
    
    public void setVisitOccurrenceId(Integer visitOccurrenceId) {
	this.visitOccurrenceId = visitOccurrenceId;
    }


        String sourceConditionCode;
    public void setSourceConditionCode(String mdsIcd) {
	this.sourceConditionCode = mdsIcd;
    }
    public String getSourceConditionCode() { 
	return sourceConditionCode;
    }

        Integer conditionSourceConceptId;
    public Integer getConditionSourceConceptId() {
	return conditionSourceConceptId;
    }
    public void setConditionSourceConceptId(Integer conditionSourceConceptId) {
	this.conditionSourceConceptId = conditionSourceConceptId;
    }
}
