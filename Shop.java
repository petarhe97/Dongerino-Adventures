/*
  Name: Shop.java
  Author: Alexander Lu, Bob Du, Ian Hu, Peter He
  Date: Jan 9, 2015
  School: A. Y. Jackson S.S.
  Purpose: The Shop class contains the method that are responsible for generating new items into the game
           and selling them 
 */

import java.io.*;
public class Shop {
	
	//required fields
	private int curLevelMemory;
	private Inventory itemsInShop;
	private final int SHOPCAPACITY = 20;
	
	//constructor
	public Shop(){
		curLevelMemory = 1;
		itemsInShop = new Inventory();
	}
	
	//accessors and mutators
	public int getCurLevelMemory(){
		return curLevelMemory;
	}
	
	public Item[] getItemsInShop(){
		return itemsInShop.getAllItems();
	}
	
	public void setCurLevelMemory(int curLevelMemory){
		this.curLevelMemory = curLevelMemory;
	}
	
	public void setItemsInShop(Inventory itemsInShop){
		this.itemsInShop = itemsInShop;
	}

	//this method is responsible for loading the already generated items in the Shop that has been saved
	//for accessing until a new level is reached
	public void loadAllItems(int level){
		try{
		BufferedReader in = new BufferedReader(new FileReader("TextFiles/ShopText/ShopSave.txt"));
		int itemType;
		String name;
		int levelRequired;
		int primaryStat;
		int classPossible;
		
		curLevelMemory = Integer.parseInt(in.readLine());
		
		if(curLevelMemory == level){
			for(int i = 0; i < SHOPCAPACITY; i++){
				itemType = Integer.parseInt(in.readLine());
				name = in.readLine();
				levelRequired = Integer.parseInt(in.readLine());
				primaryStat = Integer.parseInt(in.readLine());
				switch(itemType){
				case 0:
					itemsInShop.addItem(new HealthPotion(name,levelRequired,primaryStat));
					break;
				case 1:
					classPossible = Integer.parseInt(in.readLine());
					itemsInShop.addItem(new Weapon(name,levelRequired,primaryStat,classPossible));
					break;
				case 2:
					itemsInShop.addItem(new Shield(name,levelRequired,primaryStat));
					break;
				case 3:
					itemsInShop.addItem(new BodyArmor(name,levelRequired,primaryStat));
					break;
				case 4:
					itemsInShop.addItem(new Hat(name,levelRequired,primaryStat));
					break;
				}
			}
		}else{
			generateItems();
		}
		in.close();
		
		}catch(IOException iox){
			System.out.println("Error reading file.");
		}
	}
	
