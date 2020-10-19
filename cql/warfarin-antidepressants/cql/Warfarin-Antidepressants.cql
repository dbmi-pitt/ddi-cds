library "Warfarin-Antidepressants" version '1.0'
// ./cql-to-elm/build/install/cql-to-elm/bin/cql-to-elm  --format=JSON --input /Users/erc53/Github/CQL-Testing-Framework/pddi-rule-tests/warfarin-antidepressants/cql/Warfarin-Antidepressants.cql   --output /Users/erc53/Github/CQL-Testing-Framework/pddi-rule-tests/warfarin-antidepressants/cql/

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0' called FHIRHelpers 

include CDS_Connect_Commons_for_FHIRv300 version '1.1.0' called C3F 

include ReusableFunctions_FHIRv300 version '1.0.0' called RF

codesystem "RXNORM": 'http://www.nlm.nih.gov/research/umls/rxnorm'
codesystem "ICD9D": 'http://hl7.org/fhir/sid/icd-9-cm/diagnosis'

valueset "Warfarins VS": '2.16.840.1.113762.1.4.1213.8'
valueset "Mirtazapines VS": '2.16.840.1.113762.1.4.1213.32'
valueset "Tricyclics VS": '2.16.840.1.113762.1.4.1213.31'
valueset "Bupropions VS": '2.16.840.1.113762.1.4.1213.30'
valueset "SSRIs and SNRIs VS": '2.16.840.1.113762.1.4.1213.33'

valueset "Systemic Corticosteroids VS": '2.16.840.1.113762.1.4.1213.46'
valueset "Aldosterone Antagonists VS": '2.16.840.1.113762.1.4.1213.52'
valueset "Antiplatelet Medications VS": '2.16.840.1.113762.1.4.1213.50'
valueset "NSAIDs VS": '2.16.840.1.113762.1.4.1213.9'
valueset "Aspirins VS": '2.16.840.1.113762.1.4.1213.29'

valueset "History of Gastrointestinal Bleeds VS": '2.16.840.1.113762.1.4.1213.53'

context Patient

// RISK CONSIDERATIONS

define "Warfarins":
  C3F.MedicationStatementLookBack([MedicationStatement: "Warfarins VS"], 180 days)

define "Report Warfarins":
  "Warfarins" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Mirtazapines":
  C3F.MedicationStatementLookBack([MedicationStatement: "Mirtazapines VS"], 180 days)

define "Report Mirtazapines":
  "Mirtazapines" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Tricyclics":
  C3F.MedicationStatementLookBack([MedicationStatement: "Tricyclics VS"], 180 days)

define "Report Tricyclics":
  "Tricyclics" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Bupropions":
  C3F.MedicationStatementLookBack([MedicationStatement: "Bupropions VS"], 180 days)

define "Report Bupropions":
  "Bupropions" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "SSRIs and SNRIs":
  C3F.MedicationStatementLookBack([MedicationStatement: "SSRIs and SNRIs VS"], 180 days)

define "Report SSRIs and SNRIs":
  "SSRIs and SNRIs" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

// RISK FACTORS FOR BLEEDING

define Is65orOlder:
  AgeInYears() >= 65

define Report65orOlder:
  if Is65orOlder then
    {
      Name: '65 years or older',
      Status: 'active',
      Onset: null as String,
      DateRecorded: null as String,
      Abatement: null as String,
      Visit: null as Tuple{ Start String, End String }
    }
  else
    null

define "GI Bleeding":
  C3F.Confirmed(C3F.ActiveOrRecurring([Condition: "History of Gastrointestinal Bleeds VS"]))

define "Report GI Bleeding":
  "GI Bleeding" C
  return {
    Name:  RF.ConceptText(C.code),
    Status:    C.clinicalStatus.value,
    Onset: RF.Onset(C),
    DateRecorded: RF.DateTimeText(C.assertedDate)
  }
  sort by (Onset as String) desc

define "NSAIDs":
  C3F.MedicationStatementLookBack([MedicationStatement: "NSAIDs VS"], 180 days)

define "Report NSAIDs":
  "NSAIDs" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Aspirins":
  C3F.MedicationStatementLookBack([MedicationStatement: "Aspirins VS"], 180 days)

