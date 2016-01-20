/*
 Name: Cutscene.java
 Author: Alexander Lu, Bob Du, Ian Hu, Peter He
 Date: Jan 9, 2015
 School : A.Y. Jackson S.S.
 Purpose: The Cutscene class stores the information of a cutscene, which involves the speakers and the dialogues.
*/

import java.io.*;
public class Cutscene {
	
	static int background;
	static int numDialogue;
	static int[][] characters;
	static int[] speaker;
	static String[] text;
	
	//This method loads in the cutscene, given a scene number and whether or not it is a pre-battle or a post-battle cutscene
	public static void loadCutscene(int sceneNumber, boolean preCutscene){
		try{
			BufferedReader in;
			if (preCutscene){
				
				//Load in the appropriate pre-battle cutscene
				in = new BufferedReader (new FileReader("TextFiles/CutsceneFiles/PreBattle/PreCutscene" + sceneNumber + ".txt"));
			}else{
				
				//Load in the appropriate post-battle cutscene
				in = new BufferedReader (new FileReader("TextFiles/CutsceneFiles/PostBattle/PostCutscene" + sceneNumber + ".txt"));
			}
			
			background = Integer.parseInt(in.readLine());
			numDialogue = Integer.parseInt(in.readLine());
			characters = new int[numDialogue][1];
			speaker = new int[numDialogue];
			text = new String[numDialogue];
			
			//Loading in the dialogue
			for (int i = 0; i < numDialogue; i++){
				String[] cur = in.readLine().split(" ");
				
				if (cur.length == 1){
					characters[i] = new int[1];
					characters[i][0] = Integer.parseInt(cur[0]);
				}else{
					characters[i] = new int[2];
					characters[i][0] = Integer.parseInt(cur[0]);
					characters[i][1] = Integer.parseInt(cur[1]);
				}
				
				speaker[i] = Integer.parseInt(in.readLine());
				
				text[i] = in.readLine();
			}
			
		}catch(IOException iox){
			iox.printStackTrace();
		}
	}
	
	//Cutscene for attacks
	public static void loadAttackCutscene (int attacker, int defender, String message, int backgroundLoad){
		background = backgroundLoad;
		numDialogue = 1;
		characters = new int[1][2];
		speaker = new int[1];
		text = new String[1];
		
		characters[0][0] = attacker;
		characters[0][1] = defender;
		
		speaker[0] = -1;
		
		text[0] = message;
	}
	
	
	//Accessors
	public static int getBackground(){
		return background;
	}
	
	public static int getNumDialogue(){
		return numDialogue;
	}
	
	public static String getText(int index){
		return text[index];
	}
	
	public static int[] getCharacters(int index){
		return characters[index];
	}
	
	public static int getSpeaker(int index){
		return speaker[index];
	}
	
	public static int getNumLines(){
		return text.length;
	}
	
	//This method checks if the characters that need to appear on the screen are unique from the previous scene's
	//This method is primarily used for the updateCutscene method in the GUI class, and is the driving force behind
	//animating the cutscenes
	public static boolean[] uniqueCharacters(int index){
		int numCharacters = characters[index].length;
		boolean[] unique = new boolean[numCharacters];
		
		//Initialize array
		for (int i = 0; i < numCharacters; i++){
			unique[i] = false;
		}
		
		//Check number of characters
		if (numCharacters == 1){
			
			//If no previous characters, return true
			if (index == 0){
				unique[0] = true;
				
			//Else, check for unique characters
			}else{
				if (characters[index][0] != characters[index - 1][0]){
					unique[0] = true;
				}
				
				if(characters[index - 1].length == 2){
					unique = new boolean[2];
					unique[1] = true;
					if (characters[index - 1][0] != characters[index][0]){
						unique[0] = true;
					}
				}
			}
			
		}else{
			
			if (index == 0){
				unique[0] = true;
				unique[1] = true;
				
			}else if(characters[index - 1].length == 1){
				unique[1] = true;
				
				if (characters[index][0] != characters[index - 1][0]){
					unique[0] = true;
				}
			}else{
				if (characters[index][0] != characters[index - 1][0]){
					unique[0] = true;
				}
				
				if (characters[index - 1].length == 1){
					unique[1] = true;
					
				}else if (characters[index][1] != characters[index - 1][1]){
					unique[1] = true;
				}
			}
		}
		return unique;
	}
}