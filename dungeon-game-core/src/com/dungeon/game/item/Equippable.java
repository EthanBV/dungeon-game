package com.dungeon.game.item;

public abstract class Equippable extends Item {

	public int durability;
	public int curDurability;
	
	public Equippable(String name, String desc, int durability) {
		super();
		
		this.durability  = durability;
		curDurability = durability;
	}
	
	public void use(){
		curDurability--;
	}
	
	

}
