package com.dungeon.game.spritesheet;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Spritesheet {
	public static Texture[] getSprites(String filename, int width, int height) {
		Texture[] textures;
		
		Texture tempSheet = new Texture(filename);
		
		int sheetWidth = tempSheet.getWidth()/width;
		int sheetHeight = tempSheet.getHeight()/height;
		
		TextureRegion[] spritesheet = new TextureRegion[sheetWidth * sheetHeight];
		
		for(int i = 0; i < sheetHeight; i++) {
			for(int k = 0; k < sheetWidth; k++) {
				spritesheet[i*sheetWidth+k] = new TextureRegion(new Texture(filename), k*width,i*height,width,height);
			}
		}
		
		textures  = new Texture[spritesheet.length];
		if (!tempSheet.getTextureData().isPrepared()) {
		    tempSheet.getTextureData().prepare();
		}
		Pixmap wholePixmap = tempSheet.getTextureData().consumePixmap();
		
		for(int i = 0; i < textures.length; i++){
			Pixmap tempMap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
			for (int x = 0; x < spritesheet[i].getRegionWidth(); x++) {
			    for (int y = 0; y < spritesheet[i].getRegionHeight(); y++) {
			        int colorInt = wholePixmap.getPixel(spritesheet[i].getRegionX() + x, spritesheet[i].getRegionY() + y);
			        tempMap.drawPixel(x, y, colorInt);
			    }
			}
			textures[i] = new Texture(tempMap);
//			tempMap.dispose();
		}
		wholePixmap.dispose();
		
		return textures;
	}
	
	public static Pixmap rotatePixmap (Pixmap src, int numRotates){
		if(numRotates == 0) return src;
		Pixmap rotated = new Pixmap(src.getHeight(), src.getWidth(), src.getFormat());
		for (int x = 0; x < src.getWidth(); x++) {
	        for (int y = 0; y < src.getHeight(); y++) {
	        	int offx = x - src.getWidth()/2, offy = y - src.getHeight()/2+1;
	        	int destx = -offy + rotated.getWidth()/2, desty = offx+rotated.getHeight()/2;
	        	rotated.drawPixel(destx, desty, src.getPixel(x, y));
	        }
		}
		if(numRotates == 1)return rotated;
		return rotatePixmap(rotated, numRotates-1);
		
	}
	
}
