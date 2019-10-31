package connection;

public abstract class ClientType {
	private Client client;
//	protected Camera camera = new Camera();

	public ClientType() {
		this.client = new Client(this);
		this.client.connect();
		
		initComponent();
	}

	public void initComponent() {
	}

	public void write(String message) {}
}
