CREATE TABLE nfl."team"
(
    team_identifier text not null,
    team_name text,
    team_link text,
    from_year integer,
    to_year integer,
    CONSTRAINT team_pk PRIMARY KEY (team_identifier)
);

CREATE TABLE nfl."game"
(
    game_identifier text,
    game_link text,
    "date" date,
    game_number integer,
    week integer,
    "day" text,
    year_id integer,
    gametime text,
    local_time text,
    league text,
    result text,
    season_type text,
    home_team_identifier text,
    away_team_identifier text,
    home_score integer,
    away_score integer,
    overtime boolean,
    CONSTRAINT game_pk PRIMARY KEY (game_identifier),
    CONSTRAINT game_hometeam_fk FOREIGN KEY (home_team_identifier) references nfl.team(team_identifier),
    CONSTRAINT game_awayteam_fk FOREIGN KEY (away_team_identifier) references nfl.team(team_identifier)
);

CREATE TABLE nfl."game_team_stats"
(
	  id bigserial NOT NULL,
	  game_identifier text,
	  team_identifier text,
	  year_id integer,
	  gametime text,
	  local_time text,
	  overtime text,
	  pass_cmp integer,
	  pass_att integer,
	  pass_cmp_perc double precision,
	  pass_yds integer,
	  pass_td integer,
	  pass_int integer,
	  pass_sacked integer,
	  pass_sacked_yds integer,
	  pass_rating double precision,
	  rush_att integer,
	  rush_yds integer,
	  rush_yds_per_att double precision,
	  rush_td integer,
	  tot_yds integer,
	  plays_offense integer,
	  yds_per_play_offense double precision,
	  plays_defense integer,
	  yds_per_play_defense double precision,
	  turnovers integer,
	  time_of_poss text,
	  duration text,
	  penalties integer,
	  penalties_yds integer,
	  penalties_opp integer,
	  penalties_yds_opp integer,
	  first_down integer,
	  first_down_rush integer,
	  first_down_pass integer,
	  first_down_penalty integer,
	  third_down_att integer,
	  third_down_success double precision,
	  third_down_pct double precision,
	  fourth_down_att integer,
	  fourth_down_success double precision,
	  fourth_down_pct double precision,
	  quarter_1_score_tgl integer,
	  quarter_2_score_tgl integer,
	  quarter_3_score_tgl integer,
	  quarter_4_score_tgl integer,
	  quarter_1_score_opp integer,
	  quarter_2_score_opp integer,
	  quarter_3_score_opp integer,
	  quarter_4_score_opp integer,
	  half_1_score_tgl integer,
	  half_2_score_tgl integer,
	  half_1_score_opp integer,
	  half_2_score_opp integer,
	  surface text,
	  roof text,
	  temperature double precision,
	  team_score integer,
	  opponent_score integer,
	  opponent_identifier text,
    CONSTRAINT game_team_stats_pk PRIMARY KEY (id),
    CONSTRAINT gameteamstats_teamidentifier_fk FOREIGN KEY (team_identifier) references nfl.team(team_identifier),
    CONSTRAINT gameteamstats_game_identifier_fk FOREIGN KEY (game_identifier) references nfl.game(game_identifier)
);


CREATE TABLE nfl."game_play"
(
    id bigserial,
    game_identifier text,
    description text,
    yards_gained integer,
    play_type text,
    expected_pointsAfter float,
    expected_pointsBefore float,
    home_win_probability float,
    down integer,
    quarter integer,
    score_away integer,
    score_home integer,
    yards_to_go integer,
    location text,
    quarter_time_remaining text,
    CONSTRAINT game_play_pk PRIMARY KEY (id),
    CONSTRAINT game_play_game_identifier_fk FOREIGN KEY (game_identifier) references nfl.game(game_identifier)
);


CREATE TABLE nfl."game_scoring_play"
(
    id bigserial,
    game_identifier text,
    scoring_team text,
    home_team_score integer,
    quarter integer,
    time text,
    visiting_team_score integer,
    description text,
    CONSTRAINT gamescoringplay_pk PRIMARY KEY (id),
    CONSTRAINT gamescoringplay_game_identifier_fk FOREIGN KEY (game_identifier) references nfl.game(game_identifier),
    CONSTRAINT gamescoringplay_teamidentifier_fk FOREIGN KEY (scoring_team) references nfl.team(team_identifier)
);


CREATE TABLE nfl."game_rushing"
(
    id bigserial,
    rushingYardsPerAttempt float,
    rushingAttempts integer,
    rushingTouchdowns integer,
    rushingYards integer,
    name text,
    player_identifier text,
    game_identifier text,
    team_identifier text,
    team_score integer,
    opp_score integer,
    CONSTRAINT gamerushing_pk PRIMARY KEY (id),
    CONSTRAINT gamerushingfk FOREIGN KEY (team_identifier) references nfl.team(team_identifier),
    CONSTRAINT gamerushing_game_identifier_fk FOREIGN KEY (game_identifier) references nfl.game(game_identifier)
);


CREATE TABLE nfl."game_receiving"
(
    id bigserial,
    catchPercentage float,
    yardsPerReception float,
    yardsPerTarget float,
    receptions integer,
    targets integer,
    touchdowns integer,
    yards integer,
    name text,
    player_identifier text,
    game_identifier text,
    team_identifier text,
    team_score integer,
    opp_score integer,
    CONSTRAINT gamereceiving_pk PRIMARY KEY (id),
    CONSTRAINT gamereceivingfk FOREIGN KEY (team_identifier) references nfl.team(team_identifier),
    CONSTRAINT gamereceiving_game_identifier_fk FOREIGN KEY (game_identifier) references nfl.game(game_identifier)
);


CREATE TABLE nfl."game_defense"
(
    id bigserial,
    sacks float,
    assists integer,
    interceptions integer,
    interceptionTouchdowns integer,
    interceptionYards integer,
    tackles integer,
    name text,
    player_identifier text,
    game_identifier text,
    team_identifier text,
    team_score integer,
    opp_score integer,
    CONSTRAINT gamedefense_pk PRIMARY KEY (id),
    CONSTRAINT gamedefensefk FOREIGN KEY (team_identifier) references nfl.team(team_identifier),
    CONSTRAINT gamedefense_game_identifier_fk FOREIGN KEY (game_identifier) references nfl.game(game_identifier)
);

CREATE TABLE nfl."game_passing"
(
    id bigserial,
    adjustedYardsPerAttempt float,
    completionPercentage float,
    rating float,
    yardsPerAttempt float,
    attempts integer,
    completions integer,
    interceptions integer,
    sacks integer,
    sackYards integer,
    touchdowns integer,
    yards integer,
    player_identifier text,
    name text,
    game_identifier text,
    team_identifier text,
    team_score integer,
    opp_score integer,
    CONSTRAINT gamepassing_pk PRIMARY KEY (id),
    CONSTRAINT gamepassingfk FOREIGN KEY (team_identifier) references nfl.team(team_identifier),
    CONSTRAINT gamepassing_game_identifier_fk FOREIGN KEY (game_identifier) references nfl.game(game_identifier)
);
