package map;

public class generateQix extends generator {
  
	map floor; //Since we have multiple functions modifying the map, we need to store it outside of the generate method.
  
	public generateQix() {
	}
	
	/**
  *f=floor,w=wall
  */
	public void smooth(int f, int w) {
		int x,y;
		int n;
		for(x=1;x<floor.width-1;x++) {
			for(y=1;y<floor.height-1;y++) {
				if(floor.getTile(x,y)==f) {
					n=0;
					if(floor.getTile(x-1, y)==w) {
						n++;
					}
					if(floor.getTile(x+1, y)==w) {
						n++;
					}
					if(floor.getTile(x, y-1)==w) {
						n++;
					}
					if(floor.getTile(x, y+1)==w) {
						n++;
					}
					if(n>=3) {
						floor.setTile(x, y, w);
					}
				}
			}
		}
	}
  
	public void drawQixWall(Vector2 s, Vector2 d) {
		int x,y;
		int range;
		
		if(!this.floor.compareTile(s.x,s.y,Tiles.roomFloor.tileID)) {
			return;
		}
		range = rand.rand_range(5, 20);
		x=s.x;
		y=s.y;
		while(range>0) {
			this.floor.setTile(x, y, Tiles.roomWall.tileID);
			if(!this.floor.compareTile(x+d.y,y+d.x,Tiles.roomFloor.tileID)) {
				break;
			}
			if(!this.floor.compareTile(x-d.y,y-d.x,Tiles.roomFloor.tileID)) {
				break;
			}
			if(!this.floor.compareTile(x+d.x,y+d.y,Tiles.roomFloor.tileID)) {
				break;
			}
			x+=d.x;
			y+=d.y;
			range--;
		}
		
		if(range==0) {
			x-=d.x;
			y-=d.y;
			if(d.x!=0) {
				d.y=d.x;
				d.x=0;
			}else {
				d.x= -d.y;
				d.y=0;
			}
			x+=d.x;
			x+=d.y;
			this.drawQixWall(new Vector2(x,y), d);
		}
	}
	
