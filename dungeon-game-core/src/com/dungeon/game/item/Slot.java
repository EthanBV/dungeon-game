package com.dungeon.game.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dungeon.game.world.World;

public class Slot {

	public Item item;
	
	public int type;
	
	public int x;
	public int y;
	
	private Inventory inv;
	
	public Slot(int[] data, Inventory inv) {
		this.item = null;
		
		this.type = data[0];
		
		this.x = data[1];
		this.y = data[2];
		
		this.inv = inv;
	}
	
	public void swap(Slot that) {
		Item temp = that.item;
		
		that.item = this.item;
		this.item = temp;
	}
	
	public void calc(World world) {
		if(world.mouse.lb_pressed && world.mouse.x > x+inv.graphic.x && world.mouse.x < x+Item.SIZE+inv.graphic.x && world.mouse.y > y+inv.graphic.y && world.mouse.y < y+Item.SIZE+inv.graphic.y) {
			if(item != null && world.mouse.slot.item != null && world.mouse.slot.item.name.equals(item.name) && item.stack < item.maxStack) {
				item.stack+=world.mouse.slot.item.stack;
				world.mouse.slot.item = null;
			}
			else swap(world.mouse.slot);
		}
		else if(world.mouse.rb_pressed && world.mouse.x > x+inv.graphic.x && world.mouse.x < x+Item.SIZE+inv.graphic.x && world.mouse.y > y+inv.graphic.y && world.mouse.y < y+Item.SIZE+inv.graphic.y) {
			if(item != null && world.mouse.slot.item != null && world.mouse.slot.item.name.equals(item.name) && world.mouse.slot.item.stack < item.maxStack) {
				item.stack--;
				world.mouse.slot.item.stack++;
			}
			else if(item != null && world.mouse.slot.item == null) {
				item.stack--;
				world.mouse.slot.item = (Item) item.clone();
				world.mouse.slot.item.stack = 1;
			}
			else if(item == null && world.mouse.slot.item != null) {
				world.mouse.slot.item.stack--;
				item = (Item) world.mouse.slot.item.clone();
				item.stack = 1;
			}
			else swap(world.mouse.slot);
			if(item != null && item.stack == 0) item = null;
			if(world.mouse.slot.item != null && world.mouse.slot.item.stack == 0) world.mouse.slot.item = null;
		}
	}
	
	public void update(World world) {
		calc(world);
	}
	
	public void draw(SpriteBatch batch, int xoff, int yoff) {
		if(item != null) {
			batch.draw(item.sprite, x+xoff, y+yoff, Item.SIZE, Item.SIZE);
			
			if(item.stack > 1) {
				BitmapFont font = new BitmapFont();
				font.setColor(Color.LIGHT_GRAY);
				font.getData().setScale(1f);
				
				font.draw(batch, Integer.toString(item.stack), x+xoff+Item.SIZE-font.getScaleX()*8, y+yoff+font.getScaleY()*12+1);
			}
		}
	}
}
