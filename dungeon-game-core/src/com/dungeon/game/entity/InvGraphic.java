package com.dungeon.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dungeon.game.item.Inventory;
import com.dungeon.game.item.Slot;
import com.dungeon.game.world.World;

public class InvGraphic extends Hud {
	Slot[] slot;
	
	Inventory inv;
	
	public InvGraphic(String sprite, Inventory inv) {
		super(0, 0);
		
		this.slot = inv.slot;
		
		this.sprite = new Texture(sprite);
		
		this.inv = inv;
	}

	@Override
	public void init() {
		x = 10;
		y = 100;
	}

	@Override
	public void calc(World world) {
		inv.update(world);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(sprite, x, y);
		
		for(Slot s: slot) {
			s.draw(batch, (int)x, (int)y);
		}
	}
}
