package com.dungeon.game.entity;

import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.item.Ammo;
import com.dungeon.game.item.Medium;
import com.dungeon.game.item.Ranged;
import com.dungeon.game.spell.Spell;
import com.dungeon.game.world.World;

public class SpellProjectile extends Projectile {
	
	public Medium weapon;
	
	public Spell spell;
	
	public SpellProjectile(World world, Medium weapon, Spell spell, float x, float y, float angle, float power, Polygon hitbox, float originX, float originY, int range) {
		super(world, x, y, angle, power, hitbox, originX, originY, range);


		this.weapon = weapon;
		this.spell = spell;
		
		sprite = spell.sprite;
		
		d_width = sprite.getWidth();
		d_height = sprite.getHeight();
		
		owner = weapon.owner;
		System.out.println(getVel());
	}

	@Override
	protected void hit(Character character) {
		spell.hit(character, weapon, this);

	}

	@Override
	protected void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void post() {
		// TODO Auto-generated method stub

	}

}
