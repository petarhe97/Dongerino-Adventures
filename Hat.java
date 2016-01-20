/*
 Name: Hat.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The Hat class contains the information of a Hat Item.
*/

public class Hat extends Item{

	//Constructors
	
	//This constructor is used when generating new items
	public Hat(String name,int levelRequired){
		super(name,levelRequired);
		itemId = 4;
		price = (levelRequired*50)/40*60;
		salePrice = (int)(price*0.75);
		primaryStat = levelRequired*3/10 + (int)(Math.random()*3+1);
	}
	
	//This constructor is used when loading a save and loading pre-existing items
	public Hat(String name, int levelRequired, int primaryStat){
		super(name,levelRequired,primaryStat);
		itemId = 4;
		price = (levelRequired*50)/40*60;
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
