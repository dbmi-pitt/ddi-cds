library "Warfarin-NSAIDs" version '1'
// ./cql-to-elm/build/install/cql-to-elm/bin/cql-to-elm  --format=JSON --input /Users/erc53/Github/CQL-Testing-Framework/pddi-rule-tests/warfarin-nsaids/cql/Warfarin-NSAIDs.cql   --output /Users/erc53/Github/CQL-Testing-Framework/pddi-rule-tests/warfarin-nsaids/cql/

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0' called FHIRHelpers 

include CDS_Connect_Commons_for_FHIRv300 version '1.1.0' called C3F 

include ReusableFunctions_FHIRv300 version '1.0.0' called RF

// include CDS_Connect_Conversions version '1' called Convert 

codesystem "RXNORM": 'http://www.nlm.nih.gov/research/umls/rxnorm'

valueset "Warfarins VS": '2.16.840.1.113762.1.4.1213.8'
valueset "NSAIDs VS": '2.16.840.1.113762.1.4.1213.9'
valueset "PPIs and Misoprostols VS": '2.16.840.1.113762.1.4.1213.5'

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

define "PPIs and Misoprostols":
  C3F.MedicationStatementLookBack([MedicationStatement: "PPIs and Misoprostols VS"], 180 days)

define "Report PPIs and Misoprostols":
  "PPIs and Misoprostols" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Basic Concomitant Exposure":
  exists("Warfarins")
  and exists("NSAIDs")

define "MeetsInclusionCriteria":
  exists("Warfarins")
  and exists("NSAIDs")

define "Warfarin - NSAIDS with PPIs / Misoprostols":
  if "InPopulation" is not true then
    null
  else
    exists("PPIs and Misoprostols")

define "InPopulation":
  "MeetsInclusionCriteria" 

define "Recommendation":
  if "Warfarin - NSAIDS with PPIs / Misoprostols" then 'Assess risk and take action if necessary'
  else null

define "Rationale":
  if "Warfarin - NSAIDS with PPIs / Misoprostols" then 'Possible increased risk of bleeding'
  else null
define "Errors":
  null

define Summary: {
  Patient: {
    Name: Combine(Patient.name.given G return G.value, ' ') + ' ' + Combine(Patient.name.family F return F.value, ' '),
    Gender: Patient.gender.value,
    Age: AgeInYears()
  },
  HistoricalTreatments: {
    Warfarins: "Report Warfarins",
    NSAIDs: "Report NSAIDs",
    BasicConcomitant: "Basic Concomitant Exposure",
    PPIsAndMisoprostols: "Report PPIs and Misoprostols"
  },
  Alert: {
    Recommendation: "Recommendation",
    Rationale: "Rationale"
  }
}
