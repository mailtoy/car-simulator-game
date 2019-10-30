package connection;

import java.io.Serializable;

public class TestObject implements Serializable {
	int value;
	String id;

	public TestObject(int v, String s) {
		this.value = v;
		this.id = s;
	}
	
	public String getId() {
		return this.id;
	}
}