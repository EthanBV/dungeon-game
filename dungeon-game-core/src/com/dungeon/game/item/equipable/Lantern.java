package com.dungeon.game.item.equipable;

import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.entity.character.Character;
import com.dungeon.game.entity.weapon.HandheldGraphic;
import com.dungeon.game.light.Light;
import com.dungeon.game.world.World;

public class Lantern extends Hand {

	public Lantern(World world) {
		super(world, "lantern.png");
		name = "Lantern";
		desc = "A small, hand held Lantern.";
		graphic = new HandheldGraphic(world,this, new Polygon(new float[]{0,0,32,0,32,32,0,32}), 16, 16);
		graphic.light = new Light(world, graphic.x, graphic.y, 16, 100, Light.ORANGE, 1, graphic);
	}

	@Override
	public void reset() {
		
	}

	@Override
	public float[] getPos(boolean mousedown, boolean mousepressed) {
		if(!leftSide) return new float[]{22,-40,45+180};
		return new float[]{22,40,45+180};
	}

	public boolean isInUse() {
		return false;
	}
	
	public void equip(Character owner, boolean leftSide){
		super.equip(owner, leftSide);
		this.graphic.light.load();
	}
	
	public void unequip(){
		super.unequip();
		this.graphic.light.unload();
	}

}
