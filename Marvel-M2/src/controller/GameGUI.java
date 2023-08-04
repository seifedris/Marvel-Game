package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import engine.Game;
import engine.GameListener;
import engine.Player;
import exceptions.AbilityUseException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import view.GameView;
import view.GameViewListener;
import view.InfoView;

public class GameGUI implements GameListener, GameViewListener, MouseListener {

	private Game game;
	private GameView view;
	private ArrayList<JButton> abilityBtns;
	private ArrayList<JButton> boardBtns;
	final JPanel panel = new JPanel();
	private ImageIcon coverIcon;
	private int champsleft;
	private String old_text;
	private Color old_color;
	private Ability ssability;

	public GameGUI(Player first, Player second) {

		view = new GameView();
		view.setListener(this);

		view.setVisible(true);
		this.game = new Game(first, second);
		this.game.setListener(this);
		abilityBtns = new ArrayList<>();
		boardBtns = new ArrayList<>();
		game.placeChampions();
		view.instructions();
		view.updateChampionsData(game.getFirstPlayer(), game.getSecondPlayer(), game.isFirstLeaderAbilityUsed(),
				game.isSecondLeaderAbilityUsed());
		view.updateCurrentChampion(game.getCurrentChampion(), (Champion) game.getTurnOrder().nextTurn());
		onUpdatePlayground(game.getBoard());
		onAddAbilities();
		view.validate();
		champsleft = 6;
		for (Champion c : game.getFirstPlayer().getTeam()) {
			game.getAvailableChampions().add(c);
		}
		for (Champion c : game.getSecondPlayer().getTeam()) {
			game.getAvailableChampions().add(c);
		}
	}

	public void onAddAbilities() {
		ArrayList<JButton> abilityBtn = new ArrayList<>();
		int i = 1;
		for (Ability a : game.getCurrentChampion().getAbilities()) {
			JButton btn = new JButton();
			btn.setOpaque(true);
			btn.setBorderPainted(false);
			btn.setForeground(Color.white);
			btn.setBackground(Color.DARK_GRAY.darker());
			btn.setBorder(new LineBorder(Color.WHITE));
			btn.setEnabled(false);
			btn.setForeground(Color.white);
			btn.setText(i + " - " + a.getName());
			btn.setToolTipText("<html>"+a.toString().replace("\n", "<br>"));
			if (!a.getName().equals("Punch"))
				abilityBtn.add(btn);
			i++;
		}
		view.addAbilities(abilityBtn, game.getCurrentChampion());
	}

	public void onUpdatePlayground(Object[][] b) {
		boardBtns = new ArrayList<JButton>();
		Object[][] board = game.getBoard();
		for (int i = 4; i != -1; i--) {
			int j = 0;
			for (j = 0; j != 5; j++) {
				Damageable d = (Damageable) board[i][j];
				JButton s = new JButton();
				s.setOpaque(true);
				s.setForeground(Color.white);
				s.setBorder(new LineBorder(Color.WHITE));
				s.setFocusable(false);
				s.addMouseListener(this);
				if (d instanceof Cover) {
					coverIcon = new ImageIcon(getClass().getResource("cover.png"));
					s.setToolTipText("HP: " + d.getCurrentHP());
					s.setText(Integer.toString(d.getCurrentHP()));
					s.setIcon(coverIcon);

				}
				if (d instanceof Champion) {
					s.setText(((Champion) d).getName());
					s.setToolTipText("HP: " + d.getCurrentHP());
					if (d == game.getCurrentChampion())
						s.setBackground(Color.green.darker().darker());
					else if (game.getFirstPlayer().getTeam().contains(d))
						s.setBackground(Color.BLUE.darker());
					else
						s.setBackground(Color.RED.darker().darker());
				}

				boardBtns.add(s);
			}
		}
		view.updatePlayground(boardBtns);

	}

	public void onUpdateChampions(Player p1, Player p2) {

	}

	public static void playSound(String fileName)
			throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException {
		File url = new File(fileName);
		Clip clip = AudioSystem.getClip();

		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		clip.start();
	}

