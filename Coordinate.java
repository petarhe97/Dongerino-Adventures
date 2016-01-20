/*
 Name: Coordinate.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The Coordinate class contains the information of a coordinate, which is used to identify the location of Units.
*/

public class Coordinate{
	
	//Fields
	private int x;
	private int y;
	
	//Constructor
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Coordinate (Coordinate c) {
		x = c.x;
		y = c.y;
	}
	
	//Accessors
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	//Mutator
	public void setCoordinate(Coordinate newCoordinates){
		this.x = newCoordinates.x;
		this.y = newCoordinates.y;
	}
	
	//These methods move the unit, depending on the user input
	public void moveUp(){
		y ++;
	}
	
	public void moveDown(){
		y --;
	}
	
	public void moveLeft(){
		x --;
	}
	
	public void moveRight(){
		x ++;
	}
	
	//This method compares the coordinates of the implicit object to that of the explicit object
	public boolean compareToCoordinates(Coordinate other){
		return (this.getX() == other.getX() && this.getY() == other.getY());
	}
	
	//This method compares the coordinates of an array of coordinates with the implicit object, plus an offset
	//Note: This method is mostly used to see if a unit is 1 tile away from another unit, and is primarily meant to
	//be used in the path finding algorithm
	public boolean compareToListOfCoordinates(Coordinate[] other, int addX, int addY){

		for (int i = 0; i < other.length; i++){
			if (other[i] != null && x + addX == other[i].x && y + addY == other[i].y){
				return false;
			}
		}
		return true;
	}
	
	//This method returns the information of the coordinate in a String form
	public String toString(){
		return x + " " + y;
	}
}