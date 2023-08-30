import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue; 
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import exceptions.AdjacentTilesException;
import exceptions.InvalidCountException;
import exceptions.InvalidDirectionException;
import exceptions.OverlapTilesException;
import exceptions.OversizeException;
import helper.helper;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Battleship extends Application {

	GameMenu main_menu;
	Pane root;
	
	
	
	@Override     
	public void start(Stage stage) throws Exception { 

		root = new Pane();
		stage.setTitle("Medialab Battleship"); 
		stage.setWidth(900);
		stage.setHeight(600);
		
		Image main_menu_image = new Image(new FileInputStream("./res/Menu_Image.jpg"));
		ImageView main_menu_view = new ImageView(main_menu_image); 
		main_menu_view.setFitWidth(900);
		main_menu_view.setFitHeight(600);
		
		main_menu = new GameMenu();


		root.getChildren().addAll(main_menu_view, main_menu);
		
			
		Scene scene = new Scene(root);   
		
		stage.setScene(scene); 
		stage.show(); 
	   
   }

	private static class MenuButton extends StackPane{
		private Text text;
		public MenuButton(String name) {
			text = new Text(name);
			text.setFont(Font.font(20));
			text.setFill(Color.BLACK);
			
			Rectangle bg = new Rectangle(250, 30);
			bg.setOpacity(0.5);
			bg.setFill(Color.BLACK);
			bg.setEffect(new GaussianBlur(3.5));
			
			setAlignment(Pos.CENTER);
			setRotate(-0.5);
			getChildren().addAll(bg, text);
			
			this.setOnMouseEntered(event ->{
				bg.setFill(Color.WHITE);
				text.setFill(Color.BLACK);
				}
			);
			
			this.setOnMouseExited(event ->{
				bg.setFill(Color.BLACK);
				text.setFill(Color.WHITE);
				}
			);
			
			//glow when clicked
			DropShadow d = new DropShadow(50, Color.WHITE);
			d.setInput(new Glow());
			
			this.setOnMousePressed(event -> {	
									setEffect(d);
									bg.setFill(Color.WHITE);}
				);
			this.setOnMouseReleased(event -> {
										setEffect(null);
										bg.setFill(Color.BLACK);
												
				});
		}
	}
	
	private class GameMenu extends Parent{
		String p1_string, p2_string;
		Game g;
		
		
		public GameMenu() {
			String init_files_path = "./game_init/";
			p1_string = init_files_path + "player_default.txt";
			p2_string = init_files_path + "enemy_default.txt";
			VBox menu = new VBox(10);
			menu.setTranslateX(100);
			menu.setTranslateY(200);
						
			//Start Button: Initiates the game
			MenuButton start_button = new MenuButton("New Game");
			start_button.setOnMouseClicked(event -> {
				this.g = new Game(p1_string, p2_string,this);
				
				
				//this.setVisible(false);
				
				
				for(Node b: this.getChildren()){
					System.out.println(b.getClass().getName());
					
					if(b.getClass().getName().equals("GameUI")) {
						this.getChildren().remove(b);
						break;
					}
					else {
						b.setVisible(false);
					}
				}
				
				
				System.out.println("hi1");
				this.getChildren().add(g.getUI());
				//this.setVisible(true);
				System.out.println("hi2");
				
				

				
				
								
			});
			
			//Settings Button: Changes player files
			MenuButton settings_button = new MenuButton("Change Scenario");
			
			settings_button.setOnMouseClicked(event -> {				
				Stage settingswindow=new Stage();
				settingswindow.initModality(Modality.APPLICATION_MODAL);
				settingswindow.setTitle("");	
				TextField tf = new TextField();
				Label label1= new Label("Enter SCENARIO-ID:");	
				Label msglabel = new Label("");
				Button button1= new Button("Apply");		     
				button1.setOnAction(e -> {
					try {
						//System.out.println(init_files_path+"player_"+tf.getText()+".txt");
						if ((tf.getText() != null && !tf.getText().isEmpty())) {
							String ps1 = init_files_path+"player_"+tf.getText()+".txt";
							File fp1 = new File(ps1);
							Scanner myReader = new Scanner(fp1);
							myReader.close();
							String ps2 = init_files_path+"enemy_"+tf.getText()+".txt";
							File fp2 = new File(ps2);
							myReader = new Scanner(fp2);
							myReader.close();
							
							p1_string = init_files_path+"player_"+tf.getText()+".txt";
							p2_string = init_files_path+"enemy_"+tf.getText()+".txt";
							tf.setText("");
							msglabel.setText("");
							settingswindow.close();	
				        } else {
				            msglabel.setText("Please enter a SCENARIO-ID");
				        }
					}
					catch (FileNotFoundException e1) {
						msglabel.setText("Please enter a valid SCENARIO-ID");
					}
					
									
									
									
				});
				VBox layout= new VBox(10);			      
				layout.getChildren().addAll(label1,tf, button1,msglabel);      
				layout.setAlignment(Pos.CENTER);
				Scene scene1= new Scene(layout, 300, 250);     
				settingswindow.setScene(scene1);
				settingswindow.showAndWait();
			});
			
			//Exit Button: Pretty Self-Explanatory
			MenuButton exit_button = new MenuButton("Exit");
			exit_button.setOnMouseClicked(event -> {
				System.exit(0);
			});
			
			menu.getChildren().addAll(start_button, settings_button, exit_button);
			Rectangle bg = new Rectangle(900,600);
			bg.setFill(Color.GREY);
			bg.setEffect(new GaussianBlur(1.0));
			bg.setOpacity(0.5);
			
			getChildren().addAll(bg,menu);
		}
	
		
		void removefromchildren(Node x) {
			this.getChildren().remove(x);
		}
	}

	public class Game extends Pane{

		int turns_left = 40;
		private Player P1;
		private Player P2;
		protected GameUI UI;
		private GameMenu m;
		
		private ArrayList< Integer[] > tiles_not_attacked_yet = new ArrayList<Integer[]>();
		private ArrayList< Integer[] > successful_hits = new ArrayList<Integer[]>();
		
			
		private boolean[] ships_sinked = {false,false,false,false,false};
		private ArrayList< Integer[] > next_targets = new ArrayList<Integer[]>();;
		private int whoplaysfirst; //0 if player first, 1 if computer first
		
				
		public Game(String s1, String s2, GameMenu m) {
			try {
				P1 = new Player(s1);
				P2 = new Player(s2);
			}
			catch (OversizeException e) {
				Stage exceptionwindow=new Stage();
				exceptionwindow.initModality(Modality.APPLICATION_MODAL);
				exceptionwindow.setTitle("ERROR!");		      
				Label label1= new Label("OversizeException spotted.\nPlease make sure that enemy\nand player files are valid");		     
				Button button1= new Button("Back to main menu");		     
				button1.setOnAction(e1 -> {
					exceptionwindow.close();
				});
				VBox layout= new VBox(10);			      
				layout.getChildren().addAll(label1, button1);      
				layout.setAlignment(Pos.CENTER);
				Scene scene1= new Scene(layout, 300, 250);     
				exceptionwindow.setScene(scene1);
				exceptionwindow.showAndWait();
			} 
			catch (OverlapTilesException e) {
				Stage exceptionwindow=new Stage();
				exceptionwindow.initModality(Modality.APPLICATION_MODAL);
				exceptionwindow.setTitle("ERROR!");		      
				Label label1= new Label("OverlapTilesException spotted.\nPlease make sure that enemy\nand player files are valid");		     
				Button button1= new Button("Back to main menu");		     
				button1.setOnAction(e1 -> {
					exceptionwindow.close();
				});
				VBox layout= new VBox(10);			      
				layout.getChildren().addAll(label1, button1);      
				layout.setAlignment(Pos.CENTER);
				Scene scene1= new Scene(layout, 300, 250);     
				exceptionwindow.setScene(scene1);
				exceptionwindow.showAndWait();
			} 
			catch (AdjacentTilesException e) {
				Stage exceptionwindow=new Stage();
				exceptionwindow.initModality(Modality.APPLICATION_MODAL);
				exceptionwindow.setTitle("ERROR!");		      
				Label label1= new Label("AdjacentTilesException spotted.\nPlease make sure that enemy\nand player files are valid");		     
				Button button1= new Button("Back to main menu");		     
				button1.setOnAction(e1 -> {
					exceptionwindow.close();
				});
				VBox layout= new VBox(10);			      
				layout.getChildren().addAll(label1, button1);      
				layout.setAlignment(Pos.CENTER);
				Scene scene1= new Scene(layout, 300, 250);     
				exceptionwindow.setScene(scene1);
				exceptionwindow.showAndWait();
			} 
			catch (InvalidCountException e) {
				Stage exceptionwindow=new Stage();
				exceptionwindow.initModality(Modality.APPLICATION_MODAL);
				exceptionwindow.setTitle("ERROR!");		      
				Label label1= new Label("InvalidCountException spotted.\nPlease make sure that enemy\nand player files are valid");		     
				Button button1= new Button("Back to main menu");		     
				button1.setOnAction(e1 -> {
					exceptionwindow.close();
				});
				VBox layout= new VBox(10);			      
				layout.getChildren().addAll(label1, button1);      
				layout.setAlignment(Pos.CENTER);
				Scene scene1= new Scene(layout, 300, 250);     
				exceptionwindow.setScene(scene1);
				exceptionwindow.showAndWait();
			} 
			catch (InvalidDirectionException e) {
				Stage exceptionwindow=new Stage();
				exceptionwindow.initModality(Modality.APPLICATION_MODAL);
				exceptionwindow.setTitle("ERROR!");		      
				Label label1= new Label("InvalidDirectionException spotted.\nPlease make sure that enemy\nand player files are valid");		     
				Button button1= new Button("Back to main menu");		     
				button1.setOnAction(e1 -> {
					exceptionwindow.close();
				});
				VBox layout= new VBox(10);			      
				layout.getChildren().addAll(label1, button1);      
				layout.setAlignment(Pos.CENTER);
				Scene scene1= new Scene(layout, 300, 250);     
				exceptionwindow.setScene(scene1);
				exceptionwindow.showAndWait();
			}
			
			
			
			
			
			
			UI = new GameUI(P1,P2);	
			this.m = m;
			
			Random x = new Random();
			whoplaysfirst = x.nextInt(2);
			//whoplaysfirst = 1;
			
			if(whoplaysfirst==0) {
				System.out.println("Player plays first");
			}
			else {
				System.out.println("Computer plays first");
			}
			
			
			for(int i=0; i<10; i++) {
				for(int j=0; j<10; j++) {
					@SuppressWarnings("deprecation")
					final Integer innerI = new Integer(i);
					@SuppressWarnings("deprecation")
					final Integer innerJ = new Integer(j);
					
					if(whoplaysfirst==0) {
						UI.PG2.GridUI[i][j].setOnMouseClicked(event -> {	
							UI.PG2.GridUI[innerI][innerJ].setEffect(null);
							P1.total_hits ++;
							Attack(UI.PG2.GridUI[innerI][innerJ].getX(), UI.PG2.GridUI[innerI][innerJ].getY());
							P2.total_hits ++;
							OpponentAttack();
							turns_left--;
							if(turns_left<=0) {
								if(P1.getPoints() > P2.getPoints()) {
									Stage endgamewindow=new Stage();
									endgamewindow.initModality(Modality.APPLICATION_MODAL);
									endgamewindow.setTitle("");		      
									Label label1= new Label("PLAYER WINS!");		     
									Button button1= new Button("Back to main menu");		     
									button1.setOnAction(e -> {
										endgamewindow.close();
										
										this.m.setVisible(true);
										
										for(Node b: this.m.getChildrenUnmodifiable()) {
											b.setVisible(true);
										}
										this.UI.setVisible(false);
										
										
										
										
										});
									VBox layout= new VBox(10);			      
									layout.getChildren().addAll(label1, button1);      
									layout.setAlignment(Pos.CENTER);
									Scene scene1= new Scene(layout, 300, 250);     
									endgamewindow.setScene(scene1);
									endgamewindow.showAndWait();
								}
								else if(P1.getPoints() < P2.getPoints()) {
									Stage endgamewindow=new Stage();
									endgamewindow.initModality(Modality.APPLICATION_MODAL);
									endgamewindow.setTitle("");		      
									Label label1= new Label("COMPUTER WINS!");		     
									Button button1= new Button("Back to main menu");		     
									button1.setOnAction(e -> {
										endgamewindow.close();
										
										this.m.setVisible(true);
										
										for(Node b: this.m.getChildrenUnmodifiable()) {
											b.setVisible(true);
										}
										this.UI.setVisible(false);
										
										
										
										
										});
									VBox layout= new VBox(10);			      
									layout.getChildren().addAll(label1, button1);      
									layout.setAlignment(Pos.CENTER);
									Scene scene1= new Scene(layout, 300, 250);     
									endgamewindow.setScene(scene1);
									endgamewindow.showAndWait();
								}
								else {
									Stage endgamewindow=new Stage();
									endgamewindow.initModality(Modality.APPLICATION_MODAL);
									endgamewindow.setTitle("");		      
									Label label1= new Label("Nobody knows how!");		     
									Button button1= new Button("Back to main menu");		     
									button1.setOnAction(e -> {
										endgamewindow.close();
										
										this.m.setVisible(true);
										
										for(Node b: this.m.getChildrenUnmodifiable()) {
											b.setVisible(true);
										}
										this.UI.setVisible(false);
										
										
										
										
										});
									VBox layout= new VBox(10);			      
									layout.getChildren().addAll(label1, button1);      
									layout.setAlignment(Pos.CENTER);
									Scene scene1= new Scene(layout, 300, 250);     
									endgamewindow.setScene(scene1);
									endgamewindow.showAndWait();
								}
								
							}
							
						});
					}
					else {
						UI.PG2.GridUI[i][j].setOnMouseClicked(event -> {	
							UI.PG2.GridUI[innerI][innerJ].setEffect(null);
							P1.total_hits ++;
							Attack(UI.PG2.GridUI[innerI][innerJ].getX(), UI.PG2.GridUI[innerI][innerJ].getY());
							turns_left--;
							if(turns_left<=0) {
								if(P1.getPoints() > P2.getPoints()) {
									Stage endgamewindow=new Stage();
									endgamewindow.initModality(Modality.APPLICATION_MODAL);
									endgamewindow.setTitle("");		      
									Label label1= new Label("PLAYER WINS!");		     
									Button button1= new Button("Back to main menu");		     
									button1.setOnAction(e -> {
										endgamewindow.close();
										
										this.m.setVisible(true);
										
										for(Node b: this.m.getChildrenUnmodifiable()) {
											b.setVisible(true);
										}
										this.UI.setVisible(false);
										
										
										
										
										});
									endgamewindow.initStyle(StageStyle.UNDECORATED);
									VBox layout= new VBox(10);			      
									layout.getChildren().addAll(label1, button1);      
									layout.setAlignment(Pos.CENTER);
									Scene scene1= new Scene(layout, 300, 250);     
									endgamewindow.setScene(scene1);
									endgamewindow.showAndWait();
								}
								else if(P1.getPoints() < P2.getPoints()) {
									Stage endgamewindow=new Stage();
									endgamewindow.initModality(Modality.APPLICATION_MODAL);
									endgamewindow.setTitle("");		      
									Label label1= new Label("COMPUTER WINS!");		     
									Button button1= new Button("Back to main menu");		     
									button1.setOnAction(e -> {
										endgamewindow.close();
										
										this.m.setVisible(true);
										
										for(Node b: this.m.getChildrenUnmodifiable()) {
											b.setVisible(true);
										}
										this.UI.setVisible(false);
										
										
										
										
										});
									endgamewindow.initStyle(StageStyle.UNDECORATED);
									VBox layout= new VBox(10);			      
									layout.getChildren().addAll(label1, button1);      
									layout.setAlignment(Pos.CENTER);
									Scene scene1= new Scene(layout, 300, 250);     
									endgamewindow.setScene(scene1);
									endgamewindow.showAndWait();
								}
								else {
									Stage endgamewindow=new Stage();
									endgamewindow.initModality(Modality.APPLICATION_MODAL);
									endgamewindow.setTitle("");		      
									Label label1= new Label("Nobody knows how!");		     
									Button button1= new Button("Back to main menu");		     
									button1.setOnAction(e -> {
										endgamewindow.close();
										
										this.m.setVisible(true);
										
										for(Node b: this.m.getChildrenUnmodifiable()) {
											b.setVisible(true);
										}
										this.UI.setVisible(false);
										
										
										
										
										});
									endgamewindow.initStyle(StageStyle.UNDECORATED);
									VBox layout= new VBox(10);			      
									layout.getChildren().addAll(label1, button1);      
									layout.setAlignment(Pos.CENTER);
									Scene scene1= new Scene(layout, 300, 250);     
									endgamewindow.setScene(scene1);
									endgamewindow.showAndWait();
								}
								
							}
							else {
								P2.total_hits ++;
								OpponentAttack();
							}
							
						});
					}
					
					
				}
				
			}
			
			for(int i=0;i<10;i++) {
				for(int j=0; j<10; j++) {
					Integer[] t = {i,j};			
					tiles_not_attacked_yet.add(t);
				}
			}
			
			//if computer plays first, make the first attack
			if(whoplaysfirst==1) {
				P2.total_hits ++;
				OpponentAttack();
			}
		
			
		}
		
		public GameUI getUI() {
			return this.UI;
		}
		
		public void UpdateUIPlayer(Player P, boolean HumanPlayer) {
			this.UI.UpdatePlayer(HumanPlayer, P);
		}
		
		public void Attack(int i, int j) {
			
			int[] ship_hit_points= {350,250,100,100,50};
			int[] ship_sink_points= {1000,500,250,0,0};
			String[] ship_names = {"Carrier","Battleship","Cruiser","Submarine","Destroyer"};
			//PHASE 1: HUMAN PLAYER PLAYS
			
			String coords = "("+Integer.toString(i+1)+"," +Integer.toString(j+1) +")";
			//case of miss
			if (P2.getPlayerGrid()[i][j]==0){
				this.UI.PG2.hit(i, j);
				String[] st = {coords,"miss",""};
				//this.player_last_five_moves.add(st);
				P1.addMove(st);
			}
			else {
				String[] st = {coords,"hit",ship_names[P2.getPlayerGrid()[i][j]-1]};
				P1.addMove(st);
				P1.total_successful_hits++;
				int ship_id = P2.getPlayerGrid()[i][j];
				this.UI.PG2.hit(i, j);
				this.P1.earn_points(ship_hit_points[ship_id-1]);
				for(Ship s: P2.getShips()) {
					if(s.id == ship_id) {
						s.hit();
						if(!s.isAlive()) {
							P1.earn_points(ship_sink_points[ship_id-1]);
							P2.sink_ship();
							this.UI.PG2.sink(s.ship_tiles) ;	
							UpdateUIPlayer(P2, false);
						}
						break;
					}
				}
			}
			UpdateUIPlayer(P1, true);
			
			if(P2.number_of_alive_ships==0) {
				System.out.println("Player Wins!");
				
				
				Stage endgamewindow=new Stage();
				endgamewindow.initModality(Modality.APPLICATION_MODAL);
				endgamewindow.setTitle("");		      
				Label label1= new Label("PLAYER WINS!");		     
				Button button1= new Button("Back to main menu");		     
				button1.setOnAction(e -> {
					endgamewindow.close();					
					this.m.setVisible(true);					
					for(Node b: this.m.getChildrenUnmodifiable()) {
						if (b.getClass().getName().equals("GameUI")) {
							this.m.removefromchildren(b);
						}		
						else {
							b.setVisible(true);
						}
					}					
				});
				endgamewindow.initStyle(StageStyle.UNDECORATED);
				VBox layout= new VBox(10);			      
				layout.getChildren().addAll(label1, button1);      
				layout.setAlignment(Pos.CENTER);
				Scene scene1= new Scene(layout, 300, 250);     
				endgamewindow.setScene(scene1);
				endgamewindow.showAndWait();
			}
			
			
			
		}

		
		public void OpponentAttack() {
			int[] ship_hit_points= {350,250,100,100,50};
			int[] ship_sink_points= {1000,500,250,0,0};
			String[] ship_names = {"Carrier","Battleship","Cruiser","Submarine","Destroyer"};
			
			int i,j;		
		
			//if there is not an already hit-unsinked ship, attack at random
			if(next_targets.isEmpty()) {
				Random generator = new Random();
				int index = generator.nextInt(tiles_not_attacked_yet.size());
				i = tiles_not_attacked_yet.get(index)[0];
				j = tiles_not_attacked_yet.get(index)[1];
			}
			
			//else, check the 1st possible target
			else {
				i = next_targets.get(0)[0];
				j = next_targets.get(0)[1];
				next_targets.remove(0);
				
			}
			
			Integer[] t_curr = {i,j};
		
			String coords = "("+Integer.toString(i+1)+"," +Integer.toString(j+1) +")";
			
			//case the opponent shot misses
			if (P1.getPlayerGrid()[i][j]==0){
				this.UI.PG1.hit(i, j);
				String[] st = {coords,"miss"," "};
				P2.addMove(st);
				
			}
			
			// case the opponent hits a ship of the player
			else {
				
				String[] st = {coords,"hit",ship_names[P1.getPlayerGrid()[i][j]-1]};
				P2.addMove(st);
				
				
				P2.total_successful_hits++;
				successful_hits.add(t_curr);
				
				
				//get all adjacent tiles
				Integer[] t1 = {i-1, j};
				Integer[] t2 = {i+1, j};
				Integer[] t3 = {i, j-1};
				Integer[] t4 = {i,j+1};
				
				
				
				if(helper.isInList(successful_hits,t1) || helper.isInList(successful_hits,t2)) {
					Integer[] a = {t_curr[0]-1,t_curr[1]-1};
					helper.removeFromList(next_targets, a);
					helper.removeFromList(tiles_not_attacked_yet, a);
					Integer[] b = {t_curr[0]+1,t_curr[1]+1};
					helper.removeFromList(next_targets, b);
					helper.removeFromList(tiles_not_attacked_yet, b);
					Integer[] c = {t_curr[0],t_curr[1]-1};
					helper.removeFromList(next_targets, c);
					helper.removeFromList(tiles_not_attacked_yet, c);
					Integer[] d = {t_curr[0],t_curr[1]+1};
					helper.removeFromList(next_targets, d);
					helper.removeFromList(tiles_not_attacked_yet, d);
					Integer[] e = {t_curr[0]+1,t_curr[1]-1};
					helper.removeFromList(next_targets, e);
					helper.removeFromList(tiles_not_attacked_yet, e);
					Integer[] f = {t_curr[0]-1,t_curr[1]+1};
					helper.removeFromList(next_targets, f);
					helper.removeFromList(tiles_not_attacked_yet, f);
				}				
				
				if(helper.isInList(successful_hits,t3) || helper.isInList(successful_hits,t4) ) {
					Integer[] a = {t_curr[0]-1,t_curr[1]-1};
					helper.removeFromList(next_targets, a);
					helper.removeFromList(tiles_not_attacked_yet, a);
					Integer[] b = {t_curr[0]+1,t_curr[1]+1};
					helper.removeFromList(next_targets, b);
					helper.removeFromList(tiles_not_attacked_yet, b);
					Integer[] c = {t_curr[0]-1,t_curr[1]};
					helper.removeFromList(next_targets, c);
					helper.removeFromList(tiles_not_attacked_yet, c);
					Integer[] d = {t_curr[0]+1,t_curr[1]};
					helper.removeFromList(next_targets, d);
					helper.removeFromList(tiles_not_attacked_yet, d);
					Integer[] e = {t_curr[0]+1,t_curr[1]-1};
					helper.removeFromList(next_targets, e);
					helper.removeFromList(tiles_not_attacked_yet, e);
					Integer[] f = {t_curr[0]-1,t_curr[1]+1};
					helper.removeFromList(next_targets, f);
					helper.removeFromList(tiles_not_attacked_yet, f);
				}	
						
				
				int ship_id = P1.getPlayerGrid()[i][j];
				this.UI.PG1.hit(i, j);
				this.P2.earn_points(ship_hit_points[ship_id-1]);
				
				boolean ship_sinked=false;
						
				for(Ship s: P1.getShips()) {
					if(s.id == ship_id) {
						s.hit();
						if(!s.isAlive()) {
							ship_sinked = true;
							ships_sinked[s.id-1]=true;
							P2.earn_points(ship_sink_points[ship_id-1]);
							P1.sink_ship();
							this.UI.PG1.sink(s.ship_tiles) ;
							Set<Integer[]> adj = new HashSet<Integer[]>();
							//if we sink a ship, remove all tiles adjacent to it 
							//from the search list
							for(Integer[] tile: s.ship_tiles) {
								int x=tile[0];
								int y=tile[1];
								
								Integer[] tt1 = {x-1, y};
								adj.add(tt1);
								Integer[] tt2 = {x+1, y};
								adj.add(tt2);
								Integer[] tt3 = {x, y-1};
								adj.add(tt3);
								Integer[] tt4 = {x,y+1};
								adj.add(tt4);						
							}
							for(Integer[] t:adj) {
								helper.removeFromList(tiles_not_attacked_yet, t);
								helper.removeFromList(next_targets, t);
							}
							
							
						}
						UpdateUIPlayer(P1, true);
						break;
					}
					
				}
				
				
				if(!ship_sinked) {				
					Integer[] ts1 = {Math.max(i-1, 0), j};
					if(!helper.isInList(next_targets,ts1) && helper.isInList(tiles_not_attacked_yet,ts1)) {
						next_targets.add(ts1);
					}
					Integer[] ts2 = {Math.min(i+1, 9), j};
					if(!helper.isInList(next_targets,ts2) && helper.isInList(tiles_not_attacked_yet,ts2)) {
						next_targets.add(ts2);
					}
					Integer[] ts3 = {i, Math.max(j-1, 0)};
					if(!helper.isInList(next_targets,ts3) && helper.isInList(tiles_not_attacked_yet,ts3)) {
						next_targets.add(ts3);
					}
					Integer[] ts4 = {i,Math.min(j+1, 9)};
					if(!helper.isInList(next_targets,ts4) && helper.isInList(tiles_not_attacked_yet,ts4)) {
						next_targets.add(ts4);
					}
				}
			}
			UpdateUIPlayer(P2, false);
			if(P1.number_of_alive_ships==0) {
				System.out.println("COMPUTER WINS! GIT GUD NOOB!");
				Stage endgamewindow=new Stage();
				endgamewindow.initModality(Modality.APPLICATION_MODAL);
				endgamewindow.setTitle("");		      
				Label label1= new Label("COMPUTER WINS!");		     
				Button button1= new Button("Back to main menu");		     
				button1.setOnAction(e -> {
					endgamewindow.close();
					
					this.m.setVisible(true);
					
					for(Node b: this.m.getChildrenUnmodifiable()) {
						b.setVisible(true);
					}
					this.UI.setVisible(false);
					
					
					
					
					});
				endgamewindow.initStyle(StageStyle.UNDECORATED);
				VBox layout= new VBox(10);			      
				layout.getChildren().addAll(label1, button1);      
				layout.setAlignment(Pos.CENTER);
				Scene scene1= new Scene(layout, 300, 250);     
				endgamewindow.setScene(scene1);
				endgamewindow.showAndWait();
			}
			helper.removeFromList(tiles_not_attacked_yet, t_curr);
			
			
			
		}

	}
	
	
}
