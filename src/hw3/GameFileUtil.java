package hw3;

import api.BodySegment;
import api.Cell;
import api.Exit;
import api.Wall;
import ui.GameConsole;

import java.io.File;
import java.io.FileNotFoundException;
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
//				System.out.println(line);
				lines.add(line);
			}

			String dimensions = lines.getFirst();
			String[] dimensionArray = dimensions.split("x");
			int width = Integer.parseInt(dimensionArray[0]);
			int height = Integer.parseInt(dimensionArray[1]);
			game.resetGrid(width, height);

			for(int i = 1; i <= height; i++){
				char[] line = lines.get(i).toCharArray();
				for(int j = 0; j < width; j++){
					Cell cell = game.getCell(j, i-1);
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

			for(int i = height + 1; i < lines.size(); i++){
				Lizard liz = new Lizard();
				String[] lizard = lines.get(i).split(" ");
				ArrayList<BodySegment> bodyseg = new ArrayList<>();
				for(int j = 1; j < lizard.length; j++){
					String[] coord = lizard[j].split(",");
					Cell cell = new Cell(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
					BodySegment seg = new BodySegment(liz, cell);
					bodyseg.add(seg);
					liz.setSegments(bodyseg);
				}
				game.addLizard(liz);
			}




		}
		catch(FileNotFoundException e){
			System.out.println("File not found");
		}

	}


	public static void main(String[] args) {
//		LizardGame game = new LizardGame(0, 0);
//		GameFileUtil.load("src/examples/game1.txt", game);
	}

}
