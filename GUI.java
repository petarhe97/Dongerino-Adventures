/*
 Name: GUI.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The GUI class contains the methods associated with updating the graphics and reading user inputs.
*/

import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;

public class GUI {
	
	private int screenID;
	
	//Fields
	
	//Texture arrays
	int[] enemyTextures;
	int[] outlineTextures;
	int[] tileTextures;
	int[] heroTextures;
	int[] heroPlusTextures;
	int[] cutsceneBackgroundTextures;
	int[] menuTextures;
	int[] itemTextures;
	int[][] menuButtonTextures;
	int menuButtonOutlineTexture;
	int backgroundTexture;
	int dialogueBlockTexture;
	int itemMenuOutlineTexture;
	int actionMenuButtonOutlineTexture;
	int movementOutlineTexture;
	int attackOutlineTexture;
	int infoTexture;
	int fontColorBlackTexture;
	
	//Note: the inventory action and action menu buttons have the same background, and thus currently use the same outline texture
	int inventoryActionMenuButtonOutlineTexture;
	
	int menuButtonSelected;
	
	int curMinXDisplay = 0;
	int curMinYDisplay = 0;
	
	int money;
	
	long fps;
	long lastFPS;
	int timer;
	
	//Fields for the cutscenes
	int cutsceneSpeed;
	String dialogue;
	String[] dialogueList;
	String[] dialogueDisplayed;
	int dialogueNum;
	int dialogueLine;
	int cutsceneTimer = 0;
	int animationTimer = 0;
	int numLines;
	int lineNum;
	int cutsceneTimerDelay;
	int curDialogueSegment;
	boolean doneTextDisplay;
	boolean[] uniqueCharacters;
	
	boolean animationRequiredIn;
	boolean animationRequiredOut;
	int animationX0;
	int animationX1;
	int animationIndex;
	
	//Fields for the board
	char[][] board;
	int boardHeight;
	int boardWidth;
	Coordinate cursorPosition;
	Map map;
	
	int unitSelected;
	
	//Fields for the units
	Unit[] units;
	int numHeroes;
	int numEnemies;
	
	//Fields for animations
	boolean hasPath;
	boolean hasEnemyPath;
	boolean enemyHasAttackCommand;
	
	Queue<Coordinate> path;
	
	Queue<Coordinate>[] enemyPaths;
	
	Coordinate[] possibleMoves;
	
	//Fields for the sub menu
	String[] mapInfo;
	
	//Fields for the inventory menu
	int itemSelected = 0;
	int curMinItemView = 0;
	
	Item[] itemList;
	
	//Fields for the character menu
	Unit[] charactersToDisplay;
	
	//Constants
	
	//Constants for the graphics stuff
	final static int V_SYNC = 600;
	final static int SIZE = 62;
	final static int X_RESOLUTION = 810;
	final static int Y_RESOLUTION = 624;
	final static int TIMER_DELAY = V_SYNC / 10;
	
	//Constants for keyboard input
	final static int ACTION_KEY = Keyboard.KEY_A;
	final static int BACK_KEY = Keyboard.KEY_BACK;
	final static int SEARCH_KEY_1 = Keyboard.KEY_LSHIFT;
	final static int SEARCH_KEY_2 = Keyboard.KEY_LCONTROL;
	final static int SORT_KEY = Keyboard.KEY_TAB;
	
	//Enumeration for screen ID's
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
	
	final static int[] NUM_MENU_BUTTONS = {3, 5, 1, 0, 2, 4, 4, 0, 0, 0, 0};
	
	final static int NUM_MENU_TEXTURES = NUM_MENU_BUTTONS.length;
	
	//Constants for the main menu
	final static int MAIN_BUTTON_SIZE_X = 233;
	final static int MAIN_BUTTON_SIZE_Y = 62;
	final static int MAIN_X_MARGIN = X_RESOLUTION / 2 - MAIN_BUTTON_SIZE_X / 2;
	final static int MAIN_Y_MARGIN = 400;
	final static int MAIN_Y_DIST_FROM_BUTTONS = MAIN_BUTTON_SIZE_Y + 10;
	
	//Constants for the sub menu
	final static int SUB_BUTTON_SIZE_X = 233;
	final static int SUB_BUTTON_SIZE_Y = 62;
	final static int SUB_X_MARGIN = 100;
	final static int SUB_Y_MARGIN = 300;
	final static int SUB_Y_DIST_FROM_BUTTONS = SUB_BUTTON_SIZE_Y + 10;
	
	final static int SUB_TEXT_X_MARGIN = 375;
	final static int SUB_TEXT_Y_MARGIN = 255;
	final static int SUB_TEXT_VERTICAL_SPACE = 15;
	
	//Constants for the intermediate menu
		
	//Constants for the inventory menu
	final static int INVENTORY_X_MARGIN = 75;
	final static int INVENTORY_Y_MARGIN = 200;
	final static int INVENTORY_TEXT_VERTICAL_SPACE = 30;
	final static int INVENTORY_TEXT_NUMBER_SPACE = 375;
	final static int INVENTORY_BORDER_VERTICAL_SIZE = 15;
	final static int INVENTORY_CURSOR_LEEWAY = 3;
	final static int INVENTORY_MAX_DISPLAY = 10;
	final static int INVENTORY_IMAGE_X_MARGIN = 500;
	final static int INVENTORY_IMAGE_Y_MARGIN = 325;
	final static int INVENTORY_IMAGE_SIZE = 150;
	final static int INVENTORY_BUTTON_X_MARGIN = 550;
	final static int INVENTORY_BUTTON_Y_MARGIN = 500;
	final static int INVENTORY_BUTTON_X_SIZE = 124;
	final static int INVENTORY_BUTTON_Y_SIZE = 32;
	
	final static int INVENTORY_NUM_TEXTURES = 10;
	final static int INVENTORY_WEAPON_OFFSET_LOW = 1;
	final static int INVENTORY_WEAPON_OFFSET_HIGH = 5;
	
	//Constants for the action menu
	final static int ACTION_KEY_X_SIZE = 124;
	final static int ACTION_KEY_Y_SIZE = 32;
	final static int ACTION_KEY_X_MARGIN = 0;
	final static int ACTION_KEY_Y_MARGIN = 0;
	final static int ACTION_VERTICAL_SPACE = 32;
	final static int ACTION_OUTLINE_SPACE = 2;
	
	final static int NUM_ACTION_MENU_BUTTONS = 6;
	
	//Constants for the load menu
	final static int LOAD_BUTTON_SIZE_X = 233;
	final static int LOAD_BUTTON_SIZE_Y = 62;
	final static int LOAD_X_MARGIN = X_RESOLUTION / 2 - LOAD_BUTTON_SIZE_X / 2;;
	final static int LOAD_Y_MARGIN = 300;
	final static int LOAD_Y_DIST_FROM_BUTTONS = LOAD_BUTTON_SIZE_Y + 10;
	
	//Constants for the cutscenes
	final static int CUTSCENE_X_MARGIN = 50;
	final static int CUTSCENE_Y_MARGIN = 200;
	final static int CUTSCENE_TEXT_X_MARGIN = 150;
	final static int CUTSCENE_TEXT_Y_MARGIN = 150;
	final static int CUTSCENE_TEXT_VERTICAL_SPACE = 15;
	final static int CUTSCENE_PORTRAIT_SIZE = 50;
	final static int CUTSCENE_PORTRAIT_X_MARGIN = CUTSCENE_TEXT_X_MARGIN / 2 - CUTSCENE_PORTRAIT_SIZE / 2;
	final static int CUTSCENE_PORTRAIT_Y_MARGIN = CUTSCENE_TEXT_Y_MARGIN / 2 + CUTSCENE_PORTRAIT_SIZE / 2 + 15;
	
	final static int CUTSCENE_REGULAR_DELAY = V_SYNC / 100;
	final static int CUTSCENE_FAST_DELAY = 0;
	final static int CUTSCENE_ANIMATION_DELAY = 1;
	final static int CUTSCENE_NUM_CHARACTERS_PER_LINE = 71;
	final static int CUTSCENE_TEXTURE_SIZE = 300;
	final static int CUTSCENE_ANIMATION_X_SKIP = 2;
	
	//Constants for the board
	final static int NUM_TILE_TEXTURES = 7;
	
	//Constants for units
	final static int NUM_HERO_TEXTURES = 6;
	final static int NUM_ENEMY_TEXTURES = 6;
	final static int NUM_OUTLINE_TEXTURES = 2;
	
	final static int OUTLINE_TEXTURE_BORDER_SIZE = 2;
	
	//This method takes in a screen ID and sets the GUI's ID
	public void setScreenID(int screen){
		screenID = screen;
	}
	
	//This method sets the selected button to the one in the parameter
	public void setMenuButtonSelected(int newSelection){
		menuButtonSelected = newSelection;
	}
	
	//This method initializes the board, given the board, width, and map
	public void setBoard(char[][] newBoard, int width, int height, Map newMap){
		board = newBoard;
		boardWidth = width;
		boardHeight = height;
		cursorPosition = new Coordinate(units[0].getCoordinate().getX(), units[0].getCoordinate().getY());
		map = newMap;
	}
	
	//This method takes in the information about the map as a string array.
	public void setMapInfo(String[] info){
		mapInfo = info;
	}
	
	//This method initializes the hero unit and enemy unit objects in the GUI
	public void setUnits(Unit[] heroUnits, Unit[] enemyUnits){
		
		numHeroes = heroUnits.length;
		numEnemies = enemyUnits.length;
		units = new Unit[numHeroes + numEnemies];
		
		for (int i = 0; i < numHeroes; i++){
			units[i] = heroUnits[i];
		}
		
		for (int i = numHeroes; i < numEnemies + numHeroes; i++){
			units[i] = enemyUnits[i - numHeroes];
		}
		unitSelected = 0;
	}
	
	//This method takes in an array of Coordinates to the GUI to display the moves that a selected unit can move to
	public void setPossibleMoves(Coordinate[] moves){
		possibleMoves = moves;
	}
	
	//This method returns the a Coordinate object which contains the x and y coordinate of the cursor's position
	public Coordinate getCursorPosition(){
		//Note: creating a new Coordinate is necessary so the unit does not get stuck onto the cursor's position
		return new Coordinate(cursorPosition.getX(), cursorPosition.getY());
	}
	
	//This method updates the current unit selected with the given index
	public void setCurSelectedUnit(int unit){
		unitSelected = unit;
	}
	
	//This method takes in an array of Coordinates to set the animation required for moving
	public void setPath(Coordinate[] pathToTake){
		
		//Setting the path (as an Queue) with the given coordinates
		path = new LinkedList<Coordinate>();
		
		for (int i = pathToTake.length - 1; i >= 0; i--){
			path.add(pathToTake[i]);
		}
		
		hasPath = true;
		
		timer = 0;
	}
	
	//This method takes in a boolean and sets if the enemy has an attack command to the parameter
	public void setEnemyHasAttackCommand(boolean hasAttack){
		enemyHasAttackCommand = hasAttack;
	}
	
