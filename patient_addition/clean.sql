-- delete patients encompassed by the patient addition script prior to repopulating/rerunning inserts to avoid duplicates/conflicts.

TRUNCATE TABLE public.visit_occurrence;
TRUNCATE TABLE public.drug_era;
TRUNCATE TABLE public.condition_era;
TRUNCATE TABLE public.drug_exposure;
TRUNCATE TABLE public.measurement;
TRUNCATE TABLE public.person;
TRUNCATE TABLE public.f_person;

/*
DELETE FROM visit_occurrence WHERE person_id >= 1495;

DELETE FROM drug_era WHERE person_id >= 1495;

DELETE FROM condition_era WHERE person_id >= 1495;

DELETE FROM drug_exposure WHERE person_id >= 1495;

DELETE FROM measurement WHERE person_id >= 1495;

DELETE FROM person WHERE person_id >= 1495;
*/
