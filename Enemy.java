
public class Enemy extends Unit{

	//Fields
	private int enemyID;
	private int aggressionLevel;
	private int exp;
	private int gold;
	
	private String enemyType;
	
	//Constructor
	public Enemy(int health, int attack, int defense, int range, int movement, String enemyType, int enemyID, int aggressionLevel,
			int exp, int gold) {
		super(health, attack, defense, range, movement);
		this.enemyID = enemyID;
		this.aggressionLevel = aggressionLevel;
		this.exp = exp;
		this.gold = gold;
		this.enemyType = enemyType;
	}

	//Accessors and mutators
	public int getEnemyID(){
		return enemyID;
	}
	
	public String getEnemyType(){
		return enemyType;
	}
	
	public int getAggressionLevel(){
		return aggressionLevel;
	}
	
	public int getExp(){
		return exp;
	}
	
	public int getGold(){
		return gold;
	}
	
	public void setEnemyID(int enemyID){
		this.enemyID = enemyID;
	}
	
	public void setEnemyType(String enemyType){
		this.enemyType = enemyType;
	}
	
	public void setAggressionLevel(int aggressionLevel){
		this.aggressionLevel = aggressionLevel;
	}
	
	public void setExp(int exp){
		this.exp = exp;
	}
	
	public void setGold(int gold){
		this.gold = gold;
	}
	
	//This method calculates the damage that would be done to the explicit Unit object
	public int calculateDamage(Unit unit){
		double damage = attack * (1 - (unit.getDefense() + ((Hero)unit).getEquipmentDefense()) / 100.0);
		return (int) damage;
	}
	
	public String toString(){
		return health + "\r\n" + attack + "\r\n" + defense + "\r\n" + movement + "\r\n" + range;
	}
	//Attack takes in a target and deals damage to that target. It returns true if the target is killed and false otherwise.
		public boolean attack (Unit target) {
			moveAction = false;
			otherAction = false;
			return target.deductHealth(calculateDamage(target));
		}
}
