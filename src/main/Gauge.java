package main;

import java.io.File;

import org.lwjgl.util.vector.Vector2f;

import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GaugeGUIText;
import fontRendering.GaugeTextMaster;
import renderEngine.Loader;

public class Gauge  {
	private Controller controller;
	private Player player;

	public Gauge(Controller controller,  float speed) {
		this.controller = controller;
		this.player = controller.player;
		initGaugeGUIs();
	}
	
	private void initGaugeGUIs() {
		Loader loader = controller.geLoader();

		GaugeTextMaster.init(loader);
		FontType gauge = new FontType(loader.loadFontTexture("font"), new File("res/font.fnt"));
		new GaugeGUIText(player.getCurrentSpeed()+"", 3f, gauge, new Vector2f(0f, 0f), 1f, true);
	}

	public void render() {
		GaugeTextMaster.render();
	}

	public void cleanUp() {
		GaugeTextMaster.cleanUp();
	}

}