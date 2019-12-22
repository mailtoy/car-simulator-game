package handlers;

import java.util.ArrayList;
import java.util.List;

import guis.GuiRenderer;
import guis.GuiTexture;
import main.WindowDisplay;
import renderEngine.Loader;

/**
 * Class for handling all the text and images guis displaying in the window.
 * 
 * @author Issaree Srisomboon
 *
 */
public abstract class Handler {
	protected WindowDisplay windowDisplay;
	protected GuiRenderer guiRenderer;
	protected List<GuiTexture> guis;
	protected Loader loader;

	/**
	 * Constructor of Handler handling for each window.
	 * 
	 * @param windowDisplay Window displaying
	 */
	public Handler(WindowDisplay windowDisplay) {
		this.windowDisplay = windowDisplay;
		this.guis = new ArrayList<GuiTexture>();
		this.loader = windowDisplay.getLoader();

		initGUIs();
	}

	/**
	 * Render all the guis.
	 */
	public void render() {
		guiRenderer.render(guis);
	}

	/**
	 * Initialize both text and images guis.
	 */
	protected abstract void initGUIs();

	/**
	 * Clean the all the components in guis.
	 */
	public abstract void cleanUp();

}
