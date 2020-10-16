package edu.pitt.dbmi.ohdsiv5.db;

import java.sql.Timestamp;


/**
 * Observation 
 */

public class Observation  {
 
    // Constructors

    /** default constructor */
    public Observation() {
    }

    // create Observation for case where the value is a number
    // concept. Pass TRUE for checkVocab if the observation concept is
    // thought to be in the Standard Vocabulary 
/*    public Observation(Long personId, String sourceObsCode,
		       Integer obsConceptId, Integer obsType, double obsValueAsNumber, Timestamp obsDate, Boolean checkVocab) {
		super();
		this.personId = personId;
		this.sourceObsCode = sourceObsCode;
		if (checkVocab == true)
		    this.obsConceptId = SourceToConceptMap.getConceptId(sourceObsCode);
		else if (obsConceptId != null)
		    this.obsConceptId = obsConceptId;
		else
		    this.obsConceptId = 0;
		this.obsValueAsNumber = obsValueAsNumber;
		this.obsType = obsType;
		this.obsUnitsConceptId = obsUnitsConceptId;
		this.obsDate = obsDate;
		this.providerId = 9999; // No provider
	}*/

    // create Observation for case where the value is a number
    // concept and the observation concept id is known
    public Observation(Long personId, Integer observationId, String sourceObsCode,
		       double obsValueAsNumber, Integer obsType, Timestamp obsDate) {
		super();
		this.personId = personId;
		this.obsConceptId = observationId;
		this.sourceObsCode = sourceObsCode;
		this.obsValueAsNumber = obsValueAsNumber;
		this.obsType = obsType;
		this.obsUnitsConceptId = obsUnitsConceptId;
		this.obsDate = obsDate;
		this.providerId = 9999; // No provider
	}

    // create Observation for case where the value is a string. Pass
    // TRUE for checkVocab if the observation concept is thought to be
    // in the Standard Vocabulary. NOTE: obsConceptId will not be used if
    // checkVocab is TRUE
/*    public Observation(Long personId, String sourceObsCode, 
		       String obsValueAsString, Integer obsType, Timestamp obsDate, Boolean checkVocab, Integer obsConceptId) {
		super();
		this.personId = personId;
		if ( sourceObsCode != null && sourceObsCode.length() > 50)
		    sourceObsCode = sourceObsCode.substring(0, 49); // matches the max string length of source codes 
		this.sourceObsCode = sourceObsCode;
		if (checkVocab == true)
		    this.obsConceptId = SourceToConceptMap.getConceptId(sourceObsCode);
		else if (obsConceptId != null)
		    this.obsConceptId = obsConceptId;
		else
		    this.obsConceptId = 0;
		this.obsValueAsString = obsValueAsString;
		this.obsType = obsType;
		this.obsDate = obsDate;
		this.providerId = 9999; // No provider
	}*/


        // create Observation for case where the value is a concept. Pass
        // TRUE for checkVocab if the observation concept is thought to be
        // in the Standard Vocabulary. NOTE: obsConceptId will not be used if
        // checkVocab is TRUE
/*        public Observation(Long personId, String sourceObsCode, 
		       Long obsValueAsConceptId, Integer obsType, Timestamp obsDate, Boolean checkVocab, Integer obsConceptId) {
		super();
		this.personId = personId;
		if ( sourceObsCode != null && sourceObsCode.length() > 49)
		    sourceObsCode = sourceObsCode.substring(0, 49); // matches the max string length of source codes 
		this.sourceObsCode = sourceObsCode;
		if (checkVocab == true)
		    this.obsConceptId = SourceToConceptMap.getConceptId(sourceObsCode);
		else if (obsConceptId != null)
		    this.obsConceptId = obsConceptId;
		else
		    this.obsConceptId = 0;
		this.obsValueAsConceptId = obsValueAsConceptId;
		this.obsType = obsType;
		this.obsDate = obsDate;
		this.providerId = 9999; // No provider
	}*/

 
	/**
	 * 	 */
   				        Long obsOccurrenceId = null;
	public Long getObsOccurrenceId() {
		return obsOccurrenceId;
	}


	/**
	 * 	 */
	public void setObsOccurrenceId(Long obsOccurrenceId) {
		this.obsOccurrenceId = obsOccurrenceId;
	}


	/**
	 * 	 */
        Long personId;
	public Long getPersonId() {
		return personId;
	}


	/**
	 * 	 */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}


	/**
	 * 	 */
      String sourceObsCode;
	public String getSourceObsCode() {
		return sourceObsCode;
	}


	/**
	 * 	 */
	public void setSourceObsCode(String sourceObsCode) {
		this.sourceObsCode = sourceObsCode;
	}


	/**
	 * 	 */
		Integer obsConceptId;	
	public Integer getObsConceptId() {
		return obsConceptId;
	}


	/**
	 * 	 */
	public void setObsConceptId(Integer obsConceptId) {
		this.obsConceptId = obsConceptId;
	}


	/**
	 * 	 */
        Double obsValueAsNumber;
	public Double getObsValueAsNumber() {
		return obsValueAsNumber;
	}


	/**
	 * 	 */
	public void setObsValueAsNumber(Double obsValueAsNumber) {
		this.obsValueAsNumber = obsValueAsNumber;
	}


	/**
	 * 	 */
		Timestamp obsDate;
	public Timestamp getObsDate() {
		return obsDate;
	}


	/**
	 * 	 */
	public void setObsDate(Timestamp obsDate) {
		this.obsDate = obsDate;
	}

        Integer obsType;
	public Integer getObsType() {
		return obsType;
	}


	/**
	 * 	 */
	public void setObsType(Integer obsType) {
		this.obsType = obsType;
	}


	/**
	 * 	 */
        String obsValueAsString;
	public String getObsValueAsString() {
		return obsValueAsString;
	}


	/**
	 * 	 */
	public void setObsValueAsString(String obsValueAsString) {
		this.obsValueAsString = obsValueAsString;
	}


	/**
	 * 	 */

        Long obsValueAsConceptId;
    public Long getObsValueAsConceptId() {
		return obsValueAsConceptId;
	}


	/**
	 * 	 */
	public void setObsValueAsConceptId(Long obsValueAsConceptId) {
		this.obsValueAsConceptId = obsValueAsConceptId;
	}


	/**
	 * 	 */
	    Long obsUnitsConceptId;
	public Long getObsUnitsConceptId() {
		return obsUnitsConceptId;
	}


	/**
	 * 	 */

	public void setObsUnitsConceptId(Long obsUnitsConceptId) {
		this.obsUnitsConceptId = obsUnitsConceptId;
	}
   
        Integer providerId;
	public Integer getProviderId() {
		return providerId;
	}


	/**
	 * 	 */

	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}

        Integer qualifierConceptId;
    public Integer getQualifierConceptId() {
	return this.qualifierConceptId;
    }
    public void setQualifierConceptId(Integer qualifierConceptId) {
	this.qualifierConceptId = qualifierConceptId;
    }

        Integer observationSourceConceptId;
    public Integer getObservationSourceConceptId() {
	return this.observationSourceConceptId;
    }
    public void setObservationSourceConceptId(Integer observationSourceConceptId) {
	this.observationSourceConceptId = observationSourceConceptId;
    }

        String qualifierSourceValue;
    public String getQualifierSourceValue() {
	return this.qualifierSourceValue;
    }
    public void setQualifierSourceValue(String qualifierSourceValue) {
	this.qualifierSourceValue = qualifierSourceValue;
    }
    
}
