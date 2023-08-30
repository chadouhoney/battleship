
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;




public class GameUI extends Pane{
	private upperUI upperUI;
	private lowerUI lowerUI;
	private Pane middleUI;
	public GameGrid PG1;
	public GameGrid PG2;
	public Player P1;
	public Player P2;
	int attack_x = -1;
	int attack_y = -1;
	
	
	
	public GameUI(Player P1, Player P2)  {
		this.P1 = P1;
		this.P2 = P2;
		PG1 = new GameGrid(P1.getPlayerGrid(), true);
		PG2 = new GameGrid(P2.getPlayerGrid(), false);
		
		PG1.setTranslateX(30);
		PG1.setTranslateY(20);
		
		PG2.setTranslateX(470);
		PG2.setTranslateY(20);
		
		//Upper part of the screen
		upperUI = new upperUI(P1,P2);		
		this.getChildren().add(upperUI);		
		
		//Middle Part of the screen
		middleUI = new Pane();
		middleUI.setPrefSize(900,400);
		middleUI.setTranslateX(0);
		middleUI.setTranslateY(100);
		Rectangle bg2 = new Rectangle(900,400);
		bg2.setFill(Color.LIGHTSLATEGRAY);
		bg2.setEffect(new GaussianBlur(3.5));
		bg2.setOpacity(0.7);
		middleUI.getChildren().addAll(bg2,PG1,PG2);
		
		this.getChildren().add(middleUI);
		
		
		//Lower part of the screen
		lowerUI = new lowerUI();
		this.getChildren().add(lowerUI);
				
		
	}



	public void UpdatePlayer(boolean HumanPlayer, Player P) {
		if(HumanPlayer) {
			upperUI.Update(true, P);
		}
		
		else {
			upperUI.Update(false, P);
		}
		
	}
	
	
	
	class upperUI extends Pane{
		ShipReport ship_report_player;
		ShipReport ship_report_opponent;
		PointsReport points_report_player;
		PointsReport points_report_opponent; 
		AccuracyReport accuracy_report_player;
		AccuracyReport accuracy_report_opponent;
		
		public upperUI(Player P1, Player P2) {		
			
			int offset1 = 150;
			int offset2 = 300;
			
			setPrefSize(900,100);
			setTranslateX(0);
			setTranslateY(0);
			Rectangle bg1 = new Rectangle(900,100);
			bg1.setFill(Color.LIGHTSLATEGRAY);
			bg1.setOpacity(0.9);
			bg1.setEffect(new GaussianBlur(4.0));
			
			ship_report_player = new ShipReport(P1.getShips(), "Your");
			ship_report_opponent = new ShipReport(P2.getShips(), "Opponent's");
			
			ship_report_player.setLayoutX(0);
			ship_report_opponent.setLayoutX(450);
			
			points_report_player = new PointsReport(P1.getPoints(),"Your");
			points_report_opponent = new PointsReport(P2.getPoints(),"Opponent's");
			
			points_report_player.setLayoutX(offset1);
			points_report_opponent.setLayoutX(450+offset1);
			
			accuracy_report_player = new AccuracyReport(P1.getPoints(),"Your");
			accuracy_report_opponent = new AccuracyReport(P2.getPoints(),"Opponent's");
			
			accuracy_report_player.setLayoutX(offset2);
			accuracy_report_opponent.setLayoutX(450+offset2);
			
			getChildren().addAll(bg1,ship_report_player, ship_report_opponent,points_report_player,points_report_opponent,accuracy_report_player,accuracy_report_opponent);
			
		}
		
		private void Update(boolean HumanPlayer, Player P) {
			if(HumanPlayer) {
				this.ship_report_player.update(P);
				this.points_report_player.update(P);
				this.accuracy_report_player.update(P);
			}
			
			else {
				this.ship_report_opponent.update(P);
				this.points_report_opponent.update(P);
				this.accuracy_report_opponent.update(P);
			}
		}
		
		
	}
	
	class lowerUI extends Pane{
				
