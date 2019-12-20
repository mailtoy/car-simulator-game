package handlers;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;

import guis.GuiRenderer;
import guis.GuiTexture;
import main.WindowDisplay;

public class ControllerHandler extends Handler {
	private ArrayList<GuiTexture> carCrashGUIs;
	private boolean isAdded = false;

	public ControllerHandler(WindowDisplay windowDisplay) {
		super(windowDisplay);

		TextMaster.init(loader);
		initTexts();
		initGauges();
	}

	@Override
	protected void initGUIs() {
		GuiTexture forward = new GuiTexture(loader.loadTexture("FBTN"), new Vector2f(0.7f, -0.35f),
				new Vector2f(0.06f, 0.08f));
		GuiTexture backward = new GuiTexture(loader.loadTexture("BBTN"), new Vector2f(0.7f, -0.65f),
				new Vector2f(0.06f, 0.08f));
		GuiTexture left = new GuiTexture(loader.loadTexture("LBTN"), new Vector2f(0.55f, -0.5f),
				new Vector2f(0.06f, 0.08f));
		GuiTexture right = new GuiTexture(loader.loadTexture("RBTN"), new Vector2f(0.8f, -0.5f),
				new Vector2f(0.06f, 0.08f));
		GuiTexture speedup = new GuiTexture(loader.loadTexture("ABTN"), new Vector2f(-0.7f, -0.5f),
				new Vector2f(0.17f, 0.17f));

		guis.add(forward);
		guis.add(backward);
		guis.add(right);
		guis.add(left);
		guis.add(speedup);

		GuiTexture bg = new GuiTexture(loader.loadTexture("f"), new Vector2f(0.1f, 0.0f), new Vector2f(0.5f, 0.5f));
		GuiTexture replay = new GuiTexture(loader.loadTexture("ReplayBTN"), new Vector2f(-0.1f, 0.1f),
				new Vector2f(0.2f, 0.2f));
		GuiTexture close = new GuiTexture(loader.loadTexture("QuitBTN"), new Vector2f(0.25f, 0.1f),
				new Vector2f(0.2f, 0.2f));

		carCrashGUIs = new ArrayList<GuiTexture>();
		carCrashGUIs.add(bg);
		carCrashGUIs.add(replay);
		carCrashGUIs.add(close);

		guiRenderer = new GuiRenderer(loader);

	}

	public void addCarCrashGUIs() {
		guis.addAll(carCrashGUIs);
		isAdded = true;
	}

	public void removeCarCrashGUIs() {
		guis.removeAll(carCrashGUIs);
		isAdded = false;
	}

	private void initTexts() {
		FontType font = new FontType(loader.loadFontTexture("font"), new File("res/font.fnt"));
		new GUIText("Crash!", 3f, font, new Vector2f(0f, 0f), 1f, true).setColour(255, 255, 255);

	}

	private void initGauges() {
		FontType gaugeFont = new FontType(loader.loadFontTexture("font"), new File("res/font.fnt"));
		for (int i = 0; i < 181; i++) {
			GUIText gauge = new GUIText(i + "", 3f, gaugeFont, new Vector2f(0f, 0f), 1f, true);
			if (i >= 120) {
				gauge.setColour(1, 0, 0);
			}
		}
	}

	public void textRender() {
		TextMaster.renderText();
	}

	public void gaugeRender(float currentSpeed) {
		TextMaster.renderGaugeText((int) Math.abs(currentSpeed) + "");
	}

	public boolean isAdded() {
		return isAdded;
	}

	@Override
	public void cleanUp() {
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
	}

}