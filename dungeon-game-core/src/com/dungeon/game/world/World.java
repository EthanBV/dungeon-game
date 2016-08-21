package com.dungeon.game.world;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.dungeon.game.Camera;
import com.dungeon.game.ai.CoverFinder;
import com.dungeon.game.entity.Entity;
import com.dungeon.game.entity.character.Character;
import com.dungeon.game.entity.character.Player;
import com.dungeon.game.entity.character.enemy.Enemy;
import com.dungeon.game.entity.character.enemy.Goon;
import com.dungeon.game.entity.character.friend.Friend;
import com.dungeon.game.entity.character.friend.Mentor;
import com.dungeon.game.entity.furniture.TrainingTable;
import com.dungeon.game.entity.hud.DescBox;
import com.dungeon.game.entity.hud.EffectHudBackground;
import com.dungeon.game.entity.hud.Hud;
import com.dungeon.game.entity.hud.HudBackground;
import com.dungeon.game.entity.hud.Mouse;
import com.dungeon.game.item.equipable.weapon.part.Part;
//import com.dungeon.game.light.LightMap;
import com.dungeon.game.pathing.AreaMap;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class World {
	public SpriteBatch hudBatch;
	public ShapeRenderer shapeRenderer;
	
	public Camera cam;
	public Camera hudCam;
	
	public ArrayList<Dungeon> dungeons;
	
	public Dungeon curDungeon;
	public Floor curFloor;
	
	public ArrayList<Entity> entities;
	public ArrayList<Hud> hudEntities;
	
	public Player player;
	public Mouse mouse;
	
	public DescBox descBox;
	
	public AreaMap areaMap;
	
	private BitmapFont fpsFont;
	
	boolean debug_hitbox;
	boolean debug_pathing;
	boolean debug_frames;
	boolean debug_sight;
	boolean debug_light;
	
	private com.badlogic.gdx.physics.box2d.World emptyWorld;
	private PointLight emptyWorldLight;
	
	public RayHandler rayHandler;
	
	private ArrayList<Entity> drawEnts;
	
	public World() {
		Part.doFuckAll(); //Never Forget
		
		rayHandler = new RayHandler(null);
		rayHandler.setBlurNum(15);
		rayHandler.setAmbientLight(new Color(0,0,0,0));
		RayHandler.useDiffuseLight(true);
		hudBatch = new SpriteBatch();
		
		cam = new Camera(this);
		hudCam = new Camera(this);
		
		player = new Player(this, 0, 0);
		
		shapeRenderer = new ShapeRenderer();
		
		dungeons = new ArrayList<Dungeon>();
		
		dungeons.add(new Dungeon(this));
		
		curDungeon = dungeons.get(0);
		curFloor = curDungeon.floors.get(0);
		
		player.x = curFloor.tm[0].length/2*Tile.TS-Tile.TS/2;
		player.y = curFloor.tm.length/2*Tile.TS-Tile.TS/2;
		
		areaMap = curFloor.areaMap;
		
		entities = new ArrayList<Entity>();
		hudEntities = new ArrayList<Hud>();
		
		mouse = new Mouse(this, 0, 0);
		descBox = new DescBox(this);
		
		entities = curFloor.entities;
		entities.add(0,player);
		entities.add(new Mentor(this, curFloor.tm[0].length/2*Tile.TS-Tile.TS/2+Tile.TS, curFloor.tm.length/2*Tile.TS-Tile.TS/2));
		entities.add(new TrainingTable(this, curFloor.tm[0].length/2*Tile.TS-Tile.TS/2-Tile.TS, curFloor.tm.length/2*Tile.TS+Tile.TS/2));
		hudEntities.add(new HudBackground(this));
		
		hudEntities.add(new EffectHudBackground(this, cam.width-44, cam.height-44));
		
		fpsFont = new BitmapFont(Gdx.files.internal("main_text.fnt"));
		fpsFont.setColor(Color.RED);
		
		emptyWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0,0), true);
		emptyWorldLight = new PointLight(rayHandler, 10, Color.WHITE, Integer.MAX_VALUE, 0, 0);
		emptyWorldLight.remove(false);
		
		debug_hitbox = false;
		debug_pathing = false;
		debug_frames = false; 
		debug_sight = false;
		debug_light = false;
		rayHandler.setWorld(curFloor.box2dWorld);
		
		for(Entity e: entities){
			if(e.light != null)e.light.load();
		}
//		CoverFinder.findCover(this, player, entities.get(entities.size() - 4), new int[4], new float[4], true, 10);
		
		drawEnts = new ArrayList<Entity>();
	}
	
	public void update() {
		cam.update();
		mouse.update();
		descBox.update();
//		class layerComparator implements Comparator {
//
//			@Override
//			public int compare(Object arg0, Object arg1) {
//				return 0;
//			}
//			
//		}
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
		
		for(int i = 0; i < entities.size(); i++) {
			if(entities.get(i).killMe) {
				if(entities.get(i) instanceof Character)((Character)entities.get(i)).endEffects();
				entities.get(i).dead();
				entities.remove(i);
				i--;
			}
		}
		
		for(int i = hudEntities.size()-1;i>=0;i--) {
			hudEntities.get(i).update();
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.F9)) debug_frames = !debug_frames;
		if(Gdx.input.isKeyJustPressed(Input.Keys.F8)) debug_pathing = !debug_pathing;
		if(Gdx.input.isKeyJustPressed(Input.Keys.F7)) debug_hitbox = !debug_hitbox;
		if(Gdx.input.isKeyJustPressed(Input.Keys.F10)) debug_sight = !debug_sight;
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
			debug_light = !debug_light;
			if(debug_light) {
				rayHandler.setWorld(emptyWorld);
				emptyWorldLight.add(rayHandler);
			}
			else {
				rayHandler.setWorld(curFloor.box2dWorld);
				emptyWorldLight.remove(false);
			}
		}
	}
	
	public void draw(SpriteBatch batch) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		batch.setProjectionMatrix(cam.cam.combined);
		
		curFloor.draw(batch, this);
		
		drawEnts.clear();
		for(Entity e: entities) if(e.x+e.d_width > cam.x-cam.width/2 && e.y+e.d_height > cam.y-cam.height/2 && e.x < cam.x+cam.width/2 && e.y < cam.y+cam.height/2)drawEnts.add(e);

		drawEnts.sort(new Comparator<Entity>(){

			@Override
			public int compare(Entity o1, Entity o2) {
				return (""+o2.layer).compareTo(""+o1.layer);
			}
			
		});
		
		for(int i = drawEnts.size()-1; i >= 0; i--) {
			drawEnts.get(i).draw(batch);
		}
