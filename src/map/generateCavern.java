package map;

public class generateCavern extends generator{
  boolean allowLakes;

	public generateCavern(boolean hasLakes) {
		this.allowLakes=hasLakes;
	}

	@Override
	public map generate(int depth, boolean stairs) {
		int rum,j;
		Vector2 pos = new Vector2(0,0);
		Vector2 dir = new Vector2(0,0);
		int numcuts =0;
		int tile = Tiles.stoneFloor.tileID; //Tiles.water.tileID : 
		
		map floor = new map(depth);
		
		rum=300;
		int ceiling = floor.height*floor.width/2;
		System.out.println(floor.height*floor.width/2);
		while(numcuts<(ceiling)) {
			if(numcuts>(ceiling/2)) {
				pos.x = rand.rand_range(3, floor.width);
				pos.y = rand.rand_range(3, floor.height);
				if(floor.isMapWall(pos.x, pos.y))
					continue;
				tile=Tiles.stoneFloor.tileID;
			} else {
				while(true) {
					//Pick a random drunk starting location.
					pos.x = (byte) rand.rand_range(3, floor.width);
					pos.y = (byte) rand.rand_range(3, floor.height);
					
					
					if(floor.isMapWall(pos.x,pos.y)) {
						continue;
					}
					if(floor.getTile(pos.x,pos.y)==Tiles.stoneWall.tileID) {
						break;
					}
				}
        if(allowLakes){
				  tile=Tiles.water.tileID;
				}
				//initial dig direction
			}
			dir = rand.rand_direction();
			for(j=0;j<rum;j++) {
				if(floor.getTile(pos.x, pos.y)==Tiles.stoneWall.tileID) {
					numcuts++;
				}
				if(floor.getTile(pos.x,pos.y)!=Tiles.stoneFloor.tileID &&floor.getTile(pos.x,pos.y)!=Tiles.stoneWall.tileID&&floor.getTile(pos.x,pos.y)!=Tiles.water.tileID) {
					break;
				}
				if(floor.getTile(pos.x, pos.y)!=Tiles.water.tileID) {
					floor.setTile(pos.x,pos.y,tile);
				}
				pos.x+=dir.x;
				pos.y+=dir.y;
				if(pos.x<1) {
					pos.x=1;
				}
				if(pos.x>=floor.width-2) {
					pos.x=(byte) (floor.width-2);
				}
				if(pos.y<1) {
					pos.y=1;
				}
				if(pos.y>=floor.width-2) {
					pos.y=(floor.height-2);
				}
				switch(rand.rand_choice(3)) {
					case 0:
						break;
					case 1:
						dir.turnleft();
						break;
					case 2:
						dir.turnRight();
						break;
				}
			}
			
		}
		
		if(stairs) {
			while(true) {
				pos.x = rand.rand_range(3, floor.width);
				pos.y = rand.rand_range(3, floor.height);
			
				if(floor.getTile(pos.x, pos.y)==Tiles.stoneFloor.tileID || floor.getTile(pos.x, pos.y)==Tiles.water.tileID) {
					floor.setTile(pos.x, pos.y, Tiles.stairsUp.tileID);
					break;
				}
			}
			while(true) {
				pos.x = rand.rand_range(3, floor.width);
				pos.y = rand.rand_range(3, floor.height);
			
				if(floor.getTile(pos.x, pos.y)==Tiles.stoneFloor.tileID || floor.getTile(pos.x, pos.y)==Tiles.water.tileID) {
					floor.setTile(pos.x, pos.y, Tiles.stairsDown.tileID);
					break;
				}
			}
		}
		
		return floor;
	}
}
