package com.dungeon.game.entity.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.criteria.Criteria;
import com.dungeon.game.criteria.HasItem;
import com.dungeon.game.criteria.Invert;
import com.dungeon.game.criteria.True;
import com.dungeon.game.entity.hud.dialogue.Dialogue;
import com.dungeon.game.entity.hud.dialogue.InvBubble;
import com.dungeon.game.entity.hud.dialogue.SpeechBubble;
import com.dungeon.game.entity.hud.dialogue.SpeechChoice;
import com.dungeon.game.inventory.Inventory;
import com.dungeon.game.inventory.Shop;
import com.dungeon.game.item.Arrow;
import com.dungeon.game.item.LifePotion;
import com.dungeon.game.item.Sword;
import com.dungeon.game.world.Tile;
import com.dungeon.game.world.World;

public class Mentor extends Friend {
	
	public Mentor(World world, float x, float y) {
		super(world, x, y, 32, 32, "mentor.png");
		
		speechColor = new Color(0.1f,0.5f,0.1f,1);
		speechBubble.setColor();	
		
		name = "Mentor";
		
		torq = 5;
		
		vision = 10;
		hearing = 3;
		
		speechRadius = 3;
		
		maxLife = 1;
		maxStam = 1;
		maxMana = 1;
		
		life = maxLife;
		stam = maxStam;
		mana = maxMana;
		
		acel = 1.5f;
		mvel = 5;
		fric = 0.5f;
		
		hitbox = new Polygon(new float[]{2,2,30,2,30,30,2,30});
		
		origin_x = 16;
		origin_y = 16;

		Shop shop = new Shop(world, new int[][]{new int[]{0,10,10},new int[]{0,10,60},new int[]{0,10,110}},new int[]{10,200,20}, this, 10, 10);
		shop.slot[0].item = new Arrow(world);
		shop.slot[0].item.stack = 10;
		shop.slot[1].item = new Sword(world, 10, 10);
		shop.slot[2].item = new LifePotion(world);
		shop.slot[2].item.stack = 10;
		
		// \u200B to create pause;
		dialogue = new Dialogue(world, this);
		
		dialogue.potentialBubbles.put("start", new SpeechBubble(world, this,"Hey there.", "want sword question"));
	
		
		dialogue.potentialBubbles.put("want sword question", new SpeechBubble(world, this, "Do you want a sword? You'll need a weapon to fight off the monsters. There's quite a few around here...", "want sword answer"));
		
		dialogue.potentialBubbles.put("want sword answer", new SpeechChoice(world, 
				new String[]{"Yes.", "I have a sword.","Any wares?"}, 
				new String[]{"Yeah, I'll take a sword.", "I already have a sword.", "Actually, I'd like to buy some of your wares."},
				new String[]{"check no sword", "check sword","wares"}));
		
		dialogue.potentialBubbles.put("wares", new SpeechBubble(world, this, "Absolutely, here-", "shit shop"));
		
		dialogue.potentialBubbles.put("shit shop", new InvBubble(world, this, shop, "goodbye"));
		
		dialogue.potentialBubbles.put("lie defence", new SpeechChoice(world, 
				new String[]{"What's it to you?", "What if I wanted two?", "Didn't know you cared."}, 
				new String[]{"What's it to you?", "What if I wanted two swords? Maybe I want to dual wield...", "Sorry, didn't know you cared that I already had a sword... My bad."},
				new String[]{"refuse sword", "check two swords", "reasure"}));
		
		dialogue.potentialBubbles.put("check two swords", new SpeechBubble(world, this, 
				new Criteria[]{new HasItem(world, new Sword(world, 0, 0), world.player, 2), new True(world)},
				new String[]{"Bu-\u200B\u200B\u200B Wha-\u200B\u200B\u200B I can't even believe you. You clearly have two swords... What are you even doing? Besides...", "Even if that were the case..."},
				"refuse sword"));
		
		dialogue.potentialBubbles.put("reasure", new SpeechBubble(world, this, "It's okay.", "refuse sword"));
		
		dialogue.potentialBubbles.put("refuse sword", new SpeechBubble(world, this, "I'm trying to help people who are unarmed. But you're fine, so I'm not going to give you anything.", "goodbye"));
		
		dialogue.potentialBubbles.put("check no sword", new SpeechBubble(world, this, 
				new Criteria[]{new Invert(world, new HasItem(world, new Sword(world, 0, 0), world.player)), new True(world)},
				new String[]{"No problem.", "Wait! you already have a sword! What are you tryin' to pull?"},
				new String[]{"give sword", "lie defence"}));
		
		dialogue.potentialBubbles.put("check sword", new SpeechBubble(world, this,
				new Criteria[]{new HasItem(world, new Sword(world, 0, 0), world.player), new True(world)},
				new String[]{"Oh, alright.", "Um... no you don't."}, 
				new String[]{"goodbye", "give sword"}));

		dialogue.potentialBubbles.put("give sword", new SpeechBubble(world, this, "Here ya go!", "giftSword"));
		
		Inventory invent = new Inventory(world, new int[][]{new int[]{0,0,0},new int[]{0,0,0}}, 0, 0, true);
		invent.slot[0].item = new Sword(world, 7, 10);
		invent.slot[1].item = new Arrow(world);
		invent.slot[1].item.stack = 10;
		dialogue.potentialBubbles.put("giftSword", new InvBubble(world, this, invent , "goodbye"));
		
		dialogue.potentialBubbles.put("goodbye", new SpeechBubble(world, this, "See ya!", "end"));
		
		dialogue.begin();
	}

	@Override
	public void calc() {
		if(seenEntities.contains(world.player)){
			target_angle = (float) (180/Math.PI*Math.atan2(world.player.y-y, world.player.x-x));
			if(!world.player.fightMode&&speechBubble.endText.equals("")){
				if(!world.hudEntities.contains(dialogue))speechBubble.updateText("I am here to help.");
				speechBubble.dismissed = false;
			}
			
		}else{
			speechBubble.updateText("");
			dialogue.close();
		}
		if(world.player.fightMode||Math.sqrt((x-world.player.x)*(x-world.player.x)+(y-world.player.y)*(y-world.player.y))>speechRadius*Tile.TS)speechBubble.updateText("");
	}
	
	public void hovered() {
		if(world.mouse.lb_pressed) {
			dialogue.open();
			speechBubble.dismissed = true;
		}
	}
	
	@Override
	public void post() {
		if(speechBubble.endText.equals("")){
			world.hudEntities.remove(speechBubble);
		}else{
			if(!world.hudEntities.contains(speechBubble))world.hudEntities.add(speechBubble);	
			speechBubble.x = x-world.cam.x+world.cam.WIDTH/2;
			speechBubble.y = y-world.cam.y+world.cam.HEIGHT/2;
		}
		
	}

}
