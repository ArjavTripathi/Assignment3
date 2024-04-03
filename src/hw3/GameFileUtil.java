package hw3;

import api.BodySegment;
import api.Cell;
import api.Exit;
import api.Wall;
import ui.GameConsole;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
			ArrayList<String> lines = new ArrayList<>();
			while(scnr.hasNextLine()){
				String line = scnr.nextLine();
				System.out.println(line);
				lines.add(line);
			}

			String dimensions = lines.get(0);
			String[] dimensionArray = dimensions.split("x");
			int width = Integer.parseInt(dimensionArray[0]);
			int height = Integer.parseInt(dimensionArray[1]);
			game.resetGrid(width, height);

			for(int i = 1; i < lines.size(); i++){
				char[] line = lines.get(i).toCharArray();
				for(int j = 0; j < width; j++){
					Cell cell = game.getCell(j,i);
					if(line[j] == ' '){
						if(!cell.isEmpty()){
							cell.placeWall(null);
							cell.placeExit(null);
							cell.placeLizard(null);
						}

					} else if(line[j] == 'W'){
						if(cell.getWall() == null){
							cell.placeExit(null);
							cell.placeLizard(null);
							cell.placeWall(new Wall(cell));
						}
					} else if(line[j] == 'E'){
						if(cell.getExit() == null){
							cell.placeWall(null);
							cell.placeLizard(null);
							cell.placeExit(new Exit(cell));
						}
					}

				}
			}
		}
		catch(Exception e){
			System.out.println("File not found");
		}

	}


	public static void main(String[] args) {
		LizardGame game = new LizardGame(0, 0);
		GameFileUtil.load("src/examples/game1.txt", game);
		System.out.println("width: " + game.getWidth());
	}

}
