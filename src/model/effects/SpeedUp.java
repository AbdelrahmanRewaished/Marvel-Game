package model.effects;

import model.world.Champion;

public class SpeedUp extends Effect {
public SpeedUp(int duration)
   {
   		super("SpeedUp",duration, EffectType.BUFF);
   }
   public void apply(Champion c)
   {
		 changeSpeed(c, 0.15);
		 changeActionPoints(c, 1);
   }
   public void remove(Champion c)
   {
	    changeSpeed(c, -0.15);
	    changeActionPoints(c, -1);
   }
}
