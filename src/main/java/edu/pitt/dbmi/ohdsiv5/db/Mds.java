package edu.pitt.dbmi.ohdsiv5.db;


/**
 *  * 08.26.2014
 * 
 * This is the Mds interface. This will include every possible method in
 * MDS2 and MDS3. However, what is not in MDS2 or MDS3 will just return 
 * a null value.
 */ 

public interface Mds {
	
	/**
	 * gets the ID of the row given
	 * 	 */ 
	public Long getId();
	
	/**
	 * sets the ID of the row given
	 */
	public void setId(Long id);
	
	/**
	 * Gets the patient ID as a String
	 * 	 */
	public String getPatientId();
	
	/**
	 * Sets the patient ID
	 */
	public void setPatientId(String patientId);
	
	/**
	 * 	 */
	public String getDateOfEvent();
	
	/**
	 * Sets the date of the event that happened.
	 */
	public void setDateOfEvent(String dateOfEvent);
	
	/**
	 * Gets the gender of patient
	 * 	 */
	public String getGender();
	public void setGender(String gender);

	public String getExamType();
	public void setExamType(String examType);

	public String getLocation();
	public void setLocation(String location);
	
    	public String getRace();
	public void setRace(String race);

	public String getEntrydate();
	public void setEntrydate(String entrydate);
	
	public String getComatose();
	public void setComatose(String comatose);

	//TODO: Birthdate bigdecimal in MDS2, String in MDS3
	public String getBirthdate();
	public void setBirthdate(String birthdate);

	
	/**
	 * Gets the ICD-9 of ashd
	 */
	public String getAshd();
	 /**
	  * Sets the ICD-9 of ashd
	  * 	  */
	public void setAshd(String ashd);
	
	public String getMaritalstatus();
	public void setMaritalstatus(String maritalStatus);
	
	public String getCancer();
	public void setCancer(String cancer);
	
	public String getSchizophrenia();
	public void setSchizophrenia(String schizophrenia);
	
	public String getHipfracture();
	public void setHipfracture(String hipfracture);
	
	public String getOtherfracture();
	public void setOtherfracture(String otherfracture);
	
	public String getArthritis();
	public void setArthritis(String arthritis);

	public String getOsteoporosis();
	public void setOsteoporosis(String osteoporosis);

	public String getStroke();
	public void setStroke(String cerebrovascularaccident);

	public String getDiabetes();
	public void setDiabetes(String diabetes);

	public String getHypertension();
	public void setHypertension(String hypertension);

	public String getHypotension();
	public void setHypotension(String hypotension);
	
	public String getEmphysemacopd();
	public void setEmphysemacopd(String emphysemacopd);
	
	public String getParkinsons();
	public void setParkinsons(String parkinsons);
	
	public String getAlzheimers();
	public void setAlzheimers(String alzheimers);
	
	public String getDementiaotherthanalzheimers();
	public void setDementiaotherthanalzheimers(String dementiaotherthanalzheimers);
	
	/**
	 * gets the cognitiveSkills variable
	 * 	 */
	public String getCognitiveskills();
	/**
	 * Sets the cognitiveSkills variable
	 * 	 */
	public void setCognitiveskills(String cognitiveSkills);
	
	public String getAnxiety();
	public void setAnxiety(String anxiety);
	
	//public String getPsychosis();
	//public void setPsychosis(String psychosis);
	//mds2 no psychosis -> impossible in the perl code.....
	//because it's a summary of scores in mds3...
	//mds2 no ptsd -> not in mds2
	
	//public String getPtsd();
	//public void setPtsd(String ptsd);
	
	//public String getPsychosis();
	//public void setPsychosis(String psychosis);

	public String getHearingability();
	public void setHearingability(String hearingability);
	
	public String getHearingaid();
	public void setHearingaid(String hearingaid);

	public String getUnderstandable();
	public void setUnderstandable(String understandable);
	
	public String getUnderstandothers();
	public void setUnderstandothers(String understandothers);
	
	public String getOstomy();
	public void setOstomy(String ostomy);
	
	public String getAnemia();
	public void setAnemia(String anemia);
	
	public String getDvtpepte();
	public void setDvtpepte(String dvtpepte);

	public String getHeartfailure();
	public void setHeartfailure(String heartfailure);
	//mds2 no cirrhosis at all
	
	//public String getCirrhosis();
	//public void setCirrhosis(String cirrhosis);
	
	public String getMultidrugresistantorganism();
	public void setMultidrugresistantorganism(String multidrugresistantorganism);

	public String getPneumonia();
	public void setPneumonia(String pneumonia);
	
