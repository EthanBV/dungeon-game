package com.dungeon.game.effect.regen;

import com.dungeon.game.effect.Effect;
import com.dungeon.game.entity.character.Character;
import com.dungeon.game.world.World;
public class ManaRegen extends Effect {
	private float rate;
	
	public ManaRegen(World world, int duration, float rate) {
		super(world, "Mana Regen", duration);
		this.rate = rate;
	}
	
	public ManaRegen(World world, int duration, int total) {
		super(world, "Mana Regen", duration);
		this.rate = (float)total/(float)duration;
	}
	
	public void calc(Character character){
		character.gain_mana(rate);
	}
}
