package edu.pitt.dbmi.ohdsiv5.db;
import java.math.BigDecimal;



/**
 * Mds3 
 */
public class Mds3 {
	// Fields    
	Long id;
	private String patientId;
	private String dateOfEvent;
	private String examType;
	private String gender;
	private String birthdate;
	private String location;
	private String race;
	private String marital_status;
	private String cancer;
	private String schizophrenia;
	private String fallssincelastadmission;
	private String falls1monthago;
	private String falls2to6monthsago;
	private String hipfracture;
	private String pathologicalbonefracture;
	private String arthritis;
	private String osteoporosis;
	private String cerebrovascularaccident;
	private String diabetes;
	private String hypertension;
	private String hypotension;
	private String ephysemacopd;
	private String ashd;
	private String gerdorgiulcer;
	private String parkinsons;
	private String alzheimers;
	private String dementiaotherthanalzheimers;
	private String cognitiveskills;
	private String delirium;
	private String anxiety;
	private String depressionScale;
	private String psychosis;
	private String ptsd;
	private BigDecimal adldependence;
	private String painintensity;
	private String behavioralsymptoms;
	private String infectionofthefoot; // started here 10/1
    private String providertype;
    private String federalobraassessment;
    private String ppsassessment;
    private String otherppsomraassessment;
    private String firstassessment;
    private String entrydischargereport;
    private String preaddmissionscreening; //preadmission is somehow spelled wrong lol....
    private String entrydate;
    private String typeofentry;
    private String enteredform;
    private String dischargedate;
    private String dischargestatus;
    private String assessmentreferencedate;
    private String medicarecoverage;
    private String startofmedicarestay;
    private String endofmedicarestay;
    private String comatose;
    private String hearingability;
    private String hearingaid;
    private String speechclarity;
    private String understandable;
    private String understandothers;
    private String vision;
    private String correctivelenses;
    private String doesresidentwander;
    private String wanderisintrusive;
    private String indwellingcatheter;
    private String externalcatheter;
    private String ostomy; // end the strings now onto ICDs
    private String constipation;
    private String anemia;
    private String fibrillation;
    private String dvtpepte;
    private String heartfailure;
    private String cirrhosis;
    private String benignprostatichyperplasia;
    private String neurogenicbladder;
    private String obstructiveuropathy;
    private String multidrugresistantorganism;
    private String pneumonia;
    private String septicemia;
    private String tuberculosis;
    private String urinarytractinfection;
    private String viralhepatitis;
    private String woundinfectionnotfoot;
    private String hyponatremia;
    private String hyperkalemia;
    private String hyperlipidemia;
    private String thyroiddisorder;
    private String aphasia;
    private String cerebralpalsy;
    private String hemiplegiahemiparesis; // should be hemiplegia...
    private String paraplegia;
    private String quadriplegia;
    private String multiplesclerosis;
    private String huntingtonsdisease;
    private String tourettessyndrome;
    private String seizuredisorderorepilepsy;
    private String traumaticbraininjury;
    private String depression;
    private String manicdepression;
    private String bimssum;
    private String fallsmajorinjury;
    private String fallsminorinjury;
    private String fallswithnoinjury;
    private String conductPainAssessmentInterv;
    private String painInLast5Days;
    private String painFrequencyLast5Days;
    private String painIntensity;
    private String painNonVerbal;
    private String painMedicationRegimen;
    private String receivedPRNPainMedication;
    private String receivedNonMedicationForPain;
    private String impairedBedMobility;
    private String impairedTransfer;
    private String malnutrition;
    private String pressureUlcerStage1;
    private String pressureUlcerStage2;
    private String pressureUlcerStage3;
    private String pressureUlcerStage4;
    private String antipsychoticMedication;
    private String antianxietyMedication;
    private String antidepressantMedication;
    private String hypnoticMedication;
    private String anticoagulantMedication;
    private String antibioticMedication;
    private String diureticMedication;
    private String behavioralSymptomsToOthers;
    private String depressionNHCompare;
    private String excessiveWeightLoss;
    private String fever;
    private String vomiting;
    private String dehydrated;
    private String internalBleeding;
    private String noProblemConditions;
    private String riskOfPressureUlcers;
    private String bowelIncontinence;
    private String urinaryIncontinence;

    // Constructors

