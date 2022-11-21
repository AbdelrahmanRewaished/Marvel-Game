package views;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.Border;

import engine.*;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.world.*;
import model.abilities.*;
import model.effects.*;

@SuppressWarnings("ALL")
public class MainView extends GameViews implements ActionListener
{
	private final Game game;
	private final JPanel gamepanel;
	private final JPanel left;
	private final JButton[][] boardButtons;
	private final JFrame attributeFrame;
	private final JTextArea text;
	private final JButton Up;
	private final JButton Down;
	private final JButton Left;
	private final JButton Right;
	private final JButton attack;
	private final JButton endTurn;
	private final JButton useFirstLeaderAbility;
	private final JButton useSecondLeaderAbility;
	private final JPanel turnsQueue;
	private final JTextArea current_champ_info;
	private final JTextArea current_champ_effects;
	private final JLabel currentChampionPhoto;
	private JLabel firstLeader;
	private JLabel secondLeader;
	private boolean text_written;
	private final JPanel abilitiesPanel;
	private final ArrayList<JButton> abilities;
	private final ArrayList<JButton> abilitiesIcons;
	private final ArrayList<JButton> all_actions;
	private boolean attacking;
	private boolean castingDirectionAbility;
	private boolean castingSingleAbility;
	private Ability castedAbility;
	private final JFrame frame;
	private final JPanel p;
	private final JButton sound;
	private final Clip clip;
	private final Clip attack_sound;
	private final Clip HarmingAbility_sound;
	private final Clip PositiveAbility_sound;
	private final Clip LeaderAbility_sound;
	
	public MainView(Game g) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		super();
		sound  = new JButton();
		sound.addActionListener(this);
//		sound.setBackground(Color.WHITE);
		sound.setIcon(new ImageIcon(components_package + "audio.png"));
		add(sound);
		sound.setBounds(324, 4, 50, 50);
		
		clip = getSound(components_package + "Ragnarok.wav");
		clip.start();
		clip.loop(100);
		attack_sound =  getSound(components_package + "attack.wav");
		HarmingAbility_sound = getSound(components_package + "Harming Ability.wav");
		PositiveAbility_sound = getSound(components_package + "PositiveEffectSound.wav");
		LeaderAbility_sound = getSound(components_package + "LeaderAbilitySound.wav");
		
		this.game = g;
		attributeFrame = new JFrame();
		String title = "Marvel Combat";
		attributeFrame.setTitle(title);
		ImageIcon image = new ImageIcon(components_package + "Icon_game.jpg");
		attributeFrame.setIconImage(image.getImage());
		attributeFrame.setBounds(20, 20, 300, 250);
		
		text = new JTextArea();
		text.setPreferredSize(new Dimension(250, 250));
		text.setText("");
		text.setEditable(false);
		
		JPanel panel = new JPanel (new GridLayout(3,1));
		panel.setPreferredSize(new Dimension(640,720));
		panel.setLocation(320,0);
		
		
		useFirstLeaderAbility = new JButton("Use Leader Ability");
		useFirstLeaderAbility.addActionListener(this);
		useSecondLeaderAbility = new JButton("Use Leader Ability");
		useSecondLeaderAbility.addActionListener(this);
		JPanel up = new JPanel(new GridLayout(1, 2));
		up.setPreferredSize(new Dimension(640,1000));
		JLabel firstPlayerInfo = new JLabel();
		setUpPlayerLabels(firstPlayerInfo, game.getFirstPlayer());
		up.add(firstPlayerInfo);
		JLabel secondPlayerInfo = new JLabel();
		setUpPlayerLabels(secondPlayerInfo, game.getSecondPlayer());
		up.add(secondPlayerInfo);
		panel.add(up, BorderLayout.NORTH);
		gamepanel= new JPanel();
		panel.add(gamepanel, BorderLayout.CENTER);


