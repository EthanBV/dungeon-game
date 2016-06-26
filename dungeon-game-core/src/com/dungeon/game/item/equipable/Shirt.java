package com.dungeon.game.item.equipable;

import com.badlogic.gdx.graphics.Color;
import com.dungeon.game.effect.Armor;
import com.dungeon.game.spritesheet.Spritesheet;
import com.dungeon.game.world.World;

public class Shirt extends Equipable {
	public Shirt(World world, Color color) {
		super(world, "shirt.png");
		
		sprite = Spritesheet.Tint(textures[0], color, true);
		
		name = "Shirt";
		
		physc_resist = 1;
		flame_resist = -1;
		
		type = CHEST;
		
		desc = "A simple wool shirt. Provideds little defence.\n\n Armor: " + (int)physc_resist;
	}

}
