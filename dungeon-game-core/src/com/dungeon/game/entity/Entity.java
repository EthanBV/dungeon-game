package com.dungeon.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dungeon.game.world.Floor;

public abstract class Entity {
	public float x;
	public float y;
	
	public int d_width;
	public int d_height;
	
	public int d_offx;
	public int d_offy;
	
	boolean solid;
	
	public String name;
	
	public Texture sprite;
	
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
		
		this.init();
	}
	
	public void update(Floor floor) {
		calc(floor);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(sprite, x+d_offx, y+d_offy, d_width, d_height);
	}
	
	public abstract void init();
	
	public abstract void calc(Floor floor);
}
