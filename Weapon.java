/*
 Name: Weapon.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The Weapon class contains the information of a Weapon Item.
*/

public class Weapon extends Item{

	private int classPossible;
	
	//Constructors
	
	//This constructor is used when generating new items
	public Weapon(String name,int levelRequired,int classPossible){
		super(name,levelRequired);
		itemId = 1;
		this.classPossible = classPossible;
		price = (levelRequired*50)/40*60;
		salePrice = (int)(price*0.75);
		primaryStat = levelRequired*3 + (int)(Math.random()*2+1);
	}
	
	//This constructor is used when loading a save and loading pre-existing items
	public Weapon(String name, int levelRequired, int primaryStat,int classPossible){
		super(name,levelRequired,primaryStat);
		itemId = 1;
		this.classPossible = classPossible;
		price = (levelRequired*50)/40*60;
		salePrice = (int)(price*0.75);
	}
	
	//Method to return the class which can equip the implicit item
	public int getClassPossible(){
		return classPossible;
	}
	
	//Mutator
	public void setClassPossible(int classPossible){
		this.classPossible = classPossible;
	}

	//Method to use the item
	@Override
	int use() {
		return primaryStat;
	}
	
	//Method to compare the implicit object with the explicit object
	public boolean equals(Weapon other){
		return (classPossible == other.classPossible && itemId == other.itemId && primaryStat == other.primaryStat && name.equals(other.name) && price == other.price && salePrice == other.salePrice);
	}
	
	@Override
	public String toString(){
		return (itemId+"\r\n"+super.toString()+"\r\n"+classPossible);
	}

}
