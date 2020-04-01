set search_path to banner_etl;

--big picture
select count(distinct person_id) from person;

-- distinct people who have visits during study period
select count(distinct person_id)
--select count(distinct visit_occurrence_id)
from visit_occurrence 
where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
and visit_start_datetime != visit_end_datetime;

-- distinct people who have visits during study period that are >=24 hours
select count(distinct person_id)
--select count(distinct visit_occurrence_id)
from visit_occurrence 
where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
and visit_start_datetime != visit_end_datetime;

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
  and visit_start_datetime != visit_end_datetime
);

select count(distinct measurement_id) from measurement
where measurement_datetime between '2016-01-01'::date and '2016-03-31'::date;

-- NOTE: not sure how useful observation periods are since all of them end at the same time (2017-07-05) regardless of the contents of their visit data.
select count(distinct observation_period_id) from observation_period;

##########################################################################################
## PERSON
##########################################################################################
--gender
select gender_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (select count(distinct person_id) from visit_occurrence where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date) and visit_start_datetime != visit_end_datetime)) * 100 as percentage
from person
where person_id in (
  select distinct person_id
  from visit_occurrence 
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and visit_start_datetime != visit_end_datetime
)
group by gender_source_value;
/*
|gender_source_value                               |count               |percentage                                                                                          |
|--------------------------------------------------|--------------------|----------------------------------------------------------------------------------------------------|
|Female                                            |13164               |53.51437050286597016100                                                                             |
|Male                                              |11435               |46.48562949713402983900                                                                             |
*/

--race
-- NOTE: using ethnicity (below) instead of this, since there's a bunch of artifacts in the "race_source_value" output that I'm not sure what to do with
select race_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (select count(distinct person_id) from visit_occurrence where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date) and visit_start_datetime != visit_end_datetime)) * 100 as percentage
from person
where person_id in (
  select distinct person_id
  from visit_occurrence 
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and visit_start_datetime != visit_end_datetime
)
group by race_source_value;

--ethnicity
select ethnicity_source_value, count(distinct person_id), 
(count(distinct person_id)::decimal / (select count(distinct person_id) from visit_occurrence where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date) and visit_start_datetime != visit_end_datetime)) * 100 as percentage
from person
where person_id in (
  select distinct person_id
  from visit_occurrence 
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and visit_start_datetime != visit_end_datetime
)
group by ethnicity_source_value;
/*
|ethnicity_source_value                            |count               |percentage                                                                                          |
|--------------------------------------------------|--------------------|----------------------------------------------------------------------------------------------------|
|Hispanic or Latino                                |10407               |42.30659782918004796900                                                                             |
|Not Hispanic or Latino                            |13748               |55.88845075003048904400                                                                             |
|Patient Refused                                   |49                  |0.19919508923126956400                                                                              |
|Unknown                                           |386                 |1.56916947843408268600                                                                              |
|                                                  |9                   |0.03658685312411073600                                                                              |
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
  and visit_start_datetime != visit_end_datetime
);
/*
|avg_age                                                                                             |stddev_age                                                                                          |min_age                                                                                             |max_age                                                                                             |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
|43.40989248365703694764                                                                             |23.7229137952583316911184421681943280774744                                                         |0.24657534246575342466                                                                              |105.3178082191780822                                                                                |
*/

-- age intervals
SELECT age_group, count(distinct person_id), (count(distinct person_id)::decimal / (select count(distinct person_id) from visit_occurrence where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date) and visit_start_datetime != visit_end_datetime)) * 100 as percentage
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
    and visit_start_datetime != visit_end_datetime
  )
) a
GROUP BY age_group;
/*
|age_group                                                                                           |count               |percentage                                                                                          |
|----------------------------------------------------------------------------------------------------|--------------------|----------------------------------------------------------------------------------------------------|
|18 to 29 years old                                                                                  |4193                |17.04540834993292410300                                                                             |
|30 to 39 years old                                                                                  |3140                |12.76474653441196796600                                                                             |
|40 to 49 years old                                                                                  |2873                |11.67933655839668279200                                                                             |
|50 to 59 years old                                                                                  |3476                |14.13065571771210211800                                                                             |
|60 to 69 years old                                                                                  |3331                |13.54120086182365136800                                                                             |
|70 to 79 years old                                                                                  |2298                |9.34184316435627464500                                                                              |
|80 to 89 years old                                                                                  |1158                |4.70750843530224805900                                                                              |
|90 years old and older                                                                              |287                 |1.16671409406886458800                                                                              |
|Under 18 years old                                                                                  |3843                |15.62258628399528436100                                                                             |
*/

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
and visit_start_datetime != visit_end_datetime;
--and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval;
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
  --and (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval
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

-- number of 24 hour visits per person
select sum(visit_24_hrs), avg(visit_24_hrs) as num_24hrs, stddev(visit_24_hrs), min(visit_24_hrs), max(visit_24_hrs),
sum(visit_short), avg(visit_short) as num_short, stddev(visit_short), min(visit_short), max(visit_short)
from (
  select person_id,
  sum(case when (visit_end_datetime - visit_start_datetime) >= (24 || ' hours')::interval then 1 else 0 end) as visit_24_hrs,
  sum(case when (visit_end_datetime - visit_start_datetime) < (24 || ' hours')::interval then 1 else 0 end) as visit_short
  from visit_occurrence
  where (visit_start_datetime between '2016-01-01'::date and '2016-03-31'::date 
  or visit_end_datetime between '2016-01-01'::date and '2016-03-31'::date)
  and visit_start_datetime != visit_end_datetime
  group by person_id
) v;
/*
|sum                                                                                                 |num_24hrs                                                                                           |stddev                                                                                              |min                 |max                 |sum                                                                                                 |num_short                                                                                           |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|10506                                                                                               |0.42709053213545266068                                                                              |0.62181743141947423504                                                                              |0                   |9                   |20826                                                                                               |0.84661978129192243587                                                                              |1.1356899700438285                                                                                  |0                   |51                  |
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
  and visit_start_datetime != visit_end_datetime
  group by v.visit_occurrence_id, v.person_id
) ds;
/*
|sum                                                                                                 |avg                                                                                                 |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|516387                                                                                              |17.3359854970289052                                                                                 |29.0148339204020701                                                                                 |1                   |980                 |
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
  and visit_start_datetime != visit_end_datetime
  group by v.visit_occurrence_id, v.person_id
) m;
/*
|sum                                                                                                 |avg                                                                                                 |stddev                                                                                              |min                 |max                 |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|--------------------|--------------------|
|284657                                                                                              |16.8217113816333767                                                                                 |31.5084891543746876                                                                                 |1                   |1252                |
*/
