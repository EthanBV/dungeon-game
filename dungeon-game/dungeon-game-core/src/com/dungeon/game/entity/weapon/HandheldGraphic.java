package com.dungeon.game.entity.weapon;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.entity.Dynamic;
import com.dungeon.game.item.Item;
import com.dungeon.game.item.equipable.Hand;
import com.dungeon.game.utilities.Spritesheet;
import com.dungeon.game.world.World;

public class HandheldGraphic extends Dynamic {
	
	public Hand item;
	
	public int graphic_angle;
	public int graphic_pAngle;
	public int graphic_dist;
	
	public boolean toFlip;
	public boolean flipped;
	
	public HandheldGraphic(World world, Hand item, Polygon hitbox, float originX, float originY) {
		super(world, 0, 0, Item.SIZE, Item.SIZE, "slot.png"); // x and y don't matter, they are set every frame
		sprite = item.sprite;
		
		name = "Graphic";
		this.item = item;
		rotate = true;
		this.hitbox = hitbox;
		
		this.origin_x = originX;
		this.origin_y = originY;
	}

	@Override
	public void calc() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}
	
	public void updatePos(boolean left){
		if(left){
			float xMove = (float) (Math.cos((item.owner.angle+graphic_pAngle)/180*Math.PI)*graphic_dist);
			float yMove = (float) (Math.sin((item.owner.angle+graphic_pAngle)/180*Math.PI)*graphic_dist);
			x = (float) (item.owner.x)+xMove;
			y = (float) (item.owner.y)+yMove;
			angle = item.owner.angle-135+graphic_angle;
			if(toFlip && !flipped){ // it works don't ask questions
				sprite = item.sprite;
				if(!sprite.getTextureData().isPrepared())sprite.getTextureData().prepare();
				Pixmap tempMap = sprite.getTextureData().consumePixmap();
				tempMap = Spritesheet.flipPixmap(Spritesheet.rotatePixmap(tempMap, 1));
				sprite = new Texture(tempMap);
				flipped = true;
			}else if(!toFlip && flipped){
				sprite = item.sprite;
				flipped = false;
			}
		}else{
			float xMove = (float) (Math.cos((item.owner.angle-graphic_pAngle)/180*Math.PI)*graphic_dist);
			float yMove = (float) (Math.sin((item.owner.angle-graphic_pAngle)/180*Math.PI)*graphic_dist);
			x = (float) (item.owner.x)+xMove;
			y = (float) (item.owner.y)+yMove;
			angle = item.owner.angle-135-graphic_angle;
			if(toFlip && !flipped){
				sprite = item.sprite;
				flipped = true;
			}else if(!toFlip && flipped){
				sprite = item.sprite;
				if(!sprite.getTextureData().isPrepared())sprite.getTextureData().prepare();
				Pixmap tempMap = sprite.getTextureData().consumePixmap();
				tempMap = Spritesheet.flipPixmap(Spritesheet.rotatePixmap(tempMap, 1));
				sprite = new Texture(tempMap);
				flipped = false;
			}
		}
		
	}

}
