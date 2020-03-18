--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.16
-- Dumped by pg_dump version 9.3.16
-- Started on 2019-09-13 08:44:56

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = ohdsi, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 219 (class 1259 OID 16490)
-- Name: concept_set; Type: TABLE; Schema: ohdsi; Owner: postgres; Tablespace: 
--

CREATE TABLE concept_set (
    concept_set_id integer DEFAULT nextval('concept_set_sequence'::regclass) NOT NULL,
    concept_set_name character varying(255) NOT NULL
);


-- ALTER TABLE ohdsi.concept_set OWNER TO mdia;

--
-- TOC entry 2530 (class 0 OID 16490)
-- Dependencies: 219
-- Data for Name: concept_set; Type: TABLE DATA; Schema: ohdsi; Owner: postgres
--

COPY concept_set (concept_set_id, concept_set_name) FROM stdin;
5853	NSAIDs Ingredients
5876	Warfarins
5887	NSAIDs
6094	History of GI Bleeds
6119	SSRIs and SNRIs Ingredients
6133	SSRIs and SNRIs
6208	Corticosteroids Ingredients
6218	Systemic Corticosteroids
6310	Aldosterone Antagonists Ingredients
6313	Aldosterone Antagonists
6321	HGI
6323	Potassiums
6563	K-sparing Diuretics
6646	Potassium Ingredients
6649	K-sparing Diuretics Ingredients
6665	ARBs Ingredients
6785	ARBs
6653	ACEis and ARBs
6654	ACEis and ARBs Ingredients
7205	Fluconazoles
7221	Opioids Ingredients
7224	Opioids
7203	Fluconazoles Ingredients
7402	Oxycodones Ingredients
7404	Oxycodones
7463	Fentanyls Ingredients
7465	Fentanyls
7584	Epinephrines Ingredients
7728	Epinephrines
7756	Beta-Blockers Ingredients
7773	Beta-Blockers
7871	Non-Selective Beta-Blockers
7912	Non-Selective Beta-Blockers Ingredients
7921	Betaxolols Ingredients
7923	Betaxolols
7929	Atenolols
7930	Atenolols Ingredients
8069	Bisoprolols Ingredients
8071	Bisoprolols
8202	Esmolols Ingredients
8204	Esmolols
8209	Metoprolols Ingredients
8211	Metoprolols
8386	CYP2D6 Inhibitors Ingredients
8412	CYP2D6 Inhibitors
9096	Nebivolols Ingredients
9098	Nebivolols
9160	Timolols Ingredients
9162	Timolols Eye Drops
9174	Triamterenes Ingredients
9176	Triamterenes
9207	Azole Antifungals
9309	Azole Antifungals Ingredients
9320	Immunosuppressants Ingredients
9325	Immunosuppressants
9379	Fluconazoles IV
9366	Fluconazoles PO
7201	Warfarins Ingredients
9396	Salicylates Ingredients
10140	Aspirins Ingredients
6039	PPIs and Misoprostols
6031	PPIs and Misoprostols Ingredients
10563	Calciums
10564	Calciums Ingredients
10565	Ceftriaxones
10566	Ceftriaxones Ingredients
10571	Metoclopramides Ingredients
10573	Metoclopramides
10615	Antipsychotics Ingredients
10636	Cholinesterase Inhibitors Ingredients
10640	Cholinesterase Inhibitors
10666	Antipsychotics
11087	CYP2D6 PM
11395	Bismuth Subsalicylates Ingredients
11397	Bismuth Subsalicylates
11404	Non-acetylated Salicylates Ingredients
11409	Non-acetylated Salicylates
11427	Amiodarones
11439	Amiodarones Ingredients
11441	QT-Agents
11466	QT-Agents Ingredients
11469	Flecainides Ingredients
11471	Flecainides
11478	Ondansetrons Ingredients
11480	Ondansetrons
11501	Clonidines
11533	Clonidines Ingredients
11549	COPY OF: Atenolols
11570	DOACs
12003	Timolols Oral
11700	Carteolols
11698	Carteolols Ingredients
11719	Levobunolols Ingredients
11721	Levobunolols
11738	Nadolols Ingredients
11740	Nadolols
12002	Oral Routes
12001	Eye Drops Routes
11764	Penbutolols Ingredients
11766	Penbutolols
11781	Pindolols Ingredients
11783	Pindolols
11807	Propranolols Ingredients
11809	Propranolols
12000	Anaphylaxis Indications
11936	Sotalols Ingredients
11938	Sotalols
11985	Carvedilols Ingredients
11987	Carvedilols
12038	Labetalols Ingredients
12040	Labetalols
12086	Acebutolols Ingredients
12088	Acebutolols
12108	Timolols
12109	Timolols Eye Gels
12110	Clonidines Oral
12111	Clonidines Injectable
12112	Clonidines Topical
12113	Clonidines Transdermal
12114	Citaloprams Ingredients
12115	Citaloprams
14373	Spironolactones Ingredients
14375	Spironolactones
14568	Amilorides Ingredients
14570	Amilorides
14692	Eplerenones Ingredients
14694	Eplerenones
14721	Chlorpromazines Ingredients
14723	Chlorpromazines
14854	Cilostazols Ingredients
14856	Cilostazols
14859	Ciprofloxacins Ingredients
14861	Ciprofloxacins
15050	Donepezils Ingredients
15052	Donepezils
15080	Escitaloprams Ingredients
15082	Escitaloprams
15251	Haloperidols Ingredients
15253	Haloperidols
15392	Levofloxacins Ingredients
15394	Levofloxacins
15435	Methadones Ingredients
15437	Methadones
15766	Propofols Ingredients
15768	Propofols
15836	Quinines Ingredients
15838	Quinines
11215	All Salicylates
16041	Ranolazines Ingredients
16043	Ranolazines
16163	COPY OF: SSRIs and SNRIs
22188	Antiplatelet Medications Ingredients
22210	Antiplatelet Medications
48394	Aspirins
49329	Mirtazapines Ingredients
49331	Mirtazapines
49363	Tricyclics Ingredients
49381	Tricyclics
54933	All NSAIDs
60430	Salicylates
80935	COPY OF: Non-acetylated Salicylates
49361	Bupropions Ingredients
49362	Bupropions
86957	Topical Non-acetylated Salicylates
86958	History of Thromboembolic Events
\.


--
-- TOC entry 2422 (class 2606 OID 16896)
-- Name: pk_concept_set; Type: CONSTRAINT; Schema: ohdsi; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY concept_set
    ADD CONSTRAINT pk_concept_set PRIMARY KEY (concept_set_id);


-- Completed on 2019-09-13 08:44:57

--
-- PostgreSQL database dump complete
--

