package com.dungeon.game.entity.hud;

import com.badlogic.gdx.graphics.Texture;
import com.dungeon.game.world.World;

public class PortraitBackground extends Hud {

	public PortraitBackground(World world, float x, float y) {
		super(world, x, y, 68, 68, "portraitBackground.png");

		d_width = 68;
		d_height = 68;
		sprite = new Texture("portraitBackground.png");
	}

	@Override
	public void calc() {
		super.calc();
	}

	@Override
	public void post() {}

}
