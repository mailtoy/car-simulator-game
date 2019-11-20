package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.MultiplePlayer;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import network.Client;
import network.packet.DisconnectPacket;
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
	private RawModel carModel;
	private TexturedModel staticModel, grassModel, fernModel, car;
	private TerrainTexturePack texturePack;
	private Light light;
	private List<Terrain> terrains;
	private MasterRenderer renderer;
	private List<Entity> entities;

	protected Client client;
	protected Player player; // Change to Car later
	protected Camera camera;
	protected final String TYPE = "Controller" + new Random().nextInt(100); // for now

	public WindowDisplay() {
		this.client = new Client(this);
		this.client.start();

		initComponents();
	}

	private void initComponents() {
		DisplayManager.createDisplay();
		loader = new Loader();
		terrains = new ArrayList<Terrain>();

		// Terrain TextureStaff
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("middleRoad"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("middleRoad"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("middleRoad"));

		texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("map1"));

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
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(staticModel, new Vector3f(0, 0, 0), 0, 0, 0, 3));
			entities.add(new Entity(grassModel, new Vector3f(0, 0, 0), 0, 0, 0, 1));
			entities.add(new Entity(fernModel, new Vector3f(0, 0, 0), 0, 0, 0, 0.6f));
		}

		light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));

//		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);
//		Terrain terrain2 = new Terrain(1, 0, loader, texturePack, blendMap);
//		Terrain terrain3 = new Terrain(0, 1, loader, texturePack, blendMap);
//		Terrain terrain4 = new Terrain(1, 1, loader, texturePack, blendMap);

		terrains.add(new Terrain(0, 0, loader, texturePack, blendMap));

		// * for big map size logic
		int count = 0, x = 1, z = 1, round = 4;
		for (int i = 0; i < round; i++) {
			terrains.add(new Terrain(x + i, i, loader, texturePack, blendMap));
			terrains.add(new Terrain(i, z + i, loader, texturePack, blendMap));
			terrains.add(new Terrain(x + i, z + i, loader, texturePack, blendMap));
		}
		if (round > 1) {
			for (int i = round; i > 1; i--) {
				for (int j = i - 2; j >= 0; j--) {
					terrains.add(new Terrain(i, j, loader, texturePack, blendMap));
					count++;
				}
				for (int k = i - 2; k >= 0; k--) {
					terrains.add(new Terrain(k, i, loader, texturePack, blendMap));
				}
			}
		}

		renderer = new MasterRenderer();

		carModel = OBJLoader.loadObjModel("Car", loader);
		car = new TexturedModel(carModel, new ModelTexture(loader.loadTexture("carTexture2")));
		player = new MultiplePlayer(TYPE, car, new Vector3f(305, 0, -10), 0, 180, 0, 0.6f, null, -1);
	}

	protected void render() {
		for (Terrain terrain : terrains) {
			renderer.processTerrain(terrain);
		}

		for (Entity entity : entities) {
			renderer.processEntity(entity);
		}

		renderer.render(light, camera);
		DisplayManager.updateDisplay();
	}

	protected void closeqRequest(String type) {
		renderer.cleanUp();
		loader.cleanUp();

		DisconnectPacket packet = new DisconnectPacket(type, player.getModel(), player.getPosition(), player.getRotX(),
				player.getRotY(), player.getRotZ(), player.getScale());
		packet.writeData(client);

		DisplayManager.closeDisplay();
	}

	public void addMultiplePlayer(MultiplePlayer player) {
		entities.add(player);
	}

	public void removeMultiplePlayer(String type) {
		entities.remove(loopEntities(0, type));
	}

	private int getMultiplayerIndex(String type) {
		return loopEntities(0, type);
	}

	private int loopEntities(int index, String type) {
		for (Entity entity : entities) {
			if (entity instanceof MultiplePlayer && ((MultiplePlayer) entity).getType().equals(type)) {
				break;
			}
			index++;
		}
		return index;
	}

	public void movePlayer(String type, Vector3f position, float rotX, float rotY, float rotZ) {
		int index = getMultiplayerIndex(type);
		entities.get(index).setPosition(position);
		entities.get(index).setRotX(rotX);
		entities.get(index).setRotY(rotY);
		entities.get(index).setRotZ(rotZ);
	}

	public Player getPlayer() {
		return this.player;
	}

	public Client getClient() {
		return this.client;
	}

}
