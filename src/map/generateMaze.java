package map;

public class generateMaze extends generator{
	public generateMaze() {
		super();
	}
	
	@Override
	public map generate(int depth, boolean stairs) {
		map floor = new map(depth);
		int[] cutouts;
		int[] cell;
		int numcuts;
		int x,y,cx,cy,dx,dy;
		int i;
		int cut;
		int c1,c2;
		int stride = rand.rand_choice(2)+1;
		
		cutouts = new int[512];
		cell = new int[1024];
		
		for(i=0;i<floor.tiles.length;i++) {
			floor.tiles[i]=Tiles.roomWall.tileID;
		}
		
		for(y=1;y<floor.height-stride;y+=stride+1) {
			for(x=1;x<floor.width-stride;x+=stride+1) {
				for(dy=0;dy<stride;dy++) {
					for(dx=0;dx<stride;dx++) {
						floor.setTile(x+dx, y+dy, Tiles.roomFloor.tileID);
					}
				}
			}
		}
		numcuts=0;
		for(y=1;y<floor.height-stride;y+=stride+1) {
			for(x=1;x<floor.width-stride;x+=stride+1) {
				for(dx=0;dx<stride;dx++) {
					if(x+stride<floor.width-2) {
						cutouts[numcuts++]=((y+dx)*floor.width)+x+stride;
					}
					if(y+stride<floor.height-2) {
						cutouts[numcuts++]=((y+stride)*floor.width)+x+dx;
					}
				}
			}
		}
		
		int[] fixCuts=new int[numcuts];
		
		for(i=0;i<numcuts;i++) {
			fixCuts[i]=cutouts[i];
		}
		
		for(i=0;i<256;i++) {
			cell[i]=i;
		}
		fixCuts = rand.rand_shuffle(cutouts, numcuts);
		for(i=0;i<numcuts;i++) {
			cut=cutouts[i];
			
			y=(byte) (cut/floor.width);
			x=(byte) (cut-y*floor.width);
			cx=(byte) ((x-1)/(stride+1));
			cy=(byte) ((y-1)/(stride+1));
			c1=findCell(cell,cy*(floor.width/2)+cx);
			if((cut&1)!=0) {
				c2=findCell(cell,(cy+1)*(floor.width/2)+cx);
			} else {
				c2=findCell(cell,cy*(floor.width/2)+cx+1);
			}
			
			if(c1!=c2||rand.rand_chance(10)) {
				floor.setTile(x, y, Tiles.roomFloor.tileID);
				cell[c2]=c1;
			}
		}
		
		if(stairs) {
			while(true) {
				x = rand.rand_range(1, floor.width);
				y = rand.rand_range(1, floor.height);
				
				if(floor.getTile(x, y)==Tiles.roomFloor.tileID) {
					floor.setTile(x, y, Tiles.stairsUp.tileID);
					break;
				}
			}
			while(true) {
				x = rand.rand_range(1, floor.width);
				y = rand.rand_range(1, floor.height);
				
				if(floor.getTile(x, y)==Tiles.roomFloor.tileID) {
					floor.setTile(x, y, Tiles.stairsDown.tileID);
					break;
				}
			}
		}
		return floor;
	}
}
