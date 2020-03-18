set search_path to banner_etl;

select * from ohdsi.concept_set where concept_set_name ilike '%clonidine%'; -- clonidine 11501; clonidines ingredients 11533
select * from ohdsi.concept_set where concept_set_name ilike '%beta-blocker%'; -- bb 7773; bb ingredients 7756; non-selective bb 7871; non-selective bb ingredients 7912 
select * from ohdsi.concept_set where concept_set_name ilike '%timolol%'; -- eye drops 9162; oral (don't think this is a complete set) 12003; ingredient 9160

select * from ohdsi.concept_set_item cs
inner join concept c on c.concept_id = cs.concept_id
where concept_set_id = 9160;

SELECT de1.person_id, de1.drug_exposure_id AS clon_dexp, de1.drug_concept_id AS clon_id, c1.concept_name AS clon_name, cs1.concept_name AS clon_ingr, de1.drug_exposure_start_datetime AS clon_start, de1.drug_exposure_end_datetime AS clon_end, de1.quantity, de1.sig, de1.route_concept_id as clon_route_id, de1.route_source_value as clon_route,
de2.drug_exposure_id AS bb_dexp, de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, cs2.concept_name AS bb_ingr, de2.drug_exposure_start_datetime AS beta_start, de2.drug_exposure_end_datetime AS beta_end, de2.quantity, de2.sig, de2.route_concept_id AS bb_route_id, de2.route_source_value AS bb_route
FROM drug_exposure de1 -- clonidine
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- bb
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11501)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11533) -- clonidines ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7756) -- beta-blockers ingredients
AND ((de2.drug_exposure_start_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de2.drug_exposure_end_datetime BETWEEN de1.drug_exposure_start_datetime AND de1.drug_exposure_end_datetime)
  OR (de1.drug_exposure_start_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime)
  OR (de1.drug_exposure_end_datetime BETWEEN de2.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY person_id ASC;
--expect 147 in query.

--Query ID: BB-CLON-Demo
-- person gender demographics
WITH c_bb  AS(
SELECT de1.person_id, de1.drug_concept_id AS clon_id, c1.concept_name AS clon_name, de1.drug_exposure_start_datetime AS clon_start, de1.drug_exposure_end_datetime AS clon_end, de1.quantity, de1.sig,
de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, de2.drug_exposure_start_datetime AS beta_start, de2.drug_exposure_end_datetime AS beta_end, de2.quantity, de2.sig,
o.observation_period_start_date AS obs_start, o.observation_period_end_date AS obs_end
FROM drug_exposure de1 -- clonidine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
INNER JOIN observation_period o
ON o.person_id = de1.person_id
INNER JOIN concept c1
ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2
ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1
ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN drug_strength ds2
ON ds2.drug_concept_id = de2.drug_concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11501)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND ((de2.drug_exposure_start_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_start_datetime <= de1.drug_exposure_end_datetime)
OR (de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime <= de1.drug_exposure_end_date))
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
) SELECT p.gender_source_value, count(DISTINCT p.person_id) AS freq 
FROM c_bb
INNER JOIN person p ON p.person_id = c_bb.person_id
GROUP BY p.gender_source_value
ORDER BY freq ASC;

--Query ID: BB-CLON-Age
-- average age
/*
NOTE: if we want more exact age using the snippet below might help, but the output is in number of days.
CONCAT(p.year_of_birth,'-',p.month_of_birth,'-',p.day_of_birth) AS birth_string,
de2.drug_exposure_start_datetime,
de2.drug_exposure_start_datetime - TO_DATE(CONCAT(p.year_of_birth,'-',p.month_of_birth,'-',p.day_of_birth),'YYYYMMDD') AS age, --bb should be first so use earliest one for each person
*/
WITH c_bb_ages AS (
SELECT 
EXTRACT(YEAR from de2.drug_exposure_start_datetime) - p.year_of_birth AS age, --bb should be first so use earliest one for each person
RANK() OVER( 
PARTITION BY p.person_id
ORDER BY EXTRACT(YEAR from de2.drug_exposure_start_datetime) - p.year_of_birth ASC
) agerank
FROM drug_exposure de1 -- clonidine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
INNER JOIN observation_period o
ON o.person_id = de1.person_id
INNER JOIN person p 
ON p.person_id = de1.person_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11501)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND ((de2.drug_exposure_start_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_start_datetime <= de1.drug_exposure_end_datetime)
OR (de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime <= de1.drug_exposure_end_date))
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
) 
SELECT avg(age) FROM c_bb_ages
WHERE agerank = 1; -- ages ranked over persons. get age at first exposure 

--Query ID: BB-Prod
--bb frequency by product
WITH c_bb  AS(
SELECT de1.person_id, de1.drug_exposure_id AS clon_dexp, de1.drug_concept_id AS clon_id, c1.concept_name AS clon_name, cs1.concept_name AS clon_ingr, de1.drug_exposure_start_datetime AS clon_start, de1.drug_exposure_end_datetime AS clon_end, de1.quantity, de1.sig, de1.route_concept_id as clon_route_id, de1.route_source_value as clon_route,
de2.drug_exposure_id AS bb_dexp, de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, cs2.concept_name AS bb_ingr, de2.drug_exposure_start_datetime AS beta_start, de2.drug_exposure_end_datetime AS beta_end, de2.quantity, de2.sig, de2.route_concept_id AS bb_route_id, de2.route_source_value AS bb_route,
o.observation_period_start_date AS obs_start, o.observation_period_end_date AS obs_end
FROM drug_exposure de1 -- clonidine
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- bb
INNER JOIN observation_period o ON o.person_id = de1.person_id
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11501)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11533) -- clonidines ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7756) -- beta-blockers ingredients
AND ((de2.drug_exposure_start_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_start_datetime <= de1.drug_exposure_end_datetime)
OR (de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime <= de1.drug_exposure_end_date))
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
) SELECT bb_ingr,
count(DISTINCT bb_dexp) AS freq_dexp,
count(DISTINCT person_id) AS freq_person
FROM c_bb
GROUP BY bb_ingr
ORDER BY freq_dexp DESC;

