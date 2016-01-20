/*
 Name: Player.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The Player class stores the information of a player, which includes the player's current inventory, list of heroes, current level, and money.
*/

public class Player {
	//fields
	private Inventory bag;
	private Unit[] characters;
	private int curLevel;
	private int money;
	private int numCharactersToSpawn;
	
	private static final int NUM_HEROES = 8;
	
	//constructor
	public Player(){
		numCharactersToSpawn = 3;
		curLevel = 0;		
		characters = new Unit[NUM_HEROES];
		money = 1000;
		Equipment equipment;
		bag = new Inventory();
		Weapon weapon = new Weapon("Scrubby Sword of Gaben", 1, 0);
		Shield shield = new Shield("Scrubby Shield of Gaben", 1);
		Hat hat = new Hat ("Scrubby Hat of Gaben", 1);
		BodyArmor armor = new BodyArmor("Scrubby Armor of Gaben", 1);
		
		equipment = new Equipment(weapon, shield, armor, hat);
		
		characters[0] = new Warrior ("Dongerino", 13, 4, 4, 1, 7, 1, 0, false, equipment);
		characters[1] = new Mage ("Kappa", 10, 4, 1, 2, 5, 1, 0, false, equipment);
		characters[2] = new Knight ("Scrub", 15, 2, 6, 1, 5, 1, 0, false, equipment);
		characters[3] = new Ranger ("Nob", 10, 5, 1, 2, 7, 1, 50, false, equipment);
		characters[4] = new Knight ("Greyface", 10, 6, 1, 2, 5, 1, 100, false, equipment);
		characters[5] = new Mage ("Cancer", 15, 2, 6, 1, 5, 1, 100, false, equipment);
		characters[6] = new Rogue ("Snake", 10, 5, 0, 1, 8, 1, 200, false, equipment);
		characters[7] = new SkyRider ("Twitch Stream", 20, 3, 3, 1, 10, 1, 400, false, equipment);
		
		((Hero)characters[3]).addExp(0);
		((Hero)characters[4]).addExp(0);
		((Hero)characters[5]).addExp(0);
		((Hero)characters[6]).addExp(0);
		((Hero)characters[7]).addExp(0);
		
		for (int i = 0; i < 5; i++){
			bag.addItem(new HealthPotion ("Small Potion", 1));
		}
	}
	
	public Player (int curLevel, Unit[] characters, int money, Inventory bag) {
		this.curLevel = curLevel;
		this.characters = characters;
		this.money = money;
		this.bag = bag;
		for (int i = 0; i < this.characters.length; i++){
			((Hero)this.characters[i]).addExp(0);
		}
	}
	
	public void setNumCharactersToSpawn(int newNum){
		numCharactersToSpawn = newNum;
	}
	
//	//checkTurnEnd goes through the characters and returns true if all Heroes have used up their actions and false if otherwise
//	public boolean checkTurnEnd() {
//		for (int i = 0; i < characters.length; i++) {
//			if (characters[i] instanceof Hero && characters[i].getActionsLeft() != 0) {
//				return false;
//			}
//		}
//		return true;
//	}
	
//	//killUnit removes the unit from characters
//		public void killUnit (Unit killed) {
//			Unit[] newList = new Unit[characters.length-1];
//			//cur stores the current index to be filled for newList
//			int cur = 0;
//			
//			//go through the current characters array and copy all the characters to newList, the killed unit is skipped
//			for (int i = 0; i < characters.length; i++) {
//				if (characters[i] != killed) {
//					newList[cur] = characters[i];
//					cur ++;
//				}	
//			}
//			
//			//update the characters array
//			characters = newList;
//		}
		
	//collectReward takes in an integer and adds that integer to the money
	public void collectReward (int amount) {
		money += amount;
	}
	
	//buyItem takes in an item and adds it to the bag and deducts the money if the player can buy the item
	//the method will return true if the player has enough money to purchase the item and can put the item in the bag, and false if the player doesn't
	public boolean buyItem (Item item) {
		if (money >= item.getPrice()) {
			if (bag.addItem(item)) {
				money -= item.getPrice();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	//sellItem takes in an item and removes it from the bag and adds a portion of the price to the money
	public void sellItem (Item item) {
		money += item.getPrice()  / 2;
		bag.removeItem (item);
	}
	
	//useItem takes in an item and a hero and calls the use method in hero to use the item. 
	//useItem checks if the item is consumed (null is returned) and if the item is swapped, the swapped item will be put to the bag
	public void useItem (Item item, Unit hero) {
		Item swappedItem;
		swappedItem = ((Hero)hero).use(item);
		if (swappedItem != null) {
			bag.addItem(swappedItem);
		}
		bag.removeItem (item);
	}
	
	//destroyItem takes in an Item and removes it from the bag
	public void destroyItem (Item item) {
		bag.removeItem(item);
	}
	
	
	
	//accessor methods
	public int getMoney () {
		return money;
	}
	
	public int getCurLevel() {
		return curLevel;
	}
	
	public Inventory getBag () {
		return bag;
	}
	
	public Unit[] getCharacters(){
		Unit[] temp = new Unit[numCharactersToSpawn];
		for (int i = 0; i < numCharactersToSpawn; i++){
			temp[i] = characters[i];
		}
		return temp;
	}
	
	//mutator methods
	public void setMoney (int newMoney) {
		money = newMoney;
	}
	
	public void setCurLevel (int newCurLevel) {
		curLevel = newCurLevel;
	}
	
	public void setBag (Inventory newBag) {
		bag = newBag;
	}
	
	public void resetAllHeroes(){
		for (int i = 0; i < characters.length; i++){
			characters[i].reset();
		}
	}
	
	public void resetAllHeroesForNewLevel(){
		for (int i = 0; i < characters.length; i++){
			characters[i].reset();
			characters[i].resetHP();
		}
	}
	
	public int getCharacterAverageLevel(){
		
		int average = 0;
		
		for (int i = 0; i < characters.length; i++){
			average += ((Hero)characters[i]).getLevel();
		}
		
		return average / characters.length;
	}
	
	//toString returns the string representation of the class
	public String toString () {
		String str = curLevel + "\r\n" + money + "\r\n" + bag.toString() + "\r\n" + characters.length;
		for (int i = 0; i < characters.length; i++) {
			str += "\r\n" + characters[i].toString();
		}
		return str;
	}
}
