package model.abilities;
import java.util.ArrayList;

import model.effects.*;
import model.world.Champion;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	private final Effect effect;
	public  CrowdControlAbility(String name , int cost , int baseCooldown , int castRange , AreaOfEffect area, int required , Effect effect) {
		super (name , cost , baseCooldown , castRange , area , required);
		this.effect = effect;
	}
	public Effect getEffect() 
	{
		return effect;
	}
	
	private Effect getEffect(Champion c, Effect e)
	{
		for(Effect effect: c.getAppliedEffects())
		{
			if(effect.getClass() == e.getClass())
			{
				return effect;
			}
		}
		return null;
	}
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException
	{
		for(Damageable target: targets)
		{
			if(target instanceof Champion champ)
			{

				Effect ef = (Effect)effect.clone();
				ef.apply(champ);
				Effect e = getEffect(champ, ef);
				if(e != null)
					e.setDuration(e.getDuration() + ef.getDuration());
				else
					champ.getAppliedEffects().add(ef);
			}
			//this.setCurrentCooldown(this.getBaseCooldown());
		}
	}
	
	public String toString(int s)
	{
		return super.toString(s) + "    Effect: " + effect.getName() + "\n    Effect Type: " + effect.getType().toString() + 
				"\n    Effect Duration: " + effect.getDuration();
	}
}