--Query ID: CLON-Route
--clonidine frequency by route
WITH c_bb  AS(
SELECT de1.person_id, de1.drug_exposure_id AS clon_dexp, de1.drug_concept_id AS clon_id, c1.concept_name AS clon_name, cs1.concept_name AS clon_ingr, de1.drug_exposure_start_datetime AS clon_start, de1.drug_exposure_end_datetime AS clon_end, de1.quantity, de1.sig, de1.route_concept_id as clon_route_id, de1.route_source_value as clon_route,
de2.drug_exposure_id AS bb_dexp, de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, cs2.concept_name AS bb_ingr, de2.drug_exposure_start_datetime AS beta_start, de2.drug_exposure_end_datetime AS beta_end, de2.quantity, de2.sig, de2.route_concept_id AS bb_route_id, de2.route_source_value AS bb_route,
o.observation_period_start_date AS obs_start, o.observation_period_end_date AS obs_end
FROM drug_exposure de1 -- clonidine
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- bb
INNER JOIN observation_period o ON o.person_id = de1.person_id
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11501)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11533) -- clonidines ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7756) -- beta-blockers ingredients
AND ((de2.drug_exposure_start_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_start_datetime <= de1.drug_exposure_end_datetime)
OR (de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime <= de1.drug_exposure_end_date))
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
) SELECT clon_route_id, clon_route,
count(DISTINCT clon_dexp) AS freq_dexp,
count(DISTINCT person_id) as freq_person
FROM c_bb
GROUP BY clon_route_id, clon_route
ORDER BY freq_dexp DESC;

--Query ID: BB-Route
-- beta-blockers by route
WITH c_bb  AS(
SELECT de1.person_id, de1.drug_exposure_id AS clon_dexp, de1.drug_concept_id AS clon_id, c1.concept_name AS clon_name, cs1.concept_name AS clon_ingr, de1.drug_exposure_start_datetime AS clon_start, de1.drug_exposure_end_datetime AS clon_end, de1.quantity, de1.sig, de1.route_concept_id as clon_route_id, de1.route_source_value as clon_route,
de2.drug_exposure_id AS bb_dexp, de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, cs2.concept_name AS bb_ingr, de2.drug_exposure_start_datetime AS beta_start, de2.drug_exposure_end_datetime AS beta_end, de2.quantity, de2.sig, de2.route_concept_id AS bb_route_id, de2.route_source_value AS bb_route,
o.observation_period_start_date AS obs_start, o.observation_period_end_date AS obs_end
FROM drug_exposure de1 -- clonidine
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- bb
INNER JOIN observation_period o ON o.person_id = de1.person_id
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11501)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11533) -- clonidines ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7756) -- beta-blockers ingredients
AND ((de2.drug_exposure_start_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_start_datetime <= de1.drug_exposure_end_datetime)
OR (de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime <= de1.drug_exposure_end_date))
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
) SELECT bb_route_id, bb_route,
count(DISTINCT bb_dexp) AS freq_dexp,
count(DISTINCT person_id) AS freq_person
FROM c_bb
GROUP BY bb_route_id, bb_route
ORDER BY freq_dexp DESC;

--Query ID: BB-CLON-Timolols
-- timolols
-- Timolols concept set 12108
-- Timolols Ingredient 9160
SELECT de1.person_id, de1.drug_exposure_id AS clon_dexp, de1.drug_concept_id AS clon_id, c1.concept_name AS clon_name, cs1.concept_name AS clon_ingr, de1.drug_exposure_start_datetime AS clon_start, de1.drug_exposure_end_datetime AS clon_end, de1.quantity, de1.sig, de1.route_concept_id as clon_route_id, de1.route_source_value as clon_route,
de2.drug_exposure_id AS bb_dexp, de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, cs2.concept_name AS bb_ingr, de2.drug_exposure_start_datetime AS beta_start, de2.drug_exposure_end_datetime AS beta_end, de2.quantity, de2.sig, de2.route_concept_id AS bb_route_id, de2.route_source_value AS bb_route,
o.observation_period_start_date AS obs_start, o.observation_period_end_date AS obs_end
FROM drug_exposure de1 -- clonidine
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- bb
INNER JOIN observation_period o ON o.person_id = de1.person_id
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11501)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 12108) -- timolols
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 11533) -- clonidines ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 9160) -- timolols ingredients
AND ((de2.drug_exposure_start_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_start_datetime <= de1.drug_exposure_end_datetime)
OR (de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime <= de1.drug_exposure_end_date))
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id;
/*
person ID's 1591389277, 93384315
40168654;"Clonidine Hydrochloride 0.1 MG Oral Tablet";"2016-02-20 21:00:00";"2016-02-22 18:49:20"
40168659;"10 ML Clonidine Hydrochloride 0.1 MG/ML Injection";"2016-01-05 09:19:00";"2016-01-05 09:53:11"

1594709;"Timolol 5 MG/ML Ophthalmic Solution";"2016-02-21 09:30:00";"2016-02-22 18:49:20"
1594707;"Timolol 2.5 MG/ML Ophthalmic Solution";"2016-01-05 11:30:00";"2016-01-08 18:39:14"
*/
