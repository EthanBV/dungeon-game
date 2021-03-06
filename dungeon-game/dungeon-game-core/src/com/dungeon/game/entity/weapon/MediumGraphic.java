package com.dungeon.game.entity.weapon;

import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.item.equipable.weapon.Medium;
import com.dungeon.game.item.equipable.weapon.Weapon;
import com.dungeon.game.spell.Spell;
import com.dungeon.game.world.World;

public class MediumGraphic extends HandheldGraphic {

	public MediumGraphic(World world, Weapon weapon, Polygon hitbox, float originX, float originY){
		super(world, weapon, hitbox, originX, originY);
	}

	@Override
	public void calc() {
		super.calc();
	}

	@Override
	public void post() {
		super.calc();
	}

	public void cast(Spell spell, Medium medium) {
		spell.cast(x,y,angle, medium);
	}

}
