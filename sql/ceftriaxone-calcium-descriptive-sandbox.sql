--set search_path to banner_etl;
SELECT de1.person_id, de1.drug_exposure_id AS ceft_dexp, de1.drug_concept_id AS ceft_id, c1.concept_name AS ceft_name, cs1.concept_name AS ceft_ingr, de1.drug_exposure_start_datetime AS ceft_start, de1.drug_exposure_end_datetime AS ceft_end, de1.quantity, de1.sig, de1.route_concept_id as ceft_route_id, de1.route_source_value as ceft_route,
de2.drug_exposure_id AS ca_dexp, de2.drug_concept_id AS ca_id, c2.concept_name AS ca_name, cs2.concept_name AS ca_ingr, de2.drug_exposure_start_datetime AS ca_start, de2.drug_exposure_end_datetime AS ca_end, de2.quantity, de2.sig, de2.route_concept_id AS ca_route_id, de2.route_source_value AS ca_route
FROM drug_exposure de1 -- ceft
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- calcium
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 10565)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 10563)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 10566) -- ceft ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 10564) -- calcium ingredients
AND ((de2.drug_exposure_start_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de2.drug_exposure_end_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de1.drug_exposure_start_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime)
  OR (de1.drug_exposure_end_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY person_id ASC;
-- no concomitant exposures from neither query nor the rule output

-- there are no ceftriaxone exposures in the data according to the concept set:

-- just ceftriaxone
SELECT o.concept_id, c.concept_name FROM ohdsi.concept_set_item o
INNER JOIN concept c ON c.concept_id = o.concept_id
WHERE concept_set_id = 10565;
SELECT * FROM drug_exposure WHERE drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 10566);

