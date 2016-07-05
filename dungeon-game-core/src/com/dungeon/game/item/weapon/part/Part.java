package com.dungeon.game.item.weapon.part;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dungeon.game.effect.Effect;
import com.dungeon.game.item.Item;
import com.dungeon.game.item.weapon.Weapon;
import com.dungeon.game.world.World;

public abstract class Part extends Item implements Cloneable{	
	
	protected static final Texture slot = new Texture("slot.png");

	public static int NUM;
	public static Texture[] SPRITES;
	
	public static Constructor<?>[] parts;
	
	//what type of weapon does this part go on?
	public int type;
	
	//types
	public static final int SWORD = 0;
	public static final int AXE = 1;
	public static final int BOW = 2;
	
	//what part of the weapon does it go on?
	public int part;
	
	//what is the unique Id?
	public int id;
	
	//each part has a unique id
	
	//which swings are allowed by this part?
	public String[] allowedSwings;
	
	//which swings are banned by this part?
	public String[] bannedSwings;
	
	//damage multiplier for this part
	public float damage;
	
	//speed multiplier for this part
	public float speed;
	
	//knockback multiplier for this part
	public float knockback;
	
	//stanima multiplier for this part
	public float weight;
	
	protected BitmapFont font;
	
	public Part(World world, String name, Texture sprite, int level) {
		super(world, "slot.png");
		
		this.sprite = sprite;
		this.name = name;
		
		font = new BitmapFont(Gdx.files.internal("main_text.fnt"));
		font.setColor(Color.WHITE);
		
		setStats(level);
	}

	public void hovered(){
		world.descBox.updateText(getDesc());
	}
	
	public String getDesc(){
		return name + "\n Damage: " + damage + "\n Speed: " + speed + "\n Knockback: " + knockback + "\n Weight: " + weight;
	}
	
	public abstract void draw(SpriteBatch batch, float x, float y);
	
	public Part clone(World world) {
		Part newPart = (Part) super.clone();
		newPart.allowedSwings = new String[allowedSwings.length];
		newPart.world = world;
		for(int i = 0; i < allowedSwings.length; i++)newPart.allowedSwings[i] = allowedSwings[i];
		newPart.bannedSwings = new String[bannedSwings.length];
		for(int i = 0; i < bannedSwings.length; i++)newPart.bannedSwings[i] = bannedSwings[i];
		return newPart;
	}
	
	public void begin(Weapon weapon){}; // called when the part is added to the weapon used to add passiveEffects to the weapon
	
	public ArrayList<Effect> getEffects(){ //get the effects of this part
		return new ArrayList<Effect>();
	};
	
	public abstract void setStats(float level);
	
	
	//does fuck all. VERY IMPORTANT. (actually though: if you delete it the game will be kill)
	public static final void doFuckAll(){
		
	}

}
