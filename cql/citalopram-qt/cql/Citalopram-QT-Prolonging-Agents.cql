library "Citalopram-QT-Prolonging-Agents" version '1.0'
// ./cql-to-elm/build/install/cql-to-elm/bin/cql-to-elm  --format=JSON --input /Users/erc53/Github/CQL-Testing-Framework/pddi-rule-tests/citalopram-qt/cql/Citalopram-QT-Prolonging-Agents.cql   --output /Users/erc53/Github/CQL-Testing-Framework/pddi-rule-tests/citalopram-qt/cql/

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0' called FHIRHelpers 

include CDS_Connect_Commons_for_FHIRv300 version '1.1.0' called C3F 

include ReusableFunctions_FHIRv300 version '1.0.0' called RF

// include CDS_Connect_Conversions version '1' called Convert 

codesystem "RXNORM": 'http://www.nlm.nih.gov/research/umls/rxnorm'

valueset "Citaloprams VS": '2.16.840.1.113762.1.4.1213.3'
valueset "QT Prolonging Agents VS": '2.16.840.1.113762.1.4.1213.4'

// parameter "Dose of Citalopram" default 60.0 'mg/d'

context Patient

// RISK CONSIDERATIONS

define "Citaloprams":
  C3F.MedicationStatementLookBack([MedicationStatement: "Citaloprams VS"], 180 days)

define "Report Citaloprams":
  [Medication: "Citaloprams VS"] M
  with "Citaloprams" S
    such that M.code.coding.code.value = S.medication.coding.code.value
      return {
        Name: RF.ConceptText(M.code as FHIR.CodeableConcept),
        Start: RF.MedicationStatementDate("Citaloprams"[0]), // TODO not sure how this will handle multiple 
        End: RF.DateTimeText(("Citaloprams"[0].effective as FHIR.Period)."end"),
        Status: "Citaloprams"[0].status.value,
        Ingredient: RF.ConceptText(M.ingredient[0].item as FHIR.CodeableConcept), // TODO deal with potentially more than one ingredient
        // Numerator: M.ingredient[0].amount.numerator.value.value,
        // Denominator: M.ingredient[0].amount.denominator.value.value,
        Amount: M.ingredient[0].amount.numerator.value.value / M.ingredient[0].amount.denominator.value.value,
        Dosage: "Citaloprams".dosage[0].timing.repeat.frequency.value,
        DailyDosage: "Citaloprams".dosage[0].timing.repeat.frequency.value * (M.ingredient[0].amount.numerator.value.value / M.ingredient[0].amount.denominator.value.value)
      }
      sort by Start desc, End desc

define "QT Prolonging Agents":
  C3F.MedicationStatementLookBack([MedicationStatement: "QT Prolonging Agents VS"], 180 days)

define "Report QT Prolonging Agents":
  "QT Prolonging Agents" S
  return {
    Type:  'Statement',
    Name:  RF.ConceptText(S.medication as FHIR.CodeableConcept),
    Start: RF.MedicationStatementDate(S),
    End:   RF.DateTimeText((S.effective as FHIR.Period)."end"),
    Status: S.status.value
  }
  sort by Start desc, End desc

define "Dose of Citalopram >= 60 mg/day":
  "Report Citaloprams" C
  return
    C.DailyDosage >= 60

define "Dose of Citalopram < 60 mg/day":
  "Report Citaloprams" C
  return
    C.DailyDosage < 60

define "No QT Prolonging Agents":
  not(exists([MedicationStatement: "QT Prolonging Agents VS"]))

define "Citaloprams exists":
  exists("Citaloprams")

define "QT Prolonging Agents exists":
  exists("QT Prolonging Agents")

define "Basic Concomitant Exposure":
  "Citaloprams exists"
  and "QT Prolonging Agents exists"

define "MeetsInclusionCriteria":
  "Citaloprams exists"
  // and "QT Prolonging Agents exists"

define "Citalopram < 60 mg/day":
  if "InPopulation" is not true then
    null
  else
  "Citaloprams exists"
  and "Dose of Citalopram < 60 mg/day"

define "Citalopram >= 60 mg/day - QT Prolonging Agents":
  if "InPopulation" is not true then
    null
  else
  "Citaloprams exists"
  and "Dose of Citalopram >= 60 mg/day"
  and "QT Prolonging Agents exists"

define "Citalopram >= 60 mg/day - No QT Prolonging Agents":
  if "InPopulation" is not true then
    null
  else
  "Citaloprams exists"
  and "Dose of Citalopram >= 60 mg/day"
  and "No QT Prolonging Agents"

define "InPopulation":
   "MeetsInclusionCriteria" 

define "Recommendation":
  if "Citalopram >= 60 mg/day - QT Prolonging Agents" then 'Use only if benefit outweighs risk and monitor patient ECG'
  else if "Citalopram >= 60 mg/day - No QT Prolonging Agents" then 'Minimize risk and Monitor ECG'
  else if "Citalopram < 60 mg/day" then 'No special precaution'
  else null

define "Rationale":
  if "Citalopram >= 60 mg/day - QT Prolonging Agents" then 'Increased risk of prolonged QTc likely'
  else if "Citalopram >= 60 mg/day - No QT Prolonging Agents" then 'Increased risk of prolonged QTc possible'
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
    Citaloprams: "Report Citaloprams",
    QTProlongingAgents: "Report QT Prolonging Agents",
    BasicConcomitant: "Basic Concomitant Exposure",
    HighCitalopramDose: "Dose of Citalopram >= 60 mg/day"
  },
  Alert: {
    Recommendation: "Recommendation",
    Rationale: "Rationale"
  }
}
