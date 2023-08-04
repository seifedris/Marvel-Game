package model.world;

import java.awt.Point;
import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AbilityListener;
import model.effects.Effect;
import model.effects.EffectListener;

@SuppressWarnings("rawtypes")
public abstract class Champion implements Damageable, Comparable, EffectListener, AbilityListener {
	private String name;
	private int maxHP;
	private int currentHP;
	private int mana;
	private int maxActionPointsPerTurn;
	private int currentActionPoints;
	private int attackRange;
	private int attackDamage;
	private int speed;
	private ArrayList<Ability> abilities;
	private ArrayList<Effect> appliedEffects;
	private Condition condition;
	private Point location;
	private ChampionListener listener;

	public Champion(String name, int maxHP, int mana, int actions, int speed, int attackRange, int attackDamage) {
		this.name = name;
		this.maxHP = maxHP;
		this.mana = mana;
		this.currentHP = this.maxHP;
		this.maxActionPointsPerTurn = actions;
		this.speed = speed;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		this.condition = Condition.ACTIVE;
		this.abilities = new ArrayList<Ability>();
		this.appliedEffects = new ArrayList<Effect>();
		this.currentActionPoints = maxActionPointsPerTurn;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public String getName() {
		return name;
	}

	public void setCurrentHP(int hp) {

		if (hp <= 0) {
			currentHP = 0;
			condition = Condition.KNOCKEDOUT;

		} else if (hp > maxHP)
			currentHP = maxHP;
		else
			currentHP = hp;

	}

	public int getCurrentHP() {

		return currentHP;
	}

	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int currentSpeed) {
		if (currentSpeed < 0)
			this.speed = 0;
		else
			this.speed = currentSpeed;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point currentLocation) {
		this.location = currentLocation;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public int getCurrentActionPoints() {
		return currentActionPoints;
	}

	public void setCurrentActionPoints(int currentActionPoints) {
		if (currentActionPoints > maxActionPointsPerTurn)
			currentActionPoints = maxActionPointsPerTurn;
		else if (currentActionPoints < 0)
			currentActionPoints = 0;
		this.currentActionPoints = currentActionPoints;
	}

	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}

	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}

	public int compareTo(Object o) {
		Champion c = (Champion) o;
		if (speed == c.speed)
			return name.compareTo(c.name);
		return -1 * (speed - c.speed);
	}

	public abstract void useLeaderAbility(ArrayList<Champion> targets);

	@Override
	public void onApply(Effect e) {
		appliedEffects.add(e);

	}

	@Override
	public void onRemove(Effect e) {
		for (int i = 0; i != appliedEffects.size(); i++) {
			if (appliedEffects.get(i) == e)
				appliedEffects.remove(i);
		}

	}

	@Override
	public void onExecute(Ability a) {
		for (int i = 0; i != abilities.size(); i++) {
			if (abilities.get(i) == a)
				abilities.remove(i);
		}

	}
	public void select() {
		if (listener != null)
			listener.onSelected(this);
		
	}

	public ChampionListener getListener() {
		return listener;
	}

	public void setListener(ChampionListener listener) {
		this.listener = listener;
	}

	public String toString() {
		String s= getName() + " \n" + "mana: " + getMana() + " \n" + "speed: " + getSpeed() + " \n" + "hp: " + getCurrentHP()+" \n"
				+ "action points per turn: " + getCurrentActionPoints() + " \n" + "attack damage: "
				+ getAttackDamage() + " \n" + "attack range: " + getAttackRange() + " \n" + "speed: " + getSpeed()+ " \n";
		int i=1;
		for(Ability a : getAbilities()) {
			s+= i+". " +a.toString();
			i++;
		}
		return s;
	}
}
