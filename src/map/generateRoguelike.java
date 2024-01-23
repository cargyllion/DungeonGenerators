package map;

import java.util.ArrayList;
import java.util.List;

/**
* A level generator based on the classic dungeons generated in Rogue (1980)
**/
public class generateRoguelike extends generator{

	map floor;
	
	public generateRoguelike() {
		
	}

  /**
  * Pick where we'll start digging our corridor from, as well as what direction the door is facing.
  */
	public int[][] chooseRandomExit(Room room){
		int[][] exits = new int[2][2];
		
		switch(rand.rand_choice(4)) {
			case 0:
				exits[0][0]=room.minx;
				exits[0][1]=rand.rand_range(room.miny+1, room.maxy-1);
				exits[1][0]=-1;
				exits[1][1]=0;
				break;
			case 1:
				exits[0][0]=room.maxx;
				exits[0][1]=rand.rand_range(room.miny+1, room.maxy-1);
				exits[1][0]=1;
				exits[1][1]=0;
				break;
			case 2:
				exits[0][0]=rand.rand_range(room.minx+1, room.maxx-1);
				exits[0][1]=room.miny;
				exits[1][0]=0;
				exits[1][1]=-1;
				break;
			case 3:
				exits[0][0]=rand.rand_range(room.minx+1, room.maxx-1);
				exits[0][1]=room.maxy;
				exits[1][0]=0;
				exits[1][1]=1;
				break;
		}
		return blarg;
	}

  /**
  * Create a randomly-sized room. Rooms will generate with interior dimensions ranging from 2x2 tiles to 7x7.
  */
	public Room buildRandomRoom(Room room) {
		room.minx=rand.rand_range(1, floor.width-4);
		room.miny=rand.rand_range(1, floor.height-4);
		room.maxx=rand.rand_range(room.minx+3,Math.min(room.minx+8, floor.width-2));
		room.maxy=rand.rand_range(room.miny+3,Math.min(room.miny+8, floor.height-2));
		return room;
	}

  /**
  *Check and see if the current room can be placed in the dungeon without touching any existing structures.
  */
	private boolean checkRoomFits(Room room) {
		int x,y;
    //Check the room boundaries. We don't want to override any existing level data.
		for(y=room.miny;y<room.maxy;y++) {
			for(x=room.minx;x<room.maxx;x++) {
				if(floor.getTile(x, y)!=tiles.stoneWall.tileID) {
					return false;
				}
			}
		}
    //Check the room's immediate surroundings. We don't want to generate along the map edge or directly adjacent to an existing room. This looks ugly and causes problems with drawing the corridors.
		for(x=room.minx-1;x<room.maxx+1;x++) {
			if(floor.isMapWall(x,room.miny-1)) {
				return false;
			}
			if(floor.isMapWall(x,room.maxy+1)) {
				return false;
			}
		}
		for(y=room.miny-1;y<room.maxy+1;y++) {
			if(floor.isMapWall(room.minx-1,y)) {
				return false;
			}
			if(floor.isMapWall(room.maxx+1,y)) {
				return false;
			}
		}
		return true;
	}

	/**
  *Pretty straight forward: Place the room object onto the map by drawing a rectangle with the room's bounding box.
  */
	public void drawRoom(Room room) {
			for(int x=room.minx;x<=room.maxx;x++) {
				for(int y=room.miny;y<=room.maxy;y++) {
					if(x==room.minx||x==room.maxx||y==room.miny||y==room.maxy) {
						floor.setTile(x, y, tiles.roomWall.tileID);
						
					}else {
						floor.setTile(x, y, tiles.roomFloor.tileID);
					}
				}
			}
	}

  /**
  * We don't want to indiscriminately place floor tiles, so this function is used to place different path tiles based on what's currently occupying the space.
  */
	private void setPathTile(int x, int y) {
		int newtile, oldtile;
		oldtile = floor.getTile(x, y);
		newtile=oldtile;
		if(oldtile==tiles.stoneFloor.tileID) {
      //There's no reason to continue here.
			return;
		}else if(oldtile==tiles.stoneWall.tileID) {
      newtile = tiles.stoneFloor.tileID;
		} else if(oldtile==tiles.door.tileID||oldtile==tiles.roomWall.tileID) {
			newtile=tiles.door.tileID;
		}
		floor.setTile(x, y, newtile);
		
	}
	
