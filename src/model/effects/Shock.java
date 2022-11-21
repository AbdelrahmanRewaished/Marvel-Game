package model.effects;

import model.world.Champion;

public class Shock extends Effect {
public Shock(int duration)
    {
    	super("Shock",duration, EffectType.DEBUFF);
    }
    public void apply(Champion c)
    {
 		changeAttackDamagedec(c, - 0.1); 
	    c.setSpeed((int)(c.getSpeed() * .9));
	    c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() - 1);
	    c.setCurrentActionPoints(c.getCurrentActionPoints() - 1);
    }
    public void remove(Champion c)
    {
    	c.setSpeed((int)(c.getSpeed() / .9));
        //c.setAttackDamage((int)(c.getAttackDamage()/.9));
        c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn() + 1);
        c.setCurrentActionPoints(c.getCurrentActionPoints() + 1);
   	 	changeAttackDamagedec(c, 0.1);

       
    }
}
