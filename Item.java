/*
 Name: Item.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The Item class contains the information of an Item.
 	
 */

abstract class Item {
	
	//Fields
	protected int itemId;
	protected String name;
	protected int price;
	protected int salePrice;
	protected int primaryStat;
	protected int levelRequired;
	
	//Constructors
	
	//This constructor is used when generating new items
	public Item(String name,int levelRequired){
		this.name = name;
		this.levelRequired = levelRequired;
	}
	
	//This constructor is used when loading a save and loading pre-existing items
	public Item(String name,int levelRequired, int primaryStat){
		this.name = name;
		this.levelRequired = levelRequired;
		this.primaryStat = primaryStat;
	}
	
	//Accessors and mutators
	public String getName(){
		return name;
	}
	
	public int getPrice(){
		return price;
	}
	
	public int getSalePrice(){
		return salePrice;
	}
	
	public int getPrimaryStat(){
		return primaryStat;
	}
	
	public int getLevelRequired(){
		return levelRequired;
	}
	
	public int getItemID(){
		return itemId;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setPrice(int price){
		this.price = price;
	}
	
	public void setSalePrice(int salePrice){
		this.salePrice = salePrice;
	}
	
	public void setPrimaryStat(int primaryStat){
		this.primaryStat = primaryStat;
	}
	
	public void setLevelRequired(int levelRequired){
		this.levelRequired = levelRequired;
	}
	
	//Method to compare the implicit object's name to the explicit object's name
	public int compareToAlphabet(Item other){
		return (name.compareTo(other.name));
	}
	
	//Method to compare the implicit object's price to the explicit object's price
	public int compareToPrice(Item other){
		if(price-other.price != 0){
			return price - other.price;
		}else{
			return compareToAlphabet(other);
		}
	}
	
	//Method to compare the implicit object's level requirement to the explicit object's level requirement
	public int compareToLevel(Item other){
		if(levelRequired - other.levelRequired != 0){
			return levelRequired - other.levelRequired;
		}else{
			return compareToPrice(other);
		}
	}

	//Abstract method for using the items
	abstract int use();
	
	//Method to compare the implicit item with the explicit item
	public boolean equals(Item other){
		return (itemId == other.itemId && primaryStat == other.primaryStat && name.equals(other.name) && price == other.price && salePrice == other.salePrice);
	}
	
	public String toString(){
		return(name+"\r\n"+levelRequired+"\r\n"+primaryStat);
	}
}
