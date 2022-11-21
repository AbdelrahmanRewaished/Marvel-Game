package views;

import model.world.Champion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ShowChampionAttributes extends JFrame implements ActionListener, KeyListener
{
	private final Champion champ;
	private final JButton button;
	private ChampionAdderListener listener;

	public void setListener(ChampionAdderListener listener)
	{
		this.listener = listener;
	}
	public ShowChampionAttributes(Champion c, JButton b) throws InterruptedException
	{
		super();
		this.setTitle("Marvel Combat");
		ImageIcon image = new ImageIcon(GameViews.components_package + "Icon_game.jpg");
		this.setIconImage(image.getImage());
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		champ = c;
		button = b;
		setBounds(200, 200, 500, 500);
		JTextArea text = new JTextArea();
		text.setPreferredSize(new Dimension(500, 500));
		text.setText(champ.toString(false) + champ.showAbilities());
		text.setEditable(false);
		JButton button;
		button = new JButton ("Add");
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(60, 40));
		text.addKeyListener(this);
		button.addKeyListener(this);
		JPanel p = new JPanel();
		p.add(text, BorderLayout.NORTH);
		p.add(button, BorderLayout.SOUTH);
		add(p, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(listener != null)
		{
			listener.onAdd(champ);
			button.setEnabled(false);
		}
		setVisible(false);
		dispose();
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == 10)
		{
			if(listener != null)
			{
				listener.onAdd(champ);
				button.setEnabled(false);
			}
			setVisible(false);
			dispose();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
}
