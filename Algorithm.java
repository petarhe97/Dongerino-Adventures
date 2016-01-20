/*
 Name: Algorithm.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The Algotirhm class contains the methods that find the shortest path, distance between units, 
 as well as other useful methods that are used in other classes.
*/

import java.util.LinkedList;
import java.util.Queue;


public class Algorithm {
	
	//Fields and constants
	private static Map map;
	
	private static Coordinate[] unitPositions;
	
	private static Coordinate[][] predecessor;
	private static Coordinate[] pathToTake;
	
	//Note: This constant is used for Dijkstra's algorithm
	private static final int LARGE_NUMBER = 9999999;
	
	private static final int SMALL_MOVEMENT_PENALTY = 1;
	private static final int LARGE_MOVEMENT_PENALTY = 2;
	
	//This method sets the map
	public static void setMap(Map newMap){
		map = newMap;
	}
	
	//This method updates the unit's positioning on the map
	public static void setUnitPositions(Coordinate[] positions){
		unitPositions = positions;
	}
	
	//This method returns the positions of the units
	public static Coordinate[] getUnitPositions() {
		return unitPositions;
	}
	
	//Distance method takes in the coordinates of the start and end positions, and returns the shortest distance from the first coordinate to the second
	public static int distance(Coordinate start, Coordinate end, boolean terrainPassing){
		
		if (!map.checkOutOfBounds(start, 0, 0) || !map.checkOutOfBounds(end, 0, 0)) {
			return LARGE_NUMBER;
		}
		
		int[][] graph = dijkstra(start, terrainPassing);
		
		return graph[end.getX()][end.getY()];
		
	}
	
	//Dijkstra's algorithm, used to determine the shortest distance between the starting position and every other tile on the board
	public static int[][] dijkstra(Coordinate start, boolean terrainPassing){
		//Initialize the length and width of the board
		int boardWidth = map.getWidth();
		int boardHeight = map.getHeight();
		
		//Initialize the board and graph
		char[][] board = map.getBoard();
		
		int[][] graph = new int [boardWidth][boardHeight];
		
		//Initialize the board with a movement penalty for certain tiles
		//Note: -1 indicates that the unit can not move on to the tile
		if (!terrainPassing){
			for (int q = boardHeight - 1; q >= 0; q--){
				for (int i = 0; i < boardWidth; i++){
					if (!terrainPassing && (board[i][q] == 'm' || board[i][q] == 'w')){
						graph[i][q] = -1;
					}else if (board[i][q] == 'f' || board[i][q] == 's'){
						graph[i][q] = LARGE_MOVEMENT_PENALTY;
					}else{
						graph[i][q] = SMALL_MOVEMENT_PENALTY;
					}
				}
			}
			
		//Initialize the board without movement penalties if the 
		}else{
			for (int i = 0; i < boardWidth; i++){
				for (int q = boardHeight - 1; q >= 0; q--){
					graph[i][q] = SMALL_MOVEMENT_PENALTY;
				}
			}
		}
		//Initialize distance and path
		Queue<Coordinate> path = new LinkedList<Coordinate>();
		int[][] dist = new int[boardWidth][boardHeight];
				
		for (int q = 0; q < boardHeight; q++){
			for (int i = 0; i < boardWidth; i++){
				dist[i][q] = LARGE_NUMBER;
			}
		}
		
		predecessor = new Coordinate[boardWidth][boardHeight];
		dist[start.getX()][start.getY()] = 0;
		path.add(start);
			
		Coordinate cur;
				
		//Apply Dijkstra's algorithm to find distance to desired location
		while(!path.isEmpty()){
			cur = path.poll();
			//Check the adjacent coordinates
			if (map.checkOutOfBounds(cur.getX(), cur.getY(), 1, 0) && graph[cur.getX() + 1][cur.getY()] != -1 &&
					cur.compareToListOfCoordinates(unitPositions, 1, 0)){
				if(dist[cur.getX() + 1][cur.getY()] > dist[cur.getX()][cur.getY()] + graph[cur.getX() + 1][cur.getY()]){
					dist[cur.getX() + 1][cur.getY()] = dist[cur.getX()][cur.getY()] + graph[cur.getX() + 1][cur.getY()];
					path.add(new Coordinate (cur.getX() + 1, cur.getY()));
					predecessor[cur.getX() + 1][cur.getY()] = cur;
				}
			}
			
			if (map.checkOutOfBounds(cur.getX(), cur.getY(), -1, 0) && graph[cur.getX() - 1][cur.getY()] != -1 &&
					cur.compareToListOfCoordinates(unitPositions, -1, 0)){
				if(dist[cur.getX() -1][cur.getY()] > dist[cur.getX()][cur.getY()] + graph[cur.getX() -1][cur.getY()]){
					dist[cur.getX() -1][cur.getY()] = dist[cur.getX()][cur.getY()] + graph[cur.getX() - 1][cur.getY()];
					path.add(new Coordinate (cur.getX() - 1, cur.getY()));
					predecessor[cur.getX() - 1][cur.getY()] = cur;
				}
			}
			
			if (map.checkOutOfBounds(cur.getX(), cur.getY(), 0, 1) && graph[cur.getX()][cur.getY() + 1] != -1 &&
					cur.compareToListOfCoordinates(unitPositions, 0, 1)){
				if(dist[cur.getX()][cur.getY() + 1] > dist[cur.getX()][cur.getY()] + graph[cur.getX()][cur.getY() + 1]){
					dist[cur.getX()][cur.getY() + 1] = dist[cur.getX()][cur.getY()] + graph[cur.getX()][cur.getY() + 1];
					path.add(new Coordinate (cur.getX(), cur.getY() + 1));
					predecessor[cur.getX()][cur.getY() + 1] = cur;
				}
			}
			
			if (map.checkOutOfBounds(cur.getX(), cur.getY(), 0, -1) && graph[cur.getX()][cur.getY() - 1] != -1 &&
					cur.compareToListOfCoordinates(unitPositions, 0, -1)){
				if(dist[cur.getX()][cur.getY() - 1] > dist[cur.getX()][cur.getY()] + graph[cur.getX()][cur.getY() - 1]){
					dist[cur.getX()][cur.getY() - 1] = dist[cur.getX()][cur.getY()] + graph[cur.getX()][cur.getY() - 1];
					path.add(new Coordinate (cur.getX(), cur.getY() - 1));
					predecessor[cur.getX()][cur.getY() - 1] = cur;
				}
			}
		}
		
		return dist;
	}
	//This method returns the path to get from the coordinate of the first parameter to the coordinate of the second parameter
	public static Coordinate[] shortestPath(Coordinate start, Coordinate end, boolean terrainPassing){
		
		//Note: this method calls the distance method first to initialize the predecessor field
		distance(start, end, terrainPassing);
		
		//Call the getPath method to set the pathToTake
		getPath(start, end, predecessor, 0);
		return pathToTake;
		
	}
	
