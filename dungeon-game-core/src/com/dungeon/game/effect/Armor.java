package com.dungeon.game.effect;

import com.badlogic.gdx.graphics.Texture;
import com.dungeon.game.entity.character.Character;
import com.dungeon.game.entity.hud.EffectGraphic;
import com.dungeon.game.world.World;

public class Armor extends Effect {
	
	private int amount;

	public Armor(World world, int duration, int amount) {
		super(world, "Armor", duration);
		
		this.amount = amount;
		
		texture = new Texture("dizzy.png");
		graphic = new EffectGraphic(world, this);
	}
	
	public Armor(World world, int amount) { //for use in armor so you cant see the effect :o
		super(world, "Armor", -1);
		
		this.amount = amount;
	}
	
	public void begin(Character character){
		character.armor+=amount;
	}
	
	public void end(Character character){
		character.armor-=amount;
	}
	
	public String getHoveredText() {
		return "You have " + Math.round(amount) + " extra armor";
	}
	
	public int getNum() {
		return (int) Math.ceil(duration/60f);
	}

}