//		drawEnts.get(drawEnts.size()-1).draw(batch);//have to draw player twice when using lights
		

		rayHandler.setCombinedMatrix(cam.cam);
		rayHandler.updateAndRender();
		
		batch.end();
		
		if(debug_pathing||debug_sight||debug_hitbox) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setAutoShapeType(true);
			
			shapeRenderer.setProjectionMatrix(cam.cam.combined);
			
			for(Entity e: entities){
				if(debug_hitbox) {
					if(e.solid) shapeRenderer.setColor(Color.RED);
					else shapeRenderer.setColor(Color.GREEN);
					
					shapeRenderer.polygon(e.getHitbox().getVertices());	
				}
				
				if(debug_pathing && e instanceof Character && ((Character)e).moveTo!=null&&((Character)e).path!=null){
					shapeRenderer.setColor(Color.BLUE);
					shapeRenderer.rect(((Character)e).moveTo[0]*Tile.TS, ((Character)e).moveTo[1]*Tile.TS, Tile.TS, Tile.TS);
					shapeRenderer.line(e.x, e.y, ((Character)e).moveTo[0]*Tile.TS+Tile.TS/2, ((Character)e).moveTo[1]*Tile.TS+Tile.TS/2);
					int[] prePoint = new int[]{0,0};
					shapeRenderer.setColor(Color.YELLOW);
					for(int[] point:((Character)e).path){
						if(((Character)e).path.indexOf(point)>0){
							shapeRenderer.line(prePoint[0]*Tile.TS+Tile.TS/2, prePoint[1]*Tile.TS+Tile.TS/2,point[0]*Tile.TS+Tile.TS/2,point[1]*Tile.TS+Tile.TS/2);
						}
						prePoint = point;
					}
					if(e instanceof Goon && ((Goon)e).lineOfCover != null){
						shapeRenderer.setColor(Color.CORAL);
						shapeRenderer.line(((Goon)e).lineOfCover[0], ((Goon)e).lineOfCover[1], ((Goon)e).lineOfCover[2], ((Goon)e).lineOfCover[3]);
					}
				}
				
				if(debug_sight) {
					Gdx.gl.glEnable(GL20.GL_BLEND);
					Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
					if(e instanceof Character){
						if(e instanceof Player)shapeRenderer.setColor(1,0,1,0.2f);
						if(e instanceof Friend)shapeRenderer.setColor(0,1,1,0.2f);
						if(e instanceof Enemy)shapeRenderer.setColor(1,1,0,0.2f);
						shapeRenderer.set(ShapeType.Filled);
						for(Polygon tri: ((Character)e).visTris){
							float[] points = tri.getVertices();
							shapeRenderer.triangle(points[0], points[1], points[2], points[3], points[4], points[5]);
						}
						shapeRenderer.set(ShapeType.Line);
					}
					Gdx.gl.glBlendFunc(GL20.GL_ZERO, GL20.GL_ONE);
					Gdx.gl.glDisable(GL20.GL_BLEND);
				}
			}
			
			shapeRenderer.end();
		}
		
		batch.begin();
		
		batch.setProjectionMatrix(hudCam.cam.combined);
		
		for(int i = hudEntities.size()-1;i>=0;i--) {
			hudEntities.get(i).draw(batch);
		}
		
		mouse.draw(batch);
		descBox.draw(batch);
		
		if(debug_frames) fpsFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 60f, cam.height-8f);
		
		batch.end();
	}
	
	public void changeFloor(int floor, int destX, int destY, float x, float y){
		for(int i = 0; i < entities.size(); i++){
			Entity e = entities.get(i);
			if(e.light != null)e.light.unload();
			if(e instanceof Character){
				for(int k = 0; k < ((Character)e).equipItems.length; k++){
					if(((Character)e).equipItems[k] != null)((Character)e).equipItems[k].unequip();
					((Character)e).equipItems[k] = null;
				}
			}
		}
		entities.remove(player);
		if(curDungeon.floors.size() <= floor) {
			//generate new floor
			curDungeon.floors.add(new Floor(this, "rooms", 50,50,destX,destY,(int)(x/Tile.TS),(int)(y/Tile.TS)));
		}
		curFloor = curDungeon.floors.get(floor);
		entities = curFloor.entities;
		entities.add(0,player);
		areaMap = curFloor.areaMap;
		player.x = destX*Tile.TS-Tile.TS/2;
		player.y = destY*Tile.TS-Tile.TS/2;
		for(Entity e: entities){
			if(e.light != null)e.light.load();
		}
		rayHandler.setWorld(curFloor.box2dWorld);
		if(debug_light)rayHandler.setWorld(emptyWorld);
	}
}
