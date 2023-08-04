package engine;

import java.util.ArrayList;

import model.world.Champion;
import model.world.ChampionListener;

public class Player implements ChampionListener {
	private String name;
	private ArrayList<Champion> team;
	private Champion leader;
	private PlayerListener listener;
	

	public Player(String name) {
		this.name = name;
		team = new ArrayList<Champion>();
		
	}


	public PlayerListener getListener() {
		return listener;
	}


	public void setListener(PlayerListener listener) {
		this.listener = listener;
	}


	public Champion getLeader() {
		return leader;
	}

	public void setLeader(Champion leader) {
		this.leader = leader;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Champion> getTeam() {
		return team;
	}


	@Override
	public void onSelected(Champion c) {
		team.add(c);
		if (listener != null) {
			listener.onUpdateChampions();
		}
		
	}
	


}
