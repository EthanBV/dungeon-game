package com.dungeon.game.item.weapon;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.dungeon.game.effect.Effect;
import com.dungeon.game.effect.Stun;
import com.dungeon.game.entity.character.Character;
import com.dungeon.game.entity.weapon.MeleeGraphic;
import com.dungeon.game.item.weapon.swing.Rest;
import com.dungeon.game.item.weapon.swing.Swing;
import com.dungeon.game.item.weapon.swing.SwingSet;
import com.dungeon.game.spritesheet.Spritesheet;
import com.dungeon.game.world.World;

public class Sword extends Melee {
	
	private static final Texture[] BLADES = Spritesheet.getSprites("swordBladeMap.png", 32, 32);
	private static final Texture[] GUARDS = Spritesheet.getSprites("swordGuardMap.png", 32, 32);
	private static final Texture[] HILTS = Spritesheet.getSprites("swordHiltMap.png", 32, 32); //why is guard spelled ua and not au??
	private static final int BLADE_NUM = 5;
	private static final int GUARD_NUM = 2;
	private static final int HILT_NUM = 2;
	// how should we change name and stats based on the blade guard and hilt? Arrays? Text Files? Something Else? I'll do this for now
	private static final String[] BLADE_NAMES = new String[]{"Sword", "Light Sword", "Broad Sword", "Cutlass", "Needle"};
	//blade stat order is dmg, speed, knockback
	private static final float[][] BLADE_STATS = new float[][]{
		new float[]{1,1,1}, 
		new float[]{0.8f,1.3f,0.6f},
		new float[]{1.2f,0.7f,1.3f},
		new float[]{1.5f,0.9f,0.4f},
		new float[]{0.6f,2f,0.3f},
		};

	private final int REST = 0;
	private final int WINDUP1 = 1;
	private final int WINDUP2 = 2;
	private final int WINDUP3 = 3;
	private final int SWING1 = 4;
	private final int SWING2 = 5;
	private final int SWING3 = 6;
	private final int WINDDOWN1 = 7;
	private final int WINDDOWN2 = 8;
	private final int WINDDOWN3 = 9;
	
	private final float REST_DIST = 14;
	private final float REST_PANG = 82;
	private final float REST_ANGL = -10;
	
	private final float WINDUP1_TIME = 10;
	private final float WINDUP1_DIST = 24;
	private final float WINDUP1_PANG = 70;
	private final float WINDUP1_ANGL = 35;
	
	private final float SWING1_TIME = 8;
	private final float SWING1_DIST = 14;
	private final float SWING1_PANG = -55;
	private final float SWING1_ANGL = -50;
	private final float SWING1_CDWN = 30;
	private final float SWING1_STAM = 10;
	
	private final float WINDDOWN1_TIME = 20;
	
	private final float WINDUP2_TIME = 10;
	private final float WINDUP2_DIST = 16;
	private final float WINDUP2_PANG = -75;
	private final float WINDUP2_ANGL = -80;
	
	private final float SWING2_TIME = 10;
	private final float SWING2_DIST = 26;
	private final float SWING2_PANG = 80;
	private final float SWING2_ANGL = 45;
	private final float SWING2_CDWN = 30;
	private final float SWING2_STAM = 15;
	
	private final float WINDDOWN2_TIME = 14;
	
	private final float WINDUP3_TIME = 15;
	private final float WINDUP3_DIST = 12;
	private final float WINDUP3_PANG = 30;
	private final float WINDUP3_ANGL = -7;
	
	private final float SWING3_TIME = 4;
	private final float SWING3_DIST = 28;
	private final float SWING3_PANG = 6;
	private final float SWING3_ANGL = -3;
	private final float SWING3_CDWN = 20;
	private final float SWING3_STAM = 7;

	private final float WINDDOWN3_TIME = 14;
	
