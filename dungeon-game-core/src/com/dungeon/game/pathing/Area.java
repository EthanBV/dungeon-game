package com.dungeon.game.pathing;

import java.util.ArrayList;

import com.dungeon.game.entity.Entity;
import com.dungeon.game.world.Tile;

public class Area {
	private ArrayList<int[]> points; //Array of all tiles in this area
	private ArrayList<int[]> edgePoints; //Array of all edges of this area.
	protected ArrayList<Area> adjacentAreas; //Holds reference to all adjacent Areas
	private ArrayList<ArrayList<int[]>> edges; //Holds points where this Area Borders other Areas
	private ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<int[]>>>>> minPaths; //Holds information for the minimum viable paths from one edge to another;
	private ArrayList<Entity> entities; //contains data for all entities in the area;
	
	public Area(){
		points = new ArrayList<int[]>();
		edgePoints = new ArrayList<int[]>();
		adjacentAreas = new ArrayList<Area>();
		edges = new ArrayList<ArrayList<int[]>>();
		minPaths = new ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<int[]>>>>>();
		entities = new ArrayList<Entity>();
		
	}
	
	public void addRectangleToArea(int x, int y, int width, int height){ //adds a rectangle of points to the Area.
		for(int i = x; i < x+width; i++){
			for(int k = y; k< y+height; k++){
				points.add(new int[]{x,y});
			}
		}
		calculateEdges();
	}
	private void calculateEdges() { //recalculates the edges of this area;
		edgePoints = new ArrayList<int[]>();
		for(int[] i: points){
			boolean topFound = false;
			boolean botFound = false;
			boolean leftFound = false;
			boolean rightFound = false;
			for(int[] k: points){
				if(i[0]+1==k[0]&&i[1]==k[1]) rightFound = true;
				if(i[0]-1==k[0]&&i[1]==k[1]) leftFound = true;
				if(i[0]==k[0]&&i[1]+1==k[1]) topFound = true;
				if(i[0]==k[0]&&i[1]-1==k[1]) botFound = true;
			}
			
			if(!(topFound&&botFound&&leftFound&&rightFound))edgePoints.add(i);
		}
	}
	public void calculateBorders(Area a){ //find the borders between this area and another one.
		ArrayList<int[]> borders = new ArrayList<int[]>();
		for(int[] i:edgePoints){
			for(int[] k: a.edgePoints){
				if(i[0]-1==k[0]&&i[1]==k[1])borders.add(new int[]{i[0], k[0]});
				else if(i[0]+1==k[0]&&i[1]==k[1])borders.add(new int[]{i[0], k[0]});
				else if(i[0]==k[0]&&i[1]-1==k[1])borders.add(new int[]{i[0], k[0]});
				else if(i[0]==k[0]&&i[1]+1==k[1])borders.add(new int[]{i[0], k[0]});
			}
		}
		if(borders.size()>0){
			int indexOfArea = 0;
			if(adjacentAreas.indexOf(a)==-1){
				adjacentAreas.add(a);
			}
			indexOfArea = adjacentAreas.indexOf(a);
			
			edges.set(indexOfArea, borders);
		}
	}
	
	public boolean containsPoint(int[] point){
		for(int[] i: points){
			if(point[0] == i[0]&&point[1] == i[1]) return true;
		}
		return false;
	}
	
	public ArrayList<int[]> getMinPath(Tile[][] tm, int[] startPoint, Area start, Area end){
		ArrayList<ArrayList<int[]>> minPathCandidates;
		ArrayList<int[]> minPath;
		if(start==null){
			minPathCandidates = new ArrayList<ArrayList<int[]>>();
			int endAreaIndex = adjacentAreas.indexOf(end);
			for(int[] edge: edges.get(endAreaIndex)){
				minPathCandidates.add(findPath(tm, startPoint, edge));
			}
			
		}else{
			int startAreaIndex = adjacentAreas.indexOf(start);
			int endAreaIndex = adjacentAreas.indexOf(end);
			int startPointIndex = edges.get(startAreaIndex).indexOf(startPoint);
			minPathCandidates = minPaths.get(startAreaIndex).get(endAreaIndex).get(startPointIndex);
			
		}
		minPath = minPathCandidates.get(0);
		for(ArrayList<int[]> path: minPathCandidates){
			if(path.size()<minPath.size())minPath = path;
		}	
		return minPath;
	}
	
