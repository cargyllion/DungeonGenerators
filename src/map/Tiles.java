package map;

public class tiles {

	public int tileID;
	public boolean isSolid;
	
	public tiles[] tileset = new tiles[256];
	
	public static tiles stoneWall = new tiles(0,true);
	public static tiles stoneFloor = new tiles(1,false);
	public static tiles roomWall = new tiles(2,true);
	public static tiles roomFloor=new tiles(3,false);
	public static tiles stairsDown = new tiles(4,false);
	public static tiles stairsUp = new tiles(5,false);
	public static tiles water = new tiles(6,false);
	public static tiles door = new tiles(7,true);
	
	public tiles(int id, boolean solid) {
		this.isSolid=solid;
		this.tileID=id;
		if(tileset[id]==null) {
			this.tileset[id]=this;
		}
	}
}
