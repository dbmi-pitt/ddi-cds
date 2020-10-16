
package edu.pitt.dbmi.ohdsiv5.db;


import java.sql.Timestamp;



public class ProcedureOccurrence  {


    // Constructors

    /** default constructor */
    public ProcedureOccurrence() {
    }

    public ProcedureOccurrence(Long personId, Timestamp procedureDate, Integer procedureConceptId, Integer procedureTypeConceptId, String procedureSourceValue) {
	super();
	this.personId = personId;
	this.procedureDate = procedureDate;
	this.procedureConceptId = procedureConceptId;
	this.procedureTypeConceptId = procedureTypeConceptId;
	this.procedureSourceValue = procedureSourceValue;
    }
   
    // Property accessors
                    Long ProcedureOccurrenceId;
    public Long getProcedureOccurrenceId() {
        return this.ProcedureOccurrenceId;
    }    
    public void setProcedureOccurrenceId(Long ProcedureOccurrenceId) {
        this.ProcedureOccurrenceId = ProcedureOccurrenceId;
    }

        Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }

    /**
     *      */
        Integer procedureConceptId;
    public Integer getProcedureConceptId() {
	return procedureConceptId;
    }
	
    /**
     *      */
    public void setProcedureConceptId(Integer procedureConceptId) {
	this.procedureConceptId = procedureConceptId;
    }

        Timestamp procedureDate;
    public void setProcedureDate(Timestamp procedureDate) {
	this.procedureDate = procedureDate;
    }    
    public Timestamp getProcedureDate() {
	return procedureDate;
    }

    /**
     *      */
        Integer procedureTypeConceptId;
    public Integer getProcedureTypeConceptId() {
	return procedureTypeConceptId;
    }
	
    /**
     *      */
    public void setProcedureTypeConceptId(Integer procedureTypeConceptId) {
		this.procedureTypeConceptId = procedureTypeConceptId;
    }
    
    /**
     *      */
    	String procedureSourceValue;
    public String getProcedureSourceValue() {
	return procedureSourceValue;
    }
	
    /**
     *      */
    public void setProcedureSourcevalue(String procedureSourceValue) {
		this.procedureSourceValue = procedureSourceValue;
    }
    
        Integer modifierConceptId;
    public Integer getModifierConceptId() {
	return modifierConceptId;
    }
    public void setModifierConceptId(Integer modifierConceptId) {
	this.modifierConceptId = modifierConceptId;
    }
}
