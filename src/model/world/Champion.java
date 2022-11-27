package model.world;

import java.awt.Point;
import java.util.ArrayList;
import model.abilities.Ability;
import model.effects.Effect;
import model.effects.Shield;

@SuppressWarnings("rawtypes")
abstract public class Champion implements Damageable, Comparable{
	private final String name;
	private final int maxHP;
	private int currentHP;
	private int mana;
	private int maxActionPointsPerTurn;
	private int currentActionPoints;
	private final int attackRange;
	private int attackDamage;
	private int speed;
	private final ArrayList<Ability> abilities;
	private final ArrayList<Effect> appliedEffects;
	private Condition condition;
	private Point location;
	private boolean isLeader;
	private boolean leaderSet;
	
	public Champion(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage){
		this.name = name;
		this.maxHP = maxHP;
		setCurrentHP(maxHP);
		this.mana = mana;
		this.maxActionPointsPerTurn = maxActions;
		setCurrentActionPoints(maxActionPointsPerTurn);
		this.speed = speed;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		this.condition = Condition.ACTIVE;
		abilities  = new ArrayList<>();
		appliedEffects = new ArrayList<>();
		isLeader = false;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMaxHP() {
		return maxHP;
	}
	
	public int getCurrentHP() {
		return currentHP;
	}
	
	
	private boolean shielded()
	{
		for(Effect effect: getAppliedEffects())
		{
			if(effect instanceof Shield)
			{
				effect.remove(this);
				this.getAppliedEffects().remove(effect);
				return true;	
			}
		}
		return false;
	}
	
	
	public void setCurrentHP(int currHP) 
	{
		if(currHP < getCurrentHP())
		{
			if(shielded())
			{
				return;
			}
		}
		if(currHP < 0)
			currentHP = 0;
		else currentHP = Math.min(currHP, maxHP);
	}

	public int getMana() {
		return mana;
	}
	public void setMana(int mana)
	{
		this.mana = mana;
	}
	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}
	
	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}
	
	public int getCurrentActionPoints() {
		return currentActionPoints;
	}
	
	public void setCurrentActionPoints(int currPoints)
	{
		if(currPoints < 0)
			currentActionPoints = 0;
		else currentActionPoints = Math.min(currPoints, maxActionPointsPerTurn);
	}
	
	public int getAttackRange() {
		return attackRange;
	}
	
	public int getAttackDamage() {
		return attackDamage;
	}
	
	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = Math.max(speed, 0);
	}	
	public ArrayList<Ability> getAbilities() {
		return abilities;
	}
	
	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}
	
	public Condition getCondition() {
		return condition;
	}
	
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}

    
	public boolean isLeader() {
		return isLeader;
	}

	public void setToLeader(boolean isLeader) 
	{
		this.isLeader = isLeader;
	}

	public int compareTo(Object o)
	{
		Champion c = (Champion)(o);
		int this_speed = this.getSpeed();
		int other_speed = c.getSpeed();
		if(this_speed != other_speed)
			return c.getSpeed() - this.getSpeed();
		else
			return this.getName().compareTo(c.getName());
	}
	
	abstract public void useLeaderAbility(ArrayList<Champion> targets);
	
	public void setLeaderSet(boolean f)
	{
		leaderSet = f;
	}
	
	public String toString(boolean isCurrent)
	{
		String description = "- Name: " + name + "\n- Type: ";
		if(this instanceof Hero)
			description += "Hero\n";
		else if(this instanceof Villain)
			description += "Villain\n";
		else
			description += "AntiHero\n";
		description += ! leaderSet ? "- Max Health Points: " + maxHP : "- Current Health Points: " + currentHP;
		description += "\n- Mana: " + mana + "\n";
		description += ! isCurrent ? "- Max Action Points Per Turn: " + maxActionPointsPerTurn :  "- Current Action Points: " + currentActionPoints;
		description +=  "\n- Attack Range: " + attackRange +	"\n- Attack Damage: " + attackDamage + "\n- Speed: " + speed + "\n";
		if(leaderSet)
		{
			description += "- Is a Leader: ";
			description += isLeader ? "Yes" : "No";
		}
		description += showAppliedEffects();
		return description;
	}
	
	public String showAbilities()
	{
		int i = 1;
		StringBuilder description = new StringBuilder("""

				- Abilities:\s
				""");
		for(Ability a: getAbilities())
		{
			description.append(a.toString(i));
			i++;
		}
		return description.toString();
	}
	
	public String showAppliedEffects()
	{
		StringBuilder description = new StringBuilder("\n- Applied Effects: ");
		description.append(appliedEffects.isEmpty() ? "Nothing Yet" : "\n");
		for(Effect e: appliedEffects)
		{
			description.append(e.toString());
		}
		return description + "\n";
	}
	
	public static void main(String[] args) {
	
	}
}
