package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import android.R.integer;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.MultiplePlayer;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import network.Client;
import network.packet.DisconnectPacket;
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

public abstract class WindowDisplay {
	private Loader loader;
	private ModelData data;
	private RawModel treeModel, carModel;
	private TerrainTexture backgroundTexture, rTexture, gTexture, bTexture, blendMap;
	private TexturedModel staticModel, grassModel, fernModel, car;
	private TerrainTexturePack texturePack;
	private Light light;
	private Terrain terrain, terrain2;
	private MasterRenderer renderer;
	private List<Entity> entities;

	protected Client client;
	protected Player player; // Change to Car later
	protected Camera camera;

	public WindowDisplay() {
		this.client = new Client(this);
		this.client.start();

		initComponents();
	}

	private void initComponents() {
		DisplayManager.createDisplay();
		loader = new Loader();

		// Terrain TextureStaff
		backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		rTexture = new TerrainTexture(loader.loadTexture("sideRoad"));
		gTexture = new TerrainTexture(loader.loadTexture("road"));
		bTexture = new TerrainTexture(loader.loadTexture("middleRoad"));

		texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

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

		blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		terrain = new Terrain(0, 0, loader, texturePack, blendMap);
		terrain2 = new Terrain(1, 0, loader, texturePack, blendMap);

		renderer = new MasterRenderer();

		carModel = OBJLoader.loadObjModel("Car", loader);
		car = new TexturedModel(carModel, new ModelTexture(loader.loadTexture("color")));

		player = new MultiplePlayer("Controller", car, new Vector3f(110, 0, -750), 0, 0, 0, 0.6f, null, -1);
		camera = new Camera(player);
	}

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

	public void closeqRequest(String type) {
		renderer.cleanUp();
		loader.cleanUp();

		DisconnectPacket packet = new DisconnectPacket(type, player.getModel(), player.getPosition(), player.getRotX(),
				player.getRotY(), player.getRotZ(), player.getScale());
		packet.writeData(client);

		DisplayManager.closeDisplay();
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public void removeEntity(String type) {
		int index = 0;
		for (Entity entity : entities) {
			if (entity instanceof MultiplePlayer && ((MultiplePlayer) entity).getType().equals(type)) {
				break;
			}
			index++;
		}
		this.entities.remove(index);
	}

	public Player getPlayer() {
		return this.player;
	}
}
