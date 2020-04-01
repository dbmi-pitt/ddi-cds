--set search_path to banner_etl;
SELECT de1.person_id, de1.drug_exposure_id AS meto_dexp, de1.drug_concept_id AS meto_id, c1.concept_name AS meto_name, cs1.concept_name AS meto_ingr, de1.drug_exposure_start_datetime AS meto_start, de1.drug_exposure_end_datetime AS meto_end, de1.quantity, de1.sig, de1.route_concept_id as meto_route_id, de1.route_source_value as meto_route,
de2.drug_exposure_id AS diuretic_dexp, de2.drug_concept_id AS diuretic_id, c2.concept_name AS diuretic_name, cs2.concept_name AS diuretic_ingr, de2.drug_exposure_start_datetime AS diuretic_start, de2.drug_exposure_end_datetime AS diuretic_end, de2.quantity, de2.sig, de2.route_concept_id AS diuretic_route_id, de2.route_source_value AS diuretic_route
FROM drug_exposure de1 -- metoclopramide
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- antipsycho/chol inhibitor
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 10573)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id in(10666,10640))
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 10571) -- metoclopramide ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id in (10615,10636)) -- antipsych ingredients OR chol inhibitor ingredients
AND ((de2.drug_exposure_start_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de2.drug_exposure_end_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de1.drug_exposure_start_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime)
  OR (de1.drug_exposure_end_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY person_id ASC;
