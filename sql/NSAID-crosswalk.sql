---------
--- NSAIDS

SELECT de1.person_id, de1.drug_exposure_id AS warf_dexp, de1.drug_concept_id AS warf_id, c1.concept_name AS warf_name, cs1.concept_name AS warf_ingr, de1.drug_exposure_start_datetime AS warf_start, de1.drug_exposure_end_datetime AS warf_end, de1.quantity, de1.sig, de1.route_concept_id as warf_route_id, de1.route_source_value as warf_route
FROM drug_exposure de1
  INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
  INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
  INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887)
  AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5853)
ORDER BY person_id ASC;

--- 6637    -- in 2016, a three month selective sample, we see 11899
-- 
SELECT count(de1.drug_exposure_id)
FROM drug_exposure de1
  INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
  INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
  INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887)
  AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5853)
;


-- for each NSAID, see what the exposure count
SELECT de1.drug_concept_id, c1.concept_name, count(de1.drug_exposure_id)
FROM drug_exposure de1
  INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
  INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
  INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887)
group by de1.drug_concept_id, c1.concept_name
;

--  drug_concept_id |                    concept_name                     | count 
-- -----------------+-----------------------------------------------------+-------
--          1113673 | nabumetone 750 MG Oral Tablet                       |     1
--          1115066 | Naproxen 25 MG/ML Oral Suspension                   |     5
--          1150347 | meloxicam 15 MG Oral Tablet                         |     1
--          1178665 | Indomethacin 25 MG Oral Capsule                     |    33
--          1178666 | Indomethacin 50 MG Oral Capsule                     |    27
--          1178667 | Indomethacin 50 MG Rectal Suppository               |     7
--          1236609 | Sulindac 150 MG Oral Tablet                         |     3
--          1236610 | Sulindac 200 MG Oral Tablet                         |     2
--         19019050 | Ibuprofen 20 MG/ML Oral Suspension                  |   750
--         19019072 | Ibuprofen 400 MG Oral Tablet                        |   510
--         19019073 | Ibuprofen 600 MG Oral Tablet                        |  1286
--         19019074 | Ibuprofen 800 MG Oral Tablet                        |   273
--         19019272 | Naproxen 250 MG Oral Tablet                         |    41
--         19029024 | celecoxib 100 MG Oral Capsule                       |    48
--         19078461 | Ibuprofen 200 MG Oral Tablet                        |   102
--         19078521 | Indomethacin 5 MG/ML Oral Suspension                |     4
--         19078522 | Indomethacin 75 MG Extended Release Oral Capsule    |     1
--         19133853 | Ketorolac Tromethamine 10 MG Oral Tablet            |   120
--         35604184 | 2 ML Ibuprofen 10 MG/ML Injection                   |    76
--         40162359 | Diclofenac Sodium 75 MG Delayed Release Oral Tablet |     2
--         40164851 | Ketorolac Tromethamine 30 MG/ML Injectable Solution |  3345
-- (21 rows)

-- mdiadb=> 
-- mdiadb=> select count(*) from ohdsi.concept_set_item where concept_set_id = 5887;
--  count 
-- -------
--   1346
-- (1 row)

-- using the drug exposures from the 2018 dataset and joining with the
-- concept_name from the original concept_set, can we identify
-- terminology changes that affected this?
with cnames as (select distinct concept.concept_name from ohdsi.concept_set_item ci inner join banner_etl.concept on ci.concept_id = concept.concept_id where ci.concept_set_id = 5887) select distinct c.concept_id, c.concept_name from banner_2019_inpatient.drug_exposure de inner join banner_2019_inpatient.concept c on de.drug_concept_id = c.concept_id inner join cnames on c.concept_name = cnames.concept_name;
--  concept_id |                    concept_name                     
-- ------------+-----------------------------------------------------
--     1113673 | nabumetone 750 MG Oral Tablet
--     1115066 | Naproxen 25 MG/ML Oral Suspension
--     1150347 | meloxicam 15 MG Oral Tablet
--     1178665 | Indomethacin 25 MG Oral Capsule
--     1178666 | Indomethacin 50 MG Oral Capsule
--     1178667 | Indomethacin 50 MG Rectal Suppository
--     1236609 | Sulindac 150 MG Oral Tablet
--     1236610 | Sulindac 200 MG Oral Tablet
--    19019050 | Ibuprofen 20 MG/ML Oral Suspension
--    19019072 | Ibuprofen 400 MG Oral Tablet
--    19019073 | Ibuprofen 600 MG Oral Tablet
--    19019074 | Ibuprofen 800 MG Oral Tablet
--    19019272 | Naproxen 250 MG Oral Tablet
--    19029024 | celecoxib 100 MG Oral Capsule
--    19078461 | Ibuprofen 200 MG Oral Tablet
--    19078521 | Indomethacin 5 MG/ML Oral Suspension
--    19078522 | Indomethacin 75 MG Extended Release Oral Capsule
--    19133853 | Ketorolac Tromethamine 10 MG Oral Tablet
--    35604184 | 2 ML Ibuprofen 10 MG/ML Injection
--    40162359 | Diclofenac Sodium 75 MG Delayed Release Oral Tablet
--    40164851 | Ketorolac Tromethamine 30 MG/ML Injectable Solution
-- (21 rows)


