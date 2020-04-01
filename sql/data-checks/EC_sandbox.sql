set search_path to banner_etl;
set search_path to public;

-- just epinephrine
SELECT de1.person_id, de1.drug_concept_id AS epi, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end
FROM drug_exposure de1
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)--(1344242,1344243,1344245,19076899,19127146,40225943,40225960,40225973,40226817,40226820,40226823,40226826,40226837,40226840,40240717,42799042,42799043,43526160,45892245,45892250,45892254,45892487,46275414,46275916,46275966,46275975,46276169)
;
-- just bb
SELECT de2.person_id, de2.drug_concept_id AS beta, de2.drug_exposure_start_datetime AS beta_start, de2.drug_exposure_end_datetime AS beta_end
FROM drug_exposure de2
WHERE de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)
;

-- concomitant epinephrine & beta blockers
-- SELECT de2.*, de1.*
SELECT de1.person_id, de1.drug_concept_id AS epi, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_concept_id AS beta, de2.drug_exposure_start_datetime AS beta_start, de2.drug_exposure_end_datetime AS beta_end
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)--(1344242,1344243,1344245,19076899,19127146,40225943,40225960,40225973,40226817,40226820,40226823,40226826,40226837,40226840,40240717,42799042,42799043,43526160,45892245,45892250,45892254,45892487,46275414,46275916,46275966,46275975,46276169)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)--(902429,902430,902431,902452,902489,974429,974430,974431,991420,1314006,1314008,1314009,1314043,1314044,1314162,1314581,1314614,1318912,1345861,1345882,1346990,1395059,1395060,1395092,4056753,19003680,19003734,19011442,19011443,19016929,19018811,19019231,19019232,19019236,19019238,19019239,19019662,19019663,19022749,19022750,19022781,19062037,19064962,19074186,19074187,19078139,19079774,19079775,19079776,19101573,19101994,19103832,19103844,19106242,19107301,19107317,19107534,19107535,19107536,19108129,19109018,19125472,19125473,19132825,19133212,40162431,40162434,40162436,40162438,40162864,40162867,40162871,40162875,40162878,40163112,40163115,40163307,40163312,40163328,40163333,40163340,40163342,40163350,40163354,40163361,40163948,40163950,40163952,40163954,40164138,40166187,40166188,40166190,40166192,40166817,40166819,40166821,40166824,40166826,40166828,40166830,40167087,40167090,40167091,40167094,40167118,40167121,40167124,40167196,40167200,40167202,40167207,40167211,40167213,40167216,40167218,40169683,40169689,40169695,40169704,40169706,40169712,40173967,40173971,40173977,40173979,40173987,40173997,40177818,40179060,40179064,40179070,40182870,40182874,40182878,40242771,40242777,40243550,42707472,42800545,42800549,44816219,45775831,46221722,46221724)
AND ((de2.drug_exposure_start_date <= de1.drug_exposure_end_date AND de2.drug_exposure_start_date >= de1.drug_exposure_start_date)
OR (de1.drug_exposure_start_date <= de2.drug_exposure_end_date AND de1.drug_exposure_start_date >= de2.drug_exposure_start_date))
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY de1.drug_exposure_start_datetime ASC
;

-- alternative look at concomitant epi/beta
SELECT de1.person_id, de1.drug_concept_id AS epi, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_concept_id AS beta, de2.drug_exposure_start_datetime AS beta_start, de2.drug_exposure_end_datetime AS beta_end
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)--(1344242,1344243,1344245,19076899,19127146,40225943,40225960,40225973,40226817,40226820,40226823,40226826,40226837,40226840,40240717,42799042,42799043,43526160,45892245,45892250,45892254,45892487,46275414,46275916,46275966,46275975,46276169)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)--(902429,902430,902431,902452,902489,974429,974430,974431,991420,1314006,1314008,1314009,1314043,1314044,1314162,1314581,1314614,1318912,1345861,1345882,1346990,1395059,1395060,1395092,4056753,19003680,19003734,19011442,19011443,19016929,19018811,19019231,19019232,19019236,19019238,19019239,19019662,19019663,19022749,19022750,19022781,19062037,19064962,19074186,19074187,19078139,19079774,19079775,19079776,19101573,19101994,19103832,19103844,19106242,19107301,19107317,19107534,19107535,19107536,19108129,19109018,19125472,19125473,19132825,19133212,40162431,40162434,40162436,40162438,40162864,40162867,40162871,40162875,40162878,40163112,40163115,40163307,40163312,40163328,40163333,40163340,40163342,40163350,40163354,40163361,40163948,40163950,40163952,40163954,40164138,40166187,40166188,40166190,40166192,40166817,40166819,40166821,40166824,40166826,40166828,40166830,40167087,40167090,40167091,40167094,40167118,40167121,40167124,40167196,40167200,40167202,40167207,40167211,40167213,40167216,40167218,40169683,40169689,40169695,40169704,40169706,40169712,40173967,40173971,40173977,40173979,40173987,40173997,40177818,40179060,40179064,40179070,40182870,40182874,40182878,40242771,40242777,40243550,42707472,42800545,42800549,44816219,45775831,46221722,46221724)
AND (de2.drug_exposure_start_date <= de1.drug_exposure_start_date AND de2.drug_exposure_end_date >= de1.drug_exposure_end_date)
AND de1.drug_exposure_id != de2.drug_exposure_id
ORDER BY de1.drug_exposure_start_datetime ASC
;

