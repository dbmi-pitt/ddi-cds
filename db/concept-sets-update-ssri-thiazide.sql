/*
TODO's:
CONCEPT SETS:
  * List of thiazide diuretics?

  * "Sodium concentration" lab tests?
    * Sodium lab test LOINC codes taken from previous healthfacts analysis in the db: 2947-0, 2951-2 => lab measurement concept ID's 3000285, 3019550
    However it looks like in the available data these aren't used:
    SELECT * FROM banner_2019_inpatient.measurement m INNER JOIN banner_2019_inpatient.concept c ON c.concept_id = m.measurement_concept_id
    WHERE c.concept_name ILIKE '%sodium%';
    SELECT * FROM banner_etl.measurement m INNER JOIN banner_2019_inpatient.concept c ON c.concept_id = m.measurement_concept_id
    WHERE c.concept_name ILIKE '%sodium%';

  * "A396T variant of SLC02A1"? 

  * Sodium restricted diet? 
*/

-- temporary Thiazide concept set
INSERT INTO ohdsi.concept_set 
VALUES (90000, 'Thiazide/Thiazide-like Diuretics'),
(90001, 'Thiazide/Thiazide-like Diuretics Ingredients');

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
SELECT 90000 AS concept_set_id, concept_id, 0 AS is_excluded, 0 AS include_descendants, 0 AS include_mapped
FROM concept 
WHERE concept_name ILIKE '%hydrochlorothiazide%' 
AND domain_id = 'Drug' 
AND vocabulary_id = 'RxNorm' 
AND concept_class_id = 'Clinical Drug'
AND standard_concept = 'S';

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
SELECT 90001 AS concept_set_id, concept_id, 0 AS is_excluded, 0 AS include_descendants, 0 AS include_mapped
FROM concept 
WHERE concept_name ILIKE '%hydrochlorothiazide%'
AND domain_id = 'Drug' 
AND vocabulary_id = 'RxNorm' 
AND concept_class_id = 'Ingredient'
AND standard_concept = 'S';

-- concept sets for conditions
INSERT INTO ohdsi.concept_set 
VALUES (90002, 'History of HIV/AIDS'),
(90003, 'History of Adrenal Insufficiency'),
(90004, 'History of Heart Failure'),
(90005, 'History of Hepatic Cirrhosis'),
(90006, 'History of Malignancy'),
(90007, 'History of Pneumonia');

-- HIV/AIDS
INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
SELECT 90002 AS concept_set_id, concept_id, 0 AS is_excluded, 0 AS include_descendants, 0 AS include_mapped
FROM concept 
WHERE vocabulary_id IN ('ICD9CM', 'ICD10CM')
AND concept_code IN ('042', 'B20');

-- Adrenal Insufficiency
INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
SELECT 90003 AS concept_set_id, concept_id, 0 AS is_excluded, 0 AS include_descendants, 0 AS include_mapped
FROM concept 
WHERE vocabulary_id IN ('ICD9CM', 'ICD10CM')
AND ((concept_code ILIKE '255.4%' AND vocabulary_id = 'ICD9CM')
OR (concept_code IN ('E27.1', 'E27.2', 'E27.40', 'E27.49') AND vocabulary_id = 'ICD10CM'));

-- Heart Failure
INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
SELECT 90004 AS concept_set_id, concept_id, 0 AS is_excluded, 0 AS include_descendants, 0 AS include_mapped 
FROM concept 
WHERE vocabulary_id IN ('ICD9CM', 'ICD10CM')
AND ((concept_code ILIKE '428.%' AND vocabulary_id = 'ICD9CM')
OR (concept_code ILIKE '150.%' AND vocabulary_id = 'ICD10CM'));

--  Hepatic Cirrhosis
INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
SELECT 90005 AS concept_set_id, concept_id, 0 AS is_excluded, 0 AS include_descendants, 0 AS include_mapped 
FROM concept 
WHERE vocabulary_id IN ('ICD9CM', 'ICD10CM')
AND ((concept_code IN ('571.2', '571.5', '571.6') AND vocabulary_id = 'ICD9CM')
OR (concept_code ILIKE 'K70.3%' AND vocabulary_id = 'ICD10CM')
OR (concept_code ILIKE 'K74%' AND vocabulary_id = 'ICD10CM'));

-- TODO Malignancy
-- (ICD-9 codes 140.xx to 239.xx) (C* ICD10)

-- Pneumonia
INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
SELECT 90007 AS concept_set_id, concept_id, 0 AS is_excluded, 0 AS include_descendants, 0 AS include_mapped 
FROM concept 
WHERE vocabulary_id IN ('ICD9CM', 'ICD10CM')
AND ((concept_code ILIKE '480%' AND vocabulary_id = 'ICD9CM')
OR (concept_code ILIKE '481%' AND vocabulary_id = 'ICD9CM')
OR (concept_code ILIKE '482%' AND vocabulary_id = 'ICD9CM')
OR (concept_code ILIKE '483%' AND vocabulary_id = 'ICD9CM')
OR (concept_code ILIKE '484%' AND vocabulary_id = 'ICD9CM')
OR (concept_code ILIKE '485%' AND vocabulary_id = 'ICD9CM')
OR (concept_code ILIKE '486%' AND vocabulary_id = 'ICD9CM')
OR (concept_code ILIKE 'J12%' AND vocabulary_id = 'ICD10CM')
OR (concept_code ILIKE 'J13%' AND vocabulary_id = 'ICD10CM')
OR (concept_code ILIKE 'J18.1%' AND vocabulary_id = 'ICD10CM'));
