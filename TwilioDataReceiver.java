package statistics;

import java.time.LocalDate;
import java.util.*;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.instance.*;
import com.twilio.sdk.resource.list.CallList;
import com.twilio.sdk.resource.list.SmsList;

public class TwilioDataReceiver {

	private String accountSid;
	private String authToken;
	private Account mainAccount;

	public TwilioDataReceiver(String accountSid, String authToken) {
		this.accountSid = accountSid;
		this.authToken = authToken;
	}

	public String connectToAccount() {
		TwilioRestClient client = new TwilioRestClient(accountSid, authToken);
		mainAccount = client.getAccount();
		return mainAccount.getFriendlyName();
	}

	public ArrayList<Call> getCalls(LocalDate dateFrom, LocalDate dateTo) {
		Map<String, String> callParams = new HashMap<String, String>();
		callParams.put("StartTime>", dateFrom.toString());
		callParams.put("StartTime<", dateTo.toString());
		CallList calls = mainAccount.getCalls(callParams);

		ArrayList<Call> callList = new ArrayList<Call>();
		for (Call call : calls) {
			callList.add(call);
		}
		return callList;
	}

	public ArrayList<Sms> getMessages(LocalDate dateFrom, LocalDate dateTo) {
		Map<String, String> smsParams = new HashMap<String, String>();
		smsParams.put("DateSent>", dateFrom.toString());
		smsParams.put("DateSent<", dateTo.toString());
		SmsList messages = mainAccount.getSmsMessages(smsParams);
		// put the messages into an ArrayList because SmsList sucks
		ArrayList<Sms> messageList = new ArrayList<Sms>();
		for (Sms sms : messages) {
			messageList.add(sms);
		}
		return messageList;
	}

}
