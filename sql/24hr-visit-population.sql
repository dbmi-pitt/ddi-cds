-- both start and end date during study date, stay at least 24 hours
select count(distinct person_id)
from visit_occurrence 
where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
and visit_start_datetime != visit_end_datetime;

select person_id, count(distinct visit_occurrence_id) AS cnt
from visit_occurrence 
where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
and visit_start_datetime != visit_end_datetime
group by person_id
order by cnt desc;

-- drug exposures that happened overlapped with the study period
select count(drug_exposure_id) from drug_exposure
where (drug_exposure_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
or drug_exposure_end_datetime between '2016-01-01'::date and '2016-03-31'::date);

-- consider any conditions from any time, but only for patients who visited in the study period.
select count(condition_occurrence_id) from condition_occurrence
where person_id in (
  select distinct person_id
  from visit_occurrence 
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
  and visit_start_datetime != visit_end_datetime
);
-- 162147

select count(distinct measurement_id) from measurement
where measurement_datetime between '2016-01-01'::date and '2016-03-31'::date;

##########################################################################################
## PERSON
##########################################################################################
--gender
select gender_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (select count(distinct person_id) from visit_occurrence where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date) and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval and visit_start_datetime != visit_end_datetime)) * 100 as percentage
from person
where person_id in (
  select distinct person_id
  from visit_occurrence 
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
  and visit_start_datetime != visit_end_datetime
)
group by gender_source_value;
/*
|gender_source_value                               |count               |percentage                                                                                          |
|--------------------------------------------------|--------------------|----------------------------------------------------------------------------------------------------|
|Female                                            |4658                |50.75179777729352800200                                                                             |
|Male                                              |4520                |49.24820222270647199800                                                                             |
*/

--race
-- NOTE: using ethnicity (below) instead of this, since there's a bunch of artifacts in the "race_source_value" output that I'm not sure what to do with
select race_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (select count(distinct person_id) from visit_occurrence where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date) and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval and visit_start_datetime != visit_end_datetime)) * 100 as percentage
from person
where person_id in (
  select distinct person_id
  from visit_occurrence 
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
  and visit_start_datetime != visit_end_datetime
)
group by race_source_value;

--ethnicity
select ethnicity_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (select count(distinct person_id) from visit_occurrence where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date) and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval and visit_start_datetime != visit_end_datetime)) * 100 as percentage
from person
where person_id in (
  select distinct person_id
  from visit_occurrence 
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
  and visit_start_datetime != visit_end_datetime
)
group by ethnicity_source_value;
/*
|ethnicity_source_value                            |count               |percentage                                                                                          |
|--------------------------------------------------|--------------------|----------------------------------------------------------------------------------------------------|
|Hispanic or Latino                                |3249                |35.39986925256047069100                                                                             |
|Not Hispanic or Latino                            |5744                |62.58444105469601220300                                                                             |
|Patient Refused                                   |16                  |0.17432991937241229000                                                                              |
|Unknown                                           |165                 |1.79777729352800174300                                                                              |
|                                                  |4                   |0.04358247984310307300                                                                              |
*/