	public void onKeyPressed(KeyEvent e) {
		ssability = null;
		view.revalidate();
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP)
			try {
				game.move(Direction.UP);
				playSound("move.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		if (key == KeyEvent.VK_DOWN)
			try {
				game.move(Direction.DOWN);
				playSound("move.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		if (key == KeyEvent.VK_RIGHT)
			try {
				game.move(Direction.RIGHT);
				playSound("move.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			}
		if (key == KeyEvent.VK_LEFT)
			try {
				game.move(Direction.LEFT);
				playSound("move.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		if (key == KeyEvent.VK_W)
			try {
				game.attack(Direction.UP);
				playSound("attack.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		if (key == KeyEvent.VK_A)
			try {
				game.attack(Direction.LEFT);
				playSound("attack.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		if (key == KeyEvent.VK_S)
			try {
				game.attack(Direction.DOWN);
				playSound("attack.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		if (key == KeyEvent.VK_D)
			try {
				game.attack(Direction.RIGHT);
				playSound("attack.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		if (key == KeyEvent.VK_E) {
			game.endTurn();

		}
		if (key == KeyEvent.VK_L)
			try {
				game.useLeaderAbility();
				playSound("ability.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		if (key == KeyEvent.VK_I) {
			new InfoView(game.getFirstPlayer(), game.getSecondPlayer());
			try {
				playSound("click.wav");
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {

			}
		}
		if (key == KeyEvent.VK_1) {
			Ability a = game.getCurrentChampion().getAbilities().get(0);
			if (a.getCastArea() == AreaOfEffect.SELFTARGET || a.getCastArea() == AreaOfEffect.TEAMTARGET
					|| a.getCastArea() == AreaOfEffect.SURROUND)
				try {
					game.castAbility(a);
					playSound("ability.wav");
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
				String[] options = new String[] { "up", "down", "left", "right" };
				int response = JOptionPane.showOptionDialog(panel, "Choose your desired direction for the ability ",
						"Cast Ability", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
						options[0]);
				if (response == 0) {
					try {
						game.castAbility(a, Direction.UP);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (response == 1) {
					try {
						game.castAbility(a, Direction.DOWN);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (response == 2) {
					try {
						game.castAbility(a, Direction.LEFT);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (response == 3) {
					try {
						game.castAbility(a, Direction.RIGHT);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
			if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
				ssability = a;
			}
		}
		if (key == KeyEvent.VK_2) {
			Ability a = game.getCurrentChampion().getAbilities().get(1);
			if (a.getCastArea() == AreaOfEffect.SELFTARGET || a.getCastArea() == AreaOfEffect.TEAMTARGET
					|| a.getCastArea() == AreaOfEffect.SURROUND)
				try {
					game.castAbility(a);
					playSound("ability.wav");
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
				String[] options = new String[] { "up", "down", "left", "right" };
				int response = JOptionPane.showOptionDialog(panel, "Choose your desired direction for the ability ",
						"Cast Ability", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
						options[0]);
				if (response == 0) {
					try {
						game.castAbility(a, Direction.UP);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (response == 1) {
					try {
						game.castAbility(a, Direction.DOWN);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (response == 2) {
					try {
						game.castAbility(a, Direction.LEFT);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (response == 3) {
					try {
						game.castAbility(a, Direction.RIGHT);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
			if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
				ssability = a;
			}
		}
		if (key == KeyEvent.VK_3) {
			Ability a = game.getCurrentChampion().getAbilities().get(2);
			if (a.getCastArea() == AreaOfEffect.SELFTARGET || a.getCastArea() == AreaOfEffect.TEAMTARGET
					|| a.getCastArea() == AreaOfEffect.SURROUND)
				try {
					game.castAbility(a);
					playSound("ability.wav");
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
				String[] options = new String[] { "up", "down", "left", "right" };
				int response = JOptionPane.showOptionDialog(panel, "Choose your desired direction for the ability ",
						"Cast Ability", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
						options[0]);
				if (response == 0) {
					try {
						game.castAbility(a, Direction.UP);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (response == 1) {
					try {
						game.castAbility(a, Direction.DOWN);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (response == 2) {
					try {
						game.castAbility(a, Direction.LEFT);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				if (response == 3) {
					try {
						game.castAbility(a, Direction.RIGHT);
						playSound("ability.wav");
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
			if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
				ssability = a;
			}
		}
		if (game.getFirstPlayer().getTeam().size() + game.getSecondPlayer().getTeam().size() < champsleft) {
			try {
				playSound("finishhim.wav");
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {

			}
		}
		champsleft = game.getFirstPlayer().getTeam().size() + game.getSecondPlayer().getTeam().size();

		if (game.checkGameOver() != null) {
			view.dispose();
			try {
				playSound("gameover.wav");
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {

			}
			JOptionPane.showMessageDialog(panel, game.checkGameOver().getName() + " won!!!", "Game Over",
					JOptionPane.ERROR_MESSAGE);

		}

		view.updateCurrentChampion(game.getCurrentChampion(), (Champion) game.getTurnOrder().nextTurn());
		game.onUpdatePlayground();
		view.updateChampionsData(game.getFirstPlayer(), game.getSecondPlayer(), game.isFirstLeaderAbilityUsed(),
				game.isSecondLeaderAbilityUsed());
		onAddAbilities();
		view.revalidate();

	}

	


	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		JButton b = (JButton) e.getSource();
		if (ssability == null) {
			JOptionPane.showMessageDialog(panel, "You did not choose a single Target Ability", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (b.getText().equals("")) {
			JOptionPane.showMessageDialog(panel, "You can not cast an ability on an empty cell", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		Champion n = null;
		Cover m = null;
		for (Champion c : game.getFirstPlayer().getTeam()) {
			if (b.getText() == c.getName())
				n = c;
		}
		for (Champion c : game.getSecondPlayer().getTeam()) {
			if (b.getText() == c.getName())
				n = c;
		}
		if (n == null)
			for (Cover r : game.getAvailableCovers()) {
				if (Integer.parseInt(b.getText()) == r.getCurrentHP())
					m = r;
			}
		if (n != null) {
			try {
				game.castAbility(ssability, n.getLocation().x, n.getLocation().y);
				playSound("ability.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (m != null) {
			try {
				game.castAbility(ssability, m.getLocation().x, m.getLocation().y);
				playSound("ability.wav");
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(panel, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		if (game.getFirstPlayer().getTeam().size() + game.getSecondPlayer().getTeam().size() < champsleft) {
			try {
				playSound("finishhim.wav");
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {

			}
		}
		champsleft = game.getFirstPlayer().getTeam().size() + game.getSecondPlayer().getTeam().size();

		view.updateCurrentChampion(game.getCurrentChampion(), (Champion) game.getTurnOrder().nextTurn());
		game.onUpdatePlayground();
		view.updateChampionsData(game.getFirstPlayer(), game.getSecondPlayer(), game.isFirstLeaderAbilityUsed(),
				game.isSecondLeaderAbilityUsed());
		onAddAbilities();
		view.revalidate();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		JButton b=(JButton)e.getSource();
		old_color=b.getBackground();
		b.setBackground(Color.LIGHT_GRAY);

	}

	@Override
	public void mouseExited(MouseEvent e) {
		JButton b=(JButton)e.getSource();
		b.setBackground(old_color);

	}

}