-- Which NSAIDs do not show up in the newer dataset
with cnames as (select distinct concept.concept_name from ohdsi.concept_set_item ci inner join banner_etl.concept on ci.concept_id = concept.concept_id where ci.concept_set_id = 5887)
select distinct c.concept_id, c.concept_name
from banner_2019_inpatient.drug_exposure de inner join banner_2019_inpatient.concept c on de.drug_concept_id = c.concept_id
  left outer join cnames on c.concept_name = cnames.concept_name
where cnames.concept_name is null
;

-- Try to find drug_exposures to NSAIDs using the ATC mapping in the vocab
-- Concept ancestor table ATC: M01A
SELECT a.*, 
CASE WHEN cd.concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887) THEN TRUE 
  ELSE FALSE 
  END AS in_concept_set,
cd.concept_name AS desc_name, cd.concept_code AS desc_rxnorm, cd.concept_class_id, cd.domain_id, cd.vocabulary_id,
ca.concept_name AS anc_name, ca.concept_code AS anc_atc--, ca.concept_class_id, ca.domain_id, ca.vocabulary_id
FROM banner_2019_inpatient.concept_ancestor a
INNER JOIN banner_2019_inpatient.concept ca ON ca.concept_id = a.ancestor_concept_id 
LEFT JOIN banner_2019_inpatient.concept cd ON cd.concept_id = a.descendant_concept_id 
WHERE ca.vocabulary_id = 'ATC'
AND ca.concept_code = 'M01A'
AND cd.concept_class_id ILIKE '%Clinical Drug%'; -- concept set only includes "Clinical Drug" and "Quant Clinical Drug" concept classes
-- 1346 concepts are in the NSAID concept set, but just 267 are directly mapped to ATC.

SELECT *
FROM banner_2019_inpatient.concept_ancestor a
INNER JOIN banner_2019_inpatient.concept ca ON ca.concept_id = a.ancestor_concept_id 
INNER JOIN banner_2019_inpatient.drug_exposure de ON de.drug_concept_id = a.descendant_concept_id 
WHERE ca.vocabulary_id = 'ATC'
AND ca.concept_code = 'M01A';
-- 3193 drug exposures, compared with 6619 from the plain concept set (below). In the old banner data there were 11899 drug exposures.
SELECT COUNT(*) FROM banner_2019_inpatient.drug_exposure de
WHERE de.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887);

-- what is in the concept set (assuming complete mapping to through the vocab - which might not true) but not in the DE table
SELECT c.concept_id, c1.concept_name
FROM ohdsi.concept_set_item c inner join concept c1 on c.concept_id = c1.concept_id
   left outer join drug_exposure de1 on c.concept_set_id = 5887 and c.concept_set_id = de1.drug_concept_id    
where de1.drug_concept_id is null;

-- CONCEPT CLASS 
-- general look into distribution of drug concept classes, i.e. clinical drugs vs branded vs drug packs etc
SELECT c.concept_class_id, COUNT(de.drug_exposure_id) AS num
FROM banner_2019_inpatient.drug_exposure de 
INNER JOIN banner_2019_inpatient.concept c 
ON c.concept_id = de.drug_concept_id
GROUP BY c.concept_class_id
ORDER BY num DESC;
/*
|concept_class_id    |num                 |
|--------------------|--------------------|
|Clinical Drug       |1036047             |
|Quant Clinical Drug |162439              |
|Clinical Drug Form  |115                 |
|Clinical Pack       |1                   |

Concept class counts for OLD banner data below - still not a significant amount of branded drugs.
Ingredient concept classes are in there but this does not affect concept set inclusion for the individual drugs. Looking through the data, it seems like these tend to correspond with the appropriate "Drug" concept class ID's.

|concept_class_id    |num                 |
|--------------------|--------------------|
|Quant Clinical Drug |226880              |
|Ingredient          |145070              |
|Precise Ingredient  |109684              |
|Clinical Drug       |93075               |
|Clinical Drug Comp  |439                 |
|Branded Drug        |266                 |
|Clinical Drug Form  |68                  |
*/

