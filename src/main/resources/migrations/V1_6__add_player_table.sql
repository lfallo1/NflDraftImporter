CREATE TABLE player
(
  rank integer,
  name text,
  college text,
  height double precision,
  weight double precision,
  position_rank integer,
  projected_round text,
  year_class text, -- 
  id bigserial NOT NULL,
  "position" text,
  year integer
)
WITH (
  OIDS=FALSE
);