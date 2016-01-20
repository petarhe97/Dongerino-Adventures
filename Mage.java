/*
  Name: Mage.java
  Author: Alexander Lu, Bob Du, Ian Hu, Peter He
  Date: Jan 9, 2015
  School: A. Y. Jackson S.S.
  Purpose: The Mage class is a subclass of hero which defines a class of hero
            and has methods implemented differently than the other subclasses of hero
 */

public class Mage extends Hero{
	
	//Constants
	private static final String LOWER_RANK_NAME = "Mage";
	private static final String HIGHER_RANK_NAME = "ArchMage";
	
	//Constructor
	public Mage(String name, int health, int attack, int defense, int range, int movement, int level, int exp, boolean rankUp, Equipment loadOut){
		super(name, health, attack, defense, range, movement, level, exp, rankUp, loadOut);
		unitID = 1;
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
	
	//NOTE: REMEMBER TO CHANGE THIS
	//This method calculates the damage that the implicit object will deal to the explicit object
	public int calculateDamage(Unit unit){
		
	int totalAttack = attack + loadout.getMainHand().use();
		
		double armorEnemy = unit.getDefense();
		
		int damage = (int)((totalAttack*1.8*(1-armorEnemy/(double)100)));
		
		return damage;
	}
	
	public String toString(){
		return ("Mage"+ "\r\n" +super.toString());
	}
}
