package engine;

import model.world.Champion;

public interface GameListener {
	void onUpdateChampions(Player p1, Player p2);

	void onUpdatePlayground(Object[][] board);
}