--age distribution
-- NOTE there are people born during the study period. Used end of study period.
select avg(('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) as avg_age,
stddev(('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) as stddev_age,
min(('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) as min_age,
max(('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) as max_age
from person
where person_id in (
  select distinct person_id
  from visit_occurrence 
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
  and visit_start_datetime != visit_end_datetime
);
/*
|avg_age                                                                                             |stddev_age                                                                                          |min_age                                                                                             |max_age                                                                                             |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
|51.42189930059075155247                                                                             |23.6801424831801841901164729312676311184825                                                         |0.24657534246575342466                                                                              |105.3178082191780822                                                                                |
*/

-- age intervals
SELECT age_group, count(distinct person_id), (count(distinct person_id)::decimal / (select count(distinct person_id) from visit_occurrence where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date) and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval and visit_start_datetime != visit_end_datetime)) * 100 as percentage
FROM (
  SELECT
  person_id, to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY') AS dob,
  CASE
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) < 18 THEN 'Under 18 years old'
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) >= 18 AND (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) < 30 THEN '18 to 29 years old'
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) >= 30 AND (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) < 40 THEN '30 to 39 years old'
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) >= 40 AND (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) < 50 THEN '40 to 49 years old'
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) >= 50 AND (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) < 60 THEN '50 to 59 years old'
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) >= 60 AND (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) < 70 THEN '60 to 69 years old'
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) >= 70 AND (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) < 80 THEN '70 to 79 years old'
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) >= 80 AND (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) < 90 THEN '80 to 89 years old'
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) >= 90 THEN '90 years old and older'
  END AS age_group
  FROM person
  where person_id in (
    select distinct person_id
    from visit_occurrence 
    where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
    or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
    and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
    and visit_start_datetime != visit_end_datetime
  )
) a
GROUP BY age_group;
/*
|age_group                                                                                           |count               |percentage                                                                                          |
|----------------------------------------------------------------------------------------------------|--------------------|----------------------------------------------------------------------------------------------------|
|18 to 29 years old                                                                                  |996                 |10.85203748093266506900                                                                             |
|30 to 39 years old                                                                                  |935                 |10.18740466332534321200                                                                             |
|40 to 49 years old                                                                                  |971                 |10.57964698191327086500                                                                             |
|50 to 59 years old                                                                                  |1451                |15.80954456308563957300                                                                             |
|60 to 69 years old                                                                                  |1658                |18.06493789496622357800                                                                             |
|70 to 79 years old                                                                                  |1283                |13.97908040967531052500                                                                             |
|80 to 89 years old                                                                                  |749                 |8.16081935062105033800                                                                              |
|90 years old and older                                                                              |205                 |2.23360209195903246900                                                                              |
|Under 18 years old                                                                                  |930                 |10.13292656352146437100                                                                             |
*/

-- condensed age intervals
SELECT age_group, count(distinct person_id), (count(distinct person_id)::decimal / (select count(distinct person_id) from visit_occurrence where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date) and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval and visit_start_datetime != visit_end_datetime)) * 100 as percentage
FROM (
  SELECT
  person_id, to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY') AS dob,
  CASE
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) < 18 THEN 'Under 18 years old'
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) >= 18 AND (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) < 60 THEN '18 to 60 years old'
    WHEN (('2016-03-31'::date - to_date(CONCAT(month_of_birth,' ',day_of_birth,' ',year_of_birth), 'm d YYYY')) / 365.0) >= 60 THEN '60 years old and older'
  END AS age_group
  FROM person
  where person_id in (
    select distinct person_id
    from visit_occurrence 
    where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
    or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
    and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
    and visit_start_datetime != visit_end_datetime
  )
) a
GROUP BY age_group;

##########################################################################################
## VISITS
##########################################################################################
--visit length
-- NOTE: if the stay length >=24 hours filter is excluded then the average is heavily skewed.
-- 353 visit_occurrence rows where start and end datetime are the exact same. Filtering these out.
select count(visit_occurrence_id), avg(visit_end_datetime - visit_start_datetime) as avg_visit_length,
min(visit_end_datetime - visit_start_datetime),
max(visit_end_datetime - visit_start_datetime)
from visit_occurrence
where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
and visit_start_datetime != visit_end_datetime;
/*
ALL:
|count               |avg_visit_length            |min                         |max                         |
|--------------------|----------------------------|----------------------------|----------------------------|
|31332               |1 day 29:18:05.231712       |00:01:00                    |225 days 03:27:00           |

>=24 HOURS:
|count               |avg_visit_length            |min                         |max                         |
|--------------------|----------------------------|----------------------------|----------------------------|
|10506               |5 days 27:09:57.350086      |1 day                       |225 days 03:27:00           |
*/

