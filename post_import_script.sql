-- update players where position_rank = 0
-- all this does is sets the rank to the next highest value (grouped by year and position)
update player p
set position_rank = (
	with unranked as (
		select
			row_number() over(partition by pl.position, pl.year order by pl.id) as row_num, 
			(select (max(position_rank)) from player where position = pl.position and year = pl.year) as max_rank,
			pl.* 
		from 
			player pl
		where pl.position_rank = 0
	)
	select row_num + max_rank as new_rank
	from unranked
	where unranked.id = p.id
)
where p.position_rank = 0;