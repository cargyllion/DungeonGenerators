package map;

public class Tiles {

	public int tileID;
	public boolean isSolid;
	
	public Tiles[] tileset = new Tiles[256];
	
	public static Tiles stoneWall = new Tiles(0,true);
	public static Tiles stoneFloor = new Tiles(1,false);
	public static Tiles roomWall = new Tiles(2,true);
	public static Tiles roomFloor=new Tiles(3,false);
	public static Tiles stairsDown = new Tiles(4,false);
	public static Tiles stairsUp = new Tiles(5,false);
	public static Tiles water = new Tiles(6,false);
	public static Tiles door = new Tiles(7,true);
	
	public Tiles(int id, boolean solid) {
		this.isSolid=solid;
		this.tileID=id;
		if(tileset[id]==null) {
			this.tileset[id]=this;
		}
	}
}