		JPanel curr = new JPanel(new FlowLayout());
		curr.setPreferredSize(new Dimension(320, 720));
		curr.setLocation(960,0);
		JLabel label = new JLabel();
		label.setText("Current Champion: \n\n");
		label.setBackground(new Color(123,208,20));
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setForeground(new Color(0xE2F516));
		curr.add(label, BorderLayout.NORTH);
		current_champ_info = new JTextArea();
		current_champ_info.setPreferredSize(new Dimension(319, 350));
		current_champ_info.setEditable(false);
		current_champ_info.setFont(new Font("MV Boli", Font.BOLD, 15));
		current_champ_info.setAlignmentX(CENTER_ALIGNMENT);
		current_champ_info.setAlignmentY(CENTER_ALIGNMENT);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		current_champ_info.setBorder(border);
		curr.add(current_champ_info);
		add(curr, BorderLayout.EAST);

		JPanel bottom = new JPanel(new FlowLayout());
		turnsQueue = new JPanel(new GridLayout(1, 6, 2, 0));
//		turnsQueue.setPreferredSize(new Dimension(300,350));
		JTextArea turnOrderLabel = new JTextArea("Champions Turn Order ");
		turnOrderLabel.setEditable(false);
		turnOrderLabel.setFont(new Font("Arial", Font.BOLD, 20));
		bottom.add(turnOrderLabel, BorderLayout.NORTH);
		bottom.add(turnsQueue, BorderLayout.SOUTH);
//		bottom.setBackground(Color.GREEN);
		panel.add(bottom, BorderLayout.SOUTH);
		add(panel, BorderLayout.CENTER);
		
		left = new JPanel(new FlowLayout());
		left.setPreferredSize(new Dimension(320, 720));
		left.setLocation(0,0);
		label = new JLabel();
		label.setText("Current Champion: \n\n");
		label.setBackground(new Color(123,208,20));
		label.setForeground(new Color(0xE2F516));
		label.setFont(new Font("Arial", Font.BOLD, 20));
		label.setVerticalTextPosition(JLabel.TOP);
		left.add(label, BorderLayout.NORTH);
		currentChampionPhoto = new JLabel();
		left.add(currentChampionPhoto);
	
		frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setTitle("Marvel Combat");
		image = new ImageIcon(components_package + "Icon_game.jpg");
		frame.setIconImage(image.getImage());
		p = new JPanel();
		frame.setBounds(0, 0, 400, 400);
		frame.add(p, BorderLayout.CENTER);
		
		current_champ_effects = new JTextArea();
		current_champ_effects.setLayout(new FlowLayout());
		current_champ_effects.setPreferredSize(new Dimension(319, 50));
		current_champ_effects.setEditable(false);
		current_champ_effects.setFont(new Font("MV Boli", Font.BOLD, 15));
		current_champ_effects.setAlignmentX(CENTER_ALIGNMENT);
		current_champ_effects.setAlignmentY(CENTER_ALIGNMENT);
		border = BorderFactory.createLineBorder(Color.BLACK, 1);
		current_champ_effects.setBorder(border);
		