-- concomitant epinephrine & beta blockers, epi first
-- SELECT de2.*, de1.*
SELECT de1.person_id, de1.drug_concept_id, de1.drug_exposure_start_datetime, de1.drug_exposure_end_datetime, de2.drug_concept_id, de2.drug_exposure_start_datetime, de2.drug_exposure_end_datetime
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)--(1344242,1344243,1344245,19076899,19127146,40225943,40225960,40225973,40226817,40226820,40226823,40226826,40226837,40226840,40240717,42799042,42799043,43526160,45892245,45892250,45892254,45892487,46275414,46275916,46275966,46275975,46276169)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)--(902429,902430,902431,902452,902489,974429,974430,974431,991420,1314006,1314008,1314009,1314043,1314044,1314162,1314581,1314614,1318912,1345861,1345882,1346990,1395059,1395060,1395092,4056753,19003680,19003734,19011442,19011443,19016929,19018811,19019231,19019232,19019236,19019238,19019239,19019662,19019663,19022749,19022750,19022781,19062037,19064962,19074186,19074187,19078139,19079774,19079775,19079776,19101573,19101994,19103832,19103844,19106242,19107301,19107317,19107534,19107535,19107536,19108129,19109018,19125472,19125473,19132825,19133212,40162431,40162434,40162436,40162438,40162864,40162867,40162871,40162875,40162878,40163112,40163115,40163307,40163312,40163328,40163333,40163340,40163342,40163350,40163354,40163361,40163948,40163950,40163952,40163954,40164138,40166187,40166188,40166190,40166192,40166817,40166819,40166821,40166824,40166826,40166828,40166830,40167087,40167090,40167091,40167094,40167118,40167121,40167124,40167196,40167200,40167202,40167207,40167211,40167213,40167216,40167218,40169683,40169689,40169695,40169704,40169706,40169712,40173967,40173971,40173977,40173979,40173987,40173997,40177818,40179060,40179064,40179070,40182870,40182874,40182878,40242771,40242777,40243550,42707472,42800545,42800549,44816219,45775831,46221722,46221724)
AND (de2.drug_exposure_start_date <= de1.drug_exposure_end_date AND de2.drug_exposure_start_date >= de1.drug_exposure_start_date)
ORDER BY de1.drug_exposure_start_datetime ASC
;

