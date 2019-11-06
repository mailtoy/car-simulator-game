package connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import textures.TerrainTexture;
import textures.TerrainTexturePack;

/**
 * ClientType gathers all the functions that Client should have.
 * 
 * @author Issaree Srisomboon
 *
 */
public abstract class ClientType {
	private Loader loader;
	private ModelData data;
	private RawModel treeModel, carModel;
	private TexturedModel staticModel, grassModel, fernModel, car;
	private Light light;
	private Terrain terrain, terrain2;
	private MasterRenderer renderer;
	private List<Entity> entities;

	protected Client client;
	protected Player player; // Change to Car later
	protected Camera camera;

	public ClientType() {
		this.client = new Client(this);
		this.client.connect();

		initComponents();
	}

	public void initComponents() {
		DisplayManager.createDisplay();
		loader = new Loader();

		// Terrain TextureStaff
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("sideRoad"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("road"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("middleRoad"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

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

		terrain = new Terrain(0, 0, loader, texturePack, blendMap);
		terrain2 = new Terrain(1, 0, loader, texturePack, blendMap);

		renderer = new MasterRenderer();

		carModel = OBJLoader.loadObjModel("Car", loader);
		car = new TexturedModel(carModel, new ModelTexture(loader.loadTexture("color")));

		player = new Player(car, new Vector3f(110, 0, -750), 0, 0, 0, 0.6f);
		setCamera();
	}
	
	public void setCamera() {}

	public void render() {
		renderer.processEntity(player);
		renderer.processTerrain(terrain);
		renderer.processTerrain(terrain2);

		for (Entity entity : entities) {
			renderer.processEntity(entity);
		}

		renderer.render(light, camera);
		DisplayManager.updateDisplay();
	}

	public void closeRequest() {
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

		try {
			client.sendDisconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updatePosition() {
		
	}

	public void printConnection(String connectionStatus) {
		System.out.println(connectionStatus);
	}
}
