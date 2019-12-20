package handlers;

import org.lwjgl.util.vector.Vector2f;

import guis.GuiRenderer;
import guis.GuiTexture;
import main.WindowDisplay;
import renderEngine.Loader;

public class SimulatorHandler extends Handler {
	private GuiTexture zoomIn;
	private GuiTexture zoomOut;
	private GuiTexture ClickedZoomIn;
	private Boolean isClicked = false;

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
		Loader loader = windowDisplay.getLoader();
		zoomIn = new GuiTexture(loader.loadTexture("plus"), new Vector2f(0.8f, 0.55f), new Vector2f(0.06f, 0.08f));
		zoomOut = new GuiTexture(loader.loadTexture("minus"), new Vector2f(0.8f, -0.15f), new Vector2f(0.06f, 0.08f));
		ClickedZoomIn = new GuiTexture(loader.loadTexture("Clickedplus"), new Vector2f(0.8f, 0.55f), new Vector2f(0.06f, 0.08f));
//		ClickedZoomOut = new GuiTexture(loader.loadTexture("minus"), new Vector2f(0.8f, -0.15f), new Vector2f(0.06f, 0.08f));

		guis.add(zoomIn);
		guis.add(zoomOut);
		checkClick();
		guiRenderer = new GuiRenderer(loader);
	}

	@Override
	public void cleanUp() {
		guiRenderer.render(guis);
	}
	
	public void checkClick() {
		if(isClicked == true) {
			guis.add(ClickedZoomIn);
			System.out.println("เติม");
		} else {
			guis.remove(ClickedZoomIn);
			System.out.println("ลบ");
		}
		
	}

}