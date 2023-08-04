package view;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

import engine.Player;
import model.effects.Effect;
import model.world.Champion;

public class InfoView extends JFrame{
private JTextArea a;
private JScrollPane j;
public InfoView(Player p1, Player p2) {
	setVisible(true);
	setTitle("Champions' Information");
	setBounds(50, 50, 500, 500);
	String l=p1.getName() +"'s Team\n";
	for(Champion c : p1.getTeam()) {
		l+=c.toString();
		l+="Applied Effects:-\n";
		for(Effect a:c.getAppliedEffects() )
			l+=a.toString();
		l+="\n";
	}
	String t="\n------------------\n\n"+p2.getName() +"'s Team\n";
	for(Champion c : p2.getTeam()) {
		t+=c.toString();
		t+="Applied Effects:-\n";
		for(Effect a:c.getAppliedEffects() )
		t+=a.toString();
	}
	String n= l+t;
	a=new JTextArea();
	a.setText(n);
	a.setForeground(Color.white);
	a.setBackground(Color.DARK_GRAY.darker());
	j=new JScrollPane(a);
	DefaultCaret caret = (DefaultCaret)a.getCaret();
	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	
	add(j);
	

	SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
			j.getViewport().setViewPosition(new Point(0, 0));
		}
	});
}}

