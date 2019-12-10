package fontMeshCreator;

import org.lwjgl.util.vector.Vector2f;

import fontRendering.GaugeTextMaster;

/**
 * Represents number of gauge in the game.
 */
public class GaugeGUIText extends AbstractGUIText {

	public GaugeGUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered) {
		super(text, fontSize, font, position, maxLineLength, centered);
		GaugeTextMaster.loadText(this);
	}

	/**
	 * Remove the text from the screen.
	 */
	public void remove() {
		GaugeTextMaster.removeText(this);
	}

}
