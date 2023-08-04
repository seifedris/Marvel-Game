package model.effects;

import model.world.Champion;

public class Shield extends Effect {

	public Shield( int duration) {
		super("Shield", duration, EffectType.BUFF);
		
	}

	@Override
	public void apply(Champion c) {
		
		c.setSpeed((int) (c.getSpeed()*1.02));
		if(this.getListener()!=null)
			this.getListener().onApply(this);
	}

	@Override
	public void remove(Champion c) {
		
		c.setSpeed((int) (c.getSpeed()/1.02));
		if(this.getListener()!=null)
			this.getListener().onRemove(this);
	}

}
