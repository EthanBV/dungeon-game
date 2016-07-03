package com.dungeon.game.entity.hud;

import com.dungeon.game.world.World;

public abstract class Button extends Hud {

	public Button(World world, float x, float y, int width, int height, String filename){
		super(world, x, y, width, height, filename);
	}
	
	public void hovered() {
		super.hovered();
		if(world.mouse.lb_pressed) {
			System.out.println("test");
			click();
		}
	}
	
	public abstract void click();

}
