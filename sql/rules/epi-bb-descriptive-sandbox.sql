SET search_path TO banner_etl;

SELECT de1.person_id, de1.drug_exposure_id AS epi_dexp, de1.drug_concept_id AS epi_id, c1.concept_name AS epi_name, cs1.concept_name AS epi_ingr, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_exposure_id AS bb_dexp, de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, cs2.concept_name AS bb_ingr, de2.drug_exposure_start_datetime AS bb_start, de2.drug_exposure_end_datetime AS bb_end 
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- bb
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7584) -- epinephrines ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7756) -- beta-blockers ingredients
AND (de2.drug_exposure_start_datetime < de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime)
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY de1.drug_exposure_start_datetime ASC;
-- basic concomitant = 178 rows

--Query ID: EPI-BB-Demo
-- person table demographics. counted by distinct persons.
WITH epi_bb AS (
SELECT de1.person_id, de1.drug_concept_id AS epi_id, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_concept_id AS bb_id, de2.drug_exposure_start_datetime AS bb_start, de2.drug_exposure_end_datetime AS bb_end, 
o.observation_period_start_date AS obs_start, o.observation_period_end_date AS obs_end
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
INNER JOIN observation_period o
ON o.person_id = de1.person_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND (de2.drug_exposure_start_datetime < de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime)
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY de1.drug_exposure_start_datetime ASC
) SELECT gender_source_value, count(DISTINCT p.person_id) AS freq FROM epi_bb
INNER JOIN person p ON p.person_id = epi_bb.person_id 
GROUP BY gender_source_value;

--Query ID: EPI-BB-Age
-- average age
WITH epi_bb_ages AS (
SELECT 
EXTRACT(YEAR from de2.drug_exposure_start_datetime) - p.year_of_birth AS age, --bb should be first so use earliest one for each person
RANK() OVER( 
PARTITION BY p.person_id
ORDER BY EXTRACT(YEAR from de2.drug_exposure_start_datetime) - p.year_of_birth ASC
) agerank
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
INNER JOIN observation_period o
ON o.person_id = de1.person_id
INNER JOIN person p 
ON p.person_id = de1.person_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND (de2.drug_exposure_start_datetime < de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime)
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
) 
SELECT avg(age) FROM epi_bb_ages
WHERE agerank = 1; -- ages ranked over persons. get age at first exposure 

--Query ID: BB-Prod
-- bb frequencies - counted by drug co-exposures, not persons.
-- by bb product
WITH epi_bb AS (
SELECT de1.person_id, de1.drug_exposure_id AS epi_dexp, de1.drug_concept_id AS epi_id, c1.concept_name AS epi_name, cs1.concept_name AS epi_ingr, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_exposure_id AS bb_dexp, de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, cs2.concept_name AS bb_ingr, de2.drug_exposure_start_datetime AS bb_start, de2.drug_exposure_end_datetime AS bb_end, 
o.observation_period_start_date AS obs_start, o.observation_period_end_date AS obs_end
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- bb
INNER JOIN observation_period o ON o.person_id = de1.person_id
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7584) -- epinephrines ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7756) -- beta-blockers ingredients
AND (de2.drug_exposure_start_datetime < de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime)
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
) SELECT bb_ingr, 
COUNT(DISTINCT bb_dexp) AS freq_dexp,  
COUNT(DISTINCT person_id) AS freq_person
FROM epi_bb
GROUP BY bb_ingr
ORDER BY freq_dexp DESC
;

