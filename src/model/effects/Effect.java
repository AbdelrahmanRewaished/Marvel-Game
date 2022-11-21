package model.effects;

import model.world.Champion;

abstract public class Effect implements Cloneable{
    private String name;
    private int duration;
    private final EffectType type;

	public Effect(String name , int duration , EffectType type) {
		this.name = name;
		this.duration = duration;
		this.type = type;
	}
	public Effect(int duration, EffectType type)
	{
		this.duration = duration;
		this.type = type;
	}
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public String getName() {
		return name;
	}
	
	public EffectType getType() {
		return type;
	}
	abstract public void apply(Champion c);
	abstract public void remove(Champion c);
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	public void changeSpeed(Champion c, double change)
    {
     	int current_speed = c.getSpeed();
     	
     	int new_speed;//(int)(current_speed + current_speed * change);
     	if(change >= 0)
            new_speed=(int)(current_speed * (1 + change));
     	else
     		new_speed=(int)(current_speed / (1 + Math.abs(change)));
     	c.setSpeed(new_speed);
    }
	public void changeActionPoints(Champion c, int change)
	{
		   int max_actions = c.getMaxActionPointsPerTurn();
		   int current_actions = c.getCurrentActionPoints();
		   c.setMaxActionPointsPerTurn(max_actions + change);
		   c.setCurrentActionPoints(current_actions + change);
    }
	public void changeAttackDamagedec(Champion c, double change)
	{
		int current_attackDamage = c.getAttackDamage();
		int new_attackDamage;
     	if(change <= 0)
     		new_attackDamage=(int)(current_attackDamage * (1 + change));
     	else
     	{
     		change = -change;
     		new_attackDamage=(int)(current_attackDamage / (1 + change));
     	}
		c.setAttackDamage(new_attackDamage);		
	}

	public String toString()
	{
		return  "[Name: " + name + ", Duration: " + duration + ", Type: " + type.toString() + "]\n"; 
	}
}
