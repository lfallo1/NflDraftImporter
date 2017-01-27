package com.combine.profootballref.weekly.dto;

import java.util.ArrayList;
import java.util.List;

import com.combine.annotations.StatField;

public class WeeklyStatsGame extends WeeklyStats {

	@StatField("year_id")
	private Integer year_id;
	@StatField("gametime")
	private String gametime;
	@StatField("local_time")
	private String local_time;
	@StatField("overtime")
	private String overtime;
	@StatField("pass_cmp")
	private Integer pass_cmp;
	@StatField("pass_att")
	private Integer pass_att;
	@StatField("pass_cmp_perc")
	private Double pass_cmp_perc;
	@StatField("pass_yds")
	private Integer pass_yds;
	@StatField("pass_td")
	private Integer pass_td;
	@StatField("pass_int")
	private Integer pass_int;
	@StatField("pass_sacked")
	private Integer pass_sacked;
	@StatField("pass_sacked_yds")
	private Integer pass_sacked_yds;
	@StatField("pass_rating")
	private Double pass_rating;
	@StatField("rush_att")
	private Integer rush_att;
	@StatField("rush_yds")
	private Integer rush_yds;
	@StatField("rush_yds_per_att")
	private Double rush_yds_per_att;
	@StatField("rush_td")
	private Integer rush_td;
	@StatField("tot_yds")
	private Integer tot_yds;
	@StatField("plays_offense")
	private Integer plays_offense;
	@StatField("yds_per_play_offense")
	private Double yds_per_play_offense;
	@StatField("plays_defense")
	private Integer plays_defense;
	@StatField("yds_per_play_defense")
	private Double yds_per_play_defense;
	@StatField("turnovers")
	private Integer turnovers;
	@StatField("time_of_poss")
	private String time_of_poss;
	@StatField("duration")
	private String duration;
	@StatField("penalties")
	private Integer penalties;
	@StatField("penalties_yds")
	private Integer penalties_yds;
	@StatField("penalties_opp")
	private Integer penalties_opp;
	@StatField("penalties_yds_opp")
	private Integer penalties_yds_opp;
	@StatField("first_down")
	private Integer first_down;
	@StatField("first_down_rush")
	private Integer first_down_rush;
	@StatField("first_down_pass")
	private Integer first_down_pass;
	@StatField("first_down_penalty")
	private Integer first_down_penalty;
	@StatField("third_down_att")
	private Integer third_down_att;
	@StatField("third_down_success")
	private Double third_down_success;
	@StatField("third_down_pct")
	private Double third_down_pct;
	@StatField("fourth_down_att")
	private Integer fourth_down_att;
	@StatField("fourth_down_success")
	private Double fourth_down_success;
	@StatField("fourth_down_pct")
	private Double fourth_down_pct;
	@StatField("quarter_1_score_tgl")
	private Integer quarter_1_score_tgl;
	@StatField("quarter_2_score_tgl")
	private Integer quarter_2_score_tgl;
	@StatField("quarter_3_score_tgl")
	private Integer quarter_3_score_tgl;
	@StatField("quarter_4_score_tgl")
	private Integer quarter_4_score_tgl;
	@StatField("quarter_1_score_opp")
	private Integer quarter_1_score_opp;
	@StatField("quarter_2_score_opp")
	private Integer quarter_2_score_opp;
	@StatField("quarter_3_score_opp")
	private Integer quarter_3_score_opp;
	@StatField("quarter_4_score_opp")
	private Integer quarter_4_score_opp;
	@StatField("half_1_score_tgl")
	private Integer half_1_score_tgl;
	@StatField("half_2_score_tgl")
	private Integer half_2_score_tgl;
	@StatField("half_1_score_opp")
	private Integer half_1_score_opp;
	@StatField("half_2_score_opp")
	private Integer half_2_score_opp;
	@StatField("surface")
	private String surface;
	@StatField("roof")
	private String roof;
	@StatField("temperature")
	private Double temperature;

	private List<WeeklyStatsIndividualPlay> plays = new ArrayList<>();
	private List<GameScoringPlay> scoringSummary = new ArrayList<>();

	public Integer getYear_id() {
		return year_id;
	}

	public void setYear_id(Integer year_id) {
		this.year_id = year_id;
	}

	public String getGametime() {
		return gametime;
	}

	public void setGametime(String gametime) {
		this.gametime = gametime;
	}

	public String getLocal_time() {
		return local_time;
	}

	public void setLocal_time(String local_time) {
		this.local_time = local_time;
	}

