package edu.pitt.dbmi.ohdsiv5.db;
// Generated Aug 25, 2010 12:48:09 PM by Hibernate Tools 3.1.0.beta4

import java.math.BigDecimal;


/**
 * Mds2 generated by hbm2java
 */
    public class Mds2 {


    // Fields    

     Long id;
     private String reportdate;
     private String mdsversion;
     private String examType;
     private String mdsdataversion;
     private BigDecimal attestationsequencenumber;
     private Boolean transcriptionerror;
     private Boolean ndataentryerror;
     private Boolean softwareproducterror;
     private Boolean itemcodingerror;
     private Boolean othererror;
     private String testrecord;
     private Boolean eventdidnotoccur;
     private Boolean inadvertentsubmission;
     private Boolean reasonforinactivation;
     private BigDecimal attestationdate;
     private Boolean attestationcorrection;
     private String patientId;
     private String gender;
     private String race;
     private String birthdate;
     private String location;
     private String dateofentry;
     private String marital_status;
     private String comatose;
     private String shorttermmemory;
     private String cognitiveskills;
     private String hearingability;
     private String hearingaid;
     private String fullbedrails;
     private String otherrails;
     private String trunkrestraint;
     private String limbrestraint;
     private String chairpreventsrising;
     private Byte hospitalstays;
     private Boolean hospicecare;
     private Byte ervisits;
     private String makingselfunderstood;
     private String negativestatements;
     private String persistentanger;
     private String expressionunrealisticfears;
     private String repetitivehealthcomplaints;
     private String repetitiveanxiouscomplaints;
     private String sadexpressions;
     private String cryingtearfulness;
     private String bedmobilityself;
     private String transferself;
     private String eatingself;
     private String personalhygieneself;
     private String ostomy;
     private String locomotionself;
     private String dressingself;
     private String blankself;
     private String toiletself;
     private String diabetes;
     private String hyperthyroidism;
     private String hypothyroidism;
     private String ashd;
     private String cardiacdysrhythmias;
     private String congestiveheartfailure;
     private String deepveinthrombosis;
     private String hypertension;
     private String hypotension;
     private String peripheralvasculardisease;
     private String othercardiovasculardisease;
     private String arthritis;
     private String missinglimb;
     private String hipfracture;
     private String osteoporosis;
     private String pathologicalbonefracture;
     private String alzheimers;
     private String aphasia;
     private String cerebralpalsy;
     private String cerebrovascularaccident;
     private String dementiaotherthanalzheimers;
     private String hemiplegiahemiparesis;
     private String multiplesclerosis;
     private String paraplegia;
     private String parkinsons;
     private String quadriplegia;
     private String seizuredisorder;
     private String transientischemicattack;
     private String traumaticbraininjury;
     private String anxietydisorder;
     private String depression;
     private String manicdepressive;
     private String schizophrenia;
     private String asthma;
     private String emphysemacopd;
     private String cataracts;
     private String diabeticretinopathy;
     private String glaucoma;
     private String allergies;
     private String maculardegeneration;
     private String anemia;
     private String cancer;
     private String renalfailure;
     private String diseasesnoneofabove;
     private String cdiff;
     private String antibioticresistantinfection;
     private String conjunctivitis;
     private String hiv;
     private String pneumonia;
     private String respiratoryinfection;
     private String septicemia;
     private String std;
     private String tuberculosis;
     private String urinarytractinfection30;
     private String viralhepatitis;
     private String infectionsnoneofabove;
     private String otherdiagnosisicd9a;
     private String otherdiagnosisicd9b;
     private String otherdiagnosisicd9c;
     private String otherdiagnosisicd9d;
     private String otherdiagnosisicd9e;
     
     private String understandothers;
	 private String woundinfectionsnoneofabove;

    // Constructors

    /** default constructor */
    public Mds2() {
    }

	/** minimal constructor */
    public Mds2(Long id) {
        this.id = id;
    }
    
    /** full constructor */
	public Mds2(Long id, String mdsversion, String examType, String mdsdataversion, BigDecimal attestationsequencenumber, Boolean transcriptionerror, Boolean ndataentryerror, Boolean softwareproducterror, Boolean itemcodingerror, Boolean othererror, String testrecord, Boolean eventdidnotoccur, Boolean inadvertentsubmission, Boolean reasonforinactivation, BigDecimal attestationdate, Boolean attestationcorrection, String patientId, String gender, String race, String birthdate, String location, String dateofentry, String marital_status, String comatose, String shorttermmemory, String cognitiveskills, String hearingability, String hearingaid, String fullbedrails, String otherrails, String trunkrestraint, String limbrestraint, String chairpreventsrising, Byte hospitalstays, Boolean hospicecare, Byte ervisits, String makingselfunderstood, String negativestatements, String persistentanger, String expressionunrealisticfears, String repetitivehealthcomplaints, String sadexpressions, String cryingtearfulness, String bedmobilityself, String transferself, String locomotionself, String dressingself, String blankself, String toiletself, String diabetes, String hyperthyroidism, String hypothyroidism, String ashd, String cardiacdysrhythmias, String congestiveheartfailure, String deepveinthrombosis, String hypertension, String hypotension, String peripheralvasculardisease, String othercardiovasculardisease, String arthritis, String missinglimb, String hipfracture, String osteoporosis, String pathologicalbonefracture, String alzheimers, String aphasia, String cerebralpalsy, String cerebrovascularaccident, String dementiaotherthanalzheimers, String hemiplegiahemiparesis, String multiplesclerosis, String paraplegia, String parkinsons, String quadriplegia, String seizuredisorder, String transientischemicattack, String traumaticbraininjury, String anxietydisorder, String depression, String manicdepressive, String schizophrenia, String asthma, String emphysemacopd, String cataracts, String diabeticretinopathy, String glaucoma, String allergies, String maculardegeneration, String anemia, String cancer, String renalfailure, String diseasesnoneofabove, String cdiff, String antibioticresistantinfection, String conjunctivitis, String hiv, String pneumonia, String respiratoryinfection, String septicemia, String std, String tuberculosis, String urinarytractinfection30, String viralhepatitis, String infectionsnoneofabove, String otherdiagnosisicd9a, String otherdiagnosisicd9b, String otherdiagnosisicd9c, String otherdiagnosisicd9d, String otherdiagnosisicd9e, String ostomy, String personalhygieneself, String eatingself, String understandothers, String woundinfectionsnoneofabove, String reportdate) {
        super();
        this.id = id;
        this.reportdate = reportdate;
        this.mdsversion = mdsversion;
        this.examType = examType;
        this.mdsdataversion = mdsdataversion;
        this.attestationsequencenumber = attestationsequencenumber;
        this.transcriptionerror = transcriptionerror;
        this.ndataentryerror = ndataentryerror;
        this.softwareproducterror = softwareproducterror;
        this.itemcodingerror = itemcodingerror;
        this.othererror = othererror;
        this.testrecord = testrecord;
        this.eventdidnotoccur = eventdidnotoccur;
        this.inadvertentsubmission = inadvertentsubmission;
        this.reasonforinactivation = reasonforinactivation;
        this.attestationdate = attestationdate;
        this.attestationcorrection = attestationcorrection;
        this.patientId = patientId;
        this.gender = gender;
        this.race = race;
        this.birthdate = birthdate;
        this.location = location;
        this.dateofentry = dateofentry;
        this.marital_status = marital_status;
        this.comatose = comatose;
        this.shorttermmemory = shorttermmemory;
        this.cognitiveskills = cognitiveskills;
        this.hearingability = hearingability;
        this.hearingaid = hearingaid;
        this.fullbedrails = fullbedrails;
        this.otherrails = otherrails;
        this.trunkrestraint = trunkrestraint;
        this.limbrestraint = limbrestraint;
        this.chairpreventsrising = chairpreventsrising;
        this.hospitalstays = hospitalstays;
        this.hospicecare = hospicecare;
        this.ervisits = ervisits;
        this.makingselfunderstood = makingselfunderstood;
        this.negativestatements = negativestatements;
        this.persistentanger = persistentanger;
        this.expressionunrealisticfears = expressionunrealisticfears;
        this.repetitivehealthcomplaints = repetitivehealthcomplaints;
        this.sadexpressions = sadexpressions;
        this.cryingtearfulness = cryingtearfulness;
        this.bedmobilityself = bedmobilityself;
        this.transferself = transferself;
        this.locomotionself = locomotionself;
        this.dressingself = dressingself;
        this.blankself = blankself;
        this.toiletself = toiletself;
        this.diabetes = diabetes;
        this.hyperthyroidism = hyperthyroidism;
        this.hypothyroidism = hypothyroidism;
        this.ashd = ashd;
        this.cardiacdysrhythmias = cardiacdysrhythmias;
        this.congestiveheartfailure = congestiveheartfailure;
        this.deepveinthrombosis = deepveinthrombosis;
        this.hypertension = hypertension;
        this.hypotension = hypotension;
        this.peripheralvasculardisease = peripheralvasculardisease;
        this.othercardiovasculardisease = othercardiovasculardisease;
        this.arthritis = arthritis;
        this.missinglimb = missinglimb;
        this.hipfracture = hipfracture;
        this.osteoporosis = osteoporosis;
        this.pathologicalbonefracture = pathologicalbonefracture;
        this.alzheimers = alzheimers;
        this.aphasia = aphasia;
        this.cerebralpalsy = cerebralpalsy;
        this.cerebrovascularaccident = cerebrovascularaccident;
        this.dementiaotherthanalzheimers = dementiaotherthanalzheimers;
        this.hemiplegiahemiparesis = hemiplegiahemiparesis;
        this.multiplesclerosis = multiplesclerosis;
        this.paraplegia = paraplegia;
        this.parkinsons = parkinsons;
        this.quadriplegia = quadriplegia;
        this.seizuredisorder = seizuredisorder;
        this.transientischemicattack = transientischemicattack;
        this.traumaticbraininjury = traumaticbraininjury;
        this.anxietydisorder = anxietydisorder;
        this.depression = depression;
        this.manicdepressive = manicdepressive;
        this.schizophrenia = schizophrenia;
        this.asthma = asthma;
        this.emphysemacopd = emphysemacopd;
        this.cataracts = cataracts;
        this.diabeticretinopathy = diabeticretinopathy;
        this.glaucoma = glaucoma;
        this.allergies = allergies;
        this.maculardegeneration = maculardegeneration;
        this.anemia = anemia;
        this.cancer = cancer;
        this.renalfailure = renalfailure;
        this.diseasesnoneofabove = diseasesnoneofabove;
        this.cdiff = cdiff;
        this.antibioticresistantinfection = antibioticresistantinfection;
        this.conjunctivitis = conjunctivitis;
        this.hiv = hiv;
        this.pneumonia = pneumonia;
        this.respiratoryinfection = respiratoryinfection;
        this.septicemia = septicemia;
        this.std = std;
        this.tuberculosis = tuberculosis;
        this.urinarytractinfection30 = urinarytractinfection30;
        this.viralhepatitis = viralhepatitis;
        this.infectionsnoneofabove = infectionsnoneofabove;
        this.otherdiagnosisicd9a = otherdiagnosisicd9a;
        this.otherdiagnosisicd9b = otherdiagnosisicd9b;
        this.otherdiagnosisicd9c = otherdiagnosisicd9c;
        this.otherdiagnosisicd9d = otherdiagnosisicd9d;
        this.otherdiagnosisicd9e = otherdiagnosisicd9e;
        this.ostomy = ostomy;
        this.personalhygieneself = personalhygieneself;
        this.eatingself = eatingself;
        this.understandothers = understandothers;
        this.woundinfectionsnoneofabove = woundinfectionsnoneofabove;
        this.reportdate = reportdate;
    }
    

   
    // Property accessors
    //    //        //        //	
	//TODO: MDS3 ID is a long...
	//TODO: there is no patient ID here...
    //public Mds2Id getId() {
        //return this.id;
    //}
    
    //public void setId(Mds2Id id) {
        //this.id = id;
    //}
    
                    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
        public String getDateOfEvent() {
		return reportdate;
	}
	
	public void setDateOfEvent(String dateOfEvent) {
		this.reportdate = dateOfEvent;
	}
    
        public String getMdsversion() {
        return this.mdsversion;
    }
    
    public void setMdsversion(String mdsversion) {
        this.mdsversion = mdsversion;
    }
    
        public String getExamType() {
        return this.examType;
    }
    
    public void setExamType(String examType) {
        this.examType = examType;
    }
    
    
    public String getMdsdataversion() {
        return this.mdsdataversion;
    }
    
    public void setMdsdataversion(String mdsdataversion) {
        this.mdsdataversion = mdsdataversion;
    }
    
    public BigDecimal getAttestationsequencenumber() {
        return this.attestationsequencenumber;
    }
    
    public void setAttestationsequencenumber(BigDecimal attestationsequencenumber) {
        this.attestationsequencenumber = attestationsequencenumber;
    }
    
    public Boolean getTranscriptionerror() {
        return this.transcriptionerror;
    }
    
    public void setTranscriptionerror(Boolean transcriptionerror) {
        this.transcriptionerror = transcriptionerror;
    }
    
    public Boolean getNdataentryerror() {
        return this.ndataentryerror;
    }
    
    public void setNdataentryerror(Boolean ndataentryerror) {
        this.ndataentryerror = ndataentryerror;
    }
    
    public Boolean getSoftwareproducterror() {
        return this.softwareproducterror;
    }
    
    public void setSoftwareproducterror(Boolean softwareproducterror) {
        this.softwareproducterror = softwareproducterror;
    }
    
    public Boolean getItemcodingerror() {
        return this.itemcodingerror;
    }
    
    public void setItemcodingerror(Boolean itemcodingerror) {
        this.itemcodingerror = itemcodingerror;
    }
    
    public Boolean getOthererror() {
        return this.othererror;
    }
    
    public void setOthererror(Boolean othererror) {
        this.othererror = othererror;
    }
    
    public String getTestrecord() {
        return this.testrecord;
    }
    
    public void setTestrecord(String testrecord) {
        this.testrecord = testrecord;
    }
    
    public Boolean getEventdidnotoccur() {
        return this.eventdidnotoccur;
    }
    
    public void setEventdidnotoccur(Boolean eventdidnotoccur) {
        this.eventdidnotoccur = eventdidnotoccur;
    }
    
    public Boolean getInadvertentsubmission() {
        return this.inadvertentsubmission;
    }
    
    public void setInadvertentsubmission(Boolean inadvertentsubmission) {
        this.inadvertentsubmission = inadvertentsubmission;
    }
    
    public Boolean getReasonforinactivation() {
        return this.reasonforinactivation;
    }
    
    public void setReasonforinactivation(Boolean reasonforinactivation) {
        this.reasonforinactivation = reasonforinactivation;
    }
    
    public BigDecimal getAttestationdate() {
        return this.attestationdate;
    }
    
    public void setAttestationdate(BigDecimal attestationdate) {
        this.attestationdate = attestationdate;
    }
    
    public Boolean getAttestationcorrection() {
        return this.attestationcorrection;
    }
    
    public void setAttestationcorrection(Boolean attestationcorrection) {
        this.attestationcorrection = attestationcorrection;
    }
    
        public String getPatientId() {
        return this.patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
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
    
        public String getRace() {
	return this.race;
    }
    
    public void setRace(String race) {                
	this.race = race;
    }
    
    //TODO Testing how to make location variable from hibernate
		public String getLocation() {
	    return location;
	}
    public void setLocation(String location) {
	this.location = location;
    }
	
    //TODO: dateofentry has changed completely from the mds2 schema
    
    public String getEntrydate() {
        return this.dateofentry;
    }
    
    public void setEntrydate(String dateofentry) {
        this.dateofentry = dateofentry;
    }
    
        public String getMaritalstatus() {
        return marital_status;
    }
    
    public void setMaritalstatus(String marital_status) {
        this.marital_status = marital_status;
    }
    
    	public String getComatose() {
	return comatose;
    }
    
    public void setComatose(String comatose) {
	this.comatose = comatose;
    }
    
    
    public String getShorttermmemory() {
        return this.shorttermmemory;
    }
    
    public void setShorttermmemory(String shorttermmemory) {
        this.shorttermmemory = shorttermmemory;
    }
    
    public String getCognitiveskills() {
        return this.cognitiveskills;
    }
    
    	public String getHearingability() {
	return hearingability;
    }
    
    	public String getHearingaid() {
	return hearingaid;
    }
    
    public void setHearingaid(String hearingaid) {
	this.hearingaid = hearingaid;
    }
    
    public void setHearingability(String hearingability) {
	this.hearingability = hearingability;
    }
    
    public void setCognitiveskills(String cognitiveskills) {
        this.cognitiveskills = cognitiveskills;
    }
    
    public String getFullbedrails() {
        return this.fullbedrails;
    }
    
    public void setFullbedrails(String fullbedrails) {
        this.fullbedrails = fullbedrails;
    }
    
    public String getOtherrails() {
        return this.otherrails;
    }
    
    public void setOtherrails(String otherrails) {
        this.otherrails = otherrails;
    }
    
    public String getTrunkrestraint() {
        return this.trunkrestraint;
    }
    
    public void setTrunkrestraint(String trunkrestraint) {
        this.trunkrestraint = trunkrestraint;
    }
    
    public String getLimbrestraint() {
        return this.limbrestraint;
    }
    
    public void setLimbrestraint(String limbrestraint) {
        this.limbrestraint = limbrestraint;
    }
    
    public String getChairpreventsrising() {
        return this.chairpreventsrising;
    }
    
    public void setChairpreventsrising(String chairpreventsrising) {
        this.chairpreventsrising = chairpreventsrising;
    }
    
    public Byte getHospitalstays() {
        return this.hospitalstays;
    }
    
    public void setHospitalstays(Byte hospitalstays) {
        this.hospitalstays = hospitalstays;
    }
    
    public Boolean getHospicecare() {
        return this.hospicecare;
    }
    
    public void setHospicecare(Boolean hospicecare) {
        this.hospicecare = hospicecare;
    }
    
    public Byte getErvisits() {
        return this.ervisits;
    }
    
    public void setErvisits(Byte ervisits) {
        this.ervisits = ervisits;
    }
    
    public String getUnderstandable() {
        return this.makingselfunderstood;
    }
    
    public void setUnderstandable(String makingselfunderstood) {
        this.makingselfunderstood = makingselfunderstood;
    }
    
    public String getNegativestatements() {
        return this.negativestatements;
    }
    
    public void setNegativestatements(String negativestatements) {
        this.negativestatements = negativestatements;
    }
    
    public String getPersistentanger() {
        return this.persistentanger;
    }
    
    public void setPersistentanger(String persistentanger) {
        this.persistentanger = persistentanger;
    }
    
    public String getExpressionunrealisticfears() {
        return this.expressionunrealisticfears;
    }
    
    public void setExpressionunrealisticfears(String expressionunrealisticfears) {
        this.expressionunrealisticfears = expressionunrealisticfears;
    }
    
    public String getRepetitivehealthcomplaints() {
        return this.repetitivehealthcomplaints;
    }
    
    public void setRepetitivehealthcomplaints(String repetitivehealthcomplaints) {
        this.repetitivehealthcomplaints = repetitivehealthcomplaints;
    }
    
    public String getSadexpressions() {
        return this.sadexpressions;
    }
    
    public void setSadexpressions(String sadexpressions) {
        this.sadexpressions = sadexpressions;
    }
    
    public String getCryingtearfulness() {
        return this.cryingtearfulness;
    }
    
    public void setCryingtearfulness(String cryingtearfulness) {
        this.cryingtearfulness = cryingtearfulness;
    }
    
    public String getBedmobilityself() {
        return this.bedmobilityself;
    }
    
    public void setBedmobilityself(String bedmobilityself) {
        this.bedmobilityself = bedmobilityself;
    }
    
    public String getTransferself() {
        return this.transferself;
    }
    
    public void setTransferself(String transferself) {
        this.transferself = transferself;
    }
    
    public String getLocomotionself() {
        return this.locomotionself;
    }
    
    public void setLocomotionself(String locomotionself) {
        this.locomotionself = locomotionself;
    }
    
    public String getDressingself() {
        return this.dressingself;
    }
    
    public void setDressingself(String dressingself) {
        this.dressingself = dressingself;
    }
    
    public String getBlankself() {
        return this.blankself;
    }
    
    public void setBlankself(String blankself) {
        this.blankself = blankself;
    }
    
    public String getToiletself() {
        return this.toiletself;
    }
    
    public void setToiletself(String toiletself) {
        this.toiletself = toiletself;
    }
    
    public String getDiabetes() {
        return this.diabetes;
    }
    
    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }
    
    public String getHyperthyroidism() {
        return this.hyperthyroidism;
    }
    
    public void setHyperthyroidism(String hyperthyroidism) {
        this.hyperthyroidism = hyperthyroidism;
    }
    
    public String getHypothyroidism() {
        return this.hypothyroidism;
    }
    
    public void setHypothyroidism(String hypothyroidism) {
        this.hypothyroidism = hypothyroidism;
    }
    
    public String getAshd() {
        return this.ashd;
    }
    
    public void setAshd(String ashd) {
        this.ashd = ashd;
    }
    
    public String getCardiacdysrhythmias() {
        return this.cardiacdysrhythmias;
    }
    
    public void setCardiacdysrhythmias(String cardiacdysrhythmias) {
        this.cardiacdysrhythmias = cardiacdysrhythmias;
    }
    
    public String getHeartfailure() {
        return this.congestiveheartfailure;
    }
    
    public void setHeartfailure(String congestiveheartfailure) {
        this.congestiveheartfailure = congestiveheartfailure;
    }
    
    public String getDvtpepte() {
        return this.deepveinthrombosis;
    }
    
    public void setDvtpepte(String deepveinthrombosis) {
        this.deepveinthrombosis = deepveinthrombosis;
    }
    
    public String getHypertension() {
        return this.hypertension;
    }
    
    public void setHypertension(String hypertension) {
        this.hypertension = hypertension;
    }
    
    public String getHypotension() {
        return this.hypotension;
    }
    
    public void setHypotension(String hypotension) {
        this.hypotension = hypotension;
    }
    
    public String getPeripheralvasculardisease() {
        return this.peripheralvasculardisease;
    }
    
    public void setPeripheralvasculardisease(String peripheralvasculardisease) {
        this.peripheralvasculardisease = peripheralvasculardisease;
    }
    
    public String getOthercardiovasculardisease() {
        return this.othercardiovasculardisease;
    }
    
    public void setOthercardiovasculardisease(String othercardiovasculardisease) {
        this.othercardiovasculardisease = othercardiovasculardisease;
    }
    
    public String getArthritis() {
        return this.arthritis;
    }
    
    public void setArthritis(String arthritis) {
        this.arthritis = arthritis;
    }
    
    public String getMissinglimb() {
        return this.missinglimb;
    }
    
    public void setMissinglimb(String missinglimb) {
        this.missinglimb = missinglimb;
    }
    
    public String getHipfracture() {
        return this.hipfracture;
    }
    
    public void setHipfracture(String hipfracture) {
        this.hipfracture = hipfracture;
    }
    
    public String getOsteoporosis() {
        return this.osteoporosis;
    }
    
    public void setOsteoporosis(String osteoporosis) {
        this.osteoporosis = osteoporosis;
    }
    
    public String getOtherfracture() {
        return this.pathologicalbonefracture;
    }
    
    public void setOtherfracture(String pathologicalbonefracture) {
        this.pathologicalbonefracture = pathologicalbonefracture;
    }
    
    public String getAlzheimers() {
        return this.alzheimers;
    }
    
    public void setAlzheimers(String alzheimers) {
        this.alzheimers = alzheimers;
    }
    
    public String getAphasia() {
        return this.aphasia;
    }
    
    public void setAphasia(String aphasia) {
        this.aphasia = aphasia;
    }
    
    public String getCerebralpalsy() {
        return this.cerebralpalsy;
    }
    
    public void setCerebralpalsy(String cerebralpalsy) {
        this.cerebralpalsy = cerebralpalsy;
    }
    
    public String getStroke() {
        return this.cerebrovascularaccident;
    }
    
    public void setStroke(String cerebrovascularaccident) {
        this.cerebrovascularaccident = cerebrovascularaccident;
    }
    
    
    public String getDementiaotherthanalzheimers() {
        return this.dementiaotherthanalzheimers;
    }
    
    public void setDementiaotherthanalzheimers(String dementiaotherthanalzheimers) {
        this.dementiaotherthanalzheimers = dementiaotherthanalzheimers;
    }
    
    public String getHemiplegiahemiparesis() {
        return this.hemiplegiahemiparesis;
    }
    
    public void setHemiplegiahemiparesis(String hemiplegiahemiparesis) {
        this.hemiplegiahemiparesis = hemiplegiahemiparesis;
    }
    
    public String getMultiplesclerosis() {
        return this.multiplesclerosis;
    }
    
    public void setMultiplesclerosis(String multiplesclerosis) {
        this.multiplesclerosis = multiplesclerosis;
    }
    
    public String getParaplegia() {
        return this.paraplegia;
    }
    
    public void setParaplegia(String paraplegia) {
        this.paraplegia = paraplegia;
    }
    
    public String getParkinsons() {
        return this.parkinsons;
    }
    
    public void setParkinsons(String parkinsons) {
        this.parkinsons = parkinsons;
    }
    
    public String getQuadriplegia() {
        return this.quadriplegia;
    }
    
    public void setQuadriplegia(String quadriplegia) {
        this.quadriplegia = quadriplegia;
    }
    
    public String getSeizuredisorderorepilepsy() {
        return this.seizuredisorder;
    }
    
    public void setSeizuredisorderorepilepsy(String seizuredisorder) {
        this.seizuredisorder = seizuredisorder;
    }
    
    public String getTransientischemicattack() {
        return this.transientischemicattack;
    }
    
    public void setTransientischemicattack(String transientischemicattack) {
        this.transientischemicattack = transientischemicattack;
    }
    
    public String getTraumaticbraininjury() {
        return this.traumaticbraininjury;
    }
    
    public void setTraumaticbraininjury(String traumaticbraininjury) {
        this.traumaticbraininjury = traumaticbraininjury;
    }
    
    public String getAnxiety() {
        return this.anxietydisorder;
    }
    
    public void setAnxiety(String anxietydisorder) {
        this.anxietydisorder = anxietydisorder;
    }
    
    public String getDepression() {
        return this.depression;
    }
    
    public void setDepression(String depression) {
        this.depression = depression;
    }
    
    public String getManicdepression() {
        return this.manicdepressive;
    }
    
    public void setManicdepression(String manicdepressive) {
        this.manicdepressive = manicdepressive;
    }
    
    public String getSchizophrenia() {
        return this.schizophrenia;
    }
    
    public void setSchizophrenia(String schizophrenia) {
        this.schizophrenia = schizophrenia;
    }
    
    public String getAsthma() {
        return this.asthma;
    }
    
    public void setAsthma(String asthma) {
        this.asthma = asthma;
    }
    
    public String getEmphysemacopd() {
        return this.emphysemacopd;
    }
    
    public void setEmphysemacopd(String emphysemacopd) {
        this.emphysemacopd = emphysemacopd;
    }
    
    public String getCataracts() {
        return this.cataracts;
    }
    
    public void setCataracts(String cataracts) {
        this.cataracts = cataracts;
    }
    
    public String getDiabeticretinopathy() {
        return this.diabeticretinopathy;
    }
    
    public void setDiabeticretinopathy(String diabeticretinopathy) {
        this.diabeticretinopathy = diabeticretinopathy;
    }
    
    public String getGlaucoma() {
        return this.glaucoma;
    }
    
    public void setGlaucoma(String glaucoma) {
        this.glaucoma = glaucoma;
    }
    
    public String getAllergies() {
        return this.allergies;
    }
    
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }
    
    public String getMaculardegeneration() {
        return this.maculardegeneration;
    }
    
    public void setMaculardegeneration(String maculardegeneration) {
        this.maculardegeneration = maculardegeneration;
    }
    
    public String getAnemia() {
        return this.anemia;
    }
    
    public void setAnemia(String anemia) {
        this.anemia = anemia;
    }
    
    public String getCancer() {
        return this.cancer;
    }
    
    public void setCancer(String cancer) {
        this.cancer = cancer;
    }
    
    public String getRenalfailure() {
        return this.renalfailure;
    }
    
    public void setRenalfailure(String renalfailure) {
        this.renalfailure = renalfailure;
    }
    
    public String getDiseasesnoneofabove() {
        return this.diseasesnoneofabove;
    }
    
    public void setDiseasesnoneofabove(String diseasesnoneofabove) {
        this.diseasesnoneofabove = diseasesnoneofabove;
    }
    
    public String getCdiff() {
        return this.cdiff;
    }
    
    public void setCdiff(String cdiff) {
        this.cdiff = cdiff;
    }
    
    public String getMultidrugresistantorganism() {
        return this.antibioticresistantinfection;
    }
    
    public void setMultidrugresistantorganism(String antibioticresistantinfection) {
        this.antibioticresistantinfection = antibioticresistantinfection;
    }
    
    public String getConjunctivitis() {
        return this.conjunctivitis;
    }
    
    public void setConjunctivitis(String conjunctivitis) {
        this.conjunctivitis = conjunctivitis;
    }
    
    public String getHiv() {
        return this.hiv;
    }
    
    public void setHiv(String hiv) {
        this.hiv = hiv;
    }
    
    public String getPneumonia() {
        return this.pneumonia;
    }
    
    public void setPneumonia(String pneumonia) {
        this.pneumonia = pneumonia;
    }
    
    public String getRespiratoryinfection() {
        return this.respiratoryinfection;
    }
    
    public void setRespiratoryinfection(String respiratoryinfection) {
        this.respiratoryinfection = respiratoryinfection;
    }
    
    public String getSepticemia() {
        return this.septicemia;
    }
    
    public void setSepticemia(String septicemia) {
        this.septicemia = septicemia;
    }
    
    public String getStd() {
        return this.std;
    }
    
    public void setStd(String std) {
        this.std = std;
    }
    
    public String getTuberculosis() {
        return this.tuberculosis;
    }
    
    public void setTuberculosis(String tuberculosis) {
        this.tuberculosis = tuberculosis;
    }
    
    public String getUrinarytractinfection() {
        return this.urinarytractinfection30;
    }
    
    public void setUrinarytractinfection(String urinarytractinfection30) {
        this.urinarytractinfection30 = urinarytractinfection30;
    }
    
    public String getViralhepatitis() {
        return this.viralhepatitis;
    }
    
    public void setViralhepatitis(String viralhepatitis) {
        this.viralhepatitis = viralhepatitis;
    }
    
    public String getInfectionsnoneofabove() {
        return this.infectionsnoneofabove;
    }
    
    public void setInfectionsnoneofabove(String infectionsnoneofabove) {
        this.infectionsnoneofabove = infectionsnoneofabove;
    }
    
    public String getOtherdiagnosisicd9a() {
        return this.otherdiagnosisicd9a;
    }
    
    public void setOtherdiagnosisicd9a(String otherdiagnosisicd9a) {
        this.otherdiagnosisicd9a = otherdiagnosisicd9a;
    }
    
    public String getOtherdiagnosisicd9b() {
        return this.otherdiagnosisicd9b;
    }
    
    public void setOtherdiagnosisicd9b(String otherdiagnosisicd9b) {
        this.otherdiagnosisicd9b = otherdiagnosisicd9b;
    }
    
    public String getOtherdiagnosisicd9c() {
        return this.otherdiagnosisicd9c;
    }
    
    public void setOtherdiagnosisicd9c(String otherdiagnosisicd9c) {
        this.otherdiagnosisicd9c = otherdiagnosisicd9c;
    }
    
    public String getOtherdiagnosisicd9d() {
        return this.otherdiagnosisicd9d;
    }
    
    public void setOtherdiagnosisicd9d(String otherdiagnosisicd9d) {
        this.otherdiagnosisicd9d = otherdiagnosisicd9d;
    }
    
    public String getOtherdiagnosisicd9e() {
        return this.otherdiagnosisicd9e;
    }
    
    public void setOtherdiagnosisicd9e(String otherdiagnosisicd9e) {
        this.otherdiagnosisicd9e = otherdiagnosisicd9e;
    }

	/**
	 * 	 */
	public void setRepetitiveanxiouscomplaints(
			String repetitiveanxiouscomplaints) {
		this.repetitiveanxiouscomplaints = repetitiveanxiouscomplaints;
	}

	/**
	 * 	 */
	public String getRepetitiveanxiouscomplaints() {
		return repetitiveanxiouscomplaints;
	}


    	public String getPersonalhygieneself() {
		return personalhygieneself;
	}
    

	public void setPersonalhygieneself(String personalhygieneself) {
		this.personalhygieneself = personalhygieneself;
	}
    
    
        public String getOstomy() {
        return this.ostomy;
    }
    
    public void setOstomy(String ostomy) {
        this.ostomy = ostomy;
    }



	/**
	 * 	 */
    	public String getEatingself() {
		return eatingself;
	}
   
	/**
	 * 	 */
	public void setEatingself(String eatingself) {
		this.eatingself = eatingself;
	}
	
		public String getUnderstandothers() {
		return null;
	}
	
	public void setUnderstandothers(String understandable) {
		this.makingselfunderstood = understandable;
	}
	
	//TODO: not in MDS3 but in MDS2 but not mapped
	//	//public String getWoundInfection() {
		//return woundinfectionsnoneofabove;
	//}
	
	//public void setWoundInfection(String woundinfection) {
		//this.woundinfectionsnoneofabove = woundinfection;
	//}
	
	//Following below are in the MDS interface but not in Mds2
	
	//public String getPyschosis() {
		//return null;
	//}
	
	//public void setPsychosis(String psychosis) {}
	
	//public String getPtsd() {
		//return null;
	//}

	//public void setPtsd(String ptsd) {}
	
	//public String getCirrhosis() {
		//return null;
	//}
	
	//public void setCirrhosis(String cirrhosis) {}
	
	//public String getHyponatremia() {
		//return null;
	//}
	
	//public void setHyponatremia(String hyponatremia) {}
	
	//public String getHyperlipidemia() {
		//return null;
	//}
	
	//public void setHyperlipidemia(String h) {}
	
	//public String getHuntingtonsDisease() {
		//return null;
	//}
	
	//public void setHuntingtonsDisease(String h) {}
	
	//public String getTourettessyndrome() {
		//return null;
	//}
	
	//public void setTourettessyndrome(String tourettessyndrome) {}
	
	//public String getThyroiddisorder() {
		//return null;
	//}
	
	//public void setThyroiddisorder(String t) {}
	
	//public String getFallssinceadmission() {
		//return null;
	//}
	
	//public void setFallssinceadmission(String fallssincelastadmission) {}



}
