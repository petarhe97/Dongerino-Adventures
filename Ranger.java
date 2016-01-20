/*
  Name: Ranger.java
  Author: Alexander Lu, Bob Du, Ian Hu, Peter He
  Date: Jan 9, 2015
  School: A. Y. Jackson S.S.
  Purpose: The Ranger class is a subclass of hero which defines a class of hero
            and has methods implemented differently than the other subclasses of hero
 */

public class Ranger extends Hero{

	//Constants
	private static final String LOWER_RANK_NAME = "Ranger";
	private static final String HIGHER_RANK_NAME = "Bowmaster";
	
	//Constructor
	public Ranger(String name, int health, int attack, int defense, int range, int movement, int level, int exp, boolean rankUp, Equipment loadOut){
		super(name, health, attack, defense, range, movement, level, exp, rankUp, loadOut);
		unitID = 2;
		terrainPassing = false;
	}
	
	//This method checks if the implicit object has ranked up, and returns the appropriate designation
	public String checkRank(){
		if (rankUp){
			return HIGHER_RANK_NAME;
		}else{
			return LOWER_RANK_NAME;
		}
	}
	
	//This method calculates the damage that the implicit object will deal to the explicit object
	public int calculateDamage(Unit unit){
		Coordinate[] temp = Algorithm.getUnitPositions();
		
		Coordinate[] tempPositions = new Coordinate[0];
		
		Algorithm.setUnitPositions(tempPositions);
		
		int distance = Algorithm.distance(curCoordinate, unit.getCoordinate(), true);
		Algorithm.setUnitPositions(temp);
		
		int totalAttack = attack + loadout.getMainHand().use();
		double armorEnemy = unit.getDefense();
		double damage = ((totalAttack*1.3*((double)distance/range))*(1-armorEnemy / 100.0));
		
		return (int)damage;
	}
	
	public String toString(){
		return ("Ranger"+ "\r\n" +super.toString());
	}
}
