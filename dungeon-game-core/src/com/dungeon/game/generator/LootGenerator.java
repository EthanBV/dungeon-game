package com.dungeon.game.generator;

import com.dungeon.game.entity.Chest;
import com.dungeon.game.item.Arrow;
import com.dungeon.game.item.Bow;
import com.dungeon.game.item.LifePotion;
import com.dungeon.game.item.Sword;
import com.dungeon.game.world.Tile;
import com.dungeon.game.world.World;

public class LootGenerator {
	public static Chest getChest(World world, int level, int x, int y){

		Chest chest = new Chest(world, x*Tile.TS+Tile.TS/2,y*Tile.TS+Tile.TS/2);
		if(Math.random()>0.5)chest.inv.addItem(new Sword(world, (int) (7 + Math.random()*6),10));
		else {
			chest.inv.addItem(new Bow(world, (int) (0.7 + Math.random()*0.6),10));
			Arrow arrow = new Arrow(world);
			arrow.stack = (int) (Math.random()*12);
			chest.inv.addItem(arrow);
		}

		for(double i = Math.random(); i < 0.5; i+=0.1){
			chest.inv.addItem(new LifePotion(world));
		}
		return chest;
	}
}
