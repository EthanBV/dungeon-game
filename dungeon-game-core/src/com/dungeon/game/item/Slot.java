package com.dungeon.game.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dungeon.game.entity.Character;
import com.dungeon.game.world.World;

public class Slot {

	public Item item;
	
	public int type;
	
	public int x;
	public int y;
	
	private BitmapFont font;
	
	private Inventory inv;
	
	public Slot(int[] data, Inventory inv) {
		this.item = null;
		
		this.type = data[0];
		
		this.x = data[1];
		this.y = data[2];
		
		font = new BitmapFont(Gdx.files.internal("main_text.fnt"));
		font.setColor(Color.LIGHT_GRAY);
		
		this.inv = inv;
	}
	
	public void swap(Slot that) {
		Item temp = that.item;
		
		that.item = this.item;
		this.item = temp;
	}
	
	public void hovered(World world) {
		if(((world.mouse.slot.item==null?true:world.mouse.slot.item.type==type)||type==0)) {
			if(world.mouse.lb_pressed) {
				if(item != null && world.mouse.slot.item != null && world.mouse.slot.item.name.equals(item.name)) {
					if(item.stack + world.mouse.slot.item.stack <= item.maxStack) {
						item.stack+=world.mouse.slot.item.stack;
						world.mouse.slot.item = null;
					}
					else {
						world.mouse.slot.item.stack = item.stack+world.mouse.slot.item.stack-item.maxStack;
						item.stack = item.maxStack;
					}
				}
				else swap(world.mouse.slot);
			}
			else if(world.mouse.rb_pressed) {
				if(item != null && world.mouse.slot.item != null && world.mouse.slot.item.name.equals(item.name)) {
					if(world.mouse.slot.item.stack < item.maxStack) {
						item.stack--;
						world.mouse.slot.item.stack++;
					}
				}
				else if(item != null && world.mouse.slot.item == null) {
					if(item.stack == 1) {
						swap(world.mouse.slot);
					}
					else {
						item.stack--;
						world.mouse.slot.item = (Item) item.clone();
						world.mouse.slot.item.stack = 1;
					}
				}
				else if(item == null && world.mouse.slot.item != null) {
					if(world.mouse.slot.item.stack == 1) {
						swap(world.mouse.slot);
					}
					else {
						world.mouse.slot.item.stack--;
						item = (Item) world.mouse.slot.item.clone();
						item.stack = 1;
					}
				}
				else swap(world.mouse.slot);
				if(item != null && item.stack == 0) item = null;
				if(world.mouse.slot.item != null && world.mouse.slot.item.stack == 0) world.mouse.slot.item = null;
			}
			else if(world.mouse.mb_pressed) {
				if(item != null && item instanceof Consumable) {
					consume(world, world.player);
				}
			}
		}
		if(item != null)world.descBox.updateText(item);
	}
	
	public void calc(World world) {
		
	}
	
	public void update(World world) {
		calc(world);
	}
	
	public void draw(SpriteBatch batch, int xoff, int yoff) {
		if(item != null) {
			batch.draw(item.sprite, x+xoff, y+yoff, Item.SIZE, Item.SIZE);
			
			if(item.stack > 1) {
				
				font.draw(batch, Integer.toString(item.stack), (float) (x+xoff+Item.SIZE-font.getScaleX()*(Math.floor(Math.log10(item.stack))+ 1)*7)-4, y+yoff+font.getScaleY()*12+1);
			}
		}
	}
	
	public void consume(World world, Character user) {
		if(item!=null){
			item.stack--;
			if(item instanceof Consumable)((Consumable)item).use(world, user);
			if(item.stack == 0) item = null;
		}
	}
}
