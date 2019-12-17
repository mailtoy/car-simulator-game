package handlers;

import java.io.File;

import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;

import guis.GuiRenderer;
import guis.GuiTexture;
import main.WindowDisplay;

public class ControllerHandler extends Handler {

	public ControllerHandler(WindowDisplay windowDisplay) {
		super(windowDisplay);

		initTexts();
		initGauges();
	}

	@Override
	protected void initGUIs() {
		GuiTexture forward = new GuiTexture(loader.loadTexture("FBTN"), new Vector2f(0.7f, -0.35f),
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

	private void initTexts() {
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadFontTexture("font"), new File("res/font.fnt"));
		new GUIText("Crash!", 3f, font, new Vector2f(0f, 0f), 1f, true).setColour(255, 255, 255);
	}

	private void initGauges() {
		TextMaster.init(loader);
		FontType gaugeFont = new FontType(loader.loadFontTexture("font"), new File("res/font.fnt"));
		for (int i = 0; i < 180; i++) {
			new GUIText(i + "", 3f, gaugeFont, new Vector2f(0f, 0f), 1f, true);
		}
	}

	public void textRender() {
		TextMaster.renderText();
	}

	public void gaugeRender(float currentSpeed) {
		TextMaster.renderGaugeText((int) Math.abs(currentSpeed) + "");
	}

	@Override
	public void cleanUp() {
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
	}

}