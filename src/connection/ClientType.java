package connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public abstract class ClientType {
	protected Loader loader;
	private ModelData data;
	private RawModel treeModel, bunnyModel;
	private TexturedModel staticModel, grassModel, fernModel;
	protected Light light;
	protected Terrain terrain, terrain2;
	protected MasterRenderer renderer;

	protected Client client;
	protected Player player; // Change to Car later
	protected TexturedModel stanfordBunny;
	protected Camera camera;

	protected List<Entity> entities;

	public ClientType() {
		this.client = new Client(this);
		this.client.connect();

		initComponents();
	}

	public void initComponents() {
		DisplayManager.createDisplay();
		loader = new Loader();

		data = OBJFileLoader.loadOBJ("tree");
		treeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		staticModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
				new ModelTexture(loader.loadTexture("tree")));

		grassModel = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		fernModel = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));

		grassModel.getTexture().setHasTransparency(true);
		fernModel.getTexture().setUseFakeLighting(true);
		fernModel.getTexture().setHasTransparency(true);

		entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(staticModel,
					new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 3));
			entities.add(new Entity(grassModel,
					new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 1));
			entities.add(new Entity(fernModel,
					new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 0.6f));
		}

		light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));

		terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass")));

		renderer = new MasterRenderer();

		bunnyModel = OBJLoader.loadObjModel("stanfordBunny", loader);
		stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));

		player = new Player(stanfordBunny, new Vector3f(0, 0, -40), 0, 180, 0, 0.6f);
		camera = new Camera(player);
	}

	public void printConnection(String connectionStatus) {
		System.out.println(connectionStatus);
	}

	public void checkKeyInput(int keyInput) {
	}
}
