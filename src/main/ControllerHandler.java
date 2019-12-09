package main;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import fontRendering.GaugeTextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import renderEngine.Loader;

public class ControllerHandler {
	private Controller controller;
	private GuiRenderer guiRenderer;
	private List<GuiTexture> guis;
	private GuiTexture forward, backward, right, left, bg;

	public ControllerHandler(Controller controller) {
		this.controller = controller;
		this.guis = new ArrayList<GuiTexture>();
		initGUIs();
	}

	private void initGUIs() {
		Loader loader = controller.getLoader();
		forward = new GuiTexture(loader.loadTexture("FBTN"), new Vector2f(0.7f, -0.35f), new Vector2f(0.06f, 0.08f));
		backward = new GuiTexture(loader.loadTexture("BBTN"), new Vector2f(0.7f, -0.65f), new Vector2f(0.06f, 0.08f));
		left = new GuiTexture(loader.loadTexture("LBTN"), new Vector2f(0.6f, -0.5f), new Vector2f(0.06f, 0.08f));
		right = new GuiTexture(loader.loadTexture("RBTN"), new Vector2f(0.8f, -0.5f), new Vector2f(0.06f, 0.08f));
//		bg = new GuiTexture(loader.loadTexture("table"), new Vector2f(0.8f, -0.6f), new Vector2f(0.8f, 0.4f));

		guis.add(forward);
		guis.add(backward);
		guis.add(right);
		guis.add(left);

		guiRenderer = new GuiRenderer(loader);
	}
	
	public void render() {
		GaugeTextMaster.render();
		guiRenderer.render(guis);
	}

	public void cleanUp() {
		GaugeTextMaster.cleanUp();
		guiRenderer.cleanUp();
	}

}