--Query ID: BB-Route
-- route of administration bb
-- curently using route concept id
WITH epi_bb AS (
SELECT de1.person_id, de1.drug_exposure_id AS epi_dexp, de1.drug_concept_id AS epi_id, c1.concept_name AS epi_name, cs1.concept_name AS epi_ingr, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_exposure_id AS bb_dexp, de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, cs2.concept_name AS bb_ingr, de2.drug_exposure_start_datetime AS bb_start, de2.drug_exposure_end_datetime AS bb_end, 
o.observation_period_start_date AS obs_start, o.observation_period_end_date AS obs_end, de2.route_concept_id AS bb_route_id, de2.route_source_value AS bb_route
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- bb
INNER JOIN observation_period o ON o.person_id = de1.person_id
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)--(1344242,1344243,1344245,19076899,19127146,40225943,40225960,40225973,40226817,40226820,40226823,40226826,40226837,40226840,40240717,42799042,42799043,43526160,45892245,45892250,45892254,45892487,46275414,46275916,46275966,46275975,46276169)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)--(902429,902430,902431,902452,902489,974429,974430,974431,991420,1314006,1314008,1314009,1314043,1314044,1314162,1314581,1314614,1318912,1345861,1345882,1346990,1395059,1395060,1395092,4056753,19003680,19003734,19011442,19011443,19016929,19018811,19019231,19019232,19019236,19019238,19019239,19019662,19019663,19022749,19022750,19022781,19062037,19064962,19074186,19074187,19078139,19079774,19079775,19079776,19101573,19101994,19103832,19103844,19106242,19107301,19107317,19107534,19107535,19107536,19108129,19109018,19125472,19125473,19132825,19133212,40162431,40162434,40162436,40162438,40162864,40162867,40162871,40162875,40162878,40163112,40163115,40163307,40163312,40163328,40163333,40163340,40163342,40163350,40163354,40163361,40163948,40163950,40163952,40163954,40164138,40166187,40166188,40166190,40166192,40166817,40166819,40166821,40166824,40166826,40166828,40166830,40167087,40167090,40167091,40167094,40167118,40167121,40167124,40167196,40167200,40167202,40167207,40167211,40167213,40167216,40167218,40169683,40169689,40169695,40169704,40169706,40169712,40173967,40173971,40173977,40173979,40173987,40173997,40177818,40179060,40179064,40179070,40182870,40182874,40182878,40242771,40242777,40243550,42707472,42800545,42800549,44816219,45775831,46221722,46221724)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7584) -- epinephrines ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7756) -- beta-blockers ingredients
AND (de2.drug_exposure_start_datetime < de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime)
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
) SELECT bb_route_id, bb_route,
COUNT(DISTINCT bb_dexp) AS freq_dexp,
COUNT(DISTINCT person_id) AS freq_person
FROM epi_bb
GROUP BY bb_route_id, bb_route
ORDER BY freq_dexp DESC
;

--Query ID: EPI-Prod
-- epi frequencies
WITH epi_bb AS (
SELECT de1.person_id, de1.drug_exposure_id AS epi_dexp, de1.drug_concept_id AS epi_id, c1.concept_name AS epi_name, cs1.concept_name AS epi_ingr, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_exposure_id AS bb_dexp, de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, cs2.concept_name AS bb_ingr, de2.drug_exposure_start_datetime AS bb_start, de2.drug_exposure_end_datetime AS bb_end, 
o.observation_period_start_date AS obs_start, o.observation_period_end_date AS obs_end
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- bb
INNER JOIN observation_period o ON o.person_id = de1.person_id
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7584) -- epinephrines ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7756) -- beta-blockers ingredients
AND (de2.drug_exposure_start_datetime < de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime)
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
) SELECT epi_ingr,
COUNT(DISTINCT epi_dexp) AS freq_dexp,
COUNT(DISTINCT person_id) AS freq_person
FROM epi_bb
GROUP BY epi_ingr
ORDER BY freq_dexp DESC
;

