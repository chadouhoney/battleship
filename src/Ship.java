import java.util.ArrayList;
public class Ship {
	String name;
	int id;
	int length;
	int health;
	ArrayList<Integer[]> ship_tiles = new ArrayList<Integer[]>();
	
	Ship(String name, int id, int length, ArrayList<Integer[]> ship_tiles){
		this.name = name;
		this.id = id;
		this.length = length;
		this.health = length;
		for(Integer[] tile:ship_tiles) {
			this.ship_tiles.add(tile);
		}		
	}
	
	
	
	
	
	String getname() {
		return this.name;
	}
	
	void hit() {
		this.health--;
	}
	
	boolean isIntact(){
		return this.health==this.length;
	}
	
	boolean isAlive() {
		return this.health>0;
	}

}