	protected float[] dmgMult;
	protected float[] knockMult;
	
	
	public Sword(World world, float damage, float speed) {
		super(world, "sword.png");
		
		//generate the sprite, for now random, but in the future will be a parameter!
		int blade = (int) (Math.random()*BLADE_NUM);
		int guard = (int) (Math.random()*GUARD_NUM);
		int hilt = (int) (Math.random()*HILT_NUM);
		if(!BLADES[blade].getTextureData().isPrepared()) BLADES[blade].getTextureData().prepare();
		Pixmap bladeMap = BLADES[blade].getTextureData().consumePixmap();
		if(!GUARDS[guard].getTextureData().isPrepared()) GUARDS[guard].getTextureData().prepare();
		Pixmap guardMap = GUARDS[guard].getTextureData().consumePixmap();
		if(!HILTS[hilt].getTextureData().isPrepared()) HILTS[hilt].getTextureData().prepare();
		Pixmap hiltMap = HILTS[hilt].getTextureData().consumePixmap();

		bladeMap.drawPixmap(hiltMap, 0, 0);
		bladeMap.drawPixmap(guardMap, 0, 0);
		sprite = new Texture(bladeMap);
		//for some reason all the pixmaps are all disposed already... thx libgdx!
		
		
		name = BLADE_NAMES[blade];
		
		hasHit = false;
		
		
		this.damage = damage * BLADE_STATS[blade][0];
		this.speed = speed * BLADE_STATS[blade][1];
		
		knockratio = 0.4f;
		knockstr = 10 * BLADE_STATS[blade][2];		
		
		desc = "An incredibly reliable melee weapon.\n\n Damage: "+ Math.floor(this.damage*10)/10f + "\n Speed: "+ Math.floor(this.speed*10)/10f + "\n Knockback: "+ Math.floor(this.knockstr*10)/10f;

		
		dmgMult = new float[]{0.7f,1,1.5f};
		knockMult = new float[]{1,1.3f,0.7f};		
		graphic = new MeleeGraphic(world, this, new Polygon(new float[]{24,6,26,8,2,32,0,32,0,30}), 30, 2);
		
		swings = new SwingSet(world, this, new Swing[]{new Rest(world, 20, 14, 82, -10),
				new Swing(world, false, 10, 24, 70, 35, 8, 14, -55, -50), 
				new Swing(world, false, 10, 16, -75, -80, 10, 20, 80, 45),
				new Swing(world, false, 15, 12, 30, -7, 4, 28, 6, -3),
				});
		
		distance=0;
		polarAngle= 0;
		angle=0;
		
		effects = new ArrayList<Effect>();
		this.
		effects.add(new Stun(world, 30));
	}
	
