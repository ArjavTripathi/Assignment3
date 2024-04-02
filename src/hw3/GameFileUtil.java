package hw3;

import api.BodySegment;
import api.Cell;
import api.Exit;
import api.Wall;
import ui.GameConsole;

import java.io.File;
import java.util.Scanner;

/**
 * Utility class with static methods for loading game files.
 */
public class GameFileUtil {
	/**
	 * Loads the file at the given file path into the given game object. When the
	 * method returns the game object has been modified to represent the loaded
	 * game.
	 * 
	 * @param filePath the path of the file to load
	 * @param game     the game to modify
	 */
	public static void load(String filePath, LizardGame game) {
		File file = new File(filePath);
		try{
			Scanner scnr = new Scanner(file);
			while(scnr.hasNextLine()){
				String line = scnr.nextLine();
				System.out.println(line);
			}
		} catch(Exception e){
			System.out.println("File not found");
		}

	}

}
