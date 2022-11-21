package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import engine.*;
import model.world.Champion;

public class ChooseLeaders extends GameViews implements ActionListener
{
	private final Player p1;
	private final Player p2;
	private final JTextArea text;
	private boolean first_leader_chosen;
	private final ArrayList<JButton> buttons;
	private final JPanel p;
	public ChooseLeaders(Player p1, Player p2)
	{
		super();
		this.p1 = p1;
		this.p2 = p2;
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(310, 50));
		buttons = new ArrayList<>();
		p = new JPanel();
		p.setPreferredSize(new Dimension(70, 70));
		text = new JTextArea();
		text.setText(p1.getName() + ", Choose your Team's Leader");
		text.setPreferredSize(new Dimension(310 , 50));
		text.setEditable(false);
		text.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		panel.add(text);
		add(panel, BorderLayout.NORTH);
		addButtons(p, p1);
		add(p, BorderLayout.CENTER);
		validate();
		repaint();
		setVisible(true);
		setSize();
	}
	private void addButtons(JPanel panel, Player p)
	{
		buttons.clear();
		for(Champion c: p.getTeam())
		{
			ImageIcon image = new ImageIcon(components_package + c.getName() + ".png");
			JButton b = new JButton(image);
			b.setPreferredSize(new Dimension(300, 300));
			b.addActionListener(this);
			panel.add(b);
			buttons.add(b);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JButton b = (JButton)e.getSource();
		int i = buttons.indexOf(b);
		if(! first_leader_chosen)
		{
			Champion c = p1.getTeam().get(i);
			p1.setLeader(c);
			p.removeAll();
			text.setText(p2.getName() + ", Choose your Team's Leader");
			addButtons(p, p2);
			first_leader_chosen = true;
		}
		else 
		{
			Champion c = p2.getTeam().get(i);
			p2.setLeader(c);
			try 
			{
				new MainView(new Game(p1, p2));
			} 
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
			setVisible(false); //you can't see me!
			dispose(); //Destroy the JFrame object
		}
		
	}
	
}