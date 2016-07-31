package com.dungeon.game.textures;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class dirt extends proceduralTile {

	public dirt(int seed, int x, int y) {
		super(new int[]{seed, x, y});
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generateTexture(int args[]) {
		seed = args[0];
		x = args[1];
		y = args[2];
		Pixmap texMap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		
		int curX;
		int curY;
		for(int i = 0; i < 32; i++){
			for(int k = 0; k < 32; k++){
				curX = x*32+i;
				curY = y*32+k;
//				Random ran = new Random(1+seed*curX*curY);
//				Random ran1 = new Random(1+seed*(curX-1)*curY);
//				Random ran2 = new Random(1+seed*curX*(curY-1));
//				Random ran3 = new Random(1+seed*(curX+1)*curY);
//				Random ran4 = new Random(1+seed*curX*(curY+1));
				float[] ran = getDirtColor(curX, curY);
				float[] ran1 = getDirtColor(curX-1, curY);
				float[] ran2 = getDirtColor(curX, curY-1);
				float[] ran3 = getDirtColor(curX+1, curY);
				float[] ran4 = getDirtColor(curX, curY+1);
//				float num1 = (ran.nextFloat()+ran1.nextFloat()+ran2.nextFloat()+ran3.nextFloat()+ran4.nextFloat())/5;
//				float num2 = (ran.nextFloat()+ran1.nextFloat()+ran2.nextFloat()+ran3.nextFloat()+ran4.nextFloat())/5;
//				float num3 = (ran.nextFloat()+ran1.nextFloat()+ran2.nextFloat()+ran3.nextFloat()+ran4.nextFloat())/5;
				texMap.setColor(new Color((ran[0]*3 + ran1[0] + ran2[0] + ran3[0] + ran4[0])/7f,(ran[1]*3 + ran1[1] + ran2[1] + ran3[1] + ran4[1])/7f,(ran[2]*3 + ran1[2] + ran2[2] + ran3[2] + ran4[2])/7f, 1));

				
//				//check for dark dirt
//				int darkDirtRadius = ran.nextInt(5);
//				//well i did SOMETHING good here.... not stones but it'll be usefull haja
//				loop:
//				for(int y = -darkDirtRadius; y < darkDirtRadius; y++){
//					for(int x = -darkDirtRadius; x < darkDirtRadius; x++){
//						if(new Random(1+seed*(curX+x)*(curY+y)).nextFloat() < 0.03f && Math.sqrt(x*x + y*y) < darkDirtRadius){
//							texMap.setColor(new Color((90f + num1*30f) / 255f,(40f + num2*10) / 255f,(5f + num3*10) / 255f, 1));
//							break loop;
//						}
//					}
//				}
//				
//				//check for light dirt
//				int lightDirtRadius = ran.nextInt(4);
//				//well i did SOMETHING good here.... not stones but it'll be usefull haja
//				loop:
//				for(int j = -lightDirtRadius; j < lightDirtRadius; j++){
//					for(int l = -lightDirtRadius; l < lightDirtRadius; l++){
//						Random checker = new Random(1+seed*(curX+j)*(curY+l));
//						checker.nextFloat();
//						if(checker.nextFloat() < 0.03f){
//							texMap.setColor(new Color((130f + num1*70f) / 255f,(60f + num2*30) / 255f,(20f + num3*25) / 255f, 1));
//							break loop;
//						}
//					}
//				}
//				
//				//generate stones... somehow...
//				loop:
//					for(int j = -5; j < 5; j++){
//						for(int l = -5; l < 5; l++){
//							Random checker = new Random(1+seed*(curX+j)*(curY+l));
//							checker.nextFloat();
//							checker.nextFloat();
//							if(checker.nextFloat() < 0.001f){
//								int stoneSize = checker.nextInt(5);
//								float dist = (float) Math.sqrt(j*j+l*l);
//								if(dist < stoneSize){
//									texMap.setColor(new Color(0.25f+dist/10f,0.25f+dist/10f,0.25f+dist/10f, 1));
//									break loop;
//								}
//							}
//						}
//					}
				texMap.drawPixel(i, 31-k);
			}
		}
		texture = new Texture(texMap);
	}
	
	private float[] getDirtColor(int xP, int yP){
		Random ran = new Random(1+seed*xP*yP);
		
		//check for dark dirt
		int darkDirtRadius = ran.nextInt(4);
		//well i did SOMETHING good here.... not stones but it'll be usefull haja
		loop:
		for(int y = -darkDirtRadius; y < darkDirtRadius; y++){
			for(int x = -darkDirtRadius; x < darkDirtRadius; x++){
				if(new Random(1+seed*(xP+x)*(yP+y)).nextFloat() < 0.03f && Math.sqrt(x*x + y*y) < darkDirtRadius){
					return new float[]{(90f + ran.nextFloat()*30f) / 255f,(40f + ran.nextFloat()*10) / 255f,(5f + ran.nextFloat()*10) / 255f};
				}
			}
		}
		
		//check for light dirt
		int lightDirtRadius = ran.nextInt(5);
		//well i did SOMETHING good here.... not stones but it'll be usefull haja
		loop:
		for(int j = -lightDirtRadius; j < lightDirtRadius; j++){
			for(int l = -lightDirtRadius; l < lightDirtRadius; l++){
				Random checker = new Random(1+seed*(xP+j)*(yP+l));
				checker.nextFloat();
				if(checker.nextFloat() < 0.03f){
					return new float[]{(130f + ran.nextFloat()*70f) / 255f,(60f + ran.nextFloat()*30) / 255f,(20f + ran.nextFloat()*25) / 255f};
				}
			}
		}
		return new float[]{(100f + ran.nextFloat()*60f) / 255f,(50f + ran.nextFloat()*20) / 255f,(10f + ran.nextFloat()*20) / 255f};
		
	}
	

}
