package view;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Gui extends Application {
	
	private Button exportButton, smsButton, callButton, connectButton;
	
	public Gui(){
		connectButton = new Button();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Twilio CSV Export");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Label feedback = new Label("hey");

		Text scenetitle = new Text("Get your fresh Twilio data here");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

		grid.add(scenetitle, 0, 0, 2, 1);
		grid.add(feedback, 0, 4, 2, 1);
		grid.add(connectButton, 2, 1);

		Scene scene = new Scene(grid, 500, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
	public Button getExportButton() {
		return exportButton;
	}

	public Button getSmsButton() {
		return smsButton;
	}

	public Button getCallButton() {
		return callButton;
	}

	public Button getConnectButton(){
		return connectButton;
	}

}
