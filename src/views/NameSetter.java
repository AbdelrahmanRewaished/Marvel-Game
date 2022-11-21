package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import engine.Player;

public class NameSetter extends GameViews implements ActionListener, KeyListener
{
	private Player p1;
	private Player p2;
	private final JButton button;
	private final JTextField textfield;
	private final JTextArea text_area;
	private boolean first_entered;
	
	public NameSetter()
	{
		super();
		this.setLayout(new FlowLayout());
		ImageIcon image = new ImageIcon(components_package + "Whole_marvel.png");
		image.getImage().getScaledInstance(this.getWidth(), this.getHeight(), DO_NOTHING_ON_CLOSE);
		this.setContentPane(new JLabel(image));
		this.setLayout(new FlowLayout());
		button = new JButton ("Enter");
		button.addActionListener(this);
		button.addKeyListener(this);
		button.setPreferredSize(new Dimension(70, 30));
		textfield = new JTextField();
		textfield.setPreferredSize(new Dimension(getWidth()/8, 50));
		textfield.setForeground(Color.RED);
		textfield.setBackground(Color.BLACK);
		textfield.setCaretColor(Color.RED);
		textfield.setFont(new Font(Font.SANS_SERIF, Font.BOLD,20));
		textfield.setEditable(true);
		text_area = new JTextArea(BorderLayout.CENTER);
		text_area.setText("Player 1 Enter your Name");
		text_area.setPreferredSize(new Dimension(getWidth()/7, 30));
		text_area.setEditable(false);
		text_area.setFont(new Font(Font.SERIF, Font.BOLD, 16));
		add(text_area, BorderLayout.NORTH);
		add(textfield, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
		first_entered = false;
		textfield.addKeyListener(this);
		pack();
		setSize();
		setVisible(true);
		
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == button)
		{
			String t = textfield.getText();
			if(t.equals(""))
			{
				JOptionPane.showMessageDialog(null, "Please Enter your name  :)", "Marvel Combat", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(! first_entered)
			{
				p1 = new Player(t);
				text_area.setText("Player 2 Enter your Name");
				textfield.setText("");
				first_entered = true;
			}
			else
			{
				p2 = new Player(t);
				try
				{
					new ChooseChampions(p1, p2);
					setVisible(false); //you can't see me!
					dispose(); //Destroy the JFrame object
				}
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) 
	{
		
	}
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getSource() == textfield && e.getKeyCode() == 10)
		{
			String t = textfield.getText();
			if(t.equals(""))
			{
				JOptionPane.showMessageDialog(null, "Please Enter your name  :)", "Marvel Combat", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(! first_entered)
			{
				p1 = new Player(t);
				text_area.setText("Player 2 Enter your Name");
				textfield.setText("");
				first_entered = true;
			}
			else
			{
				p2 = new Player(t);
				try
				{
					new ChooseChampions(p1, p2);
					setVisible(false); //you can't see me!
					dispose(); //Destroy the JFrame object
				}
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}
			}
	   }
	}
	@Override
	public void keyReleased(KeyEvent e) {

	}
}
