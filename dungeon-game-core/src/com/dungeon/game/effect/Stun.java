package com.dungeon.game.effect;

import com.badlogic.gdx.graphics.Texture;
import com.dungeon.game.entity.character.Character;
import com.dungeon.game.entity.character.Player;
import com.dungeon.game.entity.hud.EffectGraphic;
import com.dungeon.game.world.World;

public class Stun extends Effect {

	public Stun(World world, int duration) {
		super(world, "Stun", duration);
		texture = new Texture("stun.png");
		graphic = new EffectGraphic(world, this);
	}
	
	public void begin(Character character) {
		character.stun = true;
		for(int i = 0; i < character.effects.size(); i++){
			Effect e = character.effects.get(i);
    		if(e instanceof Stun && !e.killMe && e != this){
    			e.killMe = true;
    			duration = Math.max(duration, e.duration);
    			if(character instanceof Player&&e.graphic!=null)((Player)character).effectGraphics.remove(e.graphic);
    			character.effects.remove(i);
    			i--;
    		}
    	}
	}
	
	public void end(Character character) {
		for(Effect effect: character.effects) {
			if(effect instanceof Stun&&!effect.equals(this)) return;
		}
		character.stun = false;
	}
	
	public String getHoveredText() {
		return Math.round(duration/6)/10f+" secs";
	}
	
	public int getNum() {
		return (int) Math.ceil(duration/60f);
	}
	

}
