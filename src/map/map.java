package map;

public class map {

	public int[] tiles;
	public int width;
	public int height;
	public int level;
	
	public map(int l) {
		this.level=l;
		width=32;
		height=32;
		this.tiles=new int[32*32];
	}
	
	public map(int w, int h, int l) {
		this.level=l;
		this.width=w;
		this.height=h;
		this.tiles=new int[h*w];
	}
	
	//We zero out the tilemap, filling it with stone wall tiles.
	public void clear() {
		//This looks ugly, I should add a getter that returns the array length.
		for(int i=0;i<this.tiles.length;i++) {
			this.tiles[i]=0;
		}
	}
	
	public boolean compareTile(int x, int y, int tile) {
		return (this.getTile(x, y)==tile);
	}
		
	public boolean isMapWall(int x, int y) {
		if(x<0||x>=this.width-1) {
			return true;
		}
		if(y<0||y>=this.height-1) {
			return true;
		}
		if(this.getTile(x, y)==Tiles.roomWall.tileID) {
			return true;
		}
		return false;
	}
	
	public void setTile(int x, int y, int t) {
		int off = y*width+x;
		if(0<=off&&off<tiles.length) {
			this.tiles[off]=t;
		}
	}
	
	public int getTile(int x, int y) {
		int off=y*width+x;
		if(0<=off&&off<tiles.length) {
			return this.tiles[off];
		}else {
			return 0;	//This just returns the tile ID for solid rock
		}
	}
}
