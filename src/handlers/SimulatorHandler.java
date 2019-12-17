package handlers;

import org.lwjgl.util.vector.Vector2f;

import guis.GuiRenderer;
import guis.GuiTexture;
import main.WindowDisplay;

public class SimulatorHandler extends Handler {
	private GuiTexture zoomIn;
	private GuiTexture zoomOut;

	public SimulatorHandler(WindowDisplay windowDisplay) {
		super(windowDisplay);
	}

	public GuiTexture getZoomIn() {
		return zoomIn;
	}

	public GuiTexture getZoomOut() {
		return zoomOut;
	}

	@Override
	protected void initGUIs() {
		zoomIn = new GuiTexture(loader.loadTexture("plus"), new Vector2f(0.8f, 0.55f), new Vector2f(0.06f, 0.08f));
		zoomOut = new GuiTexture(loader.loadTexture("minus"), new Vector2f(0.8f, -0.15f), new Vector2f(0.06f, 0.08f));

		guis.add(zoomIn);
		guis.add(zoomOut);

		guiRenderer = new GuiRenderer(loader);
	}

	@Override
	public void cleanUp() {
		guiRenderer.render(guis);
	}

}