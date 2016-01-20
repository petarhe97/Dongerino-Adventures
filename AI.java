/*
 Name: AI.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The AI class is responsible for issuing commands for the enemy units. 
 It goes through each enemy unit and calculate the best command to issue for that unit.
*/

import java.util.Arrays;

public class AI {
	//fields
	private int aggression;
	private Unit focusedHero;
	private Unit[] enemy;
	private Unit[] hero;
	private int enemyNum;
	private Unit[] attackCommands;
	private int[] attackCommandsDamage;
	
	private Coordinate[][] moveCommands;
	
	//class constant
	private static final int FIND_WEAKEST_HERO_REQ = 14;
	
	//constructor 
	public AI (Unit[] enemy, Unit[] hero, int aggression, Unit focusedHero/*,GUI gui*/) {
		this.aggression = aggression;
		this.focusedHero = focusedHero;
		this.enemy = enemy;
		this.hero = hero;
		//this.gui = gui;
	}
	
	//play is called for each AI round and calls issueCommand for each individual Enemy
	public void play() {
//		int temp = gui.unitSelected;
//		for (int i = 0; i < gui.numHeroes + gui.numEnemies; i++) {
//			System.out.println(i);
//			System.out.println(gui.units[i].toString());
//			System.out.println();
//		}
		moveCommands = new Coordinate[enemy.length][1];
		attackCommands = new Unit[enemy.length];
		attackCommandsDamage = new int[enemy.length];
		
		for (int i = 0; i < enemy.length; i++) {
			if (enemy[i].isAlive()){
				enemyNum = i;
				//gui.unitSelected = i + gui.numHeroes;
				//System.out.println(gui.unitSelected);
				issueCommand (enemy[i]);
			}
		}
		//gui.unitSelected = temp;
	}
	
	
	//issueCommand takes in an Enemy and issues commands for that enemy
	public void issueCommand (Unit target) {
		//calculate the total aggression level of the unit which is based on the aggression level of the AI and the unit itself
		int aggro = aggression + ((Enemy)target).getAggressionLevel() * 2;
		Unit targetHero;
		
		//if the aggressiveness of the unit if higher than the requirement, the weakest hero nearby will be found instead of the closest hero.
		//Note in both cases, if the focused hero is in range, the focused hero will be returned no matter which method is used to find hero.
		if (aggro >= FIND_WEAKEST_HERO_REQ) {
			targetHero = findWeakestHero (target);
		} else {
			//else the closest hero will be selected
			targetHero = findNearbyHero (target);
		}
		
		//if a Hero is found, then the enemy will move towards it and attack if possible
		if (targetHero != null) {
			//determine the path the unit has to take to move to the target
			Coordinate[] path = findShortestPath(target.getCoordinate(), targetHero.getCoordinate(), target.getTerrainPassing());
			
			//System.out.println(target.getMovement() + " " +  target.getRange() + " " + path.length);
			//if the target can move into attack range, then the target will move there and attack
			if (target.getMovement() + target.getRange() - 1>= path.length) {
				
				attackCommandsDamage[enemyNum] = target.calculateDamage(targetHero);
				
				if (path.length > 0){
					target.setCoordinate(path[target.getRange()-1]);
				}
				
				if (target.getMovement() > 0) {
					moveCommands[enemyNum] = Arrays.copyOfRange(path, target.getRange()-1, path.length);
					
					Coordinate[] unitPositions = new Coordinate[hero.length + enemy.length];
					//Initializing the unit positions
					for (int i = 0; i < hero.length; i++){
						if (hero[i].isAlive()){
							unitPositions[i] = hero[i].getCoordinate();
						} else {
							unitPositions[i] = null;
						}
					}
				
					for (int i = 0; i < enemy.length; i++){
						if (enemy[i].isAlive()){
							if (enemy[i] == target) {
								unitPositions[i+hero.length] = target.getCoordinate();
							} else {
								unitPositions[i + hero.length] = enemy[i].getCoordinate();
							}
						}else{
							unitPositions[i + hero.length] = null;
						}
					}
					Algorithm.setUnitPositions(unitPositions);
				}
//				if (movePath != null) {
//					gui.setPath(movePath);
//				}
				target.attack(targetHero);
				attackCommands[enemyNum] = targetHero;
			} else {
				//else the target will just move towards the hero
//				System.out.println("MOVE");
//				for (int i = path.length - 1; i >= 0; i--) {
//					System.out.println(path[i]);
//				}
//				System.out.println(path.length + " " + target.getMovement());
//				System.out.println(path[path.length - target.getMovement()]);
//				System.out.println(targetHero.getCoordinate());
//				System.out.println(target.getCoordinate());
				if (target.getMovement()>0) {
					target.setCoordinate(path[path.length - target.getMovement()]);
				
//				System.out.println(target.getCoordinate());
				
				moveCommands[enemyNum] = Arrays.copyOfRange(path, path.length-target.getMovement(), path.length-1);
				Coordinate[] unitPositions = new Coordinate[hero.length + enemy.length];
				
				//Initializing the unit positions
				for (int i = 0; i < hero.length; i++){
					if (hero[i].isAlive()){
						unitPositions[i] = hero[i].getCoordinate();
					} else {
						unitPositions[i] = null;
					}
				}
			
				for (int i = 0; i < enemy.length; i++){
					if (enemy[i].isAlive()){
						if (enemy[i] == target) {
							unitPositions[i + hero.length] = target.getCoordinate();
						} else {
							unitPositions[i + hero.length] = enemy[i].getCoordinate();
						}
					}else{
						unitPositions[i + hero.length] = null;
					}
				}
				Algorithm.setUnitPositions(unitPositions);
				
//				Coordinate[] movePath = target.move(path[target.getMovement()-1]);
//				if (movePath != null) {
//					gui.setPath(movePath);
//				}
				}
			}
		}	
	}
	