-- comparing most frequently represented NSAIDs from the concept set in old banner data and new banner data
SELECT c.concept_name AS nsaid_drug, c.concept_class_id, COUNT(DISTINCT de.drug_exposure_id) AS num
FROM banner_etl.drug_exposure de
INNER JOIN banner_etl.concept c ON c.concept_id = de.drug_concept_id 
WHERE de.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887)
GROUP BY c.concept_name, c.concept_class_id
ORDER BY num DESC;
/*
OLD DATA:

|nsaid_drug                                                                                          |concept_class_id    |num                 |
|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|1 ML Ketorolac Tromethamine 30 MG/ML Injection                                                      |Quant Clinical Drug |3890                |
|Ibuprofen 20 MG/ML Oral Suspension                                                                  |Clinical Drug       |2463                |
|Ibuprofen 600 MG Oral Tablet                                                                        |Clinical Drug       |2361                |
|Ibuprofen 800 MG Oral Tablet                                                                        |Clinical Drug       |1389                |
|Ibuprofen 400 MG Oral Tablet                                                                        |Clinical Drug       |748                 |
|Ibuprofen 200 MG Oral Tablet                                                                        |Clinical Drug       |526                 |
|2 ML Ketorolac Tromethamine 30 MG/ML Injection                                                      |Quant Clinical Drug |179                 |
|Naproxen 250 MG Oral Tablet                                                                         |Clinical Drug       |95                  |
|Naproxen 500 MG Oral Tablet                                                                         |Clinical Drug       |67                  |
|celecoxib 100 MG Oral Capsule                                                                       |Clinical Drug       |46                  |
|Ketorolac Tromethamine 10 MG Oral Tablet                                                            |Clinical Drug       |38                  |
|Indomethacin 50 MG Oral Capsule                                                                     |Clinical Drug       |20                  |
|Naproxen 500 MG Delayed Release Oral Tablet                                                         |Clinical Drug       |14                  |
|Indomethacin 50 MG Rectal Suppository                                                               |Clinical Drug       |14                  |
|Sulindac 200 MG Oral Tablet                                                                         |Clinical Drug       |12                  |
|2 ML Ibuprofen 10 MG/ML Injection                                                                   |Quant Clinical Drug |10                  |
|Sulindac 150 MG Oral Tablet                                                                         |Clinical Drug       |9                   |
|Indomethacin 25 MG Oral Capsule                                                                     |Clinical Drug       |8                   |
|Diclofenac Potassium 50 MG Oral Tablet                                                              |Clinical Drug       |4                   |
|Diclofenac Sodium 50 MG Delayed Release Oral Tablet                                                 |Clinical Drug       |2                   |
|Naproxen 25 MG/ML Oral Suspension                                                                   |Clinical Drug       |1                   |
|nabumetone 500 MG Oral Tablet                                                                       |Clinical Drug       |1                   |
|Piroxicam 20 MG Oral Capsule                                                                        |Clinical Drug       |1                   |
|Indomethacin 75 MG Extended Release Oral Capsule                                                    |Clinical Drug       |1                   |
*/

