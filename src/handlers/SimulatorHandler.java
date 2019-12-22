package handlers;

import org.lwjgl.util.vector.Vector2f;

import guis.GuiRenderer;
import guis.GuiTexture;
import main.WindowDisplay;

/**
 * For handle all gui activity in simulator display.
 * 
 * @author Worawat Chueajedton
 *
 */

public class SimulatorHandler extends Handler {
	private GuiTexture zoomIn;
	private GuiTexture zoomOut;
	private GuiTexture ClickedZoomIn, ClickedZoomOut;

	public SimulatorHandler(WindowDisplay windowDisplay) {
		super(windowDisplay);
	}

	/**
	 * 
	 * Get gui zoom in.
	 * 
	 * @return zoom in button
	 */
	public GuiTexture getZoomIn() {
		return zoomIn;
	}

	/**
	 * 
	 * Get gui zoom out.
	 * 
	 * @return zoom out button
	 */
	public GuiTexture getZoomOut() {
		return zoomOut;
	}

	/**
	 * 
	 * Creating zoom in and out button, also clicked button image and add both in
	 * the screen.
	 * 
	 */
	@Override
	protected void initGUIs() {
		zoomIn = new GuiTexture(loader.loadTexture("OutBTN"), new Vector2f(0.85f, 0.55f), new Vector2f(0.11f, 0.15f));
		zoomOut = new GuiTexture(loader.loadTexture("InBTN"), new Vector2f(0.85f, -0.15f), new Vector2f(0.11f, 0.15f));
		ClickedZoomIn = new GuiTexture(loader.loadTexture("ClickedOutBTN"), new Vector2f(0.85f, 0.55f),
				new Vector2f(0.11f, 0.15f));
		ClickedZoomOut = new GuiTexture(loader.loadTexture("ClickedInBTN"), new Vector2f(0.85f, -0.15f),
				new Vector2f(0.11f, 0.15f));

		guis.add(zoomIn);
		guis.add(zoomOut);
		guiRenderer = new GuiRenderer(loader);
	}

	/**
	 * Render all component in guis.
	 * 
	 */
	@Override
	public void cleanUp() {
		guiRenderer.render(guis);
	}

	/**
	 * 
	 * When button is clicked, we need to change the image into the clicked image.
	 * 
	 * @param whatButton use for checking what button need to change + is zoom in -
	 *                   is zoom out
	 */
	public void changeGui(String whatButton) {
		if (whatButton.equals("+")) {
			guis.remove(zoomIn);
			guis.add(ClickedZoomIn);
		}
		if (whatButton.equals("-")) {
			guis.remove(zoomOut);
			guis.add(ClickedZoomOut);
		}

	}

	/**
	 * To restore the default gui, when all button is not click.
	 * 
	 */
	public void changeDefaultGui() {
		guis.remove(ClickedZoomIn);
		guis.add(zoomIn);
		guis.remove(ClickedZoomOut);
		guis.add(zoomOut);
	}

}