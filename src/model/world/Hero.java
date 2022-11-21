package model.world;

import java.util.ArrayList;
import model.effects.*;
public class Hero extends Champion{

	public Hero(String name, int maxHP, int mana, int maxActions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, mana, maxActions, speed, attackRange, attackDamage);
	}
	public void useLeaderAbility(ArrayList<Champion> targets) 
	{
			for(Champion target: targets) 
			{
				 Embrace e = new Embrace(2);
				 for(int i = 0; i < target.getAppliedEffects().size();) 
				 {
					 Effect effect = target.getAppliedEffects().get(i);
					 if(effect.getType().equals(EffectType.DEBUFF)) 
					 {
						 target.getAppliedEffects().remove(i);
						 effect.remove(target);
					 }
					 else
						 i++;
				 }
				 target.getAppliedEffects().add(e);
				 e.apply(target);	 
			}
	}

}