	//this method generates new items into the Shop every time a new level is reached. 
	//it uses different text files to generate random names and random number generator to generate the rest
	//based on curLevelMemory
	public void generateItems(){
		
		final int RANGE = 10;
		int bottomTier;
		int itemType;
		int levelRequired;
		int classPossible;
		String name;
		
		if(curLevelMemory <= 10){
			bottomTier = 1;
		}else if(curLevelMemory <= 20 && curLevelMemory >10){
			bottomTier = 11;
		}else if(curLevelMemory <= 30 && curLevelMemory > 20){
			bottomTier = 21;
		}else{
			bottomTier = 31;
		}
		
		for(int i = 0 ; i < SHOPCAPACITY ; i++){
			itemType = (int)(Math.random()*5);
			levelRequired = (int)(Math.random()*(RANGE) + bottomTier);
			
			switch(itemType){
			case 0:
				if(bottomTier == 1){
					name = "Small Potion";
				}else if(bottomTier == 11){
					name = "Medium Potion";
				}else if(bottomTier == 21){
					name = "Greater Potion";
				}else{
					name = "Divine Potion";
				}
				itemsInShop.addItem(new HealthPotion(name,levelRequired));
				break;
			case 1:
				classPossible = (int)(Math.random()*6);
				name = generateItemName(itemType,bottomTier, classPossible);
				itemsInShop.addItem(new Weapon(name,levelRequired,classPossible));
				break;
			case 2:
				name = generateItemName(itemType,bottomTier, -1);
				itemsInShop.addItem(new Shield(name,levelRequired));
				break;
			case 3:
				name = generateItemName(itemType,bottomTier, -1);
				itemsInShop.addItem(new BodyArmor(name,levelRequired));
				break;
			case 4:
				name = generateItemName(itemType,bottomTier, -1);
				itemsInShop.addItem(new Hat(name,levelRequired));
				break;
			}
		}
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter("TextFiles/ShopText/ShopSave.txt"));
			out.write(toString());
			out.close();
		}catch(IOException iox){
			System.out.println("Error saving file.");
		}
	}
	
	//this method is a method used in generateItem() to generate the item names
	//it is split into a separate method due to how much code there are
	private String generateItemName(int type,int tier,int classPossible){
		
		try{
			String prefixFile = "TextFiles/ShopText/prefix"+(tier+10)+".txt";
			String []prefixWords;
			int numPrefix;
			String prefixFinal;
			BufferedReader prefix = new BufferedReader(new FileReader(prefixFile));
			numPrefix = Integer.parseInt(prefix.readLine());
			prefixWords = new String [numPrefix];
			for(int i = 0 ; i < numPrefix; i++){
				prefixWords[i] = prefix.readLine();
			}
			prefix.close();
			prefixFinal = prefixWords[(int)(Math.random()*numPrefix)];
			
			boolean exist = false;
			
			String suffixFinal = null;
			
			if(tier >= 20){
				int suffixNum;
				String [] suffixWords;
				BufferedReader suffix = new BufferedReader(new FileReader("TextFiles/ShopText/suffix.txt"));
				suffixNum = Integer.parseInt(suffix.readLine());
				suffixWords = new String [suffixNum];
				for(int i = 0 ; i < suffixNum; i++){
					suffixWords[i] = suffix.readLine();
				}
				suffix.close();
				suffixFinal = suffixWords[(int)(Math.random()*suffixNum)];
				exist = true;
			}
			
			String nameFile = null;
			
			if(type == 1){
			
				if(classPossible == 0){	
					nameFile = "TextFiles/ShopText/WarriorWeapon.txt";
				}else if(classPossible == 1){
					nameFile = "TextFiles/ShopText/MageWeapon.txt";
				}else if(classPossible == 2){
					nameFile = "TextFiles/ShopText/ArcherWeapon.txt";
				}else if(classPossible == 3){
					nameFile = "TextFiles/ShopText/RogueWeapon.txt";
				}else if(classPossible == 4){
					nameFile = "TextFiles/ShopText/KnightWeapon.txt";
				}else{
					nameFile = "TextFiles/ShopText/SkyriderWeapon.txt";
				}
			}else if(type == 2){
				nameFile = "TextFiles/ShopText/Shield.txt";
			}else if(type == 3){
				nameFile = "TextFiles/ShopText/BodyArmor.txt";
			}else if(type == 4){
				nameFile = "TextFiles/ShopText/Hat.txt";
			}
			
			BufferedReader in = new BufferedReader(new FileReader(nameFile));
		
			String nameFinal;
			String [] possibleNames;
			int numNames;
			numNames = Integer.parseInt(in.readLine());
			possibleNames = new String [numNames];
			for(int i = 0 ; i < numNames; i++){
				possibleNames[i] = in.readLine();
			}
			in.close();
			nameFinal = possibleNames[(int)(Math.random()*numNames)];
			
			if(exist == false){
				return prefixFinal + " " + nameFinal;
			}else{
				return prefixFinal + " " + nameFinal + " " + suffixFinal;
			}
			
		}catch(IOException iox){
			System.out.println("Error reading file.");
		}
		return null;
	}
	
	//this method is responsible for finding the item bought and then removing it from the
	//list of items using the search method and removeItem() method in Algorithm
	public void purchaseItem(Item bought){
		Item [] list = Algorithm.searchItemName(bought.getName(),itemsInShop.getAllItems());
		boolean done = false;

		for(int i = 0; i < list.length && done == false && bought!=null; i++){
			if(itemsInShop.getAllItems()[i].equals(bought)){
				itemsInShop.removeItem(itemsInShop.getAllItems()[i]);
			}
		}
	}
	
	//this method returns a string that has characteristics of this class
	public String toString(){
		String result = curLevelMemory+"\r\n";
		for(int i = 0; i < SHOPCAPACITY; i++){
			result += itemsInShop.getAllItems()[i].toString()+"\r\n";
		}
		return result;
	}
	
}
