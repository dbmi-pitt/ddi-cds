set search_path to banner_etl;

SELECT de1.person_id, de1.drug_exposure_id AS citalopram_dexp, de1.drug_concept_id AS citalopram_id, c1.concept_name AS citalopram_name, cs1.concept_name AS citalopram_ingr, de1.drug_exposure_start_datetime AS citalopram_start, de1.drug_exposure_end_datetime AS citalopram_end, ds1.amount_value, sm1.expected, de1.sig, de1.route_concept_id as citalopram_route_id, de1.route_source_value as citalopram_route,
de2.drug_exposure_id AS qt_dexp, de2.drug_concept_id AS qt_id, c2.concept_name AS qt_name, cs2.concept_name AS qt_ingr, de2.drug_exposure_start_datetime AS qt_start, de2.drug_exposure_end_datetime AS qt_end, de2.quantity, de2.sig, de2.route_concept_id AS qt_route_id, de2.route_source_value AS qt_route
FROM drug_exposure de1 
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id 
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
INNER JOIN sig_mapping sm1 ON de1.sig = sm1.sig
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 12115) -- citaloprams
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11441) -- qt-agents
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 12114) -- citaloprams ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11466) -- qt-agents ingredients
AND ((de2.drug_exposure_start_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de2.drug_exposure_end_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de1.drug_exposure_start_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime)
  OR (de1.drug_exposure_end_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY ds1.amount_value, sm1.expected DESC; -- sort by these characteristics to confirm that no dosage exceeds 60 mg/day
-- basic concomitant -- 680 rows from query.