	//findNearbyHero takes in an Enemy and finds the closest Hero to it. If no heroes are found, then void is returned. 
	//If the focused hero is also in range, the focused hero will be returned.
	public Unit findNearbyHero (Unit target) {
		//System.out.println("Closest");
		//calculate the search radius which is based on the aggression level
		int radius = target.getRange() + aggression + ((Enemy)target).getAggressionLevel();
		Unit closest = null;
		
		//go through the list to find the closest hero
		for (int i = 0; i < hero.length; i++) {
			if (hero[i].isAlive()){
				if (findNearbyDistance(target.getCoordinate(),hero[i].getCoordinate(), target.getTerrainPassing()) <= radius) {
					if (closest == null) {
						closest = hero[i];
					} else if (findNearbyDistance(target.getCoordinate(),hero[i].getCoordinate(), target.getTerrainPassing()) < findNearbyDistance(closest.getCoordinate(), hero[i].getCoordinate(), target.getTerrainPassing())) {
						closest = hero[i];
					}
				}
			}
		}
		
		//check if the focused hero is within the radius, if it is, that hero will be returned
		if (focusedHero != null) {
			if (focusedHero.isAlive() && findNearbyDistance(target.getCoordinate(),focusedHero.getCoordinate(), target.getTerrainPassing()) <= target.getRange() + target.getMovement()) {
				return focusedHero;
			}
		}
		//System.out.println(closest);
		return closest;
		
	}
	
	//findWeakestHero takes in an Enemy and finds the weakest Hero in the range. If no heroes are found, then void is returned
	//If the focused hero is also in range, the focused hero will be returned.
	public Unit findWeakestHero (Unit target) {
		//System.out.println("Weakest");
		//calculate the search radius which is based on the aggression level
		int radius = target.getRange() + target.getMovement();
		Unit weakest = null;
		
		//go through the list to find the weakest hero
		for (int i = 0; i < hero.length; i++) {		
			
			//System.out.println(findNearbyDistance(target.getCoordinate(),hero[i].getCoordinate(), target.getTerrainPassing()));
			if (hero[i].isAlive()){
				if (findNearbyDistance(target.getCoordinate(),hero[i].getCoordinate(), target.getTerrainPassing()) <= radius) {
					if (weakest == null) {
						weakest = hero[i];
					} else if (hero[i].getHealth() < weakest.getHealth()) {
						weakest = hero[i];
					}
				}
			}
		}
		
		//check if the focused hero is within the radius, if it is, that hero will be returned
		if (focusedHero != null) {
			if (focusedHero.isAlive() && findNearbyDistance(target.getCoordinate(),focusedHero.getCoordinate(), target.getTerrainPassing()) <= radius) {
				return focusedHero;
			}
		}
		
		if (weakest != null) {
			//System.out.println(weakest);
			return weakest;
		} else {
			return findNearbyHero (target);
		}
		
	}
	
