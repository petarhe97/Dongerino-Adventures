/*
  Name: Warrior.java
  Author: Alexander Lu, Bob Du, Ian Hu, Peter He
  Date: Jan 9, 2015
  School: A. Y. Jackson S.S.
  Purpose: The Warrior class is a subclass of hero which defines a class of hero
            and has methods implemented differently than the other subclasses of hero
 */

public class Warrior extends Hero{

	//Constants
	private static final String LOWER_RANK_NAME = "Warrior";
	private static final String HIGHER_RANK_NAME = "BladeMaster";
	
	//Constructor
	public Warrior(String name, int health, int attack, int defense, int range, int movement, int level, int exp, boolean rankUp, Equipment loadOut){
		super(name, health, attack, defense, range, movement, level, exp, rankUp, loadOut);
		unitID = 0;
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
		
		int totalAttack = attack + loadout.getMainHand().use();
		
		double armorEnemy = unit.getDefense();
		
		double damage = ((totalAttack*1.8*((double)health/maxHP))*(1-armorEnemy / 100.0));
		
		//System.out.println (damage);
		
		return(int) damage;
	}
	
	public String toString(){
		return ("Warrior"+ "\r\n" +super.toString());
	}
}
