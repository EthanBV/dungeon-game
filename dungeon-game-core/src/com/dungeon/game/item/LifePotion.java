package com.dungeon.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.dungeon.game.entity.Dynamic;
import com.dungeon.game.world.World;
import com.dungeon.game.entity.Character;

public class LifePotion extends Consumable {

	public LifePotion() {
		super("Health Potion");
		// TODO Auto-generated constructor stub
		sprite = new Texture("lifePotion.png");
		desc = "Ever wonder why health potions are red? \n\nYou haven't? \n\nGood.";
	}
	

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}


	@Override
	public void use(World world, Character user) {
		user.heal(30);

	}
}
