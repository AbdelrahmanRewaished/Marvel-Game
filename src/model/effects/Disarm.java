package model.effects;

import java.util.ArrayList;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect {
 
public Disarm(int duration)
	 {
	    	super("Disarm", duration, EffectType.DEBUFF);
	 }
	 public void apply(Champion c)
	 {
		 DamagingAbility da = new DamagingAbility("Punch", 0, 1, 1, AreaOfEffect.SINGLETARGET, 1, 50);
		 c.getAbilities().add(da);
	 }
	 public void remove(Champion c)
	 {
		 ArrayList<Ability> abilities = c.getAbilities();
		 for(Ability a: abilities)
		 {
			 if(a.getName().equals("Punch"))
			 {
				 abilities.remove(a);
				 break;
			 }
		 }
	 }

}
