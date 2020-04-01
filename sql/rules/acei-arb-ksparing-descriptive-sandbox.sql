--set search_path to banner_etl;
SELECT de1.person_id, de1.drug_exposure_id AS aa_dexp, de1.drug_concept_id AS aa_id, c1.concept_name AS aa_name, cs1.concept_name AS aa_ingr, de1.drug_exposure_start_datetime AS aa_start, de1.drug_exposure_end_datetime AS aa_end, de1.quantity, de1.sig, de1.route_concept_id as aa_route_id, de1.route_source_value as aa_route,
de2.drug_exposure_id AS diuretic_dexp, de2.drug_concept_id AS diuretic_id, c2.concept_name AS diuretic_name, cs2.concept_name AS diuretic_ingr, de2.drug_exposure_start_datetime AS diuretic_start, de2.drug_exposure_end_datetime AS diuretic_end, de2.quantity, de2.sig, de2.route_concept_id AS diuretic_route_id, de2.route_source_value AS diuretic_route
FROM drug_exposure de1 -- acei/arb
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- diuretic
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 6653)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 6563)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 6654) -- acei/arb ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 6649) -- diuretic ingredients
AND ((de2.drug_exposure_start_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de2.drug_exposure_end_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de1.drug_exposure_start_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime)
  OR (de1.drug_exposure_end_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY person_id ASC;
-- no concomitant exposures in query or rule output

-- there are no acei/arb exposures in the data according to the concept set: 
SELECT o.concept_id, c.concept_name FROM ohdsi.concept_set_item o
INNER JOIN concept c ON c.concept_id = o.concept_id
WHERE concept_set_id = 6653;
SELECT * FROM drug_exposure WHERE drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 6653);

