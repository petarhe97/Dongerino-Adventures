
public abstract class Hero extends Unit{
	
	//Fields
	protected String name;
	
	protected int level;
	protected int exp;
	
	protected boolean rankUp;
	protected boolean dualWield;
	
	protected Equipment loadout;
	
	protected int expCap;
	
	//Constants
	protected static final int LEVEL_UP_STATS = 1;
	protected static final int RANK_UP_STATS = 2;
	protected static final int RANK_UP_LEVEL = 11;
	protected static final int MAX_LEVEL = 40;
	protected static final int EXP_CAP_INCREASE = 12;
	
	//Constructor
	public Hero(String name, int health, int attack, int defense, int range, int movement, int level, int exp, boolean rankUp, Equipment loadOut){
		super(health, attack, defense, range, movement);
		this.name = name;
		this.level = level;
		this.exp = exp;
		this.rankUp = rankUp;
		setExpCap();
		this.loadout = loadOut;
	}
	
	//This method returns the name of the rank of the hero. It is abstract as different classes will return different names
	public abstract String checkRank();
	
	//Accessors and mutators
	public String getName(){
		return name;
	}
	
	public boolean getRank(){
		return rankUp;
	}
	
	public int getLevel(){
		return level;
	}
	
	public boolean getDualWield(){
		return dualWield;
	}
	
	public int getExpCap(){
		return expCap;
	}
	
	public int getExp(){
		return exp;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public void setExp(int exp){
		this.exp = exp;
	}
	
	public void setDualWield(boolean dualWield){
		this.dualWield = dualWield;
	}
	
	public void setExpCap(){
		expCap = level * EXP_CAP_INCREASE;
	}
	
	public void setLoadOut(Equipment loadout){
		this.loadout = loadout;
	}

	// RECURSION IS USED IN THE FOLLOWING METHOD
	
	//This method levels up a unit.
	//Note: It recursively calls itself to level up the unit if the unit acquires more EXP than what is required to level up
	//For example: if a unit has 11/12 EXP required to level up, but gains 13 EXP, they will be up to 24/12 EXP. Then, the
	//unit will level up to the next level, which would required 24 EXP. The unit will then be at 24/24 EXP, which would
	//enable them to level up again, which puts them at 24/36 EXP, which would prevent the unit from levelling up again.
	public void levelUp(){
		
		//Checking if the unit can level up
		if (exp >= expCap && level + 1 <= MAX_LEVEL){
			level++;
			addStats(LEVEL_UP_STATS);
			
			//Updating the EXP cap required for the next level
			setExpCap();
			
			//Checking if the unit has ranked up, and updating their status if they did
			if (level == RANK_UP_LEVEL){
				rankUp = true;
				addStats(RANK_UP_STATS);
			}
			
			//Calling the levelUp() method again to check if the unit can level up again.
			levelUp();
		}
	}
	
	//This method updates the stats of a unit with the given integer
	public void addStats(int statsIncrease){
		maxHP += statsIncrease;
		attack += statsIncrease;
		defense += statsIncrease;
	}
	
	//attack takes in a target and deals damage to that target. It returns true if the target is killed and false otherwise.
	public boolean attack (Unit target) {
		addExp (((Enemy)target).getExp());
		otherAction = false;
		moveAction = false;
		return target.deductHealth(calculateDamage(target));
	}
	
	//use takes in an item and allows the Hero to use the item
	//the method returns the previously equipped item and if the item used is a potion or cannot be used, the item will be returned
	public Item use (Item item) {
		//System.out.println(item.toString());
		//check if the level requirement is met
		if (level >= item.getLevelRequired()) {
			//check what type of item is used
			if (item instanceof HealthPotion) {
				//System.out.println("health potion");
					//the user cannot use a health potion at full HP
					if (health == maxHP) {
						return item;
					}
					
					//check if the unit will be healed over its maxHP
					health = Math.min (item.getPrimaryStat() + health, maxHP);
					otherAction = false;
					moveAction = false;
					return null;
			} else  {
				//check if the item met the class requirement
				if (item instanceof Weapon) {
					//System.out.println(((Weapon)item).getClassPossible() + " " + getUnitID());
					if (((Weapon)item).getClassPossible() == getUnitID()) {
						otherAction = false;
						moveAction = false;
						return loadout.changeEquipment(item);
					} 
				} else {
					otherAction = false;
					moveAction = false;
					return loadout.changeEquipment(item);
				}
			}
		} 
		return item;
	}
	
	public void addExp(int exp){

		this.exp += exp;
		//Checking if the unit has leveled up
		//Leveling up the unit if they had reached or surpassed the required amount of EXP
		levelUp();
	}
	
	//This method takes in an integer (for the hero's unit ID), and returns the appropriate String
	public static String classNumToString(int num){
		String className;
		switch(num){
		
		case 0:
			className = "Warrior";
			break;
		case 1:
			className = "Mage";
			break;
		case 2:
			className = "Ranger";
			break;
		case 3:
			className = "Rogue";
			break;
		case 4:
			className = "Knight";
			break;
		case 5:
			className = "Sky Rider";
			break;
		default:
			return null;
		}
		return className;
	}
	
	//This method calculates and returns the value for the defense by the implicit's objects equipment
	public int getEquipmentDefense(){
		int defense = 0;
		defense += loadout.getArmor().use();
		defense += loadout.getHead().use();
		
		if (loadout.getOffHand() instanceof Shield){
			defense += loadout.getOffHand().use();
		}
		
		return defense;
	}
	
	@Override
	//toString method to help with file output and saving
	public String toString(){
		return super.toString() + "\r\n" + name + "\r\n" + level + "\r\n" + exp + "\r\n" + boolToInt(rankUp) + "\r\n" + loadout.toString(); 
	}
}