	@Override
	public map generate(int depth, boolean stairs) {
		Vector2 c = new Vector2(0,0);
		Vector2 d = c;
		int i,x,y;
		int numwalls, tries, dir;
		
		generateCavern cave = new generateCavern();
		this.floor=cave.generate(depth, false);
		this.smooth(Tiles.stoneFloor.tileID, Tiles.stoneWall.tileID);
		
		for(i=0;i<floor.tiles.length;i++) {
			if(floor.tiles[i]==Tiles.stoneWall.tileID) {
				floor.tiles[i]=Tiles.roomWall.tileID;
			}else {
				floor.tiles[i]=Tiles.roomFloor.tileID;
			}
		}
		
		numwalls=rand.rand_range(17*30,28*30);
		while(numwalls-- >0) {
			tries=100;
			while(tries-- >0) {
				c.x=(byte) rand.rand_range(1,floor.width-1);
				c.y=(byte) rand.rand_range(1,floor.height-1);
				if(floor.getTile(c.x, c.y)!=Tiles.roomFloor.tileID) {
					continue;
				}
				if(floor.getTile(c.x, c.y+1)!=Tiles.roomFloor.tileID) {
					continue;
				}
				if(floor.getTile(c.x, c.y-1)!=Tiles.roomFloor.tileID) {
					continue;
				}
				if(floor.getTile(c.x+1, c.y)!=Tiles.roomFloor.tileID) {
					continue;
				}
				if(floor.getTile(c.x-1, c.y)!=Tiles.roomFloor.tileID) {
					continue;
				}
				break;
			}
			if(tries<=0) {
				break;
			}
			dir=rand.rand_choice(4);
			d = rand.getDirection(dir);
			drawQixWall(c,d);
			
			dir+=rand.rand_range(1, 3);
			dir &= 3;
			
			d=rand.getDirection(dir);
			drawQixWall(c.add(d),d);
		}
		
		int[] cutout = new int[floor.tiles.length/2];
		int[] cell = new int[floor.tiles.length];
		int numcuts=0,c1,c2;
		for(y=1;y<floor.height;y++) {
			for(x=1;x<floor.width;x++) {
				if(floor.getTile(x, y)!=Tiles.roomWall.tileID) {
					continue;
				}
				if((floor.getTile(x-1, y)==Tiles.roomFloor.tileID)&&(floor.getTile(x+1, y)==Tiles.roomFloor.tileID)&&(floor.getTile(x, y-1)==Tiles.roomWall.tileID)&&(floor.getTile(x, y+1)==Tiles.roomWall.tileID)) {
					cutout[numcuts++]=y*floor.width+x;
				}
				if((floor.getTile(x-1, y)==Tiles.roomWall.tileID)&&(floor.getTile(x+1, y)==Tiles.roomWall.tileID)&&(floor.getTile(x, y-1)==Tiles.roomFloor.tileID)&&(floor.getTile(x, y+1)==Tiles.roomFloor.tileID)) {
					cutout[numcuts++]=y*floor.width+x;
				}
			}
		}
		for(i=0;i<floor.tiles.length;i++) {
			cell[i]=i;
		}
		for(y=0;y<floor.height;y++) {
			for(x=0;x<floor.width;x++) {
				if(floor.getTile(x, y)!=Tiles.roomFloor.tileID) {
					continue;
				}
				c1 = findCell(cell,y*floor.width+x);
				
				if(floor.getTile(x+1,y)==Tiles.roomFloor.tileID) {
					c2=findCell(cell,y*floor.width+(x+1));
					if(c1<c2) {
						cell[c2]=c1;
					}else if(c1>c2) {
						cell[c1]=c2;
					}
				}
				if(floor.getTile(x, y+1)==Tiles.roomFloor.tileID) {
					c2=findCell(cell,(y+1)*floor.width+x);
					if(c1<c2) {
						cell[c2]=c1;
					}else if(c1>c2) {
						cell[c1]=c2;
					}
				}
			}
		}
		cutout = rand.rand_shuffle(cutout,numcuts);
		
		for(i=0;i<numcuts;i++) {
			x= cutout[i]%floor.width;
			y= cutout[i]/floor.width;
			
			if(floor.getTile(x+1, y)==Tiles.roomWall.tileID||floor.getTile(x-1, y)==Tiles.roomWall.tileID) {
				d.x=0;
				d.y=1;
			}
			if(floor.getTile(x, y+1)==Tiles.roomWall.tileID||floor.getTile(x, y-1)==Tiles.roomWall.tileID) {
				d.x=1;
				d.y=0;
			}
			c1=findCell(cell,(y+d.y)*floor.width+(x+d.x));
			c2=findCell(cell,(y-d.y)*floor.width+(x-d.x));
			if(c1!=c2) {
				floor.setTile(x, y, Tiles.door.tileID);
				if(c1<c2) {
					cell[c2]=c1;
				}else if(c1>c2) {
					cell[c1]=c2;
				}
				c1 = findCell(cell,y*floor.width+x);
				if(c1<c2){
					cell[c2]=c1;
				} else if(c1>c2) {
					cell[c1]=c2;
				}
			}
		}
		int c1cnt, c2cnt;
		c1cnt = 0;
		c1 = -1;
		for(y=0;y<floor.height;y++) {
			for(x=0;x<floor.width;x++) {
				if(floor.getTile(x,y)==Tiles.roomFloor.tileID||floor.getTile(x, y) == Tiles.door.tileID) {
					if(c1<0) {
						c1=findCell(cell,y*floor.width+x);
						c1cnt=0;
						for(i=0;i<1024;i++) {
							if(findCell(cell,i)==c1) {
								c1cnt++;
							}
						}
					}
					c2 = findCell(cell,y*floor.width+x);
					if(c1!=c2){
						if(c2==y*floor.width+x) {
							c2cnt=0;
							for(i=0;i<1024;i++) {
								if(findCell(cell,i)==c2) {
									c2cnt++;
								}
							}
							if(c2cnt>c1cnt) {
								c1=c2;
								c1cnt=c2cnt;
								y=0;
								x=0;
								continue;
							}
						}
						floor.setTile(x,y,Tiles.roomWall.tileID);
					}
				}
			}
		}
		if(stairs) {
			while(true) {
				x = rand.rand_range(1, floor.width);
				y = rand.rand_range(1, floor.height);
				
				if(floor.getTile(x, y)==Tiles.roomFloor.tileID) {
					floor.setTile(x, y, Tiles.stairsUp.tileID);
					animator.render(floor,"ladder");
					break;
				}
			}
			while(true) {
				x = rand.rand_range(1, floor.width);
				y = rand.rand_range(1, floor.height);
				
				if(floor.getTile(x, y)==Tiles.roomFloor.tileID) {
					floor.setTile(x, y, Tiles.stairsDown.tileID);
					animator.render(floor,"ladder");
					break;
				}
			}
		}
		
		return this.floor;
	}
}
