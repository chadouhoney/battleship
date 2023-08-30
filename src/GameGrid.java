import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;



public class GameGrid extends Pane {
	
	public GridBlock[][] GridUI;
	int offset=36;
	
		
	public GameGrid(int[][] grid, boolean playerGrid) {
		GridUI = new GridBlock[10][10];
		for(int i=0;i<10;i++) {
			for(int j=0; j<10; j++) {
				if(grid[i][j]==0 && playerGrid) {
					this.GridUI[i][j] = new GridBlock(0, false,i,j);//using 0 to indicate intact sea block
				}
				else if(grid[i][j]==0 && (!playerGrid)) {
					this.GridUI[i][j] = new GridBlock(0, true,i,j);	//using 0 to indicate intact sea block
				}
				else if(grid[i][j]!=0 && (playerGrid)) {
					this.GridUI[i][j] = new GridBlock(1, false,i,j);	//using 0 to indicate intact sea block
				}
				else if(grid[i][j]!=0 && (!playerGrid)) {
					this.GridUI[i][j] = new GridBlock(1, true,i,j);	//using 0 to indicate intact sea block
					this.GridUI[i][j].bg.setFill(Color.web("#4086AA"));
				}
				this.GridUI[i][j].setTranslateX(j*offset);
				this.GridUI[i][j].setTranslateY(i*offset);
				this.getChildren().add(GridUI[i][j]);
			}
		}
	}
	
	
	//function to hit a block
	public void hit(int i, int j) {
		if(this.GridUI[i][j].getCondition()==0) {
			this.GridUI[i][j].setCondition(2);
		}
		else if(this.GridUI[i][j].getCondition()==1) {
			this.GridUI[i][j].setCondition(3);
		}
	}
	
	//function to sink blocks
	public void sink(ArrayList<Integer[]> ship_tiles) {
		for(Integer[] tile: ship_tiles) {
			int x=tile[0];
			int y=tile[1];
			this.GridUI[x][y].setCondition(4);
		}
	}
	
		
	//Represents a single block of the grid
	static class GridBlock extends StackPane{
		private int x;
		private int y;
		//private boolean clickable;
		private int condition; //0 for intact-sea, 1 for intact-ship,2 for hit-miss, 3 for hit-success, 4 for sinked
		Rectangle bg;
		String[] color_palette= {"#4086AA","#666666","ffffff","#E25822","#7C0A02"};
		
		
		public GridBlock(int condition, boolean clickable, int x, int y) {
			this.x=x;
			this.y=y;
			this.condition=condition;
			//this.clickable=clickable;
			
			bg = new Rectangle(35, 35);
			bg.setOpacity(0.85);
			bg.setFill(Color.web(color_palette[condition]));
			bg.setEffect(new GaussianBlur(2.0));
			
			setAlignment(Pos.CENTER);
			getChildren().addAll(bg);
			
			DropShadow d = new DropShadow(1, Color.WHITE);
			d.setInput(new Glow());
			
			if(clickable) {
				this.setOnMouseEntered(event ->{
					setEffect(d);
					//bg.setFill(Color.web("#5bbdf0"));
					});
				
				this.setOnMouseExited(event ->{
					setEffect(null);
					//bg.setFill(Color.web(color_palette[this.getCondition()]));
					});
			}	
		};
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public int getCondition() {
			return this.condition;
		}
		
		public void setCondition(int c) {
			if (c<2) bg.setFill(Color.web(color_palette[c]));	
			else {
				Circle cr = new Circle(8);
				cr.setOpacity(1);
				cr.setFill(Color.web(color_palette[c]));
				//cr.setEffect(new Bloom());
				this.getChildren().add(cr);
			}
			
			//we only use this function after attacking a block. After attacking, make it un-everything
			this.setOnMouseClicked(null);
			this.setOnMouseEntered(null);
			this.setOnMouseExited(null);
			
		}
		
		
	}

}
