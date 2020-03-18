INSERT INTO observation_period (observation_period_id, person_id, observation_period_start_date, observation_period_end_date, period_type_concept_id)
SELECT row_number() over(), person_id, visit_start_date, visit_end_date, 44814722
FROM visit_occurrence;

UPDATE drug_exposure SET effective_drug_dose = 4 WHERE drug_exposure_id = 149421;
UPDATE drug_exposure SET dose_unit_concept_id = 4117638 WHERE drug_exposure_id = 149421;
UPDATE drug_exposure SET effective_drug_dose = 75 WHERE drug_exposure_id = 149422;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149422;

UPDATE drug_exposure SET effective_drug_dose = 8 WHERE drug_exposure_id = 149423;
UPDATE drug_exposure SET dose_unit_concept_id = 4117638 WHERE drug_exposure_id = 149423;
UPDATE drug_exposure SET effective_drug_dose = 75 WHERE drug_exposure_id = 149424;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149424;
UPDATE drug_exposure SET effective_drug_dose = 40 WHERE drug_exposure_id = 149425;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149425;

UPDATE drug_exposure SET effective_drug_dose = 8 WHERE drug_exposure_id = 149426;
UPDATE drug_exposure SET dose_unit_concept_id = 4117638 WHERE drug_exposure_id = 149426;
UPDATE drug_exposure SET effective_drug_dose = 75 WHERE drug_exposure_id = 149427;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149427;

UPDATE drug_exposure SET effective_drug_dose = 100 WHERE drug_exposure_id = 149428;
UPDATE drug_exposure SET dose_unit_concept_id = 4117638 WHERE drug_exposure_id = 149428;
UPDATE drug_exposure SET effective_drug_dose = 75 WHERE drug_exposure_id = 149429;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149429;

UPDATE drug_exposure SET effective_drug_dose = 100 WHERE drug_exposure_id = 149428;
UPDATE drug_exposure SET dose_unit_concept_id = 4117638 WHERE drug_exposure_id = 149428;
UPDATE drug_exposure SET effective_drug_dose = 75 WHERE drug_exposure_id = 149429;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149429;

UPDATE drug_exposure SET effective_drug_dose = 100 WHERE drug_exposure_id = 149438;
UPDATE drug_exposure SET dose_unit_concept_id = 4117638 WHERE drug_exposure_id = 149438;
UPDATE drug_exposure SET effective_drug_dose = 75 WHERE drug_exposure_id = 149439;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149439;

UPDATE drug_exposure SET effective_drug_dose = 4 WHERE drug_exposure_id = 149517;
UPDATE drug_exposure SET dose_unit_concept_id = 4117638 WHERE drug_exposure_id = 149517;
UPDATE drug_exposure SET effective_drug_dose = 75 WHERE drug_exposure_id = 149518;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149518;

UPDATE drug_exposure SET effective_drug_dose = 4 WHERE drug_exposure_id = 149560;
UPDATE drug_exposure SET dose_unit_concept_id = 4117638 WHERE drug_exposure_id = 149560;
UPDATE drug_exposure SET effective_drug_dose = 75 WHERE drug_exposure_id = 149561;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149561;
UPDATE drug_exposure SET effective_drug_dose = 50 WHERE drug_exposure_id = 149562;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149562;

UPDATE drug_exposure SET effective_drug_dose = 100 WHERE drug_exposure_id = 149469;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149469;
UPDATE drug_exposure SET effective_drug_dose = 200 WHERE drug_exposure_id = 149470;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149470;

UPDATE drug_exposure SET effective_drug_dose = 100 WHERE drug_exposure_id = 149471;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149471;
UPDATE drug_exposure SET effective_drug_dose = 200 WHERE drug_exposure_id = 149472;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149472;

UPDATE drug_exposure SET effective_drug_dose = 100 WHERE drug_exposure_id = 149473;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149473;
UPDATE drug_exposure SET effective_drug_dose = 400 WHERE drug_exposure_id = 149474;
UPDATE drug_exposure SET dose_unit_concept_id = 4120731 WHERE drug_exposure_id = 149474;

