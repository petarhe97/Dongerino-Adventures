/*
 Name: HealthPotion.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The HealthPotion class contains the information of a HealthPotion Item.
*/

public class HealthPotion extends Item{

	//Constructors
	
	//This constructor is used when generating new items
	public HealthPotion(String name,int levelRequired){
		super(name,levelRequired);
		itemId = 0;
		price = levelRequired*20;
		salePrice = (int)(price*0.75);
		primaryStat = levelRequired*10;
	}
	
	//This constructor is used when loading a save and loading pre-existing items
	public HealthPotion(String name, int levelRequired, int primaryStat){
		super(name,levelRequired,primaryStat);
		itemId = 0;
		price = levelRequired*20;
		salePrice = (int)(price*0.75);
	}

	//Method to use the item
	@Override
	int use() {
		return primaryStat;
	}
	
	@Override
	public String toString(){
		return (itemId+"\r\n"+super.toString());
	}

}
