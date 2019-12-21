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
	private boolean isActive = false;
	private GuiTexture forwardActive, backwardActive, rightActive, leftActive;
	private GuiTexture forward, backward, right, left;

	public ControllerHandler(WindowDisplay windowDisplay) {
		super(windowDisplay);

		TextMaster.init(loader);
		initTexts();
		initGauges();
	}

	@Override
	protected void initGUIs() {
		forward = new GuiTexture(loader.loadTexture("FBTN"), new Vector2f(0.7f, -0.35f),
				new Vector2f(0.06f, 0.08f));
		backward = new GuiTexture(loader.loadTexture("BBTN"), new Vector2f(0.7f, -0.65f),
				new Vector2f(0.06f, 0.08f));
		left = new GuiTexture(loader.loadTexture("LBTN"), new Vector2f(0.55f, -0.5f),
				new Vector2f(0.06f, 0.08f));
		right = new GuiTexture(loader.loadTexture("RBTN"), new Vector2f(0.8f, -0.5f),
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
		
		forwardActive = new GuiTexture(loader.loadTexture("ClickedFBTN"), new Vector2f(0.7f, -0.35f),
				new Vector2f(0.06f, 0.08f));
		backwardActive = new GuiTexture(loader.loadTexture("ClickedBBTN"), new Vector2f(0.7f, -0.65f),
				new Vector2f(0.06f, 0.08f));
		leftActive = new GuiTexture(loader.loadTexture("ClickedLBTN"), new Vector2f(0.55f, -0.5f),
				new Vector2f(0.06f, 0.08f));
		rightActive = new GuiTexture(loader.loadTexture("ClickedRBTN"), new Vector2f(0.8f, -0.5f),
				new Vector2f(0.06f, 0.08f));

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
	
	public void changeButtonGUIs(int arrow) {
		if(arrow == 1) {
			System.out.println("remove ^");
			guis.remove(forward);
			guis.add(forwardActive);
		} else if (arrow == 2) {
			guis.remove(backward);
			guis.add(backwardActive);
		}  else if (arrow == 3) {
			guis.remove(right);
			guis.add(rightActive);
		}  else if (arrow == 4) {
			System.out.println("remove <");
			guis.remove(left);
			guis.add(leftActive);
		}
		
	}
	
	public void changeButtonGUIsBack(int arrow) {
		if (arrow == 1) {
			System.out.println("add ^");
			guis.remove(forwardActive);
			guis.add(forward);
		} else if (arrow == 2) {
			guis.remove(backwardActive);
			guis.add(backward);
		} else if (arrow == 3) {
			guis.remove(rightActive);
			guis.add(right);
		} else if (arrow == 4) {
			System.out.println("add >");
			guis.remove(leftActive);
			guis.add(left);
		}
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
	
	public boolean isActive(){
		return isActive;
	}

	@Override
	public void cleanUp() {
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
	}


}