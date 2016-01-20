/*
 Name: Shield.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The Shield class contains the information of a Shield Item.
*/

public class Shield extends Item{
	
	//Constructors
	
	//This constructor is used when generating new items
	public Shield(String name,int levelRequired){
		super(name,levelRequired);
		itemId = 2;
		price = (levelRequired*50)/40*60;
		salePrice = (int)(price*0.75);
		primaryStat = levelRequired*3/5 + (int)(Math.random()*5+1);
	}
	
	//This constructor is used when loading a save and loading pre-existing items
	public Shield(String name, int levelRequired, int primaryStat){
		super(name,levelRequired,primaryStat);
		itemId = 2;
		price = (levelRequired*50)/40*60;
		salePrice = (int)(price*0.75);
	}

	//Method to use the item
	@Override
	int use() {
		return (int)(primaryStat*1.2);
	}

	@Override
	public String toString(){
		return (itemId+"\r\n"+super.toString());
	}
}
