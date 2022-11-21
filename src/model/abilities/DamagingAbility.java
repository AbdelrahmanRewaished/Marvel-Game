package model.abilities;

import java.util.ArrayList;
import model.effects.Effect;
import model.effects.Shield;
import model.world.Champion;
import model.world.Damageable;

public class DamagingAbility extends Ability {
	private int damageAmount;
	public  DamagingAbility(String name , int cost , int baseCooldown , int castRange , AreaOfEffect area, int required , int damageAmount) 
	{
		 super (name , cost , baseCooldown , castRange , area , required);
		 this.damageAmount = damageAmount;
	}
	public int getDamageAmount() 
	{
		return damageAmount;
	}
	public void setDamageAmount(int damageAmount) 
	{
		this.damageAmount = damageAmount;
	}
	private boolean shielded(Champion target)
	{
		for(Effect effect: target.getAppliedEffects())
		{
			if(effect instanceof Shield)
			{
				effect.remove(target);
				target.getAppliedEffects().remove(effect);
				return true;	
			}
		}
		return false;
	}
	public void execute(ArrayList<Damageable> targets) 
	{
		for(Damageable target: targets) 
		{
			boolean notAffected = target instanceof Champion && shielded((Champion)target);
			if(! notAffected)
				target.setCurrentHP(target.getCurrentHP() - damageAmount);
		}
		//this.setCurrentCooldown(this.getBaseCooldown());
	}
	public String toString(int s)
	{
		return super.toString(s) + "    Damage Amount: " + damageAmount + "\n";
	}
	
}
