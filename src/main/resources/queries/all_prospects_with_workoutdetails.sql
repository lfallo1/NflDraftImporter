select p.*, pos.name as "position", poscat.name as "position_category", c.name as "college", conf.name as "conference",
	MIN(forty.result) as "40-yd", 
	MAX(bench.result) as "bench",
	MAX(vert.result) as "vert",
	MAX(broad.result) as "broad",
	MIN(three.result) as "3-cone",
	MIN(twenty.result) as "20-yd-shuttle",
	MIN(sixty.result) as "60-yd-shuttle"
	from participant p
	left outer join college c on p.college = c.id
	left outer join conf conf on c.conf = conf.id
	left outer join position pos on p.position = pos.id
	left outer join position_category poscat on pos.category = poscat.id
	left outer join workout_result forty on p.id = forty.participant
	left outer join workout_result bench on p.id = bench.participant
	left outer join workout_result vert on p.id = vert.participant
	left outer join workout_result broad on p.id = broad.participant
	left outer join workout_result three on p.id = three.participant
	left outer join workout_result twenty on p.id = twenty.participant
	left outer join workout_result sixty on p.id = sixty.participant
	where (forty.workout = 1 or forty.workout is null) and (bench.workout = 2 or bench.workout is null) and (vert.workout = 3 or vert.workout is null)
	and (broad.workout = 4 or broad.workout is null) and (three.workout = 5 or three.workout is null) and (twenty.workout = 6 or twenty.workout is null) and (sixty.workout = 7 or sixty.workout is null)
	group by p.id, pos.name, c.name, poscat.name, conf.name