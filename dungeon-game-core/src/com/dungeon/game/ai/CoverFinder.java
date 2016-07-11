package com.dungeon.game.ai;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.dungeon.game.entity.Entity;
import com.dungeon.game.utilities.Spritesheet;
import com.dungeon.game.world.Tile;
import com.dungeon.game.world.World;

public class CoverFinder {

	//method to find suitable cover from enemy fire
	//world is the world
	//entity is the entity doing the cover-finding
	//danger is the threat that the entity is hiding from
	//pos is the int array to be filled with the coordinates of the cover tile
	//fire back is weather the entity should chose a position from which they can shoot at danger
	//returns weather or not an applicable cover was found
	public static boolean findCover(World world, Entity entity, Entity danger, int[] pos, boolean fireBack, int range){
		//psuedo code is something like this. any tile that HAS NO LOS to danger that is next to a tile WITH LOS to danger is considered cover when needing to shoot at danger.
		//otherwise any tile without LOS to danger that is far from danger is cover.
		ArrayList<float[]> rays = new ArrayList<float[]>(); //{startX,startY,endX,endy}
		
		for(int i = -180; i < 180; i+=18){
			rays.add(new float[]{danger.x,danger.y,danger.x+(float) (Math.cos((i)/180f*Math.PI)*range*(float)Tile.TS),danger.y+(float) (Math.sin((i)/180f*Math.PI)*range*(float)Tile.TS)});
		}
		
		for(int[] corner: world.curFloor.corners){
			if(Math.sqrt((danger.x-corner[0])*(danger.x-corner[0])+(danger.y-corner[1])*(danger.y-corner[1]))<range*Tile.TS){
				float angleSeg = (float) (Math.atan2(corner[1]-danger.y,corner[0]-danger.x)*180f/Math.PI);
				if(corner[2] == 0) {
					rays.add(new float[]{danger.x,danger.y,danger.x+(float) (Math.cos((angleSeg+0.01f)/180f*Math.PI)*range*(float)Tile.TS),danger.y+(float) (Math.sin((angleSeg+0.01)/180f*Math.PI)*range*(float)Tile.TS)});
					rays.add(new float[]{danger.x,danger.y,danger.x+(float) (Math.cos((angleSeg-0.01f)/180f*Math.PI)*range*(float)Tile.TS),danger.y+(float) (Math.sin((angleSeg-0.01)/180f*Math.PI)*range*(float)Tile.TS)});
				}
				else {
					rays.add(new float[]{danger.x,danger.y,danger.x+(float) (Math.cos((angleSeg)/180f*Math.PI)*range*(float)Tile.TS),danger.y+(float) (Math.sin((angleSeg)/180f*Math.PI)*range*(float)Tile.TS)});
				}
			}
		}
		
		//calculate the verticies
		
		float[][] verticies = new float[rays.size()][2];

		ArrayList<float[]> edges = new ArrayList<float[]>();
		
		for(float[] edge: world.curFloor.edges){
			if(Intersector.distanceSegmentPoint(edge[0], edge[1], edge[2], edge[3], danger.x, danger.y)<range*Tile.TS)edges.add(edge);
		}
		
		Vector2 endVertex = new Vector2(0,0);
		for(int i = 0; i <rays.size();i++){
			endVertex.x = rays.get(i)[2];
			endVertex.y = rays.get(i)[3];
			for(float[] edge: edges){
				Intersector.intersectSegments(rays.get(i)[0],rays.get(i)[1], endVertex.x,endVertex.y, edge[0],edge[1], edge[2],edge[3],endVertex);
			}
			verticies[i] = (new float[]{endVertex.x, endVertex.y});
		}
		
		//calculate the angles of each vertex
		float[] vertexAngles = new float[verticies.length];
		for(int i = 0; i < verticies.length; i++){
			vertexAngles[i] = (float) Math.atan2(verticies[i][1]-danger.y,verticies[i][0]-danger.x);
		}
		
		//reorder points to be in counterclockwise fashion.
		Arrays.sort(vertexAngles);
		
		float[] finalVerticies = new float[vertexAngles.length*2];
		for(int i = 1; i < finalVerticies.length; i+=2){
			for(float[] vertex: verticies){
				if(vertexAngles[i/2] == (float)Math.atan2(vertex[1]-danger.y,vertex[0]-danger.x)){
					finalVerticies[i-1] = vertex[0];
					finalVerticies[i] = vertex[1];
					break;
				}
			}
		}
		
		//create the visPolygon

		Polygon dangerPolygon = new Polygon(finalVerticies);
		
		ArrayList<Polygon> dangerTris = new ArrayList<Polygon>();
		for(int i = 0; i < (dangerPolygon.getVertices().length/2)-1;i++){
			dangerTris.add(new Polygon(new float[]{danger.x,danger.y,dangerPolygon.getVertices()[i*2],dangerPolygon.getVertices()[i*2+1],dangerPolygon.getVertices()[i*2+2],dangerPolygon.getVertices()[i*2+3]}));
		}
		dangerTris.add(new Polygon(new float[]{danger.x,danger.y,dangerPolygon.getVertices()[dangerPolygon.getVertices().length-2],dangerPolygon.getVertices()[dangerPolygon.getVertices().length-1],dangerPolygon.getVertices()[0],dangerPolygon.getVertices()[1]}));
		//TEMP SHIT
		Texture[] texturesTemp = Spritesheet.getSprites("tilemap.png", 32, 32);
		Texture[][] textures = new Texture[texturesTemp.length][8];
		for(int i = 0; i < texturesTemp.length; i++){
			textures[i][0] = texturesTemp[i];
			
			if(!texturesTemp[i].getTextureData().isPrepared()) texturesTemp[i].getTextureData().prepare();
			Pixmap tempMap = texturesTemp[i].getTextureData().consumePixmap();
			for(int k = 1; k<4; k++){
				
				textures[i][k] = new Texture(Spritesheet.rotatePixmap(tempMap, k));
			}
			
			for(int k = 4; k<8; k++){
				
				textures[i][k] = new Texture(Spritesheet.rotatePixmap(Spritesheet.flipPixmap(tempMap), k==4?3:k-5));
			}
			
			
			tempMap.dispose();
		}
		//CLOSE TEMP SHIT
		//find cover tiles
		ArrayList<int[]> coverTiles = new ArrayList<int[]>();
		for(int i = (int) Math.ceil(danger.x/Tile.TS - range); i < Math.floor(danger.x/Tile.TS + range); i++){
			for(int k = (int) Math.ceil(danger.y/Tile.TS - range); k < Math.floor(danger.y/Tile.TS + range); k++){
				if(world.curFloor.tm[k][i].data == 0 && Math.sqrt((i-danger.x/32)*(i-danger.x/32)+(k-danger.y/32)*(k-danger.y/32)) < range){
					world.curFloor.tm[k][i] = new Tile(textures, 3);
				}
			}
		}
		return false;
		
	}
}