SELECT c.concept_name AS nsaid_drug, c.concept_class_id, COUNT(DISTINCT de.drug_exposure_id) AS num
FROM banner_2019_inpatient.drug_exposure de
INNER JOIN banner_2019_inpatient.concept c ON c.concept_id = de.drug_concept_id 
WHERE de.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887)
GROUP BY c.concept_name, c.concept_class_id
ORDER BY num DESC;
/*
2019:

|nsaid_drug                                                                                          |concept_class_id    |num                 |
|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|Ketorolac Tromethamine 30 MG/ML Injectable Solution                                                 |Clinical Drug       |3343                |
|Ibuprofen 600 MG Oral Tablet                                                                        |Clinical Drug       |1286                |
|Ibuprofen 20 MG/ML Oral Suspension                                                                  |Clinical Drug       |738                 |
|Ibuprofen 400 MG Oral Tablet                                                                        |Clinical Drug       |506                 |
|Ibuprofen 800 MG Oral Tablet                                                                        |Clinical Drug       |273                 |
|Ketorolac Tromethamine 10 MG Oral Tablet                                                            |Clinical Drug       |120                 |
|Ibuprofen 200 MG Oral Tablet                                                                        |Clinical Drug       |102                 |
|2 ML Ibuprofen 10 MG/ML Injection                                                                   |Quant Clinical Drug |76                  |
|celecoxib 100 MG Oral Capsule                                                                       |Clinical Drug       |48                  |
|Naproxen 250 MG Oral Tablet                                                                         |Clinical Drug       |41                  |
|Indomethacin 25 MG Oral Capsule                                                                     |Clinical Drug       |33                  |
|Indomethacin 50 MG Oral Capsule                                                                     |Clinical Drug       |27                  |
|Indomethacin 50 MG Rectal Suppository                                                               |Clinical Drug       |7                   |
|Naproxen 25 MG/ML Oral Suspension                                                                   |Clinical Drug       |5                   |
|Indomethacin 5 MG/ML Oral Suspension                                                                |Clinical Drug       |4                   |
|Sulindac 150 MG Oral Tablet                                                                         |Clinical Drug       |3                   |
|Sulindac 200 MG Oral Tablet                                                                         |Clinical Drug       |2                   |
|Diclofenac Sodium 75 MG Delayed Release Oral Tablet                                                 |Clinical Drug       |2                   |
|nabumetone 750 MG Oral Tablet                                                                       |Clinical Drug       |1                   |
|meloxicam 15 MG Oral Tablet                                                                         |Clinical Drug       |1                   |
|Indomethacin 75 MG Extended Release Oral Capsule                                                    |Clinical Drug       |1                   |
*/

-- Finding NSAIDs based on the ingredient concept set
-- 6619 NSAID banner inpatient exposures from the plain concept set
-- 6624 when using ingredient concept set after joining with drug_strength
SELECT COUNT(DISTINCT de.drug_exposure_id)
FROM banner_2019_inpatient.drug_exposure de 
INNER JOIN banner_2019_inpatient.drug_strength ds
ON de.drug_concept_id = ds.drug_concept_id 
WHERE ds.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5853);

SELECT c.concept_name AS nsaid_ingr, COUNT(DISTINCT de.drug_exposure_id) AS num
FROM banner_etl.drug_exposure de 
INNER JOIN banner_etl.drug_strength ds
ON de.drug_concept_id = ds.drug_concept_id 
INNER JOIN banner_etl.concept c
ON c.concept_id = ds.ingredient_concept_id 
WHERE ds.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5853)
AND de.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887)
GROUP BY c.concept_name
ORDER BY num DESC;
/*
OLD DATA:

|nsaid_ingr                                                                                          |num                 |
|----------------------------------------------------------------------------------------------------|--------------------|
|Ibuprofen                                                                                           |7497                |
|Ketorolac                                                                                           |4107                |
|Naproxen                                                                                            |177                 |
|celecoxib                                                                                           |46                  |
|Indomethacin                                                                                        |43                  |
|Sulindac                                                                                            |21                  |
|Diclofenac                                                                                          |6                   |
|nabumetone                                                                                          |1                   |
|Piroxicam                                                                                           |1                   |
*/

SELECT c.concept_name AS nsaid_ingr, COUNT(DISTINCT de.drug_exposure_id) AS num
FROM banner_2019_inpatient.drug_exposure de 
INNER JOIN banner_2019_inpatient.drug_strength ds
ON de.drug_concept_id = ds.drug_concept_id 
INNER JOIN banner_2019_inpatient.concept c
ON c.concept_id = ds.ingredient_concept_id 
WHERE ds.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5853)
AND de.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 5887)
GROUP BY c.concept_name
ORDER BY num DESC;
/*
2019:

|nsaid_ingr                                                                                          |num                 |
|----------------------------------------------------------------------------------------------------|--------------------|
|Ketorolac                                                                                           |3463                |
|Ibuprofen                                                                                           |2981                |
|Indomethacin                                                                                        |72                  |
|celecoxib                                                                                           |48                  |
|Naproxen                                                                                            |46                  |
|Sulindac                                                                                            |5                   |
|Diclofenac                                                                                          |2                   |
|meloxicam                                                                                           |1                   |
|nabumetone                                                                                          |1                   |
*/
