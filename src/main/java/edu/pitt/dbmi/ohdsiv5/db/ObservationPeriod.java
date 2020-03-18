package edu.pitt.dbmi.ohdsiv5.db;

import java.sql.Timestamp;



public class ObservationPeriod  {


    // Constructors

    /** default constructor */
    public ObservationPeriod() {
    }

    public ObservationPeriod(Timestamp observationPeriodStartDate, Timestamp observationPeriodEndDate, Long personId, Long periodTypeConceptId) {
	super();
	this.observationPeriodStartDate = observationPeriodStartDate;
	this.observationPeriodEndDate = observationPeriodEndDate;
	this.personId = personId;
	this.periodTypeConceptId = periodTypeConceptId;
    }

   
    // Property accessors
                    Long observationPeriodId;
    public Long getObservationPeriodId() {
        return this.observationPeriodId;
    }    
    public void setObservationPeriodId(Long observationPeriodId) {
        this.observationPeriodId = observationPeriodId;
    }


        Timestamp observationPeriodStartDate;
    public void setObservationPeriodStartDate(Timestamp observationPeriodStartDate) {
	this.observationPeriodStartDate = observationPeriodStartDate;
    }    
    public Timestamp getObservationPeriodStartDate() {
	return observationPeriodStartDate;
    }
 


        Timestamp observationPeriodEndDate;
    public void setObservationPeriodEndDate(Timestamp observationPeriodEndDate) {
	this.observationPeriodEndDate = observationPeriodEndDate;
    }    
    public Timestamp getObservationPeriodEndDate() {
	return observationPeriodEndDate;
    }
    

        Long personId;
    public Long getPersonId() {
	return personId;
    }
    public void setPersonId(Long personId) {
	this.personId = personId;
    }

        Long periodTypeConceptId; // 35124300, MDS
    public Long getPeriodTypeConceptId() {
	return periodTypeConceptId;
    }
    public void setPeriodTypeConceptId(Long periodTypeConceptId) {
	this.periodTypeConceptId = periodTypeConceptId;
    }
}