		JPanel actions = new JPanel(new GridLayout(3, 1, 0, 5));
		actions.setPreferredSize(new Dimension(300, 520));
		actions.setBackground(new Color(0x123456));
		JPanel movements = new JPanel(new GridLayout(2, 2, 5, 5));
		int sizeX = 100;
		int sizeY = 10;
		all_actions = new ArrayList<>();
//		int s = 20;
		Up = new JButton(new ImageIcon(components_package + "up.png"));
		Up.setSize(sizeX, sizeY);
		Up.addActionListener(this);
		Down = new JButton(new ImageIcon(components_package + "down.png"));
		Down.setSize(sizeX, sizeY);
		Down.addActionListener(this);
		Right = new JButton(new ImageIcon(components_package + "right.png"));
		Right.setSize(sizeX, sizeY);
		Right.addActionListener(this);
		Left = new JButton(new ImageIcon(components_package + "left.png"));
		Left.setSize(sizeX, sizeY);
		Left.addActionListener(this);
		movements.add(Up);
		movements.add(Down);
		movements.add(Left);
		movements.add(Right);
		attack = new JButton(new ImageIcon(components_package + "attack.png"));
		attack.setBackground(Color.WHITE);
		attack.setText("Attack");
		attack.setFont(new Font("Arial", Font.BOLD, 20));
		attack.addActionListener(this);
		endTurn = new JButton("End Turn");
		endTurn.addActionListener(this);
		endTurn.setFont(new Font("Arial", Font.BOLD, 20));
		boardButtons = new JButton[5][5];
		abilities = new ArrayList<>();
		abilitiesIcons = new ArrayList<>();
		updateGameBoard();
		abilitiesPanel = new JPanel();
		abilitiesPanel.setPreferredSize(new Dimension(200, 250));
		curr.add(abilitiesPanel);
		updateCurrentChampionDetails();
		actions.add(attack);
		actions.add(movements);
		actions.add(endTurn, BorderLayout.SOUTH);
		left.add(actions);
		add(left, BorderLayout.WEST);
        updateTurnOrder(); 	  
        gamepanel.setBackground(new Color(0x123456));
		gamepanel.setOpaque(true);
        curr.setBackground(new Color(0x123456));
		curr.setOpaque(true);
		left.setBackground(new Color(0x123456));
		left.setOpaque(true);
		all_actions.add(Up);
		all_actions.add(Down);
		all_actions.add(Left);
		all_actions.add(Right);
		all_actions.add(endTurn);
		all_actions.add(useFirstLeaderAbility);
		all_actions.add(useSecondLeaderAbility);
		all_actions.add(attack);
		repaint();
		validate();
		setVisible(true);
	}
	
	private void setUpPlayerLabels(JLabel l, Player p)
	{
		l.setLayout(new GridLayout(2, 1));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
		l.setBorder(border);
		JLabel label = new JLabel();
		label.setText("Player Name: " + p.getName());
		label.setVerticalTextPosition(JLabel.CENTER);
		label.setForeground(new Color(808000)); // text color
		label.setFont(new Font("Arial", Font.BOLD, 25)); // set font of text
		label.setBackground(Color.GRAY); 
		label.setOpaque(true); // display background color
		label.setVerticalAlignment(JLabel.CENTER);
		label.setHorizontalAlignment(JLabel.CENTER) ;
		label.setBounds(100, 100, 250, 250);
		l.add(label);
		JButton temp;
		String s =  " Leader Ability Used: ";
		if(p == game.getFirstPlayer())
		{
			firstLeader = new JLabel();
			firstLeader.setLayout(new FlowLayout());
			firstLeader.add(useFirstLeaderAbility, BorderLayout.NORTH);
			temp = useFirstLeaderAbility;
			s += game.isFirstLeaderAbilityUsed() ? "Yes" : "No";
			firstLeader.setText(s);
			label = firstLeader;
		}
		else
		{
			secondLeader = new JLabel();
			secondLeader.setLayout(new FlowLayout());
			secondLeader.add(useSecondLeaderAbility, BorderLayout.NORTH);
			temp = useSecondLeaderAbility;
			s += game.isSecondLeaderAbilityUsed() ? "Yes" : "No";
			secondLeader.setText(s);
			label = secondLeader;
		}
		temp.setVerticalAlignment(JButton.TOP);
		temp.setHorizontalAlignment(JButton.CENTER);
		temp.setFont(new Font("Arial", Font.BOLD, 17));
		temp.setVerticalTextPosition(JButton.CENTER);
		temp.setHorizontalTextPosition(JButton.CENTER);
		temp.setPreferredSize(new Dimension(200, 40));
		label.setText(s);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setForeground(Color.DARK_GRAY); // text color
		label.setFont(new Font("Arial", Font.BOLD, 25)); // set font of text
		label.setBackground(Color.LIGHT_GRAY); 
		label.setOpaque(true); // display background color
		label.setVerticalAlignment(JLabel.CENTER);
		label.setHorizontalAlignment(JLabel.CENTER) ;
		label.setBounds(100, 100, 250, 250);
		l.add(label);
	}
	
	private Clip getSound(String f) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		File file = new File(f);
		AudioInputStream audio_stream = AudioSystem.getAudioInputStream(file);
		Clip c = AudioSystem.getClip();
		c.open(audio_stream);
		return c;
	}
	
	private void updateCurrentChampionDetails()
	{	
		current_champ_info.setText("Team: " + game.getChampionPlayer(game.getCurrentChampion()).getName() + "\n" + game.getCurrentChampion().toString(true));
		if(game.getChampionPlayer(game.getCurrentChampion()) == game.getFirstPlayer())
		{
			useFirstLeaderAbility.setEnabled(true);
			useSecondLeaderAbility.setEnabled(false);
		}
		else
		{
			useSecondLeaderAbility.setEnabled(true);
			useFirstLeaderAbility.setEnabled(false);
		}
		if(! game.getCurrentChampion().getAppliedEffects().isEmpty())
		{
			left.add(current_champ_effects, BorderLayout.WEST);
			current_champ_effects.setText(game.getCurrentChampion().showAppliedEffects());
		}
		currentChampionPhoto.setIcon(new ImageIcon(components_package + game.getCurrentChampion().getName() + ".png"));
		showAbilites();
	}
	
	private void showAbilites()
	{
		abilitiesPanel.removeAll();
		abilitiesPanel.setBackground(new Color(0x123456));
		int abilities_number = game.getCurrentChampion().getAbilities().size();
		abilitiesPanel.setLayout(new GridLayout(abilities_number, 1, 0, 3));
		
		while(! abilitiesIcons.isEmpty())
		{
			abilitiesIcons.remove(abilitiesIcons.size() - 1);
		}
		abilities.clear();
		int i = 1;
		for(Ability ability: game.getCurrentChampion().getAbilities())
		{
			String image = "";
			if(ability instanceof DamagingAbility)
				image = "damaging Ability";
			else if(ability instanceof HealingAbility)
				image = "healing Ability";
			else if(ability instanceof CrowdControlAbility c)
			{
				if(c.getEffect().getType().equals(EffectType.BUFF))
					image = "BUFF effect";
				else
					image = "DEBUFF effect";
			}
			JButton button = new JButton(new ImageIcon(components_package + image + ".png"));
			button.setText("Ability " + i);
			button.setFont(new Font("Arial", Font.BOLD, 20));
			button.setBackground(Color.WHITE);
			button.addActionListener(this);
			abilitiesIcons.add(button);
			abilitiesPanel.add(button);
			
			button = new JButton("Cast Ability");
			button.addActionListener(this);
			button.setPreferredSize(new Dimension(100, 50));
			abilities.add(button);
			i++;
		}
	}
	// 		abilityAttributes.setPreferredSize(new Dimension(210, 180));

	
	private void showAbilityAttributes(int index)
	{
		Ability ability = game.getCurrentChampion().getAbilities().get(index);
		JButton button = abilities.get(index);
		p.removeAll();
		JTextArea abilityAttributes = new JTextArea();
		abilityAttributes.setEditable(false);
		abilityAttributes.setFont(new Font("MV Boli", Font.BOLD, 16));
		abilityAttributes.setAlignmentX(CENTER_ALIGNMENT);
		abilityAttributes.setAlignmentY(CENTER_ALIGNMENT);
		abilityAttributes.setLayout(new FlowLayout());
		abilityAttributes.setText(ability.toString(0));
		abilityAttributes.validate();
		p.add(abilityAttributes, BorderLayout.NORTH);
		p.add(button, BorderLayout.SOUTH);
		p.validate();
		frame.pack();
		frame.setVisible(true);
	}
	
	private void updateGameBoard()
	{
		Object[][] board = game.getBoard();
		gamepanel.removeAll();
		gamepanel.setLayout(new GridLayout(5, 5));
		gamepanel.setPreferredSize(new Dimension(640, 700));
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				JButton button = new JButton();
				button.setBackground(Color.WHITE);
				ImageIcon image;
				if(board[i][j] == null)
				{
					button.setEnabled(false);
				}
				else
				{
					if(board[i][j] instanceof Champion c)
					{
						image = new ImageIcon(components_package + c.getName() + "G.png");
//						button.setText(c.getName());
					}
					else
					{
						image = new ImageIcon(components_package + "gameCover.gif");
//						button.setText("Cover");
					}
					button.setIcon(image);
					button.addActionListener(this);
				}
				gamepanel.add(button);
				boardButtons[i][j] = button;
			}
		}
		repaint();
		validate();
	}
	private Point contains(JButton b)
	{
		for(int i = 0; i < boardButtons.length; i++)
			for(int j = 0; j < boardButtons[0].length; j++)
				if(boardButtons[i][j] == b)
					return new Point(i, j);
		return null;
	}
	
	private void viewDamageableAttributes(Point p)
	{
		if(text_written)
			attributeFrame.remove(text);
		int x = p.x, y = p.y;
		Object object = game.getBoard()[x][y];
		int height;
		if(object instanceof Champion champ)
		{
			if(champ != game.getCurrentChampion())
			{
				text.setText("Team: " + game.getChampionPlayer(champ).getName() + "\n" + champ.toString(false));
				height = champ.getAppliedEffects().isEmpty() ? 300 : 400;
			}
			else
			{
				text.setText("Your Current Champion");
				height = 100;
			}
		}
		else
		{
			Cover cover = (Cover)object;
			text.setText(cover.toString());
			height = 100;
		}
		text.setFont(new Font("Arial", Font.BOLD, 15));
		attributeFrame.add(text);
		attributeFrame.setPreferredSize(new Dimension(300, height));
		attributeFrame.pack();
		attributeFrame.setVisible(true);
	}
	
	private void updateTurnOrder()
	{
		PriorityQueue turnOrder = game.getTurnOrder();
		turnsQueue.removeAll();
		ArrayList<Champion> temp = new ArrayList<>();
		while(! turnOrder.isEmpty())
		{
			Champion c = (Champion)turnOrder.peekMin();
			if(! c.getCondition().equals(Condition.INACTIVE))
			{
				JLabel label = new JLabel(new ImageIcon(components_package + c.getName() + ".png"));
				label.setText(c.getName());
				label.setFont(new Font("Arial", Font.BOLD, 20));
				label.setPreferredSize(new Dimension(130, 200));
				turnsQueue.add(label);
			}
			temp.add(c);
			turnOrder.remove();
		}
		for(Champion c: temp)
		{
			turnOrder.insert(c);
		}
	}
	private void showException(String s)
	{
		JOptionPane.showMessageDialog(null, s, "Warning", JOptionPane.WARNING_MESSAGE);
	}
	
	private void DirectionSetter(boolean getAll, boolean enable)
	{
		Point p = game.getCurrentChampion().getLocation();
		int x = p.x;
		int y = p.y;
		for(int i = 0; i < 5; i++)
		{
			for(int j = 0; j < 5; j++)
			{
				if(game.getBoard()[i][j] != null)
					boardButtons[i][j].setEnabled(enable);
			}
		}
		boardButtons[x][y].setEnabled(true);
		if(enable)
			return;
		for(int i = x + 1; i < 5; i++)
		{
			if(game.getBoard()[i][y] instanceof Damageable)
			{
				boardButtons[i][y].setEnabled(true);
				if(! getAll)
					break;
			}
		}
		for(int i = x - 1; i >= 0; i--)
		{
			if(game.getBoard()[i][y] instanceof Damageable)
			{
				boardButtons[i][y].setEnabled(true);
				if(! getAll)
					break;
			}
		}
		for(int j = y + 1; j < 5; j++)
		{
			if(game.getBoard()[x][j] instanceof Damageable)
			{
				boardButtons[x][j].setEnabled(true);
				if(! getAll)
					break;
			}
		}
		for(int j = y - 1; j >= 0; j--)
		{
			if(game.getBoard()[x][j] instanceof Damageable)
			{
				boardButtons[x][j].setEnabled(true);
				if(! getAll)
					break;
			}
		}
	}
	
	private void setButtonsWhileFunctioning(boolean enable, JButton b)
	{
		for(JButton button: all_actions)
		{
			if (! (castingDirectionAbility && (button == Up || button == Down || button == Left || button == Right)))
				button.setEnabled(enable);
		}
		for(JButton button: abilitiesIcons)
		{
			button.setEnabled(enable);
		}
		if(enable)
		{
			if(game.getChampionPlayer(game.getCurrentChampion()) == game.getFirstPlayer())
			{
				useSecondLeaderAbility.setEnabled(false);
			}
			else
			{
				useFirstLeaderAbility.setEnabled(false);
			}
		}
		b.setEnabled(true);
	}
	
	private void checkGameOver()
	{
		if(game.checkGameOver() != null)
		{
			try 
			{
				new WinnerDeclaration(game);
			} 
			catch (UnsupportedAudioFileException | LineUnavailableException | IOException e)
			{
			}
			dispose();
		}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		JButton button = (JButton)e.getSource();
		if(button == sound) 
		{
			if(clip.isRunning())
			{
				clip.stop();
				sound.setIcon(new ImageIcon(components_package + "components/mute_audio.png"));
			}
			else
			{
				clip.start();
				sound.setIcon(new ImageIcon(components_package + "components/audio.png"));
			}
		}
		Point point = contains(button);
		if(point != null)
		{
			if(attacking)
			{
				int xTarget = point.x;
				int yTarget = point.y;
				Point p = game.getCurrentChampion().getLocation();
				Direction d = null;
				if(xTarget > p.x)
				{
					d = Direction.UP;
				}
				else if(xTarget < p.x)
				{
					d = Direction.DOWN;
				}
				else if(yTarget > p.y)
				{
					d = Direction.RIGHT;
				}
				else if(yTarget < p.y)
				{
					d = Direction.LEFT;
				}
				try 
				{
					if(d != null)
						game.attack(d);
				}
				catch (ChampionDisarmedException e11) 
				{
					showException("Can not perform an Attack while you are Disarmed");
				}
				catch (NotEnoughResourcesException e1)
				{
					showException("Not enough Action Points !");
				}
				if(d != null)
				{
					attacking = false;
					if(game.isAttackEffect())
					{
						attack_sound.start();
						game.setAttackEffect(false);
					}
					checkGameOver();
					updateCurrentChampionDetails();
					updateGameBoard();
					setButtonsWhileFunctioning(true, button);
					updateTurnOrder();
				}
			}
			else if(castingSingleAbility)
			{
				Point p = contains(button);
				try 
				{
					assert p != null;
					game.castAbility(castedAbility, p.x, p.y);
				} 
				catch (NotEnoughResourcesException | InvalidTargetException | AbilityUseException e12)
				{
					showException(e12.getMessage());
				} catch (CloneNotSupportedException e14)
				{
//					e14.printStackTrace();
				}
				castingSingleAbility = false;
				if(game.isAbilityEffect())
				{
					if(game.isFriendlyAbility(castedAbility))
					{
						PositiveAbility_sound.start();
					}
					else
					{
						HarmingAbility_sound.start();
					}
					game.setAbilityEffect(false);
				}
				checkGameOver();
				updateCurrentChampionDetails();
				updateGameBoard();
				setButtonsWhileFunctioning(true, button);
				updateTurnOrder();
			}
			else if(castingDirectionAbility)
			{
				JOptionPane.showMessageDialog(null, "Choose A direction from the arrows to cast your Ability : )", "Marvel Guide", JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				attributeFrame.dispose();
				viewDamageableAttributes(point);
			}
		}
		else
		{
			Direction d = null;
			if(button == Up)
			{
				d = Direction.DOWN;
			}
			else if(button == Down)
			{
				d = Direction.UP;
			}
			else if(button == Right)
			{
				d = Direction.RIGHT;
			}
			else if(button == Left)
			{
				d = Direction.LEFT;
			}
			if(d != null  && ! castingDirectionAbility)
			{
				try 
				{
					game.move(d);
					updateGameBoard();
					updateCurrentChampionDetails();
				} 
				catch (UnallowedMovementException e1) 
				{
					showException(e1.getMessage());
				}
				catch(NotEnoughResourcesException e2)
				{
					showException("Not enough Action Points !");
				}
			}
			else if(d != null)
			{
				if(castedAbility != null)
				{
					try 
					{
						game.castAbility(castedAbility, d);
					} 
					catch (NotEnoughResourcesException | AbilityUseException e12)
					{
						showException(e12.getMessage());
					} catch (CloneNotSupportedException e14)
					{
//						e14.printStackTrace();
					}
					castingDirectionAbility = false;
					if(game.isAbilityEffect())
					{
						if(game.isFriendlyAbility(castedAbility))
						{
							PositiveAbility_sound.start();
						}
						else
						{
							HarmingAbility_sound.start();
						}
						game.setAbilityEffect(false);
					}
					checkGameOver();
					
					updateCurrentChampionDetails();
				
					updateGameBoard();
					setButtonsWhileFunctioning(true, button);
					updateTurnOrder();
				}
			}
			else if(button == endTurn)
			{
				game.endTurn();
				updateGameBoard();
				updateCurrentChampionDetails();
				updateTurnOrder();
				
			}
			else if(button == useFirstLeaderAbility || button == useSecondLeaderAbility)
			{
				try 
				{
					game.useLeaderAbility();
					String s = "Leader Ability Used: Yes";
					if(button == useFirstLeaderAbility)
					{
						firstLeader.setText(s);
					}
					else
					{
						secondLeader.setText(s);
					}
					if(game.isLeaderAbilityEffect())
					{
						LeaderAbility_sound.start();
						game.setLeaderAbilityEffect(false);
					}
					checkGameOver();
					updateGameBoard();
					updateCurrentChampionDetails();
					updateTurnOrder();
				} 
				catch (LeaderAbilityAlreadyUsedException e3)  {
					showException("Can not use your Leader ability more than once");
				}
				catch (LeaderNotCurrentException e4)
				{
					showException("Only your Team Leader Champion is allowed to use leader ability");
				}
				catch(AbilityUseException e5)
				{
					showException("Cannot Use Leader ability while you are Silenced !");
				}
			}
			else if(button == attack)
			{
				if(! attacking)
				{
					attacking = true;
					setButtonsWhileFunctioning(false, button);
					DirectionSetter(false, false);
				}
				else
				{
					attacking = false;
					setButtonsWhileFunctioning(true, button);
					DirectionSetter(false, true);
				}
			}
			
			else if(abilitiesIcons.contains(button))
			{
				int index = abilitiesIcons.indexOf(button);
				showAbilityAttributes(index);
			}
			else if(abilities.contains(button))
			{
				int i = abilities.indexOf(button);
				frame.dispose();
				Ability a = game.getCurrentChampion().getAbilities().get(i);
				if(! (a.getCastArea().equals(AreaOfEffect.DIRECTIONAL) || a.getCastArea().equals(AreaOfEffect.SINGLETARGET)))
				{
					try 
					{
						game.castAbility(a);
					}
					catch (NotEnoughResourcesException | AbilityUseException e12)
					{
						showException(e12.getMessage());
					} catch (CloneNotSupportedException e14)
					{
//						e14.printStackTrace();
					}
					if(game.isAbilityEffect())
					{
						if(game.isFriendlyAbility(castedAbility))
						{
							PositiveAbility_sound.start();
						}
						else
						{
							HarmingAbility_sound.start();
						}
						game.setAbilityEffect(false);
					}
					checkGameOver();
					updateGameBoard();
					updateCurrentChampionDetails();
					updateTurnOrder();
				}
				else if(a.getCastArea().equals(AreaOfEffect.DIRECTIONAL))
				{
					castingDirectionAbility = true;
					DirectionSetter(true, false);
					setButtonsWhileFunctioning(false, button);
					castedAbility = a;
				}
				else
				{
					castingSingleAbility = true;
					setButtonsWhileFunctioning(false, button);
					castedAbility = a;
				}
			}
		}
	}
	
//	For Testing
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
		new MainView(g);
	}

}
