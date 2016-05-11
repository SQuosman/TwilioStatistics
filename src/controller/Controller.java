package controller;

import javafx.stage.Stage;
import model.TwilioStatConnector;
import view.Gui;

public class Controller {
	
	private TwilioStatConnector model;
	private Gui gui;
	private Stage primaryStage;

	public Controller(TwilioStatConnector model) {
		this.model = model;
		this.gui = new Gui();
		
		gui.getConnectButton().setText("Connect");
		
	}
	
	public void setupGui(){
		primaryStage = new Stage();
		try {
			gui.start(primaryStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
