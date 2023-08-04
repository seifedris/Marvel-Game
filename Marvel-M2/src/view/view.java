package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import engine.Player;

public class view extends JFrame {
	private JPanel champPanel;
	private JPanel namePanel;
	private JTextArea txt;
	private ImageIcon i;
	public view(Player p1, Player p2) {
		
		setTitle("Choose your Champions");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(0, 0, 1200, 1200);
		champPanel= new JPanel();
		champPanel.setLayout(new GridLayout(3,5));
	add(champPanel, BorderLayout.CENTER);
	txt = new JTextArea();
	txt.setBackground(Color.DARK_GRAY);
	txt.setForeground(Color.white);
	txt.setPreferredSize(new Dimension(350, getHeight()));
	
	txt.setEditable(false);
	txt.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
	updateChamps(new ArrayList<String> (), p1, p2);

	add(txt, BorderLayout.EAST);
	
	}
public void addChampions(JButton champ) {
	
	champPanel.add(champ);
}

public void updateChamps(ArrayList<String> Champion,Player p1,Player p2) {
	String firstTeam = "   Choose Wisely! Your First \n   Choice is the Leader\n   Hover over Champion for info\n\n\n";
	firstTeam += p1.getName()+"'s Team:\n";
	
	for (int i=0; i!=3&& i!=Champion.size();i++) {
		if(i==0)
		firstTeam+= "   Leader: "+ Champion.get(i) + "\n";
		else
		firstTeam += "   "+(i+1) + ". " + Champion.get(i) + "\n";
	}
	String secondTeam= "";
	secondTeam += p2.getName()+"'s Team:\n";
	for (int i=3; i!=6&& i<Champion.size();i++) 
	{if(i==3)
		secondTeam+= "   Leader: "+ Champion.get(i) + "\n";
	else
		secondTeam +="   "+ (i-2)+ ". " + Champion.get(i) + "\n";
	}	
	String s = firstTeam + "\n\n"+ secondTeam;
	txt.setText(s);
}
	
}
