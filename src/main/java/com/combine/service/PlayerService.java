package com.combine.service;

import com.combine.dao.CombineDao;
import com.combine.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

//    public void clearPlayersByYear(int year) {
//        this.combineDao.clearPlayersByYear(year);
//    }
//
//    public List<College> allColleges() {
//        if (collegesCache.size() == 0) {
//            this.collegesCache = this.combineDao.allColleges();
//        }
//        return this.collegesCache;
//    }

}