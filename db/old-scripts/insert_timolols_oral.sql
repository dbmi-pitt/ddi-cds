select r.*, c.concept_name, c.domain_id from concept_relationship r 
inner join concept c 
on c.concept_id = r.concept_id_2
where concept_id_1 in (select concept_id from ohdsi.concept_set_item where concept_set_id = 12108) --12108 Timolols
and r.relationship_id = 'RxNorm has dose form'
and concept_id_2 = 19082573;
-- oral tablet = 19082573
-- timolols ingredients 902427
-- 19078139,902452,40177821,902430,19063050,19107669,902431,19003739,19063049
select * from concept where concept_id in (19078139,902452,40177821,902430,19063050,19107669,902431,19003739,19063049);
select * from ohdsi.concept_set_item where concept_set_id = 12003;
--12003 Timolols oral

INSERT INTO ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
VALUES (12003,19078139,0,0,0),
(12003,902452,0,0,0),
(12003,40177821,0,0,0),
(12003,902430,0,0,0),
(12003,19063050,0,0,0),
(12003,19107669,0,0,0),
(12003,902431,0,0,0),
(12003,19003739,0,0,0),
(12003,19063049,0,0,0);