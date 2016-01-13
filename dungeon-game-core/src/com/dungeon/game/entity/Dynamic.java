package com.dungeon.game.entity;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.dungeon.game.world.*;

//abstract class for dynamic entities, or entities that move and respond to physics.
public abstract class Dynamic extends Entity {
	public float dx;
	public float dy;
	
	double acel;
	double fric;
	double mvel;
	double torq;
	
//	public boolean inp_up;
//	public boolean inp_dn;
//	public boolean inp_lt;
//	public boolean inp_rt;
	
	public double move_angle;
	
	public float target_angle;
	
	public float maxLife;
	public float maxStam;
	public float maxMana;
	
	public float life;
	public float stam;
	public float mana;
	
	public int immunityTimer;
	public int immunityTime;
	public boolean immune;
	public boolean immortal;
	
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
	
	public int stunTimer;
	public boolean stun;
	
	public Dynamic(int x, int y) {
		super(x, y);
		
		solid = true;
		
		immune = true;
		immortal = false;
		
		immunityTime = 10;
	}

	//entity update function; called on every frame; before the draw phase.
	@Override
	public void update(World world) {
		norm();
		calc(world);
		phys(world);
		
	}
	
	//resets some variables at the start of every update cycles
	public void norm() {
		move_angle = 361;
	}
	
