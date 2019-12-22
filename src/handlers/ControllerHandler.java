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

/**
 * User Interface of controller for initializing components.
 * 
 * @author Kanchanok Kannee
 *
 */
public class ControllerHandler extends Handler {
	private ArrayList<GuiTexture> carCrashGUIs;
	private boolean isAdded = false;
	private boolean isActive = false;
	private GuiTexture forwardActive, backwardActive, rightActive, leftActive, accActive, brakeActive;
	private GuiTexture forward, backward, right, left, acc, brake;

	/**
	 * 
	 * Create text and gauge GUI.
	 * 
	 * @param windowDisplay window of the program
	 */
	public ControllerHandler(WindowDisplay windowDisplay) {
		super(windowDisplay);

		TextMaster.init(loader);
		initTexts();
		initGauges();
	}

	/**
	 * Initialized all components
	 */
	@Override
	protected void initGUIs() {
		forward = new GuiTexture(loader.loadTexture("FBTN"), new Vector2f(0.7f, -0.35f), new Vector2f(0.06f, 0.08f));
		backward = new GuiTexture(loader.loadTexture("BBTN"), new Vector2f(0.7f, -0.65f), new Vector2f(0.06f, 0.08f));
		left = new GuiTexture(loader.loadTexture("LBTN"), new Vector2f(0.55f, -0.5f), new Vector2f(0.06f, 0.08f));
		right = new GuiTexture(loader.loadTexture("RBTN"), new Vector2f(0.8f, -0.5f), new Vector2f(0.06f, 0.08f));
		acc = new GuiTexture(loader.loadTexture("AccBTN"), new Vector2f(-0.58f, -0.62f), new Vector2f(0.08f, 0.2f));
		brake = new GuiTexture(loader.loadTexture("BrakeBTN"), new Vector2f(-0.7f, -0.55f), new Vector2f(0.1f, 0.1f));

		guis.add(forward);
		guis.add(backward);
		guis.add(right);
		guis.add(left);
		guis.add(acc);
		guis.add(brake);

		GuiTexture bg = new GuiTexture(loader.loadTexture("f"), new Vector2f(0.1f, -0.2f), new Vector2f(0.5f, 0.5f));
		GuiTexture replay = new GuiTexture(loader.loadTexture("ReplayBTN"), new Vector2f(-0.1f, -0.05f),
				new Vector2f(0.2f, 0.2f));
		GuiTexture close = new GuiTexture(loader.loadTexture("QuitBTN"), new Vector2f(0.25f, -0.05f),
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

		accActive = new GuiTexture(loader.loadTexture("ClickedAccBTN"), new Vector2f(-0.58f, -0.62f),
				new Vector2f(0.08f, 0.2f));
		brakeActive = new GuiTexture(loader.loadTexture("ClickedBrakeBTN"), new Vector2f(-0.7f, -0.55f),
				new Vector2f(0.1f, 0.1f));

		guiRenderer = new GuiRenderer(loader);

	}

	/**
	 * Add replay and close button when car crashed.
	 */
	public void addCarCrashGUIs() {
		guis.addAll(carCrashGUIs);
		isAdded = true;
	}

	/**
	 * Removing the replay and close button GUI
	 */
	public void removeCarCrashGUIs() {
		guis.removeAll(carCrashGUIs);
		isAdded = false;
	}

	/**
	 * Change GUI button that button pressed
	 * 
	 * @param arrow direction's name
	 */
	public void setBtnActive(String arrow) {
		switch (arrow) {
		case "forward":
			switchActive(forward, forwardActive);
			break;
		case "backward":
			switchActive(backward, backwardActive);
			break;
		case "right":
			switchActive(right, rightActive);
			break;
		case "left":
			switchActive(left, leftActive);
			break;
		case "brake":
			switchActive(brake, brakeActive);
			break;
		case "acc":
			switchActive(acc, accActive);
			break;
		default:
			break;
		}
	}

	/**
	 * Change GUI button that button released
	 * 
	 * @param arrow direction's name
	 */
	public void setBtnInactive(String arrow) {
		switch (arrow) {
		case "forward":
			switchActive(forwardActive, forward);
			break;
		case "backward":
			switchActive(backwardActive, backward);
			break;
		case "right":
			switchActive(rightActive, right);
			break;
		case "left":
			switchActive(leftActive, left);
			break;
		case "brake":
			switchActive(brakeActive, brake);
			break;
		case "acc":
			switchActive(accActive, acc);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * Add and remove gui when button pressed or released
	 * 
	 * @param inactiveBtn button's name released
	 * @param activeBtn   button's name pressed
	 */
	private void switchActive(GuiTexture inactiveBtn, GuiTexture activeBtn) {
		guis.remove(inactiveBtn);
		guis.add(activeBtn);
	}

	/**
	 * 
	 * Create crash text gui
	 * 
	 */
	private void initTexts() {
		FontType font = new FontType(loader.loadFontTexture("font"), new File("res/font.fnt"));
		new GUIText("Crash!", 3f, font, new Vector2f(0f, 0.15f), 1f, true).setColour(255, 255, 255);
	}

	/**
	 * 
	 * Create gauge gui
	 * 
	 */
	private void initGauges() {
		FontType gaugeFont = new FontType(loader.loadFontTexture("font"), new File("res/font.fnt"));
		for (int i = 0; i < 181; i++) {
			GUIText gauge = new GUIText(i + "", 3f, gaugeFont, new Vector2f(0f, 0f), 1f, true);
			if (i >= 120) {
				gauge.setColour(1, 0, 0);
			}
		}
	}

	/**
	 * 
	 * Rendering text gui
	 * 
	 */
	public void textRender() {
		TextMaster.renderText();
	}

	/**
	 * 
	 * Rendering gauge gui
	 * 
	 * @param currentSpeed The speed at that time
	 */
	public void gaugeRender(float currentSpeed) {
		TextMaster.renderGaugeText((int) Math.abs(currentSpeed) + "");
	}

	/**
	 * 
	 * Check the status of gui that add or not add
	 * 
	 * @return boolean Return true if gui is added otherwise, return false.
	 */
	public boolean isAdded() {
		return isAdded;
	}

	/**
	 * 
	 * Check any button is pressed or not
	 * 
	 * @return boolean Return true if button is pressed otherwise, return false.
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * 
	 * Clear when program close
	 * 
	 */
	@Override
	public void cleanUp() {
		guiRenderer.cleanUp();
		TextMaster.cleanUp();
	}

}