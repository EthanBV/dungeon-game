package com.dungeon.game.item;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.dungeon.game.effect.Effect;
import com.dungeon.game.effect.Stun;
import com.dungeon.game.entity.Character;
import com.dungeon.game.entity.Projectile;
import com.dungeon.game.world.World;

public class Arrow extends Ammo {

	public Arrow(World world){
		super(world);
	}

	@Override
	public void init() {
		damage = 10;
		
		name = "Arrow";
		desc = "The better to pew pew you with.\n\nDamage: " + damage;
		
		maxStack = 12;
		
		sprite = new Texture("Arrow.png");
	}

	@Override
	public void hit(Character character, Ranged weapon, Projectile projectile) {;
		float damage = this.damage*weapon.dmgMod*projectile.getVel()/10;
		
		ArrayList<Effect> effects = new ArrayList<Effect>();
		effects.add(new Stun(world, 10));
		
		character.damage(damage, effects);
		
		Vector2 knockback = new Vector2();
		
		knockback.x = projectile.moveVec.x;
		knockback.y = projectile.moveVec.y;
		
		character.acel(knockback, false);
		
		if(!character.knownEntities.contains(projectile.owner))character.knownEntities.add(projectile.owner);
	}
}
