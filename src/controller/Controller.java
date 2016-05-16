package controller;

import java.io.File;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.TwilioStatConnector;
import view.Gui;

public class Controller {

	private TwilioStatConnector connector;
	private Gui gui;
	private Stage primaryStage;
	private Button connectButton, callButton, messageButton, exportButton;
	private Label feedbackLabel;
	private TextField account, auth;
	private DatePicker datePickerFrom, datePickerTo;
	private LocalDate dateFrom, dateTo;
	private FileChooser fileChooser;

	public Controller(TwilioStatConnector connector) {
		this.connector = connector;
		this.gui = new Gui();
	}

	public void setupGui() {
		primaryStage = new Stage();
		try {
			gui.start(primaryStage);

			getGuiElements();
			setupListeners();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getGuiElements() {
		feedbackLabel = gui.getFeedbackLabel();
		callButton = gui.getCallButton();
		messageButton = gui.getMessageButton();
		connectButton = gui.getConnectButton();
		exportButton = gui.getExportButton();
		account = gui.getAccount();
		auth = gui.getAuth();
		datePickerFrom = gui.getDatePickerFrom();
		datePickerTo = gui.getDatePickerTo();
		fileChooser = gui.getFileChooser();
	}

	private void setupListeners() {
		// connect to Twilio Account
		connectButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				feedbackLabel.setText(connector.connect(account.getText(), auth.getText()));
				messageButton.setDisable(false);
				callButton.setDisable(false);
				connectButton.setDisable(true);
			}
		});
		
		// get Calls
		callButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				dateFrom = datePickerFrom.getValue();
				dateTo = datePickerTo.getValue();
				// valid period
				if (dateFrom.isBefore(dateTo)) {
					int res = connector.getCalls(dateFrom, dateTo);
					exportButton.setDisable(false);
					feedbackLabel.setText("Found " + res + " calls");
				} else {
					feedbackLabel.setText("not a valid period");
				}
			}
		});
		
		// get messages
		messageButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				dateFrom = datePickerFrom.getValue();
				dateTo = datePickerTo.getValue();
				// valid period
				if (dateFrom.isBefore(dateTo)) {
					int res = connector.getMessages(dateFrom, dateTo);
					exportButton.setDisable(false);
					feedbackLabel.setText("Found " + res + " messages");
				} else {
					feedbackLabel.setText("not a valid period");
				}
			}
		});
		
		// export results to CSV
		exportButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				File exportFile = fileChooser.showSaveDialog(primaryStage);
				if (exportFile != null) {
					connector.exportToCsv(exportFile);
				}
			}
		});
	}
}
