package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
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
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import engine.Game;
import engine.GameListener;
import engine.Player;
import model.world.Champion;
import view.view;

public class GUI implements ActionListener, GameListener {
	private Game game;
	Player first;
	Player second;
	private view view;
	private JTextArea textArea;
	private ArrayList<JButton> btns;
	private ImageIcon champIcon;

	GUI() throws IOException {
		try {
			playSound("click.wav");
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {

		}
		String firstPlayerName = JOptionPane.showInputDialog("first player's name");
		try {
			playSound("click.wav");
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {

		}
		String secondPlayerName = JOptionPane.showInputDialog("second player's name");
		first = new Player(firstPlayerName);
		second = new Player(secondPlayerName);
		game = new Game(first, second);
		game.setListener(this);
		game.loadAbilities("Abilities.csv");
		game.loadChampions("Champions.csv");

		view = new view(first, second);
		try {
			playSound("start.wav");
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {

		}
		btns = new ArrayList<>();
		int i = 0;
		loadIcons();
		view.setVisible(true);

	}

	public void loadIcons() {
		int i = 0;
		for (final Champion c : game.getAvailableChampions()) {
			// create a JButton for each product in the supermarket
			JButton btn = new JButton();
			btn.setSize(100, 100);
			if (i == 0)
				champIcon = new ImageIcon(getClass().getResource("capamerica.jpeg"));
			if (i == 1)
				champIcon = new ImageIcon(getClass().getResource("deadpool.jpeg"));
			if (i == 2)
				champIcon = new ImageIcon(getClass().getResource("drstrange.jpeg"));
			if (i == 3)
				champIcon = new ImageIcon(getClass().getResource("electro.jpeg"));
			if (i == 4)
				champIcon = new ImageIcon(getClass().getResource("ghostrider.png"));
			if (i == 5)
				champIcon = new ImageIcon(getClass().getResource("hela.jpeg"));
			if (i == 6)
				champIcon = new ImageIcon(getClass().getResource("hulk.jpeg"));
			if (i == 7)
				champIcon = new ImageIcon(getClass().getResource("iceman.jpeg"));
			if (i == 8)
				champIcon = new ImageIcon(getClass().getResource("ironman.png"));
			if (i == 9)
				champIcon = new ImageIcon(getClass().getResource("loki.jpeg"));
			if (i == 10)
				champIcon = new ImageIcon(getClass().getResource("quicksilver.jpeg"));
			if (i == 11)
				champIcon = new ImageIcon(getClass().getResource("spiderman.jpeg"));
			if (i == 12)
				champIcon = new ImageIcon(getClass().getResource("thor.jpeg"));
			if (i == 13)
				champIcon = new ImageIcon(getClass().getResource("venom.png"));
			if (i == 14)
				champIcon = new ImageIcon(getClass().getResource("yellowjacket.jpeg"));

			btn.setIcon(champIcon);
			btn.setBorder(new LineBorder(Color.WHITE));
			// set its text to the product's info
			// add the controller as its ActionListener
			btn.addActionListener(this);
			btn.setToolTipText("<html>"+c.toString().replace("\n", "<br>"));

			// add it to the products buy buttons panel
			view.addChampions(btn);

			// and also add it to the ArrayList for later use
			btns.add(btn);
			i++;
		}
	}

	public static void playSound(String fileName)
			throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException {
		File url = new File(fileName);
		Clip clip = AudioSystem.getClip();

		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		clip.start();
	}

	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		// get its index within the ArrayList of buttons
		int i = btns.indexOf(btn);
		// get the corresponding product from the supermarket
		Champion c = game.getAvailableChampions().get(i);

		if (game.getFirstPlayer().getTeam().size() < 3) {
			if (game.getFirstPlayer().getTeam().size() == 0)
				game.getFirstPlayer().setLeader(c);
			c.setListener(first);
			btn.setEnabled(false);
		} else if (game.getSecondPlayer().getTeam().size() < 3) {
			if (game.getSecondPlayer().getTeam().size() == 0)
				game.getSecondPlayer().setLeader(c);
			c.setListener(second);
			btn.setEnabled(false);
		}
		if (game.getSecondPlayer().getTeam().size() == 3) {
			view.dispose();
			new GameGUI(first,second);
			try {
				playSound("start.wav");
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			}
		}
		// select it
		c.select();
		try {
			playSound("click.wav");
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {

		}

	}

	public void onUpdateChampions(Player p1, Player p2) {

		ArrayList<String> Champs = new ArrayList<String>();
		for (Champion c : p1.getTeam()) {
			Champs.add(c.getName());
		}
		for (Champion c : p2.getTeam()) {
			Champs.add(c.getName());
		}

		view.updateChamps(Champs, p1, p2);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public static void main(String[] a) throws IOException {

		new GUI();

	}

	public void onUpdatePlayground(Object[][] board) {
		// TODO Auto-generated method stub

	}

}
