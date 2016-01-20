/*
  Name: SkyRider.java
  Author: Alexander Lu, Bob Du, Ian Hu, Peter He
  Date: Jan 9, 2015
  School: A. Y. Jackson S.S.
  Purpose: The SkyRider class is a subclass of hero which defines a class of hero
            and has methods implemented differently than the other subclasses of hero
 */

public class SkyRider extends Hero{

	//Constants
	private static final String LOWER_RANK_NAME = "SkyRider";
	private static final String HIGHER_RANK_NAME = "Dragon Knight";
	
	//Constructor
	public SkyRider(String name, int health, int attack, int defense, int range, int movement, int level, int exp, boolean rankUp, Equipment loadOut){
		super(name, health, attack, defense, range, movement, level, exp, rankUp, loadOut);
		unitID = 5;
		terrainPassing = true;
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
		
		int damage = (int)((totalAttack*1.2*(1-armorEnemy/(double)100)));
		
		return damage;
	}
	
	public String toString(){
		return ("Sky Rider"+ "\r\n" +super.toString());
	}
}
