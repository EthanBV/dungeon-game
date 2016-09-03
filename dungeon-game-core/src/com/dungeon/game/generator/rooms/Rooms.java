package com.dungeon.game.generator.rooms;

import java.util.ArrayList;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.dungeon.game.entity.furniture.Stair;
import com.dungeon.game.generator.Generation;
import com.dungeon.game.generator.rooms.hallway.BasicHall;
import com.dungeon.game.generator.rooms.hallway.Hallway;
import com.dungeon.game.generator.rooms.room.EnemyRoom;
import com.dungeon.game.generator.rooms.room.Room;
import com.dungeon.game.pathing.newpathing.Graph;
import com.dungeon.game.pathing.newpathing.GraphLevel;
import com.dungeon.game.pathing.newpathing.Node;
import com.dungeon.game.utilities.MethodArray;
import com.dungeon.game.world.Tile;
import com.dungeon.game.world.World;

public class Rooms extends Generation {
	protected ArrayList<Rectangle> rooms;
	protected ArrayList<ArrayList<int[]>> halls;
	protected ArrayList<ArrayList<Rectangle>> hallEnds;
	protected MethodArray roomGenerators;
	
	public Rooms(World world, int width, int height, int centerX, int centerY, int upTrapX, int upTrapY, int textureSeed, Object[] args){
		super(world, width, height, textureSeed, new Object[]{args.length > 0 ? args[0]: "nope", centerX, centerY, upTrapX, upTrapY});
	}
	
	protected void generate(Object[] args){
		super.generate(args);
		rooms = new ArrayList<Rectangle>();
		halls = new ArrayList<ArrayList<int[]>>();
		hallEnds = new ArrayList<ArrayList<Rectangle>>();
		if(world.curDungeon!=null)entities.add(new Stair(world, (Integer)(args[1])*Tile.TS-Tile.TS/2, (Integer)(args[2])*Tile.TS-Tile.TS/2, false, (Integer)(args[3])+1, (Integer)(args[4])+1));
		roomGenerators = new MethodArray(4){
			@SuppressWarnings("unused")
			public void a(int x, int y, int width, int height, int dir, Rectangle room){ //these "unused" methods are def used.
				int nextX = (int) (x+width*Math.random());
				int nextY = y;
				generateHallWay(nextX, nextY-1, dir, room);
			}
			@SuppressWarnings("unused")
			public void b(int x, int y, int width, int height, int dir, Rectangle room){
				int nextX = (int) (x+width*Math.random());
				int nextY = y+height;
				generateHallWay(nextX, nextY, dir, room);
			}
			@SuppressWarnings("unused")
			public void c(int x, int y, int width, int height, int dir, Rectangle room){
				int nextX = x;
				int nextY = (int) (y+height*Math.random());
				generateHallWay(nextX-1, nextY, dir, room);
			}
			@SuppressWarnings("unused")
			public void d(int x, int y, int width, int height, int dir, Rectangle room){
				int nextX = x+width;
				int nextY = (int) (y+height*Math.random());
				generateHallWay(nextX, nextY, dir, room);
			}
		};
		generateStartRoom((Integer)(args[1]), (Integer)(args[2]));
		if(args.length == 0 ||  !args[0].equals("test")){
			populateRooms();
			populateHallWays();
			generateStairDown();
			makeWalls(10);
		}
		
	}
	

