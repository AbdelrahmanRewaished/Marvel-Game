package model.effects;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {
public PowerUp(int duration)
     {
     	super("PowerUp", duration, EffectType.BUFF);
     }
     public void apply(Champion c)
     {
    	 for(Ability a: c.getAbilities())
 		{
 			int x;
 			if(a instanceof DamagingAbility)
 			{
 				x = ((DamagingAbility) a).getDamageAmount();
 				x = (int)(x * 1.2);
 				((DamagingAbility) a).setDamageAmount(x);
 			}
 			else if(a instanceof HealingAbility)
 			{
 				x = ((HealingAbility) a).getHealAmount();
 				x = (int)(x * 1.2);
 				((HealingAbility) a).setHealAmount(x);
 			}
 		}
     }
     public void remove(Champion c)
     {
  		 for(Ability a: c.getAbilities())
  		{
  			int x;
  			if(a instanceof DamagingAbility)
  			{
  				x = ((DamagingAbility) a).getDamageAmount();
  				x = (int)(x /1.2);
  				((DamagingAbility) a).setDamageAmount(x);
  			}
  			else if(a instanceof HealingAbility)
  			{
  				x = ((HealingAbility) a).getHealAmount();
  				x = (int)(x / 1.2);
  				((HealingAbility) a).setHealAmount(x);
  			}
  		}
     }
  
}
