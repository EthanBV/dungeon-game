package com.dungeon.game.item.equipable.weapon.part.sword.blade;

import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.world.World;

public class Needle extends SwordBlade {
	public Needle(World world, int level) {
		super(world, "Needle Blade", SPRITES[9], level);
		id = 0;
		allowedSwings = new String[]{
			"Stab",
		};
		bannedSwings = new String[]{
			"Slash",
			"Chop",
			"Whirlwind",
			"Execute"
		};
		repeatable = true;
		hitbox = new Polygon(new float[]{10,20,23,7,25,9,12,22,10,22});

	}

	@Override
	public void setStats(float level) {
		damage = getStat(3+0.3f*level,0.5f+0.1f*level);
		speed = getStat(22,4);
		knockback = getStat(1.2f,1);
		weight = getStat(1.75f,0.25f);
		numSwings = 2;
	}
	
	public String getName() {
		return "Needle";
	}
}
