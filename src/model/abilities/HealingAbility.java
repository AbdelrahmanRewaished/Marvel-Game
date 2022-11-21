package model.abilities;

import java.util.ArrayList;
import model.world.*;

public class HealingAbility extends Ability {
	private int healAmount;
	public  HealingAbility(String name , int cost , int baseCooldown , int castRange , AreaOfEffect area, int required,int healAmount) {
		super (name , cost , baseCooldown , castRange , area , required);
		this.healAmount = healAmount;
}
	public int getHealAmount() 
	{
		return healAmount;
	}
	public void setHealAmount(int healAmount) 
	{
		this.healAmount = healAmount;
	}
	public void execute(ArrayList<Damageable> targets)
	{
		for(Damageable target: targets)
		{
			if(target instanceof Champion champ)
			{
				champ.setCurrentHP(champ.getCurrentHP() + healAmount);
			}
		}
		//this.setCurrentCooldown(this.getBaseCooldown());
	}
	public String toString(int s)
	{
		return super.toString(s) + "    Heal Amount: " + healAmount + "\n";
	}
}
