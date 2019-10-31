package connection;

import java.io.Serializable;

import entities.Player;

public class TestObject implements Serializable {
	private static final long serialVersionUID = 6390509500910281747L;
	private String sendType, clientType, message;
	private Player player;

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