	public float[] getPos(boolean mousedown, boolean mousepressed){
//		stageTimer++;
//		float speedConst=speed/10;
//		int index = (int) (stageTimer*speedConst);
//		switch(stage){
//		case REST:
//			if(Math.abs(distance-REST_DIST)>2)distance -= (distance-REST_DIST)/10;
//			if(Math.abs(polarAngle-REST_PANG)>2)polarAngle -= (polarAngle-REST_PANG)/10;
//			if(Math.abs(angle-REST_ANGL)>2)angle -= (angle-REST_ANGL)/10;
//			
//			if(mousedown && owner.use_stam(SWING1_STAM)){
//				stage=WINDUP1;
//				stageTimer = 0;
//			}
//			break;
//			
//		case WINDUP1:
//			if(distance<WINDUP1_DIST)distance-=speedConst*(REST_DIST-WINDUP1_DIST)/WINDUP1_TIME;
//			if(polarAngle>WINDUP1_PANG)polarAngle-=speedConst*(REST_PANG-WINDUP1_PANG)/WINDUP1_TIME;
//			if(angle<WINDUP1_ANGL)angle-=speedConst*(REST_ANGL-WINDUP1_ANGL)/WINDUP1_TIME;
//			
//			if(index>WINDUP1_TIME && !mousedown){
//				stageTimer = 0;
//				stage = SWING1;
//				if(hasHit) hasHit = false;
//			}
//			break;
//		case SWING1:
//			if(distance>SWING1_DIST)distance-=speedConst*(WINDUP1_DIST-SWING1_DIST)/SWING1_TIME;
//			if(polarAngle>SWING1_PANG)polarAngle-=speedConst*(WINDUP1_PANG-SWING1_PANG)/SWING1_TIME;
//			if(angle>SWING1_ANGL)angle-=speedConst*(WINDUP1_ANGL-SWING1_ANGL)/SWING1_TIME;
//			if(index>SWING1_TIME && !hasHit){
//				hasHit = true;
//			}
//			if(index>SWING1_TIME && mousedown && owner.use_stam(SWING2_STAM)){
//				stageTimer = 0;
//				stage = WINDUP2;
//			}
//			if(index>SWING1_CDWN*speedConst + SWING1_TIME ){
//				stageTimer = 0;
//				stage = WINDDOWN1;
//			}
//			break;
//		case WINDDOWN1:
//			if(distance>REST_DIST)distance-=speedConst*(SWING1_DIST-REST_DIST)/WINDDOWN1_TIME;
//			if(polarAngle<REST_PANG)polarAngle-=speedConst*(SWING1_PANG-REST_PANG)/WINDDOWN1_TIME;
//			if(angle<REST_ANGL)angle-=speedConst*(SWING1_ANGL-REST_ANGL)/WINDDOWN1_TIME;
//			if(index>WINDDOWN1_TIME){
//				stageTimer = 0;
//				stage = REST;
//			}
//			break;
//		case WINDUP2:
//			if(distance<WINDUP2_DIST)distance-=speedConst*(SWING1_DIST-WINDUP2_DIST)/WINDUP2_TIME;
//			if(polarAngle>WINDUP2_PANG)polarAngle-=speedConst*(SWING1_PANG-WINDUP2_PANG)/WINDUP2_TIME;
//			if(angle>WINDUP2_ANGL)angle-=speedConst*(SWING1_ANGL-WINDUP2_ANGL)/WINDUP2_TIME;
//			if(index>WINDUP2_TIME && !mousedown){
//				stageTimer = 0;
//				stage = SWING2;
//				if(hasHit) hasHit = false;
//			}
//			break;
//		case SWING2:
//			if(distance<SWING2_DIST)distance-=speedConst*(WINDUP2_DIST-SWING2_DIST)/SWING2_TIME;
//			if(polarAngle<SWING2_PANG)polarAngle-=speedConst*(WINDUP2_PANG-SWING2_PANG)/SWING2_TIME;
//			if(angle<SWING2_ANGL)angle-=speedConst*(WINDUP2_ANGL-SWING2_ANGL)/SWING2_TIME;
//			if(index>SWING1_TIME && !hasHit){
//				hasHit = true;
//			}
//			if(index>SWING2_TIME && mousedown && owner.use_stam(SWING3_STAM)){
//				stageTimer = 0;
//				stage = WINDUP3;
//			}
//			if(index>SWING2_CDWN*speedConst + SWING2_TIME){
//				stageTimer = 0;
//				stage = WINDDOWN2;
//			}
//			break;
//		case WINDDOWN2:
//			if(distance>REST_DIST)distance-=speedConst*(SWING2_DIST-REST_DIST)/WINDDOWN2_TIME;
//			if(polarAngle<REST_PANG)polarAngle-=speedConst*(SWING2_PANG-REST_PANG)/WINDDOWN2_TIME;
//			if(angle>REST_ANGL)angle-=speedConst*(SWING2_ANGL-REST_ANGL)/WINDDOWN2_TIME;
//			if(index>WINDDOWN2_TIME){
//				stageTimer = 0;
//				stage = REST;
//			}
//			break;
//		case WINDUP3:
//			if(distance>WINDUP3_DIST)distance-=speedConst*(SWING2_DIST-WINDUP3_DIST)/WINDUP3_TIME;
//			if(polarAngle>WINDUP3_PANG)polarAngle-=speedConst*(SWING2_PANG-WINDUP3_PANG)/WINDUP3_TIME;
//			if(angle>WINDUP3_ANGL)angle-=speedConst*(SWING2_ANGL-WINDUP3_ANGL)/WINDUP3_TIME;
//			if(index>WINDUP3_TIME && !mousedown){
//				stageTimer = 0;
//				stage = SWING3;
//				if(hasHit) hasHit = false;
//			}
//			break;
//		case SWING3:
//			if(distance<SWING3_DIST)distance-=speedConst*(WINDUP3_DIST-SWING3_DIST)/SWING3_TIME;
//			if(polarAngle>SWING3_PANG)polarAngle-=speedConst*(WINDUP3_PANG-SWING3_PANG)/SWING3_TIME;
//			if(angle<SWING3_ANGL)angle-=speedConst*(WINDUP3_ANGL-SWING3_ANGL)/SWING3_TIME;
//			if(index>SWING3_TIME && !hasHit){
//				hasHit = true;
//			}
//			if(index>SWING3_CDWN*speedConst + SWING3_TIME){
//				stageTimer = 0;
//				stage = WINDDOWN3;
//			}
//			break;
//		case WINDDOWN3:
//			if(distance>REST_DIST)distance-=speedConst*(SWING3_DIST-REST_DIST)/WINDDOWN3_TIME;
//			if(polarAngle<REST_PANG)polarAngle-=speedConst*(SWING3_PANG-REST_PANG)/WINDDOWN3_TIME;
//			if(angle>REST_ANGL)angle-=speedConst*(SWING3_ANGL-REST_ANGL)/WINDDOWN3_TIME;
//			if(index>WINDDOWN3_TIME){
//				stageTimer = 0;
//				stage = REST;
//			}
//			break;
//		}
//		
//		if(!leftSide) return new float[]{distance,polarAngle*-1,angle*-1};
		
		swings.progressWeapon();
		
		return new float[]{distance,polarAngle,angle};
	}
	
	

