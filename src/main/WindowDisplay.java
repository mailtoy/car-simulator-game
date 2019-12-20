package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private RawModel carModel;
	private MasterRenderer renderer;
	private Light light;
	private TerrainTexture blendMap;

	private final String defaultMap = "map1";
	private String map = defaultMap;
	private boolean isMapChanged = false;
	private boolean isKicked = false;
	private boolean isCrashed = false;
	private List<Terrain> terrains;
	private List<Entity> entities;
	private List<String> carColorList;
	private Map<String, Boolean> carColorMap;

	protected Handler handler;
	protected Camera camera;
	protected Client client;
	protected Player player;
	protected TexturedModel car;
	protected String carColor;
	protected int round = 3;
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

		carColorMap = new HashMap<String, Boolean>();
		carColorList = new ArrayList<String>();
		carColorList.add("greenColor");
		carColorList.add("grayColor");
		carColorList.add("redColor");
		carColorList.add("blueColor");
		carColorList.add("purpleColor");
		carColorList.add("yellowColor");

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("sideRoad"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("road"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("middleRoad"));

		texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		terrains = new ArrayList<Terrain>();
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

		carModel = OBJLoader.loadObjModel("Car", loader);
		carColor = carColorList.get(new Random().nextInt(carColorList.size()));
		car = new TexturedModel(carModel, new ModelTexture(loader.loadTexture(carColor)));
	}

	private void loadMap() {
		blendMap = new TerrainTexture(loader.loadTexture(map));
		terrains.add(new Terrain(0, 0, loader, texturePack, blendMap));

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

	private void checkMapChanged() {
		if (!map.equals(defaultMap) && !isMapChanged) {
			terrains.clear();
			loadMap();
			isMapChanged = true;
		}
	}

	private void checkModelsColor() {
		for (Map.Entry<String, Boolean> playerCar : carColorMap.entrySet()) {
			if (!playerCar.getValue()) {
				MultiplePlayer player = (MultiplePlayer) entities.get(getMultiplayerIndex(playerCar.getKey()));
				player.setModel(new TexturedModel(carModel, new ModelTexture(loader.loadTexture(player.getColor()))));
				playerCar.setValue(true);
			}
		}
	}

	private void checkForceQuit() {
		if (isKicked) {
			closeqRequest();
		}
	}

	protected void renderComponents() {
		for (Terrain terrain : terrains) {
			renderer.processTerrain(terrain);
		}
		for (Entity entity : entities) {
			renderer.processEntity(entity);
		}
		renderer.render(light, camera);
		handler.render();
	}

	public void closeqRequest() {
		renderer.cleanUp();
		loader.cleanUp();

		DisplayManager.closeDisplay();

		DisconnectPacket disconnectPacket = new DisconnectPacket(type);
		disconnectPacket.writeData(client);
	}

	protected void check() {
		checkMapChanged();
		checkModelsColor();
		checkForceQuit();
	}

	public void addMultiplePlayer(MultiplePlayer player) {
		entities.add(player);
		carColorMap.put(player.getType(), false);
	}

	public void removeMultiplePlayer(String type) {
		entities.remove(getMultiplayerIndex(type));
		carColorMap.remove(type);
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

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean isCrashed() {
		return this.isCrashed;
	}

	public void setCrash(boolean crashStatus) {
		this.isCrashed = crashStatus;
	}

	public void setKick(boolean kickStatus) {
		this.isKicked = kickStatus;
	}

	public Handler getHandler() {
		return handler;
	}
}