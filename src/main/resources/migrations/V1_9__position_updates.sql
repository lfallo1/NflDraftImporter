-- create position_side_of_ball table
CREATE TABLE public.position_side_of_ball
(
    id bigint NOT NULL,
    name text NOT NULL,
    PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;
ALTER TABLE public.position_side_of_ball
    OWNER to postgres;

-- add data to the position_side_of_ball table
insert into position_side_of_ball (id, name) values (1,'offense'), (2,'defense'), (3,'special teams');

-- update player & position category table
alter table player drop column position;
alter table player add column position bigint;
alter table player ADD CONSTRAINT player_position_fk FOREIGN KEY (position)
    REFERENCES public.position (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

alter table position_category add column position_side_of_ball integer;

update position_category set position_side_of_ball = 1 where name in ('QB', 'RB', 'WR', 'TE', 'OL');
update position_category set position_side_of_ball = 2 where name in ('DL', 'LB', 'CB', 'S');
update position_category set position_side_of_ball = 3 where name in ('ST');

alter table position_category ADD CONSTRAINT position_category_fk FOREIGN KEY (position_side_of_ball)
    REFERENCES public.position_side_of_ball (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;
    
-- add function
create or replace function fn_positionid_by_position(position_name text)
returns bigint
as
$$
select id from position where name = position_name;
$$ LANGUAGE sql;