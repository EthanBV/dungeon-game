package com.dungeon.game.entity.furniture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.entity.Static;
import com.dungeon.game.utilities.Spritesheet;
import com.dungeon.game.world.World;

public class Bookshelf extends Static{

	public Bookshelf(World world, float x, float y, int width, int height) {
		super(world, x, y, 16, 16, "shelfsheet.png");
		
		Pixmap tempMap = new Pixmap(width*16, height*16, Pixmap.Format.RGB888);
		
		Pixmap[] textPixmaps = new Pixmap[textures.length];
		
		for(int i = 0; i < textures.length; i++) {
			if(!textures[i].getTextureData().isPrepared()) textures[i].getTextureData().prepare();
			textPixmaps[i] = textures[i].getTextureData().consumePixmap();
		}
		int[][][] shelfMap = new int[height][width][2];
		for(int i = 0; i < width; i++) {
			for(int k = 0; k < height; k++) {
				int index = 0;
				int rotation = 0;
				if(i == 0 && k == 0){
					index = 0;
					rotation = width>height?0:3;
				}else if(i == 0 && k == height - 1){
					index = 0;
					rotation = width>height?2:3;
				}else if(i == width - 1 && k == 0){
					index = 0;
					rotation = width>height?0:1;
				}else if(i == width - 1 && k == height - 1){
					index = 0;
					rotation = width>height?2:1;
				}else if(i == 0){
					index = 0;
					rotation = 3;
				}else if(i == width-1){
					index = 0;
					rotation = 1;
				}else if(k == 0){
					index = 0;
					rotation = 0;
				}else if(k == height-1){
					index = 0;
					rotation = 2;
				}else{
					rotation = 0;
					index = 1;
				}
				shelfMap[k][i] = new int[]{index, rotation};
				Pixmap rotated = Spritesheet.rotatePixmap(textPixmaps[index],rotation);
				tempMap.drawPixmap(rotated, i*16, k*16);
			}
		}
		
		sprite = new Texture(tempMap);
		
		sprite = new com.dungeon.game.textures.entity.Bookshelf(width, height, shelfMap).texture;
		
		tempMap.dispose();
		
		dWidth = sprite.getWidth();
		dHeight = sprite.getHeight();
		
		hitbox = new Polygon(new float[] {0,0,width*16,0,width*16,height*16,0,height*16});
		genVisBox();
		
		originX = dWidth/2;
		originY = dHeight/2;
		
		solid = true;
		rotate = true;
	}
	
	public Bookshelf(World world, float x, float y, int[][][] shelfMap) {
		super(world, x, y, 16, 16, "shelfsheet.png");
		
		int width = shelfMap[0].length;
		int height = shelfMap.length;
		
		Pixmap tempMap = new Pixmap(width*16, height*16, Pixmap.Format.RGB888);
		
		Pixmap[] textPixmaps = new Pixmap[textures.length];
		
		for(int i = 0; i < textures.length; i++) {
			if(!textures[i].getTextureData().isPrepared()) textures[i].getTextureData().prepare();
			textPixmaps[i] = textures[i].getTextureData().consumePixmap();
		}
		
		for(int i = 0; i < width; i++) {
			for(int k = 0; k < height; k++) {
				int index = 0;
				int rotation = 0;
				if(shelfMap[k][i][0] == -1){
					index = (int)(Math.random()*3);
				}
				else index = shelfMap[k][i][0];
				rotation = shelfMap[k][i][1];
				Pixmap rotated = Spritesheet.rotatePixmap(textPixmaps[index],rotation);
				tempMap.drawPixmap(rotated, i*16, k*16);
			}
		}
		
		sprite = new Texture(tempMap);
		
		sprite = new com.dungeon.game.textures.entity.Bookshelf(width, height, shelfMap).texture;
		
		tempMap.dispose();
		
		
		dWidth = sprite.getWidth();
		dHeight = sprite.getHeight();
		
		hitbox = new Polygon(new float[] {0,0,width*16,0,width*16,height*16,0,height*16});
		genVisBox();
		
		originX = dWidth/2;
		originY = dHeight/2;
		
		solid = true;
		rotate = true;
	}


	@Override
	public void calc() {
		
	}

	@Override
	public void post() {}
}
