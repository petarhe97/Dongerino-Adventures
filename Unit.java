/*
  Name: Unit.java
  Author: Alexander Lu, Bob Du, Ian Hu, Peter He
  Date: Jan 9, 2015
  School: A. Y. Jackson S.S.
  Purpose: The Unit class contains the fields that are crucial to defining
           a unit in the game and basic methods and abstract method that defines
           the basic actions a unit is able to do
 */

import java.util.*;


public abstract class Unit {
	
	//Fields
	protected int health;
	protected int maxHP;
	protected int attack;
	protected int defense;
	protected int movement;
	protected int range;
	protected boolean moveAction;
	protected boolean otherAction;
	protected int unitID;
	protected boolean defend;
	
	protected boolean terrainPassing;
	
	protected Coordinate curCoordinate;
	
	//Class Constants
	private static final double DEFEND_BONUS = 2;
	
	//Constructor
	public Unit (int health, int attack, int defense, int range, int movement){
		this.health = health;
		this.attack = attack;
		this.maxHP = health;
		this.defense = defense;
		this.range = range;
		this.movement = movement;
		reset();
	}
	
	//This method, calculateDamage, returns the amount of damage dealt to another unit. It is abstract as
	//calculation vary between different classes
	public abstract int calculateDamage(Unit unit);
	
	//This method, deductHealth, deducts the health of the implicit object, and returns a boolean to indicate
	//whether the unit dies because of its loss in health
	public boolean deductHealth(int damage){
		health -= damage;
		
		return health <= 0;
	}
	
	//Accessors and mutators
	public int getHealth(){
		return health;
	}
	
	public int getMaxHP(){
		return maxHP;
	}
	
	public int getAttack(){
		return attack;
	}
	
	public int getDefense(){
		return defense;
	}
	
	public int getMovement(){
		return movement;
	}
	
	public int getRange(){
		return range;
	}
	
	public int getUnitID(){
		return unitID;
	}
	
	public boolean getMoveAction(){
		return moveAction;
	}
	
	public boolean getOtherAction(){
		return otherAction;
	}
	
	public boolean getTerrainPassing(){
		return terrainPassing;
	}
	
	public Coordinate getCoordinate(){
		return curCoordinate;
	}
	
	public void setCoordinate(Coordinate newCoordinate){
		curCoordinate = newCoordinate;
	}
	
	public void setMoveAction(boolean moveAction){
		this.moveAction = moveAction;
	}
	
	public void setOtherAction(boolean otherAction){
		this.otherAction = otherAction;
	}
	
	public void setTerrainPassing(boolean terrainPassing){
		this.terrainPassing = terrainPassing;
	}
	
	public void setRange(int range){
		this.range = range;
	}
	
	public void setMovement(int movement){
		this.movement = movement;
	}
	
	public void setDefense(int defense){
		this.defense = defense;
	}
	
	public void setAttack(int attack){
		this.attack = attack;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	//isAlive returns true if the Unit is still alive and false if otherwise
	public boolean isAlive() {
		return health > 0;
	}
	
	//kill sets the coordinate of the unit to null so it is no longer in the game
	public void kill () {
		curCoordinate = null;
	}
	
	//defend is called when the user commands the unit to defend, the unit will receive a temporary defense bonus until the start of the next turn
	public void defend () {
		otherAction = false;
		moveAction = false;
		defend = true;
		defense *= DEFEND_BONUS;
	}
	
	//reset is called at the beginning of a turn to reset moveAction and otherAction to true and reset the defend effects
	public void reset() {
		moveAction = true;
		otherAction = true;
		if (defend) {
			defend = false;
			defense /= DEFEND_BONUS;
		}
	}
	
	//This method resets the HP of the unit
	//Note: This method is meant to be used for transitioning from one level to another
	public void resetHP(){
		health = maxHP;
	}
	
	//This method takes in a Map and an array of Coordinates to determine the possible moves that the implicit object can move to
	public Coordinate[] getMovementArea(Map map, Coordinate[] unitPositions){
		
		//Initialize the distance to get to each coordinate on the map
		int[][] graph = Algorithm.dijkstra(curCoordinate, terrainPassing);
		
		List<Coordinate> possible = new ArrayList<Coordinate>();
		
		//Loop through the graph and determine which coordinates can be reached with the unit's movement range
		for (int i = 0; i < graph.length; i++){
			for (int q = 0; q < graph[0].length; q++){
				if(graph[i][q] <= movement && graph[i][q] != 0){
					possible.add(new Coordinate(i, q));
				}
			}
		}
		
		//Return the array of coordinates
		Coordinate[] paths = new Coordinate[possible.size()];
		return possible.toArray(paths);
	}
	
	//This method moves the unit to the designated coordinate and returns its path
	public Coordinate[] move(Coordinate target){
		
		int dist = Algorithm.distance(curCoordinate, target, terrainPassing);
		
		//If the distance to get to the target is less than the movement required, then the unit will be moved to that position
		if (dist <= movement && dist > 0){
			Coordinate[] path = Algorithm.shortestPath(curCoordinate, target, terrainPassing);
			moveAction = false;;
			return path;
		}else{
			return null;
		}
	}
	
	//the attack method is defined differently for hero and enemy.
	//a hero will gain exp after attacking while the enemy will not
	public abstract boolean attack(Unit target);
	
	@Override
	//toString method to help with file output and saving
	public String toString(){
		return maxHP + "\r\n" + attack + "\r\n" + defense + "\r\n" + movement + "\r\n" + range;
	}
	
	//This method takes in a boolean and returns an integer representation of that boolean
	//For example: 1 = true, 0 = false
	//This method was made to simplify file output
	protected int boolToInt(boolean bool){
		if (bool){
			return 1;
		}else{
			return 0;
		}
	}
}
