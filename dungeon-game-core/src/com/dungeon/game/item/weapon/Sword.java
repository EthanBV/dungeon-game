package com.dungeon.game.item.weapon;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.dungeon.game.effect.Stun;
import com.dungeon.game.entity.character.Character;
import com.dungeon.game.entity.weapon.MeleeGraphic;
import com.dungeon.game.item.weapon.parts.Part;
import com.dungeon.game.item.weapon.swing.Rest;
import com.dungeon.game.item.weapon.swing.Swing;
import com.dungeon.game.item.weapon.swing.SwingSet;
import com.dungeon.game.world.World;

public class Sword extends Melee {
	
	private final SwingSet[] BLADE_SWINGS = new SwingSet[]{
			new SwingSet(world, this, new Swing[]{new Rest(world, 20, 14, 82, -10), //sword
					new Swing(world, "Slash", false, 10, 24, 70, 35, 8, 14, -55, -50, 0.7f, 1, -90, 0.4f, 1), 
					new Swing(world, "Slash", false, 10, 16, -75, -80, 10, 20, 80, 45, 1, 1.3f, 90, 0.4f, 1),
					new Swing(world, "Stab", false, 15, 12, 30, -7, 4, 28, 6, -3, 1.5f, 0.7f, 0, 0.4f, 0.8f)
					} ,false),
			new SwingSet(world, this, new Swing[]{new Rest(world, 20, 14, 82, -10), //light sword
					new Swing(world, "Slash", false, 10, 24, 70, 35, 8, 14, -55, -50, 0.7f, 1, -90, 0.4f, 1), 
					new Swing(world, "Slash", false, 10, 16, -75, -80, 10, 20, 80, 45, 1, 1.3f, 90, 0.4f, 1),
					new Swing(world, "Stab", false, 15, 12, 30, -7, 4, 28, 6, -3, 1.5f, 0.7f, 0, 0.4f, 0.8f)
					} ,false),
			new SwingSet(world, this, new Swing[]{new Rest(world, 20, 14, 82, -10), //broad sword
					new Swing(world, "Slash", false, 10, 24, 70, 35, 8, 14, -55, -50, 0.7f, 1, -90, 0.4f, 1), 
					new Swing(world, "Slash", false, 10, 16, -75, -80, 10, 20, 80, 45, 1, 1.3f, 90, 0.4f, 1),
					new Swing(world, "Stab", false, 15, 12, 30, -7, 4, 28, 6, -3, 1.5f, 0.7f, 0, 0.4f, 0.8f)
					} ,false),
			new SwingSet(world, this, new Swing[]{new Rest(world, 20, 14, 82, -10), //cutlass
					new Swing(world, "Slash", false, 10, 24, 70, 35, 8, 14, -55, -50, 0.7f, 1, -90, 0.4f, 1), 
					new Swing(world, "Slash", false, 10, 16, -75, -80, 10, 20, 80, 45, 1, 1.3f, 90, 0.4f, 1),
					} ,true),
			new SwingSet(world, this, new Swing[]{new Rest(world, 20, 14, 82, -10), //needle
					new Swing(world, "Stab", false, 15, 12, 40, -7, 4, 28, 12, -3, 0.75f, 0.7f, 0, 0.4f, 0.5f),
					new Swing(world, "Stab", false, 15, 12, 0, 0, 4, 28, 0, 0, 1.5f, 0.7f, 0, 0.4f, 1f),
					new Swing(world, "Stab", false, 15, 12, -40, 7, 4, 28, -12, 3, 0.75f, 0.7f, 0, 0.4f, 1.5f)
					} , true)
	};
	
	private final Swing[][] GUARD_SWINGS = new Swing[][]{
		new Swing[]{},
		new Swing[]{},
		new Swing[]{new Swing(world, "Guard Hit", false, 10, 10, 20, -90, 10, 26, 15, -90, 0.5f, 1.5f, 90, 0, 2)}
	};
	
	private final Swing[][] HILT_SWINGS = new Swing[][]{
		new Swing[]{},
		new Swing[]{},
		new Swing[]{new Swing(world, "Hilt Hit", false, 10, 15, 70, 150, 10, 26, 0, 90, 2f, 0.6f, 180, 0, 2)}
	};
	