	//This method sets the path to take for enemies. It serves a similar purpose as the setPath method
	@SuppressWarnings("unchecked")
	public void setEnemyPath(Coordinate[][] pathToTake){
		enemyPaths = new LinkedList[pathToTake.length];
		for (int i = 0; i < pathToTake.length; i++){
			
			if (pathToTake[i].length > 0){
				if (pathToTake[i][pathToTake[i].length - 1] != null){
					units[i + numHeroes].setCoordinate(pathToTake[i][pathToTake[i].length - 1]);
				}
			}
			enemyPaths[i] = new LinkedList<Coordinate>(); 
			for (int q = pathToTake[i].length - 1; q >= 0; q--){
				
				if (pathToTake[i][q] != null){
				enemyPaths[i].add(pathToTake[i][q]);
				}
			}
		}
		
		hasEnemyPath = true;
		timer = 0;
	}
	
	//This method sets the items to display for the Inventory and Shop menus with an array of items
	public void setItemsToDisplay(Item[] items){
		curMinItemView = 0;
		itemList = items;
	}
	
	//This method sets the current item selected to the given index when in inventory and shop menus
	//This method serves a similar purpose to the setMenuButtonSelected
	public void setItemSelected(int select){
		itemSelected = select;
	}
	
	//This method updates the amount of money that the player has with the given integer value
	public void setMoney(int amount){
		money = amount;
	}
	
	//This method returns the current unit that is selected by the player
	public int getCurUnitSelected(){
		return unitSelected;
	}
	
	//This method sets the player's units that need to be displayed, given in a form of an array of Units, for
	//the character menu
	public void setCharactersToDisplay(Unit[] characters){
		curMinItemView = 0;
		charactersToDisplay = characters;
	}
	
	//This method sets the display window to display from the default settings (i.e. origin is at bottom left corner)
	public void setNormalOrtho(){
		curMinXDisplay = 0;
		curMinYDisplay = 0;
		
		updateOrtho();
	}
	