	public void calculateMinPaths(Tile[][] tm){ //calculates the shortest path between any two edges of this area;
		ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<int[]>>>>> listFromStartArea = new ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<int[]>>>>>();
		//need to properly arrange data here :(
		for(ArrayList<int[]> startArea: edges){
			ArrayList<ArrayList<ArrayList<ArrayList<int[]>>>> listFromEndArea = new ArrayList<ArrayList<ArrayList<ArrayList<int[]>>>>();
			for(ArrayList<int[]> endArea: edges){
				ArrayList<ArrayList<ArrayList<int[]>>> listFromStartPoint = new ArrayList<ArrayList<ArrayList<int[]>>>();
				for(int[] startPoint: startArea){
					ArrayList<ArrayList<int[]>> listFromEndPoint = new ArrayList<ArrayList<int[]>>();
					for(int[] endPoint: endArea){
						ArrayList<int[]> path = findPath(tm, startPoint, endPoint);
						listFromEndPoint.add(path);
					}
					listFromStartPoint.add(listFromEndPoint);
				}	
				listFromEndArea.add(listFromStartPoint);
			}
			listFromStartArea.add(listFromEndArea);
		}
		minPaths = listFromStartArea;
	}
	
	public ArrayList<int[]> findPath(Tile[][] tm, int[] start, int[] end){ //uses A* to find a path within from one edge to another
		ArrayList<int[]> queue = new ArrayList<int[]>();
		queue.add(new int[]{end[0],end[1],0});
		boolean endQueue = false;
		
		for(int i = 0; i < queue.size(); i++){
			ArrayList<int[]> toAdd = new ArrayList<int[]>();
			for(int[] k: points){
				if(queue.get(i)[0]+1==k[0]&&queue.get(i)[1]==k[1]&&tm[queue.get(i)[1]][queue.get(i)[0]].data!=1)toAdd.add(new int[]{queue.get(i)[0]+1,queue.get(i)[1], queue.get(i)[2]+1});
				if(queue.get(i)[0]-1==k[0]&&queue.get(i)[1]==k[1]&&tm[queue.get(i)[1]][queue.get(i)[0]].data!=1)toAdd.add(new int[]{queue.get(i)[0]-1,queue.get(i)[1], queue.get(i)[2]+1});
				if(queue.get(i)[0]==k[0]&&queue.get(i)[1]+1==k[1]&&tm[queue.get(i)[1]][queue.get(i)[0]].data!=1)toAdd.add(new int[]{queue.get(i)[0],queue.get(i)[1]+1, queue.get(i)[2]+1});
				if(queue.get(i)[0]==k[0]&&queue.get(i)[1]-1==k[1]&&tm[queue.get(i)[1]][queue.get(i)[0]].data!=1)toAdd.add(new int[]{queue.get(i)[0],queue.get(i)[1]-1, queue.get(i)[2]+1});
			}
			for(int k = toAdd.size()-1; k>=0;k--){
				for(int[] j: queue){
					if(toAdd.get(k)[0]==j[0]&&toAdd.get(k)[1]==j[1]){
						toAdd.remove(toAdd.get(k));
						break;
					}
				}
			}
			for(int[] k: toAdd){
				queue.add(k);
				if(k[0] == start[0]&&k[1] == start[1])endQueue = true;
			}
			if(endQueue)break;
		}
		//find path here with queue;
		ArrayList<int[]> path = new ArrayList<int[]>();
		boolean gotToTarget = false;
		int[] curTile = new int[]{start[0],start[1]};
		while(!gotToTarget){
			path.add(curTile);
			if(curTile[0]==end[0]&&curTile[1]==end[1])gotToTarget=true;
			
			else{
				for(int[] i: queue){
					int curCount = 0;
					int[] workingTile = new int[]{0,0};
					if(curTile[0]+1==i[0]&&curTile[1]==i[1]){
						curCount = i[2];
						workingTile = new int[]{i[0],i[1]};
					}
					if(curTile[0]-1==i[0]&&curTile[1]==i[1]&&i[2]<=curCount){ //Will prioratize left over right;
						curCount = i[2];
						workingTile = new int[]{i[0],i[1]};
					}
					if(curTile[0]==i[0]&&curTile[1]+1==i[1]&&i[2]<=curCount){ // Will prioratize up over left or right;
						curCount = i[2];
						workingTile = new int[]{i[0],i[1]};
					}
					if(curTile[0]==i[0]&&curTile[1]-1==i[1]&&i[2]<=curCount){ // will always prioratize down over all other directions
						curCount = i[2];
						workingTile = new int[]{i[0],i[1]};
					}
					curTile = workingTile;
				}
			}
		}
		return path;
	}
}
