--
-- PostgreSQL database dump
--

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
-- Name: participant; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE participant (
    id bigint NOT NULL,
    firstname text,
    lastname text,
    "position" bigint,
    height integer,
    weight integer,
    hands double precision,
    overview text,
    strengths text,
    weaknesses text,
    comparision text,
    bottom_line text,
    what_scouts_say text
);


ALTER TABLE participant OWNER TO postgres;

--
-- Name: position; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE "position" (
    id bigint NOT NULL,
    name text,
    category bigint
);


ALTER TABLE "position" OWNER TO postgres;

--
-- Name: position_category; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE position_category (
    id bigint NOT NULL,
    name text
);


ALTER TABLE position_category OWNER TO postgres;

--
-- Name: position_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE position_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE position_category_id_seq OWNER TO postgres;

--
-- Name: position_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE position_category_id_seq OWNED BY position_category.id;


--
-- Name: position_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE position_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE position_id_seq OWNER TO postgres;

--
-- Name: position_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE position_id_seq OWNED BY "position".id;


--
-- Name: workout; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE workout (
    id bigint NOT NULL,
    name text
);


ALTER TABLE workout OWNER TO postgres;

--
-- Name: workout_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE workout_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE workout_id_seq OWNER TO postgres;

--
-- Name: workout_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE workout_id_seq OWNED BY workout.id;


--
-- Name: workout_result; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE workout_result (
    id bigint NOT NULL,
    participant bigint,
    result double precision,
    workout bigint
);


ALTER TABLE workout_result OWNER TO postgres;

--
-- Name: workout_result_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE workout_result_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE workout_result_id_seq OWNER TO postgres;

--
-- Name: workout_result_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE workout_result_id_seq OWNED BY workout_result.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "position" ALTER COLUMN id SET DEFAULT nextval('position_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY position_category ALTER COLUMN id SET DEFAULT nextval('position_category_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY workout ALTER COLUMN id SET DEFAULT nextval('workout_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY workout_result ALTER COLUMN id SET DEFAULT nextval('workout_result_id_seq'::regclass);


--
-- Data for Name: participant; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: position; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO "position" (id, name, category) VALUES (1, 'QB', 1);
INSERT INTO "position" (id, name, category) VALUES (2, 'RB', 2);
INSERT INTO "position" (id, name, category) VALUES (3, 'FB', 2);
INSERT INTO "position" (id, name, category) VALUES (4, 'WR', 3);
INSERT INTO "position" (id, name, category) VALUES (5, 'TE', 4);
INSERT INTO "position" (id, name, category) VALUES (6, 'OG', 5);
INSERT INTO "position" (id, name, category) VALUES (7, 'OT', 5);
INSERT INTO "position" (id, name, category) VALUES (8, 'C', 5);
INSERT INTO "position" (id, name, category) VALUES (9, 'DE', 6);
INSERT INTO "position" (id, name, category) VALUES (10, 'DT', 6);
INSERT INTO "position" (id, name, category) VALUES (11, 'NT', 6);
INSERT INTO "position" (id, name, category) VALUES (12, 'ILB', 7);
INSERT INTO "position" (id, name, category) VALUES (13, 'OLB', 7);
INSERT INTO "position" (id, name, category) VALUES (14, 'CB', 8);
INSERT INTO "position" (id, name, category) VALUES (15, 'S', 9);
INSERT INTO "position" (id, name, category) VALUES (16, 'FS', 9);
INSERT INTO "position" (id, name, category) VALUES (17, 'SS', 9);
INSERT INTO "position" (id, name, category) VALUES (18, 'K', 10);
INSERT INTO "position" (id, name, category) VALUES (19, 'P', 10);
INSERT INTO "position" (id, name, category) VALUES (20, 'LS', 10);


--
-- Data for Name: position_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO position_category (id, name) VALUES (1, 'QB');
INSERT INTO position_category (id, name) VALUES (2, 'RB');
INSERT INTO position_category (id, name) VALUES (3, 'WR');
INSERT INTO position_category (id, name) VALUES (10, 'ST');
INSERT INTO position_category (id, name) VALUES (9, 'S');
INSERT INTO position_category (id, name) VALUES (8, 'CB');
INSERT INTO position_category (id, name) VALUES (7, 'LB');
INSERT INTO position_category (id, name) VALUES (6, 'DL');
INSERT INTO position_category (id, name) VALUES (5, 'OL');
INSERT INTO position_category (id, name) VALUES (4, 'TE');


--
-- Name: position_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('position_category_id_seq', 10, true);


--
-- Name: position_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('position_id_seq', 20, true);


--
-- Data for Name: workout; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO workout (id, name) VALUES (1, 'FORTY_YARD_DASH');
INSERT INTO workout (id, name) VALUES (2, 'BENCH_PRESS');
INSERT INTO workout (id, name) VALUES (3, 'VERTICAL_JUMP');
INSERT INTO workout (id, name) VALUES (4, 'BROAD_JUMP');
INSERT INTO workout (id, name) VALUES (5, 'THREE_CONE_DRILL');
INSERT INTO workout (id, name) VALUES (6, 'TWENTY_YARD_SHUTTLE');
INSERT INTO workout (id, name) VALUES (7, 'SIXTY_YARD_SHUTTLE');


--
-- Name: workout_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('workout_id_seq', 7, true);


--
-- Data for Name: workout_result; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: workout_result_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('workout_result_id_seq', 1, false);


--
-- Name: participant_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY participant
    ADD CONSTRAINT participant_pk PRIMARY KEY (id);


--
-- Name: position_category_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY position_category
    ADD CONSTRAINT position_category_pk PRIMARY KEY (id);


--
-- Name: position_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY "position"
    ADD CONSTRAINT position_pk PRIMARY KEY (id);


--
-- Name: workout_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY workout
    ADD CONSTRAINT workout_pk PRIMARY KEY (id);


--
-- Name: workout_result_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY workout_result
    ADD CONSTRAINT workout_result_pk PRIMARY KEY (id);


--
-- Name: fki_participant_position_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_participant_position_pk ON participant USING btree ("position");


--
-- Name: fki_position_category_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_position_category_fk ON "position" USING btree (category);


--
-- Name: fki_workout_result_participant_pk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_workout_result_participant_pk ON workout_result USING btree (participant);


--
-- Name: fki_workout_result_workout_fk; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX fki_workout_result_workout_fk ON workout_result USING btree (workout);


--
-- Name: participant_position_pk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY participant
    ADD CONSTRAINT participant_position_pk FOREIGN KEY ("position") REFERENCES "position"(id);


--
-- Name: position_category_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "position"
    ADD CONSTRAINT position_category_fk FOREIGN KEY (category) REFERENCES position_category(id);


--
-- Name: workout_result_participant_pk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY workout_result
    ADD CONSTRAINT workout_result_participant_pk FOREIGN KEY (participant) REFERENCES participant(id);


--
-- Name: workout_result_workout_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY workout_result
    ADD CONSTRAINT workout_result_workout_fk FOREIGN KEY (workout) REFERENCES workout(id);


--
-- PostgreSQL database dump complete
--

