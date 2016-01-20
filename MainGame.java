/*
 Name: MainGame.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The MainGame class contains the methods associated with updating what the current menu is selected 
 and reading in the user input and perform the correct actions.
*/

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainGame {
	
	//Constants for the enumeration of the different screens
	final static int ID_MAIN_MENU = 0;
	final static int ID_SUB_MENU = 1;
	final static int ID_INTERMEDIATE_MENU = 2;
	final static int ID_INVENTORY_MENU = 3;
	final static int ID_INVENTORY_ACTION_MENU = 4;
	final static int ID_ACTION_MENU = 5;
	final static int ID_LOAD_MENU = 6;
	final static int ID_ACTION_MOVE_MENU = 7;
	final static int ID_ACTION_ATTACK_MENU = 8;
	final static int ID_SHOP_MENU = 9;
	final static int ID_CHARACTER_MENU = 10;
	final static int ID_PRE_BATTLE_CUTSCENE = 12;
	final static int ID_POST_BATTLE_CUTSCENE = 13;
	final static int ID_BATTLE = 14;
	
	final static int NUM_LEVELS = 11;
	
	//Fields
	static boolean inBattle;
	
	static private int screenNumber;
	static private char[][] board;
	static GUI gui;
	static private Map map;
	static private int mapHeight;
	static private int mapWidth;
	static private int prevScreen;
	static private Item[] shopItems;
	static private Player player;
	static private AI ai;
	static private Shop shop;
	static private Item[] searchedItemList;
	static private Scanner sc;
	static private boolean recentlySearched;
	static private int menuSortType;
	static private Item itemSelected;
	
	static private int recentUnitSelected;
	
	static private Unit[] heroList;
	static private Unit[] enemyList;
	
	static boolean isEnemyAttacking;
	static int curEnemyAttack;
	static int totalEnemyAttacks;
	static int curEnemyAttacker;
	
	
	static Coordinate[] unitPositions;
	
	//Default constructor
	public MainGame(){
		gui = new GUI();
		
		screenNumber = 0;
		startGame();
	}
	
	//This method initializes the graphics interface
	public void startGame(){
		sc = new Scanner(System.in);
		gui.setScreenID(screenNumber);
		gui.start();
	}
	
	//This method returns the screen number
	public int getScreenNumber(){
		return screenNumber;
	}
	
	//This method is called within the GUI class to register keyboard input
	//Note: This method functions by comparing the screen number that is stored in MainGame with the IDs of the different screens.
	//Then, the method checks for the input from the GUI class. For example: if the screenNumber is 0, it would indicate that the
	//program is in the Main Menu. The buttons in the Main Menu are labelled with the top button being 0, and the bottom button
	//being 2 (number of buttons - 1). The inputCommand parameter determines what button has been pressed, and the program then
	//registers and executes the appropriate function.
	
	//The enumeration for certain functions are negative (for example: -1 represents going back) so that adding additional buttons
	//would not required non-button commands to be changed.
	public static void getCommand(int inputCommand){
		
		//Inputs for main menu
		//Note: I'm not exactly sure why there is a repeated boolean statement, but when it is removed, the program
		//fails to perform properly
		if (screenNumber == ID_MAIN_MENU){
			if (screenNumber == ID_MAIN_MENU){
				if (inputCommand == 0){
				
					//Prompting the user to create a new save
					/*
					System.out.println("Create new save? (Type 'y' to overwrite save)");
					
					Scanner sc = new Scanner(System.in);
					String input = sc.nextLine();
					if(input.length() > 0){

							char inputChar = input.charAt(0);
						
							if (inputChar == 'y'){
								*/
						player = new Player();
						MainMenu.createSave(player);
						/*
						}
					}
					*/
					
					//If the player created a new save, then initialize the game
					if (player != null){
						shop = new Shop();
						shop.setCurLevelMemory(-1);
						screenNumber = ID_PRE_BATTLE_CUTSCENE;
						Cutscene.loadCutscene(player.getCurLevel(), true);
						gui.initializeCutscene();
						gui.setScreenID(screenNumber);
					}
					
				}else if (inputCommand == 1){
					player = MainMenu.loadSave();
					openMenu(ID_SUB_MENU);
					
				}else if (inputCommand == 2){
					MainMenu.exitGame();
				}
				
				if (player != null){
					shop = new Shop();
					screenNumber = ID_PRE_BATTLE_CUTSCENE;
					Cutscene.loadCutscene(player.getCurLevel(), true);
					gui.initializeCutscene();
					gui.setScreenID(screenNumber);
				}
				
			}else if (inputCommand == 1){
				player = MainMenu.loadSave();
				openMenu(ID_SUB_MENU);
				
			}else if (inputCommand == 2){
				MainMenu.exitGame();
			}
	
		//Inputs for sub menu
		}else if (screenNumber == ID_SUB_MENU){
			
			if (inputCommand == 0){
				
				screenNumber = ID_BATTLE;
				gui.setScreenID(screenNumber);
				
				gui.setUnits(player.getCharacters(), map.getEnemies());
				
				gui.setBoard(board, mapWidth, mapHeight, map);
				player.resetAllHeroesForNewLevel();
				
			}else if (inputCommand == 1){
				openMenu(ID_INVENTORY_MENU);
				prevScreen = ID_SUB_MENU;
			}else if (inputCommand == 2){
				
				//Open character menu
				openMenu(ID_CHARACTER_MENU);
			}else if (inputCommand == 3){
				
				//Opening shop
				openMenu(ID_SHOP_MENU);
			}else if(inputCommand == 4){
				MainMenu.createSave(player);
			}
		
		//Inputs for intermediate menu
		}else if (screenNumber == ID_INTERMEDIATE_MENU){
		
			if(inputCommand == 0){
				
				if (player.getCurLevel() < NUM_LEVELS){
					screenNumber = ID_PRE_BATTLE_CUTSCENE;
					Cutscene.loadCutscene(player.getCurLevel(), true);
					gui.setScreenID(screenNumber);
					gui.initializeCutscene();
				}else{
					openMenu(ID_MAIN_MENU);
				}
				
			}
			
		//Inputs for cutscenes
		}else if (screenNumber == ID_PRE_BATTLE_CUTSCENE){
			
			if (inputCommand == 0){
				openMenu(ID_SUB_MENU);
			}
		
		//Inputs for the post battle cutscene
		}else if (screenNumber == ID_POST_BATTLE_CUTSCENE){
			
			if (inputCommand == 0 && !inBattle){
				openMenu(ID_INTERMEDIATE_MENU);
			}else{
				
				if (inBattle && !isEnemyAttacking){
					inBattle = false;
					screenNumber = ID_BATTLE;
					gui.setScreenID(screenNumber);
				}else{
					boolean found = false;
					for (int i = curEnemyAttacker + 1; i < enemyList.length && !found; i++){
						if (ai.getAttackCommands(i) != null){
							found = true;
							curEnemyAttacker = i;
						}
					}
					
					if (curEnemyAttack <= totalEnemyAttacks){
						String message = ((Enemy)enemyList[curEnemyAttacker]).getEnemyType() + " did " + ai.getAttackCommandsDamage(curEnemyAttacker) + " damage to " + ((Hero)ai.getAttackCommands(curEnemyAttacker)).getName() + "!";
						Cutscene.loadAttackCutscene(((Enemy)enemyList[curEnemyAttacker]).getEnemyID() * -1 - 1, ai.getAttackCommands(curEnemyAttacker).getUnitID(), message, map.getTileNumber(enemyList[curEnemyAttacker].getCoordinate()));
						gui.initializeCutscene();
					}
					curEnemyAttack++;
					
					if (curEnemyAttack > totalEnemyAttacks){
						isEnemyAttacking = false;
						if (!checkDefeat()){
							inBattle = false;
							openMenu (ID_MAIN_MENU);
						}else{
							
							for (int i = 0; i < player.getCharacters().length; i++){
								if (heroList[i].isAlive()){
									unitPositions[i] = heroList[i].getCoordinate();
								} else {
									unitPositions[i] = null;
								}
							}
							
							Algorithm.setUnitPositions(unitPositions);
							screenNumber = ID_BATTLE;
							gui.setScreenID(screenNumber);
							player.resetAllHeroes();
						}
						
					}
				}
			}
			
		//Inputs for the inventory
		}else if (screenNumber == ID_INVENTORY_MENU){
			
			if (inputCommand == -1){
				
				if (!recentlySearched){
					
					if (!inBattle){
						//Return to previous menu
						openMenu(ID_SUB_MENU);
					}else{
						screenNumber = ID_ACTION_MENU;
						gui.setScreenID(screenNumber);
						inBattle = false;
					}
				}else{
					recentlySearched = false;
					gui.setItemSelected(0);
					gui.setItemsToDisplay(player.getBag().getAllItems());
				}
			}else if(inputCommand == -2){
				System.out.println("Enter keyword to search (by name): ");
				String searchKeyword = sc.nextLine();
				System.out.println();
				
				searchedItemList = Algorithm.searchItemName(searchKeyword, player.getBag().getAllItems());
				gui.setItemsToDisplay(searchedItemList);
				gui.setItemSelected(0);
				recentlySearched = true;
			}else if (inputCommand == -4){
				System.out.println("Enter keyword to search (by price): ");
				try{
					int searchKeyword = sc.nextInt();
					sc.nextLine();
					searchedItemList = Algorithm.searchItemSalePrice(searchKeyword, player.getBag().getAllItems());
					gui.setItemsToDisplay(searchedItemList);
					gui.setItemSelected(0);
					recentlySearched = true;
				}catch(InputMismatchException im){
					System.out.println("Invalid input");
				}
				System.out.println();
				
				
			}else if (inputCommand == -3){
				
				Item[] items = player.getBag().getAllItems();
				gui.setItemSelected(0);
				
				if (menuSortType == 0){
					menuSortType = 1;
					
					Algorithm.sortItemsAlphabet(items);
					
				}else if (menuSortType == 1){
					menuSortType = 2;
					
					Algorithm.sortItemsPriceMinToMax(items);
					
				}else if (menuSortType == 2){
					menuSortType = 0;
					Algorithm.sortItemsPriceMaxToMin(items);
					
				}
				gui.setItemsToDisplay(items);
			}else{
				if (recentlySearched){
					itemSelected = searchedItemList[inputCommand];
					openMenu(ID_INVENTORY_ACTION_MENU);
				}else{
					if (player.getBag().getAllItems().length > 0){
						itemSelected = player.getBag().getAllItems()[inputCommand];
						openMenu(ID_INVENTORY_ACTION_MENU);
					}
				}
			}
		
		//Inputs for the inventory action menu
		}else if (screenNumber == ID_INVENTORY_ACTION_MENU){
			
			if (inputCommand == -1){
				//Return to inventory menu
				openMenu(ID_INVENTORY_MENU);
				
				if(recentlySearched){
					gui.setItemsToDisplay(searchedItemList);
					recentlySearched = false;
				}
			}else{
				if (inputCommand == 0){
				//System.out.println("char menu open");
					if (!inBattle){
						openMenu(ID_CHARACTER_MENU);
					} else {
						if (player.getCharacters()[recentUnitSelected].getOtherAction()){	
							player.useItem(itemSelected, player.getCharacters()[recentUnitSelected]);
							gui.setItemsToDisplay(player.getBag().getAllItems());
							gui.setCharactersToDisplay(player.getCharacters());
							if (!player.getCharacters()[recentUnitSelected].getOtherAction()) {
								screenNumber = ID_BATTLE;
								gui.setScreenID(screenNumber);
							}
						}
					}
				}else if (inputCommand == 1){
					player.setMoney(itemSelected.getSalePrice() + player.getMoney());
					player.getBag().removeItem(itemSelected);
					
					openMenu(ID_INVENTORY_MENU);
					gui.setMoney(player.getMoney());
					gui.setItemsToDisplay(player.getBag().getAllItems());
					
					if (player.getBag().getAllItems().length > 0){
						gui.setItemSelected(0);
					}else{
						if(!inBattle){
							openMenu(ID_SUB_MENU);
						}else{
							screenNumber = ID_BATTLE;
							gui.setScreenID(screenNumber);
						}
					}
				}
			}
		
		//Inputs for the battle screen
		}else if (screenNumber == ID_BATTLE){
			inBattle = true;
			checkTurnEnd();
			//System.out.println(inBattle + " " + player.getCharacters()[recentUnitSelected].getOtherAction());
			recentUnitSelected = inputCommand;
			//Open action menu and set possible moves
			
			if (inBattle && player.getCharacters()[recentUnitSelected].getOtherAction()) {
				openMenu(ID_ACTION_MENU);
			}
					//Inputs for the action menu
		}else if (screenNumber == ID_ACTION_MENU){
			
			if (inputCommand == -1){
				//Return to battle screen
				screenNumber = ID_BATTLE;
				gui.setScreenID(screenNumber);
			}else if(inputCommand == 0){
					screenNumber = ID_ACTION_ATTACK_MENU;
					gui.setScreenID(screenNumber);
			}else if(inputCommand == 1){
				if (player.getCharacters()[recentUnitSelected].getMoveAction()) {
					gui.setPossibleMoves(player.getCharacters()[recentUnitSelected].getMovementArea(map, unitPositions));
					screenNumber = ID_ACTION_MOVE_MENU;
					gui.setScreenID(screenNumber);
				}
			}else if (inputCommand == 2) {
					player.getCharacters()[recentUnitSelected].defend();
					screenNumber = ID_BATTLE;
					gui.setScreenID(screenNumber);
			}else if (inputCommand == 3){
					//Open inventory menu
					openMenu(ID_INVENTORY_MENU);
			}
			
		//Inputs for the Action Move menu
		}else if (screenNumber == ID_ACTION_MOVE_MENU){
			if(inputCommand == -1){
				screenNumber = ID_ACTION_MENU;
				openMenu(screenNumber);
			}else if (inputCommand == 0){
				
				//Moving the unit to the desired location using the designate path, if possible
				Coordinate[] path = player.getCharacters()[recentUnitSelected].move(gui.getCursorPosition());
				if (path != null){
					screenNumber = ID_ACTION_MENU;
					gui.setPath(path);
					gui.setScreenID(screenNumber);
				}
			}
			
		//Inputs for the Action Attack menu
		}else if (screenNumber == ID_ACTION_ATTACK_MENU){
			if (inputCommand == -1){
				screenNumber = ID_ACTION_MENU;
				openMenu(screenNumber);
			}else{
				Unit target;
				if ((target = getUnitOn(gui.getCursorPosition())) != null && Algorithm.showAttackRange(player.getCharacters()[recentUnitSelected])[gui.getCursorPosition().getX()][gui.getCursorPosition().getY()]) {
					
					//the message is displayed by Cutscene
					int damage = player.getCharacters()[recentUnitSelected].calculateDamage(target);
					String message = ((Hero)player.getCharacters()[recentUnitSelected]).getName() + " did " + damage + " damage to " + ((Enemy)target).getEnemyType() + "!";
					
					if (player.getCharacters()[recentUnitSelected].attack(target)) {
						target.kill();
						player.collectReward(((Enemy)target).getGold());
						
						message += "\n" + ((Enemy)target).getGold() + " gold was collected!";
						
						unitPositions = new Coordinate[heroList.length + enemyList.length];
						
						//Initializing the unit positions
						for (int i = 0; i < heroList.length; i++){
							if (heroList[i].isAlive()){
								unitPositions[i] = heroList[i].getCoordinate();
							} else {
								unitPositions[i] = null;
							}
						}
					
						for (int i = 0; i < enemyList.length; i++){
							if (enemyList[i].isAlive()){
								unitPositions[i + heroList.length] = enemyList[i].getCoordinate();
							}else{
								unitPositions[i + heroList.length] = null;
							}
						}
						Algorithm.setUnitPositions(unitPositions);
					}
					
					screenNumber = ID_POST_BATTLE_CUTSCENE;
					//Playing attacking cutscene
					Cutscene.loadAttackCutscene(player.getCharacters()[recentUnitSelected].getUnitID(), ((Enemy)target).getEnemyID() * -1 - 1, message, map.getTileNumber(player.getCharacters()[recentUnitSelected].getCoordinate()));
					gui.initializeCutscene();
					gui.setScreenID(screenNumber);
				}

			}
			
		//Inputs for the Shop menu
		}else if (screenNumber == ID_SHOP_MENU){
			if (inputCommand == -1){
				if (!recentlySearched){
					//Return to previous menu
					openMenu(ID_SUB_MENU);
				}else{
					recentlySearched = false;
					gui.setItemSelected(0);
					gui.setItemsToDisplay(shopItems);
				}
			}else if (inputCommand == -2){
				
				System.out.println("Enter keyword to search: ");
				String searchKeyword = sc.nextLine();
				System.out.println();
				
				searchedItemList = Algorithm.searchItemName(searchKeyword, shopItems);
				gui.setItemsToDisplay(searchedItemList);
				gui.setItemSelected(0);
				recentlySearched = true;
				
			}else if (inputCommand == -4){
				System.out.println("Enter keyword to search (by price): ");
				
				//try catch structure here to ensure that the user's input is valid
				try{
					int searchKeyword = sc.nextInt();
					sc.nextLine();
					searchedItemList = Algorithm.searchItemShopPrice(searchKeyword, shopItems);
					gui.setItemsToDisplay(searchedItemList);
					gui.setItemSelected(0);
					recentlySearched = true;
				}catch(InputMismatchException im){
					System.out.println("Invalid input");
				}
				System.out.println();
				
				
			}else if (inputCommand == -3){
				
				gui.setItemSelected(0);
				
				if (menuSortType == 0){
					menuSortType = 1;
					
					Algorithm.sortItemsAlphabet(shopItems);
					
				}else if (menuSortType == 1){
					menuSortType = 2;
					
					Algorithm.sortItemsPriceMinToMax(shopItems);
					
				}else if (menuSortType == 2){
					menuSortType = 0;
					
					Algorithm.sortItemsPriceMaxToMin(shopItems);
					
				}
				gui.setItemsToDisplay(shopItems);
			}else{
				
				if (player.getMoney() >= shopItems[inputCommand].getPrice()){
					player.setMoney(player.getMoney() - shopItems[inputCommand].getPrice());
					player.getBag().addItem(shopItems[inputCommand]);
					shop.purchaseItem(shopItems[inputCommand]);
					shopItems = shop.getItemsInShop();
					gui.setItemsToDisplay(shopItems);
					gui.setMoney(player.getMoney());
				}
			}
			
		//Inputs for the Character menu
		}else if (screenNumber == ID_CHARACTER_MENU){		
			if (inputCommand == -1){
					if (prevScreen == ID_SUB_MENU){
						openMenu(prevScreen);
					}else if (prevScreen == ID_INVENTORY_ACTION_MENU){
						openMenu(ID_INVENTORY_MENU);
					}
			} else {
				if (prevScreen == ID_INVENTORY_ACTION_MENU) {
					player.useItem(itemSelected, player.getCharacters()[inputCommand]);
					gui.setItemsToDisplay(player.getBag().getAllItems());
					gui.setCharactersToDisplay(player.getCharacters());
					openMenu (ID_INVENTORY_MENU);
				}
			}
		}
	}
	
	//This method initializes the battle
	public static void startGameBoard(int level){
		
		map = new Map(level);
		board = map.getBoard();
		mapHeight = map.getHeight();
		mapWidth = map.getWidth();
		
		startHeroes();
		startAI(map.getEnemies());
		
		unitPositions = new Coordinate[heroList.length + enemyList.length];
		
		//Initializing the unit positions
		for (int i = 0; i < heroList.length; i++){
			unitPositions[i] = heroList[i].getCoordinate();
		}
	
		for (int i = heroList.length; i < unitPositions.length; i++){
			unitPositions[i] = enemyList[i - heroList.length].getCoordinate();
		}
		//Initializing necessary fields in the Algorithm class
		Algorithm.setMap(map);
		Algorithm.setUnitPositions(unitPositions);
	}
	
	//This method initializes the starting location for the player's units
	public static void startHeroes(){
		Coordinate[] placement = map.getHeroSpawnPoints();
		
		player.setNumCharactersToSpawn(placement.length);
		
		heroList = player.getCharacters();
		
		for (int i = 0; i < placement.length; i++){
			heroList[i].setCoordinate(placement[i]);
		}
	}
	
	//This method takes in an array of Unit to initialize the AI
	public static void startAI(Unit[] enemyUnits){
		enemyList = enemyUnits;
		ai = new AI (enemyUnits, player.getCharacters(), map.getAggressionLevel(), null/*,gui*/);
	}
	
	//This method opens the desired menu, and initializes necessary fields (if applicable)
	public static void openMenu(int menuID){
		prevScreen = screenNumber;
		if (menuID != ID_ACTION_MOVE_MENU && menuID != ID_ACTION_ATTACK_MENU && menuID != ID_ACTION_MENU){
			gui.setNormalOrtho();
		}
		
		gui.setMenuButtonSelected(0);
		
		if (menuID == ID_SUB_MENU){
			startGameBoard(player.getCurLevel());
			String[] info = Map.loadMapInfo(player.getCurLevel());
			gui.setMapInfo(info);
		}
		
		if (menuID == ID_INVENTORY_MENU && screenNumber != ID_INVENTORY_ACTION_MENU){
			gui.setItemsToDisplay(player.getBag().getAllItems());
			gui.setMoney(player.getMoney());
			gui.setItemSelected(0);
		}
		
		if (menuID == ID_SHOP_MENU){
			gui.setMoney(player.getMoney());
			shop.loadAllItems(player.getCharacterAverageLevel());
			shopItems = shop.getItemsInShop();
			gui.setItemsToDisplay(shopItems);
			gui.setItemSelected(0);
		}
		
		if (menuID == ID_CHARACTER_MENU){
			gui.setCharactersToDisplay(player.getCharacters());
			System.out.println(player.getCharacters().length);
			gui.setItemSelected(0);
		}
		screenNumber = menuID;
		gui.setScreenID(screenNumber);
	}
	
	//This method returns the Unit that is on the given Coordinate
	private static Unit getUnitOn(Coordinate coordinate){
		for (int i = 0; i < enemyList.length; i++){
			if (enemyList[i].isAlive()){
				if (coordinate.compareToCoordinates(enemyList[i].getCoordinate())){
					return enemyList[i];
				}
			}
		}
		return null;
	}
	
	//This method updates the Coordinate for the unit position in the Main Game, and the Algorithm classes
	public static void updateCoordinates(int index, Coordinate newCoordinate){
		unitPositions[index] = newCoordinate;
		Algorithm.setUnitPositions(unitPositions);
	}
	
	//checkTurnEnd will check if the current turn has ended. If it has, then the AI will take its turn and the next turn will be started
	//in addition, checkTurnEnd also checks if the user has won or lost, if either happens, the game will end
	public static void checkTurnEnd(){
		boolean end = true;
		for (int i = 0; i < heroList.length && end; i++) {
			
			if (heroList[i].isAlive()){
				if (heroList[i].getOtherAction()){
					end = false;
				}
			}
		}
		
		if (checkVictory()) {
			inBattle = false;
			//screenNumber = ID_POST_BATTLE_CUTSCENE;
			//gui.setScreenID(screenNumber);
			screenNumber = ID_POST_BATTLE_CUTSCENE;
			Cutscene.loadCutscene(player.getCurLevel(), false);
			gui.setScreenID(screenNumber);
			gui.initializeCutscene();
			player.setCurLevel(player.getCurLevel()+1);
		}
		
		if (end) {
			ai.play();
			gui.setEnemyPath(ai.getMoveCommands());
			
			totalEnemyAttacks = 0;
			curEnemyAttacker = -1;
			
			for (int i = 0; i < enemyList.length; i++){
				
				if (ai.getAttackCommands(i) != null){
					totalEnemyAttacks++;
					
					if (curEnemyAttacker == -1){
						curEnemyAttacker = i;
					}
				}
			}
			
			if (totalEnemyAttacks > 0){
				gui.setEnemyHasAttackCommand(true);
				screenNumber = ID_POST_BATTLE_CUTSCENE;
				isEnemyAttacking = true;
				//gui.setScreenID(screenNumber);
				String message = ((Enemy)enemyList[curEnemyAttacker]).getEnemyType() + " did " + ai.getAttackCommandsDamage(curEnemyAttacker) + " damage to " + ((Hero)ai.getAttackCommands(curEnemyAttacker)).getName() + "!";
				Cutscene.loadAttackCutscene(((Enemy)enemyList[curEnemyAttacker]).getEnemyID() * -1 - 1, ai.getAttackCommands(curEnemyAttacker).getUnitID(), message, map.getTileNumber(enemyList[curEnemyAttacker].getCoordinate()));
				
				curEnemyAttack = 1;
				
			}else{
				player.resetAllHeroes();
			}

		}
	}
	
	//checkVictory checks if the user has met the victory condition and returns true if the user has and false otherwise
	public static boolean checkVictory(){
		boolean victory = true;
		
		for (int i = 0; i < enemyList.length && victory; i++) {
			if (enemyList[i].isAlive()) {
				victory = false;
			}
		}
		return victory;
	}
	
	//This method checks if the user has lost all of their units
	public static boolean checkDefeat(){
		boolean found = false;
		
		for (int i = gui.getCurUnitSelected() ; i < heroList.length && !found; i++){
			if (heroList[i].isAlive()){
				gui.setCurSelectedUnit(i);
				found = true;
			}
		}
		
		for (int i = 0; i < gui.getCurUnitSelected() && !found; i++){
			if (heroList[i].isAlive()){
				gui.setCurSelectedUnit(i);
				found = true;
			}
		}
		
		return found;
	}
}
