package com.dungeon.game.item;

import com.dungeon.game.world.World;

public class OneTaper extends Sword {

	public OneTaper(World world) {
		super(world, 1000, 5, 5);
		name = "One Tapper";
		desc = "This large sword is light and well balanced in your hand! \n ...'balanced' \n\n Damage: "+damage+"\n Cooldown: "+cooldown;
		knockratio = 0.3f;
		knockstr = 10;
	}

}
