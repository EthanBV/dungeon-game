package com.dungeon.game.entity.hud.dialogue;

import com.dungeon.game.entity.character.Character;
import com.dungeon.game.inventory.Inventory;
import com.dungeon.game.inventory.Shop;
import com.dungeon.game.world.World;

public class InvBubble extends SpeechChoice {

	public Inventory inv;
	public InvDisplayBubble bubble;
	
	public InvBubble(World world, Character character, Inventory inv, String key) {
		super(world, new String[]{"Close"},new String[]{"Thank you."},new String[]{key});
		this.inv = inv;
		
		hasBeenSaid = false;
		
		if(inv instanceof Shop) bubble = new ShopDisplayBubble(world, character, (Shop)inv);
		else bubble = new InvDisplayBubble(world, character, inv);
	}

}
