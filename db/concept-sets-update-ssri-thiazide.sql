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