	/**
  *Draw a corridor between the two XY pairs.
  */
	boolean findPath(int[] s, int[] e) {
		int[] d = new int[2];
		int i,dir,wallhit;
		int[] s=new int[2],e=new int[2],n=new int[2];
		int len=0;
		int ncnt=0;

		//Get the initial random direction
		dir=rand.rand_choice(2);
		setPathTile(s[0],s[1]);
		while(s[0]!=e[0]||s[1]!=e[1]) {
      //Find our current direction
			for(i=0;i<2;i++) {
				d[i]=Integer.signum(e[i]-s[i]);
			}
      //Switch direction (Why doesn't java let us use !Integer????)
			dir=(dir == 0? 1 : 0);

      //If we're facing that direction, go the opposite way
			if(d[dir]==0) {
				dir=(dir == 0? 1 : 0);
			}
			wallhit=0;

      //Move in the direction dir until a random chance fails, or we get aligned with a destination.
			while(true) {
				if(rand.rand_chance(40))
          //Random exit clause!
					break;

        // Alignment...  This only counts for straight moves, if bounced off a wall we don't want to trigger.
				if(wallhit==0&&s[dir]==e[dir])
					break;

        // Calculate the next pos...
				n[0]=s[0];
				n[1]=s[1];
				n[dir]+=d[dir];

        // Check if our current dig direction is valid...
				ncnt=0;
				while(floor.isMapWall(n[0],n[1])){
          // Not a valid dig direction!  Rotate 90 degrees and go in a random direction... As we never build walls adjacent and they are square, this (usually) guarantees a non-wall.
					dir=(dir == 0? 1 : 0);
					if(d[dir]==0) {
						d[dir]=rand.rand_sign();
					}
					n[0]=s[0];
					n[1]=s[1];
					n[dir]+=d[dir];
					wallhit=1;
					ncnt++;
					if(ncnt>100) {
            // This fails when we hit the bottom most wall, turn to the left, and then hit the left wall prior to deciding to reset
    		    // Note that alignment will never trigger as wallhit = true, and we could have been going the wrong way anyways.
						return false;
					}
				}
        // Move in the desired direction...
				s[dir]+=d[dir];
				
				setPathTile(s[0],s[1]);
				len++;
				if(len>100) {
					return false;
				}
			}
		}
		return true;
	}

	
	boolean drawCorridor(int[][] s, int[][] e) {
		boolean success;
		success=findPath(s,e);
		if(success) {
      // Draw our doors.  We only do this if our path creation was successful.
			setPathTile(s[0][0],s[0][1]);
			setPathTile(e[0][0],e[0][1]);
		}
		return success;
	}
	
	@Override
	public map generate(int depth, boolean stairs) {
		int numRooms =(int) (20);
		List<Room> rooms = new ArrayList<Room>();
		int numroom=0;
		int fails=0;
		int i;
		int src;
		int room,x,y;
		
		this.floor = new map(depth);
		floor.clear();

    // Build as many rooms as fits, up to twenty.
		while(fails<40&&numroom<numRooms) {
			
			rooms.add(buildRandomRoom(new Room()));
			
			if(checkRoomFits(rooms.get(numroom))) {
				drawRoom(rooms.get(numroom));
				numroom++;
			} else {
				rooms.remove(numroom);
				fails++;
			}
			
		}
		// Now, build corridors... We want every room to be connected. Thus, we have a pool of i connected rooms. We each round take i+1 and connect it to a room that's already been connected to the rest of the dungeon.
    // If we fail to connect 40 times, we declare this dungeon sucky and go again...
		fails=0;
		for(i=0;i<rooms.size()-1;i++) {
			int[][] vecs = new int[2][2];
			int[][] vecs2= new int[2][2];
			src=rand.rand_range(0,i);
			
			vecs = chooseRandomExit(rooms.get(src));
			vecs2 = chooseRandomExit(rooms.get(i+1));

      //Draw a corridor between these two rooms.
			if(!drawCorridor(vecs,vecs2)) {
        //Failed to draw a corridor, try again.
				i--;
				fails++;
				if(fails>40) {
					break;
				}
				continue;
			}
		}
		if(fails>40) {
			System.out.println("We messed that one up, trying again!");
			return this.generate(depth,stairs);
		}

		// Now, we pick a random up and down stair case
		if(stairs) {
			while(true) {
				room=rand.rand_choice(rooms.size());
				Room rm = rooms.get(room);
				x=rand.rand_range(rm.minx+1, rm.maxx-1);
				y=rand.rand_range(rm.miny+1, rm.maxy-1);
				if(floor.getTile(x, y)==tiles.roomFloor.tileID) {
					floor.setTile(x,y,tiles.stairsDown.tileID);
					break;
				}
			}
			while(true) {
				room=rand.rand_choice(rooms.size());
				Room rm = rooms.get(room);
				x=rand.rand_range(rm.minx+1, rm.maxx-1);
				y=rand.rand_range(rm.miny+1, rm.maxy-1);
				if(floor.getTile(x, y)==tiles.roomFloor.tileID) {
					floor.setTile(x,y,tiles.stairsUp.tileID);
					break;
				}
			}
		}
		return floor;
	}
}
