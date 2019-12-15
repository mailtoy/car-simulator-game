package handlers;

import java.io.File;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;

import guis.GuiRenderer;
import guis.GuiTexture;
import main.WindowDisplay;
import renderEngine.Loader;

public class ControllerHandler extends Handler {
	GuiTexture forward ;

	public ControllerHandler(WindowDisplay windowDisplay) {
		super(windowDisplay);
		initText();
	}

	@Override
	protected void initGUIs() {
		Loader loader = windowDisplay.getLoader();
		forward = new GuiTexture(loader.loadTexture("FBTN"), new Vector2f(0.7f, -0.35f),
				new Vector2f(0.06f, 0.08f));
		GuiTexture backward = new GuiTexture(loader.loadTexture("BBTN"), new Vector2f(0.7f, -0.65f),
				new Vector2f(0.06f, 0.08f));
		GuiTexture left = new GuiTexture(loader.loadTexture("LBTN"), new Vector2f(0.6f, -0.5f),
				new Vector2f(0.06f, 0.08f));
		GuiTexture right = new GuiTexture(loader.loadTexture("RBTN"), new Vector2f(0.8f, -0.5f),
				new Vector2f(0.06f, 0.08f));

		guis.add(forward);
		guis.add(backward);
		guis.add(right);
		guis.add(left);

		guiRenderer = new GuiRenderer(loader);
	}

	private void initText() {
		Loader loader = windowDisplay.getLoader();
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadFontTexture("font"), new File("res/font.fnt"));
		new GUIText("Crash!", 3f, font, new Vector2f(0f, 0f), 1f, true).setColour(255, 255, 255);
	}

	public void textRender() {
		TextMaster.render();
	}

	@Override
	public void cleanUp() {
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
	}

}