	//RECURSION IS USED HERE
	
	//This method recursively goes through the predecessor to set the pathToTake
	public static void getPath(Coordinate start, Coordinate end, Coordinate[][] paths, int cur){
		if (start.compareToCoordinates(end)){
			pathToTake = new Coordinate[cur];
			if (cur > 0){
				pathToTake[cur - 1] = start;
			}
		}else{
			getPath(start, paths[end.getX()][end.getY()], paths, cur + 1);
			pathToTake[cur] = end;
		}
	}
	
	//showPossibleMoves takes in a Unit and returns a boolean map of the possible locations the Unit can move to
	public static boolean[][] showAttackRange (Unit unit) {
		//initialize the boolean array
		boolean[][] possible = new boolean[map.getWidth()][map.getHeight()];
		//checked is used to ensure the recursive method does not repeat, it stores the number of movements left when the position is checked
		//if the position should never be checked, then it is set to -1
		//if the position needs to be checked, it is set to -2
		int[][] checked = new int[map.getWidth()][map.getHeight()]; 
		for (int i = 0; i < map.getWidth(); i++) {
			for (int j = 0; j < map.getHeight(); j++) {
				possible[i][j] = false;
				checked[i][j] = -2;
			}
		}
				
		//the recursive method checkMoves is called to find all the possible moves
		checkMoves (possible, checked, unit.getMovement(), unit.getCoordinate().getX(), unit.getCoordinate().getY());
		
		return possible;
	}
	
