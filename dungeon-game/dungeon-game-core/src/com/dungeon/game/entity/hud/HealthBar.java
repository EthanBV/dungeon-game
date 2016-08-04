package com.dungeon.game.entity.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.dungeon.game.world.World;

public class HealthBar extends StatusBar {
	public HealthBar(World world, float x, float y){
		super(world, x, y);
		filler = new TiledDrawable(new TextureRegion(new Texture("heart.png")));
	}

	@Override
	public void calc() {
		cur = world.player.life;
		max = world.player.maxLife;
		super.calc();
	}
	
	public void post() {}
}
