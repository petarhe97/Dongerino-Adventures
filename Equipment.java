/*
 Name: Equipment.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The Equipment class contains the information of the equipped item of a player, 
 which consists of a main hand item, an off hand item, an armor, and a hat.
*/

public class Equipment {
	//fields
	private Item mainHand;
	private Item offHand;
	private Item armor;
	private Item head;
	
	//constructors
	public Equipment (Item mainHand, Item offHand, Item armor, Item head) {
		this.mainHand = mainHand;
		this.offHand = offHand;
		this.armor = armor;
		this.head = head;
	}
	
//	public Equipment (Item mainHand, Item armor, Item head) {
//		this.mainHand = mainHand;
//		offHand = null;
//		this.armor = armor;
//		this.head = head;
//	}
	
	//change equipment takes in an item and equips that item and returns the previously equipped item
	public Item changeEquipment (Item item) {
		Item temp;
		//check what kind of item is equipped
		if (item instanceof Weapon) {
			//check if duelwield is possible, if it is, the mainHand is switched to the offHand, the new weapon is switched to the mainHand, 
			//and the previous offHand is returned. Otherwise the two items are simply swapped.
			if (offHand == null) {
				temp = mainHand;
				mainHand = item;
				return temp;
			} else {
				temp = offHand;
				offHand = mainHand;
				mainHand = item;
				return temp;
			}
		} else if (item instanceof Shield) {
			temp = offHand;
			offHand = item;
			return temp;
		} else if (item instanceof BodyArmor) {
			temp = armor;
			armor = item;
			return temp;
		} else if (item instanceof Hat) {
			temp = head;
			head = item;
			return temp;
		} else {
			return item;
		}
	}
	
	//accessor methods
	public Item getMainHand() {
		return mainHand;
	}
	
	public Item getOffHand() {
		return offHand;
	}
	
	public Item getArmor() {
		return armor;
	}
	
	public Item getHead() {
		return head;
	}
	
	//mutator methods
	public void setMainHand (Item item) {
		mainHand = item;
	}
	
	public void setOffHand (Item item) {
		offHand = item;
	}
	
	public void setArmor (Item item) {
		armor = item;
	}
	
	public void setHead (Item item) {
		head= item;
	}
	
	//toString returns the string representation of the class
	public String toString () {
		return mainHand.toString() + "\r\n" + offHand.toString() + "\r\n" + armor.toString() + "\r\n" + head.toString();
	}
}
