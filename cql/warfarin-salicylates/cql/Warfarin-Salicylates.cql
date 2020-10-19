library "Warfarin-Salicylates" version '1.0'
// ./cql-to-elm/build/install/cql-to-elm/bin/cql-to-elm  --format=JSON --input /Users/erc53/Github/CQL-Testing-Framework/pddi-rule-tests/warfarin-salicylates/cql/Warfarin-Salicylates.cql   --output /Users/erc53/Github/CQL-Testing-Framework/pddi-rule-tests/warfarin-salicylates/cql/

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0' called FHIRHelpers 

include CDS_Connect_Commons_for_FHIRv300 version '1.1.0' called C3F 

include ReusableFunctions_FHIRv300 version '1.0.0' called RF

codesystem "RXNORM": 'http://www.nlm.nih.gov/research/umls/rxnorm'
codesystem "ICD9D": 'http://hl7.org/fhir/sid/icd-9-cm/diagnosis'

valueset "Warfarins VS": '2.16.840.1.113762.1.4.1213.8'
valueset "Topical Non-Acetylated Salicylates VS": '2.16.840.1.113762.1.4.1213.26'
valueset "Non-Acetylated Salicylates VS": '2.16.840.1.113762.1.4.1213.27'
valueset "Salicylates VS": '2.16.840.1.113762.1.4.1213.25'
valueset "Aspirins VS": '2.16.840.1.113762.1.4.1213.29'

valueset "History of Thromboembolic Events VS": '2.16.840.1.113762.1.4.1213.54'

context Patient

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

define "Topical Non-Acetylated Salicylates":
  C3F.MedicationStatementLookBack([MedicationStatement: "Topical Non-Acetylated Salicylates VS"], 180 days)

define "Report Topical Non-Acetylated Salicylates":
  "Topical Non-Acetylated Salicylates" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Non-Acetylated Salicylates":
  C3F.MedicationStatementLookBack([MedicationStatement: "Non-Acetylated Salicylates VS"], 180 days)

define "Report Non-Acetylated Salicylates":
  [Medication: "Non-Acetylated Salicylates VS"] M
  with "Non-Acetylated Salicylates" S
    such that M.code.coding.code.value = S.medication.coding.code.value
      return {
        Name: RF.ConceptText(M.code as FHIR.CodeableConcept),
        Start: RF.MedicationStatementDate("Non-Acetylated Salicylates"[0]), // TODO not sure how this will handle multiple 
        End: RF.DateTimeText(("Non-Acetylated Salicylates"[0].effective as FHIR.Period)."end"),
        Status: "Non-Acetylated Salicylates"[0].status.value,
        Ingredient: RF.ConceptText(M.ingredient[0].item as FHIR.CodeableConcept), // TODO deal with potentially more than one ingredient
        // Numerator: M.ingredient[0].amount.numerator.value.value,
        // Denominator: M.ingredient[0].amount.denominator.value.value,
        Amount: M.ingredient[0].amount.numerator.value.value / M.ingredient[0].amount.denominator.value.value,
        Dosage: "Non-Acetylated Salicylates".dosage[0].timing.repeat.frequency.value,
        DailyDosage: "Non-Acetylated Salicylates".dosage[0].timing.repeat.frequency.value * (M.ingredient[0].amount.numerator.value.value / M.ingredient[0].amount.denominator.value.value)
      }
      sort by Start desc, End desc

define "Aspirins":
  C3F.MedicationStatementLookBack([MedicationStatement: "Salicylates VS"], 180 days)

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

define "Salicylates":
  C3F.MedicationStatementLookBack([MedicationStatement: "Salicylates VS"], 180 days)

define "Report Salicylates":
  "Salicylates" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Thromboembolic Events":
  C3F.Confirmed(C3F.ActiveOrRecurring([Condition: "History of Thromboembolic Events VS"]))

define "Report Thromboembolic Events":
  "Thromboembolic Events" C
  return {
    Name:  RF.ConceptText(C.code),
    Status:    C.clinicalStatus.value,
    Onset: RF.Onset(C),
    DateRecorded: RF.DateTimeText(C.assertedDate)
  }
  sort by (Onset as String) desc

define "MeetsInclusionCriteria":
  exists("Warfarins")

define "Warfarin - Topical Salicylates":
  if "InPopulation" is not true then
    null
  else
  exists("Topical Non-Acetylated Salicylates")

define "Warfarin - Non-Acetylated Salicylates":
  if "InPopulation" is not true then
    null
  else
  exists("Non-Acetylated Salicylates")

define "Non-Acetylated Salicylate dose >= 3 grams/day":
  "Report Non-Acetylated Salicylates" S
  where S.DailyDosage >= 3

define "Non-Acetylated Salicylate dose < 3 grams/day":
  not exists("Non-Acetylated Salicylate dose >= 3 grams/day")

define "Warfarin - Aspirins":
  if "InPopulation" is not true then
    null
  else
  exists("Aspirins")

define "Warfarin - Aspirins Prevention of thromboembolic events":
  if "InPopulation" is not true then
    null
  else
  exists("Aspirins") and exists("Report Thromboembolic Events")

define "InPopulation":
   "MeetsInclusionCriteria" 

define "Recommendation":
  if "Warfarin - Topical Salicylates" then 'No special precautions'
  else if "Warfarin - Aspirins Prevention of thromboembolic events" then 'No special precautions'
  else if "Warfarin - Aspirins" and "Warfarin - Aspirins Prevention of thromboembolic events" is not true then 'Use only if benefit outweighs risk'
  else if "Warfarin - Non-Acetylated Salicylates" and exists("Non-Acetylated Salicylate dose >= 3 grams/day") then 'Assess risk and take action if necessary'
  else if "Warfarin - Non-Acetylated Salicylates" and "Non-Acetylated Salicylate dose < 3 grams/day" then 'No special precautions'
  else null

define "Rationale":
  if "Warfarin - Topical Salicylates" then 'Increased risk of bleeding unlikely'
  else if "Warfarin - Aspirins Prevention of thromboembolic events" then 'Increased anti-coagulation desired'
  else if "Warfarin - Aspirins" and "Warfarin - Aspirins Prevention of thromboembolic events" is not true then 'Increased risk of bleeding probably unintentional'
  else if "Warfarin - Non-Acetylated Salicylates" and exists("Non-Acetylated Salicylate dose >= 3 grams/day") then 'Increased risk of bleeding possible'
  else if "Warfarin - Non-Acetylated Salicylates" and "Non-Acetylated Salicylate dose < 3 grams/day" then 'Increased risk of bleeding unlikely'
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
    TopicalSalicylates: "Report Topical Non-Acetylated Salicylates",
    Aspirins: "Report Aspirins",
    NonAcetylatedSalicylates: "Report Non-Acetylated Salicylates",
    HighSalicylateDose: "Non-Acetylated Salicylate dose >= 3 grams/day"
  },
  HistoricalPatientEvents: {
    ThromboembolicEvents: "Report Thromboembolic Events"
  },
  Alert: {
    Recommendation: "Recommendation",
    Rationale: "Rationale"
  }
}