	//checkMoves is the recursive method used to determine all the possible moves
	private static void checkMoves (boolean[][] possible, int[][] checked, int movement, int curX, int curY){
		possible[curX][curY] = true;
		
		//if the unit can move any further and the current tile is possible, the nearby tiles will be checked
		if (movement != 0 && possible[curX][curY]) {
			//check if the nearby tiles are not out of bound and should be checked
			if (map.checkOutOfBounds(curX, curY, 1, 0) && checked[curX+1][curY] < movement && checked[curX+1][curY] != -1) {
				checkMoves (possible, checked, movement - 1, curX + 1, curY);
			}
			
			if (map.checkOutOfBounds(curX, curY, -1, 0) && checked[curX-1][curY] < movement && checked[curX-1][curY] != -1) {
				checkMoves (possible, checked, movement - 1, curX - 1, curY);
			}
			
			if (map.checkOutOfBounds(curX, curY, 0, 1) && checked[curX][curY+1]< movement && checked[curX][curY+1] != -1) {
				checkMoves (possible, checked, movement - 1, curX, curY + 1);
			}
			
			if (map.checkOutOfBounds(curX, curY, 0, -1) && checked[curX][curY-1]< movement && checked[curX][curY-1] != -1) {
				checkMoves (possible, checked, movement - 1, curX, curY - 1);
			}
			
		}
	}
	
	//SEARCHING AND SORTING IS DONE HERE
	
	//Implementing sequential search to search by an item's name
	//This method takes in the name to be searched, and the array of Item to search through, and returns an array of Item which contains the items with the same name
	public static Item[] searchItemName(String name, Item[] list){
	
		int count = 0;
		for(int i = 0; i < list.length && name!=null; i++){
			if(name.equalsIgnoreCase(list[i].getName())){
				count ++ ;
			}
		}
		
		Item [] result = new Item [count];
		int it = 0;
		
		for(int i = 0; i < list.length && name!=null; i++){
			if(name.equalsIgnoreCase(list[i].getName())){
				result[it] = list[i];
				it++;
			}
		}
		return result;
	}

	//Implementing sequential search to search by and item's price in the shop
	//This method takes in the price to be searched, and the array of Item to search through, and returns an array of Item which contains the items with the same price
	public static Item[] searchItemShopPrice(int price, Item[] list){
		
		int count = 0;
		for(int i = 0; i < list.length; i++){
			if(price == list[i].getPrice()){
				count ++ ;
			}
		}
		
		Item [] result = new Item [count];
		int it = 0;
		
		for(int i = 0; i < list.length; i++){
			if(price == list[i].getPrice()){
				result[it] = list[i];
				it++;
			}
		}
		return result;
	}
	
	//Implementing sequential search to search by and item's price in the player's inventory
	//This method takes in the price to be searched, and the array of Item to search through, and returns an array of Item which contains the items with the same price
	public static Item[] searchItemSalePrice(int price, Item[] list){
		
		int count = 0;
		for(int i = 0; i < list.length; i++){
			if(price == list[i].getSalePrice()){
				count ++ ;
			}
		}
		
		Item [] result = new Item [count];
		int it = 0;
		
		for(int i = 0; i < list.length; i++){
			if(price == list[i].getSalePrice()){
				result[it] = list[i];
				it++;
			}
		}
		return result;
	}
	
	//This method takes an array of items and sorts them alphabetically
	public static void sortItemsAlphabet(Item [] list){
		
		Item mem;
		int j;
		for(int i = 1; i < list.length; i++){
			
			mem = list[i];
			j = i-1;
			while(j>=0 && list[j].compareToAlphabet(mem) >= 0){
				
				if(list[j].compareToAlphabet(mem) > 0 || list[j].compareToAlphabet(mem) == 0 && list[j].compareToPrice(mem) > 0){
					list[j+1] = list[j];
				}
				j--;
			}
			list[j+1] = mem; 
			
		}
	}
	
	//This method takes an array of items and sorts them by their price, from lowest to highest
	public static void sortItemsPriceMinToMax(Item [] list){
		
		Item mem;
		int j;
		for(int i = 1; i < list.length; i++){
			
			mem  = list[i];
			j = i - 1;     
			while(j >= 0 && list[j].compareToPrice(mem) > 0){
				list[j+1] = list[j];
				j--;
			}
			list[j+1] = mem;
		}
	}
	
	//This method takes an array of items and sorts them price, from highest to lowest
	public static void sortItemsPriceMaxToMin(Item [] list){
		
		Item mem;
		int j;
		for(int i = 1; i < list.length; i++){
			
			mem  = list[i];
			j = i - 1;
			mem  = list[i];
			j = i - 1;     
			while(j >= 0 && list[j].compareToPrice(mem) < 0){
				list[j+1] = list[j];
				j--;
			}
			list[j+1] = mem;
		}
	}


}
