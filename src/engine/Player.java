package engine;

import java.util.ArrayList;

import model.world.Champion;

public class Player {
	private final String name;
	private Champion leader;
	private final ArrayList<Champion> team;
	
	public Player (String name) {
		this.name = name;
		team = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public Champion getLeader() {
		return leader;
	}

	public void setLeader(Champion leader) {
		this.leader = leader;
		this.leader.setToLeader(true);
		for(Champion member: team)
		{
			member.setLeaderSet(true);
		}
	}

	public ArrayList<Champion> getTeam() {
		return team;
	}

}
