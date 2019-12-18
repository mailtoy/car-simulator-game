package handlers;

import java.util.ArrayList;
import java.util.List;

import guis.GuiRenderer;
import guis.GuiTexture;
import main.WindowDisplay;
import renderEngine.Loader;

public abstract class Handler {
	protected WindowDisplay windowDisplay;
	protected GuiRenderer guiRenderer;
	protected List<GuiTexture> guis;
	protected Loader loader;
	
	public Handler(WindowDisplay windowDisplay) {
		this.windowDisplay = windowDisplay;
		this.guis = new ArrayList<GuiTexture>();
		this.loader = windowDisplay.getLoader();
		
		initGUIs();
	}
	
	public void render() {
		guiRenderer.render(guis);
	}
	
	protected abstract void initGUIs();
	
	
	public abstract void cleanUp();
}
