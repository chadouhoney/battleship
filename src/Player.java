import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import exceptions.*;
public class Player {
	String name;
	int[][] player_grid;
	int[][] opponent_grid;
	int points=0;
	int total_hits=0;
	int total_successful_hits=0;
	int number_of_alive_ships=5;
	ArrayList<Ship> player_ships;
	private ArrayBlockingQueue<String[]> last_five_moves = new ArrayBlockingQueue<String[]>(5); 

	
	
	public Player(String filename) throws OversizeException, OverlapTilesException,AdjacentTilesException,InvalidCountException,InvalidDirectionException{
		String[] ship_names = {"Carrier","Battleship","Cruiser","Submarine","Destroyer"};
		int[] ship_sizes = {5,4,3,3,2};
		
		//initialize player ships list
		ArrayList<Ship> ps = new ArrayList<Ship>();		
			
		//initialize opponent grid (0 corresponds to an empty space)
		opponent_grid = new int[10][10];
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				opponent_grid[i][j]=0;
			}
		}
		
		//initialize player grid (0 corresponds to an intact sea space)
		player_grid = new int[10][10];
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				player_grid[i][j]=0;
			}
		}
		
		boolean[] ships_placed = new boolean[5];
		boolean errors_exist = false;
				
		try {
			File f = new File(filename);
			Scanner myReader = new Scanner(f);
			
			for(int i=1; i<=5; i++){
				//read line and get the useful data
		        String line = myReader.nextLine();
		        String[] tokens=line.split(",");
		        
		        int ship_id = Integer.parseInt(tokens[0]);
		        String ship_name = ship_names[ship_id-1];
		        int x1 = Integer.parseInt(tokens[1]);
		        int y1 = Integer.parseInt(tokens[2]);
		        int x2;
		        int y2;
		        int direction = Integer.parseInt(tokens[3]);
		        if(direction==1) {
		        	x2=x1;
		        	y2=y1+ship_sizes[ship_id-1]-1;
		        }
		        else if(direction==2) {
		        	y2=y1;
		        	x2=x1+ship_sizes[ship_id-1]-1;
		        }
		        else {
		        	System.out.println("ERROR: Direction number is wrong");
		        	throw new InvalidDirectionException();
		        }

		        
		        if(x1<0 || x1>9 || x2<0 || x2>9 || y1<0 || y1>9 || y2<0 || y2>9) {
		        	errors_exist=true;
		        	System.out.println("OversizeException");
		        	throw new OversizeException();
		        	
		        }
		        
		        //ship placed horizontally (x1 = x2)
		        if(direction==1) {		        	
		        	//check for overlapping/adjacent ships
		        	for(int y=y1; y <= y2; y++) {
		        		if(player_grid[x1][y] != 0) {
		        			errors_exist=true;
		        			System.out.println("OverlapTilesException");
		        			throw new OverlapTilesException();
		        		}
		        		
		        		//check for adjacent tiles
		        		if((player_grid[Math.max(x1-1, 0)][y] != 0) || (player_grid[Math.min(x1+1, 9)][y] != 0) || (player_grid[x1][Math.max(y-1, 0)] != 0) || (player_grid[x1][Math.min(y+1,9)] != 0)) {
		        			errors_exist=true;
		        			System.out.println("AdjacentTilesException");
		        			throw new AdjacentTilesException();
		        		}
		        				        		
		        	}
		        	
		        	if(errors_exist == true) {
		        		System.out.println("There is some error about the ship" + ship_id);
		        		break;
		        	}
		        	
		        	else {
		        		ArrayList<Integer[]> ship_tiles = new ArrayList<Integer[]>();
		        		for(int y=y1; y<=y2; y++) {
		        			player_grid[x1][y]=ship_id;
		        			Integer[] tile = {x1,y,0};
		        			ship_tiles.add(tile);
		        		}
		        		ships_placed[ship_id-1]=true;
		        		ps.add(new Ship(ship_name, ship_id,ship_sizes[ship_id-1], ship_tiles));
		        	}
		        	
		        }
		        
		        //ship placed vertically
		        else if(direction==2) {		        	
		        	//place the ship and check for overlapping/adjacent ships
		        	for(int x=x1; x<=x2; x++) {
		        		if(player_grid[x][y1] != 0) {
		        			errors_exist=true;
		        			System.out.println("OverlapTilesException");
		        			throw new OverlapTilesException();
		        		}
		        		
		        		//check for adjacent tiles
		        		if(player_grid[Math.max(x-1, 0)][y1] != 0 || player_grid[Math.min(x+1, 9)][y1] != 0 || player_grid[x][Math.max(y1-1, 0)] != 0 || player_grid[x][Math.min(y1+1,9)] != 0) {
		        			errors_exist=true;
		        			System.out.println("AdjacentTilesException");
		        			throw new AdjacentTilesException();
		        		}		        		
		        	}
		        	
		        	if(errors_exist == true) {
		        		System.out.println("There is some error about the ship" + ship_id);
		        		break;
		        	}
		        	
		        	else {
		        		ArrayList<Integer[]> ship_tiles = new ArrayList<Integer[]>();
		        		for(int x=x1; x<=x2; x++) {
		        			player_grid[x][y1]=ship_id;
		        			Integer[] tile = {x,y1,0};
		        			ship_tiles.add(tile);
		        		}
		        		ships_placed[ship_id-1]=true;
		        		ps.add(new Ship(ship_name, ship_id,ship_sizes[ship_id-1], ship_tiles));
		        	}
		        	
		        }
		        
		        
		    
		        
		        
			}
			for(boolean b : ships_placed) {
				if(!b) {
					System.out.println("Error - not all ships have been placed");
					throw new InvalidCountException();
				}
			}
			myReader.close();
		}
		
		catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		     e.printStackTrace();
		}

		player_ships = new ArrayList<Ship>();
		for(Ship s: ps) {
			player_ships.add(s);
		}
		
}

	
	void addMove(String[] s) {
		if(last_five_moves.size()<5) {
			try {
				this.last_five_moves.put(s);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				this.last_five_moves.take();
				this.last_five_moves.put(s);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public ArrayBlockingQueue<String[]> getlastfivemoves() {
		return last_five_moves;
	}
		
	public double getAccuracy() {
		return (this.total_successful_hits)*1.0/(this.total_hits);
	}
	
	public int getPoints() {
		return this.points;
	}
	
	void earn_points(int p) {
		this.points +=p;
	}
	
	void sink_ship() {
		this.number_of_alive_ships--;
	}
	
	String getName() {
		return this.name;
	}
	
	ArrayList<Ship> getShips(){
		return this.player_ships;
	}
	
	static public int[][] readGrid(String filename){
		//String[] ship_names = {"Carrier","Battleship","Cruiser","Submarine","Destroyer"};
		int[] ship_sizes = {5,4,3,3,2};
		
		//initialize player ships list
		//ArrayList<Ship> ps = new ArrayList<Ship>();		
			
		//initialize player grid (0 corresponds to an empty space)
		int[][] grid = new int[10][10];
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				grid[i][j]=0;
			}
		}
		
		boolean[] ships_placed = new boolean[5];
		boolean errors_exist = false;
				
		try {
			File f = new File(filename);
			Scanner myReader = new Scanner(f);
			
			for(int i=1; i<=5; i++){
				
				
				//read line and get the useful data
		        String line = myReader.nextLine();
		        String[] tokens=line.split(",");
		        
		        int ship_id = Integer.parseInt(tokens[0]);
		        //String ship_name = ship_names[ship_id-1];
		        int x1 = Integer.parseInt(tokens[1]);
		        int y1 = Integer.parseInt(tokens[2]);
		        int x2;
		        int y2;
		        int direction = Integer.parseInt(tokens[3]);
		        if(direction==1) {
		        	x2=x1;
		        	y2=y1+ship_sizes[ship_id-1]-1;
		        }
		        else if(direction==2) {
		        	y2=y1;
		        	x2=x1+ship_sizes[ship_id-1]-1;
		        }
		        else {
		        	System.out.println("ERROR: Direction number is wrong");
	        		break;
		        	//throw InvalidDirectionException;
		        }

		        
		        if(x1<0 || x1>9 || x2<0 || x2>9 || y1<0 || y1>9 || y2<0 || y2>9) {
		        	errors_exist=true;
		        	System.out.println("OversizeException");
		        	//throw OversizeException;
		        	
		        }
		        
		        //ship placed horizontally (x1 = x2)
		        if(direction==1) {		        	
		        	//check for overlapping/adjacent ships
		        	for(int y=y1; y <= y2; y++) {
		        		if(grid[x1][y] != 0) {
		        			errors_exist=true;
		        			System.out.println("OverlapTilesException");
		        			//throw OverlapTilesException
		        		}
		        		
		        		//check for adjacent tiles
		        		if((grid[Math.max(x1-1, 0)][y] != 0) || (grid[Math.min(x1+1, 9)][y] != 0) || (grid[x1][Math.max(y-1, 0)] != 0) || (grid[x1][Math.min(y+1,9)] != 0)) {
		        			errors_exist=true;
		        			System.out.println("AdjacentTilesException");
		        			//throw AdjacentTilesException
		        		}
		        				        		
		        	}
		        	
		        	if(errors_exist == true) {
		        		System.out.println("There is some error about the ship" + ship_id);
		        		break;
		        	}
		        	
		        	else {
		        		//ArrayList<int[]> ship_tiles = new ArrayList<int[]>();
		        		for(int y=y1; y<=y2; y++) {
		        			grid[x1][y]=ship_id;
		        			//int[] tile = {x1,y,0};
		        			//ship_tiles.add(tile);
		        		}
		        		ships_placed[ship_id-1]=true;
		        		//ps.add(new Ship(ship_name, ship_id,ship_sizes[ship_id-1], ship_tiles));
		        	}
		        	
		        }
		        
		        //ship placed vertically
		        else if(direction==2) {		        	
		        	//place the ship and check for overlapping/adjacent ships
		        	for(int x=x1; x<=x2; x++) {
		        		if(grid[x][y1] != 0) {
		        			errors_exist=true;
		        			System.out.println("OverlapTilesException");
		        			//throw OverlapTilesException
		        		}
		        		
		        		//check for adjacent tiles
		        		if(grid[Math.max(x-1, 0)][y1] != 0 || grid[Math.min(x+1, 9)][y1] != 0 || grid[x][Math.max(y1-1, 0)] != 0 || grid[x][Math.min(y1+1,9)] != 0) {
		        			errors_exist=true;
		        			System.out.println("AdjacentTilesException");
		        			//throw AdjacentTilesException;
		        		}		        		
		        	}
		        	
		        	if(errors_exist == true) {
		        		System.out.println("There is some error about the ship" + ship_id);
		        		break;
		        	}
		        	
		        	else {
		        		//ArrayList<int[]> ship_tiles = new ArrayList<int[]>();
		        		for(int x=x1; x<=x2; x++) {
		        			grid[x][y1]=ship_id;
		        			//int[] tile = {x,y1,0};
		        			//ship_tiles.add(tile);
		        		}
		        		ships_placed[ship_id-1]=true;
		        		//ps.add(new Ship(ship_name, ship_id,ship_sizes[ship_id-1], ship_tiles));
		        	}
		        	
		        }
		        
		        
		    
		        
		        
			}
			for(boolean b : ships_placed) {
				if(!b) {
					System.out.println("Error - not all ships have been placed");
					//throw InvalidCountException;
				}
			}
			myReader.close();
		}
		
		catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		     e.printStackTrace();
		}
		/*
		catch (EOFException e) {
			System.out.println("Too Few Lines");
		      e.printStackTrace();
		}
		*/
		return grid;
		
}
	
	public int[][] getPlayerGrid() {
		return this.player_grid;
	}
	
}
