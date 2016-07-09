package com.dungeon.game.item.equipable.weapon.part.sword.blade;

import com.dungeon.game.world.World;

public class Rapier extends SwordBlade {
	public Rapier(World world, int level) {
		super(world, "Rapier", SPRITES[4], level);
		id = 0;
		allowedSwings = new String[]{
			"Stab"
		};
		bannedSwings = new String[]{
			"Slash",
			"Chop",
			"Whirlwind",
			"Execute"
		};
		repeatable = true;
	}

	@Override
	public void setStats(float level) {
		damage = getStat(5.5f+0.5f*level,0.7f+0.2f*level);
		speed = getStat(17.5f,3);
		knockback = getStat(2.5f,0.7f);
		weight = getStat(4.5f,1.5f);
		numSwings = 4;
	}

}
