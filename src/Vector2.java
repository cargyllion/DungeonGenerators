/**
* A custom Vector class I wrote to help handle directional data in the level generator, as well as just to clean things up in the generator code.
*/
public class Vector2 {

	public int x, y;
	
	public Vector2(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public Vector2(int[] in) {
		this.x=in[0];
		this.y=in[1];
	}
	
	public Vector2 add(Vector2 in) {
		this.x+=in.x;
		this.y+=in.y;
		return this;
	}
	
	public void turnleft() {
        byte a;
        byte b;
        if (this.x == 0 && this.y == 1) {
            a = -1;
            b = 0;
        }
        else if (this.x == 1 && this.y == 0) {
            a = 0;
            b = 1;
        }
        else if (this.x == 0 && this.y == -1) {
            a = 1;
            b = 0;
        }
        else {
            a = 0;
            b = -1;
        }
        this.x = a;
        this.y = b;
    }
    
    public void turnRight() {
        byte a;
        byte b;
        if (this.x == 0 && this.x == 1) {
            a = 1;
            b = 0;
        }
        else if (this.x == 1 && this.y == 0) {
            a = 0;
            b = -1;
        }
        else if (this.x == 0 && this.y == -1) {
            a = -1;
            b = 0;
        }
        else {
            a = 0;
            b = 1;
        }
        this.x = a;
        this.y = b;
    }
}
