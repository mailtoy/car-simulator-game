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

/**
 * Abstract class of clients used for initializing, rendering and displaying all
 * components such as terrains and entities in the server using LWJGL2 library.
 * 
 * @author Issare Srisomboon
 *
 */
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

	/**
	 * Create an instance of client and initialize all components.
	 */
	public WindowDisplay() {
		this.client = new Client(this);
		this.client.start();

		initComponents();
	}

	/**
	 * Display the window.
	 */
	protected abstract void run();

	/**
	 * Render components based on each client.
	 */
	protected abstract void render();

	/**
	 * Initialize all necessary LWJGL2 components.
	 */
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

		TexturedModel grassModel = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel fernModel = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));

		grassModel.getTexture().setHasTransparency(true);
		fernModel.getTexture().setUseFakeLighting(true);
		fernModel.getTexture().setHasTransparency(true);

		entities = new ArrayList<Entity>();
		light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));

		carModel = OBJLoader.loadObjModel("Car", loader);
		carColor = carColorList.get(new Random().nextInt(carColorList.size()));
		car = new TexturedModel(carModel, new ModelTexture(loader.loadTexture(carColor)));
	}

	/**
	 * Add terrain components to the map
	 */
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

	/**
	 * Get the index of MultiplePlayer from provided specific type of client.
	 * 
	 * @param type Specific type of client
	 * @return The index of MultiplePlayer stored in entities ArrayList
	 */
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

	/**
	 * Check whether the map is changed from the default map or not. If it does,
	 * then reload a new map with new terrains.
	 */
	private void checkMapChanged() {
		if (!map.equals(defaultMap) && !isMapChanged) {
			terrains.clear();
			loadMap();
			isMapChanged = true;
		}
	}

	/**
	 * Check if the player's car color is already set followed by its attribute or
	 * not. If not, then add color texture to its model in order to show different
	 * color cars in the server.
	 */
	private void checkModelsColor() {
		for (Map.Entry<String, Boolean> playerCar : carColorMap.entrySet()) {
			if (!playerCar.getValue()) {
				MultiplePlayer player = (MultiplePlayer) entities.get(getMultiplayerIndex(playerCar.getKey()));
				player.setModel(new TexturedModel(carModel, new ModelTexture(loader.loadTexture(player.getColor()))));
				playerCar.setValue(true);
			}
		}
	}

	/**
	 * Check whether the server forces the client to disconnect or not. If it does,
	 * then close the window.
	 */
	private void checkForceQuit() {
		if (isKicked) {
			closeqRequest();
		}
	}

	/**
	 * Render all basic components.
	 */
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

	/**
	 * Close the display and send disconnection to the server from this client.
	 */
	public void closeqRequest() {
		renderer.cleanUp();
		loader.cleanUp();

		DisplayManager.closeDisplay();

		DisconnectPacket disconnectPacket = new DisconnectPacket(type);
		disconnectPacket.writeData(client);
	}

	/**
	 * Check if the window display all components correctly or not.
	 */
	protected void check() {
		checkMapChanged();
		checkModelsColor();
		checkForceQuit();
	}

	/**
	 * Add a new player (car) in the window displaying in case there is a new client
	 * connected in the server.
	 * 
	 * @param player A new player's car
	 */
	public void addMultiplePlayer(MultiplePlayer player) {
		entities.add(player);
		carColorMap.put(player.getType(), false);
	}

	/**
	 * Remove a player's car from the window displaying when the player disconnect
	 * from the server.
	 * 
	 * @param type Type of client
	 */
	public void removeMultiplePlayer(String type) {
		entities.remove(getMultiplayerIndex(type));
		carColorMap.remove(type);
	}

	/**
	 * Check whether the provided type of client is already added to the window or
	 * not.
	 * 
	 * @param type Type of client
	 * @return boolean True if it is already added in the window displaying
	 *         otherwise, return false.
	 */
	public boolean isAdded(String type) {
		return (getMultiplayerIndex(type) == entities.size()) ? false : true;
	}

	/**
	 * Move the provided type of player's car to the provided position.
	 * 
	 * @param type     Type of client
	 * @param position New position of player's car
	 * @param rotX     New rotation in X axis of player's car
	 * @param rotY     New rotation in Y axis of player's car
	 * @param rotZ     New rotation in Z axis of player's car
	 */
	public void movePlayer(String type, Vector3f position, float rotX, float rotY, float rotZ) {
		int index = getMultiplayerIndex(type);
		MultiplePlayer player = (MultiplePlayer) entities.get(index);
		player.setPosition(position);
		player.setRotX(rotX);
		player.setRotY(rotY);
		player.setRotZ(rotZ);
	}

	/**
	 * Get the loader
	 * 
	 * @return Loader
	 */
	public Loader getLoader() {
		return this.loader;
	}

	/**
	 * Get the model of car
	 * 
	 * @return Model of car
	 */
	public TexturedModel getCarModel() {
		return this.car;
	}

	/**
	 * Set the map to the provided map name.
	 * 
	 * @param map New map
	 */
	public void setMap(String map) {
		this.map = map;
	}

	/**
	 * Get the default of the map.
	 * 
	 * @return The default map
	 */
	public String getDefaultMap() {
		return this.defaultMap;
	}

	/**
	 * Get the specific type of the client.
	 * 
	 * @return Type of client
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Set player to the provided player.
	 * 
	 * @param player New player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Check if the player's car is crashing or not.
	 * 
	 * @return boolean True if the car is crashed otherwise, return false.
	 */
	public boolean isCrashed() {
		return this.isCrashed;
	}

	/**
	 * Set status of crashing of the car.
	 * 
	 * @param crashStatus True means the car is currently crashing and false means
	 *                    the car's still driving properly.
	 */
	public void setCrash(boolean crashStatus) {
		this.isCrashed = crashStatus;
	}

	/**
	 * Set status of forcing quit from the server.
	 * 
	 * @param kickStatus True means the server is now forcing the client to
	 *                   disconnect and false means the client is either not removed
	 *                   yet or never removed from the server.
	 */
	public void setKick(boolean kickStatus) {
		this.isKicked = kickStatus;
	}

	/**
	 * Get a handler for each client.
	 * 
	 * @return The Handler
	 */
	public Handler getHandler() {
		return handler;
	}
}