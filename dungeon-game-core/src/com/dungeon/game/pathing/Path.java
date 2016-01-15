package com.dungeon.game.pathing;

import java.util.ArrayList;

import com.dungeon.game.world.Tile;

public class Path {
	private int[] start; //start position of the path
	private int[] end; //end position of the path
	ArrayList<Area> areas; //areas along the path
	public Path(Area area, int[] start, int[] end){ // constructor used to create initial path
		areas = new ArrayList<Area>();
		areas.add(area);
		this.start = start;
		this.end = end;
	}
	public Path(Path path, Area area){ //constructor used to create paths
		areas = new ArrayList<Area>(path.areas);
		areas.add(area);
		start = new int[]{path.start[0],path.start[1]};
		end = new int[]{path.end[0],path.end[1]};
	}
	
	public Area getLastArea(){
		return areas.get(areas.size()-1);
	}
	
	public boolean isAreaOnPath(Area area){
		return areas.contains(area);
	}
	
	public ArrayList<Area> getExpandAreas(){
		ArrayList<Area>  expandAreas = new ArrayList<Area>();
		for(Area area: getLastArea().adjacentAreas){
			if(!isAreaOnPath(area))expandAreas.add(area);
		}
		return expandAreas;
	}
	
	
	public ArrayList<int[]> getTiles(Tile[][] tm){
		ArrayList<int[]> Tiles = new ArrayList<int[]>();
		//find tiles in 3 stages
		//start point to edge
		//intermediate areas
		for(Area area: areas){
			System.out.println();
			ArrayList<int[]> subPath;
			if(area.equals(areas.get(0))){
				subPath = area.getMinPath(tm, start,null, areas.get(areas.indexOf(area)+1));
			}else if(area.equals(getLastArea())){
				System.out.println("prestartPointX: "+Tiles.get(Tiles.size()-1)[0]);
				System.out.println("prestartPointY: "+Tiles.get(Tiles.size()-1)[1]);
				System.out.println("isAlreadyThere?: "+area.containsPoint(Tiles.get(Tiles.size()-1)));
				int[] startPoint = area.findAdjacentEdge(Tiles.get(Tiles.size()-1));
				System.out.println("startPointX: "+startPoint[0]);
				System.out.println("startPointY: "+startPoint[1]);
				subPath = area.findPath(tm, startPoint, end);
			}else {
				System.out.println("prestartPointX: "+Tiles.get(Tiles.size()-1)[0]);
				System.out.println("prestartPointY: "+Tiles.get(Tiles.size()-1)[1]);
				System.out.println("isAlreadyThere?: "+area.containsPoint(Tiles.get(Tiles.size()-1)));
				int[] startPoint = area.findAdjacentEdge(Tiles.get(Tiles.size()-1));
				System.out.println("startPointX: "+startPoint[0]);
				System.out.println("startPointY: "+startPoint[1]);
				subPath = area.getMinPath(tm, startPoint,areas.get(areas.indexOf(area)-1), areas.get(areas.indexOf(area)+1));
	
			}
			for(int[] point: subPath){
				Tiles.add(point);
			}
		}
		//edge to end point
		return Tiles;
	}
	
	public int getLength(Tile[][] tm){
		return getTiles(tm).size();
	}
	
	
}
