DROP TABLE workout_result;
DROP TABLE participant;

CREATE TABLE conf
(
  id bigint NOT NULL,
  name text,
  CONSTRAINT conf_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE conf
  OWNER TO postgres;

CREATE TABLE college
(
  id bigint NOT NULL,
  name text,
  conf bigint,
  CONSTRAINT college_pk PRIMARY KEY (id),
  CONSTRAINT college_conf_fk FOREIGN KEY (conf)
      REFERENCES conf (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE college
  OWNER TO postgres;

CREATE TABLE participant
(
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
  what_scouts_say text,
  college bigint,
  expert_grade double precision,
  CONSTRAINT participant_pk PRIMARY KEY (id),
  CONSTRAINT participant_college_fk FOREIGN KEY (college)
      REFERENCES college (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT participant_position_pk FOREIGN KEY ("position")
      REFERENCES "position" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE participant
  OWNER TO postgres;

  CREATE TABLE workout_result
(
  id bigserial NOT NULL,
  participant bigint,
  result double precision,
  workout bigint,
  CONSTRAINT workout_result_pk PRIMARY KEY (id),
  CONSTRAINT workout_result_participant_pk FOREIGN KEY (participant)
      REFERENCES participant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT workout_result_workout_fk FOREIGN KEY (workout)
      REFERENCES workout (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE workout_result
  OWNER TO postgres;