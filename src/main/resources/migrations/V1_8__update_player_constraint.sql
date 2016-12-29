ALTER TABLE public.player DROP CONSTRAINT player_unique_name_yr;

ALTER TABLE public.player
    ADD CONSTRAINT player_unique_name_yr UNIQUE (name);