package com.dungeon.game.item;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.dungeon.game.effect.Effect;
import com.dungeon.game.effect.Stun;
import com.dungeon.game.entity.Projectile;
import com.dungeon.game.entity.RangedGraphic;
import com.dungeon.game.entity.character.Character;
import com.dungeon.game.world.World;

public class Bow extends Ranged {
	
	private final int REST = 0;
	private final int WINDUP = 1;
	private final int FIRE = 2;
	private final int WINDDOWN = 3;
	
	private final String texturePath;
	
	private Texture[] textures;

	public Bow(World world, float dmgMod, int speed) {
		super(world, new Texture("Bow.png"));
		
		name = "Bow";
		
		this.speed = speed;
		this.dmgMod = dmgMod;
		
		strength = 10;
		knockstr = 10;
		
		texturePath = "Bow.png";
		

		desc = "SEE: Arrow  \n\n Damage Modifier: " + Math.floor(dmgMod*1000)/10f + "%";

		graphic = new RangedGraphic(world, this, new Polygon(new float[]{2,2,30,30,8,26}), 4, 28);
		
		Texture tempSheet = new Texture(texturePath);
		
		int sheetWidth = tempSheet.getWidth()/Item.SIZE;
		int sheetHeight = tempSheet.getHeight()/Item.SIZE;
		
		TextureRegion[] spritesheet = new TextureRegion[sheetWidth * sheetHeight];
		
		for(int i = 0; i < sheetHeight; i++) {
			for(int k = 0; k < sheetWidth; k++) {
				spritesheet[i*sheetWidth+k] = new TextureRegion(new Texture(texturePath),k*Item.SIZE,i*Item.SIZE,Item.SIZE,Item.SIZE);
			}
		}
		
		textures  = new Texture[spritesheet.length];
		if (!tempSheet.getTextureData().isPrepared()) {
		    tempSheet.getTextureData().prepare();
		}
		Pixmap wholePixmap = tempSheet.getTextureData().consumePixmap();
		
		for(int i = 0; i < textures.length; i++){
			Pixmap tempMap = new Pixmap(Item.SIZE, Item.SIZE, Pixmap.Format.RGBA8888);
			for (int x = 0; x < spritesheet[i].getRegionWidth(); x++) {
			    for (int y = 0; y < spritesheet[i].getRegionHeight(); y++) {
			        int colorInt = wholePixmap.getPixel(spritesheet[i].getRegionX() + x, spritesheet[i].getRegionY() + y);
			        tempMap.drawPixel(x, y, colorInt);
			    }
			}
			textures[i] = new Texture(tempMap);
			tempMap.dispose();
		}
		wholePixmap.dispose();
		changeSprite(textures[0]);
	}

	@Override
	public boolean isInUse() {
		if(stage == WINDUP||stage == FIRE)return true;
		return false;
	}

	@Override
	public float[] getPos(boolean mousedown, boolean mousepressed) {
		int distance=30;
		int polarAngle= 10;
		int angle=0;
		stageTimer++;
		int constant=2;
		int index = stageTimer*constant;
		switch(stage){
		case REST:
		if(!sprite.equals(textures[0])){
			changeSprite(textures[0]);
		}
			if(mousepressed && owner.inv.contains(new Arrow(world)) != null&&owner.use_stam(10)){
				owner.inv.contains(new Arrow(world)).item.stack--;
				if(owner.inv.contains(new Arrow(world)).item.stack==0)owner.inv.contains(new Arrow(world)).item = null;
				stage=WINDUP;
				stageTimer = 0;
			}
			break;
			
		case WINDUP:
			if(index<45){
				if(!sprite.equals(textures[1])){
					changeSprite(textures[1]);
				}
			}
			else{
				if(!sprite.equals(textures[2])){
					changeSprite(textures[2]);
				}
			}
			
			if(!mousedown){
				stageTimer = 0;
				stage = FIRE;
				((RangedGraphic) graphic).fire(strength*Math.min(index, 45)/45);
			}
			break;
		case FIRE:
			if(!sprite.equals(textures[0])){
				changeSprite(textures[0]);
			}
			if(index>10){
				stageTimer = 0;
				stage = REST;
			}
			break;
		}
		return new float[]{distance,polarAngle,angle};
	}
	
	public ArrayList<Effect> hitEffects(){
		ArrayList<Effect> effects = new ArrayList<Effect>();
		effects.add(new Stun(world, 30));
		return effects;
		
	}
	
	public String getDesc() {
		return "Hold to pull back, release to fire, repeat to win. \n\n" + desc;
	}

	@Override
	public void reset() {
		stage = REST;
		stageTimer = 0;
		
		distance = 30;
		angle = 0;
		polarAngle = 10;
		
		changeSprite(textures[0]);
	}

}
