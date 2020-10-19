library ReusableFunctions_FHIRv300 version '1.0.0'

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0' called FHIRHelpers 

include CDS_Connect_Commons_for_FHIRv300 version '1.1.0' called C3F 

// ## Re-usable functions ##

// Returns the first-found display text for a CodeableConcept, looking first at the `text` attribute, then the
// `display` on each `coding` until it finds a non-null value.
// @param c - a FHIR CodeableConcept to get text from
// @returns {System.String} the display text or null if none is found
define function ConceptText(c FHIR.CodeableConcept):
  Coalesce(c.text.value, Coalesce((c.coding) c2 return c2.display.value))

// We do not reliably get ranges back from EHR data, but ranges are important to understanding assessment scores in
// the summary.  This function adds ranges to the name for known assessments.
// @param c - a FHIR CodeableConcept to get the updated text for
// @returns {System.String} the display text with range information appended
/*define function ConceptTextWithRange(c FHIR.CodeableConcept):
  if c ~ "Patient Health Questionnaire 2 item (PHQ-2) total score [Reported]"
    then ConceptText(c) + ' [Range: 0-6]'
  else if c ~ "Patient Health Questionnaire 9 item (PHQ-9) total score [Reported]"
    then ConceptText(c) + ' [Range: 0-27]'
  else if c ~ "Generalized anxiety disorder 7 item (GAD-7) total score [Reported.PHQ]"
    then ConceptText(c) + ' [Range: 0-21]'
  else if c ~ "Total score [AUDIT]"
    then ConceptText(c) + ' [Range: 0-40]'
  else if c ~ "Total score [AUDIT-C]"
    then ConceptText(c) + ' [Range: 0-12]'
  else if c ~ "Total score [DAST-10]"
    then ConceptText(c) + ' [Range: 0-10]'
  else if c ~ "Pain severity Wong-Baker FACES Scale"
    then ConceptText(c) + ' [Range: 0-10]'
  else if c ~ "Pain Enjoyment General Activity (PEG) Assessment"
    then ConceptText(c) + ' [Range: 0-10]'
  else if c ~ "STarT Back Screening Tool"
    then ConceptText(c) + ' [Range: 0-9]'
  else
    ConceptText(c)*/

// Returns a text representation of a date using the CQL `ToString` function.
// @param d - a FHIR date to get text for
// @returns {System.String} the text representation of the date
define function DateText(d FHIR.date):
  ToString(d.value)

// Returns a text representation of a dateTime using the CQL `ToString` function.
// @param d - a FHIR dateTime to get text for
// @returns {System.String} the text representation of the dateTime
define function DateTimeText(d FHIR.dateTime):
  ToString(d.value)

// Returns a text representation of an instant using the CQL `ToString` function.
// @param i - a FHIR instant to get text for
// @returns {System.String} the text representation of the instant
define function InstantText(i FHIR.instant):
  ToString(i.value)

// Returns a text representation of a Quantity with the Quantity's value and unit.
// @param q - a FHIR Quantity to get text for
// @returns {System.String} the text representation of the Quantity
define function QuantityText(q FHIR.Quantity):
  if (q is null) then null
  else if (q.unit is not null) then ToString(q.value.value) + ' ' + q.unit.value
  else if (q.code is not null) then ToString(q.value.value) + ' ' + q.code.value
  else ToString(q.value.value)

// Returns a tuple containing a Period's start and end as text representations.
// @param p - a FHIR Period to get a tuple for
// @returns {Tuple<Start System.String, End System.String>} the tuple representation of the Period
define function PeriodObject(p FHIR.Period):
  if p is null then null
  else
    {
      Start: DateTimeText(p."start"),
      End: DateTimeText(p."end")
    }

// Returns a tuple containing a Range's low and high as text representations.
// @param r - a FHIR Range to get a tuple for
// @returns {Tuple<Low System.String, High System.String>} the tuple representation of the Range
define function RangeObject(r FHIR.Range):
  if r is null then null
  else
    {
      Low: QuantityText(r.low),
      High: QuantityText(r.high)
    }

// Returns a text representation of a date associated with an Observation, preferring `effectiveDateTime`, then
// `effectivePeriod.start`, then `issued`.
// @param o - a FHIR Observation to get the text date from
// @returns {System.String} the text representation of a relevant date from the Observation
define function ObservationDate(o FHIR.Observation):
  Coalesce(DateTimeText(o.effective as FHIR.dateTime), DateTimeText((o.effective as FHIR.Period)."start"), InstantText(o.issued))

// Returns a text representation of a date associated with a MedicationStatement, preferring `effectiveDateTime`, then
// `.start`.
// @param m - a FHIR MedicationStatement to get the text date from
// @returns {System.String} the text representation of a relevant date from the MedicationStatement
define function MedicationStatementDate(s FHIR.MedicationStatement):
  Coalesce(DateTimeText(s.effective as FHIR.dateTime), DateTimeText((s.effective as FHIR.Period)."start"))

// Returns a text representation of a Condition's onset, whether represented as a dateTime, Period, Quantity (Age),
// Range (Age), or string.
// @param c - a FHIR Condition to get the text representation of the onset from
// @returns {System.String} the text representation of the Condition's onset
define function Onset(c FHIR.Condition):
  Coalesce( List{
    DateTimeText(c.onset as FHIR.dateTime),
    QuantityText(c.onset as FHIR.Age),
    PeriodObject(c.onset as FHIR.Period),
    RangeObject(c.onset as FHIR.Range),
    (c.onset as FHIR.string).value
  })

// Returns a text representation of a Condition's abatement, whether represented as a dateTime, Period, Quantity (Age),
// Range (Age), string, or boolean.
// @param c - a FHIR Condition to get the text representation of the abatement from
// @returns {System.String} the text representation of the Condition's abatement
define function Abatement(c FHIR.Condition):
  Coalesce( List{
    DateTimeText(c.abatement as FHIR.dateTime),
    QuantityText(c.abatement as FHIR.Age),
    PeriodObject(c.abatement as FHIR.Period),
    RangeObject(c.abatement as FHIR.Range),
    (c.abatement as FHIR.string).value,
    ToString((c.abatement as FHIR.boolean).value)
  })

// Sorts the components of a PEG Observation to be in the order P (Pain), E (Enjoyment of life), G (General activity)
// @param c - a list of FHIR Observation.Components to be sorted in PEG order
// @returns {List<FHIR."Observation.Component">} the list of FHIR Observation.Components sorted in PEG order
/*define function SortPegComponents(c List<FHIR."Observation.Component">):
  flatten List{
    c OC where OC.code ~ "Pain",
    c OC where OC.code ~ "Enjoyment of life",
    c OC where OC.code ~ "General activity"
  }*/

// Returns the first-found display text for an INGREDIENT within a CodeableConcept
// Medications should have an "ingredient" field that includes a list of "itemCodeableConcept" items. 
// TODO match Medication object with MedicationStatement object based on identical coding
define function DrugDailyDose(c FHIR.Medication):
  c.ingredient[0].amount.numerator.value.value / c.ingredient[0].amount.denominator.value.value
