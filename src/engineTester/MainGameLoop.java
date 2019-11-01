package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;

public class MainGameLoop extends JFrame {

	JFrame f;
	public static JButton north;
	public static JButton east;
	public MainGameLoop() {
		// ... Create components (but without listeners)
		north = new JButton("North");
		east = new JButton("East");
		JButton south = new JButton("South");
		JButton west = new JButton("West");
		JButton center = new JButton("Center");
		
		// ... Create content pane, set layout, add components
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

		content.add(north, BorderLayout.NORTH);
		content.add(east, BorderLayout.EAST);
		content.add(south, BorderLayout.SOUTH);
		content.add(west, BorderLayout.WEST);
		content.add(center, BorderLayout.CENTER);

		// ... Set window characteristics.
		setContentPane(content);
		setTitle("BorderTest");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		// JFrame frame = new JFrame("Arrow Button Demo");
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setLayout(new BorderLayout(5,5));
		// frame.add(new BasicArrowButton(BasicArrowButton.EAST),
		// BorderLayout.EAST);
		// frame.add(new BasicArrowButton(BasicArrowButton.NORTH),
		// BorderLayout.NORTH);
		// frame.add(new BasicArrowButton(BasicArrowButton.SOUTH),
		// BorderLayout.SOUTH);
		// frame.add(new BasicArrowButton(BasicArrowButton.WEST),
		// BorderLayout.WEST);
		// frame.pack();
		// frame.setSize(300, 300);
		// frame.setVisible(true);

		// f = new JFrame();
		//
		// JButton b1 = new JButton("1");
		// JButton b2 = new JButton("2");
		// JButton b3 = new JButton("3");
		// JButton b4 = new JButton("4");
		// JButton b5 = new JButton("5");
		// JButton b6 = new JButton("6");
		// JButton b7 = new JButton("7");
		// JButton b8 = new JButton("8");
		// JButton b9 = new JButton("9");
		//
		// f.add(b1);
		// f.add(b2);
		// f.add(b3);
		// f.add(b4);
		// f.add(b5);
		// f.add(b6);
		// f.add(b7);
		// f.add(b8);
		// f.add(b9);
		//
		// f.setLayout(new GridLayout(3, 3));
		// setting grid layout of 3 rows and 3 columns
		//
		// f.setSize(300, 300);
		// f.setVisible(true);
		// GridBagLayout grid= new GridBagLayout();
		// GridBagConstraints gbc = new GridBagConstraints();
		// setLayout(grid);
		// setTitle("GridBag Layout Example");
		// GridBagLayout layout = new GridBagLayout();
		// this.setLayout(layout);
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.gridx = 0;
		// gbc.gridy = 0;
		// gbc.gridwidth = 2;
		// this.add(new Button("Button One"), gbc);
		// gbc.gridx = 1;
		// gbc.gridy = 0;
		// this.add(new Button("Button two"), gbc);
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.ipady = 20;
		// gbc.gridx = 0;
		// gbc.gridy = 1;
		// this.add(new Button("Button Three"), gbc);
		// gbc.gridx = 1;
		// gbc.gridy = 1;
		// this.add(new Button("Button Four"), gbc);
		// gbc.gridx = 0;
		// gbc.gridy = 2;
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.gridwidth = 2;
		// this.add(new Button("Button Five"), gbc);
		// setSize(300, 300);
		// setPreferredSize(getSize());
		// setVisible(true);
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

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

		// Vector3f(0, 0, -60) is position of bunny.
		Player player = new Player(stanfordBunny, new Vector3f(0, 0, -40), 0, 180, 0, 0.6f);
		Camera camera = new Camera(player);
		JFrame window = new MainGameLoop();
		window.setVisible(true);
		
		// MainGameLoop a = new MainGameLoop();
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
//			if (east.isSelected()) {
//				east.addActionListener(new ActionListener() {
//
//				    @Override
//				    public void actionPerformed(ActionEvent e) {
//				        player.checkInputsClick(1);
//				    }
//				});
//			}
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
}
