package com.dungeon.game.item.weapon.swing;

import com.dungeon.game.world.World;

public class Rest extends Swing{
	
	public Rest(World world, int duration, int dist, int angle, int polarAngle){
		super(world, false, 0, 0, 0, 0, duration, dist, angle, polarAngle);
	}
	
	public void progressSwing(int counter){
	
	}
	
	public void progressPause(int counter){
		if(weapon.owner.leftEquiped.equals(weapon) && world.mouse.lb_pressed)nextSwing = true;
		else if(world.mouse.rb_pressed)nextSwing = true;
	}
	
	public void progress(){
		counter+=weapon.speed/10; //progress the counter
		if(counter < duration)progressSwing(counter);
		else progressPause(counter);
	}
}