package com.dungeon.game.entity.furniture;

import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.entity.Static;
import com.dungeon.game.world.World;

public class Plant extends Static {

	public Plant(World world, float x, float y) {
		super(world, x, y, 32, 32, "pot.png");
		
		solid = true;
		
		origin_x = 16;
		origin_y = 16;
		
		hitbox = new Polygon(new float[]{8,8,24,8,24,24,8,24});
	}

	@Override
	public void calc() {
		
	}

	@Override
	public void post() {
		
	}

}