    /** default constructor */
    public Mds3() {
    }

    
    /** full constructor */
    public Mds3(String patientId, String dateOfEvent, String examType, String gender, String birthdate, String location, String race, String marital_status, String cancer, String schizophrenia, String fallssincelastadmission, String falls1monthago, String falls2to6monthsago, String hipfracture, String pathologicalbonefracture, String arthritis, String osteoporosis, String cerebrovascularaccident, String diabetes, String hypertension, String hypotension, String ephysemacopd, String ashd, String gerdorgiulcer, String parkinsons, String alzheimers, String dementiaotherthanalzheimers, String cognitiveskills, String delirium, String anxiety, String depressionScale, String psychosis, String ptsd, BigDecimal adldependence, String painintensity, String behavioralsymptoms, String infectionofthefoot, String providertype, String federalobraassessment, String ppsassessment, String otherppsomraassessment, String firstassessment, String entrydischargereport, String preaddmissionscreening, String entrydate, String typeofentry, String enteredform, String dischargedate, String dischargestatus, String assessmentreferencedate, String medicarecoverage, String startofmedicarestay, String endofmedicarestay, String comatose, String hearingability, String hearingaid, String speechclarity, String understandable, String understandothers, String vision, String correctivelenses, String doesresidentwander, String wanderisintrusive, String indwellingcatheter, String externalcatheter, String ostomy, String constipation, String anemia, String fibrillation, String dvtpepte, String heartfailure, String cirrhosis, String benignprostatichyperplasia, String neurogenicbladder, String obstructiveuropathy, String multdrugresistantorganism, String pneumonia, String septicemia, String tuberculosis, String urinarytractinfection, String viralhepatitis, String woundinfectionnotfoot, String hyponatremia, String hyperkalemia, String hyperlipidemia, String thyroiddisorder, String aphasia, String cerebralpalsy, String hemiplegiahemiparesis, String paraplegia, String quadriplegia, String multiplesclerosis, String huntingtonsdisease, String tourettessyndrome, String seizuredisorderorepilepsy, String traumaticbraininjury, String depression, String manicdepression, String fallsmajorinjury, String fallsminorinjury, String fallswithnoinjury, String conductPainAssessmentInterv, String painInLast5Days, String painFrequencyLast5Days, String painIntensity, String painNonVerbal, String painMedicationRegimen, String receivedPRNPainMedication, String receivedNonMedicationForPain, String impairedBedMobility, String impairedTransfer, String malnutrition, String pressureUlcerStage1, String pressureUlcerStage2, String pressureUlcerStage3, String pressureUlcerStage4, String antipsychoticMedication, String antianxietyMedication, String antidepressantMedication, String hypnoticMedication, String anticoagulantMedication, String antibioticMedication, String diureticMedication, String behavioralSymptomsToOthers, String depressionNHCompare, String excessiveWeightLoss, String fever, String vomiting, String dehydrated, String internalBleeding, String noProblemConditions, String riskOfPressureUlcers, String bowelIncontinence, String urinaryIncontinence) {
    super();
	this.patientId = patientId;
    this.dateOfEvent = dateOfEvent;
	this.examType = examType;
	this.gender = gender;
	this.birthdate = birthdate;
	this.location = location;
	this.race = race;
	this.marital_status = marital_status;
	this.cancer = cancer;
	this.schizophrenia = schizophrenia;
	this.fallssincelastadmission = fallssincelastadmission;
	this.falls1monthago = falls1monthago;
	this.falls2to6monthsago = falls2to6monthsago;
	this.hipfracture = hipfracture;
	this.pathologicalbonefracture = pathologicalbonefracture;
	this.arthritis = arthritis;
	this.osteoporosis = osteoporosis;
	this.cerebrovascularaccident = cerebrovascularaccident;
	this.diabetes = diabetes;
	this.hypertension = hypertension;
	this.hypotension = hypotension;
	this.ephysemacopd = ephysemacopd;
	this.ashd = ashd;
	this.gerdorgiulcer = gerdorgiulcer;
	this.parkinsons = parkinsons;
	this.alzheimers = alzheimers;
	this.dementiaotherthanalzheimers = dementiaotherthanalzheimers;
	this.cognitiveskills = cognitiveskills;
	this.delirium = delirium;
	this.anxiety = anxiety;
	this.depressionScale = depressionScale;
	this.psychosis = psychosis;
	this.ptsd = ptsd;
	this.adldependence = adldependence;
	this.painintensity = painintensity;
	this.behavioralsymptoms = behavioralsymptoms;
	this.infectionofthefoot = infectionofthefoot;
	this.providertype = providertype;
	this.federalobraassessment = federalobraassessment;
	this.ppsassessment = ppsassessment;
	this.otherppsomraassessment = otherppsomraassessment;
	this.firstassessment = firstassessment;
	this.entrydischargereport = entrydischargereport;
	this.preaddmissionscreening = preaddmissionscreening;
	this.entrydate = entrydate;
	this.typeofentry = typeofentry;
	this.enteredform = enteredform;
	this.dischargedate = dischargedate;
	this.dischargestatus = dischargestatus;
	this.assessmentreferencedate = assessmentreferencedate;
	this.medicarecoverage = medicarecoverage;
	this.startofmedicarestay = startofmedicarestay;
	this.endofmedicarestay = endofmedicarestay;
	this.comatose = comatose;
	this.hearingability = hearingability;
	this.hearingaid = hearingaid;
	this.speechclarity = speechclarity;
	this.understandable = understandable;
	this.understandothers = understandothers;
	this.vision = vision;
	this.correctivelenses = correctivelenses;
	this.doesresidentwander = doesresidentwander;
	this.wanderisintrusive = wanderisintrusive;
	this.indwellingcatheter = indwellingcatheter;
	this.externalcatheter = externalcatheter;
	this.ostomy = ostomy;
	this.constipation = constipation;
	this.anemia = anemia;
	this.fibrillation = fibrillation;
	this.dvtpepte = dvtpepte;
	this.heartfailure = heartfailure;
	this.cirrhosis = cirrhosis;
	this.benignprostatichyperplasia = benignprostatichyperplasia;
	this.neurogenicbladder = neurogenicbladder;
	this.obstructiveuropathy = obstructiveuropathy;
	this.multidrugresistantorganism = multidrugresistantorganism;
	this.pneumonia = pneumonia;
	this.septicemia = septicemia;
	this.tuberculosis = tuberculosis;
	this.urinarytractinfection = urinarytractinfection;
	this.viralhepatitis = viralhepatitis;
	this.woundinfectionnotfoot = woundinfectionnotfoot;
	this.hyponatremia = hyponatremia;
	this.hyperkalemia = hyperkalemia;
	this.hyperlipidemia = hyperlipidemia;
	this.thyroiddisorder = thyroiddisorder;
	this.aphasia = aphasia;
	this.cerebralpalsy = cerebralpalsy;
	this.hemiplegiahemiparesis = hemiplegiahemiparesis;
	this.paraplegia = paraplegia;
	this.quadriplegia = quadriplegia;
	this.multiplesclerosis = multiplesclerosis;
	this.huntingtonsdisease = huntingtonsdisease;
	this.tourettessyndrome = tourettessyndrome;
	this.seizuredisorderorepilepsy = seizuredisorderorepilepsy;
	this.traumaticbraininjury = traumaticbraininjury;
	this.depression = depression;
	this.manicdepression = manicdepression;
	this.fallsmajorinjury = fallsmajorinjury;
	this.fallsminorinjury = fallsminorinjury;
	this.fallswithnoinjury = fallswithnoinjury;
	this.conductPainAssessmentInterv = conductPainAssessmentInterv;
	this.painInLast5Days = painInLast5Days;
	this.painFrequencyLast5Days = painFrequencyLast5Days;
	this.painIntensity = painIntensity;
	this.painNonVerbal = painNonVerbal;
	this.painMedicationRegimen = painMedicationRegimen;
	this.receivedPRNPainMedication = receivedPRNPainMedication;
	this.receivedNonMedicationForPain = receivedNonMedicationForPain;
	this.impairedBedMobility = impairedBedMobility;
	this.impairedTransfer = impairedTransfer;
	this.malnutrition = malnutrition;
	this.pressureUlcerStage1 = pressureUlcerStage1;
	this.pressureUlcerStage2 = pressureUlcerStage2;
	this.pressureUlcerStage3 = pressureUlcerStage3;
	this.pressureUlcerStage4 = pressureUlcerStage4;
	this.antipsychoticMedication = antipsychoticMedication;
	this.antianxietyMedication = antianxietyMedication;
	this.antidepressantMedication = antidepressantMedication;
	this.hypnoticMedication = hypnoticMedication;
	this.anticoagulantMedication = anticoagulantMedication;
	this.antibioticMedication = antibioticMedication;
	this.diureticMedication = diureticMedication;
	this.behavioralSymptomsToOthers = behavioralSymptomsToOthers;
	this.depressionNHCompare = depressionNHCompare;
	this.excessiveWeightLoss = excessiveWeightLoss;
	this.fever = fever;
	this.vomiting = vomiting;
	this.dehydrated = dehydrated;
	this.internalBleeding = internalBleeding;
	this.noProblemConditions = noProblemConditions;
	this.riskOfPressureUlcers = riskOfPressureUlcers;
	this.bowelIncontinence = bowelIncontinence;
	this.urinaryIncontinence = urinaryIncontinence;
    }
   
