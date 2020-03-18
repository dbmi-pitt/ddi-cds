set search_path to public;

-- measurement
SELECT CAST(measurement.measurement_date AS TIMESTAMP) FROM measurement;
SELECT measurement.measurement_time FROM measurement WHERE measurement_time is not null; -- time columns appear to be empty throughout this schema

ALTER TABLE measurement
ADD measurement_datetime TIMESTAMP;

UPDATE measurement
SET measurement_datetime = CAST(measurement.measurement_date AS TIMESTAMP);

-- dexp
SELECT * FROM drug_exposure LIMIT 100; 
SELECT * FROM drug_exposure WHERE drug_exposure_end_date is not null;

ALTER TABLE drug_exposure
ADD drug_exposure_start_datetime TIMESTAMP,
ADD drug_exposure_end_datetime TIMESTAMP;

UPDATE drug_exposure
SET drug_exposure_start_datetime = CAST(drug_exposure_start_date AS TIMESTAMP), 
drug_exposure_end_datetime = CAST(drug_exposure_end_date AS TIMESTAMP);

select * from sig_mapping order by expected desc;
-- sig
/*
UPDATE drug_exposure
SET sig = 'Daily'
WHERE quantity >= 60;
UPDATE drug_exposure
SET sig = '2 times daily'
WHERE quantity < 60 AND quantity >= 30;
UPDATE drug_exposure
SET sig = '3 times daily'
WHERE quantity < 30 AND quantity >= 10;
UPDATE drug_exposure
SET sig = '4 times daily'
WHERE quantity < 10 AND quantity >= 5;
UPDATE drug_exposure
SET sig = 'Every 2 hours'
WHERE quantity < 5;
*/

-- indication field
ALTER TABLE drug_exposure 
ADD indication_concept_id INTEGER;

-- choose 10 random epinephrine concept id's (out of 27 total) to assign indication_concept_id=4340851
select * from ohdsi.concept_set_item where concept_set_id = 7728
order by random() limit 10;

--24 rows affected
UPDATE drug_exposure
SET indication_concept_id = 4340851
WHERE drug_concept_id IN (40226820,46275414,40225960,45892487,19076899,40226826,45892254,1344245,45892250,19127146)

-- this made all simulation data have an indication since they all had the same drug, so changed some of them to no longer have it.
--1509,1510,1511,1512,1513,1514,1515,1516,1517,1518,1519,1520,1521,1533,1534,1535
UPDATE drug_exposure
SET indication_concept_id = null
WHERE person_id IN (1509,1510,1514,1515,1516,1518,1534,1535);

select * from drug_exposure where indication_concept_id = 4340851;

--create anaphylaxis indication concept set.
select * from concept where concept_id = 4340851;
select * from ohdsi.concept_set order by concept_set_id asc;
INSERT INTO ohdsi.concept_set 
VALUES (12000, 'Anaphylaxis Indication');

select * from ohdsi.concept_set_item;
INSERT INTO ohdsi.concept_set_item
VALUES (12000, 12000, 4340851, 0, 0, 0);

-- plastic surgery indication concept set (?)
select * from concept where vocabulary_id = 'NDFRT' and concept_class_id = 'Ind / CI'
and concept_name ilike '%surgery%';

-- create concept sets for routes
-- eye drops
select * from ohdsi.concept_set_item where concept_set_id = 9160;
select * from ohdsi.concept_set_item where concept_set_id = 9162;
select * from public.drug_exposure 
where drug_concept_id IN(select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 9162)
AND person_id >= 1495 ORDER BY person_id asc;

UPDATE public.drug_exposure
SET route_concept_id = 4184451
WHERE drug_concept_id IN(select distinct concept_id from ohdsi.concept_set_item where concept_set_id = 9162);

INSERT INTO ohdsi.concept_set
VALUES(12001, 'Eye Drops Route');
INSERT INTO ohdsi.concept_set_item
VALUES(12001, 12001, 4184451, 0, 0, 0);

-- ORAL
SELECT d.drug_concept_id, c.concept_name, d.route_concept_id, d.route_source_value 
FROM drug_exposure d
INNER JOIN concept c ON c.concept_id = d.drug_concept_id
WHERE concept_name ilike '%timolol%';

SELECT d.drug_exposure_id, d.person_id, d.drug_concept_id, c.concept_name, d.route_concept_id, d.route_source_value 
FROM drug_exposure d
INNER JOIN concept c ON c.concept_id = d.drug_concept_id
WHERE concept_name ilike '%timolol%oral%';

INSERT INTO ohdsi.concept_set
VALUES(12002, 'Oral Route');
INSERT INTO ohdsi.concept_set_item
VALUES(12002, 12002, 4132161, 0, 0, 0),
(12003, 12002, 4128794, 0, 0, 0);
INSERT INTO ohdsi.concept_set
VALUES(12003, 'Timolols Oral');
INSERT INTO ohdsi.concept_set_item
VALUES(12004, 12003, 19025100, 0, 0, 0),
(12005, 12003, 902431, 0, 0, 0),
(12006, 12003, 902452, 0, 0, 0),
(12007, 12003, 902430, 0, 0, 0);

UPDATE public.drug_exposure
SET route_concept_id = 4128794
WHERE drug_concept_id IN(SELECT distinct drug_concept_id 
FROM drug_exposure d
INNER JOIN concept c ON c.concept_id = d.drug_concept_id
WHERE concept_name ilike '%timolol%oral%');