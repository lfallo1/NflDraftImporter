ALTER TABLE public.player ADD COLUMN import_uuid text;
update public.player set import_uuid = md5(random()::text || clock_timestamp()::text)::text;