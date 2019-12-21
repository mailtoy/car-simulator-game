package handlers;

import org.lwjgl.util.vector.Vector2f;

import guis.GuiRenderer;
import guis.GuiTexture;
import main.WindowDisplay;

public class SimulatorHandler extends Handler {
	private GuiTexture zoomIn;
	private GuiTexture zoomOut;
	private GuiTexture ClickedZoomIn, ClickedZoomOut;

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
		zoomIn = new GuiTexture(loader.loadTexture("OutBTN"), new Vector2f(0.85f, 0.55f), new Vector2f(0.11f, 0.15f));
		zoomOut = new GuiTexture(loader.loadTexture("InBTN"), new Vector2f(0.85f, -0.15f), new Vector2f(0.11f, 0.15f));
		ClickedZoomIn = new GuiTexture(loader.loadTexture("ClickedOutBTN"), new Vector2f(0.85f, 0.55f),
				new Vector2f(0.11f, 0.15f));
		ClickedZoomOut = new GuiTexture(loader.loadTexture("ClickedInBTN"), new Vector2f(0.85f, -0.15f), new Vector2f(0.11f, 0.15f));

		guis.add(zoomIn);
		guis.add(zoomOut);
		guiRenderer = new GuiRenderer(loader);
	}

	@Override
	public void cleanUp() {
		guiRenderer.render(guis);
	}

	public void changeGui(String whatButton) {
		if(whatButton.equals("+")) {
			guis.remove(zoomIn);
			guis.add(ClickedZoomIn);
		}
		if(whatButton.equals("-")) {
			guis.remove(zoomOut);
			guis.add(ClickedZoomOut);
		}
		
	}

	public void changeDefaultGui() {
		guis.remove(ClickedZoomIn);
		guis.add(zoomIn);
		guis.remove(ClickedZoomOut);
		guis.add(zoomOut);
	}

}