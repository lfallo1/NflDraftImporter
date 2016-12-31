alter table player drop column college;
alter table player add column college bigint;
alter table player ADD CONSTRAINT college_fk FOREIGN KEY (college)
    REFERENCES public.college (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;