	//calculates velocity and collisions for object
	public void phys(World world) {
		if(stunTimer > 0) stunTimer--;
		else if(stun) stun = false;
		
		if(immunityTimer > 0) immunityTimer--;
		else if(!immortal && immune && immunityTimer == 0) immune = false;
		
		if(angle != target_angle) {
			boolean turnRight = true;
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
		
//		double dirX = 0;
//		double dirY = 0;
		
		double vel = Math.sqrt(dx * dx + dy * dy);
		
		if(!stun && move_angle != 361) {
			if(vel < mvel) {
				dx += Math.cos(move_angle*Math.PI/180)*acel;
				dy += Math.sin(move_angle*Math.PI/180)*acel;
			}
		}
		if(dx != 0 || dy != 0){
			vel = Math.sqrt(dx * dx + dy * dy);
			
			if(vel < fric) {
				dx = 0;
				dy = 0;
			}
			
			dx -= dx/vel*fric;
			dy -= dy/vel*fric;
		}
		
		x += dx;
		y += dy;
		//Yayyyyy collision code all works, now just need to prevent rotation in the event that it would cause a collision!
		Rectangle bBox = getBoundingBox();
		Polygon hBox = getHitbox();
		int center_x = ((int) (x))/Tile.TS;
		int center_y = ((int) (y))/Tile.TS;
		int tile_lt = ((int) (bBox.x))/Tile.TS;
		int tile_rt = ((int) (bBox.x+bBox.width))/Tile.TS;
		int tile_dn = ((int) (bBox.y))/Tile.TS;
		int tile_up = ((int) (bBox.y+bBox.height))/Tile.TS;
		float xChange = 0;
		float yChange = 0;
		boolean collide_lt = false;
		boolean collide_rt = false;
		boolean collide_dn = false;
		boolean collide_up = false;
		for(int i = tile_lt; i <= tile_rt; i++) {
			for(int k = tile_dn; k <= tile_up; k++) {
				if(world.curFloor.tm[k][i].data == 1) {
					Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
					Polygon tile_hBox = new Polygon(new float[] {i*Tile.TS,k*Tile.TS,(i+1)*Tile.TS,k*Tile.TS,(i+1)*Tile.TS,(k+1)*Tile.TS,i*Tile.TS,(k+1)*Tile.TS});
					
					if(Intersector.overlapConvexPolygons(hBox, tile_hBox, mtv)) {
						if(center_x>i){
							if(center_y>k){ //Tile is to the bottom left
								if(world.curFloor.tm[k+1][i].data==1){
									collide_lt = true;
									xChange = Math.max(xChange, mtv.depth);
									if(world.curFloor.tm[k][i+1].data==1){
										collide_dn = true;
										yChange = Math.max(yChange, mtv.depth);
									}
								}else if(world.curFloor.tm[k][i+1].data==1){
									collide_dn = true;
									yChange = Math.max(yChange, mtv.depth);
								}else{
									if(Math.abs(mtv.normal.y)>Math.abs(mtv.normal.x)){
										collide_dn = true;
										yChange = Math.max(yChange, mtv.depth);
									}
									else {
										collide_lt = true;
										xChange = Math.max(xChange, mtv.depth);
									}
								}
							}else if(center_y<k){ //Tile is to the top left
								if(world.curFloor.tm[k-1][i].data==1){
									collide_lt = true;
									xChange = Math.max(xChange, mtv.depth);
									if(world.curFloor.tm[k][i+1].data==1){
										collide_up = true;
										yChange = Math.max(yChange, mtv.depth);
									}
								}else if(world.curFloor.tm[k][i+1].data==1){
									collide_up = true;
									yChange = Math.max(yChange, mtv.depth);
								}else{
									if(Math.abs(mtv.normal.y)>Math.abs(mtv.normal.x)){
										collide_up = true;
										yChange = Math.max(yChange, mtv.depth);
									}
									else{
										collide_lt = true;
										xChange = Math.max(xChange, mtv.depth);
									}
								}
							}else{ //Tile is to the left
								collide_lt = true;
								xChange = Math.max(xChange, mtv.depth);
							}
						}else if(center_x<i){
							if(center_y>k){ //Tile is the the bottom right
								if(world.curFloor.tm[k+1][i].data==1){
									collide_rt = true;
									xChange = Math.max(xChange, mtv.depth);
									if(world.curFloor.tm[k][i-1].data==1){
										collide_dn = true;
										yChange = Math.max(yChange, mtv.depth);
									}
								}else if(world.curFloor.tm[k][i-1].data==1){
									collide_dn = true;
									yChange = Math.max(yChange, mtv.depth);
								}else{
									if(Math.abs(mtv.normal.y)>Math.abs(mtv.normal.x)){
										collide_dn = true;
										yChange = Math.max(yChange, mtv.depth);
									}
									else{
										collide_rt = true;
										xChange = Math.max(xChange, mtv.depth);
									}
								}
							}else if(center_y<k){ //Tile is to the top right
								if(world.curFloor.tm[k-1][i].data==1){
									collide_rt = true;
									xChange = Math.max(xChange, mtv.depth);
									if(world.curFloor.tm[k][i-1].data==1){
										collide_up = true;
										yChange = Math.max(yChange, mtv.depth);
									}
								}else if(world.curFloor.tm[k][i-1].data==1){
									collide_up = true;
									yChange = Math.max(yChange, mtv.depth);
								}else{
									if(Math.abs(mtv.normal.y)>Math.abs(mtv.normal.x)){
										collide_up = true;
										yChange = Math.max(yChange, mtv.depth);
									}
									else{
										collide_rt = true;
										xChange = Math.max(xChange, mtv.depth);
									}
								}
							}else{ //Tile is to the right
								collide_rt = true;
								xChange = Math.max(xChange, mtv.depth);
							}
						}else if(center_y>k){ //Tile is the bottom
							collide_dn = true;
							yChange = Math.max(yChange, mtv.depth);
						}else if(center_y<k){ //Tile is to the top
							collide_up = true;
							yChange = Math.max(yChange, mtv.depth);
						}else{ //Tile is the same as the one the entity is in
							//add something here for small entities?
						}
					}
				}
			}
		}
		if(collide_lt){
			x+=xChange;
			dx = 0;
		}
		if(collide_rt){
			x-=xChange;
			dx = 0;
		}
		if(collide_dn){
			y+=yChange;
			dy = 0;
		}
		if(collide_up){
			y-=yChange;
			dy = 0;
		}
		
//		int tile_lt = (int) (x/Tile.TS);
//		int tile_dn = (int) (y/Tile.TS);
//		int tile_rt = (int) ((x+width)/Tile.TS);
//		int tile_up = (int) ((y+height)/Tile.TS);
//		
//		boolean dl = world.curFloor.tm[tile_dn][tile_lt].data == 1;
//		boolean dr = world.curFloor.tm[tile_dn][tile_rt].data == 1;
//		boolean ul = world.curFloor.tm[tile_up][tile_lt].data == 1;
//		boolean ur = world.curFloor.tm[tile_up][tile_rt].data == 1;
//		
//		if(dl && dr) {
//			y = (tile_dn+1) * Tile.TS;-
//			dy = 0;
//			
//			dl = false;
//			dr = false;
//		}
//		if(ul && ur) {
//			y = (tile_up * Tile.TS)-height;
//			dy = 0;
//			
//			ul = false;
//			ur = false;
//		}
//		if(dl && ul) {
//			x = (tile_lt+1) * Tile.TS;
//			dx = 0;
//			
//			dl = false;
//			ul = false;
//		}
//		if(dr && ur) {
//			x = (tile_rt * Tile.TS)-width;
//			dx = 0;
//			
//			ul = false;
//			ur = false;
//		}
//		
//		if(dl) {
//			if((tile_lt+1)*Tile.TS - this.x < (tile_dn+1)*Tile.TS - this.y) {
//				x = (tile_lt+1) * Tile.TS;
//				dx = 0;
//			}
//			else {
//				y = (tile_dn+1) * Tile.TS;
//				dy = 0;
//			}
//		}
//		if(dr) {
//			if(x+width - tile_rt*Tile.TS < (tile_dn+1)*Tile.TS - this.y) {
//				x = (tile_rt * Tile.TS)-width;
//				dx = 0;
//			}
//			else {
//				y = (tile_dn+1) * Tile.TS;
//				dy = 0;
//			}
//		}
//		if(ul) {
//			if((tile_lt+1)*Tile.TS - this.x < y+height - tile_up*Tile.TS) {
//				x = (tile_lt+1) * Tile.TS;
//				dx = 0;
//			}
//			else {
//				y = (tile_up * Tile.TS)-height;
//				dy = 0;
//			}
//		}
//		if(ur) {
//			if(x+width - tile_rt*Tile.TS < y+height - tile_up*Tile.TS) {
//				x = (tile_rt * Tile.TS)-width;
//				dx = 0;
//			}
//			else {
//				y = (tile_up * Tile.TS)-height;
//				dy = 0;
//			}
//		}
//		
//		for(Entity e: world.entities){
//			if(!this.equals(e) && e.solid && e.x+e.width > x && e.x < x+width &&
//			   e.y+e.height > y && e.y < y+height) {
//				
//				int dir_x = dx < 0 ? -1:dx > 0? 1:0;
//				int dir_y = dy < 0 ? -1:dy > 0? 1:0;
//
//				if(dir_x == 1 && dir_y == 0) {
//					x = e.x - width;
//					dx = 0;
//				}
//				else if(dir_x == -1 && dir_y == 0) {
//					x = e.x + e.width;
//					dx = 0;
//				}
//				else if(dir_x == 0 && dir_y == 1) {
//					y = e.y - height;
//					dy = 0;
//				}
//				else if(dir_x == 0 && dir_y == -1) {
//					y = e.y + e.height;
//					dy = 0;
//				}
//				else if(dir_x == 1 && dir_y == 1) {
//					if(x+width - e.x < y+height - e.y) {
//						x = e.x - width;
//						dx = 0;
//					}
//					else {
//						y = e.y - height;
//						dy = 0;
//					}
//				}
//				else if(dir_x == -1 && dir_y == 1) {
//					if(e.x+e.width - x < y+height - e.y) {
//						x = e.x + e.width;
//						dx = 0;
//					}
//					else {
//						y = e.y - height;
//						dy = 0;
//					}
//				}
//				else if(dir_x == 1 && dir_y == -1) {
//					if(x+width - e.x < e.y+e.height - y) {
//						x = e.x - width;
//						dx = 0;
//					}
//					else {
//						y = e.y + e.height;
//						dy = 0;
//					}
//				}
//				else if(dir_x == -1 && dir_y == -1) {
//					if(e.x+e.width - x < e.y+e.height - y) {
//						x = e.x + e.width;
//						dx = 0;
//					}
//					else {
//						y = e.y + e.height;
//						dy = 0;
//					}
//				}
//			}
//		}
//		if(x<0||y<0||x+width>(world.curFloor.tm[0].length-1)*Tile.TS||y+height>(world.curFloor.tm.length-1)*Tile.TS)killMe = true;

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
		
		System.out.println(name + " took " + amount + " damage.");
		
		return amount;
	}
	
	public float heal(float value /*Add an array of Effects*/){
		float amount = Math.max(maxLife, life+value)-life;
		life = Math.max(maxLife, life+value);
		
		return amount;
	}
}