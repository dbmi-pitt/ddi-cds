/* Due to ObservationLookBack, you will need to modify the date in the observation files to be within the past 2 days */
library ACEiARBKsparingdiuretics version '1.0'

using FHIR version '3.0.0'

include "FHIRHelpers" version '3.0.0' called FHIRHelpers
include "CDSConnectCommonsforFHIRv300" version '1.0.1' called C3F
include "CDSConnectConversions" version '1' called Convert
codesystem "LOINC": 'http://loinc.org'

valueset "K Sparing Diuretics VS": '2.16.840.1.113762.1.4.1213.41'
valueset "ACEis and ARBs VS": '2.16.840.1.113762.1.4.1213.11'

code "Potassium [Moles/volume] in Serum or Plasma code": '2823-3' from "LOINC"
  display 'Potassium [Moles/volume] in Serum or Plasma'

code "48643-1 code": '48643-1' from "LOINC"
  display 'Glomerular filtration rate/1.73 sq M.predicted among blacks [Volume Rate/Area] in Serum, Plasma or Blood by Creatinine-based formula (MDRD)'

code "48642-3 code": '48642-3' from "LOINC"
  display 'Glomerular filtration rate/1.73 sq M.predicted among non-blacks [Volume Rate/Area] in Serum, Plasma or Blood by Creatinine-based formula (MDRD)'


parameter "Potassium threshold" default 5.0
parameter "eGFR Threshold" default Interval[60,1000]

context Patient

define "generic_observation_concepts":
  [Observation: "48643-1 code"]
  union [Observation: "48642-3 code"]

define "generic_observation_concepts_1":
  [Observation: "48643-1 code"]
  union [Observation: "48642-3 code"]

define "K Sparing Diuretics Med Statement":
  [MedicationStatement: "K Sparing Diuretics VS"]

define "K Sparing Diuretics Med Order":
  [MedicationRequest: "K Sparing Diuretics VS"]

define "ACEis and ARBs Med Statements":
  [MedicationStatement: "ACEis and ARBs VS"]

define "ACEis and ARBs Med Orders":
  [MedicationRequest: "ACEis and ARBs VS"]

define "Potassium [Moles/volume] in Serum or Plasma":
  if System.Quantity { value: ((C3F.ObservationLookBack([Observation: "Potassium [Moles/volume] in Serum or Plasma code"], 48 hours)).value[0] as Quantity).value, unit: 'mmol' } < 5 'mmol' then true else false


define "eGFR":
  if System.Quantity { value: ((C3F.ObservationLookBack("generic_observation_concepts", 48 hours)).value[0] as Quantity).value, unit: 'mL' } <= 60 'mL' then true else false


define "Potassium [Moles/volume] in Serum or Plasma >= 5":
  if System.Quantity { value: ((C3F.ObservationLookBack([Observation: "Potassium [Moles/volume] in Serum or Plasma code"], 48 hours)).value[0] as Quantity).value, unit: 'mmol' } >= 5 'mmol' then true else false


define "eGFR > 60 mL":
  if System.Quantity { value: ((C3F.ObservationLookBack("generic_observation_concepts_1", 48 hours)).value[0] as Quantity).value, unit: 'ml' } > 60 'ml' then true else false


define "K Sparing Diuretics Med Statement exists":
  exists("K Sparing Diuretics Med Statement")

define "K Sparing Diuretics Med Order exists":
  exists("K Sparing Diuretics Med Order")

define "ACEis and ARBs Med Statements Exist":
  exists("ACEis and ARBs Med Statements")

define "ACEis and ARBs Med Orders Exist":
  exists("ACEis and ARBs Med Orders")

define "MeetsInclusionCriteria":
  "K Sparing Diuretics"
  and "ACEis and ARBs"

define "K Sparing Diuretics":
  "K Sparing Diuretics Med Statement exists"
  or "K Sparing Diuretics Med Order exists"

define "ACEis and ARBs":
  "ACEis and ARBs Med Statements Exist"
  or "ACEis and ARBs Med Orders Exist"

define "Low Risk ACEi/ARB - K-sparing diuretic":
  if "InPopulation" is not true then
    null
  else
  "Potassium [Moles/volume] in Serum or Plasma"
  and "eGFR > 60 mL"

define "High Risk ACEi/ARB - K-sparing diuretic:":
  if "InPopulation" is not true then
    null
  else
  "Potassium [Moles/volume] in Serum or Plasma >= 5"

define "Moderate Risk ACEi/ARB - K-sparing diuretic:":
  if "InPopulation" is not true then
    null
  else
  "Potassium [Moles/volume] in Serum or Plasma"
  and "eGFR"

define "InPopulation":
   "MeetsInclusionCriteria"

define "Recommendation":
  if "Low Risk ACEi/ARB - K-sparing diuretic" then 'Increased risk of hyperkalemia unlikely'
  else if "High Risk ACEi/ARB - K-sparing diuretic:" then 'Use only if benefit outweighs risk'
  else if "Moderate Risk ACEi/ARB - K-sparing diuretic:" then 'Monitor potassium levels, asses risk and take action if necessary'
  else null

define "Rationale":
  if "Low Risk ACEi/ARB - K-sparing diuretic" then null
  else if "High Risk ACEi/ARB - K-sparing diuretic:" then 'Increased risk of hyperkalemia possible'
  else if "Moderate Risk ACEi/ARB - K-sparing diuretic:" then 'Increased risk of hyperkalemia possible'
  else null

define "Classification":
  if "Low Risk ACEi/ARB - K-sparing diuretic" then 'No Special Precautions'
  else if "High Risk ACEi/ARB - K-sparing diuretic:" then 'Usually Avoid Combination'
  else if "Moderate Risk ACEi/ARB - K-sparing diuretic:" then 'Monitor'
  else null

define "Indicator":
  if "Low Risk ACEi/ARB - K-sparing diuretic" then 'info'
  else if "High Risk ACEi/ARB - K-sparing diuretic:" then 'critical'
  else if "Moderate Risk ACEi/ARB - K-sparing diuretic:" then 'warning'
  else null
define "Errors":
  null