-- number of visits
select sum(num_visits), avg(num_visits), stddev(num_visits), min(num_visits), max(num_visits)
from (
  select person_id, count(*) as num_visits
  from visit_occurrence
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
  --and (visit_end_datetime - visit_start_datetime) < (24 || ' hours')::interval
  and visit_start_datetime != visit_end_datetime
  group by person_id
) v;
/*
ALL: 
|sum                                                                                                 |avg                                                                                                 |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|31332                                                                                               |1.2737103134273751                                                                                  |1.1309515844134258                                                                                  |1                   |51                  |

>=24 HOURS:
|sum                                                                                                 |avg                                                                                                 |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|10506                                                                                               |1.1446938330791022                                                                                  |0.46353891083255355987                                                                              |1                   |9                   |

<24 HOURS
|sum                                                                                                 |avg                                                                                                 |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|20826                                                                                               |1.2243386243386243                                                                                  |1.1843911011694387                                                                                  |1                   |51                  |

*/

select sum(visit_24_hrs), avg(visit_24_hrs) as num_24hrs, stddev(visit_24_hrs), min(visit_24_hrs), max(visit_24_hrs),
sum(visit_short), avg(visit_short) as num_short, stddev(visit_short), min(visit_short), max(visit_short)
from (
  select person_id,
  sum(case when (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval then 1 else 0 end) as visit_24_hrs,
  sum(case when (visit_end_datetime - visit_start_datetime) < (24 || ' hours')::interval then 1 else 0 end) as visit_short
  from visit_occurrence
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
  and visit_start_datetime != visit_end_datetime
  group by person_id
) v;
/*
|sum                                                                                                 |num_24hrs                                                                                           |stddev                                                                                              |min                 |max                 |sum                                                                                                 |num_short                                                                                           |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|10506                                                                                               |1.1446938330791022                                                                                  |0.46353891083255355987                                                                              |1                   |9                   |0                                                                                                   |0.00000000000000000000                                                                              |0                                                                                                   |0                   |0                   |
*/

##########################################################################################
## DRUGS
##########################################################################################
--number of drug exposures per stay
--516387 drug exposures were started during visit and during study period 
select sum(num_drugs_started), avg(num_drugs_started), stddev(num_drugs_started), min(num_drugs_started), max(num_drugs_started)
from (
  select v.person_id, v.visit_occurrence_id, count(drug_exposure_id) as num_drugs_started 
  from drug_exposure d
  inner join visit_occurrence v
  on d.person_id = v.person_id
  and d.drug_exposure_start_datetime between v.visit_start_datetime and v.visit_end_datetime
  and (drug_exposure_start_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
  and visit_start_datetime != visit_end_datetime
  group by v.visit_occurrence_id, v.person_id
) ds;
/*
|sum                                                                                                 |avg                                                                                                 |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|336789                                                                                              |32.3431287813310285                                                                                 |43.1655821218013307                                                                                 |1                   |980                 |
*/

##########################################################################################
## MEASUREMENTS
##########################################################################################
-- number of measurements per stay
select sum(num_measurements), avg(num_measurements), stddev(num_measurements), min(num_measurements), max(num_measurements)
from (
  select v.person_id, v.visit_occurrence_id, count(measurement_id) as num_measurements 
  from measurement m
  inner join visit_occurrence v
  on m.person_id = v.person_id
  and m.measurement_datetime between v.visit_start_datetime and v.visit_end_datetime
  and m.measurement_datetime between '2016-01-01'::date and '2016-03-31'::date
  and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
  and visit_start_datetime != visit_end_datetime
  group by v.visit_occurrence_id, v.person_id
) m;
/*
|sum                                                                                                 |avg                                                                                                 |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|234353                                                                                              |27.7998813760379597                                                                                 |41.7600348434897586                                                                                 |1                   |1252                |
*/
