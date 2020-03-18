insert into ohdsi.concept_set (concept_set_id, concept_set_name)
values (12109, 'Timolols Eye Gels'), 
(12110, 'Clonidines Oral'), 
(12111, 'Clonidines Injectable'), 
(12112, 'Clonidines Topical'),
(12113, 'Clonidines Transdermal');

--timolols eye drops
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values (9162,40164138,0,0,0),
(9162,945502,0,0,0),
(9162,1594472,0,0,0),
(9162,1594484,0,0,0),
(9162,1594486,0,0,0),
(9162,1594707,0,0,0),
(9162,1594711,0,0,0),
(9162,793236,0,0,0),
(9162,793238,0,0,0);
--timolols eye gels
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values (12109,19079774,0,0,0),
(12109,19079775,0,0,0);
--clonidines oral
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values (12110,42705198,0,0,0),
(12110,40168653,0,0,0),
(12110,40169598,0,0,0),
(12110,40169602,0,0,0),
(12110,40168667,0,0,0),
(12110,40168657,0,0,0),
(12110,40168662,0,0,0),
(12110,40168669,0,0,0),
(12110,40169603,0,0,0),
(12110,40169599,0,0,0),
(12110,40227571,0,0,0),
(12110,42707371,0,0,0),
(12110,40168654,0,0,0),
(12110,40168488,0,0,0),
(12110,40168489,0,0,0),
(12110,40168664,0,0,0),
(12110,40227569,0,0,0);
-- clonidines injectable
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values (12111,40168672,0,0,0),
(12111,40168657,0,0,0),
(12111,40168673,0,0,0),
(12111,40168659,0,0,0);
-- clonidines topical
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values (12112,40223504,0,0,0),
(12112,40223506,0,0,0),
(12112,40223508,0,0,0),
(12112,40223152,0,0,0),
(12112,40223154,0,0,0),
(12112,40223156,0,0,0);

-- update existing beta-blockers concept set with new timolol concepts
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values (7773,40164138,0,0,0),
(7773,945502,0,0,0),
(7773,1594472,0,0,0),
(7773,1594484,0,0,0),
(7773,1594486,0,0,0),
(7773,1594707,0,0,0),
(7773,1594711,0,0,0),
(7773,793236,0,0,0),
(7773,793238,0,0,0),
(7773,19079774,0,0,0),
(7773,19079775,0,0,0);
-- update existing clonidines concept set 
insert into ohdsi.concept_set_item (concept_set_id, concept_id, is_excluded, include_descendants, include_mapped)
values (11501,42705198,0,0,0),
(11501,40168653,0,0,0),
(11501,40169598,0,0,0),
(11501,40169602,0,0,0),
(11501,40168667,0,0,0),
(11501,40168657,0,0,0),
(11501,40168662,0,0,0),
(11501,40168669,0,0,0),
(11501,40169603,0,0,0),
(11501,40169599,0,0,0),
(11501,40227571,0,0,0),
(11501,42707371,0,0,0),
(11501,40168654,0,0,0),
(11501,40168488,0,0,0),
(11501,40168489,0,0,0),
(11501,40168664,0,0,0),
(11501,40227569,0,0,0),
(11501,40168672,0,0,0),
(11501,40168657,0,0,0),
(11501,40168673,0,0,0),
(11501,40168659,0,0,0),
(11501,40223504,0,0,0),
(11501,40223506,0,0,0),
(11501,40223508,0,0,0),
(11501,40223152,0,0,0),
(11501,40223154,0,0,0),
(11501,40223156,0,0,0);
