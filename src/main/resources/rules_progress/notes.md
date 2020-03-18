# NOTES
### Ceftriaxone - Calcium
  - All items in the Ceftriaxone & Calcium concept sets are injectible. Treating these all of these as IV.
  - Data limitations: 
    - No clear way to determine "simultaneous" administration of calcium / ceftriaxone on an IV. Currently using same datetime.

### Clonidine - BB
  - need "Timolols Systemic" concept set?
  - Using "Clonidines Injectable" be used for "epidural"

### Epi - BB
  - How to tell if epinephrine is combined with local anesthetic?
    - A function of combo products / small doses (i.e. less than 0.01 mg/ml), ex. bupivacaine hcl. Can use this to break down different types of epinephrine (systemic vs. not systemic)
    
      `select c.concept_name, ds.amount_value, ds.numerator_value, ds.denominator_value, ds.ingredient_concept_id 
      from ohdsi.concept_set cs
      inner join ohdsi.concept_set_item i
      on i.concept_set_id = cs.concept_set_id
      inner join public.concept c
      on c.concept_id = i.concept_id
      inner join public.drug_strength ds
      on ds.drug_concept_id = i.concept_id
      where cs.concept_set_name in ('Epinephrines')
      and ds.ingredient_concept_id = 1343916
      order by ds.numerator_value asc;`
  - Data limitations: 
    - "Patient in anaphylaxis", "Anaphylaxis prevention", "Dermatological or dental use", "Plastic surgery use" branches currently not considered since the database does not have any support for "Indications", and there are no patients in the dataset that have these conditions based on the current condition concept sets.

### Immuno - Fluconazole

### Warfarin - NSAIDs
  - Our data is not able to determine if a patient is able to start on PPI. Assuming that all patients are able to do so.

### Warfarin - Salicylates
  - previous rule file had "Bismuth Subsalicylate" rule - but this branch is not depicted in either old or new diagrams. Still included this as its own independent branch.

### Warfarin - SSRI+SNRI

### ACE I/ARB - K-Sparing Diuretics
  - eGFR Measurements -- diagrams use "ml/hour" but database uses "ml/min/1.73sq.m"
    - concept ID's:
      - 3049187
      - 3053283
      - 3030354
    - LOINC:
      - 48642-3
      - 48643-1
      - 33914-3

### K - K-Sparing Diuretics

### Fluconazole - Opioids
  - eGFR Measurements (see above)
  - "Outpatient?" branch currently not considered since it doesn't look like anyone in the banner data set has visit concept id of 9202 for "outpatient". Everyone has visit_concept_id = 262 = "Emergency Room and Inpatient Visit"

---

## General Notes:
For daily doses, not sure we can rely on doseUnitSourceValue if we need to filter by unit.
Measurement units proved unreliable. Commented these out where measurements are used.
