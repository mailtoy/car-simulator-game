package main;

import java.util.ArrayList;
import java.util.List;

import guis.GuiRenderer;
import guis.GuiTexture;

public abstract class Handler {
	protected WindowDisplay windowDisplay;
	protected GuiRenderer guiRenderer;
	protected List<GuiTexture> guis;
	
	public Handler(WindowDisplay windowDisplay) {
		this.windowDisplay = windowDisplay;
		this.guis = new ArrayList<GuiTexture>();
		
		initGUIs();
	}
	
	public void render() {
		guiRenderer.render(guis);
	}
	
	protected abstract void initGUIs();
	
	public abstract void cleanUp();
}
