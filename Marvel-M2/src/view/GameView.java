package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import engine.Player;
import model.abilities.Ability;
import model.effects.Effect;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;
import model.world.Hero;
import model.world.Villain;

public class GameView extends JFrame implements KeyListener {
	private JPanel playground;
	private JTextArea rightTxt;
	private JPanel leftPanel;
	private JPanel topPanel;
	private JTextArea bottomTxt;
	private JScrollPane right;
	private GameViewListener listener;

	public GameView() {
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(this);

		setTitle("Marvel");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 800);
		playground = new JPanel();
		playground.setLayout(new GridLayout(5, 5));
		rightTxt = new JTextArea();
		rightTxt.setPreferredSize(new Dimension(200, 800));
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(200, 3000));
		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(400, 200));
		topPanel.setLayout(new GridLayout(0, 3));
		bottomTxt = new JTextArea();
		bottomTxt.setPreferredSize(new Dimension(200, 200));

		right = new JScrollPane(rightTxt);
		right.getViewport().setViewPosition(new Point(0, 0));
		right.revalidate();

		add(topPanel, BorderLayout.NORTH);

		add(right, BorderLayout.EAST);
		add(leftPanel, BorderLayout.WEST);

		rightTxt.setEditable(false);

		bottomTxt.setEditable(false);
		playground.setPreferredSize(new Dimension(400, 400));
		updatePlayground(new ArrayList<JButton>());
	}

	public void addAbilities(ArrayList<JButton> btns, Champion current) {
		remove(topPanel);
		topPanel = new JPanel();
		for (JButton b : btns) {
			topPanel.add(b);
		}
		topPanel.setPreferredSize(new Dimension(200, 200));
		topPanel.setLayout(new GridLayout(0, 4));
		add(topPanel, BorderLayout.NORTH);
		JButton n = new JButton();
		n.setOpaque(true);
		n.setBorderPainted(false);
		n.setForeground(Color.white);
		n.setBackground(Color.DARK_GRAY.darker());
		n.setBorder(new LineBorder(Color.WHITE));
		n.setEnabled(false);
		n.setForeground(Color.white);
		n.setText("\nHP");
		JProgressBar p = new JProgressBar(0, current.getMaxHP());
		p.setValue(current.getCurrentHP());
		p.setOpaque(true);
		p.setBackground(Color.DARK_GRAY.darker());
		n.add(p);
		n.setToolTipText("Current Player's HP");
		topPanel.add(n);
		topPanel.revalidate();
	}

	public void updatePlayground(ArrayList<JButton> btns) {
		remove(playground);
		playground = new JPanel();
		for (JButton b : btns) {

			playground.add(b);
		}
		playground.setLayout(new GridLayout(5, 5));
		playground.setPreferredSize(new Dimension(400, 400));
		add(playground, BorderLayout.CENTER);
		playground.revalidate();
	}

	public void updateCurrentChampion(Champion current, Champion next) {
		remove(right);
		rightTxt = new JTextArea();
		String s = "Playing : " + current.getName() + "   ";
		if (current instanceof Hero)
			s += "(Hero) \n";
		if (current instanceof AntiHero)
			s += "(AntiHero)\n";
		if (current instanceof Villain)
			s += "(Villain)\n";
		s += "hp: " + current.getCurrentHP() + "\n" + "mana: " + current.getMana() + "\naction points: "
				+ current.getCurrentActionPoints() + "\nattack damage: " + current.getAttackDamage()
				+ "\nattack range: " + current.getAttackRange() + "\n\n";
		for (Ability a : current.getAbilities()) {
			s += "\n" + a.toString();
		}
		for (Effect e : current.getAppliedEffects()) {
			s += "\napplied: " + e.toString();
		}
		if (next != null)
			s += "\n\n\n\nNext Champion: " + next.getName();
		else
			s += "\n\n\n\nNext Champion: Preparing Turns...";
		rightTxt.setPreferredSize(new Dimension(250, 850));
		rightTxt.setText(s);

		right = new JScrollPane(rightTxt);
		rightTxt.setBackground(Color.DARK_GRAY.darker());
		rightTxt.setForeground(Color.white);
		rightTxt.setEditable(false);
		right.setPreferredSize(new Dimension(250, 850));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				right.getViewport().setViewPosition(new Point(0, 0));
			}
		});
		add(right, BorderLayout.EAST);

		right.getViewport().setViewPosition(new Point(0, 0));
		right.setAutoscrolls(true);
		right.getVerticalScrollBar().setValue(0);
	}

	public void updateChampionsData(Player p1, Player p2, boolean firstLeaderAbility, boolean secondLeaderAbility) {
		remove(leftPanel);
		leftPanel.setLayout(new GridLayout(0, 1));
		leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(200, 600));
		String s = p1.getName() + "'s Team:-\n";
		JButton k = new JButton();
		k.setOpaque(true);
		k.setEnabled(false);
		k.setBorder(new LineBorder(Color.WHITE));
		k.setForeground(Color.black);
		k.setText("Available Champions");
		leftPanel.add(k);
		JButton r = new JButton();
		r.setOpaque(true);
		r.setEnabled(false);
		r.setBorder(new LineBorder(Color.WHITE));
		r.setBackground(Color.blue.darker());
		r.setForeground(Color.black);
		r.setText(p1.getName() + "'s Champions");
		leftPanel.add(r);
		for (Champion c : p1.getTeam()) {
			JButton n = new JButton();
			n.setOpaque(true);
			n.setEnabled(false);
			n.setBorder(new LineBorder(Color.WHITE));
			n.setBackground(Color.blue.darker());
			n.setForeground(Color.black);
			n.setToolTipText("<html>"+c.toString().replace("\n", "<br>"));
			if (c == p1.getLeader() && c instanceof AntiHero)
				s = "Leader (AntiHero) " + c.getName();
			if (c == p1.getLeader() && c instanceof Villain)
				s = "Leader (Villain) " + c.getName();
			if (c == p1.getLeader() && c instanceof Hero)
				s = "Leader (Hero) " + c.getName();
			if (c != p1.getLeader() && c instanceof AntiHero)
				s = "(AntiHero) " + c.getName();
			if (c != p1.getLeader() && c instanceof Villain)
				s = "(Villain) " + c.getName();
			if (c != p1.getLeader() && c instanceof Hero)
				s = "(Hero) " + c.getName();
			n.setText(s);
			leftPanel.add(n);

		}
		String a = "";
		if (firstLeaderAbility) {
			a = p1.getName() + "'s Leader Ability is Used\n";
			JButton n = new JButton();
			n.setOpaque(true);
			n.setEnabled(false);
			n.setBorder(new LineBorder(Color.WHITE));
			n.setBackground(Color.blue.darker());
			n.setForeground(Color.black);
			n.setText(a);
			leftPanel.add(n);
		} else {
			a = p1.getName() + "'s Leader Ability Available\n";
			JButton n = new JButton();
			n.setOpaque(true);
			n.setEnabled(false);
			n.setBorder(new LineBorder(Color.WHITE));
			n.setBackground(Color.blue.darker());
			n.setForeground(Color.black);
			n.setText(a);
			leftPanel.add(n);
		}
		JButton q = new JButton();
		q.setOpaque(true);
		q.setEnabled(false);
		q.setBorder(new LineBorder(Color.WHITE));
		q.setBackground(Color.red.darker().darker());
		q.setForeground(Color.black);
		q.setText(p2.getName() + "'s Champions");
		leftPanel.add(q);

		for (Champion c : p2.getTeam()) {
			JButton n = new JButton();
			n.setOpaque(true);
			n.setEnabled(false);
			n.setBorder(new LineBorder(Color.WHITE));
			n.setBackground(Color.red.darker().darker());
			n.setForeground(Color.white);
			n.setToolTipText("<html>"+c.toString().replace("\n", "<br>"));
			if (c == p2.getLeader() && c instanceof AntiHero)
				s = "Leader (AntiHero) " + c.getName();
			if (c == p2.getLeader() && c instanceof Villain)
				s = "Leader (Villain) " + c.getName();
			if (c == p2.getLeader() && c instanceof Hero)
				s = "Leader (Hero) " + c.getName();
			if (c != p2.getLeader() && c instanceof AntiHero)
				s = "(AntiHero) " + c.getName();
			if (c != p2.getLeader() && c instanceof Villain)
				s = "(Villain) " + c.getName();
			if (c != p2.getLeader() && c instanceof Hero)
				s = "(Hero) " + c.getName();
			n.setText(s);
			leftPanel.add(n);
		}

		if (secondLeaderAbility) {
			a = p2.getName() + "'s Leader Ability Used\n";
			JButton n = new JButton();
			n.setEnabled(false);
			n.setOpaque(true);
			n.setBorder(new LineBorder(Color.WHITE));
			n.setBackground(Color.red.darker().darker());
			n.setForeground(Color.white);

			n.setText(a);
			leftPanel.add(n);
		} else {
			a = p2.getName() + "'s Leader Ability Available\n";
			JButton n = new JButton();
			n.setEnabled(false);
			n.setOpaque(true);
			n.setBorder(new LineBorder(Color.WHITE));
			n.setBackground(Color.red.darker().darker());
			n.setForeground(Color.white);
			n.setText(a);
			leftPanel.add(n);
		}
		leftPanel.setBackground(Color.DARK_GRAY.darker());
		add(leftPanel, BorderLayout.WEST);
	}

	public void instructions() {
		String s = "   \n\n\n                                                                        W,A,S,D: up,left,down,right ATTACK"
				+ "         Hover over abilities, covers and champions for more info\n"
				+ "                             use arrows for movement"
				+ "      E: end your turn      1,2,3: use abilities shown above     L: use leader ability      i: More Info      Select Cell "
				+ "for Single Target Ability";
		bottomTxt.setText(s);
		bottomTxt.setBackground(Color.DARK_GRAY.darker());
		bottomTxt.setForeground(Color.white);
		bottomTxt.setFont(new Font(Font.DIALOG, Font.BOLD, 14));

		add(bottomTxt, BorderLayout.SOUTH);
	}

	public JPanel getPlayground() {
		return playground;
	}

	public void setPlayground(JPanel playground) {
		this.playground = playground;
	}

	public JTextArea getRightTxt() {
		return rightTxt;
	}

	public void setRightTxt(JTextArea rightTxt) {
		this.rightTxt = rightTxt;
	}

	public JPanel getLeftTxt() {
		return leftPanel;
	}

	public JPanel getTopPanel() {
		return topPanel;
	}

	public void setTopPanel(JPanel topPanel) {
		this.topPanel = topPanel;
	}

	public JTextArea getBottomTxt() {
		return bottomTxt;
	}

	public void setBottomTxt(JTextArea bottomTxt) {
		this.bottomTxt = bottomTxt;
	}

	public JScrollPane getRight() {
		return right;
	}

	public void setRight(JScrollPane right) {
		this.right = right;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (listener != null)
			listener.onKeyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	public GameViewListener getListener() {
		return listener;
	}

	public void setListener(GameViewListener listener) {
		this.listener = listener;
	}

}