-- concomitant epinephrine & beta blockers, bb first
-- SELECT de1.*, de2.*
SELECT de1.person_id, de1.drug_concept_id AS epi, de1.drug_exposure_start_datetime AS epi_start, de1.drug_exposure_end_datetime AS epi_end,
de2.drug_concept_id AS beta, de2.drug_exposure_start_datetime AS beta_start, de2.drug_exposure_end_datetime AS beta_end
FROM drug_exposure de1 -- epinephrine
INNER JOIN drug_exposure de2 -- beta blockers
ON de1.person_id = de2.person_id
WHERE de1.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7728)--(1344242,1344243,1344245,19076899,19127146,40225943,40225960,40225973,40226817,40226820,40226823,40226826,40226837,40226840,40240717,42799042,42799043,43526160,45892245,45892250,45892254,45892487,46275414,46275916,46275966,46275975,46276169)
AND de2.drug_concept_id IN (select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 7773)--(902429,902430,902431,902452,902489,974429,974430,974431,991420,1314006,1314008,1314009,1314043,1314044,1314162,1314581,1314614,1318912,1345861,1345882,1346990,1395059,1395060,1395092,4056753,19003680,19003734,19011442,19011443,19016929,19018811,19019231,19019232,19019236,19019238,19019239,19019662,19019663,19022749,19022750,19022781,19062037,19064962,19074186,19074187,19078139,19079774,19079775,19079776,19101573,19101994,19103832,19103844,19106242,19107301,19107317,19107534,19107535,19107536,19108129,19109018,19125472,19125473,19132825,19133212,40162431,40162434,40162436,40162438,40162864,40162867,40162871,40162875,40162878,40163112,40163115,40163307,40163312,40163328,40163333,40163340,40163342,40163350,40163354,40163361,40163948,40163950,40163952,40163954,40164138,40166187,40166188,40166190,40166192,40166817,40166819,40166821,40166824,40166826,40166828,40166830,40167087,40167090,40167091,40167094,40167118,40167121,40167124,40167196,40167200,40167202,40167207,40167211,40167213,40167216,40167218,40169683,40169689,40169695,40169704,40169706,40169712,40173967,40173971,40173977,40173979,40173987,40173997,40177818,40179060,40179064,40179070,40182870,40182874,40182878,40242771,40242777,40243550,42707472,42800545,42800549,44816219,45775831,46221722,46221724)
AND (de2.drug_exposure_start_date <= de1.drug_exposure_start_date and de2.drug_exposure_end_date >= de1.drug_exposure_start_date )
AND de1.person_id IN (select distinct person_id from drug_era)
ORDER BY de1.drug_exposure_start_datetime ASC
;

-- generic query for finding measurements within a drug_era
SELECT * FROM measurement m 
INNER JOIN drug_era de
ON de.person_id = m.person_id
WHERE m.measurement_date >= de.drug_era_start_date AND m.measurement_date <= de.drug_era_end_date
LIMIT 1000;

-- eGFR rule - this measure to be used instead of outdated creatinine clearance
-- less than 60 should trigger rule
-- SELECT count(distinct person_id) FROM measurement -- 2786
SELECT * FROM measurement 
WHERE measurement_concept_id = 3049187 -- Non-black
AND value_as_number < 60 
AND unit_source_value IN ('mL/min/1.73m2', 'ml/min/1.73sq.m');

-- SELECT count(distinct person_id) FROM measurement -- 2907
SELECT * FROM measurement 
WHERE measurement_concept_id = 3053283 -- black
AND value_as_number < 60 
AND unit_source_value IN ('mL/min/1.73m2', 'ml/min/1.73sq.m');

select distinct unit_concept_id, unit_source_value from measurement where measurement_concept_id = 3049187; -- no unit concept ID's currently coded
select * from concept where concept_code = '48642-3';
select * from concept where concept_id = 4172704; -- greater than operator

-- Atenolol dose > 100 mg/day rule
WITH dailydoses AS (
SELECT de.*, ds.amount_value, ds.numerator_value, ds.denominator_value, sm.expected, sm.min, sm.max, (ds.amount_value * sm.expected) AS sig_daily_dose,
CASE WHEN (ds.amount_value * sm.expected) > 100 THEN 'Acute hypertensive reaction is possible'
ELSE null END AS rule
FROM drug_exposure de
INNER JOIN drug_strength ds ON de.drug_concept_id = ds.drug_concept_id
LEFT JOIN sig_mapping sm ON sm.sig = de.sig
WHERE de.drug_concept_id IN (991420,40177818,19003680,1314009,1314044,1314006,19016929,19107535,19103832,19064962,19018811,1314162,1395092,1395059,19107534,1395060,19101994,1318912,1314043,1314008)
AND de.dose_unit_source_value = 'mg')
SELECT amount_value, COUNT(drug_exposure_id) FROM dailydoses GROUP BY amount_value
;

