package model.effects;

import model.world.Champion;

public class Shield extends Effect {
public Shield(int duration)
     {
     	super("Shield",duration, EffectType.BUFF);
     }
     public void apply(Champion c)
     {
  		 changeSpeed(c, 0.02);
     }
     public void remove(Champion c)
     {
  		 changeSpeed(c, -0.02); 
     }
    
}
