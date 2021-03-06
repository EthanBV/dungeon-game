package com.dungeon.game.entity.hud.dialogue;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dungeon.game.entity.character.friend.Friend;
import com.dungeon.game.entity.hud.Hud;
import com.dungeon.game.item.Item;
import com.dungeon.game.world.World;

public class SpeechPopup extends Hud {
	private final NinePatch SPEECH_POPUP = new NinePatch(new Texture("speechBubble.png"), 21, 4, 4, 21);
	
	public String text;
	
	public String endText;
	
	BitmapFont font;
	
	private Friend character;
	
	public boolean dismissed;
	
	private int speechSpeed;
	private int speechCounter;

	public SpeechPopup(World world, Friend character) {
		super(world, 0, 0, Item.SIZE, Item.SIZE, "slot.png");
		this.character = character;
		dWidth = 200;
		dHeight = 100;
		
		dOffX = 16;
		dOffY = 0;
		
		font = new BitmapFont(Gdx.files.internal("main_text.fnt"));
		font.getData().setScale(1f);
		font.setUseIntegerPositions(false);
		
		speechSpeed = 3;
		speechCounter = 0;
		text = "";
		endText = "";
		dismissed = false;
	}
	
	@Override
	public void calc() {
		if(speechCounter == 0){
			int textLength = text.length();
			int endTextLength = endText.length();
			if(textLength!=endTextLength){
				text+=endText.charAt(textLength);
			}
			speechCounter = speechSpeed;
			
			char lastChar = text.charAt(text.length()-1);
			
			if(lastChar == ' '||lastChar == '\'') speechCounter = 0;
			else if(lastChar == ',') speechCounter*=3;
			else if(lastChar == '.' || lastChar == '!' || lastChar == '?' || lastChar == ';') speechCounter*=5;
			else if(lastChar == '\u200B') speechCounter*=2;
			else if(lastChar == '-') speechCounter*=10;
			else if(lastChar == ':') speechCounter*=10;
		}
		else speechCounter--;
		
		if(!world.entities.contains(character)) {
			dismissed = true;
			close();
		}
	}
	
	public void updateText(String text) {
		this.endText = "";
		this.text = "";
		
		ArrayList<String> lines = new ArrayList<String>(Arrays.asList(text.split("\\r?\\n")));
		
		int max_line_length = 0;
		
		for(int i = 0; i < lines.size(); i++) {
			int startIndex = 0;
			int charsCovered = 0;
			for(int k = 0; k < lines.get(i).length();k++){
				if(lines.get(i).charAt(k)!='\u200B'){
					charsCovered++;
					if(charsCovered == 20){
						startIndex = k;
						break;
					}
				}
			}
			if(charsCovered == 20) {
				for(int k = startIndex; k > 0; k--) {
					if(lines.get(i).charAt(k) == ' ') {
						lines.add(i+1,lines.get(i).substring(k+1));
						
						lines.set(i,lines.get(i).substring(0,k));
						
						break;
					}
				}
			}
			
			endText += lines.get(i) + "\n";
		}
		
		for( String line: lines){
			int length = 0;
			for(int k = 0; k < line.length(); k++){
				if(line.charAt(k)!='\u200B'){
					length++;
				}
			}
			max_line_length = Math.max(max_line_length, length);
		}
		
		if(text.equals("")){
			text = "";
			endText = "";
		}
		
		dHeight = lines.size() * 16 + 18;
		
		dWidth = max_line_length * 9 + 38;
	}
	
	public void hovered(){
		if(!dismissed){
			if(world.mouse.rb_pressed){
				dismissed = true;
				close();
			}
			else if(world.mouse.lb_pressed) {
				if(character.dialogue != null) {
					character.dialogue.open();
					world.player.focusedEntity = character;
					dismissed = true;
					close();
				}
			}
		}
	}
	
	public boolean done(){
		return text==null||text.equals(endText);
	}

	@Override
	public void post() {

	}
	
	public void setColor() {
		Color temp = character.speechColor;
		temp.a = 0.7f;
		font.setColor(temp);
	}
	
	public void draw(SpriteBatch batch) {
		batch.setColor(1,1,1,0.8f);
		if(!dismissed&&!text.equals("")) {
			SPEECH_POPUP.draw(batch, x, y, dWidth-dOffX, dHeight-dOffY);
			
			font.draw(batch, text, x+16, y+dHeight-6);
		}

		batch.setColor(1,1,1,1);
	}
	
	public void close(){
		world.hudEntities.remove(this);
	}
	
	public void open(){
		world.hudEntities.add(this);
	}
		

}
