package com.dungeon.game.entity.hud;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dungeon.game.world.World;
import com.dungeon.game.entity.Character;

public class SpeechPopup extends Hud {
	private final NinePatch SPEECH_POPUP = new NinePatch(new Texture("speechBubble.png"), 4, 4, 4, 4);
	
	private int bubbleWidth;
	private int bubbleHeight;
	
	public String text;
	
	public String endText;
	
	BitmapFont font;
	
	private Character character;
	
	private int speechSpeed;
	private int speechCounter;

	public SpeechPopup(World world, Character character) {
		super(world, 0, 0);
		this.character = character;
		d_width = 200;
		d_height = 100;
		
		d_offx = 16;
		d_offy = 0;
		
		font = new BitmapFont(Gdx.files.internal("main_text.fnt"));
		font.setColor(0,0,0,0.75f);
		font.getData().setScale(1f);
		
		speechSpeed = 5;
		speechCounter = 0;
		text = "";
		endText = "";
	}

	@Override
	public void init() {
	}

	@Override
	public void calc() {
		if(speechCounter == 0){
			System.out.println("addChar");
			int textLength = text.length();
			int endTextLength = endText.length();
			if(textLength!=endTextLength){

				System.out.println("addedChar");
				text+=endText.charAt(textLength);
			}
			speechCounter = speechSpeed;
		}else speechCounter--;
	}
	
	public void updateText(String text) {
		this.endText = "";
		this.text = "";
		
		ArrayList<String> lines = new ArrayList<String>(Arrays.asList(text.split("\\r?\\n")));
		
		int max_line_length = 0;
		
		for(int i = 0; i < lines.size(); i++) {
			if(lines.get(i).length() > 30) {
				for(int k = 30; k > 0; k--) {
					if(lines.get(i).charAt(k) == ' ') {
						lines.add(i+1,lines.get(i).substring(k+1));
						
						lines.set(i,lines.get(i).substring(0,k));
						
						break;
					}
				}
			}
			
			max_line_length = Math.max(max_line_length, lines.get(i).length());
			
			endText += lines.get(i) + "\n";
		}
		
		if(text.equals("")){
			text = "";
			endText = "";
		}
		
		d_height = lines.size() * 16 + 8;
		
		d_width = max_line_length * 9 + 27;
	}
	
	public void hovered(){
		if(world.mouse.rb_pressed){
			updateText("");
		}
	}
	
	public boolean done(){
		return text==null||text.equals(endText);
	}

	@Override
	public void post() {

	}
	
	public void draw(SpriteBatch batch) {
		if(!text.equals("")) {
			System.out.println("test");
//			if(x + d_width > bubbleWidth) x -= d_width - 32;
//			if(y + d_height > bubbleHeight) y -= d_height + 16;
			
			SPEECH_POPUP.draw(batch, x, y, d_width-d_offx, d_height-d_offy);
			
			font.draw(batch, text, x+6, y+d_height-6);
			
//			text = "";
		}
	}
		

}
