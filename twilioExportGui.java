package gui;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import com.twilio.sdk.resource.instance.Call;
import com.twilio.sdk.resource.instance.Sms;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import statistics.TwilioDataReceiver;

public class twilioExportGui extends Application {

	private DatePicker datePickerFrom, datePickerTo;
	private LocalDate dateFrom, dateTo;
	private TwilioDataReceiver dataReceiver;
	private Button exportButton, smsButton, callButton, connectButton;
	private Label feedback;
	private FileChooser fileChooser;
	private FileWriter fileWriter;
	private TextField account, auth;
	private ArrayList<Call> callResult;
	private ArrayList<Sms> messageResult;
	// CSV config
	private String lineSeparator = "\r\n";
	private String commaSeparator = ";";

	public static void main(String[] args) {
		launch(args);
	}
	private String connect(String accountSid, String authToken){
		dataReceiver = new TwilioDataReceiver(accountSid, authToken);		
		return "Connected to " + dataReceiver.connectToAccount();
	}

	private void getCalls() {
		callResult = dataReceiver.getCalls(dateFrom, dateTo);
		feedback.setText("Found " + callResult.size() + " calls");
		// delete message data from previous export
		messageResult = null;
	}

	private void getMessages() {
		messageResult = dataReceiver.getMessages(dateFrom, dateTo);
		feedback.setText("Found " + messageResult.size() + " messages");
		// delete call data from previous export
		callResult = null;
	}

	private void writeToCsv(File targetFile) {
		try {
			fileWriter = new FileWriter(targetFile);
			// calls or messages?
			if (callResult == null) {
				fileWriter.append("Time;From;To;Body;Status"); // Header
				fileWriter.append(lineSeparator);
				String body;
				for (Sms sms : messageResult) {
					fileWriter.append(sms.getDateSent().toString());
					fileWriter.append(commaSeparator);
					fileWriter.append(sms.getFrom());
					fileWriter.append(commaSeparator);
					fileWriter.append(sms.getTo());
					fileWriter.append(commaSeparator);
					// Remocve linebreaks
					body = sms.getBody().replace("\n", " ").replace("\r", " ");
					fileWriter.append(body);
					fileWriter.append(commaSeparator);
					fileWriter.append(sms.getStatus());
					fileWriter.append(lineSeparator);
				}
			} else {
				fileWriter.append("Time;From;To;Duration;Status"); // Header
				fileWriter.append(lineSeparator);
				for (Call call : callResult) {
					fileWriter.append(call.getStartTime().toString());
					fileWriter.append(commaSeparator);
					fileWriter.append(call.getFrom());
					fileWriter.append(commaSeparator);
					fileWriter.append(call.getTo());
					fileWriter.append(commaSeparator);
					fileWriter.append(call.getDuration());
					fileWriter.append(commaSeparator);
					fileWriter.append(call.getStatus());
					fileWriter.append(lineSeparator);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {

		
		fileChooser = new FileChooser();
		fileChooser.setTitle("Export CSV");
		fileChooser.getExtensionFilters()
				.add(new FileChooser.ExtensionFilter("CSV", "*.csv"));

		primaryStage.setTitle("Twilio CSV Export");
		
		// Connect to account
		connectButton = new Button();
		connectButton.setText("Connect");
		connectButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				feedback.setText(connect(account.getText(), auth.getText()));
				smsButton.setDisable(false);
				callButton.setDisable(false);
				connectButton.setDisable(true);
			}
		});

		// Get Messages
		smsButton = new Button();
		smsButton.setText("Get Messages");
		smsButton.setDisable(true);
		smsButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				dateFrom = datePickerFrom.getValue();
				dateTo = datePickerTo.getValue();
				// valid period
				if (dateFrom.isBefore(dateTo)) {
					getMessages();
					exportButton.setDisable(false);
				} else {
					feedback.setText("not a valid period");
				}
			}
		});

		// Get Calls
		callButton = new Button();
		callButton.setText("Get Calls");
		callButton.setDisable(true);
		callButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				dateFrom = datePickerFrom.getValue();
				dateTo = datePickerTo.getValue();
				// valid period
				if (dateFrom.isBefore(dateTo)) {
					getCalls();
					exportButton.setDisable(false);
				} else {
					feedback.setText("not a valid period");
				}
			}
		});

		// Export Data to CSV
		exportButton = new Button();
		exportButton.setText("Export CSV");
		exportButton.setDisable(true);
		exportButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				File exportFile = fileChooser.showSaveDialog(primaryStage);
				if (exportFile != null) {
					writeToCsv(exportFile);
				}
			}
		});

		// setup Layout
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		feedback = new Label("");

		Text scenetitle = new Text("Get your fresh Twilio data here");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		
		
		account = new TextField("");
		auth = new TextField("");

		
		datePickerFrom = new DatePicker();
		datePickerTo = new DatePicker();

		grid.add(scenetitle, 0, 0, 2, 1);
		grid.add(account, 0, 1);
		grid.add(auth, 1, 1);
		grid.add(connectButton, 2, 1);
		grid.add(datePickerFrom, 0, 2);
		grid.add(datePickerTo, 1, 2);
		grid.add(smsButton, 0, 3);
		grid.add(callButton, 1, 3);
		grid.add(feedback, 0, 4, 2, 1);
		grid.add(exportButton, 0, 5);

		

		Scene scene = new Scene(grid, 500, 300);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
