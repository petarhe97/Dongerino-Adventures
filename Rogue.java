/*
  Name: Rogue.java
  Author: Alexander Lu, Bob Du, Ian Hu, Peter He
  Date: Jan 9, 2015
  School: A. Y. Jackson S.S.
  Purpose: The Rogue class is a subclass of hero which defines a class of hero
            and has methods implemented differently than the other subclasses of hero
 */

public class Rogue extends Hero{

	//Constants
	private static final String LOWER_RANK_NAME = "Rogue";
	private static final String HIGHER_RANK_NAME = "Assassin";
	
	//Constructor
	public Rogue(String name, int health, int attack, int defense, int range, int movement, int level, int exp, boolean rankUp, Equipment loadOut){
		super(name, health, attack, defense, range, movement, level, exp, rankUp, loadOut);
		unitID = 3;
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
		
		double armorEnemy = unit.getDefense();
		
		int damage = (int)((attack*1.4 + loadout.getMainHand().use()*1.6)*(1-armorEnemy/(double)100));
		
		return damage;
	}
	
	public String toString(){
		return ("Rogue"+ "\r\n" +super.toString());
	}
}
