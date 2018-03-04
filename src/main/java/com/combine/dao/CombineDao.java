package com.combine.dao;

import com.combine.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class CombineDao extends JdbcDaoSupport {

    private static final Logger logger = Logger.getLogger(CombineDao.class);

    private List<College> colleges = null;

    static final String PLAYER_COL_ID = "id";
    static final String PLAYER_COL_RANK = "rank";
    static final String PLAYER_COL_NAME = "name";
    static final String PLAYER_COL_HEIGHT = "height";
    static final String PLAYER_COL_WEIGHT = "weight";
    static final String PLAYER_COL_POSITION_RANK = "position_rank";
    static final String PLAYER_COL_PROJECTED_ROUND = "projected_round";
    static final String PLAYER_COL_YEAR_CLASS = "year_class";
    static final String PLAYER_COL_YEAR = "year";
    static final String PLAYER_COL_UUID = "import_uuid";

    private static final String DELETE_PLAYERS_BY_YEAR = "delete from player where year = ?";
    private static final String INSERT_PARTICIPANT = "INSERT INTO participant( id, firstname, lastname, position, height, weight, hands, overview, strengths, weaknesses, comparision, bottom_line, what_scouts_say, college, expert_grade, link, pick) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String INSERT_WORKOUTRESULT = "INSERT INTO workout_result(participant, result, workout) VALUES (?, ?, ?);";
    private static final String INSERT_CONFERENCE = "INSERT INTO conf (id, name) VALUES (?,?)";
    private static final String INSERT_COLLEGE = "INSERT INTO college (id, conf, name) VALUES (?,?,?)";
    private static final String INSERT_PLAYER = "INSERT INTO player (college, college_text, height, name, position, position_rank, projected_round, rank, weight, year_class, year, import_uuid, source) VALUES (?,?,?,?,public.fn_positionid_by_position(?),?,?,?,?,?,?,?,?)";
    private static final String UPDATE_PLAYER = "update player set position = public.fn_positionid_by_position(?), position_rank = ?, projected_round = ?, rank = ?, year_class = ?, year = ?, import_uuid = ?, source = ? where name = ?";

    private static final String FIND_PLAYER_BY_FULLNAME_AND_COLLEGE = "select p.* from player p " +
            "left outer join position pos on p.position = pos.id " +
            "left outer join college c on p.college = c.id " +
            "left outer join conf conf on c.conf = conf.id " +
            "where (lower(regexp_replace(p.name, '[^a-zA-Z]\\s{1}', '', 'g')) like :firstname and lower(regexp_replace(p.name, '[^a-zA-Z]', '', 'g')) like :lastname and lower(p.college_text) like :college)";

    private static final String FIND_PLAYER_BY_NAME = "select p.* from player p " +
            "left outer join position pos on p.position = pos.id " +
            "left outer join college c on p.college = c.id " +
            "left outer join conf conf on c.conf = conf.id " +
            "where (lower(regexp_replace(p.name, '[\\.,]', '', 'g')) like :firstname and lower(regexp_replace(p.name, '[^a-zA-Z]', '', 'g')) like :lastname)";

    private static final String FIND_PLAYER_BY_FIRSTNAME_POSITION_COLLEGE = "select p.* from player p " +
            "left outer join position pos on p.position = pos.id " +
            "left outer join college c on p.college = c.id " +
            "left outer join conf conf on c.conf = conf.id " +
            "where (lower(regexp_replace(p.name, '[\\.,]', '', 'g')) like :firstname and lower(pos.name) like :position and lower(p.college_text) like :college)";

    private static final String FIND_PLAYER_BY_LASTNAME_POSITION_COLLEGE = "select p.* from player p " +
            "left outer join position pos on p.position = pos.id " +
            "left outer join college c on p.college = c.id " +
            "left outer join conf conf on c.conf = conf.id " +
            "where (lower(regexp_replace(p.name, '[\\.,]', '', 'g')) like :lastname and lower(pos.name) like :position and lower(p.college_text) like :college)";

    private static final String FIND_PLAYER_BY_FIRSTNAME_POSITION_CONFERENCE = "select p.* from player p " +
            "left outer join position pos on p.position = pos.id " +
            "left outer join college c on p.college = c.id " +
            "left outer join conf conf on c.conf = conf.id " +
            "where (lower(regexp_replace(p.name, '[\\.,]', '', 'g')) like :firstname and lower(pos.name) like :position and lower(conf.name) like :conference)";

    private static final String FIND_PLAYER_BY_LASTNAME_POSITION_CONFERENCE = "select p.* from player p " +
            "left outer join position pos on p.position = pos.id " +
            "left outer join college c on p.college = c.id " +
            "left outer join conf conf on c.conf = conf.id " +
            "where (lower(regexp_replace(p.name, '[\\.,]', '', 'g')) like :lastname and lower(pos.name) like :position and lower(conf.name) like :conference)";

    private static final String[] FIND_BY_ATTRIBUTE_QUERIES = new String[]{FIND_PLAYER_BY_FULLNAME_AND_COLLEGE, FIND_PLAYER_BY_NAME, FIND_PLAYER_BY_FIRSTNAME_POSITION_COLLEGE, FIND_PLAYER_BY_LASTNAME_POSITION_COLLEGE, FIND_PLAYER_BY_FIRSTNAME_POSITION_CONFERENCE, FIND_PLAYER_BY_LASTNAME_POSITION_CONFERENCE};

    private static final String UPDATE_WORKOUT_RESULTS_PRE = "UPDATE player set ";

    private static final String UPDATE_WORKOUT_RESULTS = "UPDATE player set forty_yard_dash = ?, bench_press = ?, vertical_jump = ?, broad_jump = ?, " +
            "three_cone_drill = ?, twenty_yard_shuttle = ?, sixty_yard_shuttle = ?" +
            "where id = ?;";

    private static final String UPDATE_ARMLENGTH_HANDSIZE = "UPDATE player set hand_size = ?, arm_length = ? where id = ?;";

    private static final String UPDATE_DRAFT_PICK = "UPDATE player set round = ?, pick = ?, team = ? where id = ?";

    private static final RowMapper<Player> PLAYER_ID_ROW_MAPPER = new RowMapper<Player>() {

        @Override
        public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
            Player player = new Player();
            player.setId(rs.getInt("id"));
            player.setRound(rs.getInt("round"));
            return player;
        }

    };

    public CombineDao(@Autowired DataSource dataSource) {
        setDataSource(dataSource);
    }

    public List<Position> getPositions() {
        return this.getJdbcTemplate().query("select * from position", new GenericMapper<Position>(Position.class));
    }

    public List<Workout> getWorkouts() {
        return getJdbcTemplate().query("select * from workout", new GenericMapper<Workout>(Workout.class));
    }

    public void deleteWorkoutResult() {
        getJdbcTemplate().update("delete from workout_result");
    }

    public void deleteParticipant() {
        getJdbcTemplate().update("delete from participant");
    }

    public void insertParticipant(Participant participant) {
        getJdbcTemplate().update(INSERT_PARTICIPANT, new Object[]{participant.getId(), participant.getFirstname(), participant.getLastname(),
                participant.getPosition(), participant.getHeight(), participant.getWeight(), participant.getHands(),
                participant.getOverview(), participant.getStrengths(), participant.getWeaknesses(), participant.getComparision(),
                participant.getBottom_line(), participant.getWhatScoutsSay(), participant.getCollege(),
                participant.getExpertGrade(), participant.getLink(), participant.getPick()});
    }

    public void insertWorkoutResult(WorkoutResult workoutResult) {
        getJdbcTemplate().update(INSERT_WORKOUTRESULT, new Object[]{workoutResult.getParticipant(),
                workoutResult.getResult(), workoutResult.getWorkout()});
    }

    public void insertConference(Conference conference) {
        getJdbcTemplate().update(INSERT_CONFERENCE, new Object[]{conference.getId(),
                conference.getName()});
    }

    public void insertCollege(College college) {
        getJdbcTemplate().update(INSERT_COLLEGE, new Object[]{college.getId(), college.getConf(), college.getName()});
    }

    /**
     * insert a single player.
     *
     * @param player
     * @return 1 if inserted or updated, otherwise 0
     */
    public int insertPlayer(Player player) {
        try {
            return getJdbcTemplate().update(INSERT_PLAYER, new Object[]{player.getCollege(), player.getCollegeText(), player.getHeight(), player.getName(), player.getPosition(), player.getPositionRank(), player.getProjectedRound(), player.getRank(), player.getWeight(), player.getYearClass(), player.getYear(), player.getImportUUID(), player.getSource()});
        } catch (DuplicateKeyException e) {
            Player existingPlayer = this.getByNameAndYear(player);
            if (existingPlayer.getImportUUID() == null || !existingPlayer.getImportUUID().equals(player.getImportUUID())) {
                return this.updatePlayer(player);
            }
            return 0;
        }
    }

    public Player getByNameAndYear(Player player) {
        return getJdbcTemplate().queryForObject("select * from player where name = ?", new Object[]{player.getName()}, new RowMapper<Player>() {

            @Override
            public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
                Player player = new Player();
                player.setName(rs.getString(PLAYER_COL_NAME));
                player.setRank(rs.getInt(PLAYER_COL_RANK));
                player.setPositionRank(rs.getInt(PLAYER_COL_POSITION_RANK));
                player.setProjectedRound(rs.getString(PLAYER_COL_PROJECTED_ROUND));
                player.setYear(rs.getInt(PLAYER_COL_YEAR));
                player.setYearClass(PLAYER_COL_YEAR_CLASS);
                player.setImportUUID(rs.getString(PLAYER_COL_UUID));
                return player;
            }

        });
    }

    public int updatePlayer(Player player) {
        try {
            return getJdbcTemplate().update(UPDATE_PLAYER, new Object[]{player.getPosition(), player.getPositionRank(), player.getProjectedRound(), player.getRank(), player.getYearClass(), player.getYear(), player.getImportUUID(), player.getSource(), player.getName()});
        } catch (DuplicateKeyException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void clearPlayersByYear(int year) {
        getJdbcTemplate().update(DELETE_PLAYERS_BY_YEAR, new Object[]{year});
    }

    public List<College> allColleges() {
        return getJdbcTemplate().query("select * from college", new GenericMapper<College>(College.class));
    }

    public Player findByAttributes(String firstname, String lastname, String college, String conference,
                                   String position) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstname", firstname + "%");
        params.addValue("lastname", "%" + lastname + "%");
        params.addValue("college", "%" + college + "%");
        params.addValue("conference", "%" + conference + "%");
        params.addValue("position", "%" + position + "%");
        List<Player> playerResults = null;

        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getJdbcTemplate().getDataSource());

        try {

            for (int i = 0; i < FIND_BY_ATTRIBUTE_QUERIES.length; i++) {
                playerResults = template.query(FIND_BY_ATTRIBUTE_QUERIES[i], params, PLAYER_ID_ROW_MAPPER);
                if (playerResults != null && playerResults.size() > 0) {
                    if (playerResults.size() > 1) {
                        System.out.println("Multiple matches! " + firstname + ", " + lastname);
                        break;
                    }
                    return playerResults.get(0);
                }
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        System.out.println("No match found! " + firstname + ", " + lastname);
        return null;
    }

    public int updateWorkoutResults(Player player, Integer college, Double result, String fieldName) {
        String sql = UPDATE_WORKOUT_RESULTS_PRE + fieldName + " = ? where lower(name) = ? and year = ?";
        return getJdbcTemplate().update(sql, new Object[]{result, player.getName().toLowerCase(), player.getYear()});
    }

    public int updateWorkoutResults(Player player) {
        return getJdbcTemplate().update(UPDATE_WORKOUT_RESULTS, new Object[]{player.getFortyYardDash(), player.getBenchPress(),
                player.getVerticalJump(), player.getBroadJump(), player.getThreeConeDrill(), player.getTwentyYardShuttle(),
                player.getSixtyYardShuttle(), player.getId()});
    }

    public int updateArmLengthAndHandSize(Player player) {
        return getJdbcTemplate().update(UPDATE_ARMLENGTH_HANDSIZE, new Object[]{player.getHandSize(), player.getArmLength(), player.getId()});
    }

    public int updatePick(Player p) {
        return getJdbcTemplate().update(UPDATE_DRAFT_PICK, new Object[]{p.getRound(), p.getPick(), p.getTeam(), p.getId()});
    }

    public void init() {
        this.colleges = this.allColleges();
    }

    public List<College> getColleges() {
        if (this.colleges == null) {
            init();
        }
        return colleges;
    }

    public void addParserProgress(ParserProgressMessage event) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(getJdbcTemplate());
        // set the table name, primary key column name, and the array of column
        // names
        jdbcInsert.setTableName("public.parser_progress");
        jdbcInsert.setColumnNames(Arrays.asList("id", "username", "date", "started", "finished", "description", "progress"));

        // set the values to be inserted
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("id", event.getId());
        parameters.put("username", event.getUsername());
        parameters.put("date", new Date());
        parameters.put("started", event.getStarted());
        parameters.put("finished", event.getFinished());
        parameters.put("description", event.getDescription());
        parameters.put("progress", event.getProgress());

        // execute insert
        Number key = null;
        try {
            // perform the insert and return the key
            jdbcInsert.execute(new MapSqlParameterSource(
                    parameters));
        } catch (DuplicateKeyException e) {
            this.updateParserProgress(event);
        } catch (Exception e) {
            logger.warn("CombinaDao::addParserProgress -> Error adding parser object: " + e.toString());
        }
    }

    public Integer updateParserProgress(ParserProgressMessage event) {
        try {
            return getJdbcTemplate().update("update public.parser_progress set date = ?, description = ?, finished = ?, progress = ? where id = ?",
                    new Object[]{event.getDate(), event.getDescription(), event.getFinished(), event.getProgress(), event.getId()});
        } catch (DataAccessException e) {
            logger.warn("CombinaDao::updateParserProgress -> Error updating parser object: " + e.toString());
            return -1;
        }
    }

    public List<Conference> getConferences() {
        return getJdbcTemplate().query("select * from conf", new GenericMapper<Conference>(Conference.class));
    }
}
