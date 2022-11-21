package views;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import engine.Game;
import engine.Player;
import model.world.Champion;

public class ChooseChampions extends GameViews implements ActionListener, ChampionAdderListener
{ 
	private final JPanel panel;
	private final JPanel lPanel;
	private final JPanel rPanel;
	private final ArrayList<JButton> buttons;
	private final ArrayList<Champion> champions;
	private ShowChampionAttributes sca;
	private final JTextArea txt;
	private final Player p1;
	private final Player p2;
	int clicks;
	public ChooseChampions(Player p1, Player p2) throws Exception 
	{
		super();
		this.p1 = p1;
		this.p2 = p2;
	//	this.setBackground(Color.RED);
		JPanel p = new JPanel();
		txt = new JTextArea();
		txt.setLineWrap(true);
		txt.setText("           " + p1.getName() + ", Choose 3 Champions for your Team");
		txt.setPreferredSize(new Dimension(getWidth() - 10, 30));
		txt.setEditable(false);
		txt.setFont(new Font ("Segoe Script", Font.BOLD, 16));
		p.add(txt, BorderLayout.NORTH);
		panel = new JPanel();
		panel.setLayout(new GridLayout(3, 5));
		panel.setPreferredSize(new Dimension(110, getHeight()));
		JPanel leftPanel = new JPanel(new FlowLayout());
		JPanel rightPanel = new JPanel(new FlowLayout());
		JTextArea leftText = new JTextArea("    "  + p1.getName() + " Champions");
		JTextArea rightText = new JTextArea("    " + p2.getName() + " Champions");
		leftText.setFont(new Font("MV Boli", Font.BOLD, 20));
		rightText.setFont(new Font("MV Boli", Font.BOLD, 20));
		lPanel = new JPanel();
		lPanel.setLayout(new GridLayout(3, 1));
		leftPanel.setPreferredSize(new Dimension(250, getHeight()));
		rPanel = new JPanel();
		rPanel.setLayout(new GridLayout(3, 1));
		rightPanel.setPreferredSize(new Dimension(250, getHeight()));
		leftPanel.add(leftText, BorderLayout.NORTH);
		leftPanel.add(lPanel, BorderLayout.CENTER);
		rightPanel.add(rightText, BorderLayout.NORTH);
		rightPanel.add(rPanel, BorderLayout.CENTER);
		buttons = new ArrayList<>();
		champions = Game.getAvailableChampions();
		Game.loadAbilities("src/Abilities.csv");
		Game.loadChampions("src/Champions.csv");
		createButtons();
		add(p, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		setSize();
		setVisible(true);
	}
	
	private void createButtons()
	{
		for(Champion champ: champions)
		{
			ImageIcon image = new ImageIcon(components_package + champ.getName()+ ".png");
			JButton b = new JButton(image);
			b.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
			b.addActionListener(this);
			buttons.add(b);
			panel.add(b);
		}
		validate();
		repaint();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(sca != null)
		{
			sca.dispose();
		}
		JButton b = (JButton)e.getSource();
		int i = buttons.indexOf(b);
		Champion champ = champions.get(i);
		
		try 
		{
			sca = new ShowChampionAttributes(champ, b);
			sca.setListener(this);
		} 
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
	}

	@Override
	public void onAdd(Champion champ) 
	{
		ImageIcon image = new ImageIcon(components_package + champ.getName() + ".png");
		JLabel label = new JLabel(image);
		if(clicks < 3)
		{
			p1.getTeam().add(champ);
			lPanel.add(label);
		}
		else if(clicks < 6)
		{
			p2.getTeam().add(champ);
			rPanel.add(label);
		}
		validate();
		repaint();
		clicks++;
		if(clicks == 6)
		{
			setVisible(false); //you can't see me!
			new ChooseLeaders(p1, p2);
			dispose(); //Destroy the JFrame object
		}
		else if(clicks == 3)
		{
			txt.setText("           " + p2.getName() + ", Choose 3 Champions for your Team");
		}
		validate();
		repaint();
	}
}
