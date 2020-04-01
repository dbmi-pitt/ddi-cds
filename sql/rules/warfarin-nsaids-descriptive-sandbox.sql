--set search_path to banner_etl;
SELECT de1.person_id, de1.drug_exposure_id AS warf_dexp, de1.drug_concept_id AS warf_id, c1.concept_name AS warf_name, cs1.concept_name AS warf_ingr, de1.drug_exposure_start_datetime AS warf_start, de1.drug_exposure_end_datetime AS warf_end, de1.quantity, de1.sig, de1.route_concept_id as warf_route_id, de1.route_source_value as warf_route,
de2.drug_exposure_id AS nsaid_dexp, de2.drug_concept_id AS nsaid_id, c2.concept_name AS nsaid_name, cs2.concept_name AS nsaid_ingr, de2.drug_exposure_start_datetime AS nsaid_start, de2.drug_exposure_end_datetime AS nsaid_end, de2.quantity, de2.sig, de2.route_concept_id AS nsaid_route_id, de2.route_source_value AS nsaid_route
FROM drug_exposure de1 -- warfarin
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- nsaid
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5876)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7201) -- warfarin ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5853) -- nsaid ingredients
AND ((de2.drug_exposure_start_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de2.drug_exposure_end_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de1.drug_exposure_start_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime)
  OR (de1.drug_exposure_end_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY person_id ASC;
-- 102 in query

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

-- on PPI or misoprostol during concomitant exposure
WITH w_n AS (
  SELECT de1.person_id, de1.drug_exposure_id AS warf_dexp, de1.drug_concept_id AS warf_id, c1.concept_name AS warf_name, cs1.concept_name AS warf_ingr, de1.drug_exposure_start_datetime AS warf_start, de1.drug_exposure_end_datetime AS warf_end, de1.quantity, de1.sig, de1.route_concept_id as warf_route_id, de1.route_source_value as warf_route,
  de2.drug_exposure_id AS nsaid_dexp, de2.drug_concept_id AS nsaid_id, c2.concept_name AS nsaid_name, cs2.concept_name AS nsaid_ingr, de2.drug_exposure_start_datetime AS nsaid_start, de2.drug_exposure_end_datetime AS nsaid_end, de2.quantity, de2.sig, de2.route_concept_id AS nsaid_route_id, de2.route_source_value AS nsaid_route
  FROM drug_exposure de1 -- warfarin
  INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- nsaid
  INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
  INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
  INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
  INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
  INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
  INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
  WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5876)
  AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887)
  AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7201) -- warfarin ingredients
  AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5853) -- nsaid ingredients
  AND ((de2.drug_exposure_start_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
    OR (de2.drug_exposure_end_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
    OR (de1.drug_exposure_start_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime)
    OR (de1.drug_exposure_end_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime))
  AND de1.drug_exposure_id != de2.drug_exposure_id
) SELECT w_n.*, de.drug_exposure_id AS pm_dexp_id, de.drug_concept_id AS pm_id, de.drug_exposure_start_datetime AS pm_start, de.drug_exposure_end_datetime AS pm_end, de.quantity, de.sig, de.route_concept_id, de.route_source_value
FROM drug_exposure de
INNER JOIN w_n ON w_n.person_id = de.person_id
WHERE ((w_n.warf_start BETWEEN de.drug_exposure_start_datetime AND de.drug_exposure_end_datetime)
  OR (w_n.warf_end BETWEEN de.drug_exposure_start_datetime AND de.drug_exposure_end_datetime))
AND de.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 6039) -- PPIs and Misoprostols
ORDER BY person_id, w_n.warf_start ASC;
