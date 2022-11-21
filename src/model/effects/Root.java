package model.effects;

import model.world.*;

public class Root extends Effect {
public Root(int duration)
    {
    	super("Root",duration, EffectType.DEBUFF);
    }
    public void apply(Champion c)
    { 
    	if(! c.getCondition().equals(Condition.INACTIVE))
    		c.setCondition(Condition.ROOTED);
    }
    public void remove(Champion c)
    {
    	if(c.getCondition().equals(Condition.INACTIVE))
    		return;
    	
    	boolean still_rooted = false;
    	for(Effect effect: c.getAppliedEffects())
    	{
    		if(effect instanceof Root)
    		{
    			still_rooted = true;
    			break;
    		}
    		
    	}
    	if(! still_rooted)
    	{
    		c.setCondition(Condition.ACTIVE);
    	}	
    }
}