--Query ID: EPI_Route
-- epi routes
WITH epi_bb AS (
SELECT de1.person_id, de1.drug_exposure_id AS epi_dexp, de1.drug_concept_id AS epi_id, c1.concept_name AS epi_name, cs1.concept_name AS epi_ingr, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end, de1.route_concept_id AS epi_route_id, de1.route_source_value AS epi_route,
de2.drug_exposure_id AS bb_dexp, de2.drug_concept_id AS bb_id, c2.concept_name AS bb_name, cs2.concept_name AS bb_ingr, de2.drug_exposure_start_datetime AS bb_start, de2.drug_exposure_end_datetime AS bb_end, 
o.observation_period_start_date AS obs_start, o.observation_period_end_date AS obs_end
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 ON de1.person_id = de2.person_id -- bb
INNER JOIN observation_period o ON o.person_id = de1.person_id
INNER JOIN concept c1 ON de1.drug_concept_id = c1.concept_id
INNER JOIN concept c2 ON de2.drug_concept_id = c2.concept_id
INNER JOIN drug_strength ds1 ON ds1.drug_concept_id = de1.drug_concept_id
INNER JOIN concept cs1 ON ds1.ingredient_concept_id = cs1.concept_id
INNER JOIN drug_strength ds2 ON ds2.drug_concept_id = de2.drug_concept_id
INNER JOIN concept cs2 ON ds2.ingredient_concept_id = cs2.concept_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND ds1.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7584) -- epinephrines ingredients
AND ds2.ingredient_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7756) -- beta-blockers ingredients
AND (de2.drug_exposure_start_datetime < de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime)
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
) SELECT epi_route_id, epi_route,
COUNT(DISTINCT epi_dexp) AS freq_dexp,
COUNT(DISTINCT person_id) AS freq_person
FROM epi_bb
GROUP BY epi_route_id, epi_route
ORDER BY freq_dexp DESC
;

-- plastic surgery concept ID's
select * from concept where concept_code IN ('Z41.1', 'Z42');	
select * from concept where concept_name ilike '%plastic surgery%' and vocabulary_id ilike '%icd9%';
select * from concept where concept_code IN ('V50.1', 'V51', 'V51.0', 'V51.8') and vocabulary_id = 'ICD9CM';
-- procedure concept id 44831963
-- observation concept id 44828555, 44822708, 44833113
select * from concept where concept_code IN ('85.70','85.71','85.72','85.73','85.74','85.75', '85.76','85.79');
-- procedure concept id 2006453,2006454,2006455,2006456,2006457,2006458,2006459,2006460
select * from condition_occurrence where condition_concept_id = 44831963;
select * from condition_occurrence 
where condition_concept_id in (44831963, 44828555, 44822708, 44833113, 2006453,2006454,2006455,2006456,2006457,2006458,2006459,2006460)
AND person_id IN (1717188169,754886905,1048049774,2027275242,648162762,1908910285,259083625,602303101,641969996,1970441934,570865286,527464247,705934921,624409145,509358601,850723734,878337985,960247475,1685420646,70739676,2029566824,1952394599,2032036108,1501364224,2025811877,1493577601,2013804729,2030883388,1080543256,2033231830,2031167709,357258330,2033015200,829571957,1725135658,1955424484,256851208,1402546733,1992431617,518197642,2031531459,196433365,1190954173,708704633,92230467,2035437586,637411327,933914894,1444345323,2032295352,825603677,1763407201,1467635874,187793271,1918758283,635402674,2038088845,637605863,2035281637,2038678374,1371456400,2002426558,413411218,603258729,833333955,1486704831,1731134782,2038875437,2039500580,314659961,1331480549,839209509,1157768228,334375445,2012117214,1580165900,2007076677,1292044690,1369752891,1999788906,1129966731,1039576644,2039555547,2031229656,2042217635);
;

SELECT de1.person_id, de1.drug_concept_id AS epi_id, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_concept_id AS bb_id, de2.drug_exposure_start_datetime AS bb_start, de2.drug_exposure_end_datetime AS bb_end,
co.*
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
INNER JOIN condition_occurrence co
ON co.person_id = de1.person_id
INNER JOIN observation_period o
ON o.person_id = de1.person_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND (de2.drug_exposure_start_datetime < de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime)
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
AND co.condition_concept_id IN (44831963, 44828555, 44822708, 44833113, 2006453,2006454,2006455,2006456,2006457,2006458,2006459,2006460)
ORDER BY de1.drug_exposure_start_datetime ASC
;

-- "Anaphylactic shock, unspecified" concept ID's
select * from concept where concept_code IN ('T78.0x', 'T78.2');
select * from concept where concept_code IN ('V13.8', 'V13.89');
-- observation concept id's 44827318, 44820387
select * from concept where concept_name ilike '%anaphylact%' and vocabulary_id ilike '%icd9%' order by concept_code asc;
-- condition concept id's 44821406,44821407,44823658,44824839,44826025,44827179,44828343,44829475,44829488,44831787,44832926,44834121,44834126,44835303,44836484,44837633
select * from condition_occurrence where condition_concept_id in (44821406,44821407,44823658,44824839,44826025,44827179,44828343,44829475,44829488,44831787,44832926,44834121,44834126,44835303,44836484,44837633);
select * from condition_era 
where condition_concept_id in (44821406,44821407,44823658,44824839,44826025,44827179,44828343,44829475,44829488,44831787,44832926,44834121,44834126,44835303,44836484,44837633)
order by condition_era_start_date;

