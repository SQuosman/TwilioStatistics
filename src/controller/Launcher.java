package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import model.TwilioStatConnector;
import view.Gui;

public class Launcher extends Application{
	
	private TwilioStatConnector connector;

	// creates the model and launches the controller
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TwilioStatConnector model = new TwilioStatConnector();
		Controller controller = new Controller(model);
		controller.setupGui();
		
	}
	

	
}
