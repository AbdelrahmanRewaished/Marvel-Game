package model.abilities;

import model.world.*;
import java.util.ArrayList;

abstract public class Ability {
	private final String name;
	private final int manaCost;
	private final int baseCooldown;
	private int currentCooldown;
	private final int castRange;
	private final int requiredActionPoints;
	private final AreaOfEffect castArea;
	public Ability(String name , int manacost , int baseCooldown , int castRange , AreaOfEffect castarea, int required) 
	{
		 this.name = name;
		 this.manaCost = manacost;
		 this.baseCooldown = baseCooldown;
		 setCurrentCooldown(0);
		 this.castRange = castRange;
		 this.castArea = castarea;
		 requiredActionPoints = required;
	}
	public String getName() {
		return name;
	}
	public int getManaCost() {
		return manaCost;
	}
	public int getBaseCooldown() {
		return baseCooldown;
	}
	public int getCurrentCooldown() {
		return currentCooldown;
	}
	public void setCurrentCooldown(int currentCooldown) {
		if(currentCooldown < 0)
			this.currentCooldown = 0;
		else this.currentCooldown = Math.min(currentCooldown, baseCooldown);
	}
	public int getCastRange() {
		return castRange;
	}
	public int getRequiredActionPoints() {
		return requiredActionPoints;
	}
	public AreaOfEffect getCastArea() {
		return castArea;
	}	
	abstract public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException;	
	public String toString(int s)
	{
		String description = "Ability Name: " + name +  " \nAbility Type: ";
		if(this instanceof CrowdControlAbility)
		{
			description += "Crowd Control Ability";
		}
		else if(this instanceof DamagingAbility)
		{
			description += "Damaging Ability";
		}
		else
		{
			description += "Healing Ability";
		}
		description += "\n" + "    Mana Cost: " + manaCost + "\n" + "    Base Cool Down: " + baseCooldown + 
				"\n    Current Cool Down: " + currentCooldown + "\n" +   "    Cast Range: " + castRange + "\n" + "    Required Action Points: " + requiredActionPoints + "\n" +
				"    Cast Area: " + castArea.toString() + "\n";
		if(s > 0)
			description = s + " )" + description;
		return description;
	}
}

