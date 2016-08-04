package com.dungeon.game.effect.regen;

import com.badlogic.gdx.graphics.Texture;
import com.dungeon.game.effect.Effect;
import com.dungeon.game.entity.character.Character;
import com.dungeon.game.entity.hud.EffectGraphic;
import com.dungeon.game.world.World;
public class LifeRegen extends Effect {
	private float rate;
	
	public LifeRegen(World world, int duration, float rate) {
		super(world, "Healing", duration);
		this.rate = rate;
		texture = new Texture("heal.png");
		graphic = new EffectGraphic(world, this);
	}
	
	public LifeRegen(World world, int duration, int total) {
		super(world, "Healing", duration);
		this.rate = (float)total/(float)duration;
		texture = new Texture("heal.png");
		graphic = new EffectGraphic(world, this);
	}
	

	
	public LifeRegen(World world, float rate) {
		super(world, "Healing", -1);
		this.rate = rate;
	}
	
	public void calc(Character character){
		character.gain_life(rate);
	}
	
	public String getHoveredText() {
		return "Recovering " + Math.round(rate*60f)+" health per second" +  (duration > 0 ? (" for "+ Math.round(duration/6)/10f +" seconds."):("."));
	}
	
	public int getNum() {
		return (int) Math.ceil(duration/60f);
	}
}
