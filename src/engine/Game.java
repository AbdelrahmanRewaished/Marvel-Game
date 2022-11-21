package engine;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.SplittableRandom;
import exceptions.*;
import java.util.ArrayList;
import model.abilities.*;
import model.effects.*;
import model.world.*;
import java.awt.Point;

public class Game {
	private final Player firstPlayer;
	private final Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final Object [][] board;
	private static final ArrayList<Champion> availableChampions = new ArrayList<>();
	private static final ArrayList<Ability> availableAbilities = new ArrayList<>();
	private final ArrayList<Cover> covers;
	private final PriorityQueue turnOrder;
	private final static int BOARDHEIGHT = 5;
	private final static int BOARDWIDTH = 5;
	private boolean attackEffect;
	private boolean abilityEffect;
	private boolean leaderAbilityEffect;
	
	public Game(Player first, Player second) throws Exception 
	{
		this.firstPlayer = first;
		this.secondPlayer = second;
		board = new Object[BOARDHEIGHT][BOARDWIDTH];
		turnOrder = new PriorityQueue(6);
		prepareChampionTurns();
		placeChampions();
		covers = new ArrayList<>();
		placeCovers();
		firstLeaderAbilityUsed = false;
		secondLeaderAbilityUsed = false;
	}
	public ArrayList<Cover> getCovers() {
		return covers;
	}
	public Player getFirstPlayer() {
		return firstPlayer;
	}
	public Player getSecondPlayer() {
		return secondPlayer;
	}
	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}
	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}
	public Object[][] getBoard() {
		return board;
	}
	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isAttackEffect()
	{
		return attackEffect;
	}
	public void setAttackEffect(boolean attackEffect) {
		this.attackEffect = attackEffect;
	}
	public boolean isAbilityEffect() {
		return abilityEffect;
	}
	public void setAbilityEffect(boolean abilityEffect) {
		this.abilityEffect = abilityEffect;
	}
	public boolean isLeaderAbilityEffect() {
		return leaderAbilityEffect;
	}
	public void setLeaderAbilityEffect(boolean leaderAbilityEffect) {
		this.leaderAbilityEffect = leaderAbilityEffect;
	}
	
	private void placeChampions() {

		ArrayList<Champion> first_team = firstPlayer.getTeam();
		ArrayList<Champion> second_team = secondPlayer.getTeam();
		for(int i = 1; i < 4; i++) 
		{
			int j = i - 1;
			
			if(j < first_team.size())
			{
				Champion champ1 = first_team.get(j);
				board[0][i] = champ1;
				champ1.setLocation(new Point(0, i));
			}	
			if(j < second_team.size())
			{
				Champion champ2 = second_team.get(j);
				board[BOARDHEIGHT - 1][i] = champ2;
				champ2.setLocation(new Point(BOARDHEIGHT - 1, i));
			}
		}
	}
	private void placeCovers()
	{
		ArrayList<int[]> positions = new ArrayList<>();
		int[] position;
		for (int i = 1; i < 4; i++)
		{
			for (int j = 0; j < 5; j++)
			{
				position = new int[2];
				position[0] = i;
				position[1] = j;
				positions.add(position);
			}
		}
		SplittableRandom random = new SplittableRandom();
		for(int i = 0; i < 5; i++)
		{
			int position_index = random.nextInt(positions.size());
			position = positions.get(position_index);
			int x = position[0];
			int y = position[1];
			Cover c = new Cover(x, y);
			covers.add(c);
			board[x][y] = c;
			positions.remove(position_index);
		}
	}
	
	private static Effect getEffect(String effect_name, int duration)
	{
		return switch (effect_name) {
			case "Disarm" -> new Disarm(duration);
			case "PowerUp" -> new PowerUp(duration);
			case "Shield" -> new Shield(duration);
			case "Silence" -> new Silence(duration);
			case "SpeedUp" -> new SpeedUp(duration);
			case "Embrace" -> new Embrace(duration);
			case "Root" -> new Root(duration);
			case "Shock" -> new Shock(duration);
			case "Dodge" -> new Dodge(duration);
			case "Stun" -> new Stun(duration);
			default -> null;
		};

	}
	private static Ability abilityType(String type, String name, int manaCost, int castRange, int baseCooldown, AreaOfEffect effect_area, int reqActionPerTurn, String x, String dur)
	{
		Ability ability = null;
		switch (type) {
			case "DMG" -> {
				int damageAmount = Integer.parseInt(x);
				ability = new DamagingAbility(name, manaCost, baseCooldown, castRange, effect_area, reqActionPerTurn, damageAmount);
			}
			case "HEL" -> {
				int healAmount = Integer.parseInt(x);
				ability = new HealingAbility(name, manaCost, baseCooldown, castRange, effect_area, reqActionPerTurn, healAmount);
			}
			case "CC" -> {
				int duration = Integer.parseInt(dur);
				Effect ef = getEffect(x, duration);
				ability = new CrowdControlAbility(name, manaCost, baseCooldown, castRange, effect_area, reqActionPerTurn, ef);
			}
		}
		return ability;
	}
	private static Ability getAbility(String line)
	{
		String[] this_line_content = line.split(",");
		String type = this_line_content[0];
		String name = this_line_content[1];
		int manaCost = Integer.parseInt(this_line_content[2]);
		int castRange = Integer.parseInt(this_line_content[3]);
		int baseCooldown = Integer.parseInt(this_line_content[4]);
		
		String ea = this_line_content[5];
		AreaOfEffect effect_area = AreaOfEffect.valueOf(ea);
	
		int reqActionPerTurn = Integer.parseInt(this_line_content[6]);
		String x = this_line_content[7];
		String duration = null;
		if(type.equals("CC"))
			duration = this_line_content[8];
		
		return abilityType(type, name, manaCost, castRange, baseCooldown, effect_area, reqActionPerTurn, x, duration);
	}
	public static void loadAbilities(String filePath) throws Exception
	{
		BufferedReader br  = new BufferedReader(new FileReader(filePath));
		String this_line = br.readLine();
		while(this_line != null)
		{
			Ability ability = getAbility(this_line);	
			availableAbilities.add(ability);
			this_line = br.readLine();
		} 
		br.close();
	}
	private static Ability getCorrespondingAbility(String name)
	{
		for (Ability ability : Game.availableAbilities) {
			if (ability.getName().equals(name))
				return ability;
		}
		return null;
	}
	private static Champion championType(String type, String name, int maxHP, int mana, int actions, int speed, int attackRange, int attackDamage, String ab1
			,String ab2, String ab3)
	{
		char champ = type.charAt(0);
		Champion champion = switch (champ) {
			case 'H' -> new Hero(name, maxHP, mana, actions, speed, attackRange, attackDamage);
			case 'V' -> new Villain(name, maxHP, mana, actions, speed, attackRange, attackDamage);
			case 'A' -> new AntiHero(name, maxHP, mana, actions, speed, attackRange, attackDamage);
			default -> null;
		};
		Ability ability1 = getCorrespondingAbility(ab1);
		Ability ability2 = getCorrespondingAbility(ab2);
		Ability ability3 = getCorrespondingAbility(ab3);
		assert champion != null;

		champion.getAbilities().add(ability1);
		champion.getAbilities().add(ability2);
		champion.getAbilities().add(ability3);
		return champion;
	}
	private static Champion getChampion(String line) 
	{
		
		String[] this_line_content = line.split(",");
		String type = this_line_content[0];
		String name = this_line_content[1];
		int maxHP = Integer.parseInt(this_line_content[2]);
		int mana = Integer.parseInt(this_line_content[3]);
		int actions = Integer.parseInt(this_line_content[4]);
		int speed = Integer.parseInt(this_line_content[5]);
		int attackRange = Integer.parseInt(this_line_content[6]);
		int attackDamage = Integer.parseInt(this_line_content[7]);
		String ability1 = this_line_content[8];
		String ability2 = this_line_content[9];
		String ability3 = this_line_content[10];
		return championType(type, name, maxHP, mana, actions, speed, attackRange, attackDamage, ability1, ability2, ability3);	
	}
	public static void loadChampions(String filePath) throws Exception
	{
		BufferedReader br  = new BufferedReader(new FileReader(filePath));
		String this_line = br.readLine();
		while(this_line != null)
		{
			Champion champion = getChampion(this_line);	
			availableChampions.add(champion);
			this_line = br.readLine();
		} 
		br.close();
	}
	
	public Champion getCurrentChampion()
	{
		return (Champion)turnOrder.peekMin();
	}
	public Player checkGameOver()
	{
		boolean empty_first_team = firstPlayer.getTeam().isEmpty();
		boolean empty_second_team = secondPlayer.getTeam().isEmpty();
		if(empty_first_team)
			return secondPlayer;
		else if(empty_second_team)
			return firstPlayer;
		return null;		
	}
	
	private void replaceCurrentPlace(Champion c, int x, int y)
	{
		int champ_current_action_points = c.getCurrentActionPoints();
		c.setCurrentActionPoints(champ_current_action_points - 1);
		c.setLocation(new Point(x, y));
		board[x][y] = c;
		
	}
	
	private boolean canMove(Champion c) throws UnallowedMovementException, NotEnoughResourcesException
	{
		Condition condition = c.getCondition();
		boolean notAllowedMovement = condition.equals(Condition.ROOTED) || condition.equals(Condition.INACTIVE);
		boolean notAllowedAction = c.getCurrentActionPoints() < 1;
		if(notAllowedAction)
			throw new NotEnoughResourcesException();
		if(notAllowedMovement)
			throw new UnallowedMovementException("Can not Move while You are Rooted");
		return true;
	}
	
	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException
	{
		
		Champion current_champion = getCurrentChampion();
		Point p = current_champion.getLocation();
		int x = p.x;
		int y = p.y;
		if(canMove(current_champion))
		{
			switch(d)
			{
				case LEFT:
					if(y > 0 && board[x][y - 1] == null)
					{
						board[x][y--] = null;
					}
					else 
						throw new UnallowedMovementException("Can not move");
					break;
				case RIGHT:
					if(y < BOARDWIDTH - 1 && board[x][y + 1] == null)
					{
						board[x][y++] = null;
					}
					else 
						throw new UnallowedMovementException("Can not Move");
					break;
				case DOWN:
					if(x > 0 && board[x - 1][y] == null)
					{
						board[x--][y] = null;
					}
					else 
						throw new UnallowedMovementException("Can not Move");
					break;
				case UP:
					if(x < BOARDHEIGHT - 1 && board[x + 1][y] == null)
					{
						board[x++][y] = null;
					}
					else 
						throw new UnallowedMovementException("Can not Move");
					break;		
			}
			replaceCurrentPlace(current_champion, x, y);
		}
	}
	
	private ArrayList<Champion> getCorrespondingTeam(Champion c)
	{
		ArrayList<Champion> team1 = firstPlayer.getTeam();
		ArrayList<Champion> team2 = secondPlayer.getTeam();
		if(team1.contains(c))
			return team1;
		else
			return team2;
	}
	
	private ArrayList<Champion> getOpposingTeam(Champion c)
	{
		ArrayList<Champion> team1 = firstPlayer.getTeam();
		ArrayList<Champion> team2 = secondPlayer.getTeam();
		if(team1.contains(c))
			return team2;
		else
			return team1;
	}
	
	private boolean areOpponents(Champion c1, Champion c2)
	{
		return getOpposingTeam(c1).contains(c2);
	}
	
	private boolean validPoint(Point p)
	{
		return p.x >= 0 && p.x < BOARDHEIGHT && p.y >= 0 && p.y < BOARDWIDTH;
	}
	
	private boolean allowedDistance(Champion c, Ability a, Point p) 
	{
		return  validPoint(p) && getDistance(c.getLocation(), p) <= a.getCastRange();
	}
	
	private boolean isNormalDamage(Champion champion, Champion target)
	{
		boolean is_heros = champion instanceof Hero && target instanceof Hero;
		boolean is_villains = champion instanceof Villain && target instanceof Villain;
		boolean is_anti_heros = champion instanceof AntiHero && target instanceof AntiHero;
		return is_heros || is_villains || is_anti_heros;
	}
	
	private int getDistance(Point p1, Point p2)
	{
		int x1 = p1.x;
		int y1 = p1.y;
		int x2 = p2.x;
		int y2 = p2.y;
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}
	
	private boolean allowedDistance(Champion c, Point p) 
	{
		return validPoint(p) && getDistance(c.getLocation(), p) <= c.getAttackRange();	
	}
	
	private boolean isKnockedOut(Damageable d)
	{
		return d.getCurrentHP() == 0 || (d instanceof Champion && ((Champion)d).getCondition().equals(Condition.KNOCKEDOUT));
	}
	
	private void decreaseTargetHealth(Damageable target, int damage)
	{
		target.setCurrentHP(target.getCurrentHP() - damage);
		if(isKnockedOut(target))
		{
			Point p = target.getLocation();
			board[p.x][p.y] = null;		
			if(target instanceof Champion champion)
			{
				champion.setCondition(Condition.KNOCKEDOUT);
				getCorrespondingTeam(champion).remove(champion);
				ArrayList<Champion> temp = new ArrayList<>();
				while(! turnOrder.isEmpty())
				{
					champion = (Champion)turnOrder.remove();
					if(champion == target)
						break;
					temp.add(champion);
				}
				while(! temp.isEmpty())
				{
					turnOrder.insert(temp.remove(0));
				}
			}
		}
	}
	
	 private static int generateRandom()
	 {
	    	SplittableRandom random = new SplittableRandom();
			return random.nextInt(0, 2);
	 }
	
	private boolean attackTarget(int x, int y, Champion c)
	{
		// Initializations
		boolean exist = false;
		Damageable target;
		
		if(board[x][y] != null) 
		{
			exist = true;   // found a target
			target = (Damageable)board[x][y];
			int damage = 0;
			if(target instanceof Cover)
			{
				damage = c.getAttackDamage();
				attackEffect = true;
			}
			else if(areOpponents(c, (Champion)target))
			{
				if(hasEffect((Champion)target, "Dodge") &&  generateRandom() == 1)
				{
					return true;
				}
				if(isNormalDamage(c, (Champion)target))
					damage = c.getAttackDamage();
				else
					damage = c.getAttackDamage() + c.getAttackDamage() / 2;
				attackEffect = true;
			}
			decreaseTargetHealth(target, damage);
		}
		return exist;
	} 
	
	public void attack(Direction d) throws ChampionDisarmedException, NotEnoughResourcesException
	{
		Champion c = getCurrentChampion();
		int current_actions = c.getCurrentActionPoints();
		boolean enoughActions = current_actions >= 2;

		if(! isInactive(c))
		{
			if(! enoughActions)
				throw new NotEnoughResourcesException();
		    if(hasEffect(c, "Disarm"))
				throw new ChampionDisarmedException();
			else
			{	
				// Initializations
				Point p = c.getLocation();
				int x = p.x;
				int y = p.y;
				boolean noAttack;
				do
				{
					switch (d) {
						case DOWN -> x--;
						case UP -> x++;
						case RIGHT -> y++;
						case LEFT -> y--;
					}
					p = new Point(x, y);
					if(allowedDistance(c, p))
					{
						noAttack = ! attackTarget(x, y, c);
					}
					else
					{
						break;
					}
				} while(noAttack);
				c.setCurrentActionPoints(current_actions - 2); 		
			}
		}
	}
		
	
	private boolean enoughMana(Champion c, Ability a)
	{
		return c.getMana() >= a.getManaCost();
	}
	
	private boolean hasOpposingEffect(Ability a)
	{
		return a instanceof CrowdControlAbility && ((CrowdControlAbility)a).getEffect().getType().equals(EffectType.DEBUFF);
	}
	
	public boolean isFriendlyAbility(Ability a)
	{
		return a instanceof HealingAbility || (a instanceof CrowdControlAbility && ((CrowdControlAbility)a).getEffect().getType().equals(EffectType.BUFF));
	}
	private boolean isDamagingAbility(Ability a)
	{
		return a instanceof DamagingAbility;
	}
	private ArrayList<Damageable> getValidChampions(Champion c, Ability a, ArrayList<Champion> candidates)
	{
		ArrayList<Damageable> valid_targets = new ArrayList<>();
		for(Damageable target: candidates)
		{
			Point p = target.getLocation();
			if(allowedDistance(c, a, p))
			{
				valid_targets.add(target);
			}
		}
		return valid_targets;
	}
	
	private ArrayList<Damageable> getValidCovers(Champion c, Ability a, ArrayList<Cover> candidates)
	{
		ArrayList<Damageable> valid_targets = new ArrayList<>();
		for(Damageable target: candidates)
		{
			Point p = target.getLocation();
			if(allowedDistance(c, a, p))
			{
				valid_targets.add(target);
			}
		}
		return valid_targets;
	}
	
	private boolean isValidAbilityTarget(Champion c, Ability a, Point p)
	{
		int x = p.x;
		int y = p.y;
		boolean valid_point = validPoint(p);
		if(! valid_point)
			return false;
		if(board[x][y] == null)
			return false;
		Damageable target = (Damageable)board[x][y];
		if(isFriendlyAbility(a))
		{
			return target instanceof Champion && ! areOpponents(c, (Champion)target);
		}
		else if(hasOpposingEffect(a))
		{
			return target instanceof Champion && areOpponents(c, (Champion)target);
		}
		else if(isDamagingAbility(a))
		{
			return target instanceof Cover || (target instanceof Champion && areOpponents(c, (Champion)target )) ;
		}
		return false;
	}
	
	private ArrayList<Damageable> getValidSurroundingPoints(Champion c, Ability a)
	{
		ArrayList<Damageable> targets = new ArrayList<>();
		Point p = c.getLocation();
		int x = p.x;
		int y = p.y;
		Point p1 = new Point(x + 1, y);
		Point p2 = new Point(x + 1, y + 1);
		Point p3 = new Point(x + 1, y - 1);
		Point p4 = new Point(x - 1, y);
		Point p5 = new Point(x - 1, y + 1);
		Point p6 = new Point(x - 1, y - 1);
		Point p7 = new Point(x, y + 1);
		Point p8 = new Point(x, y - 1);
		
		// Checking validity of each point
		if(isValidAbilityTarget(c, a, p1))
			targets.add((Damageable)board[p1.x][p1.y]);
		
		if(isValidAbilityTarget(c, a, p2))
			targets.add((Damageable)board[p2.x][p2.y]);
		
		if(isValidAbilityTarget(c, a, p3))
			targets.add((Damageable)board[p3.x][p3.y]);
		
		if(isValidAbilityTarget(c, a, p4))
			targets.add((Damageable)board[p4.x][p4.y]);
		
		if(isValidAbilityTarget(c, a, p5))
			targets.add((Damageable)board[p5.x][p5.y]);
		
		if(isValidAbilityTarget(c, a, p6))
			targets.add((Damageable)board[p6.x][p6.y]);
		
		if(isValidAbilityTarget(c, a, p7))
			targets.add((Damageable)board[p7.x][p7.y]);
		
		if(isValidAbilityTarget(c, a, p8))
			targets.add((Damageable)board[p8.x][p8.y]);
		
		return targets;
	}
		
	private void checkCastingAbility(Champion c, Ability a) throws NotEnoughResourcesException, AbilityUseException {
		if(a.getCurrentCooldown() > 0)
			throw new AbilityUseException("Can not cast Ability before its baseCoolDown. Remaining: " + a.getCurrentCooldown() + " Turns");
		else
			a.setCurrentCooldown(a.getBaseCooldown());
		
		
		if(c.getCurrentActionPoints() < a.getRequiredActionPoints())
			throw new NotEnoughResourcesException("Not enough Action points !");
		else
			c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
		
		if(! enoughMana(c, a))
			throw new NotEnoughResourcesException("Not enough Mana for casting this Ability");
		
		if(isInactive(c) || hasEffect(c, "Silence"))
		{
			throw new AbilityUseException("Can not cast an Ability while you are Silenced");
		}
	}
	
	public void castAbility(Ability a) throws NotEnoughResourcesException, AbilityUseException, CloneNotSupportedException
	{
		Champion c = getCurrentChampion();
		checkCastingAbility(c, a);
		AreaOfEffect aof  = a.getCastArea();
		if(! (aof.equals(AreaOfEffect.TEAMTARGET) || aof.equals(AreaOfEffect.SURROUND) || aof.equals(AreaOfEffect.SELFTARGET)))
			return;
		
		ArrayList<Damageable> targets = new ArrayList<>();
		AreaOfEffect effect_area = a.getCastArea();
		
		if(effect_area.equals(AreaOfEffect.TEAMTARGET))
		{		
			if(isFriendlyAbility(a))
			{
				 targets = getValidChampions(c, a, getCorrespondingTeam(c));
			}
			else if(hasOpposingEffect(a))
			{
				 targets = getValidChampions(c, a, getOpposingTeam(c));
			}
			else if(isDamagingAbility(a))
			{
				 targets = getValidChampions(c, a, getOpposingTeam(c));
				 targets.addAll(getValidCovers(c, a, covers));
			}
		}	
	    else if(effect_area.equals(AreaOfEffect.SURROUND))
	    {
			targets = getValidSurroundingPoints(c, a);	
	    }
	    else if(effect_area.equals(AreaOfEffect.SELFTARGET))
	    {
		    targets.add(c);
	    }	
		
		a.execute(targets);
		if(! targets.isEmpty())
		{
			abilityEffect = true;
		}
		if(a instanceof DamagingAbility)
		{
			for(Damageable target: targets)
				decreaseTargetHealth(target, 0);
		}
		c.setMana(c.getMana()-a.getManaCost());
	}
	
	public void castAbility(Ability a, Direction d) throws NotEnoughResourcesException, AbilityUseException, CloneNotSupportedException
	{   
		Champion c = getCurrentChampion();
		checkCastingAbility(c, a);
		AreaOfEffect aof  = a.getCastArea();
		if(! aof.equals(AreaOfEffect.DIRECTIONAL))
			return;
		
		ArrayList<Damageable> targets = new ArrayList<>();
		
		// Initializations
		Point p = c.getLocation();
		int x = p.x;
		int y = p.y;
		while(allowedDistance(c, a, p))
		{
			switch (d) {
				case DOWN -> x--;
				case UP -> x++;
				case RIGHT -> y++;
				case LEFT -> y--;
			}
			p = new Point(x, y);
			if(isValidAbilityTarget(c, a, p))
			{
				targets.add((Damageable)board[p.x][p.y]);
			}
		}
		a.execute(targets);
		if(! targets.isEmpty())
		{
			abilityEffect = true;
		}
		c.setMana(c.getMana()-a.getManaCost());	
		if(a instanceof DamagingAbility)
		{
			for(Damageable damageable: targets)
				decreaseTargetHealth(damageable, 0);
		}
	}
	
	public void castAbility(Ability a, int x, int y) throws InvalidTargetException, NotEnoughResourcesException, AbilityUseException, CloneNotSupportedException
	{
		Champion c = getCurrentChampion();
		checkCastingAbility(c, a);
		Point p = new Point(x, y);
		Damageable targ = (Damageable)board[x][y];
		if(! isValidAbilityTarget(c, a, p))
		{
			String s = "";
			if(isFriendlyAbility(a))
			{
				if(targ instanceof Champion)
					s = "Can not cast a friendly ability on an Opponent  :) ";
				else
					s = "Can not cast a Crowd Control Ability on a Cover ";
			}
			else if(isDamagingAbility(a))
			{
				s = "Can not cast a harming ability on a friend  :) ";
			}
			else
			{	
				if(hasOpposingEffect(a))
				{
					if(targ instanceof Champion)
						s = "Can not cast a harming ability on a friend  :) ";
					else
						s = "Can not cast a Crowd Control Ability on a Cover ";
				}
			}
			throw new InvalidTargetException(s);
		}
		if(! allowedDistance(c, a, p))
		{
			throw new AbilityUseException("Target Out of Ability Range");
		} 
		ArrayList<Damageable> target = new ArrayList<>();
		target.add(targ);
		
		c.setMana(c.getMana()-a.getManaCost());
		a.execute(target);
		if(! target.isEmpty())
		{
			abilityEffect = true;
		}
		if(a instanceof DamagingAbility)
		{
			decreaseTargetHealth(targ, 0);
		}	
	}
	
	private ArrayList<Champion> getLeaderTarget(Champion c)
	{
		ArrayList<Champion> target = new ArrayList<>();
		if(c instanceof Hero)
		{
			target = getCorrespondingTeam(c);
			leaderAbilityEffect = true;
		}
		else if(c instanceof Villain)
		{
			for(Champion candidate: getOpposingTeam(c))
			{
				if(candidate.getCurrentHP() < 0.3 * candidate.getMaxHP())
				{
					target.add(candidate);
				}
			}
			leaderAbilityEffect = true;
		}
		else if(c instanceof AntiHero)
		{
			for(Champion champion: firstPlayer.getTeam())
			{
				if(! champion.isLeader())
					target.add(champion);
			}
			for(Champion champion: secondPlayer.getTeam())
			{
				if(! champion.isLeader())
					target.add(champion);
			}
			if(! target.isEmpty())
			{
				leaderAbilityEffect = true;
			}
		}
		return target;
	}
	
	
	public void useLeaderAbility() throws LeaderAbilityAlreadyUsedException, LeaderNotCurrentException, AbilityUseException
	{
		Champion c = getCurrentChampion();
		if(c.isLeader())
		{
			if(firstPlayer.getTeam().contains(c))
				if(firstLeaderAbilityUsed)  
					throw new LeaderAbilityAlreadyUsedException();
				else
					firstLeaderAbilityUsed = true;
			
			if (secondPlayer.getTeam().contains(c))
				if(secondLeaderAbilityUsed)
					throw new LeaderAbilityAlreadyUsedException();
				else
					secondLeaderAbilityUsed = true;
			
			if(hasEffect(c, "Silence"))
				throw new AbilityUseException();
			
			ArrayList<Champion> targets = getLeaderTarget(c);
			c.useLeaderAbility(targets);
			if(c instanceof Villain)
			{
				for(Damageable target: targets)
				{
					decreaseTargetHealth(target, target.getCurrentHP());
				}
			}	
		}
		else
			throw new LeaderNotCurrentException();
	}

	
	private boolean hasEffect(Champion c, String effect)
	{
		for(Effect e: c.getAppliedEffects())
		{
			if(e.getName().equals(effect))
				return true;
		}
		return false;
	}
	
	private boolean isInactive(Champion c)
	{
		return hasEffect(c, "Stun") || c.getCondition().equals(Condition.INACTIVE);
	}
	
	private boolean expiredEffect(Effect effect)
	{
		return effect.getDuration() <= 0;
	}
	
	private void updateTimers(Champion c)
	{ 
		for(int i = 0; i < c.getAppliedEffects().size();)
		{
			
			Effect effect = c.getAppliedEffects().get(i);
			int effect_time = effect.getDuration();
			effect.setDuration(effect_time - 1);
			if(expiredEffect(effect))
			{
				c.getAppliedEffects().remove(i);
				effect.remove(c);
			}
			else
				i++;
		}
		for(Ability ability: c.getAbilities())
		{
			if(ability.getCurrentCooldown() > 0)
			{
				int current_ability_cool_down = ability.getCurrentCooldown() - 1;
				ability.setCurrentCooldown(current_ability_cool_down);
			}
		}
	}
	public Player getChampionPlayer(Champion c)
	{
		if(getCorrespondingTeam(c) == firstPlayer.getTeam())
			return firstPlayer;
		return secondPlayer;
	}
	
	private void prepareChampionTurns()
	{
		ArrayList<Champion> team1 = firstPlayer.getTeam();
		ArrayList<Champion> team2 = secondPlayer.getTeam();	
		for(Champion champion: team1)
		{
			turnOrder.insert(champion);
		}
		for(Champion champion: team2)
		{
			turnOrder.insert(champion);
		}
	}
	
	public void endTurn()
	{
		turnOrder.remove();
		while(! turnOrder.isEmpty())
		{	
			Champion champ = getCurrentChampion();
			champ.setCurrentActionPoints(champ.getMaxActionPointsPerTurn());
			boolean inActive = isInactive(champ);
			updateTimers(champ);
			if(inActive)
				turnOrder.remove();
			else
				break;
		}
		if(turnOrder.isEmpty())
		{
			prepareChampionTurns();
			Champion champ = getCurrentChampion();
			champ.setCurrentActionPoints(champ.getMaxActionPointsPerTurn());
			updateTimers(champ);
		}		
	}
	
}
