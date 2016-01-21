package com.dungeon.game.entity;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.dungeon.game.item.Inventory;
import com.dungeon.game.item.Weapon;
import com.dungeon.game.world.Tile;
import com.dungeon.game.world.World;

public abstract class Character extends Dynamic {
	public float move_angle;
	public float target_angle;
	
	public float maxLife;
	public float maxStam;
	public float maxMana;
	
	public float life;
	public float stam;
	public float mana;
	
	public float stamRegen;
	public float manaRegen;
	
	public int immunityTimer;
	public int immunityTime;
	public boolean immune;
	public boolean immortal;

	public int stunTimer;
	public boolean stun;
	
	public boolean fight_mode;
	
	public float physc_resist;
	public float arcan_resist;
	public float flame_resist;
	public float ligtn_resist;
	public float poisn_resist;
	
	public float base_physc_resist;
	public float base_arcan_resist;
	public float base_flame_resist;
	public float base_ligtn_resist;
	public float base_poisn_resist;
	
	protected float vision;
	
	public ArrayList<Entity> knownEntities;
		
	public Inventory inv;
	
	public Character(int x, int y) {
		super(x, y);
		
		immune = true;
		immortal = false;
		
		immunityTime = 10;
		
		vision = 0;
		
		knownEntities = new ArrayList<Entity>();
	}

	public void norm() {
		move_angle = 361;
	}
	
	public void update(World world) {
		norm();
		calc(world);
		move(world);
		phys(world);
		post(world);
		stam_regen();
		mana_regen();
		sight(world);
	}
	
	public void move(World world) {
		if(stunTimer > 0) stunTimer--;
		else if(stun) stun = false;
		
		if(immunityTimer > 0) immunityTimer--;
		else if(!immortal && immune && immunityTimer == 0) immune = false;
		
		Vector2 acelVec = new Vector2();
		acelVec.x = (float) (Math.cos(move_angle*Math.PI/180)*acel);
		acelVec.y = (float) (Math.sin(move_angle*Math.PI/180)*acel);
		if(!stun && move_angle != 361)acel( acelVec, true );
		
		boolean turnRight = true;
		float originalAngle = angle;
		if(angle != target_angle) {
			float tempAngle = angle+180;
			float tempTargetAngle = target_angle+180;
			
			if(tempAngle>180&&tempTargetAngle>180){
				if(tempTargetAngle<tempAngle)turnRight = false;
				else turnRight = true;
			}else if(tempAngle<180&&tempTargetAngle<180){
				if(tempAngle>tempTargetAngle)turnRight = false;
				else turnRight = true;
			}else if(tempAngle>180&&tempTargetAngle<180){
				if(tempTargetAngle<tempAngle-180)turnRight = true;
				else turnRight = false;
			}else if(tempAngle<180&&tempTargetAngle>180){
				if(tempTargetAngle-180>tempAngle)turnRight = false;
				else turnRight = true;
			}
			
			if(turnRight)angle+=torq;
			else angle-=torq;
			
			float difference = 0;
			float angleModifier1 = 0;
			float angleModifier2 = 0;
			
			if(tempAngle > tempTargetAngle){
				angleModifier1 = tempAngle;
				angleModifier2 = tempTargetAngle;
			}
			else {
				if(tempAngle == tempTargetAngle)difference = 0;
				else {
					angleModifier1 = tempTargetAngle;
					angleModifier2 = tempAngle;
				}
			}
			
			if(angleModifier1-180<angleModifier2){
				difference = angleModifier1-angleModifier2;
			}
			else {
				difference = angleModifier2+Math.abs(angleModifier1-360);
			}
			
			if(difference < torq) angle = target_angle;
				
			if(angle > 180) angle -= 360;
			if(angle < -180) angle += 360;
		}
	}
	
	public void sight(World world){
		for(Entity e: world.entities){
			if(!knownEntities.contains(e)){
				float dist = (float) Math.sqrt(Math.abs(x-e.x)*Math.abs(x-e.x)+Math.abs(y-e.y)*Math.abs(y-e.y));
				if(dist < vision*Tile.TS)knownEntities.add(e);
			}
		}
	}
	
	public float damage(float value /*Add an array of Effects*/){
		if(immune) return 0;
		
		float amount = life - Math.max(life-value,0);
		life-=value;
		
		if(life <= 0) killMe = true;
		
		immunityTimer = immunityTime;
		immune = true;
		
		stunTimer = 20;
		stun = true;
		
		System.out.println(name + " took " + amount + " damage" + (life<=0? " and was killed.":"."));
		
		return amount;
	}
	
	public float heal(float value /*Add an array of Effects*/){
		float amount = Math.min(maxLife, life+value)-life;
		life = Math.min(maxLife, life+value);
		
		System.out.println(name + " gained " + amount + " life.");
		
		return amount;
	}
	
	public boolean consume_stam(float value) {
		if(stam >= value) {
			stam -= value;
			return true;
		}
		
		return false;
	}
	
	public boolean consume_mana(float value) {
		if(mana >= value) {
			mana -= value;
			return true;
		}
		
		return false;
	}
	
	public void stam_regen() {
		if(stam<maxStam)stam = (float) Math.min(stam+stamRegen,maxStam);
	}
	
	public void mana_regen() {
		if(mana<maxMana)mana = (float) Math.min(mana+manaRegen,maxMana);
	}
	
	public void equip(World world, Weapon weapon) {
		weapon.equip(world, this);
	}
	
	public void unequip(World world, Weapon weapon) {
		weapon.unequip(world, this);
	}
}