select * from condition_occurrence where condition_concept_id in (44827318, 44820387);
select * from condition_occurrence 
where condition_concept_id in (44827318, 44820387, 44821406,44821407,44823658,44824839,44826025,44827179,44828343,44829475,44829488,44831787,44832926,44834121,44834126,44835303,44836484,44837633)
AND person_id IN (1717188169,754886905,1048049774,2027275242,648162762,1908910285,259083625,602303101,641969996,1970441934,570865286,527464247,705934921,624409145,509358601,850723734,878337985,960247475,1685420646,70739676,2029566824,1952394599,2032036108,1501364224,2025811877,1493577601,2013804729,2030883388,1080543256,2033231830,2031167709,357258330,2033015200,829571957,1725135658,1955424484,256851208,1402546733,1992431617,518197642,2031531459,196433365,1190954173,708704633,92230467,2035437586,637411327,933914894,1444345323,2032295352,825603677,1763407201,1467635874,187793271,1918758283,635402674,2038088845,637605863,2035281637,2038678374,1371456400,2002426558,413411218,603258729,833333955,1486704831,1731134782,2038875437,2039500580,314659961,1331480549,839209509,1157768228,334375445,2012117214,1580165900,2007076677,1292044690,1369752891,1999788906,1129966731,1039576644,2039555547,2031229656,2042217635);

SELECT de1.person_id, de1.drug_concept_id AS epi_id, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_concept_id AS bb_id, de2.drug_exposure_start_datetime AS bb_start, de2.drug_exposure_end_datetime AS bb_end,
co.*
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
INNER JOIN condition_occurrence co
ON co.person_id = de1.person_id
INNER JOIN observation_period o
ON o.person_id = de1.person_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND (de2.drug_exposure_start_datetime < de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime)
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
AND co.condition_concept_id IN (44827318, 44820387, 44821406,44821407,44823658,44824839,44826025,44827179,44828343,44829475,44829488,44831787,44832926,44834121,44834126,44835303,44836484,44837633)
ORDER BY de1.drug_exposure_start_datetime ASC
;

-- multiple stays
SELECT o.person_id, COUNT(DISTINCT observation_period_id) AS obsCount
FROM observation_period o
GROUP BY o.person_id
HAVING COUNT(DISTINCT observation_period_id) > 1;
-- no patient has >1 observation period

SELECT de1.person_id, de1.drug_concept_id AS epi_id, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_concept_id AS bb_id, de2.drug_exposure_start_datetime AS bb_start, de2.drug_exposure_end_datetime AS bb_end,
o.*
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
LEFT JOIN observation_period o
ON o.person_id = de1.person_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
AND (de2.drug_exposure_start_datetime < de1.drug_exposure_start_datetime AND de2.drug_exposure_end_datetime >= de1.drug_exposure_start_datetime)
AND (de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date)
AND (('2016-01-01' BETWEEN o.observation_period_start_date AND o.observation_period_end_date)
OR ('2016-04-30' BETWEEN o.observation_period_start_date AND o.observation_period_end_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
AND (((de2.drug_exposure_start_date <= o.observation_period_end_date AND de2.drug_exposure_start_date >= o.observation_period_start_date)
OR (de2.drug_exposure_end_date <= o.observation_period_end_date AND de2.drug_exposure_end_date >= o.observation_period_start_date))
AND ((de1.drug_exposure_start_date <= o.observation_period_end_date AND de1.drug_exposure_start_date >= o.observation_period_start_date)
OR (de1.drug_exposure_end_date <= o.observation_period_end_date AND de1.drug_exposure_end_date >= o.observation_period_start_date)))
ORDER BY de1.drug_exposure_start_datetime ASC
;