define "Report Aspirins":
  "Aspirins" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Systemic Corticosteroids":
  C3F.MedicationStatementLookBack([MedicationStatement: "Systemic Corticosteroids VS"], 180 days)

define "Report Systemic Corticosteroids":
  "Systemic Corticosteroids" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Aldosterone Antagonists":
  C3F.MedicationStatementLookBack([MedicationStatement: "Aldosterone Antagonists VS"], 180 days)

define "Report Aldosterone Antagonists":
  "Aldosterone Antagonists" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Antiplatelet Medications":
  C3F.MedicationStatementLookBack([MedicationStatement: "Antiplatelet Medications VS"], 180 days)

define "Report Antiplatelet Medications":
  "Antiplatelet Medications" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Report Medication Risk Factors for Bleeding":
  ("Report NSAIDs" union "Report Aspirins" union "Report Systemic Corticosteroids" union "Report Aldosterone Antagonists" union "Report Antiplatelet Medications") M

// - History of CNS hemorrhagic bleeding
// - History of UGIB or peptic ulcer 
// - Older than 64 years of age 
// - NSAIDs or aspirin
// - Systemic corticosteroids
// - Aldosterone antagonists
// - Anti-platelet medications
define "One or more risk factors for major bleeding":
  exists("Report GI Bleeding") or exists(Report65orOlder) or exists("Report Medication Risk Factors for Bleeding")

define "No risk factors for major bleeding":
  not("One or more risk factors for major bleeding")

define "MeetsInclusionCriteria":
  exists("Warfarins")

define "Warfarin - Mirtazapine":
  if "InPopulation" is not true then
    null
  else
  exists("Mirtazapines")

define "Warfarin - Tricyclics/Bupropion":
  if "InPopulation" is not true then
    null
  else
  exists("Tricyclics")
  or exists("Bupropions")

define "Warfarin - SSRIs and SNRIs":
  if "InPopulation" is not true then
    null
  else
  exists("SSRIs and SNRIs")

define "Patient has one or more risk factors for major bleeding":
  if "InPopulation" is not true then
    null
  else
  "One or more risk factors for major bleeding"

define "Patient has no risk factors for major bleeding":
  if "InPopulation" is not true then
    null
  else
  "No risk factors for major bleeding"

define "InPopulation":
   "MeetsInclusionCriteria" 

define "Recommendation":
  if "Warfarin - SSRIs and SNRIs" and "Patient has one or more risk factors for major bleeding" then 'Use only if benefit outweighs risk'
  else if "Warfarin - SSRIs and SNRIs" and "Patient has no risk factors for major bleeding" then 'Assess risk and take action if necessary'
  else if "Warfarin - Mirtazapine" then 'Assess risk and take action if necessary'
  else if "Warfarin - Tricyclics/Bupropion" then 'No special precautions'
  else null

define "Rationale":
  if "Warfarin - SSRIs and SNRIs" and "Patient has one or more risk factors for major bleeding" then 'Increased GI and non-GI bleeding risk is likely'
  else if "Warfarin - SSRIs and SNRIs" and "Patient has no risk factors for major bleeding" then 'Increased GI and non-GI bleeding risk is possible'
  else if "Warfarin - Mirtazapine" then 'Increased GI and non-GI bleeding risk is possible'
  else if "Warfarin - Tricyclics/Bupropion" then 'Increased bleeding risk unlikely'
  else null

define "Errors":
  if null then
    {''}
  else null

define Summary: {
  Patient: {
    Name: Combine(Patient.name.given G return G.value, ' ') + ' ' + Combine(Patient.name.family F return F.value, ' '),
    Gender: Patient.gender.value,
    Age: AgeInYears()
  },
  HistoricalTreatments: {
    Warfarins: "Report Warfarins",
    SSRIsSNRIs: "Report SSRIs and SNRIs",
    Mirtazapines: "Report Mirtazapines",
    Tricyclics: "Report Tricyclics",
    Bupropions: "Report Bupropions"
  },
  RiskFactorsForMajorBleeding: {
    GastrointestinalBleeding: "Report GI Bleeding",
    Is65orOlder: "Is65orOlder",
    MedicationBleedingRisks: "Report Medication Risk Factors for Bleeding"
  },
  Alert: {
    Recommendation: "Recommendation",
    Rationale: "Rationale"
  }
}
