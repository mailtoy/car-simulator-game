package connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import engineTester.Simulator;
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
	Camera camera;

	public ClientType() {
		initComponent();
	}

	public void initComponent() {
		JFrame f = new JFrame("Button Example");
		JButton b = new JButton("Click Here");
		b.setBounds(50, 100, 95, 30);
		f.add(b);
		f.setSize(400, 400);
		f.setLayout(null);
		f.setVisible(true);
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		// RawModel model = OBJLoader.loadObjModel("tree", loader);
		ModelData data = OBJFileLoader.loadOBJ("tree");
		RawModel treeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(),
				data.getIndices());
		TexturedModel staticModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
				new ModelTexture(loader.loadTexture("tree")));

		// TexturedModel staticModel = new TexturedModel(model, new
		// ModelTexture(loader.loadTexture("tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));

		grass.getTexture().setHasTransparency(true);
		fern.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(staticModel,
					new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 3));
			entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),
					0, 0, 0, 1));
			entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0,
					0, 0, 0.6f));
		}

		Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));

		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass")));

		MasterRenderer renderer = new MasterRenderer();
		RawModel bunnyModel = OBJLoader.loadObjModel("stanfordBunny", loader);
	
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));
		Player player = new Player(stanfordBunny, new Vector3f(0, 0, -40), 0, 180, 0, 0.6f);
		camera = new Camera(player);
		// Vector3f(0, 0, -60) is position of bunny.
		
		setCamera();

		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	private void setCamera() {
		if (this.getClass().equals(Simulator.class)) {
			camera.setPosition(new Vector3f(100, 200, -100));
			camera.setPitch(90);
		}
	}

	public void write(String message) {
	}
}