		public lowerUI() {
			
			this.setPrefSize(900,100);
			this.setTranslateX(0);
			this.setTranslateY(500);
			Rectangle bg3 = new Rectangle(900,100);
			bg3.setFill(Color.LIGHTSLATEGRAY);
			bg3.setOpacity(0.9);
			bg3.setEffect(new GaussianBlur(3.5));
			
			//RESTART BUTTON
			/*
			MenuButton restartbutton = new MenuButton("Restart", 100,40);
			restartbutton.setTranslateX(100);
			restartbutton.setTranslateY(5);
			
			/*
			restartbutton.setOnMouseClicked(e1 -> {
				this.getParent().setVisible(false);				
				for(Node b: this.getParent().getParent().getChildrenUnmodifiable()) {
					if(b.getClass().getName().equals("GameUI")) {
						this.getParent().getParent().getChildrenUnmodifiable().remove(b);
					}
				}
				
				Game g = new Game(P1,P2,)
				this.getParent().setVisible(false);
			});
			*/
			
			//DETAILS BUTTON
			MenuButton detailsbutton = new MenuButton("Details", 100,40);
			detailsbutton.setTranslateX(100);
			detailsbutton.setTranslateY(5);
			detailsbutton.setOnMouseClicked(e1 -> {
				Stage detailswindow=new Stage();
				detailswindow.initModality(Modality.APPLICATION_MODAL);
				detailswindow.setTitle("");		      
				Label label1= new Label("Your last \nfive shots");		     
				Label label2 = new Label("Computer's last \nfive shots");	
				Label label3 = new Label("Computer's ships \nhealth");	
				
				HBox layout = new HBox(30);

				//player's last five moves display (its bad but ok)
				VBox vb1= new VBox(10);	
				vb1.getChildren().addAll(label1);
				
				ArrayBlockingQueue<String[]> moves = ((GameUI) (this.getParent())).P1.getlastfivemoves();
				int x = moves.size();
				int i=x;
				for(String[] move : moves )  {
					String s = Integer.toString(i)+": "+move[0]+", "+move[1]+", "+move[2];
					Label l = new Label(s);
					vb1.getChildren().add(l);
					i-=1;
				}
				
				//computer's last five moves display (its bad but ok)
				VBox vb2= new VBox(10);	
				vb2.getChildren().addAll(label2);
				
				moves = ((GameUI) (this.getParent())).P2.getlastfivemoves();
				x = moves.size();
				
				
				i=x;
				
				
				for(String[] move : moves )  {
					String s = Integer.toString(i)+": "+move[0]+", "+move[1]+", "+move[2];
					Label l = new Label(s);
					vb2.getChildren().add(l);
					i-=1;
				}
				
				//computer's ships
				VBox vb3= new VBox(10);
				vb3.getChildren().addAll(label3);
				ArrayList<Ship> enemyships = ((GameUI) (this.getParent())).P2.getShips();
				for(Ship S: enemyships) {
					String txt = S.getname()+": "+Integer.toString(S.health)+"/"+Integer.toString(S.length);
					Label l = new Label(txt);
					vb3.getChildren().add(l);
				}
				layout.getChildren().addAll(vb1,vb2,vb3);   
				Scene scene1= new Scene(layout, 400, 300);     
				detailswindow.setScene(scene1);
				detailswindow.showAndWait();
			});
			
			
			
			
			//BACK TO MAIN MENU BUTTON
			MenuButton exitbutton = new MenuButton("Main Menu", 100,40);
			exitbutton.setTranslateX(700);
			exitbutton.setTranslateY(5);
			exitbutton.setOnMouseClicked(e1 -> {
				this.getParent().setVisible(false);				
				for(Node b: this.getParent().getParent().getChildrenUnmodifiable()) {
					b.setVisible(true);
				}
				this.getParent().setVisible(false);
			});
			
							
			this.getChildren().addAll(bg3,detailsbutton,exitbutton);
		}
		
	}
	
	private class ShipReport extends VBox{
		
		Text txt;
		VBox ships = new VBox(1);
		ArrayList<Text> ships_text = new ArrayList<Text>();
		
		public ShipReport(ArrayList<Ship> _ships, String txt) {
			this.setTranslateX(0);
			this.setTranslateY(0);

			this.txt = new Text(txt+" Ships:");
			this.txt.setUnderline(true);		
					
			getChildren().add(this.txt);
			
			for(Ship s: _ships) {
				Color c;
				Text n = new Text(s.getname());
				if(s.isIntact()) {
					c = Color.BLACK;	
				}
				else if(s.isAlive()) {
					c=Color.ORANGE;					
				}
				else {
					c=Color.RED;
					n.setStrokeWidth(2);
				}
				n.setFill(c);
				ships_text.add(n);
				this.ships.getChildren().add(n);
			}
			this.getChildren().addAll(ships);
			
		}
	
		private void update(Player P) {
			ArrayList<Ship> playerships = P.getShips();
			this.ships.getChildren().clear();
		
			for(Ship s: playerships) {
				Color c;
				Text n = new Text(s.getname());
				if(s.isIntact()) {
					c = Color.BLACK;	
				}
				else if(s.isAlive()) {
					c=Color.ORANGE;					
				}
				else {
					c=Color.RED;
					n.setStrokeWidth(2);
				}
				n.setFill(c);
				ships_text.add(n);
				this.ships.getChildren().add(n);
			}
			//this.getChildren().addAll(ships);
				
		}
	}
	
	private class PointsReport extends VBox {
		Text txt;
		Text pts;
		
		public PointsReport(int points, String txt) {
			this.setTranslateX(0);
			this.setTranslateY(0);
			
			this.txt = new Text(txt+" Points:");
			this.txt.setUnderline(true);		
			
			this.pts = new Text(Integer.toString(points));
			
			this.getChildren().addAll(this.txt,this.pts);
		}
			
		public void update(Player P) {
			int points = P.getPoints();
			this.getChildren().remove(this.pts);
			this.pts = new Text(Integer.toString(points));
			this.getChildren().add(this.pts);
		}
	}
	
	private class AccuracyReport extends VBox {
		Text txt;
		Text accuracy;
		
		public AccuracyReport(double acc, String txt) {
			this.setTranslateX(0);
			this.setTranslateY(0);
			
			this.txt = new Text(txt+" Accuracy:");
			this.txt.setUnderline(true);		
			
			
			
			this.accuracy = new Text(new DecimalFormat("#.##").format(acc));
			//String.format("%.2f", Double.toString(acc));
			//this.accuracy = new Text(String.format("%.2f", s));
			
			this.getChildren().addAll(this.txt,this.accuracy);
		}
		
		
		
		public void update(Player P) {
			double acc = P.getAccuracy();
			this.getChildren().remove(this.accuracy);
			this.accuracy = new Text(new DecimalFormat("#.##").format(acc));
			this.getChildren().add(this.accuracy);
		}
	}
	
	private static class MenuButton extends StackPane{
		private Text text;
		public MenuButton(String name, int width, int height) {
			text = new Text(name);
			text.setFont(Font.font(20));
			text.setFill(Color.BLACK);
			
			Rectangle bg = new Rectangle(width, height);
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
	
}
