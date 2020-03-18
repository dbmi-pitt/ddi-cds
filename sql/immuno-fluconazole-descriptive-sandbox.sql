--set search_path to banner_etl;
SELECT de1.person_id, de1.drug_exposure_id AS immuno_dexp, de1.drug_concept_id AS immuno_id, c1.concept_name AS imm_name, cs1.concept_name AS immuno_ingr, de1.drug_exposure_start_datetime AS immuno_start, de1.drug_exposure_end_datetime AS immuno_end, de1.quantity, de1.sig, de1.route_concept_id as immuno_route_id, de1.route_source_value as immuno_route,
de2.drug_exposure_id AS fluc_dexp, de2.drug_concept_id AS fluc_id, c2.concept_name AS fluc_name, cs2.concept_name AS fluc_ingr, de2.drug_exposure_start_datetime AS fluc_start, de2.drug_exposure_end_datetime AS fluc_end, de2.quantity, de2.sig, de2.route_concept_id AS fluc_route_id, de2.route_source_value AS fluc_route
FROM drug_exposure de1 -- immuno
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- fluconazole
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 9325)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7205)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 9320) -- immuno ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7203) -- fluconazole ingredients
AND ((de2.drug_exposure_start_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de2.drug_exposure_end_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de1.drug_exposure_start_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime)
  OR (de1.drug_exposure_end_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY person_id ASC;
-- 239 in query
