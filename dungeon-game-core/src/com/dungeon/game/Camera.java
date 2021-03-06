package com.dungeon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dungeon.game.entity.hud.Hud;
import com.dungeon.game.world.Tile;
import com.dungeon.game.world.World;
import com.dungeon.game.entity.character.Character;

public class Camera {
	public OrthographicCamera cam;
	
	public OrthographicCamera lightCam;
	
	public Viewport view;
	
	private final float TWEEN = 0.3f;
	
	public float x;
	public float y;
	
	public int width = 640;
	public int height = 360;
	
	public World world;
	
	public Camera(World world){
		this.x = 0;
		this.y = 0;
		
		this.world = world;

		cam = new OrthographicCamera(width, height);
		cam.position.set(this.x+width/2, this.y+height/2, 0);
		lightCam = new OrthographicCamera(width, height);
		lightCam.position.set(this.x+width/2, this.y+height/2, 0);
		lightCam.zoom = 1f/Tile.PPM;
		
		view = new FitViewport(width, height, cam);
		view.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public void update(){
		if(world.debug_freeCam){
			x += ((world.mouse.x - width / 2));
			y += ((world.mouse.y - height / 2));
			world.mouse.x = width / 2;
			world.mouse.y = height / 2;
		}else if(world.player.actionState[2] && !(world.player.focusedEntity instanceof Character)) {
			x += (world.player.x - x) * TWEEN;
			y += (world.player.y - y) * TWEEN;
		}
		else if(Hud.class.isInstance(world.player.focusedEntity)) {
			x += ((4*world.player.x+(world.player.focusedEntity.x-width/2)+x)/5 - x)*TWEEN;
			y += ((4*world.player.y+(world.player.focusedEntity.y-height/2)+y)/5 - y)*TWEEN;
		}
		else if(world.player.focusedEntity != null) {
			x += ((4*world.player.x+world.player.focusedEntity.x)/5 - x)*TWEEN;
			y += ((4*world.player.y+world.player.focusedEntity.y)/5 - y)*TWEEN;
		}
		cam.position.set(x, y, 0);
		lightCam.position.set(x/Tile.PPM, y/Tile.PPM, 0);
		cam.update();
		lightCam.update();
	}
}