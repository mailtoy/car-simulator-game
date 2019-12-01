package main;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import buttons.AbstractButton;
import buttons.IButton;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.MultiplePlayer;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import network.Client;
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
	protected TexturedModel staticModel, grassModel, fernModel, car;
	private TerrainTexturePack texturePack;
	private Light light;
	private List<Terrain> terrains;
	private MasterRenderer renderer;
	private GuiRenderer guiRenderer;
	private AbstractButton pathButton;
	private List<GuiTexture> guis;
	private List<Entity> entities;
	private GuiTexture forward, backward, right, left, bg;
	private List<GuiTexture> guiTextures;
	protected int round = 3;

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
		terrains = new ArrayList<Terrain>();

		// Terrain TextureStaff
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("sideRoad"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("road"));
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

		// Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);
		// Terrain terrain2 = new Terrain(1, 0, loader, texturePack, blendMap);
		// Terrain terrain3 = new Terrain(0, 1, loader, texturePack, blendMap);
		// Terrain terrain4 = new Terrain(1, 1, loader, texturePack, blendMap);

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
		renderer = new MasterRenderer();

		carModel = OBJLoader.loadObjModel("Car", loader);
		car = new TexturedModel(carModel, new ModelTexture(loader.loadTexture("carTexture2")));

		guis = new ArrayList<GuiTexture>();
		forward = new GuiTexture(loader.loadTexture("FBTN"), new Vector2f(0.7f, -0.55f), new Vector2f(0.06f, 0.08f));
		backward = new GuiTexture(loader.loadTexture("BBTN"), new Vector2f(0.7f, -0.85f), new Vector2f(0.06f, 0.08f));
		
		left = new GuiTexture(loader.loadTexture("LBTN"), new Vector2f(0.6f, -0.7f), new Vector2f(0.06f, 0.08f));
		right = new GuiTexture(loader.loadTexture("RBTN"), new Vector2f(0.8f, -0.7f), new Vector2f(0.06f, 0.08f));
		bg = new GuiTexture(loader.loadTexture("table"), new Vector2f(0.8f, -0.8f), new Vector2f(0.8f, 0.4f));
		guis.add(bg);
		guis.add(forward);
		guis.add(backward);
		guis.add(right);
		guis.add(left);
		
		// guiTextures = new ArrayList<GuiTexture>();
		guiRenderer = new GuiRenderer(loader);
		//
		// pathButton = new AbstractButton(loader, "path", new Vector2f(0, 0),
		// new Vector2f(0.2f, 0.2f)) {
		//
		// @Override
		// public void whileHovering(IButton button) {
		// }
		//
		// @Override
		// public void resetScale() {
		// }
		//
		// @Override
		// public void onStopHover(IButton button) {
		// button.resetScale();
		// }
		//
		// @Override
		// public void onStartHover(IButton button) {
		// button.playHoverAnimation(0.092f);
		// }
		//
		// @Override
		// public void onClick(IButton button) {
		// System.out.println("click");
		// }
		// };

	}

	protected void render() {
		// while (Keyboard.next()) {
		// if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
		// if (pathButton.isHidden()) {
		// pathButton.show(guiTextures);
		// } else {
		// pathButton.hide(guiTextures);
		// }
		// }
		// }
		// // buttons
		// pathButton.update();

		for (Terrain terrain : terrains) {
			renderer.processTerrain(terrain);
		}

		for (Entity entity : entities) {
			renderer.processEntity(entity);
		}

		renderer.render(light, camera);
		guiRenderer.render(guis);

		DisplayManager.updateDisplay();
	}

	protected void closeqRequest() {
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();

		DisplayManager.closeDisplay();
	}

	public void addMultiplePlayer(MultiplePlayer player) {
		entities.add(player);
	}

	public void removeMultiplePlayer(String type) {
		entities.remove(loopEntities(type));
	}

	public boolean isAdded(String type) {
		return (loopEntities(type) == entities.size()) ? false : true;
	}

	private int getMultiplayerIndex(String type) {
		return loopEntities(type);
	}

	private int loopEntities(String type) {
		int index = 0;
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
		MultiplePlayer player = (MultiplePlayer) entities.get(index);
		player.setPosition(position);
		player.setRotX(rotX);
		player.setRotY(rotY);
		player.setRotZ(rotZ);
	}

	public Player getPlayer() {
		return this.player;
	}

	public Client getClient() {
		return this.client;
	}

}