	//This method initializes the GUI and other necessary fields
	public void start(){

		//Initialize FPS counter
		lastFPS = getTime();
		fps = 0;
		
		//Initialize the display window
		try{
			Display.setDisplayMode(new DisplayMode(X_RESOLUTION, Y_RESOLUTION));
			Display.create();
			
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
		
		//Initialize the graphics stuff
		glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    glOrtho(curMinXDisplay, curMinXDisplay + X_RESOLUTION, curMinYDisplay, curMinYDisplay + Y_RESOLUTION, 1, -1);
	    glMatrixMode(GL_MODELVIEW);
	    glEnable(GL_BLEND);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	    glEnable(GL_TEXTURE_2D);
		
	    //Preload all of the available textures
	    loadAllAvailableTextures();
	    
	    //Begin rendering
	    render();
	    
	}
	
	//Method to render the graphic repeatedly until the game is closed
	public void render(){
		while(!Display.isCloseRequested()){
			//Updates the display window
			Display.update();
			
			//Locks the framerate to the desired framerate (denoted by the constant V_SYNC)
			Display.sync(V_SYNC);
			
			//Update the framerate
			updateFPS();
			
			//Polling keyboard input
			pollInput();
			
			//Rendering the appropriate screen
			if (screenID == ID_MAIN_MENU){
				renderMainMenu();
			}else if (screenID == ID_SUB_MENU){
				renderSubMenu();
			}else if(screenID == ID_INTERMEDIATE_MENU){
				renderIntermediateMenu();
			}else if(screenID == ID_INVENTORY_MENU || screenID == ID_INVENTORY_ACTION_MENU){
				renderInventoryMenu();
				
				if(screenID == ID_INVENTORY_ACTION_MENU){
					renderInventoryActionMenu();
				}
			}else if (screenID == ID_SHOP_MENU ){
				renderShopMenu();
				
			}else if(screenID == ID_CHARACTER_MENU){
				renderCharacterMenu();
			}else if(isCutscene()){
				
				//Checking if an animation is required
				if(animationRequiredOut){
					renderCutsceneAnimation();
					updateAnimationOut();
				}else if (animationRequiredIn){
					renderCutsceneAnimation();
					updateAnimationIn();
				}else{
					renderCutscene();
					if(!doneTextDisplay){
						updateCutsceneTimer();
					}
				}
			}else if (screenID == ID_BATTLE || screenID == ID_ACTION_MENU || screenID == ID_ACTION_MOVE_MENU || screenID == ID_ACTION_ATTACK_MENU){
				renderBoard();
				renderUnits();
				checkOrtho();
				
				//Rendering the animation for movement
				if(hasPath || hasEnemyPath){
					updateMovementTimer();
				}
				
				if (screenID == ID_ACTION_MENU){
					renderActionMenu();
				}
			}
		}
		//Close the display
		Display.destroy();
	}
	
	//Method to update the paths of units if they are moving
	private void updateCoordinates(){
		
		//Update the player's units if they have a path
		if (hasPath){
			
			if (!path.isEmpty()){
				units[unitSelected].setCoordinate(path.poll());
			}else{
				MainGame.updateCoordinates(unitSelected, units[unitSelected].getCoordinate());
				hasPath = false;
			}
		
		//Otherwise, update the enemy units if they have a path
		}else{
			
			
			int check = 0;
			for (int i = 0; i < enemyPaths.length; i++){
				if (!enemyPaths[i].isEmpty()){
					units[numHeroes + i].setCoordinate(enemyPaths[i].poll());
					
				}else{
					check++;
					MainGame.updateCoordinates(numHeroes + i, units[numHeroes + i].getCoordinate());
				}
			}
			
			if (check == enemyPaths.length){
				hasEnemyPath = false;
				for (int i = 0; i < enemyPaths.length; i++){
					MainGame.updateCoordinates(numHeroes + i, units[numHeroes + i].getCoordinate());
				}
				
				if (enemyHasAttackCommand){
					screenID = ID_POST_BATTLE_CUTSCENE;
					initializeCutscene();
					enemyHasAttackCommand = false;
				}
			}
		}
	}
	
	//This method keeps track of the delay for movement and updates the coordinates when necessary
	public void updateMovementTimer(){
		timer++;
	    if(timer == TIMER_DELAY){
	    	timer = 0;
	    	updateCoordinates();
	    }
	}
	
	//Methods to render the different menus and maps
	
	//This method renders the Main menu
	public void renderMainMenu(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_TEXTURE_2D);
		
		//Render the menu background
		glBindTexture(GL_TEXTURE_2D, menuTextures[ID_MAIN_MENU]);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, Y_RESOLUTION);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, Y_RESOLUTION);
			
		}glEnd();
		
		//Render the buttons for the main menu
		//Note: The buttons are drawn from the top button (button 0) to the bottom button (button n - 1)
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_MAIN_MENU]; i++){
			glBindTexture(GL_TEXTURE_2D, menuButtonTextures[ID_MAIN_MENU][i]);
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(MAIN_X_MARGIN, Y_RESOLUTION - MAIN_Y_MARGIN - MAIN_Y_DIST_FROM_BUTTONS * i);
				
				glTexCoord2f(1, 1);
				glVertex2f(MAIN_X_MARGIN + MAIN_BUTTON_SIZE_X, Y_RESOLUTION - MAIN_Y_MARGIN - MAIN_Y_DIST_FROM_BUTTONS * i);
				
				glTexCoord2f(1, 0);
				glVertex2f(MAIN_X_MARGIN + MAIN_BUTTON_SIZE_X, Y_RESOLUTION - MAIN_Y_MARGIN + MAIN_BUTTON_SIZE_Y - MAIN_Y_DIST_FROM_BUTTONS * i);
				
				glTexCoord2f(0, 0);
				glVertex2f(MAIN_X_MARGIN, Y_RESOLUTION - MAIN_Y_MARGIN + MAIN_BUTTON_SIZE_Y - MAIN_Y_DIST_FROM_BUTTONS * i);
				
			}glEnd();
			
		}
		glBindTexture(GL_TEXTURE_2D, menuButtonOutlineTexture);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(MAIN_X_MARGIN, Y_RESOLUTION - MAIN_Y_MARGIN - MAIN_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
			glTexCoord2f(1, 1);
			glVertex2f(MAIN_X_MARGIN + MAIN_BUTTON_SIZE_X, Y_RESOLUTION - MAIN_Y_MARGIN - MAIN_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
			glTexCoord2f(1, 0);
			glVertex2f(MAIN_X_MARGIN + MAIN_BUTTON_SIZE_X, Y_RESOLUTION - MAIN_Y_MARGIN + MAIN_BUTTON_SIZE_Y - MAIN_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
			glTexCoord2f(0, 0);
			glVertex2f(MAIN_X_MARGIN, Y_RESOLUTION - MAIN_Y_MARGIN + MAIN_BUTTON_SIZE_Y - MAIN_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
		}glEnd();
	}
	
	//Method to render the Sub menu
	public void renderSubMenu(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_TEXTURE_2D);
		
		//Render the menu background
		glBindTexture(GL_TEXTURE_2D, menuTextures[ID_SUB_MENU]);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, Y_RESOLUTION);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, Y_RESOLUTION);
			
		}glEnd();
		
		//Render the buttons for the Sub menu
		//Note: The buttons are drawn from the top button (button 0) to the bottom button (button n - 1)
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_SUB_MENU]; i++){
			glBindTexture(GL_TEXTURE_2D, menuButtonTextures[ID_SUB_MENU][i]);
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(SUB_X_MARGIN, Y_RESOLUTION - SUB_Y_MARGIN - SUB_Y_DIST_FROM_BUTTONS * i);
				
				glTexCoord2f(1, 1);
				glVertex2f(SUB_X_MARGIN + SUB_BUTTON_SIZE_X, Y_RESOLUTION - SUB_Y_MARGIN - SUB_Y_DIST_FROM_BUTTONS * i);
				
				glTexCoord2f(1, 0);
				glVertex2f(SUB_X_MARGIN + SUB_BUTTON_SIZE_X, Y_RESOLUTION - SUB_Y_MARGIN + SUB_BUTTON_SIZE_Y - SUB_Y_DIST_FROM_BUTTONS * i);
				
				glTexCoord2f(0, 0);
				glVertex2f(SUB_X_MARGIN, Y_RESOLUTION - SUB_Y_MARGIN + SUB_BUTTON_SIZE_Y - SUB_Y_DIST_FROM_BUTTONS * i);
				
			}glEnd();
			
		}
		glBindTexture(GL_TEXTURE_2D, menuButtonOutlineTexture);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(SUB_X_MARGIN, Y_RESOLUTION - SUB_Y_MARGIN - SUB_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
			glTexCoord2f(1, 1);
			glVertex2f(SUB_X_MARGIN + SUB_BUTTON_SIZE_X, Y_RESOLUTION - SUB_Y_MARGIN - SUB_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
			glTexCoord2f(1, 0);
			glVertex2f(SUB_X_MARGIN + SUB_BUTTON_SIZE_X, Y_RESOLUTION - SUB_Y_MARGIN + SUB_BUTTON_SIZE_Y - SUB_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
			glTexCoord2f(0, 0);
			glVertex2f(SUB_X_MARGIN, Y_RESOLUTION - SUB_Y_MARGIN + SUB_BUTTON_SIZE_Y - SUB_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
		}glEnd();
		
		glBindTexture(GL_TEXTURE_2D, fontColorBlackTexture);
		
		for (int i = 0; i < mapInfo.length; i++){
			SimpleText.drawString(mapInfo[i], SUB_TEXT_X_MARGIN, Y_RESOLUTION - SUB_TEXT_Y_MARGIN - i * SUB_TEXT_VERTICAL_SPACE);
		}
	}
	//Method to render the Intermediate menu
	public void renderIntermediateMenu(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_TEXTURE_2D);
		
		//Render the menu background
		glBindTexture(GL_TEXTURE_2D, menuTextures[ID_INTERMEDIATE_MENU]);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, Y_RESOLUTION);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, Y_RESOLUTION);
			
		}glEnd();
		
		//Render the buttons for the main menu
		//Note: The buttons are drawn from the top button (button 0) to the bottom button (button n - 1)
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_INTERMEDIATE_MENU]; i++){
			glBindTexture(GL_TEXTURE_2D, menuButtonTextures[ID_INTERMEDIATE_MENU][i]);
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(MAIN_X_MARGIN, Y_RESOLUTION - MAIN_Y_MARGIN - MAIN_Y_DIST_FROM_BUTTONS * i);
				
				glTexCoord2f(1, 1);
				glVertex2f(MAIN_X_MARGIN + MAIN_BUTTON_SIZE_X, Y_RESOLUTION - MAIN_Y_MARGIN - MAIN_Y_DIST_FROM_BUTTONS * i);
				
				glTexCoord2f(1, 0);
				glVertex2f(MAIN_X_MARGIN + MAIN_BUTTON_SIZE_X, Y_RESOLUTION - MAIN_Y_MARGIN + MAIN_BUTTON_SIZE_Y - MAIN_Y_DIST_FROM_BUTTONS * i);
				
				glTexCoord2f(0, 0);
				glVertex2f(MAIN_X_MARGIN, Y_RESOLUTION - MAIN_Y_MARGIN + MAIN_BUTTON_SIZE_Y - MAIN_Y_DIST_FROM_BUTTONS * i);
				
			}glEnd();
			
		}
		glBindTexture(GL_TEXTURE_2D, menuButtonOutlineTexture);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(MAIN_X_MARGIN, Y_RESOLUTION - MAIN_Y_MARGIN - MAIN_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
			glTexCoord2f(1, 1);
			glVertex2f(MAIN_X_MARGIN + MAIN_BUTTON_SIZE_X, Y_RESOLUTION - MAIN_Y_MARGIN - MAIN_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
			glTexCoord2f(1, 0);
			glVertex2f(MAIN_X_MARGIN + MAIN_BUTTON_SIZE_X, Y_RESOLUTION - MAIN_Y_MARGIN + MAIN_BUTTON_SIZE_Y - MAIN_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
			glTexCoord2f(0, 0);
			glVertex2f(MAIN_X_MARGIN, Y_RESOLUTION - MAIN_Y_MARGIN + MAIN_BUTTON_SIZE_Y - MAIN_Y_DIST_FROM_BUTTONS * menuButtonSelected);
			
		}glEnd();
	}
	
	//This method renders a cutscene
	public void renderCutscene(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, backgroundTexture);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, Y_RESOLUTION);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, Y_RESOLUTION);

		}glEnd();
		
		
		if (Cutscene.getCharacters(curDialogueSegment)[0] >= 0){
			glBindTexture(GL_TEXTURE_2D, heroTextures[Cutscene.getCharacters(curDialogueSegment)[0]]);
		}else{
			glBindTexture(GL_TEXTURE_2D, enemyTextures[Math.abs(Cutscene.getCharacters(curDialogueSegment)[0]) - 1]);
		}
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(CUTSCENE_X_MARGIN, CUTSCENE_Y_MARGIN);
			
			glTexCoord2f(1, 1);
			glVertex2f(CUTSCENE_TEXTURE_SIZE + CUTSCENE_X_MARGIN, CUTSCENE_Y_MARGIN);
			
			glTexCoord2f(1, 0);
			glVertex2f(CUTSCENE_TEXTURE_SIZE + CUTSCENE_X_MARGIN, CUTSCENE_TEXTURE_SIZE + CUTSCENE_Y_MARGIN);
			
			glTexCoord2f(0, 0);
			glVertex2f(CUTSCENE_X_MARGIN, CUTSCENE_TEXTURE_SIZE + CUTSCENE_Y_MARGIN);

		}glEnd();
		
		if (Cutscene.getCharacters(curDialogueSegment).length == 2){
			
			if (Cutscene.getCharacters(curDialogueSegment)[1] >= 0){
				glBindTexture(GL_TEXTURE_2D, heroTextures[Cutscene.getCharacters(curDialogueSegment)[1]]);
			}else{
				glBindTexture(GL_TEXTURE_2D, enemyTextures[Math.abs(Cutscene.getCharacters(curDialogueSegment)[1]) - 1]);
			}
			
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(X_RESOLUTION - CUTSCENE_X_MARGIN, CUTSCENE_Y_MARGIN);

				glTexCoord2f(1, 1);
				glVertex2f(X_RESOLUTION - CUTSCENE_TEXTURE_SIZE - CUTSCENE_X_MARGIN, CUTSCENE_Y_MARGIN);

				glTexCoord2f(1, 0);
				glVertex2f(X_RESOLUTION - CUTSCENE_TEXTURE_SIZE - CUTSCENE_X_MARGIN, CUTSCENE_TEXTURE_SIZE + CUTSCENE_Y_MARGIN);

				glTexCoord2f(0, 0);
				glVertex2f(X_RESOLUTION - CUTSCENE_X_MARGIN, CUTSCENE_TEXTURE_SIZE + CUTSCENE_Y_MARGIN);
				
			}glEnd();
		}
		
		glBindTexture(GL_TEXTURE_2D, dialogueBlockTexture);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, CUTSCENE_Y_MARGIN);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, CUTSCENE_Y_MARGIN);
			
		}glEnd();
		
		if (Cutscene.getSpeaker(curDialogueSegment) >= 0){
			glBindTexture(GL_TEXTURE_2D, heroTextures[Cutscene.getSpeaker(curDialogueSegment)]);
		}else if (Cutscene.getSpeaker(curDialogueSegment) < -1){
			glBindTexture(GL_TEXTURE_2D, enemyTextures[Math.abs(Cutscene.getSpeaker(curDialogueSegment)) - 1]);
		}else{
			glBindTexture(GL_TEXTURE_2D, dialogueBlockTexture);
		}
		
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(CUTSCENE_PORTRAIT_X_MARGIN, CUTSCENE_PORTRAIT_Y_MARGIN);
			
			glTexCoord2f(1, 1);
			glVertex2f(CUTSCENE_PORTRAIT_X_MARGIN + CUTSCENE_PORTRAIT_SIZE, CUTSCENE_PORTRAIT_Y_MARGIN);
			
			glTexCoord2f(1, 0);
			glVertex2f(CUTSCENE_PORTRAIT_X_MARGIN + CUTSCENE_PORTRAIT_SIZE, CUTSCENE_PORTRAIT_Y_MARGIN + CUTSCENE_PORTRAIT_SIZE);
			
			glTexCoord2f(0, 0);
			glVertex2f(CUTSCENE_PORTRAIT_X_MARGIN, CUTSCENE_PORTRAIT_Y_MARGIN + CUTSCENE_PORTRAIT_SIZE);
			
		}glEnd();
		
		
		glDisable(GL_TEXTURE_2D);
		
		for (int i = 0; i < dialogueList.length;i ++){
			SimpleText.drawString(dialogueDisplayed[i], CUTSCENE_TEXT_X_MARGIN, CUTSCENE_TEXT_Y_MARGIN - (i * CUTSCENE_TEXT_VERTICAL_SPACE));
		}
	}
	
	//Method to render the cutscene animations
	public void renderCutsceneAnimation(){
		
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, backgroundTexture);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, Y_RESOLUTION);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, Y_RESOLUTION);

		}glEnd();
		
		if (Cutscene.getCharacters(animationIndex)[0] >= 0){
			glBindTexture(GL_TEXTURE_2D, heroTextures[Cutscene.getCharacters(animationIndex)[0]]);
		}else{
			glBindTexture(GL_TEXTURE_2D, enemyTextures[Math.abs(Cutscene.getCharacters(animationIndex)[0]) - 1]);
		}
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(animationX0, CUTSCENE_Y_MARGIN);
			
			glTexCoord2f(1, 1);
			glVertex2f(CUTSCENE_TEXTURE_SIZE + animationX0, CUTSCENE_Y_MARGIN);
			
			glTexCoord2f(1, 0);
			glVertex2f(CUTSCENE_TEXTURE_SIZE + animationX0, CUTSCENE_TEXTURE_SIZE + CUTSCENE_Y_MARGIN);
			
			glTexCoord2f(0, 0);
			glVertex2f(animationX0, CUTSCENE_TEXTURE_SIZE + CUTSCENE_Y_MARGIN);
		}glEnd();
		
		if (Cutscene.getCharacters(animationIndex).length == 2){
			if (Cutscene.getCharacters(animationIndex)[1] >= 0){
				glBindTexture(GL_TEXTURE_2D, heroTextures[Cutscene.getCharacters(animationIndex)[1]]);
			}else{
				glBindTexture(GL_TEXTURE_2D, enemyTextures[Math.abs(Cutscene.getCharacters(animationIndex)[1]) - 1]);
			}
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(animationX1, CUTSCENE_Y_MARGIN);

				glTexCoord2f(1, 1);
				glVertex2f(animationX1 - CUTSCENE_TEXTURE_SIZE - CUTSCENE_X_MARGIN, CUTSCENE_Y_MARGIN);

				glTexCoord2f(1, 0);
				glVertex2f(animationX1 - CUTSCENE_TEXTURE_SIZE - CUTSCENE_X_MARGIN, CUTSCENE_TEXTURE_SIZE + CUTSCENE_Y_MARGIN);

				glTexCoord2f(0, 0);
				glVertex2f(animationX1, CUTSCENE_TEXTURE_SIZE + CUTSCENE_Y_MARGIN);
				
			}glEnd();
		}
		
		glBindTexture(GL_TEXTURE_2D, dialogueBlockTexture);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, CUTSCENE_Y_MARGIN);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, CUTSCENE_Y_MARGIN);
			
		}glEnd();
	}
	
	//Method to render the map when in a battle
	public void renderBoard(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_TEXTURE_2D);
		int tile = 0;
		
		for (int q = boardHeight - 1; q >= 0; q--){
			for (int i = 0; i < boardWidth; i++){
				
				if (board[i][q] == 'g'){
					tile = 0;
				}else if (board[i][q] == 'f'){
					tile = 1;
				}else if (board[i][q] == 'w'){
					tile = 2;
				}else if (board[i][q] == 's'){
					tile = 3;
				}else if (board[i][q] == 'm'){
					tile = 4;
				}else if (board[i][q] == 'b'){
					tile = 5;
				}else if (board[i][q] == 'v'){
					tile = 6;
				}
				
				glBindTexture(GL_TEXTURE_2D, tileTextures[tile]);
				glBegin(GL_QUADS);{
					glTexCoord2f(0, 1);
					glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + i * SIZE, OUTLINE_TEXTURE_BORDER_SIZE + q * SIZE);
					
					glTexCoord2f(1, 1);
			        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + i * SIZE + SIZE, OUTLINE_TEXTURE_BORDER_SIZE + q * SIZE);
			        
			        glTexCoord2f(1, 0);
			        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + i * SIZE + SIZE, OUTLINE_TEXTURE_BORDER_SIZE + q * SIZE + SIZE);
			        
			        glTexCoord2f(0, 0);
			        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + i * SIZE, OUTLINE_TEXTURE_BORDER_SIZE + q * SIZE + SIZE);

				}glEnd();
			}
		}
	}
	
	//Method to render the units when in a battle
	public void renderUnits(){
		glEnable(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, outlineTextures[0]);
		glBegin(GL_QUADS);{
			glTexCoord2f(0, 1);
			glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + units[unitSelected].getCoordinate().getX() * SIZE - 2, OUTLINE_TEXTURE_BORDER_SIZE + units[unitSelected].getCoordinate().getY() * SIZE - 2);
			
			glTexCoord2f(1, 1);
	        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + units[unitSelected].getCoordinate().getX() * SIZE + SIZE + 2, OUTLINE_TEXTURE_BORDER_SIZE + units[unitSelected].getCoordinate().getY() * SIZE - 2);
	        
	        glTexCoord2f(1, 0);
	        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + units[unitSelected].getCoordinate().getX() * SIZE + SIZE + 2, OUTLINE_TEXTURE_BORDER_SIZE + units[unitSelected].getCoordinate().getY() * SIZE + SIZE + 2);
	        
	        glTexCoord2f(0, 0);
	        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + units[unitSelected].getCoordinate().getX() * SIZE - 2, OUTLINE_TEXTURE_BORDER_SIZE + units[unitSelected].getCoordinate().getY() * SIZE + SIZE + 2);
	    
		}glEnd();
		
		glBindTexture(GL_TEXTURE_2D, outlineTextures[1]);
		glBegin(GL_QUADS);{
			glTexCoord2f(0, 1);
			glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getX() * SIZE - 2, OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getY() * SIZE - 2);
			
			glTexCoord2f(1, 1);
	        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getX() * SIZE + SIZE + 2, OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getY() * SIZE - 2);
	        
	        glTexCoord2f(1, 0);
	        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getX() * SIZE + SIZE + 2, OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getY() * SIZE + SIZE + 2);
	        
	        glTexCoord2f(0, 0);
	        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getX() * SIZE - 2, OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getY() * SIZE + SIZE + 2);
	    
		}glEnd();
		
		for (int i = 0; i < units.length; i++){
			
			if (units[i].isAlive()){
				if (units[i] instanceof Hero){
					if (!((Hero)units[i]).getRank()){
						glBindTexture(GL_TEXTURE_2D, heroTextures[((Hero)units[i]).getUnitID()]);
					}else{
						glBindTexture(GL_TEXTURE_2D, heroPlusTextures[((Hero)units[i]).getUnitID()]);
					}
				}else{
					glBindTexture(GL_TEXTURE_2D, enemyTextures[((Enemy)units[i]).getEnemyID()]);
				}
				
				glBegin(GL_QUADS);{
					glTexCoord2f(0, 1);
					glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + units[i].getCoordinate().getX() * SIZE, OUTLINE_TEXTURE_BORDER_SIZE + units[i].getCoordinate().getY() * SIZE);
					
					glTexCoord2f(1, 1);
			        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + units[i].getCoordinate().getX() * SIZE + SIZE, OUTLINE_TEXTURE_BORDER_SIZE + units[i].getCoordinate().getY() * SIZE);
			        
			        glTexCoord2f(1, 0);
			        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + units[i].getCoordinate().getX() * SIZE + SIZE, OUTLINE_TEXTURE_BORDER_SIZE + units[i].getCoordinate().getY() * SIZE + SIZE);
			       
			        glTexCoord2f(0, 0);
			        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + units[i].getCoordinate().getX() * SIZE, OUTLINE_TEXTURE_BORDER_SIZE + units[i].getCoordinate().getY() * SIZE + SIZE);
			   
				}glEnd();
			}
		}
		
		if (screenID == ID_ACTION_MOVE_MENU){
			glBindTexture(GL_TEXTURE_2D, movementOutlineTexture);
			for (int i = 0; i < possibleMoves.length; i++){
				
				glBegin(GL_QUADS);{
					
					glTexCoord2f(0, 1);
					glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + possibleMoves[i].getX() * SIZE, OUTLINE_TEXTURE_BORDER_SIZE + possibleMoves[i].getY() * SIZE);
					
					glTexCoord2f(1, 1);
			        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + possibleMoves[i].getX() * SIZE + SIZE, OUTLINE_TEXTURE_BORDER_SIZE + possibleMoves[i].getY() * SIZE);
			        
			        glTexCoord2f(1, 0);
			        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + possibleMoves[i].getX() * SIZE + SIZE, OUTLINE_TEXTURE_BORDER_SIZE + possibleMoves[i].getY() * SIZE + SIZE);
			       
			        glTexCoord2f(0, 0);
			        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + possibleMoves[i].getX() * SIZE, OUTLINE_TEXTURE_BORDER_SIZE + possibleMoves[i].getY() * SIZE + SIZE);
					
				}glEnd();
			}
		}else if (screenID == ID_ACTION_ATTACK_MENU){
			glBindTexture(GL_TEXTURE_2D, attackOutlineTexture);
			for (int i = -units[unitSelected].getRange(); i <= units[unitSelected].getRange(); i++){
				for (int q = -units[unitSelected].getRange(); q <= units[unitSelected].getRange(); q++){
					
					if (Math.abs(i) + Math.abs(q) <= units[unitSelected].getRange() && Math.abs(i) + Math.abs(q) != 0){
						
						glBegin(GL_QUADS);{
							
							glTexCoord2f(0, 1);
							glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + (units[unitSelected].getCoordinate().getX() + i) * SIZE, OUTLINE_TEXTURE_BORDER_SIZE + (units[unitSelected].getCoordinate().getY() + q) * SIZE);
							
							glTexCoord2f(1, 1);
					        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + (units[unitSelected].getCoordinate().getX() + i) * SIZE + SIZE, OUTLINE_TEXTURE_BORDER_SIZE + (units[unitSelected].getCoordinate().getY() + q) * SIZE);
					        
					        glTexCoord2f(1, 0);
					        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + (units[unitSelected].getCoordinate().getX() + i) * SIZE + SIZE, OUTLINE_TEXTURE_BORDER_SIZE + (units[unitSelected].getCoordinate().getY() + q) * SIZE + SIZE);
					       
					        glTexCoord2f(0, 0);
					        glVertex2f(OUTLINE_TEXTURE_BORDER_SIZE + (units[unitSelected].getCoordinate().getX() + i) * SIZE, OUTLINE_TEXTURE_BORDER_SIZE + (units[unitSelected].getCoordinate().getY() + q) * SIZE + SIZE);
							
						}glEnd();
					}
				}
			}
		}
		
		boolean exit = false;
		
		for (int i = 0; i < numEnemies && !exit; i++){
			if (units[i + numHeroes].isAlive()){
				if (cursorPosition.compareToCoordinates(units[i + numHeroes].getCoordinate())){
					exit = true;
					glBindTexture(GL_TEXTURE_2D, infoTexture);
					glBegin(GL_QUADS);{
						
						glTexCoord2f(0, 1);
						glVertex2f(curMinXDisplay, Y_RESOLUTION + curMinYDisplay - SIZE);
						
						glTexCoord2f(1, 1);
				        glVertex2f(curMinXDisplay + 4 * SIZE, Y_RESOLUTION + curMinYDisplay - SIZE);
				        
				        glTexCoord2f(1, 0);
				        glVertex2f(curMinXDisplay + 4 * SIZE, Y_RESOLUTION + curMinYDisplay);
				        
				        glTexCoord2f(0, 0);
				        glVertex2f(curMinXDisplay, Y_RESOLUTION + curMinYDisplay);
						
					}glEnd();
					
					glBindTexture(GL_TEXTURE_2D, enemyTextures[((Enemy)units[i + numHeroes]).getEnemyID()]);
					glBegin(GL_QUADS);{
						
						glTexCoord2f(0, 1);
						glVertex2f(curMinXDisplay + 10, Y_RESOLUTION + curMinYDisplay - 62 + 2);
						
						glTexCoord2f(1, 1);
				        glVertex2f(curMinXDisplay + 72, Y_RESOLUTION + curMinYDisplay - 62 + 2);
				        
				        glTexCoord2f(1, 0);
				        glVertex2f(curMinXDisplay + 72, Y_RESOLUTION + curMinYDisplay - 2);
				        
				        glTexCoord2f(0, 0);
				        glVertex2f(curMinXDisplay + 10, Y_RESOLUTION + curMinYDisplay - 2);
						
					}glEnd();
					
					glDisable(GL_TEXTURE_2D);
					SimpleText.drawString(((Enemy)units[i + numHeroes]).getEnemyType(), curMinXDisplay + 100, Y_RESOLUTION + curMinYDisplay - 15);
					
					SimpleText.drawString("HP" + units[i + numHeroes].getHealth() + "/" + units[i + numHeroes].getMaxHP(), curMinXDisplay + 100, Y_RESOLUTION + curMinYDisplay - 35);
					
					glEnable(GL_TEXTURE_2D);
				}
			}
		}
		
		for (int i = 0; i < numHeroes && !exit; i++){
			if (units[i].isAlive()){
				if (cursorPosition.compareToCoordinates(units[i].getCoordinate())){
					exit = true;
					glBindTexture(GL_TEXTURE_2D, infoTexture);
					glBegin(GL_QUADS);{
						
						glTexCoord2f(0, 1);
						glVertex2f(curMinXDisplay, Y_RESOLUTION + curMinYDisplay - SIZE);
						
						glTexCoord2f(1, 1);
				        glVertex2f(curMinXDisplay + 4 * SIZE, Y_RESOLUTION + curMinYDisplay - SIZE);
				        
				        glTexCoord2f(1, 0);
				        glVertex2f(curMinXDisplay + 4 * SIZE, Y_RESOLUTION + curMinYDisplay);
				        
				        glTexCoord2f(0, 0);
				        glVertex2f(curMinXDisplay, Y_RESOLUTION + curMinYDisplay);
						
					}glEnd();
					
					if (!((Hero)units[i]).getRank()){
						glBindTexture(GL_TEXTURE_2D, heroTextures[((Hero)units[i]).getUnitID()]);
					}else{
						glBindTexture(GL_TEXTURE_2D, heroPlusTextures[((Hero)units[i]).getUnitID()]);
					}
					glBegin(GL_QUADS);{
						
						glTexCoord2f(0, 1);
						glVertex2f(curMinXDisplay + 10, Y_RESOLUTION + curMinYDisplay - 62 + 2);
						
						glTexCoord2f(1, 1);
				        glVertex2f(curMinXDisplay + 72, Y_RESOLUTION + curMinYDisplay - 62 + 2);
				        
				        glTexCoord2f(1, 0);
				        glVertex2f(curMinXDisplay + 72, Y_RESOLUTION + curMinYDisplay - 2);
				        
				        glTexCoord2f(0, 0);
				        glVertex2f(curMinXDisplay + 10, Y_RESOLUTION + curMinYDisplay - 2);
						
					}glEnd();
					
					glDisable(GL_TEXTURE_2D);
					SimpleText.drawString(((Hero)units[i]).getName(), curMinXDisplay + 100, Y_RESOLUTION + curMinYDisplay - 15);
					SimpleText.drawString("HP" + units[i].getHealth() + "/" + units[i].getMaxHP(), curMinXDisplay + 100, Y_RESOLUTION + curMinYDisplay - 35);
					
					glEnable(GL_TEXTURE_2D);
				}
			}
		}
	}
	
	//Method to render the Action menu
	public void renderActionMenu(){
		
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_ACTION_MENU]; i++){
			
			glBindTexture(GL_TEXTURE_2D, menuButtonTextures[ID_ACTION_MENU][i]);
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(X_RESOLUTION + curMinXDisplay - ACTION_KEY_X_MARGIN - ACTION_KEY_X_SIZE, Y_RESOLUTION + curMinYDisplay - ACTION_KEY_Y_MARGIN - ACTION_KEY_Y_SIZE - i * ACTION_VERTICAL_SPACE);
				
				glTexCoord2f(1, 1);
				glVertex2f(X_RESOLUTION + curMinXDisplay - ACTION_KEY_X_MARGIN, Y_RESOLUTION + curMinYDisplay - ACTION_KEY_Y_MARGIN - ACTION_KEY_Y_SIZE - i * ACTION_VERTICAL_SPACE);
				
				glTexCoord2f(1, 0);
				glVertex2f(X_RESOLUTION + curMinXDisplay - ACTION_KEY_X_MARGIN, Y_RESOLUTION + curMinYDisplay - ACTION_KEY_Y_MARGIN - i * ACTION_VERTICAL_SPACE);
				
				glTexCoord2f(0, 0);
				glVertex2f(X_RESOLUTION + curMinXDisplay - ACTION_KEY_X_MARGIN - ACTION_KEY_X_SIZE, Y_RESOLUTION + curMinYDisplay - ACTION_KEY_Y_MARGIN - i * ACTION_VERTICAL_SPACE);
				
				
			}glEnd();
		}
		
		glBindTexture(GL_TEXTURE_2D, actionMenuButtonOutlineTexture);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(X_RESOLUTION + curMinXDisplay - ACTION_KEY_X_MARGIN - ACTION_KEY_X_SIZE - ACTION_OUTLINE_SPACE, Y_RESOLUTION + curMinYDisplay - ACTION_KEY_Y_MARGIN - ACTION_KEY_Y_SIZE - menuButtonSelected * ACTION_VERTICAL_SPACE);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION + curMinXDisplay - ACTION_KEY_X_MARGIN + ACTION_OUTLINE_SPACE, Y_RESOLUTION + curMinYDisplay - ACTION_KEY_Y_MARGIN - ACTION_KEY_Y_SIZE - menuButtonSelected * ACTION_VERTICAL_SPACE);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION + curMinXDisplay - ACTION_KEY_X_MARGIN + ACTION_OUTLINE_SPACE, Y_RESOLUTION + curMinYDisplay - ACTION_KEY_Y_MARGIN - menuButtonSelected * ACTION_VERTICAL_SPACE);
			
			glTexCoord2f(0, 0);
			glVertex2f(X_RESOLUTION + curMinXDisplay - ACTION_KEY_X_MARGIN - ACTION_KEY_X_SIZE - ACTION_OUTLINE_SPACE, Y_RESOLUTION + curMinYDisplay - ACTION_KEY_Y_MARGIN - menuButtonSelected * ACTION_VERTICAL_SPACE);
			
		}glEnd();
	}
	
	//Method to render the Inventory menu
	public void renderInventoryMenu(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, menuTextures[ID_INVENTORY_MENU]);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, Y_RESOLUTION);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, Y_RESOLUTION);
			
		}glEnd();
		if (itemList.length > 0){
			
			glBindTexture(GL_TEXTURE_2D, itemMenuOutlineTexture);
			glBegin(GL_QUADS);{
				glTexCoord2f(0, 1);
				glVertex2f(INVENTORY_X_MARGIN - OUTLINE_TEXTURE_BORDER_SIZE, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE - OUTLINE_TEXTURE_BORDER_SIZE - INVENTORY_CURSOR_LEEWAY);
				
				glTexCoord2f(1, 1);
				glVertex2f(INVENTORY_X_MARGIN + INVENTORY_TEXT_NUMBER_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + 15, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE - OUTLINE_TEXTURE_BORDER_SIZE - INVENTORY_CURSOR_LEEWAY);
				
				glTexCoord2f(1, 0);
				glVertex2f(INVENTORY_X_MARGIN + INVENTORY_TEXT_NUMBER_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + 15, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + INVENTORY_BORDER_VERTICAL_SIZE);
				
				glTexCoord2f(0, 0);
				glVertex2f(INVENTORY_X_MARGIN - OUTLINE_TEXTURE_BORDER_SIZE, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + INVENTORY_BORDER_VERTICAL_SIZE);
				
			}glEnd();
			
			if (itemList[itemSelected  + curMinItemView] instanceof Weapon){
				glBindTexture(GL_TEXTURE_2D, itemTextures[((Weapon)itemList[itemSelected + curMinItemView]).getClassPossible() + INVENTORY_WEAPON_OFFSET_LOW]);
			}else if (itemList[itemSelected  + curMinItemView] instanceof HealthPotion){
				glBindTexture(GL_TEXTURE_2D, itemTextures[0]);
			}else{
				glBindTexture(GL_TEXTURE_2D, itemTextures[itemList[itemSelected + curMinItemView].getItemID() + INVENTORY_WEAPON_OFFSET_HIGH]);
			}
			
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN);
				
				glTexCoord2f(1, 1);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN + INVENTORY_IMAGE_SIZE, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN);
				
				glTexCoord2f(1, 0);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN + INVENTORY_IMAGE_SIZE, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN + INVENTORY_IMAGE_SIZE);
				
				glTexCoord2f(0, 0);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN + INVENTORY_IMAGE_SIZE);
				
			}glEnd();
			
			glDisable(GL_TEXTURE_2D);
			for (int i = curMinItemView; i < curMinItemView + INVENTORY_MAX_DISPLAY && i < itemList.length; i++){
				SimpleText.drawString(itemList[i].getName(), INVENTORY_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - (i - curMinItemView) * INVENTORY_TEXT_VERTICAL_SPACE);
			}
			
			SimpleText.drawString("Level Requirement: " + itemList[itemSelected + curMinItemView].getLevelRequired(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE);
			SimpleText.drawString("Primary Statistic: " + itemList[itemSelected + curMinItemView].getPrimaryStat(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE / 2);
			SimpleText.drawString("Sale Value: " + itemList[itemSelected + curMinItemView].getSalePrice(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE / 2 * 3);
			
			SimpleText.drawString("Current money: " + money, INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_IMAGE_SIZE - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE * 5);
			
			if (itemList[itemSelected + curMinItemView] instanceof Weapon){
				SimpleText.drawString("Class Required: " + Hero.classNumToString(((Weapon)itemList[itemSelected + curMinItemView]).getClassPossible()), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE );
			}
		}
	}
	
	//Method to render the Inventory Action menu
	public void renderInventoryActionMenu(){
		
		glEnable(GL_TEXTURE_2D);
		
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_INVENTORY_ACTION_MENU]; i++){
			glBindTexture(GL_TEXTURE_2D, menuButtonTextures[ID_INVENTORY_ACTION_MENU][i]);
			glBegin(GL_QUADS);{
					
				glTexCoord2f(0, 1);
				glVertex2f(INVENTORY_BUTTON_X_MARGIN, Y_RESOLUTION - INVENTORY_BUTTON_Y_MARGIN - i * INVENTORY_BUTTON_Y_SIZE);
				
				glTexCoord2f(1, 1);
				glVertex2f(INVENTORY_BUTTON_X_MARGIN + INVENTORY_BUTTON_X_SIZE, Y_RESOLUTION - INVENTORY_BUTTON_Y_MARGIN  - i * INVENTORY_BUTTON_Y_SIZE);
				
				glTexCoord2f(1, 0);
				glVertex2f(INVENTORY_BUTTON_X_MARGIN + INVENTORY_BUTTON_X_SIZE, Y_RESOLUTION - INVENTORY_BUTTON_Y_MARGIN - (i - 1) * INVENTORY_BUTTON_Y_SIZE);
				
				glTexCoord2f(0, 0);
				glVertex2f(INVENTORY_BUTTON_X_MARGIN, Y_RESOLUTION - INVENTORY_BUTTON_Y_MARGIN - (i - 1) * INVENTORY_BUTTON_Y_SIZE);

			}glEnd();
			
			glBindTexture(GL_TEXTURE_2D, inventoryActionMenuButtonOutlineTexture);
			glBegin(GL_QUADS);{
					
				glTexCoord2f(0, 1);
				glVertex2f(INVENTORY_BUTTON_X_MARGIN, Y_RESOLUTION - INVENTORY_BUTTON_Y_MARGIN - INVENTORY_BUTTON_Y_SIZE * menuButtonSelected);
				
				glTexCoord2f(1, 1);
				glVertex2f(INVENTORY_BUTTON_X_MARGIN + INVENTORY_BUTTON_X_SIZE, Y_RESOLUTION - INVENTORY_BUTTON_Y_MARGIN  - INVENTORY_BUTTON_Y_SIZE * menuButtonSelected);
				
				glTexCoord2f(1, 0);
				glVertex2f(INVENTORY_BUTTON_X_MARGIN + INVENTORY_BUTTON_X_SIZE, Y_RESOLUTION - INVENTORY_BUTTON_Y_MARGIN - INVENTORY_BUTTON_Y_SIZE * (menuButtonSelected - 1));
				
				glTexCoord2f(0, 0);
				glVertex2f(INVENTORY_BUTTON_X_MARGIN, Y_RESOLUTION - INVENTORY_BUTTON_Y_MARGIN - INVENTORY_BUTTON_Y_SIZE * (menuButtonSelected - 1));

			}glEnd();
		}

	}
	
	//Method to render the Load menu
	//Note: this method is currently not used because there is no load menu. However, there will hopefully be future
	//plans to use this method and incorporate multiple save files
	public void renderLoadMenu(){
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_TEXTURE_2D);
		
		//Render the menu background
		glBindTexture(GL_TEXTURE_2D, menuTextures[ID_LOAD_MENU]);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, Y_RESOLUTION);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, Y_RESOLUTION);
			
		}glEnd();
		
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_LOAD_MENU]; i++){
			glBindTexture(GL_TEXTURE_2D, menuButtonTextures[ID_LOAD_MENU][i]);
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(LOAD_X_MARGIN, Y_RESOLUTION - LOAD_Y_MARGIN - i * LOAD_Y_DIST_FROM_BUTTONS);
				
				glTexCoord2f(1, 1);
				glVertex2f(LOAD_X_MARGIN + LOAD_BUTTON_SIZE_X, Y_RESOLUTION - LOAD_Y_MARGIN - i * LOAD_Y_DIST_FROM_BUTTONS);
				
				glTexCoord2f(1, 0);
				glVertex2f(LOAD_X_MARGIN + LOAD_BUTTON_SIZE_X, Y_RESOLUTION - LOAD_Y_MARGIN + LOAD_BUTTON_SIZE_Y - i * LOAD_Y_DIST_FROM_BUTTONS);
				
				glTexCoord2f(0, 0);
				glVertex2f(LOAD_X_MARGIN, Y_RESOLUTION - LOAD_Y_MARGIN + LOAD_BUTTON_SIZE_Y - i * LOAD_Y_DIST_FROM_BUTTONS);
				
			}glEnd();
			
		}
		
		glBindTexture(GL_TEXTURE_2D, menuButtonOutlineTexture);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(LOAD_X_MARGIN, Y_RESOLUTION - LOAD_Y_MARGIN - menuButtonSelected * LOAD_Y_DIST_FROM_BUTTONS);
			
			glTexCoord2f(1, 1);
			glVertex2f(LOAD_X_MARGIN + LOAD_BUTTON_SIZE_X, Y_RESOLUTION - LOAD_Y_MARGIN - menuButtonSelected * LOAD_Y_DIST_FROM_BUTTONS);
			
			glTexCoord2f(1, 0);
			glVertex2f(LOAD_X_MARGIN + LOAD_BUTTON_SIZE_X, Y_RESOLUTION - LOAD_Y_MARGIN + LOAD_BUTTON_SIZE_Y - menuButtonSelected * LOAD_Y_DIST_FROM_BUTTONS);
			
			glTexCoord2f(0, 0);
			glVertex2f(LOAD_X_MARGIN, Y_RESOLUTION - LOAD_Y_MARGIN + LOAD_BUTTON_SIZE_Y - menuButtonSelected * LOAD_Y_DIST_FROM_BUTTONS);
			
		}glEnd();
	}
	
	//Method to render the Shop menu
	public void renderShopMenu(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, menuTextures[ID_SHOP_MENU]);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, Y_RESOLUTION);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, Y_RESOLUTION);
			
		}glEnd();
		if (itemList.length > 0){
			
			glBindTexture(GL_TEXTURE_2D, itemMenuOutlineTexture);
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(INVENTORY_X_MARGIN - OUTLINE_TEXTURE_BORDER_SIZE, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE - OUTLINE_TEXTURE_BORDER_SIZE - INVENTORY_CURSOR_LEEWAY);
				
				glTexCoord2f(1, 1);
				glVertex2f(INVENTORY_X_MARGIN + INVENTORY_TEXT_NUMBER_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + 15, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE - OUTLINE_TEXTURE_BORDER_SIZE - INVENTORY_CURSOR_LEEWAY);
				
				glTexCoord2f(1, 0);
				glVertex2f(INVENTORY_X_MARGIN + INVENTORY_TEXT_NUMBER_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + 15, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + INVENTORY_BORDER_VERTICAL_SIZE);
				
				glTexCoord2f(0, 0);
				glVertex2f(INVENTORY_X_MARGIN - OUTLINE_TEXTURE_BORDER_SIZE, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + INVENTORY_BORDER_VERTICAL_SIZE);
				
			}glEnd();
			
			if (itemList[itemSelected + curMinItemView] instanceof Weapon){
				glBindTexture(GL_TEXTURE_2D, itemTextures[((Weapon)itemList[itemSelected + curMinItemView]).getClassPossible() + INVENTORY_WEAPON_OFFSET_LOW]);
				
			}else if (itemList[itemSelected + curMinItemView] instanceof HealthPotion){
				glBindTexture(GL_TEXTURE_2D, itemTextures[0]);
			}else{
				glBindTexture(GL_TEXTURE_2D, itemTextures[itemList[itemSelected + curMinItemView].getItemID() + INVENTORY_WEAPON_OFFSET_HIGH]);
			}
			
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN);
				
				glTexCoord2f(1, 1);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN + INVENTORY_IMAGE_SIZE, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN);
				
				glTexCoord2f(1, 0);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN + INVENTORY_IMAGE_SIZE, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN + INVENTORY_IMAGE_SIZE);
				
				glTexCoord2f(0, 0);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN + INVENTORY_IMAGE_SIZE);
				
			}glEnd();
			
			glDisable(GL_TEXTURE_2D);
			for (int i = curMinItemView; i < curMinItemView + INVENTORY_MAX_DISPLAY && i < itemList.length; i++){
				SimpleText.drawString(itemList[i].getName(), INVENTORY_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - (i - curMinItemView) * INVENTORY_TEXT_VERTICAL_SPACE);
			}
			
			SimpleText.drawString("Level Requirement: " + itemList[itemSelected + curMinItemView].getLevelRequired(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE);
			SimpleText.drawString("Primary Statistic: " + itemList[itemSelected + curMinItemView].getPrimaryStat(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE / 2);
			SimpleText.drawString("Price: " + itemList[itemSelected + curMinItemView].getPrice(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE / 2 * 3);
			SimpleText.drawString("Current money: " + money, INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_IMAGE_SIZE - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE * 5);
			
			if (itemList[itemSelected + curMinItemView] instanceof Weapon){
				SimpleText.drawString("Class Required: " + Hero.classNumToString(((Weapon)itemList[itemSelected + curMinItemView]).getClassPossible()), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE * 2);
			}
		}
	}
	
	//Method to render the Character menu
	public void renderCharacterMenu(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, menuTextures[ID_CHARACTER_MENU]);
		glBegin(GL_QUADS);{
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(X_RESOLUTION, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(X_RESOLUTION, Y_RESOLUTION);
			
			glTexCoord2f(0, 0);
			glVertex2f(0, Y_RESOLUTION);
			
		}glEnd();
		if (charactersToDisplay.length > 0){
			
			glBindTexture(GL_TEXTURE_2D, itemMenuOutlineTexture);
			glBegin(GL_QUADS);{
				glTexCoord2f(0, 1);
				glVertex2f(INVENTORY_X_MARGIN - OUTLINE_TEXTURE_BORDER_SIZE, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE - OUTLINE_TEXTURE_BORDER_SIZE - INVENTORY_CURSOR_LEEWAY);
				
				glTexCoord2f(1, 1);
				glVertex2f(INVENTORY_X_MARGIN + INVENTORY_TEXT_NUMBER_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + 15, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE - OUTLINE_TEXTURE_BORDER_SIZE - INVENTORY_CURSOR_LEEWAY);
				
				glTexCoord2f(1, 0);
				glVertex2f(INVENTORY_X_MARGIN + INVENTORY_TEXT_NUMBER_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + 15, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + INVENTORY_BORDER_VERTICAL_SIZE);
				
				glTexCoord2f(0, 0);
				glVertex2f(INVENTORY_X_MARGIN - OUTLINE_TEXTURE_BORDER_SIZE, Y_RESOLUTION - INVENTORY_Y_MARGIN - itemSelected * INVENTORY_TEXT_VERTICAL_SPACE + OUTLINE_TEXTURE_BORDER_SIZE + INVENTORY_BORDER_VERTICAL_SIZE);
				
			}glEnd();
			
			if (((Hero)charactersToDisplay[itemSelected]).getRank()){
				glBindTexture(GL_TEXTURE_2D, heroPlusTextures[charactersToDisplay[itemSelected].getUnitID()]);
			}else{
				glBindTexture(GL_TEXTURE_2D, heroTextures[charactersToDisplay[itemSelected].getUnitID()]);
			}
			
			glBegin(GL_QUADS);{
				
				glTexCoord2f(0, 1);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN);
				
				glTexCoord2f(1, 1);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN + INVENTORY_IMAGE_SIZE, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN);
				
				glTexCoord2f(1, 0);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN + INVENTORY_IMAGE_SIZE, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN + INVENTORY_IMAGE_SIZE);
				
				glTexCoord2f(0, 0);
				glVertex2f(INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_IMAGE_Y_MARGIN + INVENTORY_IMAGE_SIZE);
				
			}glEnd();
			
			glDisable(GL_TEXTURE_2D);
			for (int i = curMinItemView; i < curMinItemView + INVENTORY_MAX_DISPLAY && i < charactersToDisplay.length; i++){
				SimpleText.drawString(((Hero)charactersToDisplay[i]).getName(), INVENTORY_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - (i - curMinItemView) * INVENTORY_TEXT_VERTICAL_SPACE);
			}
			
			SimpleText.drawString("Class: " + ((Hero)charactersToDisplay[itemSelected]).checkRank(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE);
			SimpleText.drawString("Level: " + ((Hero)charactersToDisplay[itemSelected + curMinItemView]).getLevel(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE / 2);
			SimpleText.drawString("Attack: " + charactersToDisplay[itemSelected + curMinItemView].getAttack(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE);
			SimpleText.drawString("Defense: " + charactersToDisplay[itemSelected + curMinItemView].getDefense(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE / 2 * 3);
			SimpleText.drawString("Movement Range: " + charactersToDisplay[itemSelected + curMinItemView].getMovement(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE * 2);
			SimpleText.drawString("EXP: " + ((Hero)charactersToDisplay[itemSelected + curMinItemView]).getExp() + "/" + ((Hero)charactersToDisplay[itemSelected + curMinItemView]).getExpCap(), INVENTORY_IMAGE_X_MARGIN, Y_RESOLUTION - INVENTORY_Y_MARGIN - INVENTORY_IMAGE_SIZE - INVENTORY_TEXT_VERTICAL_SPACE /2 * 5);
		}
	}
	
	//Methods to enable scrolling in the board
	public void updateOrtho(){
		glMatrixMode(GL11.GL_PROJECTION);
	    glLoadIdentity(); 
		glOrtho(curMinXDisplay, curMinXDisplay + X_RESOLUTION, curMinYDisplay, curMinYDisplay + Y_RESOLUTION, 1, -1);
	}
	
	//This method checks if the display's coordinates need to be changed to scroll the board
	public void checkOrtho(){
		
		//This section checks for units moving and centers the display onto the character
		if(hasPath && !hasEnemyPath){
			while (units[unitSelected].getCoordinate().getY() * SIZE > curMinYDisplay + Y_RESOLUTION - SIZE && units[unitSelected].getCoordinate().getY() * SIZE + SIZE < boardHeight * SIZE){
				curMinYDisplay += SIZE;
				updateOrtho();
			}
			
			while (units[unitSelected].getCoordinate().getY() * SIZE < curMinYDisplay + SIZE && units[unitSelected].getCoordinate().getY() * SIZE - SIZE >= 0){
				curMinYDisplay -= SIZE;
				updateOrtho();
			}
			
			while (units[unitSelected].getCoordinate().getX() * SIZE > curMinXDisplay + X_RESOLUTION - SIZE && units[unitSelected].getCoordinate().getX() * SIZE + SIZE < boardWidth * SIZE){
				curMinXDisplay += SIZE;
				updateOrtho();
			}
			
			
			while (units[unitSelected].getCoordinate().getX() * SIZE < curMinXDisplay + SIZE && units[unitSelected].getCoordinate().getX() * SIZE - SIZE >= 0){
				curMinXDisplay -= SIZE;
				updateOrtho();
			}
			
		//This section checks for the cursor's position and scrolls the board to ensure that the cursor is within the display
		}else{
			if (OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getY() * SIZE > curMinYDisplay + Y_RESOLUTION - SIZE - OUTLINE_TEXTURE_BORDER_SIZE){
				curMinYDisplay += SIZE;
				updateOrtho();
			}
			
			if (OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getY() * SIZE - OUTLINE_TEXTURE_BORDER_SIZE < curMinYDisplay + SIZE){
				curMinYDisplay -= SIZE;
				updateOrtho();
			}
			
			if (OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getX() * SIZE > curMinXDisplay + X_RESOLUTION - SIZE - OUTLINE_TEXTURE_BORDER_SIZE){
				curMinXDisplay += SIZE;
				updateOrtho();
			}
			
			if (OUTLINE_TEXTURE_BORDER_SIZE + cursorPosition.getX() * SIZE - OUTLINE_TEXTURE_BORDER_SIZE < curMinXDisplay + SIZE){
				curMinXDisplay -= SIZE;
				updateOrtho();
			}
			
			//Note: this is a quick fix for a bug where swapping between two characters will not update the ortho if the character to be swapped to is at the edge
			if (cursorPosition.getY() == boardHeight - 1){
				curMinYDisplay = boardHeight * SIZE - Y_RESOLUTION + 3;
				updateOrtho();
			}
			
			if (cursorPosition.getY() == 0){
				curMinYDisplay = 0;
				updateOrtho();
			}
			
			if (cursorPosition.getX() == boardWidth - 1){
				curMinXDisplay = boardWidth * SIZE - X_RESOLUTION + 3;
				updateOrtho();
			}
			
			if (cursorPosition.getX() == 0){
				curMinXDisplay = 0;
				updateOrtho();
			}
		}
	}
	

	
	//Method to check if the current screen ID is a menu
	public boolean isMenu(){
		return screenID == ID_MAIN_MENU || screenID == ID_SUB_MENU || screenID == ID_INTERMEDIATE_MENU || screenID == ID_ACTION_MENU || screenID == ID_LOAD_MENU || screenID == ID_INVENTORY_ACTION_MENU;
	}
	
	//Method to check if the current screen ID is a cutscene
	public boolean isCutscene(){
		return screenID == ID_PRE_BATTLE_CUTSCENE || screenID == ID_POST_BATTLE_CUTSCENE;
	}
	
	//Method to check if character animations are required
	public boolean checkCutsceneAnimationRequired(){
		
		boolean required = false;
		
		for (int i = 0; i < uniqueCharacters.length && !required; i++){
			if (uniqueCharacters[i]){
				required = true;
			}
		}
		
		return required;
	}
	
	//Method for getting keyboard input
	public void pollInput(){
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				
				//Commands for the menus
				if (isMenu()){
					if(Keyboard.getEventKey() == ACTION_KEY){
						MainGame.getCommand(menuButtonSelected);
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_UP){
						menuButtonSelected--;
						if(menuButtonSelected < 0){
							menuButtonSelected = NUM_MENU_BUTTONS[screenID] - 1;
						}
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_DOWN){
						menuButtonSelected++;
						if(menuButtonSelected > NUM_MENU_BUTTONS[screenID] - 1){
							menuButtonSelected = 0;
						}
					}
					
					if (screenID == ID_ACTION_MENU || screenID == ID_INVENTORY_ACTION_MENU){
						if(Keyboard.getEventKey() == BACK_KEY){
							MainGame.getCommand(-1);
						}
					}
				}else if (isCutscene()){
					if(Keyboard.getEventKey() == ACTION_KEY && !doneTextDisplay){
						setCutsceneTimerDelay(true);
					}
					
					if(Keyboard.getEventKey() == ACTION_KEY && doneTextDisplay && curDialogueSegment < Cutscene.getNumDialogue() - 1){
						updateCutscene();
					}
					
					if (Keyboard.getEventKey() == ACTION_KEY && doneTextDisplay && curDialogueSegment == Cutscene.getNumDialogue() - 1){
						MainGame.getCommand(0);
					}
				}else if (screenID == ID_BATTLE && !hasPath){
					if(Keyboard.getEventKey() == Keyboard.KEY_UP && map.checkOutOfBounds(cursorPosition, 0, 1)){
						cursorPosition.moveUp();
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_DOWN && map.checkOutOfBounds(cursorPosition, 0, -1)){
						cursorPosition.moveDown();
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_LEFT && map.checkOutOfBounds(cursorPosition, -1, 0)){
						cursorPosition.moveLeft();
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT && map.checkOutOfBounds(cursorPosition, 1, 0)){
						cursorPosition.moveRight();
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_Z){
						
						boolean found = false;
						
						for (int i = unitSelected + 1; i < numHeroes && !found; i++){
							if (units[i].isAlive()){
								unitSelected = i;
								found = true;
							}
						}
						
						for (int i = 0; i < unitSelected + 1 && !found; i++){
							if (units[i].isAlive()){
								unitSelected = i;
								found = true;
							}
						}
						
						if (found){
							cursorPosition = new Coordinate(units[unitSelected].getCoordinate().getX(), units[unitSelected].getCoordinate().getY());
						}
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_X){
						
						boolean found = false;
						
						for (int i = unitSelected - 1; i >= 0 && !found; i--){
							if (units[i].isAlive()){
								unitSelected = i;
								found = true;
							}
						}
						
						for (int i = numHeroes - 1; i > unitSelected - 1 && !found; i--){
							if (units[i].isAlive()){
								unitSelected = i;
								found = true;
							}
						}
						
						if (found){
							cursorPosition = new Coordinate(units[unitSelected].getCoordinate().getX(), units[unitSelected].getCoordinate().getY());
						}
					}
					
					if(Keyboard.getEventKey() == ACTION_KEY){
						MainGame.getCommand(unitSelected);
					}
				}else if (screenID == ID_ACTION_MOVE_MENU || screenID == ID_ACTION_ATTACK_MENU){
					
					if(Keyboard.getEventKey() == Keyboard.KEY_UP && map.checkOutOfBounds(cursorPosition, 0, 1)){
						cursorPosition.moveUp();
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_DOWN && map.checkOutOfBounds(cursorPosition, 0, -1)){
						cursorPosition.moveDown();
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_LEFT && map.checkOutOfBounds(cursorPosition, -1, 0)){
						cursorPosition.moveLeft();
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT && map.checkOutOfBounds(cursorPosition, 1, 0)){
						cursorPosition.moveRight();
					}
					
					if(Keyboard.getEventKey() == BACK_KEY){
						MainGame.getCommand(-1);
					}
					
					if(Keyboard.getEventKey() == ACTION_KEY){
						MainGame.getCommand(0);
					}
					
				}else if (screenID == ID_INVENTORY_MENU || screenID == ID_SHOP_MENU){
					if(Keyboard.getEventKey() == Keyboard.KEY_UP){
						itemSelected--;
						if(itemSelected < 0){
							curMinItemView--;
							itemSelected = 0;
							
							if(curMinItemView < 0){
								
								if(itemList.length < INVENTORY_MAX_DISPLAY){
									curMinItemView = 0;
									itemSelected = itemList.length - 1;
								}else{
									curMinItemView = itemList.length - INVENTORY_MAX_DISPLAY;
									itemSelected = INVENTORY_MAX_DISPLAY - 1;
								}
							}
						}
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_DOWN){
						
						itemSelected++;
						if(itemSelected >= INVENTORY_MAX_DISPLAY){
							itemSelected = INVENTORY_MAX_DISPLAY - 1;
							curMinItemView++;
							
							if (curMinItemView + itemSelected >= itemList.length){
								curMinItemView = 0;
								itemSelected = 0;
							}
							
						}else if (curMinItemView + itemSelected >= itemList.length){
							curMinItemView = 0;
							itemSelected = 0;
						}
					}
					
					if(Keyboard.getEventKey() == ACTION_KEY){
						MainGame.getCommand(itemSelected);
					}
					
					if(Keyboard.getEventKey() == BACK_KEY){
						MainGame.getCommand(-1);
					}
					
					if(Keyboard.getEventKey() == SEARCH_KEY_1){
						MainGame.getCommand(-2);
					}
					
					if(Keyboard.getEventKey() == SEARCH_KEY_2){
						MainGame.getCommand(-4);
					}
					
					if (Keyboard.getEventKey() == SORT_KEY){
						MainGame.getCommand(-3);
					}
				}else if (screenID == ID_CHARACTER_MENU){
					if(Keyboard.getEventKey() == Keyboard.KEY_UP){
						itemSelected--;
						if(itemSelected < 0){
							curMinItemView--;
							itemSelected = 0;
							
							if(curMinItemView < 0){
								
								if(charactersToDisplay.length < INVENTORY_MAX_DISPLAY){
									curMinItemView = 0;
									itemSelected = charactersToDisplay.length - 1;
								}else{
									curMinItemView = charactersToDisplay.length - INVENTORY_MAX_DISPLAY;
									itemSelected = INVENTORY_MAX_DISPLAY - 1;
								}
							}							
						}
					}
					
					if(Keyboard.getEventKey() == Keyboard.KEY_DOWN){
						
						itemSelected++;
						if(itemSelected >= INVENTORY_MAX_DISPLAY){
							itemSelected = INVENTORY_MAX_DISPLAY - 1;
							curMinItemView++;
							
							if (curMinItemView + itemSelected >= charactersToDisplay.length){
								curMinItemView = 0;
								itemSelected = 0;
							}
							
						}else if (curMinItemView + itemSelected >= charactersToDisplay.length){
							curMinItemView = 0;
							itemSelected = 0;
						}
					}
					
					if(Keyboard.getEventKey() == BACK_KEY){
						MainGame.getCommand(-1);
					}
					
					if(Keyboard.getEventKey() == ACTION_KEY){
						MainGame.getCommand(itemSelected);
					}
				}
			}
		}
	}

	//This method initializes the necessary fields and calls the necessary methods to start a cutscene
	public void initializeCutscene(){
		curMinXDisplay = 0;
		curMinYDisplay = 0;
		
		updateOrtho();
		
		animationIndex = 0;
		curDialogueSegment = 0;
		dialogueNum = 0;
		dialogueLine = 0;
		setCutsceneTimerDelay(false);
		loadCutsceneTextures();
		setDialogueList();
		doneTextDisplay = false;
		uniqueCharacters = Cutscene.uniqueCharacters(curDialogueSegment);
		
		//Checking if animations are required
		animationRequiredIn = checkCutsceneAnimationRequired();
		if (animationRequiredIn){
			animationX0 = -CUTSCENE_TEXTURE_SIZE;
			
			if(uniqueCharacters.length == 2){
				animationX1 = X_RESOLUTION + CUTSCENE_TEXTURE_SIZE;
			}
		}
	}
	
	//This method updates the cutscenes to show the next scene
	public void updateCutscene(){
		curDialogueSegment++;
		setCutsceneTimerDelay(false);
		setDialogueList();
		doneTextDisplay = false;
		dialogueNum = 0;
		dialogueLine = 0;
		uniqueCharacters = Cutscene.uniqueCharacters(curDialogueSegment);
		animationRequiredOut = checkCutsceneAnimationRequired();
		
		//Checking if animations are required
		if (animationRequiredOut){
			
			if (uniqueCharacters[0]){
				animationX0 = CUTSCENE_X_MARGIN;
			}
			
			if (uniqueCharacters[0] || uniqueCharacters[1]){
				animationRequiredIn = true;
			}
			
			if(uniqueCharacters.length == 2 && uniqueCharacters[1]){
				animationX1 = X_RESOLUTION - CUTSCENE_X_MARGIN;
			}
		}
		
		if (animationIndex != 0 && uniqueCharacters.length == 2 && uniqueCharacters[1] && !uniqueCharacters[0] && Cutscene.getCharacters(animationIndex).length == 1 && uniqueCharacters[1] && !uniqueCharacters[0]){
				animationIndex++;
				animationRequiredIn = true;
				animationRequiredOut = false;
				animationX1 = X_RESOLUTION + CUTSCENE_TEXTURE_SIZE;
		}
		
		if (!animationRequiredIn && !animationRequiredOut){
			animationIndex++;
		}
	}
	
	//This method updates the animation for units going into the scene
	public void updateAnimationIn(){
		animationTimer++;
		if (animationTimer >= CUTSCENE_ANIMATION_DELAY){
			animationTimer = 0;
			
			if (uniqueCharacters[0]){
				animationX0 += CUTSCENE_ANIMATION_X_SKIP;
				animationTimer = 0;
			}
			
			if (uniqueCharacters.length == 2 && uniqueCharacters[1]){
				animationX1 -= CUTSCENE_ANIMATION_X_SKIP;
				animationTimer = 0;
			}
		}
		if (uniqueCharacters[0] && animationX0 == CUTSCENE_X_MARGIN){
			animationRequiredIn = false;
		}else if (uniqueCharacters.length == 2 && uniqueCharacters[1] && animationX1 == X_RESOLUTION - CUTSCENE_X_MARGIN){
			animationRequiredIn = false;
		}
	}
	
	//This method updates the animation for units going out of the scene
	public void updateAnimationOut(){
		animationTimer++;
		if (animationTimer == CUTSCENE_ANIMATION_DELAY){
			animationTimer = 0;
			
			if (uniqueCharacters[0]){
				animationX0 -= CUTSCENE_ANIMATION_X_SKIP;
				animationTimer = 0;
			}
			
			if (uniqueCharacters.length == 2 && uniqueCharacters[1]){
				animationX1 += CUTSCENE_ANIMATION_X_SKIP;
				animationTimer = 0;
			}
		}
		if (uniqueCharacters[0] && animationX0 == -CUTSCENE_TEXTURE_SIZE){
			animationIndex++;
			animationRequiredOut = false;
		}else if (uniqueCharacters.length == 2 && (uniqueCharacters[0] || uniqueCharacters[1]) && animationX1 == X_RESOLUTION + CUTSCENE_TEXTURE_SIZE){
			animationIndex++;
			animationRequiredOut = false;
		}
	}
	
	//This method updates the timer for the units
	public void updateCutsceneTimer(){
		cutsceneTimer++;
		if(cutsceneTimer >= cutsceneTimerDelay && dialogueNum < dialogue.length() && dialogueLine < dialogueList.length){
			cutsceneTimer = 0;
			dialogueDisplayed[dialogueLine] += dialogueList[dialogueLine].charAt(dialogueNum);
			dialogueNum++;
			if (dialogueList[dialogueLine].length() == dialogueNum){
				dialogueLine++;
				dialogueNum = 0;
			}
		}
		if (dialogueNum == dialogueList[dialogueLine].length() - 1 && dialogueLine == dialogueList.length - 1){
			doneTextDisplay = true;
		}
	}
	
	//This method sets the dialogue blocks into different lines so that they can be on the screen without going out of the screen
	public void setDialogueList(){
		dialogue = Cutscene.getText(curDialogueSegment);
		String[] temp = dialogue.split(" ");
		
		int max = (int)Math.ceil(dialogue.length() / (double)CUTSCENE_NUM_CHARACTERS_PER_LINE);
		
		dialogueDisplayed = new String[max];
		
		for (int i = 0; i < max; i++){
			dialogueDisplayed[i] = "";
		}
		
		dialogueList = new String[max];
		
		boolean exit = false;
		
		int count = 0;
		
		for (int i = 0; i < max; i++){
			dialogueList[i] = "";
			for (int q = count; q < temp.length && !exit; q++){
				if (dialogueList[i].length() + temp[q].length() + 1 > CUTSCENE_NUM_CHARACTERS_PER_LINE){
					count += q - count;
					exit = true;
				}else{
					dialogueList[i] += temp[q] + " ";
				}
			}
			exit = false;
		}
	}
	
	//The following two methods are used in calculating the framerate
	public static long getTime() {
		 return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	public void updateFPS() {
	    if (getTime() - lastFPS > 1000) {
	        Display.setTitle("Dongerino Adventures FPS: " + fps);
	        fps = 0; //reset the FPS counter
	        lastFPS += 1000; //add one second
	    }
	    fps++;
	}
	
	//This method loads the textures with a given fileName (as a form of a String)
	//Note: Please don't ask me how this works.
	public static int loadTexture(String fileName){
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		
		for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
		
		buffer.flip();
		
	    int textureID = glGenTextures(); //Generate texture ID
	    glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID
	        
	    //Setup wrap mode
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

	    //Setup texture scaling filtering
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	        
	    //Send texel data to OpenGL
	    glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	    return textureID;
	}
	
	//NOTE: REMEMBER TO CHANGE THE NUMBERS FOR THE FILE LOCATION (e.g. "Images/Menus/Menu0Buttons/Button" + i + ".png") SO THAT THE MENU#BUTTONS CORRELATES WITH THE MENU ID
	//This method loads all of the textures
	public void loadAllAvailableTextures(){
		menuTextures = new int[NUM_MENU_TEXTURES];
		for (int i = 0; i < NUM_MENU_TEXTURES; i++){
			//Loading all of the menu textures
			menuTextures[i] = loadTexture("Images/Menus/Menus/Menu" + i + ".png");
		}
		
		menuButtonOutlineTexture = loadTexture("Images/Menus/MenuButtonOutlines/MenuButtonOutline.png");
		itemMenuOutlineTexture = loadTexture("Images/Menus/MenuButtonOutlines/ItemMenuOutline.png");
		
		//Loading textures for main menu buttons
		int[] tempArray = new int[NUM_MENU_BUTTONS[ID_MAIN_MENU]];
		menuButtonTextures = new int[NUM_MENU_BUTTONS.length][1];
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_MAIN_MENU]; i++){
			//Loading all of the main menu button textures
			tempArray[i] = loadTexture("Images/Menus/Menu" + ID_MAIN_MENU + "Buttons/Button" + i + ".png");
		}
		menuButtonTextures[ID_MAIN_MENU] = tempArray;
		
		//Loading textures for sub menu buttons
		tempArray = new int[NUM_MENU_BUTTONS[ID_SUB_MENU]];
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_SUB_MENU]; i++){
			tempArray[i] = loadTexture("Images/Menus/Menu" + ID_SUB_MENU + "Buttons/Button" + i + ".png");
		}
		menuButtonTextures[ID_SUB_MENU] = tempArray;
		
		//Loading textures for intermediate menu buttons
		tempArray = new int[NUM_MENU_BUTTONS[ID_INTERMEDIATE_MENU]];
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_INTERMEDIATE_MENU]; i++){
			tempArray[i] = loadTexture("Images/Menus/Menu" + ID_INTERMEDIATE_MENU + "Buttons/Button" + i + ".png");
		}
		menuButtonTextures[ID_INTERMEDIATE_MENU] = tempArray;
		
		//Loading textures for the action menu buttons
		tempArray = new int[NUM_MENU_BUTTONS[ID_ACTION_MENU]];
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_ACTION_MENU]; i++){
			tempArray[i] = loadTexture("Images/Menus/Menu" + ID_ACTION_MENU + "Buttons/Button" + i + ".png");
		}
		menuButtonTextures[ID_ACTION_MENU] = tempArray;
		
		//Loading textures for the loading menu buttons
		tempArray = new int[NUM_MENU_BUTTONS[ID_LOAD_MENU]];
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_LOAD_MENU]; i++){
			tempArray[i] = loadTexture("Images/Menus/Menu" + ID_LOAD_MENU + "Buttons/Button" + i + ".png");
		}
		menuButtonTextures[ID_LOAD_MENU] = tempArray;
		
		//Loading textures for inventory action menu
		tempArray = new int[NUM_MENU_BUTTONS[ID_INVENTORY_ACTION_MENU]];
		for (int i = 0; i < NUM_MENU_BUTTONS[ID_INVENTORY_ACTION_MENU]; i++){
			tempArray[i] = loadTexture("Images/Menus/Menu" + ID_INVENTORY_ACTION_MENU + "Buttons/Button" + i + ".png");
		}
		menuButtonTextures[ID_INVENTORY_ACTION_MENU] = tempArray;
		
		//Loading texture for dialogue blocks in cutscenes
		dialogueBlockTexture = loadTexture("Images/Cutscenes/CutsceneBackgrounds/DialogueBlock.png");
		
		//Loading textures for units
		heroTextures = new int[NUM_HERO_TEXTURES];
		heroPlusTextures = new int[NUM_HERO_TEXTURES];
		
		for (int i = 0; i < NUM_HERO_TEXTURES; i++){
			heroTextures[i] = loadTexture("Images/Units/Heroes/Hero" + i + ".png");
		}
		
		for (int i = 0; i < NUM_HERO_TEXTURES; i++){
			heroPlusTextures[i] = loadTexture("Images/Units/Heroes/Hero" + i + "+.png");
		}
		
		enemyTextures = new int[NUM_ENEMY_TEXTURES];
		
		for (int i = 0; i < NUM_ENEMY_TEXTURES; i++){
			enemyTextures[i] = loadTexture("Images/Units/Enemies/Monster" + i + ".png");
		}
		
		//Loading textures for tiles
		tileTextures = new int[NUM_TILE_TEXTURES];
		
		for (int i = 0; i < NUM_TILE_TEXTURES; i++){
			tileTextures[i] = loadTexture("Images/BoardTiles/Tiles" + i + ".png");
		}
		
		//Loading textures for outlines
		outlineTextures = new int[NUM_OUTLINE_TEXTURES];
		
		for (int i = 0; i < NUM_OUTLINE_TEXTURES; i++){
			outlineTextures[i] = loadTexture("Images/Units/Outline" + i + ".png");
		}
		
		actionMenuButtonOutlineTexture = loadTexture("Images/Menus/MenuButtonOutlines/ActionMenuButtonOutline.png");
		inventoryActionMenuButtonOutlineTexture = actionMenuButtonOutlineTexture;
		
		itemTextures = new int[INVENTORY_NUM_TEXTURES];
		for (int i = 0; i < INVENTORY_NUM_TEXTURES; i++){
			itemTextures[i] = loadTexture("Images/Items/Item" + i + ".png");
		}
		
		movementOutlineTexture = loadTexture("Images/Units/Movement.png");
		attackOutlineTexture = loadTexture("Images/Units/Attack.png");
		
		infoTexture = loadTexture("Images/Units/InfoBlock.png");
		fontColorBlackTexture = loadTexture("Images/Menus/FontColorBlack.png");
	}

	
	//This method loads the cutscene textures, which only become available once a cutscene is called
	public void loadCutsceneTextures(){
		backgroundTexture = loadTexture("Images/Cutscenes/CutsceneBackgrounds/CutsceneBackground" + Cutscene.getBackground() + ".png");
	}
	
	//This method updates the cutscene timer delay to display text
	public void setCutsceneTimerDelay(boolean fast){
		if (!fast){
			cutsceneTimerDelay = CUTSCENE_REGULAR_DELAY;
		}else{
			cutsceneTimerDelay = CUTSCENE_FAST_DELAY;
		}
	}
	
	//This method is used primarily used for debugging. It is used for when I have no idea what is wrong and I need to print something to see.
	public static void plz(){
		System.out.println("plz");
	}
}