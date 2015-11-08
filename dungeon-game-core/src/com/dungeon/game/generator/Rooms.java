package com.dungeon.game.generator;

import java.util.ArrayList;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Rooms extends Generation {
	private ArrayList<Rectangle> rooms;
	private ArrayList<ArrayList<int[]>> halls;
	private ArrayList<Rectangle[]> hallEnds;
	
	public Rooms(int width, int height){
		super(width, height);
		rooms = new ArrayList<Rectangle>();
		halls = new ArrayList<ArrayList<int[]>>();
		hallEnds = new ArrayList<Rectangle[]>();
		int x = height/2;
		int y = width/2;
		generateStartRoom(x, y);
	}
	
	public boolean generateStartRoom(int x, int y){
		int height = (int) (3+Math.random()*map.length/7);
		y-=(int) (Math.random()*height);
		int width = (int) (3+Math.random()*map[0].length/7);
		x-=(int) (Math.random()*width);
		Rectangle room = new Rectangle(x,y,width,height);
		int nextX;
		int nextY;
		if(isValidRoom(room)){
			addRoomToMap(room);
			for(int i = 0; i<100;i++){
				int dir = (int) (Math.random()*4);
				if(dir == 0){
					nextX = (int) (x+width*Math.random());
					nextY = y;
					generateHallWay(nextX, nextY-1, dir, room);
				}else if(dir == 1){
					nextX = (int) (x+width*Math.random());
					nextY = y+height;
					generateHallWay(nextX, nextY, dir, room);
				}else if(dir == 2){
					nextX = x;
					nextY = (int) (y+height*Math.random());
					generateHallWay(nextX-1, nextY, dir, room);
				}else if(dir == 3){
					nextX = x+width;
					nextY = (int) (y+height*Math.random());
					generateHallWay(nextX, nextY, dir, room);
				}
			}
			return true;
		}
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
			hallEnds.add(new Rectangle[]{room, lastRoom});
			for(int i=0;i<hall.size();i++){
				map[hall.get(i)[1]][hall.get(i)[0]]=0;
				if(i==0)map[hall.get(i)[1]][hall.get(i)[0]]=3;
				if(i==hall.size()-1)map[hall.get(i)[1]][hall.get(i)[0]]=3;
			}
			addRoomToMap(room);
			for(int i = 0; i<100;i++){
				int dir = (int) (Math.random()*4);
				if(dir == 0){
					nextX = (int) (x+width*Math.random());
					nextY = y;
					generateHallWay(nextX, nextY-1, dir, room);
				}else if(dir == 1){
					nextX = (int) (x+width*Math.random());
					nextY = y+height;
					generateHallWay(nextX, nextY, dir, room);
				}else if(dir == 2){
					nextX = x;
					nextY = (int) (y+height*Math.random());
					generateHallWay(nextX-1, nextY, dir, room);
				}else if(dir == 3){
					nextX = x+width;
					nextY = (int) (y+height*Math.random());
					generateHallWay(nextX, nextY, dir, room);
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
			hallEnds.add(new Rectangle[]{room, lastRoom});
			for(int i=0;i<hall.size();i++){
				map[hall.get(i)[1]][hall.get(i)[0]]=0;
				if(i==0)map[hall.get(i)[1]][hall.get(i)[0]]=3;
				if(i==hall.size()-1)map[hall.get(i)[1]][hall.get(i)[0]]=3;
			}
			addRoomToMap(room);
			for(int i = 0; i<100;i++){
				int dir = (int) (Math.random()*4);
				if(dir == 0){
					nextX = (int) (x+width*Math.random());
					nextY = y;
					generateHallWay(nextX, nextY-1, dir, room);
				}else if(dir == 1){
					nextX = (int) (x+width*Math.random());
					nextY = y+height;
					generateHallWay(nextX, nextY, dir, room);
				}else if(dir == 2){
					nextX = x;
					nextY = (int) (y+height*Math.random());
					generateHallWay(nextX-1, nextY, dir, room);
				}else if(dir == 3){
					nextX = x+width;
					nextY = (int) (y+height*Math.random());
					generateHallWay(nextX, nextY, dir, room);
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
			hallEnds.add(new Rectangle[]{room, lastRoom});
			for(int i=0;i<hall.size();i++){
				map[hall.get(i)[1]][hall.get(i)[0]]=0;
				if(i==0)map[hall.get(i)[1]][hall.get(i)[0]]=3;
				if(i==hall.size()-1)map[hall.get(i)[1]][hall.get(i)[0]]=3;
			}
			addRoomToMap(room);
			for(int i = 0; i<100;i++){
				int dir = (int) (Math.random()*4);
				if(dir == 0){
					nextX = (int) (x+width*Math.random());
					nextY = y;
					generateHallWay(nextX, nextY-1, dir, room);
				}else if(dir == 1){
					nextX = (int) (x+width*Math.random());
					nextY = y+height;
					generateHallWay(nextX, nextY, dir, room);
				}else if(dir == 2){
					nextX = x;
					nextY = (int) (y+height*Math.random());
					generateHallWay(nextX-1, nextY, dir, room);
				}else if(dir == 3){
					nextX = x+width;
					nextY = (int) (y+height*Math.random());
					generateHallWay(nextX, nextY, dir, room);
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
			hallEnds.add(new Rectangle[]{room, lastRoom});
			for(int i=0;i<hall.size();i++){
				map[hall.get(i)[1]][hall.get(i)[0]]=0;
				if(i==0)map[hall.get(i)[1]][hall.get(i)[0]]=3;
				if(i==hall.size()-1)map[hall.get(i)[1]][hall.get(i)[0]]=3;
			}
			addRoomToMap(room);
			for(int i = 0; i<100;i++){
				int dir = (int) (Math.random()*4);
				if(dir == 0){
					nextX = (int) (x+width*Math.random());
					nextY = y;
					generateHallWay(nextX, nextY-1, dir, room);
				}else if(dir == 1){
					nextX = (int) (x+width*Math.random());
					nextY = y+height;
					generateHallWay(nextX, nextY, dir, room);
				}else if(dir == 2){
					nextX = x;
					nextY = (int) (y+height*Math.random());
					generateHallWay(nextX-1, nextY, dir, room);
				}else if(dir == 3){
					nextX = x+width;
					nextY = (int) (y+height*Math.random());
					generateHallWay(nextX, nextY, dir, room);
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
			} else{
				hallCoordinates.add(new int[]{x,y,dir});
				if(dir == 0)y--;
				if(dir == 1)y++;
				if(dir == 2)x--;
				if(dir == 3)x++;
				if(isTileInRoom(x,y, room)!=null){
					i=length;
					generateRoom = false;
					generateHall = true;
					hallDest = isTileInRoom(x,y,room);
					if(isHallTaken(room, hallDest))generateHall = false;
				}else{
					i=length;
					generateHall = false;
					generateRoom = false;
				}
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
		if(generateHall&&Math.random()>0.9&&hallCoordinates.size()>=5){
			hallEnds.add(new Rectangle[]{room,hallDest});
			halls.add(hallCoordinates);
			for(int i=0;i<hallCoordinates.size();i++){
				map[hallCoordinates.get(i)[1]][hallCoordinates.get(i)[0]]=0;
				if(i==0)map[hallCoordinates.get(i)[1]][hallCoordinates.get(i)[0]]=3;
				if(i==hallCoordinates.size()-1)map[hallCoordinates.get(i)[1]][hallCoordinates.get(i)[0]]=3;
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
		if(room.y+room.height>map.length-1)result = false;
		if(room.x+room.width>map[0].length-1)result = false;
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
		if(x<1||y<1||y>map.length-1||x>map[0].length-1)result = false;
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
	
	public boolean isHallTaken(Rectangle roomOne, Rectangle roomTwo) {
		for(Rectangle[] i: hallEnds){
			if(roomOne == i[0]&&roomTwo==i[1])return true;
			if(roomOne == i[1]&&roomTwo==i[0])return true;
		}
		return false;
	}
	
	public void addRoomToMap(Rectangle room){
		rooms.add(room);
		int x = (int) room.x;
		int y = (int) room.y;
		float width = room.width;
		float height = room.height;
		for(int i = 0; i<height; i++){
			for(int k = 0; k<width; k++){
				map[y][x]=1;
				x++;
			}
			y++;
			x-=width;
		}
	}
}
