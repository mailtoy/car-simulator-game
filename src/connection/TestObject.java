package connection;

import java.io.Serializable;

public class TestObject implements Serializable {
	private String sendType, clientType, message;

	public TestObject(String sendType, String clientType, String message) {
		this.sendType = sendType;
		this.clientType = clientType;
		this.message = message;
	}
	
	public String getSendType() {
		return this.sendType;
	}
	
	public String getClientType() {
		return this.clientType;
	}
	
	public String getMessage() {
		return this.message;
	}
}