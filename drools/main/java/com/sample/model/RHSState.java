package com.sample.model;

import edu.pitt.dbmi.ohdsiv5.db.ExtendedDrugExposure;
import edu.pitt.dbmi.ohdsiv5.db.DrugEra;
import edu.pitt.dbmi.ohdsiv5.db.Person;


public class RHSState {
    private String stateName;
    private String state;
    private Person person;
    private ExtendedDrugExposure dexp1;
    private ExtendedDrugExposure dexp2;
	
    public RHSState() {
	super();
	// TODO Auto-generated constructor stub
    }

    public RHSState( String stateName, String state, Person person) {
    	super();
    	this.stateName = stateName;
    	this.state = state;
    	this.person = person;
    }

    public RHSState( String stateName, String state, Person person, ExtendedDrugExposure dexp1, ExtendedDrugExposure dexp2) {
        super();
        this.stateName = stateName;
        this.state = state;
        this.person = person;
        this.dexp1 = dexp1;
        this.dexp2 = dexp2;
    }

    public int hashCode()
    {
	//int hashVal = 37*(stateName.hashCode() + state.hashCode() + person.getPersonId().hashCode() + dexp1.getExtendedDrugExposureId().hashCode() + dexp2.getExtendedDrugExposureId().hashCode());
    int hashVal = 37*(stateName.hashCode() + state.hashCode() + person.getPersonId().hashCode());
    if (dexp1 != null && dexp2 != null){
	   hashVal = 37*(stateName.hashCode() + state.hashCode() + person.getPersonId().hashCode() + dexp1.getLogInfo().hashCode() + dexp2.getLogInfo().hashCode()); // changed to use hash for getLogInfo instead of getDrugExposureId. For some currently known reason hashing getDrugExposureId would confuse some drug pairs as duplicates for a specific patient: SELECT * FROM drug_exposure WHERE drug_concept_id IN(40163460,40163473,40163510) and person_id = 14046582
    }
	return hashVal;
    }
	
    public boolean equals( Object obj )
    {
	boolean flag = false;
	RHSState rhs = ( RHSState )obj;
	//if( rhs.stateName == stateName && rhs.state == state && rhs.person.getPersonId() == person.getPersonId() && rhs.dexp1.getExtendedDrugExposureId() == dexp1.getExtendedDrugExposureId() && rhs.dexp2.getExtendedDrugExposureId() == dexp2.getExtendedDrugExposureId() )
	if( rhs.stateName == stateName && rhs.state == state && rhs.person.getPersonId() == person.getPersonId())
	    flag = true;
	return flag;
    }
    
    public String getStateName() {
	return stateName;
    }
    public void setStateName(String stateName) {
	this.stateName = stateName;
    }
    public String getState() {
	return state;
    }
    public void setState(String state) {
	this.state = state;
    }
    public Person getPerson() {
	return person;
    }
    public void setPerson(Person person) {
	this.person = person;
    }
    public ExtendedDrugExposure getDexp1() {
    	return dexp1;
    }
    public void setDexp1(ExtendedDrugExposure dexp1) {
    	this.dexp1 = dexp1;
    }
    public ExtendedDrugExposure getDexp2() {
    	return dexp2;
    }
    public void setDexp2(ExtendedDrugExposure dexp2) {
    	this.dexp2 = dexp2;
    }
}
