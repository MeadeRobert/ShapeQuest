import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Shape {

	//class specific random
	private Random r = new Random();
	
	//shape variables
	private int shapex;
	private int shapey;
	private int sides;
	private int color = 1;
	private int style;
	private int size = 128;
	private int shapeheight;
	private int shapewidth;
	
	//other variables
	private Main m;

	
	
	public Shape(int sides, int color, int style, int shapex, int shapey, Main m) {
		this.sides = sides;
		this.color = color;
		this.style = style;
		this.setM(m);
		this.shapex = shapex;
		this.shapey = shapey;
	}

	public void paint(Graphics g) {
		// declare array variables for points on polygons

		// TODO implement size control and color switch case
		switch (this.color) {
		case 1:
			g.setColor(Color.white);
			break;
		case 2:
			g.setColor(Color.red);
			break;
		case 3:
			g.setColor(Color.pink);
			break;
		case 4:
			g.setColor(Color.yellow);
			break;
		case 5:
			g.setColor(Color.green);
			break;
		case 6:
			g.setColor(Color.cyan);
			break;
		case 7:
			g.setColor(Color.blue);
			break;
		case 8:
			g.setColor(Color.magenta);
			break;
		}

		switch (this.sides) {
			case 1:
				// draw a circle
				if (style == 1) {
					g.drawOval(shapex, shapey, this.size, this.size);
				} else {
					g.fillOval(shapex, shapey, this.size, this.size);
				}
			case 3:
				//declare variables needed for ****Polygon() methods
				int xpoints[] = new int[3];
				int ypoints[] = new int[3];
				shapewidth = this.size;
	
				//algorithm for height of an equilateral triangle
				shapeheight = (int) Math.sqrt(shapewidth * shapewidth
						- shapewidth / 2 * shapewidth / 2);
	
				// setup triangle points
				xpoints[0] = shapex;
				ypoints[0] = shapey - shapeheight;
				xpoints[1] = shapex + shapewidth / 2;
				ypoints[1] = shapey;
				xpoints[2] = shapex - shapewidth / 2;
				ypoints[2] = shapey;
	
				// draw the triangle
				if (this.style == 1) {
					g.drawPolygon(xpoints, ypoints, sides);
				} else {
					g.fillPolygon(xpoints, ypoints, sides);
				}
				break;
		}
	}

	//getters and setters; "these are not the droids you're looking for"
	public void update(Main m) {
		shapex++;
	}

	public int getShapeX() {
		return shapex;
	}
	
	public int getShapeY() {
		return shapey;
	}
	
	public void setShapeX(int shapex){
		this.shapex = shapex;
	}
	
	public void setShapeY(int shapey){
		this.shapey = shapey;
	}
	
	public int getSides(){
		return sides;
	}
	
	public int getSize(){
		return size;
	}
	
	public int getShapeHeight(){
		return shapeheight;
	}
	
	public int getShapeWidth(){
		return shapewidth;
	}
	
	public int getStyle(){
		return style;
	}
	
	public int getColor(){
		return color;
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public void setSides(int sides){
		this.sides = sides;
	}
	
	public void setStyle(int style){
		this.style = style;
	}

	public Random getR() {
		return r;
	}

	public void setR(Random r) {
		this.r = r;
	}

	public Main getM() {
		return m;
	}

	public void setM(Main m) {
		this.m = m;
	}
	
}

