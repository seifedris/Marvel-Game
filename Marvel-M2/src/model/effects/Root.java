package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root(int duration) {
		super("Root", duration, EffectType.DEBUFF);

	}

	@Override
	public void apply(Champion c) {
		if (c.getCondition() != Condition.INACTIVE)
			c.setCondition(Condition.ROOTED);
		if (this.getListener() != null)
			this.getListener().onApply(this);

	}

	@Override
	public void remove(Champion c) {
		boolean found = false;
		for (Effect e : c.getAppliedEffects()) {
			if (e instanceof Root)
				found = true;
		}

		if (c.getCondition() != Condition.INACTIVE && !found)
			c.setCondition(Condition.ACTIVE);
		if (this.getListener() != null)
			this.getListener().onRemove(this);

	}

}
