package com.dungeon.game.entity.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.entity.hud.dialogue.Dialogue;
import com.dungeon.game.entity.hud.dialogue.SpeechBubble;
import com.dungeon.game.entity.hud.dialogue.SpeechChoice;
import com.dungeon.game.entity.hud.dialogue.SpeechPopup;
import com.dungeon.game.world.Tile;
import com.dungeon.game.world.World;

public class Mentor extends Friend {
	
	public Mentor(World world, float x, float y) {
		super(world, x, y);
		
		speechColor = Color.ORANGE;
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
		
		d_width = 32;
		d_height = 32;
		
		dialogue = new Dialogue(world, this);
		dialogue.potentialBubbles.add(new SpeechBubble(world,this,3,"Hello There! How you doin'?",1));
		dialogue.potentialBubbles.add(new SpeechChoice(world,new String[]{"Life Sucks","Not Bad"},new String[]{"I'm having a hard time...", "Things have been looking up!"},new int[]{2,3}));
		dialogue.potentialBubbles.add(new SpeechBubble(world,this,3,"Thats a shame. \nBut back to what I was saying... :(",1));
		dialogue.potentialBubbles.add(new SpeechBubble(world,this,3,"Good to hear, friend. \nBut lets talk!",1));
		dialogue.begin();
		
		sprite = new Texture("mentor.png");
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