    // Property accessors
    //     //     //        //    } )
                    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

        public String getPatientId() {
        return this.patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

        public String getDateOfEvent() {
        return this.dateOfEvent;
    }
    
    public void setDateOfEvent(String dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

        public String getExamType() {
        return this.examType;
    }
    
    public void setExamType(String examType) {
        this.examType = examType;
    }
	
    
    public String getGender() {
        return this.gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }

        public String getBirthdate() {
        return this.birthdate;
    }
    
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

      public String getLocation() {
	    return location;
	}
    public void setLocation(String location) {
	this.location = location;
    }

      public String getRace() {
	    return race;
	}
    public void setRace(String race) {
	this.race = race;
    }

      public String getMaritalstatus() {
	    return marital_status;
	}
    public void setMaritalstatus(String marital_status) {
	this.marital_status = marital_status;
    }


      public String getCancer() {
	    return cancer;
	}
    public void setCancer(String cancer) {
	this.cancer = cancer;
    }


      public String getSchizophrenia() {
	    return schizophrenia;
	}
    public void setSchizophrenia(String schizophrenia) {
	this.schizophrenia = schizophrenia;
    }

      public String getFallssinceadmission() {
	    return fallssincelastadmission;
	}
    public void setFallssinceadmission(String fallssincelastadmission) {
	this.fallssincelastadmission = fallssincelastadmission;
    }


      public String getFallsonemonthago() {
	    return falls1monthago;
	}
    public void setFallsonemonthago(String falls1monthago) {
	this.falls1monthago = falls1monthago;
    }


      public String getFallstwotosixmonths() {
	    return falls2to6monthsago;
	}
    public void setFallstwotosixmonths(String falls2to6monthsago) {
	this.falls2to6monthsago = falls2to6monthsago;
    }

      public String getHipfracture() {
	    return hipfracture;
	}
    public void setHipfracture(String hipfracture) {
	this.hipfracture = hipfracture;
    }

      public String getOtherfracture() {
	    return pathologicalbonefracture;
	}
    public void setOtherfracture(String pathologicalbonefracture) {
	this.pathologicalbonefracture = pathologicalbonefracture;
    }

      public String getArthritis() {
	    return arthritis;
	}
    public void setArthritis(String arthritis) {
	this.arthritis = arthritis;
    }

      public String getOsteoporosis() {
	    return osteoporosis;
	}
    public void setOsteoporosis(String osteoporosis) {
	this.osteoporosis = osteoporosis;
    }


      public String getStroke() {
	    return cerebrovascularaccident;
	}
    public void setStroke(String cerebrovascularaccident) {
	this.cerebrovascularaccident = cerebrovascularaccident;
    }

      public String getDiabetes() {
	    return diabetes;
	}
    public void setDiabetes(String diabetes) {
	this.diabetes = diabetes;
    }


      public String getHypertension() {
	    return hypertension;
	}
    public void setHypertension(String hypertension) {
	this.hypertension = hypertension;
    }

      public String getHypotension() {
	    return hypotension;
	}
    public void setHypotension(String hypotension) {
	this.hypotension = hypotension;
    }


      public String getEmphysemacopd() {
	    return ephysemacopd;
	}
    public void setEmphysemacopd(String ephysemacopd) {
	this.ephysemacopd = ephysemacopd;
    }

      public String getAshd() {
	    return ashd;
	}
    public void setAshd(String ashd) {
	this.ashd = ashd;
    }


      public String getGerdorgiulcer() {
	    return gerdorgiulcer;
	}
    public void setGerdorgiulcer(String gerdorgiulcer) {
	this.gerdorgiulcer = gerdorgiulcer;
    }


      public String getParkinsons() {
	    return parkinsons;
	}
    public void setParkinsons(String parkinsons) {
	this.parkinsons = parkinsons;
    }


      public String getAlzheimers() {
	    return alzheimers;
	}
    public void setAlzheimers(String alzheimers) {
	this.alzheimers = alzheimers;
    }


      public String getDementiaotherthanalzheimers() {
	    return dementiaotherthanalzheimers;
	}
    public void setDementiaotherthanalzheimers(String dementiaotherthanalzheimers) {
	this.dementiaotherthanalzheimers = dementiaotherthanalzheimers;
    }

      public String getCognitiveskills() {
	    return cognitiveskills;
	}
    public void setCognitiveskills(String cognitiveskills) {
	this.cognitiveskills = cognitiveskills;
    }


      public String getDelirium() {
	    return delirium;
	}
    public void setDelirium(String delirium) {
	this.delirium = delirium;
    }

      public String getAnxiety() {
	    return anxiety;
	}
    public void setAnxiety(String anxiety) {
	this.anxiety = anxiety;
    }
    
    /// NOTE: as of 12/31/2014 - this is replaced by DepressionNHCompare
    // jlj69: had to change this to match what this is, the other depression getter is for icd9
   //    // public String getDepressionScale() {
   // 	    return depressionScale;
   // 	}
   //  public void setDepressionScale(String depressionScale) {
   // 	this.depressionScale = depressionScale;
   //  }


      public String getPsychosis() {
	    return psychosis;
	}
    public void setPsychosis(String psychosis) {
	this.psychosis = psychosis;
    }

      public String getPtsd() {
	    return ptsd;
	}
    public void setPtsd(String ptsd) {
	this.ptsd = ptsd;
    }

      public BigDecimal getAdldependence() {
	    return adldependence;
	}
    public void setAdldependence(BigDecimal adldependence) {
	this.adldependence = adldependence;
    }

      public String getBehavioralsymptoms() {
	    return behavioralsymptoms;
	}
    public void setBehavioralsymptoms(String behavioralsymptoms) {
	this.behavioralsymptoms = behavioralsymptoms;
    }
    // jlj69: start here 10/1/2013
        public String getInfectionOfTheFoot() {
	return infectionofthefoot;
    }
    
    public void setInfectionOfTheFoot(String infectionofthefoot) {
	this.infectionofthefoot = infectionofthefoot;
    }

        public String getProviderType() {
	return providertype;
    }
    
    public void setProviderType(String providertype) {
	this.providertype = providertype;
    }
    
    	public String getFederalObraAssessment() {
	return federalobraassessment;
    }

    public void setFederalObraAssessment(String federalobraassessment) {
	this.federalobraassessment = federalobraassessment;
    }
    
    	public String getPpsassessment() {
	return ppsassessment;
    }
   
    public void setPpsassessment(String ppsassessment) {
	this.ppsassessment = ppsassessment;
    }

    	public String getOtherppsomraassessment() {
	return otherppsomraassessment;
    }
    
    public void setOtherppsomraassessment(String otherppsomraassessment) {
	this.otherppsomraassessment = otherppsomraassessment;
    }

    	public String getFirstassessment() {
	return firstassessment;
    }
    
    public void setFirstassessment(String firstassessment) {
	this.firstassessment = firstassessment;
    }
   
    	public String getEntrydischargereport() {
	return entrydischargereport;
    }

    public void setEntrydischargereport(String entrydischargereport) {
	this.entrydischargereport = entrydischargereport;
    }

    	public String getPreaddmissionscreening() {
	return preaddmissionscreening;
    }

    public void setPreaddmissionscreening(String preaddmissionscreening) {
	this.preaddmissionscreening = preaddmissionscreening;
    }

    	public String getEntrydate() {
	return entrydate;
    }

    public void setEntrydate(String entrydate) {
	this.entrydate= entrydate;
    }

    	public String getTypeofentry() {
	return typeofentry;
    }

    public void setTypeofentry(String typeOfEntry) {
	this.typeofentry = typeofentry;
    }

    	public String getEnteredform() {
	return enteredform;
    }

    public void setEnteredform(String enteredform) {
	this.enteredform = enteredform;
    }

    	public String getDischargedate() {
	return dischargedate;
    }

    public void setDischargedate(String dischargedate) {
	this.dischargedate = dischargedate;
    }

    	public String getDischargestatus() {
	return dischargestatus;
    }

    public void setDischargestatus(String dischargestatus) {
	this.dischargestatus = dischargestatus;
    }

    	public String getAssessmentreferencedate() {
	return assessmentreferencedate;
    }

    public void setAssessmentreferencedate(String assessmentreferencedate) {
	this.assessmentreferencedate = assessmentreferencedate;
    }

    	public String getMedicarecoverage() {
	return medicarecoverage;
    }
    
    public void setMedicarecoverage(String medicareCoverage) {
	this.medicarecoverage = medicarecoverage;
    }

    	public String getStartofmedicarestay() { 
	return startofmedicarestay;
    }

    public void setStartofmedicarestay(String startofmedicarestay) {
	this.startofmedicarestay = startofmedicarestay;
    }

    	public String getEndofmedicarestay() {
	return endofmedicarestay;
    }

    public void setEndofmedicarestay(String endofmedicarestay) {
	this.endofmedicarestay = endofmedicarestay;
    }

    	public String getComatose() {
	return comatose;
    }
    
    public void setComatose(String comatose) {
	this.comatose = comatose;
    }

    	public String getHearingability() {
	return hearingability;
    }

    public void setHearingability(String hearingability) {
	this.hearingability = hearingability;
    }

    	public String getHearingaid() {
	return hearingaid;
    }
    
    public void setHearingaid(String hearingaid) {
	this.hearingaid = hearingaid;
    }

    	public String getSpeechclarity() {
	return speechclarity;
    }

    public void setSpeechclarity(String speechclarity) {
	this.speechclarity = speechclarity;
    }

    	public String getUnderstandable() {
	return understandable;
    }
    
    public void setUnderstandable(String understandable) {
	this.understandable = understandable;
    }

    	public String getUnderstandothers() {
	return understandothers;
    }

    public void setUnderstandothers(String understandothers) {
	this.understandothers = understandothers;
    }

    	public String getVision() {
	return vision;
    }
   
    public void setVision(String vision) {
	this.vision = vision;
    }

    	public String getCorrectivelenses() {
	return correctivelenses;
    }
    
    public void setCorrectivelenses(String correctivelenses) {
	this.correctivelenses = correctivelenses;
    }

    	public String getDoesresidentwander() {
	return doesresidentwander;
    }

    public void setDoesresidentwander(String doesresidentwander) {
	this.doesresidentwander = doesresidentwander;
    }

    	public String getWanderisintrusive() {
	return wanderisintrusive;
    }

    public void setWanderisintrusive(String wanderisintrusive) {
	this.wanderisintrusive = wanderisintrusive;
    }

    	public String getIndwellingcatheter() {
	return indwellingcatheter;
    }

    public void setIndwellingcatheter(String indwellingcatheter) {
	this.indwellingcatheter = indwellingcatheter;
    }

    	public String getExternalcatheter() {
	return externalcatheter;
    }

    public void setExternalcatheter(String externalcatheter) {
	this.externalcatheter = externalcatheter;
    }

    	public String getOstomy() {
	return ostomy;
    }
    
    public void setOstomy(String ostomy) {
	this.ostomy = ostomy;
    }

    	public String getConstipation() {
	return constipation;
    }

    public void setConstipation(String constipation) {
	this.constipation = constipation;
    }

    	public String getAnemia() {
	return anemia;
    }

    public void setAnemia(String anemia) {
	this.anemia = anemia;
    }
	
	//TODO: need to do something about this since it's missing in the sql, ctl, and python script
	//public String getFibrillation() {
	//return fibrillation;
    //}

    //public void setFibrillation(String fibrillation) {
	//this.fibrillation = fibrillation;
    //}

    	public String getDvtpepte() {
	return dvtpepte;
    }

    public void setDvtpepte(String dvtpepte) {
	this.dvtpepte = dvtpepte;
    }

    	public String getHeartfailure() {
	return heartfailure;
    }

    public void setHeartfailure(String heartfailure) {
	this.heartfailure = heartfailure;
    }

    	public String getCirrhosis() {
	return cirrhosis;
    }
    
    public void setCirrhosis(String cirrhosis) {
	this.cirrhosis = cirrhosis;
    }

    	public String getBenignprostatichyperplasia() {
	return benignprostatichyperplasia;
    }

    public void setBenignprostatichyperplasia(String benignprostatichyperplasia) {
	this.benignprostatichyperplasia = benignprostatichyperplasia;
    }

    	public String getNeurogenicbladder() {
	return neurogenicbladder;
    }
    
    public void setNeurogenicbladder(String neurogenicbladder) {
	this.neurogenicbladder = neurogenicbladder;
    }

    	public String getObstructiveuropathy() {
	return obstructiveuropathy;
    }

    public void setObstructiveuropathy(String obstructiveuropathy) {
	this.obstructiveuropathy = obstructiveuropathy;
    }

    	public String getMultidrugresistantorganism() {
	return multidrugresistantorganism;
    }

    public void setMultidrugresistantorganism(String multidrugresistantorganism) {
	this.multidrugresistantorganism = multidrugresistantorganism;
    }

    	public String getPneumonia() {
	return pneumonia;
    }

    public void setPneumonia(String pneumonia) {
	this.pneumonia = pneumonia;
    }

    	public String getSepticemia() {
	return septicemia;
    }

    public void setSepticemia(String septicemia) {
	this.septicemia = septicemia;
    }

    	public String getTuberculosis() {
	return tuberculosis;
    }

    public void setTuberculosis(String tuberculosis) {
	this.tuberculosis = tuberculosis;
    }

    	public String getUrinarytractinfection() {
	return urinarytractinfection;
    }

    public void setUrinarytractinfection(String urinarytractinfection) {
	this.urinarytractinfection = urinarytractinfection;
    }

    	public String getViralhepatitis() {
	return viralhepatitis;
    }
    
    public void setViralhepatitis(String viralhepatitis) {
	this.viralhepatitis = viralhepatitis;
    }

    	public String getWoundinfectionnotfoot() {
	return woundinfectionnotfoot;
    }
    
    public void setWoundinfectionnotfoot(String woundinfectionnotfoot) {
	this.woundinfectionnotfoot = woundinfectionnotfoot;
    }

    	public String getHyponatremia() {
	return hyponatremia;
    }

    public void setHyponatremia(String hyponatremia) {
	this.hyponatremia = hyponatremia;
    }

    	public String getHyperkalemia() {
	return hyperkalemia;
    }

    public void setHyperkalemia(String hyprkalemia) {
	this.hyperkalemia = hyperkalemia;
    }

    	public String getHyperlipidemia() {
	return hyperlipidemia;
    }

    public void setHyperlipidemia(String hyperlipidemia) {
	this.hyperlipidemia = hyperlipidemia;
    }

    // TODO: I have ThyroidDisorder but not hyper and hypo thyroidism
    	public String getThyroiddisorder() {
	return thyroiddisorder;
    }
    
    public void setThyroiddisorder(String thyroiddisorder) {
	this.thyroiddisorder = thyroiddisorder;
    }

    	public String getAphasia() {
	return aphasia;
    }
    
    public void setAphasia(String aphasia) {
	this.aphasia = aphasia;
    }

    	public String getCerebralpalsy() {
	return cerebralpalsy;
    }

    public void setCerebralpalsy(String cerebralpalsy) {
	this.cerebralpalsy = cerebralpalsy;
    }

    	public String getHemiplegiahemiparesis() {
	return hemiplegiahemiparesis;
    }

    public void setHemiplegiahemiparesis(String hemiplegiahemiparesis) {
	this.hemiplegiahemiparesis = hemiplegiahemiparesis;
    }

    	public String getParaplegia() {
	return paraplegia;
    }

    public void setParaplegia(String paraplegia) {
	this.paraplegia = paraplegia;
    }

    	public String getQuadriplegia() {
	return quadriplegia;
    }

    public void setQuadriplegia(String quadriplegia) {
	this.quadriplegia = quadriplegia;
    }

    	public String getMultiplesclerosis() {
	return multiplesclerosis;
    }

    public void setMultiplesclerosis(String multiplesclerosis) {
	this.multiplesclerosis = multiplesclerosis;
    }

    	public String getHuntingtonsDisease() {
	return huntingtonsdisease;
    }
    
    public void setHuntingtonsDisease(String huntingtonsdisease) {
	this.huntingtonsdisease = huntingtonsdisease;

    }

    	public String getTourettessyndrome() {
	return tourettessyndrome;
    }

    public void setTourettessyndrome(String tourettessyndrome) {
	this.tourettessyndrome = tourettessyndrome;
    }

    	public String getSeizuredisorderorepilepsy() {
	return seizuredisorderorepilepsy;
    }

    public void setSeizuredisorderorepilepsy(String seizuredisorderorepilepsy) {
	this.seizuredisorderorepilepsy = seizuredisorderorepilepsy;
    }

    	public String getTraumaticbraininjury() {
	return traumaticbraininjury;
    }

    public void setTraumaticbraininjury(String traumaticbraininjury) {
	this.traumaticbraininjury = traumaticbraininjury;
    }

    	public String getDepression() {
	return depression;
    }

    public void setDepression(String depression) {
	this.depression = depression;
    }

    	public String getManicdepression() {
	return manicdepression;
    }

    public void setManicdepression(String manicdepression) {
	this.manicdepression = manicdepression;
    }

        public String getBimssum() {
	return bimssum;
    }

    public void setBimssum(String bimssum) {
	this.bimssum = bimssum;
    }

        public String getFallsmajorinjury() {
	return fallsmajorinjury;
    }

    public void setFallsmajorinjury(String fallsmajorinjury) {
	this.fallsmajorinjury = fallsmajorinjury;
    }

        public String getFallsminorinjury() {
	return fallsminorinjury;
    }

    public void setFallsminorinjury(String fallsminorinjury) {
	this.fallsminorinjury = fallsminorinjury;
    }

        public String getFallswithnoinjury() {
	return fallswithnoinjury;
    }

    public void setFallswithnoinjury(String fallswithnoinjury) {
	this.fallswithnoinjury = fallswithnoinjury;
    }

        public String getConductPainAssessmentInterv() {
	return conductPainAssessmentInterv;
    }

    public void setConductPainAssessmentInterv(String conductPainAssessmentInterv) {
	this.conductPainAssessmentInterv = conductPainAssessmentInterv;
    }

        public String getPainInLast5Days() {
	return painInLast5Days;
    }

    public void setPainInLast5Days(String painInLast5Days) {
	this.painInLast5Days = painInLast5Days;
    }

        public String getPainFrequencyLast5Days() {
	return painFrequencyLast5Days;
    }

    public void setPainFrequencyLast5Days(String painFrequencyLast5Days) {
	this.painFrequencyLast5Days = painFrequencyLast5Days;
    }


        public String getPainIntensity() {
	return painIntensity;
    }

    public void setPainIntensity(String painIntensity) {
	this.painIntensity = painIntensity;
    }

        public String getPainNonVerbal() {
	return painNonVerbal;
    }

    public void setPainNonVerbal(String painNonVerbal) {
	this.painNonVerbal = painNonVerbal;
    }


        public String getPainMedicationRegimen() {
	return painMedicationRegimen;
    }

    public void setPainMedicationRegimen(String painMedicationRegimen) {
	this.painMedicationRegimen = painMedicationRegimen;
    }

        public String getReceivedPRNPainMedication() {
	return receivedPRNPainMedication;
    }

    public void setReceivedPRNPainMedication(String receivedPRNPainMedication) {
	this.receivedPRNPainMedication = receivedPRNPainMedication;
    }

        public String getReceivedNonMedicationForPain() {
	return receivedNonMedicationForPain;
    }

    public void setReceivedNonMedicationForPain(String receivedNonMedicationForPain) {
	this.receivedNonMedicationForPain = receivedNonMedicationForPain;
    }

        public String getImpairedBedMobility() {
	return impairedBedMobility;
    }

    public void setImpairedBedMobility(String impairedBedMobility) {
	this.impairedBedMobility = impairedBedMobility;
    }

        public String getImpairedTransfer() {
	return impairedTransfer;
    }

    public void setImpairedTransfer(String impairedTransfer) {
	this.impairedTransfer = impairedTransfer;
    }

        public String getMalnutrition() {
	return malnutrition;
    }

    public void setMalnutrition(String malnutrition) {
	this.malnutrition = malnutrition;
    }

        public String getPressureUlcerStage1() {
	return pressureUlcerStage1;
    }

    public void setPressureUlcerStage1(String pressureUlcerStage1) {
	this.pressureUlcerStage1 = pressureUlcerStage1;
    }

        public String getPressureUlcerStage2() {
	return pressureUlcerStage2;
    }

    public void setPressureUlcerStage2(String pressureUlcerStage2) {
	this.pressureUlcerStage2 = pressureUlcerStage2;
    }


        public String getPressureUlcerStage3() {
	return pressureUlcerStage3;
    }

    public void setPressureUlcerStage3(String pressureUlcerStage3) {
	this.pressureUlcerStage3 = pressureUlcerStage3;
    }


        public String getPressureUlcerStage4() {
	return pressureUlcerStage4;
    }

    public void setPressureUlcerStage4(String pressureUlcerStage4) {
	this.pressureUlcerStage4 = pressureUlcerStage4;
    }

        public String getAntipsychoticMedication() {
	return antipsychoticMedication;
    }

    public void setAntipsychoticMedication(String antipsychoticMedication) {
	this.antipsychoticMedication = antipsychoticMedication;
    }
    

        public String getAntianxietyMedication() {
	return antianxietyMedication;
    }

    public void setAntianxietyMedication(String antianxietyMedication) {
	this.antianxietyMedication = antianxietyMedication;
    }


        public String getAntidepressantMedication() {
	return antidepressantMedication;
    }

    public void setAntidepressantMedication(String antidepressantMedication) {
	this.antidepressantMedication = antidepressantMedication;
    }

        public String getHypnoticMedication() {
	return hypnoticMedication;
    }

    public void setHypnoticMedication(String hypnoticMedication) {
	this.hypnoticMedication = hypnoticMedication;
    }

        public String getAnticoagulantMedication() {
	return anticoagulantMedication;
    }

    public void setAnticoagulantMedication(String anticoagulantMedication) {
	this.anticoagulantMedication = anticoagulantMedication;
    }

        public String getAntibioticMedication() {
	return antibioticMedication;
    }

    public void setAntibioticMedication(String antibioticMedication) {
	this.antibioticMedication = antibioticMedication;
    }

        public String getDiureticMedication() {
	return diureticMedication;
    }

    public void setDiureticMedication(String diureticMedication) {
	this.diureticMedication = diureticMedication;
    }

        public String getBehavioralSymptomsToOthers() {
	return behavioralSymptomsToOthers;
    }

    public void setBehavioralSymptomsToOthers(String behavioralSymptomsToOthers) {
	this.behavioralSymptomsToOthers = behavioralSymptomsToOthers;
    }

        public String getDepressionNHCompare() {
	return depressionNHCompare;
    }

    public void setDepressionNHCompare(String depressionNHCompare) {
	this.depressionNHCompare = depressionNHCompare;
    }

        public String getExcessiveWeightLoss() {
	return excessiveWeightLoss;
    }

    public void setExcessiveWeightLoss(String excessiveWeightLoss) {
	this.excessiveWeightLoss = excessiveWeightLoss;
    }

        public String getFever() {
	return fever;
    }

    public void setFever(String fever) {
	this.fever = fever;
    }

        public String getVomiting() {
	return vomiting;
    }

    public void setVomiting(String vomiting) {
	this.vomiting = vomiting;
    }

        public String getDehydrated() {
	return dehydrated;
    }

    public void setDehydrated(String dehydrated) {
	this.dehydrated = dehydrated;
    }

        public String getInternalBleeding() {
	return internalBleeding;
    }

    public void setInternalBleeding(String internalBleeding) {
	this.internalBleeding = internalBleeding;
    }

        public String getNoProblemConditions() {
	return noProblemConditions;
    }

    public void setNoProblemConditions(String noProblemConditions) {
	this.noProblemConditions = noProblemConditions;
    }

        public String getRiskOfPressureUlcers() {
	return riskOfPressureUlcers;
    }

    public void setRiskOfPressureUlcers(String riskOfPressureUlcers) {
	this.riskOfPressureUlcers = riskOfPressureUlcers;
    }

        public String getBowelIncontinence() {
	return bowelIncontinence;
    }

    public void setBowelIncontinence(String bowelIncontinence) {
	this.bowelIncontinence = bowelIncontinence;
    }

        public String getUrinaryIncontinence() {
	return urinaryIncontinence;
    }

    public void setUrinaryIncontinence(String urinaryIncontinence) {
	this.urinaryIncontinence = urinaryIncontinence;
    }

    //following below are in the mds interface and mds2 but not in mds3
    
    //public String getWoundinfectionsnoneofabove() {
		//return null;
	//}
	
	//public void setWoundInfectionsnoneofabove(String woundinfections) {}
	
	//public String getHyperthyroidism() {
		//return null;
	//}
	
	//public void setHyperthyroidism(String hyperthyroidism) {}
	
	//public String getHypothyroidism() {
		//return null;
	//}
	
	//public void setHypothyroidism(String hypothyroidism) {}
}
