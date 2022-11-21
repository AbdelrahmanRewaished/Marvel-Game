package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect{
public Stun(int duration)
    {
   		super("Stun",duration, EffectType.DEBUFF);
    }
    public void apply(Champion c)
    {
		 c.setCondition(Condition.INACTIVE);
    }
    public void remove(Champion c)
    {
    	boolean still_stunned = false;
    	boolean rooted = false;
    	for(Effect effect: c.getAppliedEffects())
    	{
    		if(effect instanceof Stun && effect != this)
    		{
    			still_stunned = true;
    		}
    		else if(effect instanceof Root)
    		{
    			rooted = true;
    		}
    	}
    	if(! still_stunned)
    	{	if(rooted)
	    		c.setCondition(Condition.ROOTED);
    		else
    			c.setCondition(Condition.ACTIVE);
    	}
    }
}
