--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.16
-- Dumped by pg_dump version 9.3.16
-- Started on 2019-04-29 10:30:18

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
-- TOC entry 367 (class 1259 OID 24940)
-- Name: sig_mapping; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sig_mapping (
    id integer NOT NULL,
    sig character varying(200),
    expected integer,
    min integer,
    max integer
);


ALTER TABLE public.sig_mapping OWNER TO postgres;

--
-- TOC entry 366 (class 1259 OID 24938)
-- Name: sig_mapping_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sig_mapping_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sig_mapping_id_seq OWNER TO postgres;

--
-- TOC entry 2534 (class 0 OID 0)
-- Dependencies: 366
-- Name: sig_mapping_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sig_mapping_id_seq OWNED BY sig_mapping.id;


--
-- TOC entry 2418 (class 2604 OID 24943)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sig_mapping ALTER COLUMN id SET DEFAULT nextval('sig_mapping_id_seq'::regclass);


--
-- TOC entry 2529 (class 0 OID 24940)
-- Dependencies: 367
-- Data for Name: sig_mapping; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sig_mapping (id, sig, expected, min, max) FROM stdin;
1	2 times daily	2	2	2
2	2 times daily before meals	2	2	2
3	2 times daily PRN	0	0	2
4	2 times daily with meals	2	2	2
5	3 times daily	3	3	3
6	3 times daily before meals	3	3	3
7	3 times daily before meals PRN	0	0	3
8	3 times daily PRN	0	0	3
9	3 times daily with meals	3	3	3
10	30 min pre-op	1	1	1
11	4 times daily	4	4	4
12	4 times daily after meals and nightly	4	4	4
13	4 times daily before meals and nightly	4	4	4
14	4 times daily PRN	0	0	4
15	4 times daily while awake	4	4	4
16	5 times daily	5	5	5
17	60 min pre-op	1	1	1
18	After breakfast	1	1	1
19	After breakfast and dinner	2	2	2
20	After dialysis	0	0	1
21	After Every Dialysis	0	0	1
22	At bedside	1	1	1
23	Before breakfast	1	1	1
24	Before breakfast and dinner	2	2	2
25	Before breakfast and dinner PRN	0	0	2
26	Before dialysis	0	0	1
27	Before dinner	1	1	1
28	Before meals and nightly PRN	0	0	4
29	Code/trauma continuous med	1	1	1
30	Code/trauma medication	1	1	1
31	Continuous	1	1	4
32	Continuous PRN	1	1	4
33	Continuous TPN	1	1	1
34	Cyclic TPN - see admin instructions	1	1	1
35	Daily	1	1	1
36	Daily at 1800	1	1	1
37	Daily at 2000	1	1	1
38	Daily before lunch	1	1	1
39	Daily PRN	0	0	1
40	Daily with breakfast	1	1	1
41	Daily with dinner	1	1	1
42	Daily with lunch	1	1	1
43	Every 1 hour PRN	0	6	24
44	Every 1 min	1	6	24
45	Every 10 min	6	6	6
46	Every 10 min PRN	0	0	144
47	Every 12 hours	2	2	2
48	Every 12 hours PRN	0	0	2
49	Every 12 hours scheduled	2	2	2
50	Every 14 days	0	0	1
51	Every 15 min	4	4	92
52	Every 15 min PRN	0	0	\N
53	every 2 hours	12	12	12
54	Every 2 hours	12	12	12
55	Every 2 hours PRN	0	0	12
56	Every 2 hours while awake	8	8	12
57	Every 2 minutes PRN	0	0	720
58	Every 20 hours	1	1	1
59	Every 20 min	3	3	72
60	Every 20 min PRN	0	0	72
61	Every 21 days	0	0	1
62	Every 24 hours	1	1	1
63	Every 24 hours scheduled	1	1	1
64	Every 28 days	0	0	1
65	Every 3 hours	8	8	8
66	Every 3 hours PRN	0	0	8
67	Every 30 days	0	0	1
68	Every 30 min	2	2	2
69	Every 30 min PRN	0	0	2
70	Every 4 hours	6	6	6
71	Every 4 hours PRN	0	0	6
72	Every 4 hours scheduled	6	6	6
73	Every 4 hours while awake	6	6	6
74	Every 48 hours	0	0	1
75	Every 48 hours PRN	0	0	1
76	Every 5 hours	5	5	5
77	Every 5 min	12	12	480
78	Every 5 min PRN	0	0	480
79	Every 6 hours	4	4	4
80	Every 6 hours PRN	0	0	4
81	Every 6 hours sched	4	4	4
82	Every 6 hours scheduled	4	4	4
83	Every 6 hours while awake	4	4	4
84	Every 7 days	0	0	1
85	Every 72 hours	0	0	1
86	Every 8 hours	3	3	3
87	Every 8 hours PRN	0	0	3
88	Every 8 hours scheduled	3	3	3
89	Every evening	1	1	1
90	Every evening PRN	0	0	1
91	Every hour	24	24	24
92	Every Mon-Wed-Fri-Sun at 2000	0	0	1
93	Every Mon-Wed-Fri at 0900	0	0	1
94	Every Mon-Wed-Fri at 2000	0	0	1
95	Every morning	1	1	1
96	Every morning before breakfast	1	1	1
97	Every other day	0	0	1
98	Every shift	2	1	2
99	Every Tue-Thur-Sat-Sun at 2000	0	0	1
100	every Tue, Thu, Sat and Sun at 0900	0	0	1
101	Every Tue,Thu,Sat at 2000	0	0	1
102	Five times daily PRN	0	0	5
103	Nightly	1	1	1
104	Nightly - one time	1	1	1
105	Nightly may repeat once PRN	1	1	2
106	Nightly PRN	0	0	1
107	On call	1	0	1
108	Once	1	1	1
109	Once - Respiratory	1	1	1
110	Once at 2000	1	1	1
111	Once in dialysis	1	1	1
112	Once PRN	0	0	1
113	Once this morning	1	1	1
114	Once tonight	1	1	1
115	PRN	0	0	6
116	Select Days at 2000	0	0	1
117	Six times daily PRN	0	0	6
118	Three times daily around food	3	3	3
119	Three times weekly	0	0	3
120	Titrated	1	1	4
121	User specified	0	0	4
122	Weekly	0	0	1
123	With meals and nightly	4	4	4
\.


--
-- TOC entry 2535 (class 0 OID 0)
-- Dependencies: 366
-- Name: sig_mapping_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('sig_mapping_id_seq', 1, false);


--
-- TOC entry 2420 (class 2606 OID 24945)
-- Name: sig_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sig_mapping
    ADD CONSTRAINT sig_mapping_pkey PRIMARY KEY (id);


-- Completed on 2019-04-29 10:30:18

--
-- PostgreSQL database dump complete
--

