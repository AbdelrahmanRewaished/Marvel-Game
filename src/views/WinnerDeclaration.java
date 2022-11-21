package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import engine.Game;
import engine.Player;
import model.world.Champion;

public class WinnerDeclaration extends GameViews implements ActionListener
{
	private final JButton end;
	public WinnerDeclaration(Game game) throws UnsupportedAudioFileException, IOException, LineUnavailableException 
	{
		super();
		File file = new File("components/gameEndSound.wav");
		AudioInputStream audiostream = AudioSystem.getAudioInputStream(file);
		Clip c = AudioSystem.getClip();
		c.open(audiostream);
		c.start();
		c.loop(100);
		
		this.setLayout(new BorderLayout());
		this.setTitle("Marvel Combat");
		ImageIcon image = new ImageIcon("components/Icon_game.jpg");
		this.setIconImage(image.getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setBounds(0,0,screenSize.width, screenSize.height);
		this.setResizable(true);  
		getContentPane().setBackground(Color.WHITE);
		JLabel label = new JLabel();
		Player p = game.checkGameOver();
		if(p == game.getFirstPlayer())
		{
			label.setText(p.getName() + " Wins");
		}
		else
		{
			label.setText(p.getName() + " Wins");
		}
		label.setFont(new Font("Mv Boli", Font.BOLD, 50));
		end  = new JButton("End Game Now");
		end.setFont(new Font("Arial", Font.BOLD, 15));
		end.addActionListener(this);
	    label.add(end, BorderLayout.CENTER);
	    label.setVerticalTextPosition(JLabel.NORTH);
//	    end.setVerticalAlignment(JButton.SOUTH);
	    end.setHorizontalAlignment(JButton.CENTER);
	    label.setVerticalAlignment(JLabel.CENTER);
	    label.setHorizontalAlignment(JLabel.CENTER);
	    add(label, BorderLayout.CENTER);
	    validate();
	    repaint();
	    setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(end == e.getSource())
		{
			dispose();
		}
	}
	public static void main(String[] args) throws Exception 
	{
		Game.loadAbilities("Abilities.csv");
		Game.loadChampions("Champions.csv");
		Player p1 = new Player("Ahmed");
		Player p2 = new Player("Hossam");
		int i = 0;
		for(Champion c: Game.getAvailableChampions())
		{
			if(i < 3)
			{	
				p1.getTeam().add(c);
			}
			else if(i < 6)
			{
				p2.getTeam().add(c);
			}
			else
				break;
			i++;
		}
		p1.setLeader(p1.getTeam().get(0));
		p2.setLeader(p1.getTeam().get(0));
		Game g = new Game(p1, p2);
		p1.getTeam().clear();
		new WinnerDeclaration(g);
	}
}
