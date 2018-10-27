package com.combine.model;

import java.util.ArrayList;
import java.util.List;

public class DraftRefreshPayload {

    List<Player> players = new ArrayList<>();

    public DraftRefreshPayload(){}

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
