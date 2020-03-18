--set search_path to banner_etl;
SELECT de1.person_id, de1.drug_exposure_id AS warf_dexp, de1.drug_concept_id AS warf_id, c1.concept_name AS warf_name, cs1.concept_name AS warf_ingr, de1.drug_exposure_start_datetime AS warf_start, de1.drug_exposure_end_datetime AS warf_end, de1.quantity, de1.sig, de1.route_concept_id as warf_route_id, de1.route_source_value as warf_route,
de2.drug_exposure_id AS salicylate_dexp, de2.drug_concept_id AS salicylate_id, c2.concept_name AS salicylate_name, cs2.concept_name AS salicylate_ingr, de2.drug_exposure_start_datetime AS salicylate_start, de2.drug_exposure_end_datetime AS salicylate_end, de2.quantity, de2.sig, de2.route_concept_id AS salicylate_route_id, de2.route_source_value AS salicylate_route
FROM drug_exposure de1 -- warfarin
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- salicylate
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5876)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11215)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7201) -- warfarin ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 9396) -- salicylate ingredients
AND ((de2.drug_exposure_start_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de2.drug_exposure_end_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de1.drug_exposure_start_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime)
  OR (de1.drug_exposure_end_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY person_id ASC;
-- 449 in query

/*
6119  SSRIs and SNRIs Ingredients
6133  SSRIs and SNRIs
5876  Warfarins
5853  NSAIDs Ingredients
9396  Salicylates Ingredients
5887  NSAIDs
7201  Warfarins Ingredients
11215 Salicylates
*/

/*
ADDITIONAL BRANCHES:
11409 Non-acetylated Salicylates
11397 Bismuth Subsalicylates
11127 Aspirins
*/
