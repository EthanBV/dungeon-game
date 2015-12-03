package com.dungeon.game.entity;

import java.util.ArrayList;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.dungeon.game.world.Tile;

public class Pathfinder {
	public int[][] map;
	public float width;
	public float height;
	
	public Pathfinder(){
		
	}
	
	public void setData(int[][] map, float width, float height){
		this.map = map;
		this.width = width;
		this.height = height;
	}
	
	public int[] findPath(int[] start, int[] end, float x, float y){
		if(start[0]==end[0]&&start[1]==end[1])return end;
		ArrayList<int[]> queue = new ArrayList<int[]>();
		queue.add(new int[]{end[0],end[1],0});
		int count = 0;
		boolean endQueue = false;
		for(int i = 0; i < queue.size(); i++){
			count++;
			if(count>1000)return null;
			ArrayList<int[]> toAdd = new ArrayList<int[]>();
			if(!(queue.get(i)[0]+1>map[0].length||map[queue.get(i)[1]][queue.get(i)[0]+1]==1))toAdd.add(new int[]{queue.get(i)[0]+1,queue.get(i)[1],count});
			if(!(queue.get(i)[1]+1>map.length||map[queue.get(i)[1]+1][queue.get(i)[0]]==1))toAdd.add(new int[]{queue.get(i)[0],queue.get(i)[1]+1,count});
			if(!(queue.get(i)[0]-1<0||map[queue.get(i)[1]][queue.get(i)[0]-1]==1))toAdd.add(new int[]{queue.get(i)[0]-1,queue.get(i)[1],count});
			if(!(queue.get(i)[1]+1>map.length||map[queue.get(i)[1]-1][queue.get(i)[0]]==1))toAdd.add(new int[]{queue.get(i)[0],queue.get(i)[1]-1,count});
			for(int k = toAdd.size()-1; k>=0;k--){
				for(int[] e: queue){
					toAdd.get(k);
					if(toAdd.get(k)[0]==e[0]&&toAdd.get(k)[1]==e[1]&&toAdd.get(k)[2]>=e[2]){
						toAdd.remove(toAdd.get(k));
						break;
					}
				}
			}
			for(int[] k: toAdd){
				if(k[0] == start[0]&&k[1] == start[1])endQueue = true;
				queue.add(k);
			}
			if(endQueue)break;
		}
		ArrayList<int[]> path = new ArrayList<int[]>();
		boolean gotToTarget = false;
		int[] curTile = new int[]{start[0],start[1]};
		int curCount=count+1;
		int temp = 0;
		while(!gotToTarget&&temp<10){
			temp++;
			path.add(curTile);
			if(curTile[0]==end[0]&&curTile[1]==end[1])gotToTarget=true;
			else{
				for(int[] i: queue){
					if(curTile[0]+1==i[0]&&curTile[1]==i[1]){
						curCount = i[2];
						curTile = new int[]{i[0],i[1]};
						break;
					}
					if(curTile[0]-1==i[0]&&curTile[1]==i[1]&&i[2]<=curCount){
						curCount = i[2];
						curTile = new int[]{i[0],i[1]};
						break;
					}
					if(curTile[0]==i[0]&&curTile[1]+1==i[1]&&i[2]<=curCount){
						curCount = i[2];
						curTile = new int[]{i[0],i[1]};
						break;
					}
					if(curTile[0]==i[0]&&curTile[1]-1==i[1]&&i[2]<=curCount){
						curCount = i[2];
						curTile = new int[]{i[0],i[1]};
						break;
					}
				}
			}
		}
		int[] target = path.get(1);
		for(int i = 0;i<path.size();i++){
			boolean changeTarget = true;
			for(int k = 0; k< map.length;k++){
				for(int j = 0; j <map[k].length;j++){
					if(map[k][j]==1){
						float[] verticies = new float[]{j*Tile.TS,k*Tile.TS,(j+1)*Tile.TS,k*Tile.TS,(j+1)*Tile.TS,(k+1)*Tile.TS,(j)*Tile.TS,(k+1)*Tile.TS};
						if(Intersector.intersectSegmentPolygon(new Vector2(x+width/2,y+height/2), new Vector2(path.get(i)[0]*Tile.TS+Tile.TS/2,path.get(i)[1]*Tile.TS+Tile.TS/2), new Polygon(verticies))){
							changeTarget = false;
						}
					}
				}
			}
			if(changeTarget){
				target = path.get(i);
			}
		}
		return target;
	}
}