	@Override
	public boolean isInUse() {
		return stage == WINDUP1||stage == WINDUP2||stage == WINDUP3||stage == SWING1||stage == SWING2||stage == SWING3||stage == WINDDOWN1||stage == WINDDOWN2||stage == WINDDOWN3;
	}

	public boolean inAttack() {
		return !hasHit && (stage == SWING1 || stage == SWING2 || stage == SWING3);
	}
	
	public void hit(Character e) {
		
		if(!e.knownEntities.contains(owner))e.knownEntities.add(owner);
		hasHit = true;
		float weaponangle = graphic.angle+135;
		float angleModifier;
		float cur_dmgMult = 0;
		float cur_knockMult = 0;
		
		if(stage == SWING1){
			angleModifier = -90;
			cur_dmgMult = dmgMult[0];
			cur_knockMult = knockMult[0];
		}
		else if(stage == SWING2){
			angleModifier = 90;
			cur_dmgMult = dmgMult[1];
			cur_knockMult = knockMult[1];
		}
		else{
			angleModifier = 0;
			cur_dmgMult = dmgMult[2];
			cur_knockMult = knockMult[2];
		}
		if(e.damage(damage*cur_dmgMult, getEffects())>0){
			
			float xSword = (float) (Math.cos((weaponangle+angleModifier)/180f*Math.PI)*knockstr);
			float ySword = (float) (Math.sin((weaponangle+angleModifier)/180f*Math.PI)*knockstr);
			float xOwner = (float) (Math.cos((weaponangle)/180*Math.PI)*knockstr);
			float yOwner = (float) (Math.sin((weaponangle)/180*Math.PI)*knockstr);
			Vector2 knockVec = new Vector2();
			knockVec.x = (xSword*(1-knockratio)+xOwner*(knockratio))*cur_knockMult;
			knockVec.y = (ySword*(1-knockratio)+yOwner*(knockratio))*cur_knockMult;
			e.acel(knockVec, false);
		}
		//temp solution to effects issue
	}
	
	public String getDesc() {
		return "The accepted standard for all melee weapons, and the standard for all sword class melee weapons. Using it will swing it in a short combo of swings, damaging the first target it comes in contact with. For this particular sword, the combo begins with a fronthand swing across the body, followed by a backhand swing, and ends with a jab.\n\n\"My sword shall lead me to glory!\" -final words of Tanturin, fabled warrior\n\n Damage: "+ Math.floor(damage*10)/10f  + "\n Speed: "+ Math.floor(this.speed*10)/10f + "\n Knockback: "+ Math.floor(this.knockstr*10)/10f;
	}

	@Override
	public void reset() {
		stage = REST;
		stageTimer = 0;
		
		distance = REST_DIST;
		polarAngle = REST_PANG;
		angle = REST_ANGL;
	}

	@Override
	public ArrayList<Effect> getEffects() {
		ArrayList<Effect> effects = new ArrayList<Effect>();
		effects.add(new Stun(world, 30));
		return effects;
	}
}
