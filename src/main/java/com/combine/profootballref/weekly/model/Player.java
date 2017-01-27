package com.combine.profootballref.weekly.model;

public class Player {

	private String playerIdentifier;
	private String playerName;
	private String playerLink;

	public Player() {
	}

	public Player(String playerIdentifier, String playerName, String playerLink) {
		this.playerIdentifier = playerIdentifier;
		this.playerName = playerName;
		this.playerLink = playerLink;
	}

	public String getPlayerIdentifier() {
		return playerIdentifier;
	}

	public void setPlayerIdentifier(String playerIdentifier) {
		this.playerIdentifier = playerIdentifier;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerLink() {
		return playerLink;
	}

	public void setPlayerLink(String playerLink) {
		this.playerLink = playerLink;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerIdentifier == null) ? 0 : playerIdentifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (playerIdentifier == null) {
			if (other.playerIdentifier != null)
				return false;
		} else if (!playerIdentifier.equals(other.playerIdentifier))
			return false;
		return true;
	}

}
