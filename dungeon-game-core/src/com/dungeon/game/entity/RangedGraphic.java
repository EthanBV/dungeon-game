package com.dungeon.game.entity;

import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.item.Weapon;
import com.dungeon.game.world.World;

public class RangedGraphic extends WeaponGraphic {
	
	private boolean toFire;
	
	private float power;
	
	public RangedGraphic(World world, Weapon weapon, float originX, float originY){
		super(world, weapon);
		this.origin_x = originX;
		this.origin_y = originY;

		hitbox = new Polygon(new float[]{0,0,0,0,0,0});
	}
	
	@Override
	public void init() {
		
	}

	@Override
	public void calc() {
		if(toFire) {
			toFire = false;
			Polygon projectileHitBox = new Polygon(new float[]{1,28,4,31,0,32});
			world.entities.add(new ArrowGraphic(world, (int)x,(int)y,angle,power, projectileHitBox, 16, 16, weapon));
		}
	}

	public void fire(float power) {
		toFire = true;
		this.power = power;
	}

	@Override
	public void post() {}
}
