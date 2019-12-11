package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.MultiplePlayer;
import entities.Player;
import handlers.Handler;
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
	private TerrainTexturePack texturePack;
	private final String defaultMap = "map1";
	private String map = defaultMap;
	private boolean isMapChanged = false;
	private boolean isKicked = false;
	private List<String> carColor;

	protected MasterRenderer renderer;
	protected Light light;
	protected List<Terrain> terrains;
	protected List<Entity> entities;
	protected TexturedModel car;
	protected TerrainTexture blendMap;

	protected Handler handler;
	protected Client client;
	protected Player player; // Change to Car later
	protected Camera camera;
	protected int round = 3;
	protected boolean isCrashed = false;
	protected final String type = this.getClass().toString().substring(11) + new Random().nextInt(100); // for now

	public WindowDisplay() {
		this.client = new Client(this);
		this.client.start();

		initComponents();
	}

	protected abstract void run();

	protected abstract void render();

	private void initComponents() {
		DisplayManager.createDisplay("Car " + type);
		loader = new Loader();
		renderer = new MasterRenderer(loader);
		terrains = new ArrayList<Terrain>();
		setcarColor();

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("sideRoad"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("road"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("middleRoad"));

		texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		loadMap();

		TexturedModel staticModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
				new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grassModel = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel fernModel = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));

		grassModel.getTexture().setHasTransparency(true);
		fernModel.getTexture().setUseFakeLighting(true);
		fernModel.getTexture().setHasTransparency(true);

		entities = new ArrayList<Entity>();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(staticModel, new Vector3f(800, 0, 800), 0, 0, 0, 3));
			entities.add(new Entity(grassModel, new Vector3f(0, 0, 0), 0, 0, 0, 1));
			entities.add(new Entity(fernModel, new Vector3f(0, 0, 0), 0, 0, 0, 0.6f));
		}

		light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));
		RawModel carModel = OBJLoader.loadObjModel("Car", loader);
		int rnd = new Random().nextInt(carColor.size());
		car = new TexturedModel(carModel, new ModelTexture(loader.loadTexture(carColor.get(rnd))));
	}

	private void loadMap() {
		blendMap = new TerrainTexture(loader.loadTexture(map));
		terrains.add(new Terrain(0, 0, loader, texturePack, blendMap));

		// * for big map size logic
		int x = 1, z = 1;
		for (int i = 0; i < round; i++) {
			terrains.add(new Terrain(x + i, i, loader, texturePack, blendMap));
			terrains.add(new Terrain(i, z + i, loader, texturePack, blendMap));
			terrains.add(new Terrain(x + i, z + i, loader, texturePack, blendMap));
		}
		if (round > 1) {
			for (int i = round; i > 1; i--) {
				for (int j = i - 2; j >= 0; j--) {
					terrains.add(new Terrain(i, j, loader, texturePack, blendMap));
				}
				for (int k = i - 2; k >= 0; k--) {
					terrains.add(new Terrain(k, i, loader, texturePack, blendMap));
				}
			}
		}
	}

	private void reloadMap() {
		terrains.clear();
		loadMap();

		isMapChanged = true;
	}
	
	protected void renderComponents() {
		for (Iterator<Terrain> iterator = terrains.iterator(); iterator.hasNext();) {
			Terrain terrain = iterator.next();
			renderer.processTerrain(terrain);
		}
		for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext();) {
			Entity entity = iterator.next();
			renderer.processEntity(entity);
		}

		renderer.render(light, camera);
		handler.render();
	}

	protected void closeqRequest() {
		renderer.cleanUp();
		loader.cleanUp();

		DisplayManager.closeDisplay();

		DisconnectPacket disconnectPacket = new DisconnectPacket(type);
		disconnectPacket.writeData(client);
	}

	protected void checkMapChanged() {
		if (!map.equals(defaultMap) && !isMapChanged) {
			reloadMap();
		}
	}

	protected void checkForceQuit() {
		if (isKicked) {
			closeqRequest();
		}
	}

	private int getMultiplayerIndex(String type) {
		int index = 0;
		for (Entity entity : entities) {
			if (entity instanceof MultiplePlayer && ((MultiplePlayer) entity).getType().equals(type)) {
				break;
			}
			index++;
		}
		return index;
	}

	public void addMultiplePlayer(MultiplePlayer player) {
		entities.add(player);
	}

	public void removeMultiplePlayer(String type) {
		entities.remove(getMultiplayerIndex(type));
	}

	public boolean isAdded(String type) {
		return (getMultiplayerIndex(type) == entities.size()) ? false : true;
	}

	public void movePlayer(String type, Vector3f position, float rotX, float rotY, float rotZ) {
		int index = getMultiplayerIndex(type);
		MultiplePlayer player = (MultiplePlayer) entities.get(index);
		player.setPosition(position);
		player.setRotX(rotX);
		player.setRotY(rotY);
		player.setRotZ(rotZ);
	}

	public Loader getLoader() {
		return this.loader;
	}

	public TexturedModel getCarModel() {
		return this.car;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getDefaultMap() {
		return this.defaultMap;
	}

	public String getType() {
		return this.type;
	}

	public void setCrash(boolean crashStatus) {
		this.isCrashed = crashStatus;
	}

	public void setKick(boolean kickStatus) {
		this.isKicked = kickStatus;
	}

	public void setcarColor() {
		carColor = new ArrayList<String>();
		carColor.add("greenColor");
		carColor.add("grayColor");
		carColor.add("redColor");
		carColor.add("blueColor");
		carColor.add("purpleColor");
		carColor.add("yellowColor");
	}
}