	//findNearbyDistance returns the minimum distance out of the distance to the surrounding tiles of a tile
	public int findNearbyDistance (Coordinate currentCor, Coordinate heroCor, boolean terrainPassing) {
		Coordinate targetCor = new Coordinate(heroCor);
		
		targetCor.moveDown();
		int distance = Algorithm.distance(currentCor, targetCor, terrainPassing);
		//System.out.println(targetCor);
		targetCor.moveUp();
		
		//System.out.println(distance);
		
		targetCor.moveUp();
		distance = Math.min(distance, Algorithm.distance(currentCor, targetCor, terrainPassing));
		//System.out.println(targetCor);
		targetCor.moveDown();
		
		//System.out.println(distance);
		
		targetCor.moveLeft();
		distance = Math.min(distance, Algorithm.distance(currentCor, targetCor, terrainPassing));
		//System.out.println(targetCor);
		targetCor.moveRight();
		
		//System.out.println(distance);
		
		targetCor.moveRight();
		distance = Math.min(distance, Algorithm.distance(currentCor, targetCor, terrainPassing));
		//System.out.println(targetCor);
		targetCor.moveLeft();
		
		//System.out.println(distance);
		
		return distance;
	}
	
	//findNearbyShortest returns the shortest path to go to a surrounding tile of a tile
		public Coordinate[] findShortestPath (Coordinate currentCor, Coordinate heroCor, boolean terrainPassing) {
			Coordinate targetCor = new Coordinate(heroCor);
			
			Coordinate shortest;
			
			targetCor.moveDown();
			int distance = Algorithm.distance(currentCor, targetCor, terrainPassing);
			shortest = new Coordinate (targetCor);
			targetCor.moveUp();
			
			targetCor.moveUp();
			if (Algorithm.distance(currentCor, targetCor, terrainPassing) < distance) {
				distance = Algorithm.distance(currentCor, targetCor, terrainPassing);
				shortest = new Coordinate (targetCor);
			}
			targetCor.moveDown();
			
			targetCor.moveLeft();
			if (Algorithm.distance(currentCor, targetCor, terrainPassing) < distance) {
				distance = Algorithm.distance(currentCor, targetCor, terrainPassing);
				shortest = new Coordinate (targetCor);
			}
			targetCor.moveRight();
			
			targetCor.moveRight();
			if (Algorithm.distance(currentCor, targetCor, terrainPassing) < distance) {
				distance = Algorithm.distance(currentCor, targetCor, terrainPassing);
				shortest = new Coordinate (targetCor);
			}
			targetCor.moveLeft();
			
			return Algorithm.shortestPath(currentCor, shortest, terrainPassing);
			
			
		}
	
	//accessor methods
	public int getAggression() {
		return aggression;
	}
	
	public Unit getFocusedHero() {
		return focusedHero;
	}
	
	//mutator methods
	public void setAggression (int newAggression) {
		aggression = newAggression;
	}
	
	public void setFocusedHero (Unit newHero) {
		focusedHero = newHero;
	}
	
	public Coordinate[][] getMoveCommands(){
		return moveCommands;
	}
	
	public Unit getAttackCommands(int index){
		return attackCommands[index];
	}
	
	public int getAttackCommandsDamage(int index){
		return attackCommandsDamage[index];
	}
	
	//toString returns the string representation of the class
	public String toString() {
		String str = "Aggression: " + aggression + "\r\nCurrently Focused Hero: ";
		if (focusedHero != null) {
			str += "\r\n" + focusedHero.toString();
		} else {
			str += "None";
		}
		return str;
	}
	
	
}
