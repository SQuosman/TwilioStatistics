package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Call;
import com.twilio.sdk.resource.instance.Sms;
import com.twilio.sdk.resource.list.CallList;
import com.twilio.sdk.resource.list.SmsList;

public class TwilioStatConnector {
	
	private Account mainAccount;
	private ArrayList<Call> callList;
	private ArrayList<Sms> messageList;
	private FileWriter fileWriter;
	
	// CSV config
	private String lineSeparator = "\r\n";
	private String commaSeparator = ";";
	
	public String connect(String account, String auth){
		TwilioRestClient client = new TwilioRestClient(account, auth);
		mainAccount = client.getAccount();
		return mainAccount.getFriendlyName();
	}
	
	public int getCalls(LocalDate dateFrom, LocalDate dateTo){
		Map<String, String> callParams = new HashMap<String, String>();
		callParams.put("StartTime>", dateFrom.toString());
		callParams.put("StartTime<", dateTo.toString());
		CallList calls = mainAccount.getCalls(callParams);
		
		// put the calls in an ArrayList because CallList doesn't have a size()-method
		callList = new ArrayList<Call>();
		for (Call call : calls) {
			callList.add(call);
		}
		messageList = null;
		return callList.size();
	}
	
	public int getMessages(LocalDate dateFrom, LocalDate dateTo){
		Map<String, String> smsParams = new HashMap<String, String>();
		smsParams.put("DateSent>", dateFrom.toString());
		smsParams.put("DateSent<", dateTo.toString());
		SmsList messages = mainAccount.getSmsMessages(smsParams);

		messageList = new ArrayList<Sms>();
		for (Sms sms : messages) {
			messageList.add(sms);
		}
		callList = null;
		return messageList.size();
	}
	
	public void exportToCsv(File targetFile){
		
		try {
			fileWriter = new FileWriter(targetFile);
			// calls or messages?
			if (callList == null) {
				fileWriter.append("Time;From;To;Body;Status"); // Header
				fileWriter.append(lineSeparator);
				String body;
				for (Sms sms : messageList) {
					fileWriter.append(sms.getDateSent().toString());
					fileWriter.append(commaSeparator);
					fileWriter.append(sms.getFrom());
					fileWriter.append(commaSeparator);
					fileWriter.append(sms.getTo());
					fileWriter.append(commaSeparator);
					// Remove linebreaks
					body = sms.getBody().replace("\n", " ").replace("\r", " ");
					fileWriter.append(body);
					fileWriter.append(commaSeparator);
					fileWriter.append(sms.getStatus());
					fileWriter.append(lineSeparator);
				}
			} else {
				fileWriter.append("Time;From;To;Duration;Status"); // Header
				fileWriter.append(lineSeparator);
				for (Call call : callList) {
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
				System.out.println("Error while flushing/closing fileWriter");
				e.printStackTrace();
			}
		}
	}
}
