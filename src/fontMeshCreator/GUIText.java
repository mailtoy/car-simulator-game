package fontMeshCreator;

import org.lwjgl.util.vector.Vector2f;

import fontRendering.TextMaster;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText extends AbstractGUIText {

	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered) {
		super(text, fontSize, font, position, maxLineLength, centered);
		TextMaster.loadText(this);
	}

	/**
	 * Remove the text from the screen.
	 */
	public void remove() {
		TextMaster.removeText(this);
	}

}