	public String getSepticemia();
	public void setSepticemia(String septicemia);
	
	public String getTuberculosis();
	public void setTuberculosis(String tuberculosis);
	
	public String getUrinarytractinfection();
	public void setUrinarytractinfection(String urinarytractinfection);
	
	public String getViralhepatitis();
	public void setViralhepatitis(String viralhepatitis);
	
	// woundInfectionsnoneofabove in mds2 but diff in mds3 ->deprecated
	
	//public String getWoundinfectionsnoneofabove();
	//public void setWoundInfectionsnoneofabove(String woundinfections);
	
	// hyponatremia and hyperkalemia not in mds2 -> new in MDS3
	
	/////**
	 ////* get the icd-9 of hyponatremia
	 ////* 
	 ////* In MDS3 not MDS2
	 ////* 	 ////*/ 
	////public String getHyponatremia();
	
	/////**
	 ////* Set the ICD-9 of Hyponatremia
	 ////* 
	 ////* In Mds3 not MDS2
	 ////*/ 
	////public void setHyponatremia(String hyponatremia);
	//// no hyperlipedemia in mds2
	
	/////**
	 ////* Gets the ICD-9 of Hyperlipidemia
	 ////* 
	 ////* in MDS3 not MDS2
	 ////* 
	 ////* 	 ////*/
	////public String getHyperlipidemia();
	
	/////**
	 ////* Sets the code for Hyperlipidemia
	 ////* 
	 ////* In MDS3 nto MDS2
	 ////*/
	 ////public void setHyperlipidemia(String hyperlipidemia);

	// mds3 has thyroidDisorder instead of hte two others
	
	/////**
	 ////* MDS3 getter for hypo/hyperthyroidism
	 ////* 
	 ////* 	 ////*/
	////public String getThyroiddisorder();
	
	/////**
	 ////* MDS3 setter for Hypo/hyperthyroidism
	 ////*/
	////public void setThyroiddisorder(String thyroiddisorder);
	
	/////**
	 ////* MDS2 getter for hyperthyroidism
	 ////* 
	 ////* 	 ////*/
	////public String getHyperthyroidism();
	
	/////**
	 ////* MDS2 setter for hyperthyroidism
	 ////*/
	////public void setHyperthyroidism(String hyperthyroidism);
	
	/////**
	 ////* MDS2 getter for hypothyroidism
	 ////* 
	 ////* 	 ////*/ 
	////public String getHypothyroidism();
	
	/////**
	 ////* MDS2 setter for hypothyroidism
	 ////*/ 
	////public void setHypothyroidism(String hypothyroidism);
	
	public String getAphasia();
	public void setAphasia(String aphasia);
	
	public String getCerebralpalsy();
	public void setCerebralpalsy(String cerebralpalsy);
	
	public String getHemiplegiahemiparesis();
	public void setHemiplegiahemiparesis(String hemiplegiahemiparesis);
	
	public String getParaplegia();
	public void setParaplegia(String paraplegia);
	
	public String getQuadriplegia();
	public void setQuadriplegia(String quadriplegia);
	
	public String getMultiplesclerosis();
	public void setMultiplesclerosis(String multiplesclerosis);
	
	/////**
	 ////* In MDS3 not MDS2
	 ////* 	 ////*/
	////public String getHuntingtonsDisease();
	
	/////**
	 ////* sets the code for Huntington's Disease.
	 ////* 
	 ////* In MDS3 not in MDS2
	 ////*/
	////public void setHuntingtonsDisease(String huntingtonsdisease);
	
	/////**
	 ////* In MDS3 not MDS2
	 ////* 	 ////*/
	////public String getTourettessyndrome();
	
	/////**
	 ////* Sets the code for Tourette's Syndrome
	 ////* 
	 ////* In MDS3 not MDS2
	 ////*/
	////public void setTourettessyndrome(String tourettessyndrome);
	
	public String getSeizuredisorderorepilepsy();
	public void setSeizuredisorderorepilepsy(String seizuredisorderorepilepsy);
	
	public String getTraumaticbraininjury();
	public void setTraumaticbraininjury(String traumaticbraininjury);
	
	public String getDepression();
	public void setDepression(String depression);
	
	public String getManicdepression();
	public void setManicdepression(String manicdepression);
	
	/////**
	 //* MDS3 getter for falls since admission
	 //* 
	 //* 	 //*/ 
	//public String getFallssinceadmission();
	
	/////**
	 //* MDS3 setter for falls since admission
	 //*/ 
	//public void setFallssinceadmission(String fallssincelastadmission);
	
	//public String getF
	
	
}
