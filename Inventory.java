/*
 Name: Inventory.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The Inventory class stores a list of items and contains the methods associated with the list of items.
*/

import java.util.ArrayList;
import java.util.Arrays;


public class Inventory {
	//fields
	private ArrayList<Item> allItems;
	
	//class constant
	private static final int MAX_CAPACITY = 30;
	
	//constructor
	public Inventory () {
		allItems = new ArrayList<Item>(0);
	}
	
	//addItem takes in an Item and adds it to the Inventory
	//if the inventory is full, addItem will return false and true if the item is added
	public boolean addItem (Item item) {
		if (allItems.size() < MAX_CAPACITY) {
			allItems.add(0,item);
			return true;
		} else {
			return false;
		}
	}
	
	//removeItem removes the Item from the Inventory
	public void removeItem (Item item) {
		allItems.remove(item);
	}
	
	//accessor methods
	public Item[] getAllItems () {
		return allItems.toArray(new Item[allItems.size()]);
	}
	
	//mutator methods
	public void setAllItems(Item[] itemList) {
		allItems.clear();
		allItems.addAll (Arrays.asList(itemList));
	}
	
	//toString returns the string representation of the class
	public String toString () {
		String str = "" + allItems.size();
		for (int i = 0; i < allItems.size(); i ++) {
			str += "\r\n" + allItems.get(i).toString();
		}
		return str;
	}
}
