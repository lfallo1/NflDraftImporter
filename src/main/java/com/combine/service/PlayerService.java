package com.combine.service;

import com.combine.dao.CombineDao;
import com.combine.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    @Autowired
    private CombineDao combineDao;

    public void clearDb() {
        this.combineDao.deleteWorkoutResult();
        this.combineDao.deleteParticipant();
    }

    /**
     * add list of players to db
     *
     * @param players
     * @return total number of players added
     */
    public int addPlayers(List<Player> players) {
        int count = 0;
        for (int i = 0; i < players.size(); i++) {
            count += this.combineDao.insertPlayer(players.get(i));
        }
        return count;
    }

    public void addParticipants(List<Participant> participants) {
        for (int i = 0; i < participants.size(); i++) {
            this.combineDao.insertParticipant(participants.get(i));
        }
    }

    public void addWorkoutResults(List<WorkoutResult> workoutResults) {
        for (int i = 0; i < workoutResults.size(); i++) {
            this.combineDao.insertWorkoutResult(workoutResults.get(i));
        }
    }

    public void addConferences(List<Conference> conferences) {
        for (int i = 0; i < conferences.size(); i++) {
            this.combineDao.insertConference(conferences.get(i));
        }
    }

    public void addColleges(List<College> colleges) {
        for (int i = 0; i < colleges.size(); i++) {
            this.combineDao.insertCollege(colleges.get(i));
        }
    }

    /**
     * find college by id
     *
     * @param college
     * @param colleges
     * @return
     */
    public College findCollegeById(Integer college, List<College> colleges) {
        return colleges.stream().filter(c -> c.getId() == college).findFirst().get();
    }

    //    public void clearPlayersByYear(int year) {
//        this.combineDao.clearPlayersByYear(year);
//    }
//
    public List<College> allColleges() {
        return this.combineDao.allColleges();
    }

    public Integer findPositionByName(String positionText, List<Position> positions) {
        Optional<Position> position = positions.stream().filter(p -> p.getName().equalsIgnoreCase(positionText)).findFirst();
        if (position.isPresent()) {
            return position.get().getId();
        }
        return -1;
    }

    public Conference getConferenceByCollegeId(College college, List<Conference> conferences) {
        Optional<Conference> conference = conferences.stream().filter(c -> c.getId() == college.getConf()).findFirst();
        if (conference.isPresent()) {
            return conference.get();
        }
        return null;
    }

    public College findCollegeByName(String collegeText, List<College> colleges) {
        Optional<College> college = colleges.stream().filter(c -> c.getName().equalsIgnoreCase(collegeText)).findFirst();
        if (college.isPresent()) {
            return college.get();
        }
        return null;
    }
}