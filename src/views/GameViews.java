package views;

import java.awt.*;
import javax.swing.*;


abstract public class GameViews extends JFrame
{
	public static final String components_package = "src/components/";
	public GameViews() 
	{
		super();
		setLayout(new BorderLayout());
		this.setTitle("Marvel Combat");
		ImageIcon image = new ImageIcon(components_package + "components/game_logo.png");
		this.setIconImage(image.getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	    this.setBounds(0,0,screenSize.width, screenSize.height);
		this.setResizable(true);  
		this.getContentPane().setBackground(new Color(0x123456));
	}
public void setSize()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setBounds(0,0,screenSize.width, screenSize.height);
	}
	

}
