package map;

public abstract class generator {

  public generator() {
		
  }
  /**
  *An algorithm for finding the root parent of of element 'c' in the cells array. Useful for figuring out if two tiles are contiguous (directly touching)
  **/
	public int findCell(int[] cells, int c) {
		int n;
		n=cells[c];
		if(n==c) {
			return c;
		}
		n=findCell(cells,n);
		cells[c]=n;
		return n;
	}
	
	public abstract map generate(int depth, boolean stairs);
}
