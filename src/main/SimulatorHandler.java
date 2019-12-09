package main;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import guis.GuiRenderer;
import guis.GuiTexture;
import renderEngine.Loader;

public class SimulatorHandler {
	private Simulator simulator;
	private GuiRenderer guiRenderer;
	private List<GuiTexture> guis;
	private GuiTexture zoomIn, zoomOut;

	public SimulatorHandler(Simulator simulator) {
		this.simulator = simulator;
		this.guis = new ArrayList<GuiTexture>();
		initGUIs();
	}

	private void initGUIs() {
		Loader loader = simulator.geLoader();
		zoomIn = new GuiTexture(loader.loadTexture("plus"), new Vector2f(0.8f, 0.55f), new Vector2f(0.06f, 0.08f));
		zoomOut = new GuiTexture(loader.loadTexture("minus"), new Vector2f(0.8f, -0.15f), new Vector2f(0.06f, 0.08f));
	

		guis.add(zoomIn);
		guis.add(zoomOut);

		guiRenderer = new GuiRenderer(loader);
	}

	public void render() {
		guiRenderer.render(guis);
	}

}