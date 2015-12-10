package com.dungeon.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.dungeon.game.item.Inventory;
import com.dungeon.game.item.Item;
import com.dungeon.game.world.Tile;
import com.dungeon.game.world.World;

public class Chest extends Static {
	public Inventory inv;
	public Chest(int x, int y) {
		super(x, y);
	}

	@Override
	public void init() {
		int[][] invLayout = new int[][]{
			new int[] {0, 8, 8},
			new int[] {0, 48, 8},
			new int[] {0, 88, 8},
			new int[] {0, 128, 8},
			new int[] {0, 168, 8},
			new int[] {0, 8, 48},
			new int[] {0, 48, 48},
			new int[] {0, 88, 48},
			new int[] {0, 128, 48},
			new int[] {0, 168, 48},
			new int[] {0, 8, 88},
			new int[] {0, 48, 88},
			new int[] {0, 88, 88},
			new int[] {0, 128, 88},
			new int[] {0, 168, 88},
			new int[] {0, 8, 128},
			new int[] {0, 48, 128},
			new int[] {0, 88, 128},
			new int[] {0, 128, 128},
			new int[] {0, 168, 128},
			new int[] {0, 8, 168},
			new int[] {0, 48, 168},
			new int[] {0, 88, 168},
			new int[] {0, 128, 168},
			new int[] {0, 168, 168}
		};
		inv = new Inventory(invLayout, "chestBackground.png", 200, 140, 0, 200, 208, 16);
		sprite = new Texture("chest.png");
		solid = true;
		
		d_width = 32;
		d_height = 32;
		
		width = 32;
		height = 32;
	}

	@Override
	public void calc(World world) {
		if(world.mouse.rb_pressed&&world.mouse.x > x-world.cam.x+world.cam.WIDTH/2 && world.mouse.x < x+Item.SIZE-world.cam.x+world.cam.WIDTH/2 && world.mouse.y > y-world.cam.y+world.cam.HEIGHT/2 && world.mouse.y < y+Item.SIZE-world.cam.y+world.cam.HEIGHT/2){
			if(world.hudEntities.contains(inv.graphic)){
				world.hudEntities.remove(inv.graphic);
			}else{
				world.hudEntities.add(0,inv.graphic);
			}
		}
		if(Math.sqrt(Math.pow((x+d_width/2) - (world.player.x + world.player.d_width/2), 2) + Math.pow((y+d_height/2) - (world.player.y + world.player.d_height/2), 2)) >= world.player.REACH){
			world.hudEntities.remove(inv.graphic);
		}
		for(int i = 0; i< world.curFloor.tm.length;i++){
			for(int k = 0; k <world.curFloor.tm[i].length;k++){
				if(world.curFloor.tm[k][i].data==1){
					float[] verticies = new float[]{i*Tile.TS,k*Tile.TS,(i+1)*Tile.TS,k*Tile.TS,(i+1)*Tile.TS,(k+1)*Tile.TS,(i)*Tile.TS,(k+1)*Tile.TS};
					if(Intersector.intersectSegmentPolygon(new Vector2(x+d_width/2,y+d_height/2), new Vector2(world.player.x + world.player.d_width/2,world.player.y + world.player.d_height/2), new Polygon(verticies))) world.hudEntities.remove(inv.graphic);
				}
			}
		}
	}
		

}
