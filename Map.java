/*
 Name: Map.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The map class contains the stores the information of the current map.
*/

import java.io.*;

public class Map {
	
	//Fields
	private int width;
	private int height;
	private int aggressionLevel;
	
	private char[][] board;
	private Coordinate[] heroSpawnPoints;
	private Coordinate[] enemySpawnPoints;
	
	private Enemy[] enemies;
	
	//Constructor
	public Map(int level){
		
		loadMap(level);
	}
	
	public char[][] getBoard(){
		return board;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
	//This method takes in a coordinate and returns the number which represents the tile at the designated coordinate
	public int getTileNumber(Coordinate cor){
		int tile = 0;
		switch(board[cor.getX()][cor.getY()]){
		
		case 'g':
			tile = 0;
			break;
			
		case 'f':
			tile = 1;
			break;
			
		case 'w':
			tile = 2;
			break;
			
		case 's':
			tile = 3;
			break;
			
		case 'm':
			tile = 4;
			break;
			
		case 'b':
			tile = 5;
			break;
			
		case 'v':
			tile = 6;
			break;
		}
		
		return tile;
	}
	
	//This method returns an array of Coordinate which represents the spawn points for the heroes
	public Coordinate[] getHeroSpawnPoints(){
		return heroSpawnPoints;
	}
	
	//This method returns an array of Coordinate which represents the spawn points for the enemies
	public Coordinate[] getEnemySpawnPoints(){
		return enemySpawnPoints;
	}
	
	//This method returns an array of Enemy which contains the information for the enemies to spawn
	public Enemy[] getEnemies(){
		return enemies;
	}
	
	//This method returns the aggression level for the loaded level
	public int getAggressionLevel(){
		return aggressionLevel;
	}
	
	
	//Method to load the map
	public void loadMap(int level){
		try{
			
			//Reading in the length and width of the map
			BufferedReader in = new BufferedReader(new FileReader ("TextFiles/MapFiles/Level" + level + ".txt"));
			width = Integer.parseInt(in.readLine());
			height = Integer.parseInt(in.readLine());
			
			board = new char[width][height];
			
			//Reading in the map
			String cur;
			for (int q = height - 1; q >= 0; q--){
				cur = in.readLine();
				for (int i = 0; i < width; i++){
					board[i][q] = cur.charAt(i);
				}
			}
			
			//Reading in the hero spawn points
			int heroSpawns = Integer.parseInt(in.readLine());
			
			heroSpawnPoints = new Coordinate[heroSpawns];
			
			String[] temp;
			
			for (int i = 0; i < heroSpawns; i++){
				cur = in.readLine();
				temp = cur.split(" ");
				heroSpawnPoints[i] = new Coordinate(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
			}
			
			//Reading in the enemy spawn points and enemy info
			
			aggressionLevel = Integer.parseInt(in.readLine());
			
			int enemySpawns = Integer.parseInt(in.readLine());
			
			enemySpawnPoints = new Coordinate[enemySpawns];
			enemies = new Enemy[enemySpawns];
			
			for (int i = 0; i < enemySpawns; i++){
				cur = in.readLine();
				temp = cur.split(" ");
				
				enemySpawnPoints[i] = new Coordinate(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
				enemies[i] = new Enemy(Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), in.readLine(), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()), Integer.parseInt(in.readLine()));
				enemies[i].setCoordinate(enemySpawnPoints[i]);
			}
			
			in.close();
		}catch(IOException iox){
			iox.printStackTrace();
		}
	}
	
	//This method loads the information for the map (to be displayed in the Sub Menu) and returns it in an array of String
	public static String[] loadMapInfo(int level){
		try{
			BufferedReader in = new BufferedReader(new FileReader("TextFiles/MapInfoFiles/MapInfoFile" + level + ".txt"));
			
			int numLines = Integer.parseInt(in.readLine());
			
			String[] mapInfo = new String[numLines];
			
			for (int i = 0; i < numLines; i++){
				mapInfo[i] = in.readLine();
			}
			in.close();
			return mapInfo;
		}catch(IOException iox){
			iox.printStackTrace();
		}
		
		return null;
	}
	
	//This method checks if the the given x and y coordinate, and its respective add factor exceed the bounds of the map
	public boolean checkOutOfBounds(int x, int y, int addX, int addY){
		return (x + addX < width && x + addX >= 0 && y + addY < height && y + addY >= 0);
	}
	//This method checks if the the given Coordinate object, and its respective add factor exceed the bounds of the map
	public boolean checkOutOfBounds(Coordinate cor, int addX, int addY){
		return (cor.getX() + addX < width && cor.getX() + addX >= 0 && cor.getY() + addY < height && cor.getY() + addY >= 0);
	}
	
	//This method checks if the terrain can be passed by regular units
	public boolean checkPassableTerrain(int x, int y){
		return board[x][y] != 'w' && board[x][y] != 'm';
	}
}
