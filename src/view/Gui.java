package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Gui extends Application {

	private Button exportButton, messageButton, callButton, connectButton;
	private TextField account, auth;

	private Label feedback;
	private DatePicker datePickerFrom, datePickerTo;

	private FileChooser fileChooser;

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Twilio CSV Export");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("Get your fresh Twilio data here");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

		feedback = new Label("");

		account = new TextField("");
		auth = new TextField("");

		connectButton = new Button("Connect");
		messageButton = new Button("Get messages");
		messageButton.setDisable(true);
		callButton = new Button("get Calls");
		callButton.setDisable(true);
		exportButton = new Button("Export");
		exportButton.setDisable(true);

		datePickerFrom = new DatePicker();
		datePickerTo = new DatePicker();

		fileChooser = new FileChooser();
		fileChooser.setTitle("Export CSV");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));

		grid.add(scenetitle, 0, 0, 2, 1);
		grid.add(new Label("account"), 0, 1);
		grid.add(new Label("authentication"), 1, 1);
		grid.add(account, 0, 2);
		grid.add(auth, 1, 2);
		grid.add(connectButton, 2, 2);
		grid.add(new Label("Date from"), 0, 3);
		grid.add(new Label("Date to"), 1, 3);
		grid.add(datePickerFrom, 0, 4);
		grid.add(datePickerTo, 1, 4);
		grid.add(messageButton, 0, 5);
		grid.add(callButton, 1, 5);
		grid.add(feedback, 0, 6, 2, 1);
		grid.add(exportButton, 0, 7);

		Scene scene = new Scene(grid, 500, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public Button getExportButton() {
		return exportButton;
	}

	public Button getMessageButton() {
		return messageButton;
	}

	public Button getCallButton() {
		return callButton;
	}

	public Button getConnectButton() {
		return connectButton;
	}

	public Label getFeedbackLabel() {
		return feedback;
	}

	public TextField getAccount() {
		return account;
	}

	public TextField getAuth() {
		return auth;
	}

	public DatePicker getDatePickerFrom() {
		return datePickerFrom;
	}

	public DatePicker getDatePickerTo() {
		return datePickerTo;
	}

	public FileChooser getFileChooser() {
		return fileChooser;
	}
	

}