	public String getOvertime() {
		return overtime;
	}

	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}

	public Integer getPass_cmp() {
		return pass_cmp;
	}

	public void setPass_cmp(Integer pass_cmp) {
		this.pass_cmp = pass_cmp;
	}

	public Integer getPass_att() {
		return pass_att;
	}

	public void setPass_att(Integer pass_att) {
		this.pass_att = pass_att;
	}

	public Double getPass_cmp_perc() {
		return pass_cmp_perc;
	}

	public void setPass_cmp_perc(Double pass_cmp_perc) {
		this.pass_cmp_perc = pass_cmp_perc;
	}

	public Integer getPass_yds() {
		return pass_yds;
	}

	public void setPass_yds(Integer pass_yds) {
		this.pass_yds = pass_yds;
	}

	public Integer getPass_td() {
		return pass_td;
	}

	public void setPass_td(Integer pass_td) {
		this.pass_td = pass_td;
	}

	public Integer getPass_int() {
		return pass_int;
	}

	public void setPass_int(Integer pass_int) {
		this.pass_int = pass_int;
	}

	public Integer getPass_sacked() {
		return pass_sacked;
	}

	public void setPass_sacked(Integer pass_sacked) {
		this.pass_sacked = pass_sacked;
	}

	public Integer getPass_sacked_yds() {
		return pass_sacked_yds;
	}

	public void setPass_sacked_yds(Integer pass_sacked_yds) {
		this.pass_sacked_yds = pass_sacked_yds;
	}

	public Double getPass_rating() {
		return pass_rating;
	}

	public void setPass_rating(Double pass_rating) {
		this.pass_rating = pass_rating;
	}

	public Integer getRush_att() {
		return rush_att;
	}

	public void setRush_att(Integer rush_att) {
		this.rush_att = rush_att;
	}

	public Integer getRush_yds() {
		return rush_yds;
	}

	public void setRush_yds(Integer rush_yds) {
		this.rush_yds = rush_yds;
	}

	public Double getRush_yds_per_att() {
		return rush_yds_per_att;
	}

	public void setRush_yds_per_att(Double rush_yds_per_att) {
		this.rush_yds_per_att = rush_yds_per_att;
	}

	public Integer getRush_td() {
		return rush_td;
	}

	public void setRush_td(Integer rush_td) {
		this.rush_td = rush_td;
	}

	public Integer getTot_yds() {
		return tot_yds;
	}

	public void setTot_yds(Integer tot_yds) {
		this.tot_yds = tot_yds;
	}

	public Integer getPlays_offense() {
		return plays_offense;
	}

	public void setPlays_offense(Integer plays_offense) {
		this.plays_offense = plays_offense;
	}

	public Double getYds_per_play_offense() {
		return yds_per_play_offense;
	}

	public void setYds_per_play_offense(Double yds_per_play_offense) {
		this.yds_per_play_offense = yds_per_play_offense;
	}

	public Integer getPlays_defense() {
		return plays_defense;
	}

	public void setPlays_defense(Integer plays_defense) {
		this.plays_defense = plays_defense;
	}

	public Double getYds_per_play_defense() {
		return yds_per_play_defense;
	}

	public void setYds_per_play_defense(Double yds_per_play_defense) {
		this.yds_per_play_defense = yds_per_play_defense;
	}

	public Integer getTurnovers() {
		return turnovers;
	}

	public void setTurnovers(Integer turnovers) {
		this.turnovers = turnovers;
	}

	public String getTime_of_poss() {
		return time_of_poss;
	}

	public void setTime_of_poss(String time_of_poss) {
		this.time_of_poss = time_of_poss;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Integer getPenalties() {
		return penalties;
	}

	public void setPenalties(Integer penalties) {
		this.penalties = penalties;
	}

	public Integer getPenalties_yds() {
		return penalties_yds;
	}

	public void setPenalties_yds(Integer penalties_yds) {
		this.penalties_yds = penalties_yds;
	}

	public Integer getPenalties_opp() {
		return penalties_opp;
	}

	public void setPenalties_opp(Integer penalties_opp) {
		this.penalties_opp = penalties_opp;
	}

	public Integer getPenalties_yds_opp() {
		return penalties_yds_opp;
	}

	public void setPenalties_yds_opp(Integer penalties_yds_opp) {
		this.penalties_yds_opp = penalties_yds_opp;
	}

	public Integer getFirst_down() {
		return first_down;
	}

	public void setFirst_down(Integer first_down) {
		this.first_down = first_down;
	}

	public Integer getFirst_down_rush() {
		return first_down_rush;
	}

	public void setFirst_down_rush(Integer first_down_rush) {
		this.first_down_rush = first_down_rush;
	}

	public Integer getFirst_down_pass() {
		return first_down_pass;
	}

	public void setFirst_down_pass(Integer first_down_pass) {
		this.first_down_pass = first_down_pass;
	}

	public Integer getFirst_down_penalty() {
		return first_down_penalty;
	}

	public void setFirst_down_penalty(Integer first_down_penalty) {
		this.first_down_penalty = first_down_penalty;
	}

	public Integer getThird_down_att() {
		return third_down_att;
	}

	public void setThird_down_att(Integer third_down_att) {
		this.third_down_att = third_down_att;
	}

	public Double getThird_down_success() {
		return third_down_success;
	}

	public void setThird_down_success(Double third_down_success) {
		this.third_down_success = third_down_success;
	}

	public Double getThird_down_pct() {
		return third_down_pct;
	}

	public void setThird_down_pct(Double third_down_pct) {
		this.third_down_pct = third_down_pct;
	}

	public Integer getFourth_down_att() {
		return fourth_down_att;
	}

	public void setFourth_down_att(Integer fourth_down_att) {
		this.fourth_down_att = fourth_down_att;
	}

	public Double getFourth_down_success() {
		return fourth_down_success;
	}

	public void setFourth_down_success(Double fourth_down_success) {
		this.fourth_down_success = fourth_down_success;
	}

	public Double getFourth_down_pct() {
		return fourth_down_pct;
	}

	public void setFourth_down_pct(Double fourth_down_pct) {
		this.fourth_down_pct = fourth_down_pct;
	}

	public Integer getQuarter_1_score_tgl() {
		return quarter_1_score_tgl;
	}

	public void setQuarter_1_score_tgl(Integer quarter_1_score_tgl) {
		this.quarter_1_score_tgl = quarter_1_score_tgl;
	}

	public Integer getQuarter_2_score_tgl() {
		return quarter_2_score_tgl;
	}

	public void setQuarter_2_score_tgl(Integer quarter_2_score_tgl) {
		this.quarter_2_score_tgl = quarter_2_score_tgl;
	}

	public Integer getQuarter_3_score_tgl() {
		return quarter_3_score_tgl;
	}

	public void setQuarter_3_score_tgl(Integer quarter_3_score_tgl) {
		this.quarter_3_score_tgl = quarter_3_score_tgl;
	}

	public Integer getQuarter_4_score_tgl() {
		return quarter_4_score_tgl;
	}

	public void setQuarter_4_score_tgl(Integer quarter_4_score_tgl) {
		this.quarter_4_score_tgl = quarter_4_score_tgl;
	}

	public Integer getQuarter_1_score_opp() {
		return quarter_1_score_opp;
	}

	public void setQuarter_1_score_opp(Integer quarter_1_score_opp) {
		this.quarter_1_score_opp = quarter_1_score_opp;
	}

	public Integer getQuarter_2_score_opp() {
		return quarter_2_score_opp;
	}

	public void setQuarter_2_score_opp(Integer quarter_2_score_opp) {
		this.quarter_2_score_opp = quarter_2_score_opp;
	}

	public Integer getQuarter_3_score_opp() {
		return quarter_3_score_opp;
	}

	public void setQuarter_3_score_opp(Integer quarter_3_score_opp) {
		this.quarter_3_score_opp = quarter_3_score_opp;
	}

	public Integer getQuarter_4_score_opp() {
		return quarter_4_score_opp;
	}

	public void setQuarter_4_score_opp(Integer quarter_4_score_opp) {
		this.quarter_4_score_opp = quarter_4_score_opp;
	}

	public Integer getHalf_1_score_tgl() {
		return half_1_score_tgl;
	}

	public void setHalf_1_score_tgl(Integer half_1_score_tgl) {
		this.half_1_score_tgl = half_1_score_tgl;
	}

	public Integer getHalf_2_score_tgl() {
		return half_2_score_tgl;
	}

	public void setHalf_2_score_tgl(Integer half_2_score_tgl) {
		this.half_2_score_tgl = half_2_score_tgl;
	}

	public Integer getHalf_1_score_opp() {
		return half_1_score_opp;
	}

	public void setHalf_1_score_opp(Integer half_1_score_opp) {
		this.half_1_score_opp = half_1_score_opp;
	}

	public Integer getHalf_2_score_opp() {
		return half_2_score_opp;
	}

	public void setHalf_2_score_opp(Integer half_2_score_opp) {
		this.half_2_score_opp = half_2_score_opp;
	}

	public String getSurface() {
		return surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}

	public String getRoof() {
		return roof;
	}

	public void setRoof(String roof) {
		this.roof = roof;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public List<WeeklyStatsIndividualPlay> getPlays() {
		return plays;
	}

	public void setPlays(List<WeeklyStatsIndividualPlay> plays) {
		this.plays = plays;
	}

	public List<GameScoringPlay> getScoringSummary() {
		return scoringSummary;
	}

	public void setScoringSummary(List<GameScoringPlay> scoringSummary) {
		this.scoringSummary = scoringSummary;
	}

}
