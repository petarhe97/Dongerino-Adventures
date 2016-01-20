/*
 Name: MainMenu.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The MainMenu class contains the methods that is used for the main menu.
*/

import java.io.*;

public class MainMenu {
	//class constant
	private static final String FILE_NAME = "Player.sav";
	
	//createSave creates a save file that is used to store the Player information
	public static void createSave (Player player) {
		
		
		
		try {
			BufferedWriter out = new BufferedWriter (new FileWriter ("TextFiles/SaveFiles/" + FILE_NAME));
			out.write (player.toString());
			out.close();
		} catch (IOException iox) {
			System.out.println ("Error saving file.");
		}
	}
	
	//loadSave loads the save file and returns a Player object which is generated from the file
	public static Player loadSave () {
		try {
			BufferedReader in = new BufferedReader (new FileReader ("TextFiles/SaveFiles/" + FILE_NAME));
			int curLevel, money, num;
			curLevel = Integer.parseInt(in.readLine());
			money = Integer.parseInt(in.readLine());
			num = Integer.parseInt(in.readLine());
			
			//load the items
			Inventory bag = new Inventory();
			for (int i = 0; i < num; i++) {
				bag.addItem(loadItem(in));
			}
						
			//load the characters
			num = Integer.parseInt(in.readLine());
			Unit[] characters = new Unit[num];
			for (int i = 0 ;i < num; i++) {
				String type = in.readLine();
				int health,attack,defense,movement,range;
				
				health = Integer.parseInt(in.readLine());
				attack = Integer.parseInt(in.readLine());
				defense = Integer.parseInt(in.readLine());
				movement = Integer.parseInt(in.readLine());
				range = Integer.parseInt(in.readLine());
		
					int level,exp;
					String name;
					boolean rankUp;
					Equipment loadout;
					
					name = in.readLine();
					level = Integer.parseInt(in.readLine());
					exp = Integer.parseInt(in.readLine());
					rankUp = Integer.parseInt(in.readLine()) == 1;
					loadout = new Equipment (loadItem(in),loadItem(in),loadItem(in),loadItem(in));
					
					if (type.equals("Knight")) {
						characters[i] = new Knight (name, health, attack, defense, range, movement, level, exp, rankUp, loadout);
					} else if (type.equals("Ranger")) {
						characters[i] = new Ranger (name, health, attack, defense, range, movement, level, exp, rankUp, loadout);
					} else if (type.equals("Sky Rider")) {
						characters[i] = new SkyRider (name, health, attack, defense, range, movement, level, exp, rankUp, loadout);
					} else if (type.equals("Warrior")) {
						characters[i] = new Warrior (name, health, attack, defense, range, movement, level, exp, rankUp, loadout);
					} else if (type.equals("Mage")) {
						characters[i] = new Mage (name, health, attack, defense, range, movement, level, exp, rankUp, loadout);
					} else if (type.equals("Rogue")) {
						characters[i] = new Rogue (name, health, attack, defense, range, movement, level, exp, rankUp, loadout);
					} 
				}
			in.close();
			return new Player(curLevel, characters, money, bag);
		} catch (IOException iox) {
			System.out.println("Error");
			return null;
		}
	}
	
	//loadItem is used to load an item in the save file. The method takes in the current BufferedReader being used which is used to
	//read the information of an item and returned as an Item object.
	private static Item loadItem (BufferedReader in) {
		Item item = null;
		try {
			int itemType;
			String name;
			int levelRequired;
			int primaryStat;
			int classPossible;
			
			itemType = Integer.parseInt(in.readLine());
			name = in.readLine();
			levelRequired = Integer.parseInt(in.readLine());
			primaryStat = Integer.parseInt(in.readLine());
			switch(itemType){
			case 0:
				item = new HealthPotion(name,levelRequired,primaryStat);
				break;
			case 1:
				classPossible = Integer.parseInt(in.readLine());
				item = new Weapon(name,levelRequired,primaryStat,classPossible);
				break;
			case 2:
				item = new Shield(name,levelRequired,primaryStat);
				break;
			case 3:
				item = new BodyArmor(name,levelRequired,primaryStat);
				break;
			case 4:
				item = new Hat(name,levelRequired,primaryStat);
				break;
			}
		} catch (IOException iox) {}
		
		return item;
	}
	
	//exitGame terminates the program
	public static void exitGame() {
		System.exit(0);
	}
}

