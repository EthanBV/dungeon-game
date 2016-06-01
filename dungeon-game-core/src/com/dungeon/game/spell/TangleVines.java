package com.dungeon.game.spell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.effect.Stun;
import com.dungeon.game.effect.Tangle;
import com.dungeon.game.entity.character.Character;
import com.dungeon.game.entity.weapon.SpellProjectile;
import com.dungeon.game.item.Item;
import com.dungeon.game.item.weapon.Medium;
import com.dungeon.game.world.World;

public class TangleVines extends Spell {

	public TangleVines(World world) {
		super(world);
		this.sprite = new Texture("vine.png");
		mana = 5;
		cooldown = 120;
	}

	@Override
	public void cast(float x, float y, float angle, Medium medium) {
		float xOff = (float) (Math.cos((angle+135)/180*Math.PI)*30);

		float yOff = (float) (Math.sin((angle+135)/180*Math.PI)*30);
		world.entities.add(new SpellProjectile(world, medium, this, (x+xOff), (y+yOff), angle, 4, new Polygon(new float[]{0,28,4,28,4,32,0,32}), 0, 32, 70, Item.SIZE, Item.SIZE, "vine.png"));
		
	}

	@Override
	public void hit(Character character, Medium weapon, SpellProjectile projectile) {
		character.addEffect(new Tangle(world, 300));
		character.addEffect(new Stun(world, 5));
		if(!character.knownEntities.contains(projectile.owner))character.knownEntities.add(projectile.owner);
		
	}

}