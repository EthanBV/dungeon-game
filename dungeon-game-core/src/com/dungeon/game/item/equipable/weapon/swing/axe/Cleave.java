package com.dungeon.game.item.equipable.weapon.swing.axe;

import com.dungeon.game.item.equipable.weapon.swing.Swing;
import com.dungeon.game.world.World;

public class Cleave extends AxeSwing {

	public Cleave(World world) {
		super(world, "Cleave");
		
	}

	@Override
	public void setPrevSwing(Swing prevSwing) {
		this.prevSwing = prevSwing;
		if(prevSwing.endingZone == LEFT){
			setStats(true, 10, 24, 80, 45, 8, 14, -65, -55, 0.7f, 1, -90, 0.2f, 1);
			endingZone = RIGHT;
		}else{
			setStats(true, 10, 24, -80, -45, 8, 14, 65, 55, 0.7f, 1, 90, 0.2f, 1);
			endingZone = LEFT;
		}
	}

}