-- Atenolol and eGFR measurement -- adjusted so that if Atenolol >= 100
SELECT DISTINCT t.person_id FROM (
SELECT de.*, ds.amount_value, ds.numerator_value, ds.denominator_value, sm.expected, sm.min, sm.max, (ds.amount_value * sm.expected) AS sig_daily_dose,
CASE WHEN (ds.amount_value * sm.expected) >= 100 THEN 'Acute hypertensive reaction is possible'
ELSE null END AS atenolol_rule,
m.measurement_id, m.measurement_concept_id, m.measurement_date, m.value_as_number, m.unit_source_value,
CASE WHEN m.value_as_number < 60 AND (ds.amount_value * sm.expected) < 100 THEN 'Acute hypertensive reaction is possible'
ELSE 'unlikely' END AS eGFR_rule
FROM drug_exposure de
INNER JOIN drug_strength ds ON de.drug_concept_id = ds.drug_concept_id
LEFT JOIN sig_mapping sm ON sm.sig = de.sig
INNER JOIN measurement m ON m.person_id = de.person_id
WHERE (de.drug_concept_id IN (991420,40177818,19003680,1314009,1314044,1314006,19016929,19107535,19103832,19064962,19018811,1314162,1395092,1395059,19107534,1395060,19101994,1318912,1314043,1314008)
AND de.dose_unit_source_value = 'mg')
AND ((m.measurement_concept_id = 3053283 OR m.measurement_concept_id = 3049187)
AND m.unit_source_value IN ('mL/min/1.73m2', 'ml/min/1.73sq.m'))
AND (m.measurement_date <= de.drug_exposure_end_date AND m.measurement_date >= de.drug_exposure_start_date)
) AS t
WHERE t.eGFR_rule = 'Acute hypertensive reaction is possible'
ORDER BY t.person_id
;

-- Bisoprolol rule -- if patient on Bisoprolol, then check eGFR < 60. If eGFR >= 60, check Bisoprolol dose > 20
-- looks like there are no alerts for dose > 20
SELECT de.*, ds.amount_value, ds.numerator_value, ds.denominator_value, sm.expected, sm.min, sm.max, (ds.amount_value * sm.expected) AS sig_daily_dose,
CASE WHEN m.value_as_number < 60 THEN 'Acute hypertensive reaction is possible'
ELSE null END AS eGFR_rule,
m.measurement_id, m.measurement_concept_id, m.measurement_date, m.value_as_number, m.unit_source_value,
CASE WHEN ((ds.amount_value * sm.expected) > 20 AND m.value_as_number >=60) THEN 'Acute hypertensive reaction is possible'
ELSE 'unlikely' END AS eGFR_bisoprolol_rule
FROM drug_exposure de
INNER JOIN drug_strength ds ON de.drug_concept_id = ds.drug_concept_id
LEFT JOIN sig_mapping sm ON sm.sig = de.sig
INNER JOIN measurement m ON m.person_id = de.person_id
WHERE (de. drug_concept_id in (19106242,19109018,40166187,40162864,40162867,40162871,40166188,40166190,40162875,40162878,40166192)
AND dose_unit_source_value = 'mg')
AND ((m.measurement_concept_id = 3053283 OR m.measurement_concept_id = 3049187)
AND m.unit_source_value IN ('mL/min/1.73m2', 'ml/min/1.73sq.m'))
AND (m.measurement_date <= de.drug_exposure_end_date AND m.measurement_date >= de.drug_exposure_start_date)
;

-- Betaxolol dose > 10 mg/day rule
-- only one row returns for the betaxolol concept set, the dose unit is just "drop"
SELECT de.*, ds.amount_value, ds.numerator_value, ds.denominator_value, sm.expected, sm.min, sm.max, (ds.amount_value * sm.expected) AS sig_daily_dose,
CASE WHEN (ds.amount_value * sm.expected) > 10 THEN 'Acute hypertensive reaction is possible'
ELSE 'unlikely' END AS rule
FROM drug_exposure de
INNER JOIN drug_strength ds ON de.drug_concept_id = ds.drug_concept_id
LEFT JOIN sig_mapping sm ON sm.sig = de.sig
WHERE de.drug_concept_id in (19132825,19074186,19074187,42800545,42800549);
--AND de.dose_unit_source_value = 'mg';

select * from drug_exposure de WHERE de.drug_concept_id in (19132825,19074186,19074187,42800545,42800549);

-- Esmolol rule: alert if dose >= 300 mcg/kg/min
-- can't find weight data, using 60 kg as a placeholder
SELECT de.*, ds.amount_value, ds.numerator_value, ds.denominator_value, sm.expected, sm.min, sm.max, ((numerator_value / 60 / denominator_value) * sm.expected) AS sig_daily_dose,
CASE WHEN ((numerator_value / 60 / denominator_value) * sm.expected) > 300 THEN 'Acute hypertensive reaction is possible'
ELSE null END AS rule
FROM drug_exposure de
INNER JOIN drug_strength ds ON de.drug_concept_id = ds.drug_concept_id
LEFT JOIN sig_mapping sm ON sm.sig = de.sig
WHERE de.drug_concept_id in (40182874,40182870,40182878); 

select * from observation;