	protected float[] dmgMult;
	protected float[] knockMult;
	
	public Part blade;
	public Part guard;
	public Part hilt;
	
	public Sword(World world, float damage, float speed, float weight) {
		super(world, "sword.png");
		
		//generate the sprite, for now random, but in the future will be a parameter!
		blade = Part.SWORD_BLADES[(int) (Math.random()*Part.SWORD_BLADE_NUM)].clone();
		guard = Part.SWORD_GUARDS[(int) (Math.random()*Part.SWORD_GUARD_NUM)].clone();
		hilt = Part.SWORD_HILTS[(int) (Math.random()*Part.SWORD_HILT_NUM)].clone();
		if(!blade.sprite.getTextureData().isPrepared()) blade.sprite.getTextureData().prepare();
		Pixmap bladeMap = blade.sprite.getTextureData().consumePixmap();
		if(!guard.sprite.getTextureData().isPrepared()) guard.sprite.getTextureData().prepare();
		Pixmap guardMap = guard.sprite.getTextureData().consumePixmap();
		if(!hilt.sprite.getTextureData().isPrepared()) hilt.sprite.getTextureData().prepare();
		Pixmap hiltMap = hilt.sprite.getTextureData().consumePixmap();
		
		Pixmap spr = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
		spr.drawPixmap(bladeMap, 0, 0);
		spr.drawPixmap(hiltMap, 0, 0);
		spr.drawPixmap(guardMap, 0, 0);
		sprite = new Texture(spr);
		spr.dispose();
		//for some reason all the pixmaps are all disposed already... thx libgdx!
		
		
		name = blade.name;
		
		hasHit = false;
		
		this.damage = damage * blade.dmgMult * guard.dmgMult * hilt.dmgMult;
		this.speed = speed * blade.speedMult * guard.speedMult * hilt.speedMult;
		
		knockratio = 0.4f;
		knockstr = 10 * blade.knockMult * guard.knockMult * hilt.knockMult;	
		
		this.weight = weight * blade.weightMult * guard.weightMult * hilt.weightMult;	
		
		desc = "The most common and widely used melee weapon.\n\n Damage: "+ Math.floor(this.damage*10)/10f + "\n Speed: "+ Math.floor(this.speed*10)/10f + "\n Knockback: "+ Math.floor(this.knockstr*10)/10f;

		
		dmgMult = new float[]{0.7f,1,1.5f};
		knockMult = new float[]{1,1.3f,0.7f};	
		
		graphic = new MeleeGraphic(world, this, new Polygon(new float[]{24,6,26,8,2,32,0,32,0,30}), 30, 2);
		
		swings = BLADE_SWINGS[blade.id]; // do we have to clone it? I guess not
		for(int i = 0; i < GUARD_SWINGS[guard.id].length; i++){
			swings.addSwing(GUARD_SWINGS[guard.id][i]);
		}
		for(int i = 0; i < HILT_SWINGS[hilt.id].length; i++){
			swings.addSwing(HILT_SWINGS[hilt.id][i]);
		}
		swings.setWorld(world); //is this neccicary? (I think so)
		
		hitEffects.add(new Stun(world, 30));
	}
	
	public float[] getPos(boolean mousedown, boolean mousepressed){
		swings.progressWeapon();
		
		return new float[]{distance,polarAngle,angle};
	}
	
	

	@Override
	public boolean isInUse() {
		return swings.isInUse;
	}

	public boolean inAttack() {
		return swings.isInAttack;
	}
	
	public void hit(Character c) {
		swings.hit(c);
	}
	
	public String getDesc() {
		return "The accepted standard for melee weapons, mainly due to how common it is. Swords are generally considered very reliable, despite the diverse range of blades. This reliability is another reason for their wide acceptance.\n\n\"My sword shall lead me to glory!\" -final words of Tanturin, fabled warrior\n\n Damage: "+ Math.floor(damage*10)/10f  + "\n Speed: "+ Math.floor(this.speed*10)/10f + "\n Knockback: "+ Math.floor(this.knockstr*10)/10f;
	}

	@Override
	public void reset() {
		swings.reset();
	}
}