	public boolean generateStartRoom(int x, int y){
		int originX = x;
		int originY = y;
		int height = (int) (3+Math.random()*map.length/7);
		y-=(int) (Math.random()*height)+1;
		int width = (int) (3+Math.random()*map[0].length/7);
		x-=(int) (Math.random()*width)+1;
		Rectangle room = new Rectangle(x,y,width,height);
		int nextX;
		int nextY;
		if(isValidRoom(room)){
			addRoomToMap(room, false);
			for(int i = 0; i<100;i++){
				int dir = (int) (Math.random()*4);
				try {
					roomGenerators.methods[dir].invoke(roomGenerators, x, y, width, height, dir, room);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}else //generateStartRoom(originX, originY);
		return false;
	}
	
	public boolean generateBelowRoom(int x, int y, ArrayList<int[]> hall, Rectangle lastRoom){
		int doorX = x;
		int doorY = y-1;
		int height = (int) (3+Math.random()*map.length/7);
		y-=height;
		int width = (int) (3+Math.random()*map[0].length/7);
		x-=(int) (Math.random()*width);
		Rectangle room = new Rectangle(x, y, width, height);
		int nextX;
		int nextY;
		halls.add(hall);
		if(isValidRoom(room)){
			ArrayList<Rectangle> hallEnd = new ArrayList<Rectangle>();
			hallEnd.add(room);
			hallEnd.add(lastRoom);
			hallEnds.add(hallEnd);
			for(int i=0;i<hall.size();i++){
				map[hall.get(i)[1]][hall.get(i)[0]]=tileMap.getTile(4);
			}
			addRoomToMap(room, true);
			for(int i = 0; i<100;i++){
				int dir = (int) (Math.random()*4);
				try {
					roomGenerators.methods[dir].invoke(roomGenerators, x, y, width, height, dir, room);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
		halls.remove(halls.size()-1);
		return false;
	}
	
	public boolean generateAboveRoom(int x, int y, ArrayList<int[]> hall, Rectangle lastRoom){
		int doorX = x;
		int doorY = y;
		int height = (int) (3+Math.random()*map.length/7);
		int width = (int) (3+Math.random()*map[0].length/7);
		x-=(int) (Math.random()*width);
		Rectangle room = new Rectangle(x, y, width, height);
		int nextX;
		int nextY;
		halls.add(hall);
		if(isValidRoom(room)){
			ArrayList<Rectangle> hallEnd = new ArrayList<Rectangle>();
			hallEnd.add(room);
			hallEnd.add(lastRoom);
			hallEnds.add(hallEnd);
			for(int i=0;i<hall.size();i++){
				map[hall.get(i)[1]][hall.get(i)[0]]=tileMap.getTile(4);
			}
			addRoomToMap(room, true);
			for(int i = 0; i<100;i++){
				int dir = (int) (Math.random()*4);
				try {
					roomGenerators.methods[dir].invoke(roomGenerators, x, y, width, height, dir, room);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
		halls.remove(halls.size()-1);
		return false;
	}
	
	public boolean generateLeftRoom(int x, int y, ArrayList<int[]> hall, Rectangle lastRoom){
		int doorX = x-1;
		int doorY = y;
		int height = (int) (3+Math.random()*map.length/7);
		y-=(int)(Math.random()*height);
		int width = (int) (3+Math.random()*map[0].length/7);
		x-=width;
		Rectangle room = new Rectangle(x, y, width, height);
		int nextX;
		int nextY;
		halls.add(hall);
		if(isValidRoom(room)){
			ArrayList<Rectangle> hallEnd = new ArrayList<Rectangle>();
			hallEnd.add(room);
			hallEnd.add(lastRoom);
			hallEnds.add(hallEnd);
			for(int i=0;i<hall.size();i++){
				map[hall.get(i)[1]][hall.get(i)[0]]=tileMap.getTile(4);
			}
			addRoomToMap(room, true);
			for(int i = 0; i<100;i++){
				int dir = (int) (Math.random()*4);
				try {
					roomGenerators.methods[dir].invoke(roomGenerators, x, y, width, height, dir, room);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
		halls.remove(halls.size()-1);
		return false;
	}
	
	public boolean generateRightRoom(int x, int y, ArrayList<int[]> hall, Rectangle lastRoom){
		int doorX = x;
		int doorY = y;
		int height = (int) (3+Math.random()*map.length/7);
		y-=(int)(Math.random()*height);
		int width = (int) (3+Math.random()*map[0].length/7);
		Rectangle room = new Rectangle(x, y, width, height);
		int nextX;
		int nextY;
		halls.add(hall);
		if(isValidRoom(room)){
			ArrayList<Rectangle> hallEnd = new ArrayList<Rectangle>();
			hallEnd.add(room);
			hallEnd.add(lastRoom);
			hallEnds.add(hallEnd);
			for(int i=0;i<hall.size();i++){
				map[hall.get(i)[1]][hall.get(i)[0]]=tileMap.getTile(4);
			}
			addRoomToMap(room, true);
			for(int i = 0; i<100;i++){
				int dir = (int) (Math.random()*4);
				try {
					roomGenerators.methods[dir].invoke(roomGenerators, x, y, width, height, dir, room);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
		halls.remove(halls.size()-1);
		return false;
	}
	
	public void generateHallWay(int x, int y, int dir, Rectangle room){
		Rectangle hallDest = null;
		boolean justTurned = true;
		boolean generateHall = true;
		boolean generateRoom = true;
		boolean addHallToArrayList = true;
		ArrayList<int[]> hallCoordinates = new ArrayList<int[]>();
		hallCoordinates.add(new int[]{x,y, dir});
		int length=5+(int) (Math.random()*15);
		for(int i = 0; i < length; i++){
			if(dir == 0)y--;
			if(dir == 1)y++;
			if(dir == 2)x--;
			if(dir == 3)x++;
			if(isValidHallTile(x,y, hallCoordinates)){
				hallCoordinates.add(new int[]{x,y,dir});
				if(Math.random()>0.2&&justTurned==false){
					justTurned=true;
					if(dir == 0||dir == 1){
						dir=2+(int) (Math.random()*2);
					}else{
						dir=(int) (Math.random()*2);
					}
				}else{
					justTurned = false;
				}
			} else if(hallCoordinates.size()>=4){
				hallCoordinates.add(new int[]{x,y,dir});
				if(dir == 0)y--;
				if(dir == 1)y++;
				if(dir == 2)x--;
				if(dir == 3)x++;
				if(isTileInRoom(x,y, room)!=null&&Math.random()>0.9){
					i=length;
					generateRoom = false;
					generateHall = true;
					hallDest = isTileInRoom(x,y,room);
					if(isHallTaken(room, hallDest))generateHall = false;
				}else if(isTileInHall(x,y, room, hallCoordinates)&&isTileInRoom(x,y, room)==null){
					addHallToArrayList = false;
					i=length;
					generateRoom = false;
					generateHall = true;
					
				}else{
					i=length;
					generateHall = false;
					generateRoom = false;
				}
			}else{
				i=length;
				generateHall = false;
				generateRoom = false;
			}
		}
		if(generateRoom&&generateHall){
			generateHall=false;
			if(hallCoordinates.get(hallCoordinates.size()-1)[2] == 0){
				generateBelowRoom(x, y, hallCoordinates, room);
			}else if(hallCoordinates.get(hallCoordinates.size()-1)[2] == 1){
				generateAboveRoom(x, y+1, hallCoordinates, room);
			}else if(hallCoordinates.get(hallCoordinates.size()-1)[2] == 2){
				generateLeftRoom(x, y, hallCoordinates, room);
			}else if(hallCoordinates.get(hallCoordinates.size()-1)[2] == 3){
				generateRightRoom(x+1, y, hallCoordinates, room);
			}
		}
		if(generateHall){
			if(addHallToArrayList){
				ArrayList<Rectangle> hallEnd = new ArrayList<Rectangle>();
				hallEnd.add(room);
				hallEnd.add(hallDest);
				hallEnds.add(hallEnd);
				halls.add(hallCoordinates);
			}
			for(int i=0;i<hallCoordinates.size();i++){
				map[hallCoordinates.get(i)[1]][hallCoordinates.get(i)[0]]=tileMap.getTile(4);
				if(i==0){
//					if(hallCoordinates.get(0)[2] == 0||hallCoordinates.get(0)[2] == 1)addDoor(hallCoordinates.get(0)[0],hallCoordinates.get(0)[1],0);
//					if(hallCoordinates.get(0)[2] == 2||hallCoordinates.get(0)[2] == 3)addDoor(hallCoordinates.get(0)[0],hallCoordinates.get(0)[1],1);
				}
				if(addHallToArrayList&&i==hallCoordinates.size()-1){
//					if(hallCoordinates.get(hallCoordinates.size()-1)[2] == 0||hallCoordinates.get(hallCoordinates.size()-1)[2] == 1)addDoor(hallCoordinates.get(hallCoordinates.size()-1)[0],hallCoordinates.get(hallCoordinates.size()-1)[1],0);
//					if(hallCoordinates.get(hallCoordinates.size()-1)[2] == 2||hallCoordinates.get(hallCoordinates.size()-1)[2] == 3)addDoor(hallCoordinates.get(hallCoordinates.size()-1)[0],hallCoordinates.get(hallCoordinates.size()-1)[1],1);
				}
			}
			
		}
	}

	public boolean isValidRoom(Rectangle room){
		boolean result = true;
		for(Rectangle i: rooms){
			if(Intersector.intersectRectangles(new Rectangle(room.x-1,room.y-1,room.width+1,room.height+1), new Rectangle(i.x-1,i.y-1,i.width+1,i.height+1), new Rectangle()))result = false;
		}
		for(ArrayList<int[]> i: halls){
			for(int[] k: i){
				if(!(i==halls.get(halls.size()-1)&&k==i.get(i.size()-1))){
				boolean xInter = false;
				boolean yInter = false;
				if(k[0]>=room.getX()-1&&k[0]<room.getX()+room.getWidth()+1)xInter = true;
				if(k[1]>=room.getY()-1&&k[1]<room.getY()+room.getHeight()+1)yInter = true;
				if(xInter&&yInter)result = false;
				}
			}
		}
		if(room.x<1)result = false;
		if(room.y<1)result = false;
		if(room.y+room.height>map.length-2)result = false;
		if(room.x+room.width>map[0].length-2)result = false;
		return result;
	}
	
	public boolean isValidHallTile(int x, int y, ArrayList<int[]> otherTiles){
		boolean result = true;
		for(Rectangle i: rooms){
			boolean xInter = false;
			boolean yInter = false;
			if(x>=i.getX()-1&&x<i.getX()+i.getWidth()+1)xInter = true;
			if(y>=i.getY()-1&&y<i.getY()+i.getHeight()+1)yInter = true;
			if(xInter&&yInter)result = false;
		}
		if(x<1||y<1||y>map.length-2||x>map[0].length-2)result = false;
		for(ArrayList<int[]> i: halls){
			for(int[] k: i){
				if(x == k[0]&&y==k[1])result = false;
				if(x+1 == k[0]&&y==k[1])result = false;
				if(x-1 == k[0]&&y==k[1])result = false;
				if(x == k[0]&&y+1==k[1])result = false;
				if(x == k[0]&&y-1==k[1])result = false;
			}
		}
		for(int[] i: otherTiles){
			if(i[0]==x&&i[1]==y)result = false;
		}
		return result;
	}
	
	public Rectangle isTileInRoom(int x, int y, Rectangle room){
		for(Rectangle i: rooms){
			boolean xInter = false;
			boolean yInter = false;
			if(i!=room){
				if(x>=i.getX()&&x<i.getX()+i.getWidth())xInter = true;
				if(y>=i.getY()&&y<i.getY()+i.getHeight())yInter = true;
				if(xInter&&yInter)return i;
			}
		}
		return null;
	}

	protected boolean isTileInHall(int x, int y, Rectangle room, ArrayList<int[]> hallTiles) {
		//loops for all halls
		for(int i = 0; i< halls.size(); i++){
			//loops for all tiles
			for(int k = 0; k<halls.get(i).size();k++){
				//checks if tile matches
				if(x == halls.get(i).get(k)[0]&& y == halls.get(i).get(k)[1]){
					//loops for all roomEnds of the hall
					for(int e = 0; e <hallEnds.get(i).size(); e++){
						if(room==hallEnds.get(i).get(e)||isHallTaken(room,hallEnds.get(i).get(e))){
							return false;
						}
					}
					hallEnds.get(i).add(room);
					for(int[] o: hallTiles){
						halls.get(i).add(o);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isHallTaken(Rectangle roomOne, Rectangle roomTwo) {
		for(ArrayList<Rectangle> i: hallEnds){
			boolean roomOneFound = false;
			boolean roomTwoFound = false;
			for(Rectangle k: i){
				if(k == roomOne)roomOneFound = true;
				if(k == roomTwo)roomTwoFound = true;
				if(roomOneFound&&roomTwoFound)return true;
			}
		}
		return false;
	}
	
	public void addRoomToMap(Rectangle room, boolean hasEnemies){
		boolean addedChest = false;
		rooms.add(room);
		int x = (int) room.x;
		int y = (int) room.y;
		int width = (int) room.width;
		int height = (int) room.height;
		for(int i = 0; i<height; i++){
			for(int k = 0; k<width; k++){
				map[y][x]=tileMap.getTile(4);
//				if(!addedChest&&i>0&&k>0&&i<height-1&&k<width-1&&Math.random()>1f-(1f/((float)width*(float)height))){
//					entities.add(LootGenerator.getChest(world, 1,x,y));
//					addedChest = true;
//				}
//				if(hasEnemies && i>-1&&k>-1&&i<height&&k<width&&Math.random()>0.96){
//					Goon enemy =  new Goon(world, x*Tile.TS+Tile.TS/2f, y*Tile.TS+Tile.TS/2f);
//					enemy.angle = (float) (180f-Math.random()*360f);
//					enemy.target_angle = enemy.angle;
//					entities.add(enemy);
//				}
				x++;
			}
			y++;
			x-=width;
		}
	}

	
	private void populateRooms() {
		ArrayList<Rectangle> rooms = (ArrayList<Rectangle>) this.rooms.clone();
		rooms.remove(0);
		Rectangle normRoomsRect;
		int[][] normRoomsDoorFinder;
		Room normRoom;
		while(rooms.size()>0){
			normRoomsRect = rooms.remove((int) (rooms.size()*Math.random()));
			normRoomsDoorFinder = findDoors(normRoomsRect);
			normRoom = new EnemyRoom(world, normRoomsRect, normRoomsDoorFinder, tileMap);
			normRoom.addToMap(map, entities);
		}
	}
	

	
	private void populateHallWays(){
		ArrayList<ArrayList<int[]>> halls = (ArrayList<ArrayList<int[]>>) this.halls.clone();
		Hallway hall;
		while(halls.size()>0){
			hall = new BasicHall(world, halls.remove((int) (halls.size()*Math.random())), tileMap);
			hall.addToMap(map, entities);
		}
	}
	

	
	protected int[][] findDoors(Rectangle room){
		ArrayList<int[]> doors = new ArrayList<int[]>();
		int x,y;
		x = (int) room.x-1;
		y = (int) room.y;
		for(int i = 0; i < room.height; i++){
			if(!Tile.isSolid(map[y][x])){
//				map[y][x]=tileMap.getTile(5);
//				return new int[]{0,x,y};
				doors.add(new int[]{0,x,y});
			}

			y++;
		}
		x = (int) (room.x+room.width);
		y = (int) room.y;
		for(int i = 0; i < room.height; i++){
			if(!Tile.isSolid(map[y][x])){
//				map[y][x]=tileMap.getTile(5);
//				return new int[]{1,x,y};
				doors.add(new int[]{1,x,y});
			}

			y++;
		}
		x = (int) room.x;
		y = (int) room.y-1;
		for(int i = 0; i < room.width; i++){
			if(!Tile.isSolid(map[y][x])){
//				map[y][x]=tileMap.getTile(5);
//				return new int[]{2,x,y};
				doors.add(new int[]{2,x,y});
			}

			x++;
		}
		x = (int) room.x;
		y = (int) (room.y+room.height);
		for(int i = 0; i < room.width; i++){
			if(!Tile.isSolid(map[y][x])){
//				map[y][x]=tileMap.getTile(5);
//				return new int[]{3,x,y};
				doors.add(new int[]{3,x,y});
			}

			x++;
		}
		int[][] ds = doors.toArray(new int[doors.size()][3]);
		return ds;
	}
	
	public void generateStairDown(){
		Rectangle room = rooms.get(1+(int) (Math.random()*(rooms.size()-1)));
		int width = 1+(int) (Math.random()*(room.width-1));
		int height = 1+(int) (Math.random()*(room.width-1));
		entities.add(new Stair(world,(room.x+width)*Tile.TS+Tile.TS/2 , (room.y+height)*Tile.TS+Tile.TS/2, true, 15+(int) (Math.random()*10), 15+((int) Math.random()*10)));
//		for(int i = 0; i < rooms.size(); i++){
//			if(Math.random()<(i+1)/rooms.size()){
//				Rectangle room = rooms.get(i);
//				for(int k = 1; k < room.width-1; k++){
//					for(int j = 1; j < room.height-1; j++){
//						if(Math.random()>((k+1)*(j+1))/((room.width-1)*(room.height-1))){
//							entities.add(new Stair(world,(room.x+k)*Tile.TS , (room.y+j)*Tile.TS, true, 15+(int) (Math.random()*10), 15+((int) Math.random()*10)));
//							return;
//						}
//					}
//				}
//			}
//		}
	}


	@Override
	public Graph getPathGraph() {
		
		GraphLevel gl0 = new GraphLevel();
		GraphLevel gl1 = new GraphLevel();
		
		Node[][] nodeArray = new Node[width][height];
		//generate nodes for each tile
		for(Rectangle room: rooms){
			Node zoneNode = gl1.addNode(room.x+room.width/2, room.y+room.height/2);
			for(int i = (int) room.x; i < room.x + room.width; i++){
				for(int k = (int) room.y; k < room.y + room.height; k++){
					if(!Tile.isSolid(map[k][i])){
						Node node = gl0.addNode(i + 0.5f, k + 0.5f);
						node.upNode = zoneNode;
						zoneNode.downNodes.add(node);
						nodeArray[i][k] = node;
					}
				}
			}
		}
		for(ArrayList<int[]> h: halls){
			Node zoneNode = gl1.addNode(h.get(h.size()/2)[0] + 0.5f, h.get(h.size()/2)[1] + 0.5f);
			for(int[] p: h){
				Node node = gl0.addNode(p[0] + 0.5f, p[1] + 0.5f);
				node.upNode = zoneNode;
				zoneNode.downNodes.add(node);
				nodeArray[p[0]][p[1] ] = node;
				
			}
			
		}
		
		for(int i = 0; i <  nodeArray.length; i++){
			for(int k = 0; k <  nodeArray[0].length; k++){
				Node n = nodeArray[i][k];
				if(n != null){
					if(i > 0 && nodeArray[i - 1][k] != null)n.addConnection(nodeArray[i - 1][k], 1);
					if(i < nodeArray.length - 1 && nodeArray[i + 1][k] != null)n.addConnection(nodeArray[i + 1][k], 1);
					if(k > 0 && nodeArray[i][k - 1] != null)n.addConnection(nodeArray[i][k - 1], 1);
					if(k < nodeArray[0].length - 1 && nodeArray[i][k + 1] != null)n.addConnection(nodeArray[i][k + 1], 1);
				}
			}
		}
		
		for(Node n1: gl1.nodes){
			for(Node n2: gl1.nodes){
				if(!n1.equals(n2) && n1.isAdjacentTo(n2)){
					n1.addConnection(n2, n1.findDistance(n2.x*Tile.TS, n2.y*Tile.TS));
				}
			}
		}
		for(Node n: gl1.nodes){
			n.setDownNode();
		}
		
		return new Graph(new GraphLevel[]{gl0, gl1});
	}
}
