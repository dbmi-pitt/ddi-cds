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
SET drug_exposure_end_date = '2008-04-27'
WHERE drug_exposure_id in (149408,149409,149410,149411,149412,149413,149414,149415,149416,149417,149418,149419,149420,149421,149422,149423,149424,149425,149426,149427,149428,149429,149430,149431,149432,149433,149434,149435,149436,149437,149438,149439,149440,149441,149442,149443,149444,149445,149446,149447,149448,149449,149450,149451,149452,149453,149454,149455,149456,149457,149458,149459,149460,149461,149462,149463,149464,149465,149466,149467,149468,149469,149470,149471,149472,149473,149474,149475,149476,149477,149478,149479,149480,149481,149482,149487,149483,149484,149485,149486,149488,149489,149490,149491,149492,149493,149494,149495,149496,149497,149498,149499,149500,149501,149502,149503,149504,149505,149506,149507,149508,149509,149510,149511,149512,149513,149514,149515,149516,149517,149518,149519,149520,149521,149522,149523,149524,149525,149526,149527,149529,149530,149531,149532,149533,149534,149535,149537,149538,149539,149540,149541,149542,149543,149544,149545,149546,149547,149548,149549,149550,149551,149552,149553,149554,149555,149556,149557,149558,149559,149560,149561,149562,149563,149564,149565,149566);
-- these dexp ID's are used for the "patient-addition" script. The end date specified matches the end date used for every drug era in this script.

UPDATE drug_exposure
SET drug_exposure_start_datetime = CAST(drug_exposure_start_date AS TIMESTAMP), 
drug_exposure_end_datetime = CAST(drug_exposure_end_date AS TIMESTAMP);

select * from sig_mapping order by expected desc;
-- sig
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
