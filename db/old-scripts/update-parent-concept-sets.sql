/*
Beta-Blockers Includes:
Beta-Blockers

(Selective)
Acebutolol
Atenolol
Bisoprolol
Esmolol
Metoprolol
Nebivolol
Betaxolol

(Alpha-Blocking)
Carvedilol
Labetalol

(Non-Selective)
Carteolol
Levobunolol
Nadolol
Penbutolol
Pindolol
Propranolol
Sotalol
Timolol

Timolols Eye Drops
Timolols Eye Gels
Timolols Oral
*/

##########################################################################################
## BETA-BLOCKERS
##########################################################################################

-- UPDATE BETA-BLOCKERS INGREDIENTS
BEGIN TRANSACTION; 
-- temp table
CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Beta-Blockers Ingredients','Acebutolols Ingredients','Atenolols Ingredients','Bisoprolols Ingredients','Esmolols Ingredients','Metoprolols Ingredients','Nebivolols Ingredients','Betaxolols Ingredients','Carvedilols Ingredients','Labetalols Ingredients','Carteolols Ingredients','Levobunolols Ingredients','Nadolols Ingredients','Penbutolols Ingredients','Pindolols Ingredients','Propranolols Ingredients','Sotalols Ingredients','Timolols Ingredients'));

--delete currently existing parent concept set items so that duplicates don't accumulate
DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 7756;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 7756 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;

-- UPDATE BETA-BLOCKERS
BEGIN TRANSACTION;

CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Beta-Blockers','Acebutolols','Atenolols','Bisoprolols','Esmolols','Metoprolols','Nebivolols','Betaxolols','Carvedilols','Labetalols','Carteolols','Levobunolols','Nadolols','Penbutolols','Pindolols','Propranolols','Sotalols','Timolols','Timolols Eye Drops', 'Timolols Eye Gels', 'Timolols Oral'));

DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 7773;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 7773 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;

-- 9/25 - FINDING CONCEPTS INCLUDED IN BETA-BLOCKERS BUT NOT ITS "CHILD" CONCEPT-SETS:
--826
select count(distinct i.concept_id) from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Acebutolols','Atenolols','Bisoprolols','Esmolols','Metoprolols','Nebivolols','Betaxolols','Carvedilols','Labetalols','Carteolols','Levobunolols','Nadolols','Penbutolols','Pindolols','Propranolols','Sotalols','Timolols','Timolols Eye Drops', 'Timolols Eye Gels', 'Timolols Oral');

--838
select count(distinct i.concept_id) from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Beta-Blockers');

--reveals the 12 concept id difference
select cs.*, i.concept_id, c.concept_name from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
inner join concept c
on c.concept_id = i.concept_id
where cs.concept_set_name in ('Beta-Blockers')
and i.concept_id not in (select i.concept_id from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Acebutolols','Atenolols','Bisoprolols','Esmolols','Metoprolols','Nebivolols','Betaxolols','Carvedilols','Labetalols','Carteolols','Levobunolols','Nadolols','Penbutolols','Pindolols','Propranolols','Sotalols','Timolols','Timolols Eye Drops', 'Timolols Eye Gels', 'Timolols Oral'));
/*
40163948 -- added to carvedilol
40163950 -- added to carvedilol
40163952 -- added to carvedilol
40163954 -- added to carvedilol
40166817 -- added to metoprolol
40166819 -- added to metoprolol
40166821 -- added to metoprolol
40166824 -- added to metoprolol
40166826 -- added to metoprolol
40166828 -- added to metoprolol
40166830 -- added to metoprolol
1346990 -- added to carvedilol
*/
-- update carvedilol
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values
(11987,40163948,0,0,0),
(11987,40163950,0,0,0),
(11987,40163952,0,0,0),
(11987,40163954,0,0,0),
(11987,1346990,0,0,0);

-- update metoprolol
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values
(8211,40166817,0,0,0),
(8211,40166819,0,0,0),
(8211,40166821,0,0,0),
(8211,40166824,0,0,0),
(8211,40166826,0,0,0),
(8211,40166828,0,0,0),
(8211,40166830,0,0,0);

##########################################################################################
## CLONIDINE
##########################################################################################

/*
Clonidines Includes:
Clonidines
Clonidines Injectable
Clonidines Oral
Clonidines Transdermal
*/

BEGIN TRANSACTION;

CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Clonidines','Clonidines Injectable','Clonidines Oral','Clonidines Transdermal'));

DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 11501;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 11501 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;


##########################################################################################
## GI BLEED CONDITIONS
##########################################################################################
/*
Update "History of GI Bleeds" concept set based on ICD9/10 codes in protocol
*/

BEGIN TRANSACTION;

CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('History of GI Bleeds'));

DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 6094;

INSERT INTO ohdsi.concept_set_item (concept_id, concept_set_id, is_excluded, include_descendants, include_mapped)
(select distinct concept_id, 6094 as concept_set_id, 0 as is_excluded, 0 as include_descendants, 0 as include_mapped from concept where concept_code in ('578.0','532.00','578.9','530.21','530.82','531.0','531.2','531.4','531.6','532.0','532.2','532.4','532.6','533.0','533.2','533.4','533.6','534.0','534.2','534.4','534.6','K92.2','K25.0','K25.2','K25.4','K25.6','K26.0','K26.2','K26.4','K26.6','K27.0','K27.2','K27.4','K27.6','K28.0','K28.2','K28.4','K28.6','K29.01','K29.31','K29.41','K29.51','K29.61','K29.71','K29.81','K29.91','K31.811','K31.82'));

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;

##########################################################################################
## K-SPARING DIURETICS
##########################################################################################

/*
Update K-sparing Diuretics
Includes: 
Spironolactones
Amilorides
Eplerenones
Triamterenes
*/

BEGIN TRANSACTION;

CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Spironolactones','Amilorides','Eplerenones','Triamterenes'));

DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 6563;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 6563 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;

##########################################################################################
## QT PROLONGING
##########################################################################################
/*
Update QT-Agents Ingredients
Chlorpromazines Ingredients
Cilostazols Ingredients
Ciprofloxacins Ingredients
Donepezils Ingredients
Escitaloprams Ingredients
Flecainides Ingredients
Haloperidols Ingredients
Levofloxacins Ingredients
Methadones Ingredients
Ondansetrons Ingredients
Propofols Ingredients
Quinines Ingredients
Ranolazines Ingredients
*/

BEGIN TRANSACTION; 
-- temp table
CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Chlorpromazines Ingredients','Cilostazols Ingredients','Ciprofloxacins Ingredients','Donepezils Ingredients','Escitaloprams Ingredients','Flecainides Ingredients','Haloperidols Ingredients','Levofloxacins Ingredients','Methadones Ingredients','Ondansetrons Ingredients','Propofols Ingredients','Quinines Ingredients','Ranolazines Ingredients'));

--delete currently existing parent concept set items so that duplicates don't accumulate
DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 11466;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 11466 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;

/*
UPDATE QT-AGENTS
Chlorpromazines
Cilostazols
Ciprofloxacins
Donepezils
Escitaloprams
Flecainides
Haloperidols
Levofloxacins
Methadones
Ondansetrons
Propofols
Quinines
Ranolazines
*/

BEGIN TRANSACTION;

CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Chlorpromazines','Cilostazols','Ciprofloxacins','Donepezils','Escitaloprams','Flecainides','Haloperidols','Levofloxacins','Methadones','Ondansetrons','Propofols','Quinines','Ranolazines'));

DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 11441;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 11441 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;

##########################################################################################
## SALICYLATES
##########################################################################################
/*
SALICYLATES INGREDIENTS:
Non-aceylated Salicylates Ingredients
Aspirins Ingredients
*/

BEGIN TRANSACTION; 
-- temp table
CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Non-acetylated Salicylates Ingredients','Aspirins Ingredients'));

--delete currently existing parent concept set items so that duplicates don't accumulate
DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 9396;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 9396 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;

/*
SALICYLATES:
Non-aceylated Salicylates
Aspirins
*/
BEGIN TRANSACTION; 
-- temp table
CREATE TABLE ohdsi.temp_concept_set_item (
  concept_set_item_id serial NOT NULL,
  concept_set_id int4 NOT NULL,
  concept_id int4 NOT NULL,
  is_excluded int4 NOT NULL,
  include_descendants int4 NOT NULL,
  include_mapped int4 NOT NULL
);

INSERT INTO ohdsi.temp_concept_set_item
(select i.concept_set_item_id, i.concept_set_id, i.concept_id, i.is_excluded, i.include_descendants, i.include_mapped 
from ohdsi.concept_set cs
inner join ohdsi.concept_set_item i
on i.concept_set_id = cs.concept_set_id
where cs.concept_set_name in ('Non-acetylated Salicylates','Aspirins'));

--delete currently existing parent concept set items so that duplicates don't accumulate
DELETE FROM ohdsi.concept_set_item WHERE concept_set_id = 60430;

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
(select 60430 as concept_set_id, concept_id, is_excluded, include_descendants, include_mapped from ohdsi.temp_concept_set_item);

DROP TABLE ohdsi.temp_concept_set_item;

COMMIT TRANSACTION;
