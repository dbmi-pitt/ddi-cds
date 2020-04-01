--set search_path to banner_etl;
SELECT de1.person_id, de1.drug_exposure_id AS ssrisnri_dexp, de1.drug_concept_id AS ssrisnri_id, c1.concept_name AS ssrisnri_name, cs1.concept_name AS ssrisnri_ingr, de1.drug_exposure_start_datetime AS ssrisnri_start, de1.drug_exposure_end_datetime AS ssrisnri_end, de1.quantity, de1.sig, de1.route_concept_id as ssrisnri_route_id, de1.route_source_value as ssrisnri_route,
de2.drug_exposure_id AS thiaz_dexp, de2.drug_concept_id AS thiaz_id, c2.concept_name AS thiaz_name, cs2.concept_name AS thiaz_ingr, de2.drug_exposure_start_datetime AS thiaz_start, de2.drug_exposure_end_datetime AS thiaz_end, de2.quantity, de2.sig, de2.route_concept_id AS thiaz_route_id, de2.route_source_value AS thiaz_route
FROM drug_exposure de1 -- ssri/snri
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- thiazide
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id in (6133))
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id in (90000))
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id in (6119)) -- ssri/snri  ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id in (90001)) -- thiazide ingredients
AND ((de2.drug_exposure_start_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de2.drug_exposure_end_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de1.drug_exposure_start_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime)
  OR (de1.drug_exposure_end_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY person_id ASC;
