package handlers;

import java.util.ArrayList;
import java.util.List;

import guis.GuiRenderer;
import guis.GuiTexture;
import main.WindowDisplay;

public abstract class Handler {
	protected WindowDisplay windowDisplay;
	protected GuiRenderer guiRenderer;
	protected List<GuiTexture> guis;
	protected Boolean isClick = false;
	
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
