--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.16
-- Dumped by pg_dump version 9.3.16
-- Started on 2019-04-29 12:49:01

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 288 (class 1259 OID 16813)
-- Name: observation_period; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE observation_period (
    observation_period_id integer NOT NULL,
    person_id integer NOT NULL,
    observation_period_start_date date NOT NULL,
    observation_period_end_date date NOT NULL,
    period_type_concept_id integer NOT NULL
);


ALTER TABLE public.observation_period OWNER TO postgres;

--
-- TOC entry 2530 (class 0 OID 16813)
-- Dependencies: 288
-- Data for Name: observation_period; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY observation_period (observation_period_id, person_id, observation_period_start_date, observation_period_end_date, period_type_concept_id) FROM stdin;
\.


--
-- TOC entry 2420 (class 2606 OID 16978)
-- Name: xpk_observation_period; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY observation_period
    ADD CONSTRAINT xpk_observation_period PRIMARY KEY (observation_period_id);


--
-- TOC entry 2418 (class 1259 OID 17067)
-- Name: idx_observation_period_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX idx_observation_period_id ON observation_period USING btree (person_id);

ALTER TABLE observation_period CLUSTER ON idx_observation_period_id;


--
-- TOC entry 2421 (class 2606 OID 17525)
-- Name: fpk_observation_period_concept; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY observation_period
    ADD CONSTRAINT fpk_observation_period_concept FOREIGN KEY (period_type_concept_id) REFERENCES concept(concept_id);


--
-- TOC entry 2422 (class 2606 OID 17530)
-- Name: fpk_observation_period_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY observation_period
    ADD CONSTRAINT fpk_observation_period_person FOREIGN KEY (person_id) REFERENCES person(person_id);


-- Completed on 2019-04-29 12:49:01

--
-- PostgreSQL database dump complete
--

